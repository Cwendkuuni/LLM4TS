package com.google.javascript.jscomp.test;

import junit.framework.TestCase;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

public class CheckSideEffectsTest extends TestCase {

 private Compiler compiler;
 private CheckSideEffects checkSideEffects;

 @Before
 public void setUp() {
 compiler = new Compiler();
 checkSideEffects = new CheckSideEffects(compiler, CheckLevel.VERBOSE, false);
 }

 @Test
 public void testCheckSideEffectsSimple() {
 Node root = new Node("function foo() { x = 1; }");
 NodeTraversal.traverse(compiler, root, checkSideEffects);
 compiler.reportAllErrors();
 assertEquals(1, compiler.getDiagnostics().size());
 }

 @Test
 public void testCheckSideEffectsVoidStatement() {
 Node root = new Node("function foo() { ; }");
 NodeTraversal.traverse(compiler, root, checkSideEffects);
 compiler.reportAllErrors();
 assertEquals(0, compiler.getDiagnostics().size());
 }

 @Test
 public void testCheckSideEffectsFunctionCall() {
 Node root = new Node("function foo() { return bar(); }");
 NodeTraversal.traverse(compiler, root, checkSideEffects);
 compiler.reportAllErrors();
 assertEquals(0, compiler.getDiagnostics().size());
 }

 @Test
 public void testCheckSideEffectsExpressionResult() {
 Node root = new Node("function foo() { return 5; }");
 NodeTraversal.traverse(compiler, root, checkSideEffects);
 compiler.reportAllErrors();
 assertEquals(0, compiler.getDiagnostics().size());
 }

 @Test
 public void testCheckSideEffectsForStatement() {
 Node root = new Node("function foo() { for (var i = 0; i < 5; i++) {} }");
 NodeTraversal.traverse(compiler, root, checkSideEffects);
 compiler.reportAllErrors();
 assertEquals(0, compiler.getDiagnostics().size());
 }

 @Test
 public void testCheckSideEffectsStatementWithSideEffect() {
 Node root = new Node("function foo() { console.log('Hello World'); }");
 NodeTraversal.traverse(compiler, root, checkSideEffects);
 compiler.reportAllErrors();
 assertEquals(1, compiler.getDiagnostics().size());
 }

 @Test
 public void testCheckSideEffectsStringConcatenation() {
 Node root = new Node("function foo() { var s = 'This is a long string that is continued on the next line but you forgot the +'; }");
 NodeTraversal.traverse(compiler, root, checkSideEffects);
 compiler.reportAllErrors();
 assertEquals(1, compiler.getDiagnostics().size());
 }

 @Test
 public void testCheckSideEffectsCommaOperator() {
 Node root = new Node("function foo() { x = 1, y = 2; }");
 NodeTraversal.traverse(compiler, root, checkSideEffects);
 compiler.reportAllErrors();
 assertEquals(1, compiler.getDiagnostics().size());
 }

 @Test
 public void testCheckSideEffectsStatementWithSideEffectInForLoop() {
 Node root = new Node("function foo() { for (var i = 0; i < 5; i++) { console.log(i); } }");
 NodeTraversal.traverse(compiler, root, checkSideEffects);
 compiler.reportAllErrors();
 assertEquals(1, compiler.getDiagnostics().size());
 }

 @Test
 public void testCheckSideEffectsStatementWithSideEffectInIfStatement() {
 Node root = new Node("function foo() { if (true) { console.log('Hello World'); } }");
 NodeTraversal.traverse(compiler, root, checkSideEffects);
 compiler.reportAllErrors();
 assertEquals(1, compiler.getDiagnostics().size());
 }

 @Test
 public void testCheckSideEffectsStatementWithSideEffectInSwitchStatement() {
 Node root = new Node("function foo() { switch (1) { case 1: console.log('Hello World'); break; } }");
 NodeTraversal.traverse(compiler, root, checkSideEffects);
 compiler.reportAllErrors();
 assertEquals(1, compiler.getDiagnostics().size());
 }

 @After
 public void tearDown() {
 compiler = null;
 checkSideEffects = null;
 }
}