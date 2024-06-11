package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.NodeTraversal.ScopedCallback;
import com.google.javascript.jscomp.Renamer;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.TokenStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;

/**
 * Tests for the MakeDeclaredNamesUnique class.
 */
public class MakeDeclaredNamesUniqueTest {
  private Compiler compiler;
  private TokenStream tokenStream;
  private Scope scope;
  private AbstractPostOrderCallback callback;

  @Before
  public void setUp() {
    compiler = mock(Compiler.class);
    tokenStream = mock(TokenStream.class);
    scope = mock(Scope.class);
    when(scope.getVar(Eq("foo"))).thenReturn(new Var(Eq("foo"), null, null, null, null));
    when(scope.getVar(Eq("bar"))).thenReturn(null);
    when(compiler.getTokenStream()).thenReturn(tokenStream);
    when(scope.isDeclared(Eq("foo"), Boolean.FALSE)).thenReturn(true);
    when(scope.isDeclared(Eq("baz"), Boolean.FALSE)).thenReturn(false);
    when(scope.getRootNode()).thenReturn(createTestNode("function foo() {}"));

    callback = new MakeDeclaredNamesUnique();
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    makeDeclaredNamesUnique.enterScope(new NodeTraversal(compiler, scope));
    callback = makeDeclaredNamesUnique;
  }

  @Test
  public void testUniqueNamesWithNoConflict() {
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, scope);
    nodeTraversal.traverse(tokenStream.getRootNode());

    ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

    // Check that names were not modified for global variables.
    Var foo = scope.getVar("foo");
    assertThat(foo.getNameNode().getString()).isEqualTo("foo");

    // Check that new unique names were added for local variables.
    createTestNode("function test() { var foo = 1; }");
    nodeTraversal.traverse(tokenStream.getRootNode());
    var fooVar = scope.getVar("foo");
    assertThat(fooVar.getNameNode().getString()).isNotEmpty();
    assertThat(fooVar.getNameNode().getString()).doesNotMatch("foo$\\d+");

    // Check that new unique names were added for function names.
    createTestNode("function foo() {} function test() { var bar = foo; }")
        .traverse(tokenStream);
    nodeTraversal.traverse(tokenStream.getRootNode());
    Var fooVar = scope.getVar("foo");
    Var barVar = scope.getVar("bar");
    assertThat(fooVar.getNameNode().getString()).isNotEmpty();
    assertThat(barVar.getNameNode().getString()).isNotEmpty();
    assertThat(barVar.getNameNode().getString()).doesNotMatch("foo$\\d+");

    // Check that new unique names were added for catch exceptions.
    createTestNode("try { throw new Error('test'); } catch (e) { }")
        .traverse(tokenStream);
    nodeTraversal.traverse(tokenStream.getRootNode());
    Var catchVar = scope.getVar("e");
    assertThat(catchVar.getNameNode().getString()).isNotEmpty();
    assertThat(catchVar.getNameNode().getString()).doesNotMatch("e$\\d+");
  }

  @Test
  public void testUniqueNamesWithConflict() {
    createTestNode("function foo() { var foo = 1; } function test() { var foo = 2; }")
        .traverse(tokenStream);
    nodeTraversal.traverse(tokenStream.getRootNode());
    Var fooVar1 = scope.getVar("foo");
    Var fooVar2 = scope.getVar("foo");
    assertThat(fooVar1.getNameNode().getString()).isNotEmpty();
    assertThat(fooVar1.getNameNode().getString()).doesNotMatch("foo$\\d+");
    assertThat(fooVar2.getNameNode().getString()).isNotEmpty();
    assertThat(fooVar2.getNameNode().getString()).isEqualTo(fooVar1.getNameNode().getString());
  }

  @Test
  public void testUniqueNamesWithCycle() {
    // This test case will create a cycle in the renaming process and
    // should fail with an error.
    createTestNode("function foo() { var bar = foo; } function test() { var foo = bar; }")
        .traverse(tokenStream);
    nodeTraversal.traverse(tokenStream.getRootNode());
    assertThat(scope.getRootScope().getVar("foo")).isNull();
  }

  @Test
  public void testUniqueNamesWithExistingGlobal() {
    createTestNode("var foo = 1; function test() { var bar = foo; }")
        .traverse(tokenStream);
    nodeTraversal.traverse(tokenStream.getRootNode());
    Var fooVar = scope.getVar("foo");
    Var barVar = scope.getVar("bar");
    assertThat(fooVar.getNameNode().getString()).isEqualTo("foo");
    assertThat(barVar.getNameNode().getString()).isNotEmpty();
    assertThat(barVar.getNameNode().getString()).doesNotMatch("foo$\\d+");
  }

  @Test
  public void testUniqueNamesWithExistingLocal() {
    createTestNode("function foo() { var foo = 1; function test() { var bar = foo; } }")
        .traverse(tokenStream);
    nodeTraversal.traverse(tokenStream.getRootNode());
    Var fooVar = scope.getVar("foo");
    Var barVar = scope.getVar("bar");
    assertThat(fooVar.getNameNode().getString()).isEqualTo("foo");
    assertThat(barVar.getNameNode().getString()).isNotEmpty();
    assertThat(barVar.getNameNode().getString()).doesNotMatch("foo$\\d+");
  }

  @Test
  public void testUniqueNamesWithExistingFunction() {
    createTestNode("function foo() {} function test() { function bar() {} }")
        .traverse(tokenStream);
    nodeTraversal.traverse(tokenStream.getRootNode());
    Var fooVar = scope.getVar("foo");
    Node barNode = scope.getVar("bar").getNode();
    assertThat(fooVar.getNameNode().getString()).isEqualTo("foo");
    assertThat(barNode.getType()).isEqualTo(Token.FUNCTION);
    assertThat(barNode.getFirstChild().getString()).isNotEmpty();
    assertThat(barNode.getFirstChild().getString()).doesNotMatch("foo$\\d+");
  }

  @Test
  public void testUniqueNamesWithExistingCatch() {
    createTestNode("try { throw new Error('test'); } catch (e) {} function test() { }")
        .traverse(tokenStream);
    nodeTraversal.traverse(tokenStream.getRootNode());
    Var eVar = scope.getVar("e");
    assertThat(eVar.getNameNode().getString()).isNotEmpty();
    assertThat(eVar.getNameNode().getString()).doesNotMatch("e$\\d+");
  }

  @Test
  public void testUniqueNamesWithExistingChildScope() {
    createTestNode("function foo() { function bar() {} } function test() { }")
        .traverse(tokenStream);
    nodeTraversal.traverse(tokenStream.getRootNode());
    Var fooVar = scope.getVar("foo");
    Scope childScope = fooVar.getScope();
    createTestNode("function bar() {} function test() { }")
        .traverse(tokenStream);
    nodeTraversal.traverse(tokenStream.getRootNode());
    Var barVar = childScope.getVar("bar");
    assertThat(fooVar.getNameNode().getString()).isEqualTo("foo");
    assertThat(barVar.getNameNode().getString()).isNotEmpty();
    assertThat(barVar.getNameNode().getString()).doesNotMatch("bar$\\d+");
  }

  @Test
  public void testUniqueNamesWithExistingGlobalAndChildScope() {
    createTestNode("var foo = 1; function foo() {} function test() { }")
        .traverse(tokenStream);
    nodeTraversal.traverse(tokenStream.getRootNode());
    Var fooVar1 = scope.getVar("foo");
    Scope childScope = fooVar1.getScope();
    createTestNode("function foo() {} function test() { }")
        .traverse(tokenStream);
    nodeTraversal.traverse(tokenStream.getRootNode());
    Var fooVar2 = childScope.getVar("foo");
    assertThat(fooVar1.getNameNode().getString()).isEqualTo("foo");
    assertThat(fooVar2.getNameNode().getString()).isNotEmpty();
    assertThat(fooVar2.getNameNode().getString()).doesNotMatch("foo$\\d+");
  }

  @Test
  public void testUniqueNamesWithExistingGlobalAndFunction() {
    createTestNode("var foo = 1; function foo() {} function test() { }")
        .traverse(tokenStream);
    nodeTraversal.traverse(tokenStream.getRootNode());
    Var fooVar1 = scope.getVar("foo");
    createTestNode("function foo() {} function test() { }")
        .traverse(tokenStream);
    nodeTraversal.traverse(tokenStream.getRootNode());
    Var fooVar2 = scope.getVar("foo");
    assertThat(fooVar1.getNameNode().getString()).isEqualTo("foo");
    assertThat(fooVar2.getNameNode().getString()).isNotEmpty();
    assertThat(fooVar2.getNameNode().getString()).doesNotMatch("foo$\\d+");
  }

  @Test
  public void testUniqueNamesWithExistingFunctionAndChildScope() {
    createTestNode("function foo() {} function foo() {} function test() { }")
        .traverse(tokenStream);
    nodeTraversal.traverse(tokenStream.getRootNode());
    Var fooVar1 = scope.getVar("foo");
    Scope childScope = fooVar1.getScope();
    createTestNode("function foo() {} function test() { }")
        .traverse(tokenStream);
    nodeTraversal.traverse(tokenStream.getRootNode());
    Var fooVar2 = childScope.getVar("foo");
    assertThat(fooVar1.getNameNode().getString()).isNotEmpty();
    assertThat(fooVar1.getNameNode().getString()).doesNotMatch("foo$\\d+");
    assertThat(fooVar2.getNameNode().getString()).isNotEmpty();
    assertThat(fooVar2.getNameNode().getString()).doesNotMatch("foo$\\d+");
  }

  @Test
  public void testUniqueNamesWithExistingGlobalAndFunctionAndChildScope() {
    createTestNode("var foo = 1; function foo() {} function foo() {} function test() { }")
        .traverse(tokenStream);
    nodeTraversal.traverse(tokenStream.getRootNode());
    Var fooVar1 = scope.getVar("foo");
    Scope childScope1 = fooVar1.getScope();
    createTestNode("function foo() {} function foo() {} function test() { }")
        .traverse(tokenStream);
    nodeTraversal.traverse(tokenStream.getRootNode());
    Var fooVar2 = childScope1.getVar("foo");
    Scope childScope2 = fooVar2.getScope();
    createTestNode("function foo() {} function test() { }")
        .traverse(tokenStream);
    nodeTraversal.traverse(tokenStream.getRootNode());
    Var fooVar3 = childScope2.getVar("foo");
    assertThat(fooVar1.getNameNode().getString()).isNotEmpty();
    assertThat(fooVar1.getNameNode().getString()).doesNotMatch("foo$\\d+");
    assertThat(fooVar2.getNameNode().getString()).isNotEmpty();
    assertThat(fooVar2.getNameNode().getString()).doesNotMatch("foo$\\d+");
    assertThat(fooVar3.getNameNode().getString()).isNotEmpty();
    assertThat(fooVar3.getNameNode().getString()).doesNotMatch("foo$\\d+");
  }

  @Test
  public void testUniqueNamesWithMixedConflicts() {
    createTestNode("function foo() {} function foo() {} function test() { var bar = 1; }")
        .traverse(tokenStream);
    nodeTraversal.traverse(tokenStream.getRootNode());
    Var fooVar1 = scope.getVar("foo");
    Var fooVar2 = fooVar1.getScope().getVar("foo");
    Var barVar = scope.getVar("bar");
    assertThat(fooVar1.getNameNode().getString()).isNotEmpty();
    assertThat(fooVar1.getNameNode().getString()).doesNotMatch("foo$\\d+");
    assertThat(fooVar2.getNameNode().getString()).isNotEmpty();
    assertThat(fooVar2.getNameNode().getString()).doesNotMatch("foo$\\d+");
    assertThat(barVar.getNameNode().getString()).isNotEmpty();
    assertThat(barVar.getNameNode().getString()).doesNotMatch("bar$\\d+");
  }

  private Node createTestNode(String nodeString) {
    TokenStream tokenStream = new TokenStream();
    tokenStream.parse(nodeString, 0, nodeString.length());
    return tokenStream.getRootNode();
  }

  private static <T> T Eq(T value) {
    return value;
  }
}