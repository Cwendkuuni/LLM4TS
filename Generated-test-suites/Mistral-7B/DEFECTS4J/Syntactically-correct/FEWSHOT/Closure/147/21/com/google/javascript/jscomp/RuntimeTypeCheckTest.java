package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.io.CharStreams;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.jscomp.Shim;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.FunctionType;
import com.google.javascript.jscomp.ObjectType;
import com.google.javascript.jscomp.UnionType;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;

@RunWith(JUnit4.class)
public class RuntimeTypeCheckTest {

  @Test
  public void testAddMarkers() throws IOException {
    String input = "function Foo() {}\n" +
        "function Bar() {}\n" +
        "function Baz() {}\n" +
        "function Quux() implements Foo {\n" +
        "  this.x = 1;\n" +
        "}\n" +
        "function QuuxQuux() implements Bar, Foo {\n" +
        "  this.y = 2;\n" +
        "  this.z = 3;\n" +
        "}";

    CompilerInput inputSource = new CompilerInput(new InputStreamReader(
        new ByteArrayInputStream(input.getBytes()), Charsets.UTF_8), null);
    Compiler compiler = new Compiler();

    AbstractCompiler abstractCompiler = new Compiler() {
      @Override
      public Node getNodeForCodeInsertion(Node parent) {
        return null;
      }
    };

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(abstractCompiler, null);
    Node root = compiler.parseCompilationUnit(inputSource, null);
    runtimeTypeCheck.process(null, root);

    Node functionFoo = Iterables.getFirst(
        Iterables.filter(root.getChildren(), NodeUtil::isFunction), null);
    Node constructorFoo = functionFoo.getLastChild();
    assertTrue(constructorFoo.getNext() != null);
    assertTrue(constructorFoo.getNext().getNext() != null);
    assertEquals("prototype", constructorFoo.getNext().getNext().getFirstChild().getString());
    assertEquals("instance_of__Foo", constructorFoo.getNext().getNext().getLastChild().getString());

    Node functionBar = Iterables.getFirst(
        Iterables.filter(root.getChildren(), NodeUtil::isFunction), null);
    Node constructorBar = functionBar.getLastChild();
    assertTrue(constructorBar.getNext() != null);
    assertTrue(constructorBar.getNext().getNext() != null);
    assertEquals("prototype", constructorBar.getNext().getNext().getFirstChild().getString());
    assertEquals("instance_of__Bar", constructorBar.getNext().getNext().getLastChild().getString());

    Node functionQuux = Iterables.getFirst(
        Iterables.filter(root.getChildren(), NodeUtil::isFunction), null);
    Node constructorQuux = functionQuux.getLastChild();
    assertTrue(constructorQuux.getNext() != null);
    assertTrue(constructorQuux.getNext().getNext() != null);
    assertEquals("prototype", constructorQuux.getNext().getNext().getFirstChild().getString());
    assertEquals("implements__Foo", constructorQuux.getNext().getNext().getLastChild().getString());

    Node functionQuuxQuux = Iterables.getFirst(
        Iterables.filter(root.getChildren(), NodeUtil::isFunction), null);
    Node constructorQuuxQuux = functionQuuxQuux.getLastChild();
    assertTrue(constructorQuuxQuux.getNext() != null);
    assertTrue(constructorQuuxQuux.getNext().getNext() != null);
    assertEquals("prototype", constructorQuuxQuux.getNext().getNext().getFirstChild().getString());
    assertEquals("implements__Bar", constructorQuuxQuux.getNext().getNext().getLastChild().getString());
    assertEquals("implements__Foo", constructorQuuxQuux.getNext().getNext().getLastChild().getNext().getString());
  }

  @Test
  public void testAddChecks() throws IOException {
    String input = "function Foo(x) {}\n" +
        "function Bar(y) {}\n" +
        "function FooBar(x, y) {}\n";

    CompilerInput inputSource = new CompilerInput(new InputStreamReader(
        new ByteArrayInputStream(input.getBytes()), Charsets.UTF_8), null);
    Compiler compiler = new Compiler();

    AbstractCompiler abstractCompiler = new Compiler() {
      @Override
      public Node getNodeForCodeInsertion(Node parent) {
        return null;
      }

      @Override
      public FunctionType getFunctionType(Node n, boolean allowExternal,
          boolean isTopLevel, boolean allowSuperCall, boolean isGenerator) {
        FunctionType functionType = super.getFunctionType(n, allowExternal,
            isTopLevel, allowSuperCall, isGenerator);
        if (functionType.getReturnType() == null &&
            functionType.getParameters().size() > 0) {
          functionType.setReturnType(getPrimitiveType("void"));
        }
        return functionType;
      }
    };

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(abstractCompiler, null);
    Node root = compiler.parseCompilationUnit(inputSource, null);
    runtimeTypeCheck.process(null, root);

    Node functionFoo = Iterables.getFirst(
        Iterables.filter(root.getChildren(), NodeUtil::isFunction), null);
    Node constructorFoo = functionFoo.getLastChild();
    Node blockFoo = constructorFoo.getParent();
    Node firstChildFoo = blockFoo.getFirstChild();
    Node paramXFoo = firstChildFoo.getNext();
    Node checkNodeFoo = Iterables.getFirst(
        Iterables.filter(blockFoo.getChildren(), n -> n.getToken().getType() == Token.CALL), null);
    assertNotNull(checkNodeFoo);
    assertEquals("checkType", checkNodeFoo.getFirstChild().getString());
    assertEquals("[valueChecker('null'), classChecker('Foo')]",
        checkNodeFoo.getLastChild().getString());
    assertEquals(paramXFoo, checkNodeFoo.getNext());

    Node functionBar = Iterables.getFirst(
        Iterables.filter(root.getChildren(), NodeUtil::isFunction), null);
    Node constructorBar = functionBar.getLastChild();
    Node blockBar = constructorBar.getParent();
    Node firstChildBar = blockBar.getFirstChild();
    Node paramYBar = firstChildBar.getNext();
    Node checkNodeBar = Iterables.getFirst(
        Iterables.filter(blockBar.getChildren(), n -> n.getToken().getType() == Token.CALL), null);
    assertNotNull(checkNodeBar);
    assertEquals("checkType", checkNodeBar.getFirstChild().getString());
    assertEquals("[valueChecker('null'), classChecker('Bar')]",
        checkNodeBar.getLastChild().getString());
    assertEquals(paramYBar, checkNodeBar.getNext());

    Node functionFooBar = Iterables.getFirst(
        Iterables.filter(root.getChildren(), NodeUtil::isFunction), null);
    Node constructorFooBar = functionFooBar.getLastChild();
    Node blockFooBar = constructorFooBar.getParent();
    Node firstChildFooBar = blockFooBar.getFirstChild();
    Node paramxFooBar = firstChildFooBar.getNext();
    Node paramYFooBar = firstChildFooBar.getNext().getNext();
    Node checkNodeFooBar = Iterables.getFirst(
        Iterables.filter(blockFooBar.getChildren(), n -> n.getToken().getType() == Token.CALL), null);
    assertNotNull(checkNodeFooBar);
    assertEquals("checkType", checkNodeFooBar.getFirstChild().getString());
    assertEquals("[valueChecker('null'), classChecker('Foo'), classChecker('Bar')]",
        checkNodeFooBar.getLastChild().getString());
    assertEquals(paramxFooBar, checkNodeFooBar.getNext().getNext());
    assertEquals(paramYFooBar, checkNodeFooBar.getNext().getNext().getNext());
  }

  private void assertEquals(Node expected, Node actual) {
    assertEquals(expected.toString(), actual.toString());
  }

  private void assertNotNull(Node node) {
    assertNotNull(node.getFirstChild());
  }

  private Node getPrimitiveType(String typeName) {
    return new Node(
        Token.CALL,
        NodeUtil.newQualifiedNameNode("jscomp.typecheck.valueChecker", -1, -1),
        Node.newString(typeName)
    );
  }

  @Test
  public void testNoAddChecks() throws IOException {
    String input = "function Foo() {}\n" +
        "function Foo(x) {}\n" +
        "function Bar() {}\n";

    CompilerInput inputSource = new CompilerInput(new InputStreamReader(
        new ByteArrayInputStream(input.getBytes()), Charsets.UTF_8), null);
    Compiler compiler = new Compiler();

    AbstractCompiler abstractCompiler = new Compiler() {
      @Override
      public Node getNodeForCodeInsertion(Node parent) {
        return null;
      }

      @Override
      public FunctionType getFunctionType(Node n, boolean allowExternal,
          boolean isTopLevel, boolean allowSuperCall, boolean isGenerator) {
        FunctionType functionType = super.getFunctionType(n, allowExternal,
            isTopLevel, allowSuperCall, isGenerator);
        if (functionType.getReturnType() == null) {
          functionType.setReturnType(getPrimitiveType("void"));
        }
        return functionType;
      }
    };

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(abstractCompiler, null);
    Node root = compiler.parseCompilationUnit(inputSource, null);
    runtimeTypeCheck.process(null, root);

    Node functionFoo = Iterables.getFirst(
        Iterables.filter(root.getChildren(), NodeUtil::isFunction), null);
    Node constructorFoo = functionFoo.getLastChild();
    if (constructorFoo.getNext() != null) {
      fail();
    }

    Node functionFooWithParam = Iterables.getFirst(
        Iterables.filter(root.getChildren(), NodeUtil::isFunction), null);
    Node constructorFooWithParam = functionFooWithParam.getLastChild();
    if (constructorFooWithParam.getNext() != null) {
      fail();
    }

    Node functionBar = Iterables.getFirst(
        Iterables.filter(root.getChildren(), NodeUtil::isFunction), null);
    Node constructorBar = functionBar.getLastChild();
    if (constructorBar.getNext() != null) {
      fail();
    }
  }
}