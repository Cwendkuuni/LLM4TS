package org.apache.commons.cli.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.FunctionRewriter.Reducer;
import com.google.javascript.jscomp.FunctionRewriter;

public class FunctionRewriterTest {

 private static FunctionRewriter createFunctionRewriter(Node root) {
 NodeCompiler compiler = new NodeCompiler();
 Node externs = Nodeops.createExterns();
 return new FunctionRewriter(compiler);
 }

 @Test
 public void testEmptyFunctionReducer() {
 Node root = Nodeops.compileString("(function() {})()");
 FunctionRewriter functionRewriter = createFunctionRewriter(root);
 functionRewriter.process(null, root);
 Node expectedRoot = Nodeops.compileString("JSCompiler_emptyFn()()");
 assertEquals(expectedRoot, root);
 }

 @Test
 public void testIdentityFunctionReducer() {
 Node root = Nodeops.compileString("(function(x) {return x})()");
 FunctionRewriter functionRewriter = createFunctionRewriter(root);
 functionRewriter.process(null, root);
 Node expectedRoot = Nodeops.compileString("JSCompiler_identityFn(JSCompiler_identityFn_value)()");
 assertEquals(expectedRoot, root);
 }

 @Test
 public void testReturnConstantReducer() {
 Node root = Nodeops.compileString("(function() {return 10})()");
 FunctionRewriter functionRewriter = createFunctionRewriter(root);
 functionRewriter.process(null, root);
 Node expectedRoot = Nodeops.compileString("JSCompiler_returnArg(10)()");
 assertEquals(expectedRoot, root);
 }

 @Test
 public void testGetterReducer() {
 Node root = Nodeops.compileString("(function() {return this.a})()");
 FunctionRewriter functionRewriter = createFunctionRewriter(root);
 functionRewriter.process(null, root);
 Node expectedRoot = Nodeops.compileString("JSCompiler_get(\"a\")()");
 assertEquals(expectedRoot, root);
 }

 @Test
 public void testSetterReducer() {
 Node root = Nodeops.compileString("(function(value) {this.a = value})()");
 FunctionRewriter functionRewriter = createFunctionRewriter(root);
 functionRewriter.process(null, root);
 Node expectedRoot = Nodeops.compileString(
 "JSCompiler_set(\"a\")(JSCompiler_identityFn_value)");
 assertEquals(expectedRoot, root);
 }

 @Test
 public void testReducersNoSavings() {
 Node root = Nodeops.compileString("(function() {return 1 + 1;})()");
 FunctionRewriter functionRewriter = createFunctionRewriter(root);
 functionRewriter.process(null, root);
 Node expectedRoot = Nodeops.compileString("(function() {return 1 + 1;})()");
 assertEquals(expectedRoot, root);
 }

 @Test
 public void testReducersWithSavings() {
 Node root = Nodeops.compileString("(function() {return 10 * 10;})()");
 FunctionRewriter functionRewriter = createFunctionRewriter(root);
 functionRewriter.process(null, root);
 Node expectedRoot = Nodeops.compileString("JSCompiler_returnArg(100)()");
 assertEquals(expectedRoot, root);
 }

 @Test
 public void testComplexFunctionWithSavings() {
 Node root = Nodeops.compileString("(function(x) {return x * x;})()");
 FunctionRewriter functionRewriter = createFunctionRewriter(root);
 functionRewriter.process(null, root);
 Node expectedRoot = Nodeops.compileString("JSCompiler_mul(JSCompiler_identityFn_value, JSCompiler_identityFn_value)()");
 assertEquals(expectedRoot, root);
 }

 @Test
 public void testFunctionWithMultipleStatements() {
 Node root = Nodeops.compileString("(function() {return 1; x = 2;})()");
 FunctionRewriter functionRewriter = createFunctionRewriter(root);
 functionRewriter.process(null, root);
 // This test case checks that no reductions were applied, since the function contains multiple statements
 assertNull(functionRewriter.parseHelperCode(Reducer.class.getName() + ":helper"));
 }

 @Test
 public void testSetterWithMultipleProperties() {
 Node root = Nodeops.compileString("(function() {this.a = 1; this.b = 2;})()");
 FunctionRewriter functionRewriter = createFunctionRewriter(root);
 functionRewriter.process(null, root);
 Node expectedRoot = Nodeops.compileString("JSCompiler_set(\"a\")(JSCompiler_returnArg(1))\nJSCompiler_set(\"b\")(JSCompiler_returnArg(2))");
 assertEquals(expectedRoot.toString(), root.toString());
 }
}