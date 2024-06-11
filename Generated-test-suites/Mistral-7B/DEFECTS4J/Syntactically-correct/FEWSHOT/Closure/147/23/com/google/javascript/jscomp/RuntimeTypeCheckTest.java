package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.CharStreams;
import com.google.javascript.jscomp.compiler.AbstractCompiler;
import com.google.javascript.jscomp.compiler.CompilerPass;
import com.google.javascript.jscomp.compiler.CompilerPassManager;
import com.google.javascript.jscomp.compiler.Node;
import com.google.javascript.jscomp.compiler.NodeTraversal;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.jstype.JSType;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Test class for RuntimeTypeCheck.
 */
@RunWith(JUnit4.class)
public class RuntimeTypeCheckTest {

  @Test
  public void testAddChecks() throws IOException {
    String inputCode = "function Foo() {}";
    String outputCode = "function Foo() {\n" +
            "  checkType(arguments[0], [valueChecker(\"Object\"), classChecker(\"Foo\")]);\n" +
            "  // constructor body\n" +
            "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerInput input = new CompilerInput(inputCode, "test.js");
    Node root = compiler.parse(input, null);

    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new RuntimeTypeCheck(compiler, null));
    passManager.runPasses(root, null);

    Node functionNode = NodeUtil.getFunctionByName(root, "Foo");
    assertNotNull(functionNode);
    assertNotNull(functionNode.getLastChild());
    assertEquals(outputCode, NodeUtil.getSource(functionNode.getLastChild()));

    // Test for interface implementation check.
    String interfaceInputCode =
        "function Bar() {\n" +
        "  this.implements__Foo = true;\n" +
        "}";
    AbstractCompiler interfaceCompiler = new AbstractCompiler();
    CompilerInput interfaceInput = new CompilerInput(interfaceInputCode, "interface.js");
    Node interfaceRoot = interfaceCompiler.parse(interfaceInput, null);
    Node interfaceFunctionNode = NodeUtil.getFunctionByName(interfaceRoot, "Bar");
    assertNotNull(interfaceFunctionNode);
    assertNotNull(interfaceFunctionNode.getPrototype());
    assertNotNull(interfaceFunctionNode.getPrototype().getProperty("implements__Foo"));
  }

  @Test
  public void testAddMarkers() throws IOException {
    String inputCode = "function Foo() {\n" +
        "  function Bar() {\n" +
        "    this.b = 1;\n" +
        "  }\n" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerInput input = new CompilerInput(inputCode, "test.js");
    Node root = compiler.parse(input, null);

    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new RuntimeTypeCheck(compiler, null));
    passManager.runPasses(root, null);

    Node constructorNode = NodeUtil.getFunctionByName(root, "Foo");
    Node markerNode = null;
    for (Node node = constructorNode.getFirstChild(); node != null;
        node = node.getNext()) {
      if (node.getType() == Token.BLOCK) {
        for (Node child = node.getFirstChild(); child != null;
            child = child.getNext()) {
          if (child.getType() == Token.GETPROP
              && child.getString().equals("prototype")) {
            for (Node propertyNode = child.getFirstChild();
                propertyNode != null;
                propertyNode = propertyNode.getNext()) {
              if (propertyNode.getType() == Token.IDENT
                  && propertyNode.getString().equals("instance_of__Foo")) {
                markerNode = propertyNode;
                break;
              }
            }
          }
        }
      }
    }
    assertNotNull(markerNode);
  }

  @Test
  public void testCreateCheckerNode() {
    JSType stringType = JSType.STRING;
    JSType fooType = new ObjectType("Foo");
    JSType nullType = JSType.NULL;

    Function<JSType, Node> checkerNodeCreator =
        new Function<JSType, Node>() {
          @Override
          public Node apply(JSType input) {
            return createCheckerNode(input);
          }
        };

    Node stringChecker = checkerNodeCreator.apply(stringType);
    assertNotNull(stringChecker);
    assertNotNull(stringChecker.getString());
    assertEquals("valueChecker(\"String\")", stringChecker.getString());

    Node fooChecker = checkerNodeCreator.apply(fooType);
    assertNotNull(fooChecker);
    assertNotNull(fooChecker.getString());
    assertEquals("classChecker(\"Foo\")", fooChecker.getString());

    Node nullChecker = checkerNodeCreator.apply(nullType);
    assertNull(nullChecker);
  }

  @Test
  public void testCreateCheckTypeCallNode() {
    JSType stringType = JSType.STRING;
    JSType fooType = new ObjectType("Foo");

    Function<JSType, Node> checkTypeCallNodeCreator =
        new Function<JSType, Node>() {
          @Override
          public Node apply(JSType input) {
            return createCheckTypeCallNode(input, Node.newNull());
          }
        };

    Node stringChecker = checkTypeCallNodeCreator.apply(stringType);
    assertNotNull(stringChecker);
    Node stringExpression = Node.newString("foo");
    Node stringCheckerCall = checkTypeCallNodeCreator.apply(stringType).apply(stringExpression);
    assertNotNull(stringCheckerCall);
    assertEquals(
        "checkType(foo, [valueChecker(\"String\"), null])",
        stringCheckerCall.toString());

    Node fooChecker = checkTypeCallNodeCreator.apply(fooType);
    assertNotNull(fooChecker);
    Node fooExpression = NodeUtil.newQualifiedNameNode("Foo", -1, -1);
    Node fooCheckerCall = checkTypeCallNodeCreator.apply(fooType).apply(fooExpression);
    assertNotNull(fooCheckerCall);
    assertEquals(
        "checkType(Foo, [null, classChecker(\"Foo\")])",
        fooCheckerCall.toString());
  }

  @Test
  public void testChecksForUnionTypes() {
    String inputCode = "function Foo() {}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerInput input = new CompilerInput(inputCode, "test.js");
    Node root = compiler.parse(input, null);

    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new RuntimeTypeCheck(compiler, null));
    passManager.runPasses(root, null);

    FunctionType fooType = (FunctionType) NodeUtil.getFunctionByName(root, "Foo")
        .getJSType();
    UnionType unionType = (UnionType) fooType.getReturnType();
    JSType firstAlternate = unionType.getAlternates().get(0);
    Node firstChecker = createCheckerNode(firstAlternate);
    assertNotNull(firstChecker);

    JSType secondAlternate = unionType.getAlternates().get(1);
    Node secondChecker = createCheckerNode(secondAlternate);
    assertNotNull(secondChecker);

    Node rootWithCheckers = new Node(Token.SCRIPT, null, null);
    rootWithCheckers.addChildrenToBack(root.getChildren());
    rootWithCheckers.addChildToBack(firstChecker);
    rootWithCheckers.addChildToBack(secondChecker);

    String outputCode = "function Foo() {\n" +
        "  checkType(arguments[0], [valueChecker(\"Object\"), classChecker(\"Foo\")]);\n" +
        "  // constructor body\n" +
        "}\n" +
        "checkType(undefined, [valueChecker(\"Object\"), classChecker(\"Foo\")]);\n" +
        "checkType(null, [valueChecker(\"Object\"), classChecker(\"Foo\")]);\n" +
        "checkType(1, [valueChecker(\"Number\"), classChecker(\"Foo\")]);\n" +
        "checkType(\"foo\", [valueChecker(\"String\"), classChecker(\"Foo\")]);\n";

    assertEquals(outputCode, NodeUtil.getSource(rootWithCheckers));
  }

  private Node createCheckerNode(JSType type) {
    return new RuntimeTypeCheck(null, null).createCheckerNode(type);
  }

  private Node createCheckTypeCallNode(JSType type, Node expr) {
    return new RuntimeTypeCheck(null, null).createCheckTypeCallNode(type, expr);
  }
}