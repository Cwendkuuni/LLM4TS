package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.test.BaseCompilerTest;
import com.google.javascript.rhino.Token;

public class RemoveConstantExpressionsTest extends BaseCompilerTest {

 private static final String TEST_SOURCE = "RemoveConstantExpressionsTest.js";

 @Test
 public void testRemoveConstantExpressions() throws Exception {
 Compiler compiler = newCompiler();
 CompilerOptions options = new CompilerOptions();
 CompilerPass pass = new RemoveConstantExpressions(null);

 // Test simple case
 String simpleSource = "1 + foo() + bar()";
 compileAndVerify(compiler, options, pass, simpleSource);

 // Test case with side effect
 String sideEffectSource = "1 + foo() + console.log('test') + bar()";
 compileAndVerify(compiler, options, pass, sideEffectSource);

 // Test case with multiple side effects
 String multipleSideEffectsSource = "1 + foo() + console.log('test') + console.log('test2') + bar()";
 compileAndVerify(compiler, options, pass, multipleSideEffectsSource);
 }

 private void compileAndVerify(Compiler compiler, CompilerOptions options,
 CompilerPass pass, String source) throws Exception {
 Node externs = parser.parse(source, null, null);
 Node root = externs;
 if (source.charAt(0) == '\u002F' && source.charAt(1) == '*') {
 root = parser.parseJavaScriptSource(source.substring(1), null);
 externs = parser.parseJsDocCommentToNode(source.substring(0, 1));
 }

 compiler.compile(externs, root, options);
 List<Node> ast = getAST();

 Node originalRemoveConstantExpressions = findNodeByType(ast, Token.EXPR_RESULT);
 Node simplifiedRemoveConstantExpressions = null;

 if (originalRemoveConstantExpressions != null) {
 NodeTraversal traversal = new NodeTraversal(ast, null);
 traversal.traverse(new RemoveConstantExpressionsTest_TraverseRemoveConstantExpressionsCallback(
 traversal, originalRemoveConstantExpressions));
 simplifiedRemoveConstantExpressions = findNodeByType(ast, Token.EXPR_RESULT);
 }

 assertFalse("RemoveConstantExpressions test failed. Original expression not removed.",
 originalRemoveConstantExpressions != null && simplifiedRemoveConstantExpressions == null);
 if (simplifiedRemoveConstantExpressions != null) {
 Node simplifiedBody = simplifiedRemoveConstantExpressions.getFirstChild();
 List<Node> sideEffectNodes = Lists.newArrayList();
 for (int i = 0; i < simplifiedBody.getNumChildren(); i++) {
 Node child = simplifiedBody.getChild(i);
 if (NodeUtil.nodeTypeMayHaveSideEffects(child)) {
 sideEffectNodes.add(child);
 }
 }
 List<Node> expectedSideEffectNodes = Lists.newArrayList();
 Node foo = findNodeByType(ast, Token.IDENTIFIER, "foo");
 Node bar = findNodeByType(ast, Token.IDENTIFIER, "bar");
 Node consoleLog = findNodeByType(ast, Token.CALL_EXPR, "console.log");
 expectedSideEffectNodes.add(foo);
 expectedSideEffectNodes.add(bar);
 expectedSideEffectNodes.add(consoleLog);
 assertTrue("RemoveConstantExpressions test failed. Incorrect side effect nodes.",
 sideEffectNodes.size() == expectedSideEffectNodes.size() &&
 sideEffectNodes.containsAll(expectedSideEffectNodes));
 }
 }

 private static class RemoveConstantExpressionsTest_TraverseRemoveConstantExpressionsCallback
 extends AbstractPostOrderCallback {
 private final NodeTraversal traversal;
 private final Node nodeToReplace;

 RemoveConstantExpressionsTest_TraverseRemoveConstantExpressionsCallback(
 NodeTraversal traversal, Node nodeToReplace) {
 this.traversal = traversal;
 this.nodeToReplace = nodeToReplace;
 }

 @Override
 public void visit(NodeTraversal traversal, Node node, Node parent) {
 if (node.getType() == Token.EXPR_RESULT && node == nodeToReplace) {
 Node body = node.getFirstChild();
 List<Node> sideEffectNodes = Lists.newArrayList();
 for (int i = 0; i < body.getNumChildren(); i++) {
 Node child = body.getChild(i);
 if (NodeUtil.nodeTypeMayHaveSideEffects(child)) {
 sideEffectNodes.add(child);
 }
 }
 traversal.replaceWith(parent, node, new Node(Token.EXPR_STATEMENT, sideEffectNodes.get(0)));
 for (int i = 1; i < sideEffectNodes.size(); i++) {
 Node child = sideEffectNodes.get(i);
 new NodeTraversal(parent, null).traverse(new NodeTraversal.Callback() {
 @Override
 public void visit(NodeTraversal traversal, Node node, Node parent) {
 if (node.getType() != Token.EXPR_RESULT) {
 traversal.insertBefore(parent, new Node(Token.EXPR_STATEMENT, node));
 }
 }
 }, child);
 }
 return;
 }
 }
 }
}