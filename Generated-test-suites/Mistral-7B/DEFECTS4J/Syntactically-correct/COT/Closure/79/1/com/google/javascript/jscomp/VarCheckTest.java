package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;

/**
 * Tests for the VarCheck class.
 */
public class VarCheckTest {

  private AbstractCompiler compiler;
  private VarCheck varCheck;
  private NodeTraversal nodeTraversal;
  private Scope mockScope;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    nodeTraversal = mock(NodeTraversal.class);
    mockScope = mock(Scope.class);

    when(compiler.newExternInput("{SyntheticVarsDeclar}")).thenReturn(new CompilerInput());
    when(compiler.getErrorLevel(any()))
        .thenReturn(CheckLevel.WARNING); // Set warning level for testing

    varCheck = new VarCheck(compiler);
  }

  @Test
  public void testBasicFunctionDeclaration() {
    Node functionNode = Node.newFunction(Token.NAME, "foo");
    varCheck.visit(nodeTraversal, null, functionNode);
    assertNull(varCheck.getSynthesizedExternsInput());
  }

  @Test
  public void testUndefinedVariable() {
    Node varNode = Node.newName("bar");
    varCheck.visit(nodeTraversal, null, null);

    Scope scope = nodeTraversal.getScope();
    assertFalse(scope.contains("bar"));
    assertSame(varCheck.getSynthesizedExternsInput(),
        varCheck.getSynthesizedExternsRoot().getFirstChild());
  }

  @Test(expected = IllegalStateException.class)
  public void testUndefinedVariableWithSanityCheck() {
    varCheck = new VarCheck(compiler, true);
    Node varNode = Node.newName("bar");
    varCheck.visit(nodeTraversal, null, null);
  }

  @Test
  public void testVariableWithDependentModule() {
    JSModule moduleA = mock(JSModule.class);
    JSModule moduleB = mock(JSModule.class);
    CompilerInput inputA = new CompilerInput("ModuleA.js", moduleA);
    CompilerInput inputB = new CompilerInput("ModuleB.js", moduleB);

    when(compiler.getModuleGraph().dependsOn(moduleA, moduleB)).thenReturn(true);

    Node varNodeA = Node.newName("varA");
    Node varNodeB = Node.newName("varB");

    when(nodeTraversal.getInput()).thenReturn(inputA);
    when(nodeTraversal.getScope().getVar("varB")).thenReturn(new Scope.Var(null, varNodeB));

    varCheck.visit(nodeTraversal, null, null);

    when(nodeTraversal.getInput()).thenReturn(inputB);

    varCheck.visit(nodeTraversal, null, null);

    assertSame(inputB.getAstRoot(compiler), varCheck.getSynthesizedExternsRoot());
  }

  @Test
  public void testVariableWithModuleDependencyViolation() {
    JSModule moduleA = mock(JSModule.class);
    JSModule moduleB = mock(JSModule.class);
    CompilerInput inputA = new CompilerInput("ModuleA.js", moduleA);
    CompilerInput inputB = new CompilerInput("ModuleB.js", moduleB);

    when(compiler.getModuleGraph().dependsOn(moduleB, moduleA)).thenReturn(true);

    Node varNodeA = Node.newName("varA");
    Node varNodeB = Node.newName("varB");

    when(nodeTraversal.getInput()).thenReturn(inputB);
    when(nodeTraversal.getScope().getVar("varA")).thenReturn(new Scope.Var(null, varNodeA));

    varCheck.visit(nodeTraversal, null, null);

    when(nodeTraversal.getInput()).thenReturn(inputA);

    varCheck.visit(nodeTraversal, null, null);

    assertSame(inputA.getAstRoot(compiler), varCheck.getSynthesizedExternsRoot());
  }

  @Test
  public void testVariableWithMissingModuleDependency() {
    JSModule moduleA = mock(JSModule.class);
    JSModule moduleB = mock(JSModule.class);
    CompilerInput inputA = new CompilerInput("ModuleA.js", moduleA);
    CompilerInput inputB = new CompilerInput("ModuleB.js", moduleB);

    when(compiler.getModuleGraph().dependsOn(moduleA, moduleB)).thenReturn(false);

    Node varNodeA = Node.newName("varA");
    Node varNodeB = Node.newName("varB");

    when(nodeTraversal.getInput()).thenReturn(inputB);
    when(nodeTraversal.getScope().getVar("varA")).thenReturn(null);

    varCheck.visit(nodeTraversal, null, null);

    assertNull(varCheck.getSynthesizedExternsInput());
  }

  @Test
  public void testVariableWithStrictModuleDependency() {
    JSModule moduleA = mock(JSModule.class);
    JSModule moduleB = mock(JSModule.class);
    CompilerInput inputA = new CompilerInput("ModuleA.js", moduleA);
    CompilerInput inputB = new CompilerInput("ModuleB.js", moduleB);

    Node varNodeA = Node.newName("varA");
    Node varNodeB = Node.newName("varB");

    when(nodeTraversal.getInput()).thenReturn(inputB);
    when(nodeTraversal.getScope().getVar("varA")).thenReturn(new Scope.Var(null, varNodeA));

    varCheck = new VarCheck(compiler, true);

    varCheck.visit(nodeTraversal, null, null);

    when(nodeTraversal.getInput()).thenReturn(inputA);

    varCheck.visit(nodeTraversal, null, null);

    assertNull(varCheck.getSynthesizedExternsInput());
  }

  @Test
  public void testVariableWithEmptyNameFunctionDeclaration() {
    Node functionNode = Node.newFunction(Token.NAME, "");
    varCheck.visit(nodeTraversal, null, functionNode);
    assertNull(varCheck.getSynthesizedExternsInput());
  }

  @Test
  public void testVariableWithDuplicateDeclaration() {
    Node varNode = Node.newName("foo");
    Node varNodeDuplicate = Node.newName("foo");

    Scope scope = Sets.newHashSet("foo");
    when(nodeTraversal.getScope()).thenReturn(mockScope);

    varCheck.visit(nodeTraversal, null, null);
    varCheck.visit(nodeTraversal, null, null);

    Preconditions.checkState(nodeTraversal.getLastReportedDiagnostic() != null);
  }
}