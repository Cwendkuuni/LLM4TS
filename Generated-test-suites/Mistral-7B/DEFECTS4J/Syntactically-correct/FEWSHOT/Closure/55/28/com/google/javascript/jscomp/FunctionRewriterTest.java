package org.apache.commons.cli.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.Token;

public class FunctionRewriterTest {

 /* Test utility methods */

 private Node createNode(int type, int line, int charPositionInLine, String text) {
 return new Node(type, line, charPositionInLine, text);
 }

 private Node createFunctionNode(int line, int charPositionInLine, String functionText) {
 return new Node(Token.FUNCTION, line, charPositionInLine)
 .addChildToBack(createNode(Token.NAME, line, charPositionInLine, "function"))
 .addChildToBack(createNode(Token.LPAREN, line, charPositionInLine))
 .addChildToBack(createNode(Token.RPAREN, line, charPositionInLine + functionText.length()))
 .addChildToBack(createNode(Token.LCURLY, line, charPositionInLine + functionText.length() + 1, ""))
 .addChildToBack(createNode(Token.RCURLY, line, charPositionInLine + functionText.length() + 2, ""));
 }

 private Node getFirstChild(Node node) {
 return node != null ? node.getFirstChild() : null;
 }

 private Node getLastChild(Node node) {
 return node != null ? node.getLastChild() : null;
 }

 /**
 * Test for EmptyFunctionReducer
 */
 @Test
 public void testEmptyFunctionReducer() {
 FunctionRewriter functionRewriter = new FunctionRewriter(null);

 // Test case 1: empty function
 Node emptyFunctionNode = createFunctionNode(1, 1, "");
 Node expectedEmptyFunctionNode = createNode(Token.CALL, 1, 1, "JSCompiler_emptyFn");

 FunctionRewriter.EmptyFunctionReducer emptyFunctionReducer = new FunctionRewriter.EmptyFunctionReducer();
 Callback reducerCallback = new ReductionApplier(functionRewriter, emptyFunctionReducer);
 NodeTraversal.traverse(functionRewriter, emptyFunctionNode, reducerCallback);

 Node actualEmptyFunctionNode = getFirstChild(getLastChild(functionRewriter.rootNode));
 assertEquals(expectedEmptyFunctionNode.toString(), actualEmptyFunctionNode.toString());

 // Test case 2: non-empty function
 Node nonEmptyFunctionNode = createFunctionNode(1, 1, "function(){}");
 Node expectedNonEmptyFunctionNode = nonEmptyFunctionNode;

 NodeTraversal.traverse(functionRewriter, nonEmptyFunctionNode, reducerCallback);
 assertEquals(expectedNonEmptyFunctionNode.toString(), actualEmptyFunctionNode.toString());
 }

 /**
 * Test for IdentityReducer
 */
 @Test
 public void testIdentityReducer() {
 FunctionRewriter functionRewriter = new FunctionRewriter(null);

 // Test case 1: identity function
 Node identityFunctionNode = createFunctionNode(1, 1, "function(a){return a}");
 Node expectedIdentityFunctionNode = createNode(Token.CALL, 1, 1, "JSCompiler_identityFn_value");
 Node argumentNode = createNode(Token.NAME, 1, 1, "a");

 FunctionRewriter.IdentityReducer identityReducer = new FunctionRewriter.IdentityReducer();
 Callback reducerCallback = new ReductionApplier(functionRewriter, identityReducer);
 NodeTraversal.traverse(functionRewriter, identityFunctionNode, reducerCallback);

 Node actualIdentityFunctionNode = getFirstChild(getLastChild(functionRewriter.rootNode));
 assertEquals(expectedIdentityFunctionNode.toString(), actualIdentityFunctionNode.toString());

 // Test case 2: non-identity function
 Node nonIdentityFunctionNode = createFunctionNode(1, 1, "function(a){return a*a}");
 Node expectedNonIdentityFunctionNode = nonIdentityFunctionNode;

 NodeTraversal.traverse(functionRewriter, nonIdentityFunctionNode, reducerCallback);
 assertEquals(expectedNonIdentityFunctionNode.toString(), actualIdentityFunctionNode.toString());
 }

 /**
 * Test for SingleReturnStatementReducer
 */
 @Test
 public void testSingleReturnStatementReducer() {
 FunctionRewriter functionRewriter = new FunctionRewriter(null);

 // Test case 1: single return statement function with constant return value
 Node singleReturnStatementFunctionNodeConstant = createFunctionNode(1, 1, "function(){return 1}");
 Node expectedSingleReturnStatementFunctionNodeConstant = createNode(Token.CALL, 1, 1, "JSCompiler_returnArg_1");

 FunctionRewriter.SingleReturnStatementReducer singleReturnStatementReducerConstant = new FunctionRewriter.SingleReturnStatementReducer() {};
 Callback reducerCallback = new ReductionApplier(functionRewriter, singleReturnStatementReducerConstant);
 NodeTraversal.traverse(functionRewriter, singleReturnStatementFunctionNodeConstant, reducerCallback);

 Node actualSingleReturnStatementFunctionNodeConstant = getFirstChild(getLastChild(functionRewriter.rootNode));
 assertEquals(expectedSingleReturnStatementFunctionNodeConstant.toString(), actualSingleReturnStatementFunctionNodeConstant.toString());

 // Test case 2: single return statement function with variable return value
 Node singleReturnStatementFunctionNodeVariable = createFunctionNode(1, 1, "function(){return a}");
 Node expectedSingleReturnStatementFunctionNodeVariable = singleReturnStatementFunctionNodeVariable;
 Node argumentNodeVariable = createNode(Token.NAME, 1, 1, "a");

 FunctionRewriter.SingleReturnStatementReducer singleReturnStatementReducerVariable = new FunctionRewriter.SingleReturnStatementReducer() {};
 Callback reducerCallbackVariable = new ReductionApplier(functionRewriter, singleReturnStatementReducerVariable);

 NodeTraversal.traverse(functionRewriter, singleReturnStatementFunctionNodeVariable, reducerCallbackVariable);
 assertEquals(expectedSingleReturnStatementFunctionNodeVariable.toString(), actualSingleReturnStatementFunctionNodeVariable.toString());
 }

 /**
 * Test for GetterReducer
 */
 @Test
 public void testGetterReducer() {
 FunctionRewriter functionRewriter = new FunctionRewriter(null);

 // Test case 1: getter function
 Node getterFunctionNode = createFunctionNode(1, 1, "function(){return this.a}");
 Node expectedGetterFunctionNode = createNode(Token.CALL, 1, 1, "JSCompiler_get_a");
 Node propertyNameNode = createNode(Token.NAME, 1, 1, "a");

 FunctionRewriter.GetterReducer getterReducer = new FunctionRewriter.GetterReducer();
 Callback reducerCallback = new ReductionApplier(functionRewriter, getterReducer);
 NodeTraversal.traverse(functionRewriter, getterFunctionNode, reducerCallback);

 Node actualGetterFunctionNode = getFirstChild(getLastChild(functionRewriter.rootNode));
 assertEquals(expectedGetterFunctionNode.toString(), actualGetterFunctionNode.toString());

 // Test case 2: non-getter function
 Node nonGetterFunctionNode = createFunctionNode(1, 1, "function(a){return a*a}");
 Node expectedNonGetterFunctionNode = nonGetterFunctionNode;

 NodeTraversal.traverse(functionRewriter, nonGetterFunctionNode, reducerCallback);
 assertEquals(expectedNonGetterFunctionNode.toString(), actualGetterFunctionNode.toString());
 }

 /**
 * Test for SetterReducer
 */
 @Test
 public void testSetterReducer() {
 FunctionRewriter functionRewriter = new FunctionRewriter(null);

 // Test case 1: setter function
 Node setterFunctionNode = createFunctionNode(1, 1, "function(a){this.a = a}");
 Node expectedSetterFunctionNode = createNode(Token.CALL, 1, 1, "JSCompiler_set_a");
 Node propertyNameNode = createNode(Token.NAME, 1, 1, "a");

 FunctionRewriter.SetterReducer setterReducer = new FunctionRewriter.SetterReducer();
 Callback reducerCallback = new ReductionApplier(functionRewriter, setterReducer);
 NodeTraversal.traverse(functionRewriter, setterFunctionNode, reducerCallback);

 Node actualSetterFunctionNode = getFirstChild(getLastChild(functionRewriter.rootNode));
 assertEquals(expectedSetterFunctionNode.toString(), actualSetterFunctionNode.toString());

 // Test case 2: non-setter function
 Node nonSetterFunctionNode = createFunctionNode(1, 1, "function(a){return a*a}");
 Node expectedNonSetterFunctionNode = nonSetterFunctionNode;

 NodeTraversal.traverse(functionRewriter, nonSetterFunctionNode, reducerCallback);
 assertEquals(expectedNonSetterFunctionNode.toString(), actualSetterFunctionNode.toString());
 }
}

class ReductionApplier implements Callback {
 private final FunctionRewriter functionRewriter;
 private final Reducer reducer;

 ReductionApplier(FunctionRewriter functionRewriter, Reducer reducer) {
 this.functionRewriter = functionRewriter;
 this.reducer = reducer;
 }

 @Override
 public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
 return functionRewriter.isReduceableFunctionExpression(node) && reducer.canReduce(node);
 }

 @Override
 public void visit(NodeTraversal nodeTraversal, Node node, Node parent) {
 Node newChild = reducer.reduce(node);
 if (newChild != null) {
 functionRewriter.replaceChild(parent, node, newChild);
 }
 }
}