import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RuntimeTypeCheck.*;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.rhino.Node;

@RunWith(JUnit4.class)
public class RuntimeTypeCheckTest {

  @Test
  public void testAddMarkers() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    passManager.addPass(new RuntimeTypeCheck(compiler, null));

    String input = "function Foo() {}";
    Node externs = Node.newText(input);
    Node root = Node.newText(input);

    passManager.process(externs, root, options);

    Node fooConstructor = NodeUtil.getFunctionFromNode(root, "Foo");
    Node marker = findMarkerNode(fooConstructor, "instance_of__Foo");
    assertNotNull(marker);
  }

  @Test
  public void testAddMarkersForInterface() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    passManager.addPass(new RuntimeTypeCheck(compiler, null));

    String input = "interface I {} function Foo() implements I {}";
    Node externs = Node.newText(input);
    Node root = Node.newText(input);

    passManager.process(externs, root, options);

    Node fooConstructor = NodeUtil.getFunctionFromNode(root, "Foo");
    Node marker = findMarkerNode(fooConstructor, "implements__I");
    assertNotNull(marker);
  }

  @Test
  public void testAddChecks() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    passManager.addPass(new RuntimeTypeCheck(compiler, null));

    String input = "function Foo() {return 'hello';}";
    Node externs = Node.newText(input);
    Node root = Node.newText(input);

    passManager.process(externs, root, options);

    Node fooFunction = NodeUtil.getFunctionFromNode(root, "Foo");
    Node checkTypeCall = findCheckTypeCallNode(fooFunction);
    assertNotNull(checkTypeCall);
  }

  @Test
  public void testAddChecksForReturnType() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    passManager.addPass(new RuntimeTypeCheck(compiler, null));

    String input = "function Foo() {return 1;}";
    Node externs = Node.newText(input);
    Node root = Node.newText(input);

    passManager.process(externs, root, options);

    Node fooFunction = NodeUtil.getFunctionFromNode(root, "Foo");
    Node checkTypeCall = findCheckTypeCallNode(fooFunction.getLastChild());
    assertNotNull(checkTypeCall);
  }

  @Test
  public void testAddChecksForFunctionParameter() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    passManager.addPass(new RuntimeTypeCheck(compiler, null));

    String input = "function Foo(x) {}";
    Node externs = Node.newText(input);
    Node root = Node.newText(input);

    passManager.process(externs, root, options);

    Node fooFunction = NodeUtil.getFunctionFromNode(root, "Foo");
    Node paramNode = findParameterNode(fooFunction, 0);
    Node checkTypeCall = findCheckTypeCallNode(paramNode);
    assertNotNull(checkTypeCall);
  }

  @Test
  public void testAddChecksForClassParameter() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    passManager.addPass(new RuntimeTypeCheck(compiler, null));

    String input = "class Foo {constructor(x) {}}";
    Node externs = Node.newText(input);
    Node root = Node.newText(input);

    passManager.process(externs, root, options);

    Node fooConstructor = NodeUtil.getConstructorFromNode(root, "Foo");
    Node paramNode = findParameterNode(fooConstructor, 0);
    Node checkTypeCall = findCheckTypeCallNode(paramNode);
    assertNotNull(checkTypeCall);
  }

  @Test
  public void testAddChecksForInterfaceImplementation() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    passManager.addPass(new RuntimeTypeCheck(compiler, null));

    String input = "interface I {" +
        "methodA(x: number): void;" +
        "methodB(y: string): void;" +
        "}" +
        "class Foo implements I {" +
        "constructor() {}" +
        "methodA(x: number) {}" +
        "methodB(y: string) {}" +
        "}";
    Node externs = Node.newText(input);
    Node root = Node.newText(input);

    passManager.process(externs, root, options);

    Node fooConstructor = NodeUtil.getConstructorFromNode(root, "Foo");
    Node methodANode = NodeUtil.getMethodFromNode(fooConstructor, "methodA");
    Node methodBNode = NodeUtil.getMethodFromNode(fooConstructor, "methodB");

    Node methodACheckTypeCall = findCheckTypeCallNode(methodANode.getParameter(0));
    Node methodBCheckTypeCall = findCheckTypeCallNode(methodBNode.getParameter(0));

    assertNotNull(methodACheckTypeCall);
    assertNotNull(methodBCheckTypeCall);

    Node methodAValueCheckerCall = findValueCheckerCallNode(methodACheckTypeCall);
    Node methodBValueCheckerCall = findValueCheckerCallNode(methodBCheckTypeCall);

    assertNotNull(methodAValueCheckerCall);
    assertNotNull(methodBValueCheckerCall);

    assertEquals("number", methodAValueCheckerCall.getFirstChild().getString());
    assertEquals("string", methodBValueCheckerCall.getFirstChild().getString());
  }

  @Test
  public void testAddChecksForUnionType() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);

    passManager.addPass(new RuntimeTypeCheck(compiler, null));

    String input = "interface I {}" +
        "class Foo {" +
        "  constructor(x: number | string) {}" +
        "}";
    Node externs = Node.newText(input);
    Node root = Node.newText(input);

    passManager.process(externs, root, options);

    Node fooConstructor = NodeUtil.getConstructorFromNode(root, "Foo");
    Node paramNode = fooConstructor.getParameter(0);

    Node checkTypeCall = findCheckTypeCallNode(paramNode);
    Node unionTypeCheckerCall = findUnionTypeCheckerCallNode(checkTypeCall);

    assertNotNull(checkTypeCall);
    assertNotNull(unionTypeCheckerCall);

    Node numberCheckerCall = findCheckerCallNode(unionTypeCheckerCall, 0);
    Node stringCheckerCall = findCheckerCallNode(unionTypeCheckerCall, 1);

    assertNotNull(numberCheckerCall);
    assertNotNull(stringCheckerCall);

    assertEquals("number", numberCheckerCall.getFirstChild().getString());
    assertEquals("string", stringCheckerCall.getFirstChild().getString());
  }

  private Node findMarkerNode(Node node, String markerName) {
    for (Node child = node; child != null; child = child.getNext()) {
      if (child.getType() == Token.PROPERTY
          && markerName.equals(child.getFirstChild().getString())) {
        return child;
      }
    }
    return null;
  }

  private Node findParameterNode(Node node, int index) {
    for (Node child = node; child != null; child = child.getNext()) {
      if (child.getType() == Token.LIST
          && child.getFirstChild().getType() == Token.FN
          && child.getFirstChild().getParameterCount() > index) {
        return child.getFirstChild().getParameter(index);
      }
    }
    return null;
  }

  private Node findCheckTypeCallNode(Node node) {
    for (Node child = node; child != null; child = child.getNext()) {
      if (child instanceof Node && child.getType() == Token.CALL
          && jsCode("checkType").equals(child.getFirstChild())) {
        return (Node) child;
      }
    }
    return null;
  }

  private Node findValueCheckerCallNode(Node checkTypeCallNode) {
    Node checkTypeCall = findCheckTypeCallNode(checkTypeCallNode);
    for (Node child = checkTypeCall; child != null; child = child.getNext()) {
      if (child instanceof Node && child.getType() == Token.CALL
          && jsCode("valueChecker").equals(child.getFirstChild())) {
        return (Node) child;
      }
    }
    return null;
  }

  private Node findUnionTypeCheckerCallNode(Node checkTypeCallNode) {
    Node checkTypeCall = findCheckTypeCallNode(checkTypeCallNode);
    for (Node child = checkTypeCall; child != null; child = child.getNext()) {
      if (child instanceof Node && child.getType() == Token.CALL
          && jsCode("unionTypeChecker").equals(child.getFirstChild())) {
        return (Node) child;
      }
    }
    return null;
  }

  private Node findCheckerCallNode(Node unionTypeCheckerCallNode, int index) {
    Node unionTypeCheckerCall = findUnionTypeCheckerCallNode(unionTypeCheckerCallNode);
    int listSize = Arrays.stream(unionTypeCheckerCall.getChildren())
        .mapToInt(Node::getChildCount).sum();
    for (int i = 0; i < listSize; i++) {
      Node child = unionTypeCheckerCall.getChild(i);
      if (child instanceof Node && child.getType() == Token.CALL
          && (i == index || i == listSize - 1)) {
        return (Node) child;
      }
    }
    return null;
  }

  private static Node jsCode(String prop) {
    return NodeUtil.newQualifiedNameNode("jscomp.typecheck." + prop, -1, -1);
  }
}