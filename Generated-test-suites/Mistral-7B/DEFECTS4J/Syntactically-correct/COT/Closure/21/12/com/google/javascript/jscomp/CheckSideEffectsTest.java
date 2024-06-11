package com.google.javascript.jscomp.tests;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private CompilerInput input;
  private CheckSideEffects checkSideEffects;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    nodeFactory = NodeFactory.getInitial();
    input = new CompilerInput(new SourceFile("TestFile.js", ""));
  }

  @Test
  public void testEmptyStatement() {
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node root = nodeFactory.createTree("");
    checkSideEffects.process(input, root);
  }

  @Test
  public void testVoidStatement() {
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node root = nodeFactory.createTree(";");
    checkSideEffects.process(input, root);
  }

  @Test
  public void testExpressionResultUsed() {
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node root = nodeFactory.createTree("var x = 1; return x + 1;");
    checkSideEffects.process(input, root);
  }

  @Test
  public void testCommaOperator() {
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node root = nodeFactory.createTree("a = 1, b = 2;");
    checkSideEffects.process(input, root);
  }

  @Test
  public void testStatementInsideBlock() {
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node root = nodeFactory.createTree("{a = 1;}");
    checkSideEffects.process(input, root);
  }

  @Test
  public void testStatementWithinForLoop() {
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node root = nodeFactory.createTree("for (var i = 0; i < 5; i++) { i++; }");
    checkSideEffects.process(input, root);
  }

  @Test(expected = AssertionError.class)
  public void testStatementWithoutSemicolon() {
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node root = nodeFactory.createTree("a = b; x = y;");
    checkSideEffects.process(input, root);
  }

  @Test
  public void testSimpleOperatorWithoutSideEffect() {
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node root = nodeFactory.createTree("a = b + c;");
    checkSideEffects.process(input, root);
  }

  @Test
  public void testSimpleOperatorWithSideEffect() {
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node root = nodeFactory.createTree("var s = \"this string is \"; s += \"continued on the next line\";");
    checkSideEffects.process(input, root);
  }

  @Test
  public void testMissingPlusSign() {
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node root = nodeFactory.createTree("var s = \"this string is \"; s = \"continued on the next line but you forgot the +\";");
    checkSideEffects.process(input, root);
  }

  @Test
  public void testFunctionCallWithoutSideEffect() {
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node root = nodeFactory.createTree("function foo() {} foo();");
    checkSideEffects.process(input, root);
  }

  @Test(expected = AssertionError.class)
  public void testFunctionCallWithSideEffect() {
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node root = nodeFactory.createTree("function foo() { document.write('Hello, World!'); } foo();");
    checkSideEffects.process(input, root);
  }

  @Test
  public void testFunctionCallWithProtectedSideEffect() {
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    Node root = nodeFactory.createTree("function foo() { document.write('Hello, World!'); } JSCOMPILER_PRESERVE(foo)();");
    checkSideEffects.process(input, root);
  }

  @Test
  public void testProtectSideEffects() {
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    checkSideEffects.process(input, createProtectedCode());
  }

  @Test
  public void testStripProtection() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    CompilerPassManager passManager = mock(CompilerPassManager.class);
    when(compiler.getPassManager()).thenReturn(passManager);

    StripProtection stripProtection = new StripProtection(compiler);
    passManager.registerPass(CompilerPass.Phase.SYNTHESIZE, stripProtection);

    NodeFactory nodeFactory = NodeFactory.getInitial();
    Node root = nodeFactory.createTree("function JSCOMPILER_PRESERVE(foo) { return foo(); } JSCOMPILER_PRESERVE(1 + 2);");
    checkSideEffects.process(input, root);

    ArgumentCaptor<CompilerPass> passCaptor = ArgumentCaptor.forClass(CompilerPass.class);
    verify(passManager).runPasses(eq(Phase.SYNTHESIZE), passCaptor.capture());
    StripProtection stripProtectionInstance = passCaptor.getValue();
    verify(stripProtectionInstance).process(eq(input), eq(root));
  }

  private Node createProtectedCode() {
    NodeFactory nodeFactory = NodeFactory.getInitial();
    Node root = nodeFactory.createTree("function JSCOMPILER_PRESERVE(foo) { return foo(); } JSCOMPILER_PRESERVE(1 + 2);");
    return root;
  }
}