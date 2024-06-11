package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.util.SourceFile;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    SourceFile sourceFile = new SourceFile("Test.js", "test");
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traverser = new NodeTraversal(checker, sourceFile, null);

    Node functionNode = Node.newFunction("Test", null, null, null, null, null, null, null);
    Node assignNode = Node.newAssign(Node.newName("x"), Node.newThis());

    traverser.traverse(NodeUtil.makeBlock(functionNode, assignNode));

    assertThat(compiler.getDiagnostics()).hasSize(1);
    DiagnosticType diagnosticType = compiler.getDiagnostics().get(0).getType();
    assertThat(diagnosticType).isEqualTo(CheckGlobalThis.GLOBAL_THIS);
  }

  @Test
  public void testCheckGlobalThisPrototype() {
    SourceFile sourceFile = new SourceFile("Test.js", "test");
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traverser = new NodeTraversal(checker, sourceFile, null);

    Node functionNode = Node.newFunction("Test", null, null, null, null, null, null, null);
    Node prototypeNode = Node.newName("prototype");
    Node getNode = Node.newGetProp(Node.newName("Test"), prototypeNode);
    Node assignNode = Node.newAssign(getNode, Node.newThis());

    traverser.traverse(NodeUtil.makeBlock(functionNode, assignNode));

    assertThat(compiler.getDiagnostics()).hasSize(1);
    DiagnosticType diagnosticType = compiler.getDiagnostics().get(0).getType();
    assertThat(diagnosticType).isEqualTo(CheckGlobalThis.GLOBAL_THIS);
  }

  @Test
  public void testCheckGlobalThisFunctionCall() {
    SourceFile sourceFile = new SourceFile("Test.js", "test");
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traverser = new NodeTraversal(checker, sourceFile, null);

    Node functionNode = Node.newFunction("Test", null, null, null, null, null, null, null);
    Node callNode = Node.newCall(Node.newName("Test"), null, null, null);

    traverser.traverse(NodeUtil.makeBlock(functionNode, callNode));

    assertNull(compiler.getDiagnostics());
  }

  @Test
  public void testCheckGlobalThisConstructor() {
    SourceFile sourceFile = new SourceFile("Test.js", "test");
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traverser = new NodeTraversal(checker, sourceFile, null);

    Node constructorNode = Node.newFunction("Test", null, null, CheckLevel.ERROR, null, null, null, null);
    Node constructorThisNode = Node.newThis(constructorNode);

    traverser.traverse(constructorThisNode);

    assertNull(compiler.getDiagnostics());
  }

  @Test
  public void testCheckGlobalThisAnnotatedFunction() {
    SourceFile sourceFile = new SourceFile("Test.js", "test");
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traverser = new NodeTraversal(checker, sourceFile, null);

    Node functionNode = Node.newFunction("Test", null, null, null, null, null, "@this Test", null);
    Node assignNode = Node.newAssign(Node.newName("x"), Node.newThis());

    traverser.traverse(NodeUtil.makeBlock(functionNode, assignNode));

    assertNull(compiler.getDiagnostics());
  }

  @Test
  public void testCheckGlobalThisPrototypeFunction() {
    SourceFile sourceFile = new SourceFile("Test.js", "test");
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traverser = new NodeTraversal(checker, sourceFile, null);

    Node prototypeNode = Node.newName("prototype");
    Node functionNode = Node.newFunction("Test", null, null, null, null, null, null, null);
    Node prototypeFunctionNode = Node.newFunction("", null, null, null, null, null, null, prototypeNode);
    Node getPrototypeNode = Node.newGetProp(Node.newName("Test"), prototypeNode);
    Node getPrototypeFunctionNode = Node.newGetProp(getPrototypeNode, "Test");

    traverser.traverse(NodeUtil.makeBlock(functionNode, prototypeFunctionNode, getPrototypeNode, getPrototypeFunctionNode));

    assertNull(compiler.getDiagnostics());
  }

  @Test
  public void testCheckGlobalThisAnnotatedPrototypeFunction() {
    SourceFile sourceFile = new SourceFile("Test.js", "test");
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal traverser = new NodeTraversal(checker, sourceFile, null);

    Node prototypeNode = Node.newName("prototype");
    Node functionNode = Node.newFunction("Test", null, null, null, null, null, null, prototypeNode);
    Node prototypeFunctionNode = Node.newFunction("", null, null, null, null, null, "@this Test", null);
    Node getPrototypeNode = Node.newGetProp(Node.newName("Test"), prototypeNode);
    Node getPrototypeFunctionNode = Node.newGetProp(getPrototypeNode, "Test");

    traverser.traverse(NodeUtil.makeBlock(functionNode, prototypeFunctionNode, getPrototypeNode, getPrototypeFunctionNode));

    assertNull(compiler.getDiagnostics());
  }
}