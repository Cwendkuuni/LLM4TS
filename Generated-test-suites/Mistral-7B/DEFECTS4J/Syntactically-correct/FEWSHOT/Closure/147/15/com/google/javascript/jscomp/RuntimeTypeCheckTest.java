package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import org.junit.Test;

public class RuntimeTypeCheckTest {

  @Test
  public void testAddMarkers() {
    AbstractCompiler compiler = new Compiler();
    String logFunction = "function(warning, expr) { print(warning + ': ' + expr); }";
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, logFunction);

    String sourceCode = "function Foo() {}";
    Node root = NodeUtil.parse(sourceCode);
    runtimeTypeCheck.process(null, root);

    Node fooPrototype = NodeUtil.getPrototype(NodeUtil.getFunctionName(sourceCode), root);
    assertNotNull(fooPrototype);

    Node instanceOfChecker = fooPrototype.getChildByName("instance_of__Foo");
    assertNotNull(instanceOfChecker);

    Node implementsChecker = null;
    for (Node child : fooPrototype.getChildren()) {
      if (child.getType() == Token.GETPROP && child.getChild(0).getString().equals("implements")) {
        implementsChecker = child;
        break;
      }
    }
    assertNotNull(implementsChecker);

    // Test for an interface implementation
    sourceCode = "function Bar() {} function BarImpl() implements Bar {}";
    root = NodeUtil.parse(sourceCode);
    runtimeTypeCheck.process(null, root);

    Node barPrototype = NodeUtil.getPrototype(NodeUtil.getFunctionName("Bar"), root);
    for (Node child : barPrototype.getChildren()) {
      if (child.getType() == Token.GETPROP && child.getChild(0).getString().equals("implements")) {
        TreeSet<Node> children = Sets.newTreeSet(new Comparator<Node>() {
          @Override
          public int compare(Node o1, Node o2) {
            return NodeUtil.getFunctionName(o1.getFirstChild().getString()).compareTo(
                NodeUtil.getFunctionName(o2.getFirstChild().getString()));
          }
        });
        children.addAll(Iterables.asList(child.getChildren()));
        Node barImplPrototype = Iterables.get(children, 0).getParent();
        assertNotNull(barImplPrototype);
        Node implementsCheckerImpl = barImplPrototype.getChildByName("implements__Bar");
        assertNotNull(implementsCheckerImpl);
      }
    }
  }

  @Test
  public void testAddChecks() {
    AbstractCompiler compiler = new Compiler();
    String logFunction = "function(warning, expr) { print(warning + ': ' + expr); }";
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, logFunction);

    String sourceCode = "function Foo(x) { return x; }";
    Node root = NodeUtil.parse(sourceCode);
    runtimeTypeCheck.process(null, root);

    Node constructor = NodeUtil.getFunctionByName("Foo", root);
    Node checkNode = null;
    for (Node child : constructor.getLastChild().getChildren()) {
      if (child.getType() == Token.CALL && child.getFirstChild().toString().equals("checkType")) {
        checkNode = child;
        break;
      }
    }
    assertNotNull(checkNode);

    Node x = constructor.getChildByName("x");
    checkNode = (Node) checkNode.getChild(1);
    Node paramType = checkNode.getFirstChild();
    assertNotNull(paramType);
    assertTrue(paramType.getType() == Token.STRING);
    assertTrue(x.getType() == Token.VAR);
    assertFalse(x.getJSType().isOptional());

    sourceCode = "function Foo(x) { return x || 42; }";
    root = NodeUtil.parse(sourceCode);
    runtimeTypeCheck.process(null, root);

    checkNode = null;
    for (Node child : constructor.getLastChild().getChildren()) {
      if (child.getType() == Token.CALL && child.getFirstChild().toString().equals("checkType")) {
        checkNode = child;
        break;
      }
    }
    assertNotNull(checkNode);

    paramType = checkNode.getFirstChild();
    assertNotNull(paramType);
    assertTrue(paramType.getType() == Token.OR);
    assertTrue(x.getType() == Token.VAR);
    assertTrue(x.getJSType().isOptional());

    sourceCode = "function Foo(x = 42) { return x; }";
    root = NodeUtil.parse(sourceCode);
    runtimeTypeCheck.process(null, root);

    checkNode = null;
    for (Node child : constructor.getLastChild().getChildren()) {
      if (child.getType() == Token.CALL && child.getFirstChild().toString().equals("checkType")) {
        checkNode = child;
        break;
      }
    }
    assertNotNull(checkNode);

    paramType = checkNode.getFirstChild();
    assertNotNull(paramType);
    assertTrue(paramType.getType() == Token.VAR);
    assertTrue(x.getType() == Token.VAR);
    assertTrue(x.getJSType().isOptional());

    sourceCode = "function Foo(x: number) { return x; }";
    root = NodeUtil.parse(sourceCode);
    runtimeTypeCheck.process(null, root);

    checkNode = null;
    for (Node child : constructor.getLastChild().getChildren()) {
      if (child.getType() == Token.CALL && child.getFirstChild().toString().equals("checkType")) {
        checkNode = child;
        break;
      }
    }
    assertNotNull(checkNode);

    paramType = checkNode.getFirstChild();
    assertNotNull(paramType);
    assertTrue(paramType.getType() == Token.IDENT);
    assertTrue(paramType.getString().equals("x"));
    assertTrue(x.getType() == Token.VAR);
    assertFalse(x.getJSType().isOptional());

    sourceCode = "function Foo(x: number) { return x || 42; }";
    root = NodeUtil.parse(sourceCode);
    runtimeTypeCheck.process(null, root);

    checkNode = null;
    for (Node child : constructor.getLastChild().getChildren()) {
      if (child.getType() == Token.CALL && child.getFirstChild().toString().equals("checkType")) {
        checkNode = child;
        break;
      }
    }
    assertNotNull(checkNode);

    paramType = checkNode.getFirstChild();
    assertNotNull(paramType);
    assertTrue(paramType.getType() == Token.IDENT);
    assertTrue(paramType.getString().equals("x"));
    assertTrue(x.getType() == Token.VAR);
    assertTrue(x.getJSType().isOptional());

    sourceCode = "function Foo(x: number = 42) { return x; }";
    root = NodeUtil.parse(sourceCode);
    runtimeTypeCheck.process(null, root);

    checkNode = null;
    for (Node child : constructor.getLastChild().getChildren()) {
      if (child.getType() == Token.CALL && child.getFirstChild().toString().equals("checkType")) {
        checkNode = child;
        break;
      }
    }
    assertNotNull(checkNode);

    paramType = checkNode.getFirstChild();
    assertNotNull(paramType);
    assertTrue(paramType.getType() == Token.VAR);
    assertTrue(x.getType() == Token.VAR);
    assertTrue(x.getJSType().isOptional());

    sourceCode = "function Foo(x: number) { function Bar() {} return new Bar(); }";
    root = NodeUtil.parse(sourceCode);
    runtimeTypeCheck.process(null, root);

    checkNode = null;
    for (Node child : constructor.getLastChild().getChildren()) {
      if (child.getType() == Token.CALL && child.getFirstChild().toString().equals("checkType")) {
        checkNode = child;
        break;
      }
    }
    assertNotNull(checkNode);

    paramType = checkNode.getFirstChild();
    assertNotNull(paramType);
    assertTrue(paramType.getType() == Token.CALL);
    assertTrue(paramType.getLastChild().getType() == Token.IDENT);
    assertTrue(paramType.getLastChild().getString().equals("Bar"));

    Node barConstructor = NodeUtil.getFunctionByName("Bar", root);
    assertNotNull(barConstructor);
    assertNull(barConstructor.getPrototype());

    checkNode = null;
    for (Node child : constructor.getLastChild().getChildren()) {
      if (child.getType() == Token.CALL && child.getFirstChild().toString().equals("checkType")) {
        checkNode = child;
        break;
      }
    }
    assertNotNull(checkNode);

    paramType = checkNode.getFirstChild();
    assertNotNull(paramType);
    assertTrue(paramType.getType() == Token.GETPROPERTY);
    assertTrue(paramType.getLastChild().getType() == Token.IDENT);
    assertTrue(paramType.getLastChild().getString().equals("prototype"));

    Node barPrototype = NodeUtil.getPrototype(NodeUtil.getFunctionName("Bar"), root);
    assertNotNull(barPrototype);
    assertNull(barPrototype.getPrototype());

    checkNode = null;
    for (Node child : constructor.getLastChild().getChildren()) {
      if (child.getType() == Token.CALL && child.getFirstChild().toString().equals("checkType")) {
        checkNode = child;
        break;
      }
    }
    assertNotNull(checkNode);

    paramType = checkNode.getFirstChild();
    assertNotNull(paramType);
    assertTrue(paramType.getType() == Token.GETPROPERTY);
    assertTrue(paramType.getLastChild().getType() == Token.IDENT);
    assertTrue(paramType.getLastChild().getString().equals("implements"));

    sourceCode = "interface Bar { foo(); } function Foo() implements Bar {}";
    root = NodeUtil.parse(sourceCode);
    runtimeTypeCheck.process(null, root);

    checkNode = null;
    for (Node child : constructor.getLastChild().getChildren()) {
      if (child.getType() == Token.CALL && child.getFirstChild().toString().equals("checkType")) {
        checkNode = child;
        break;
      }
    }
    assertNotNull(checkNode);

    paramType = checkNode.getFirstChild();
    assertNotNull(paramType);
    assertTrue(paramType.getType() == Token.CALL);
    assertTrue(paramType.getLastChild().getType() == Token.IDENT);
    assertTrue(paramType.getLastChild().getString().equals("interfaceChecker"));
    assertNotNull(paramType.getFirstChild().getFirstChild());
    assertTrue(paramType.getFirstChild().getFirstChild().getType() == Token.STRING);
    assertTrue(paramType.getFirstChild().getFirstChild().getString().equals("Bar"));
  }
}