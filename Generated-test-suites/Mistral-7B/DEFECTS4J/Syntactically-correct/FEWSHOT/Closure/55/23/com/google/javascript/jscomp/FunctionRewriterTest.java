package org.apache.commons.cli.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.Token;

import org.apache.commons.cli.FunctionRewriter;
import org.apache.commons.cli.FunctionRewriter.Reducer;
import org.apache.commons.cli.FunctionRewriter.Reduction;
import org.apache.commons.cli.FunctionRewriter.ReductionGatherer;
import org.apache.commons.cli.FunctionRewriter.SingleReturnStatementReducer;

@RunWith(JUnit4.class)
public class FunctionRewriterTest {

    @Test
    public void testEmptyFunctionReducer() {
        Compiler compiler = new Compiler();
        FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
        Node root = compiler.parseJs("function foo() {}");
        functionRewriter.process(null, root);
        Node foo = root.getChildAt(0);
        assertEquals(Token.CALL, foo.getType());
        assertEquals("JSCompiler_emptyFn", foo.getFirstChild().getString());
    }

    @Test
    public void testIdentityReducer() {
        Compiler compiler = new Compiler();
        FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
        Node root = compiler.parseJs("function foo(x) { return x; }");
        functionRewriter.process(null, root);
        Node foo = root.getChildAt(0);
        assertEquals(Token.CALL, foo.getType());
        assertEquals("JSCompiler_identityFn", foo.getFirstChild().getString());
        assertEquals("x", foo.getChildAt(1).getString());
    }

    @Test
    public void testReturnConstantReducer() {
        Compiler compiler = new Compiler();
        FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
        Node root = compiler.parseJs("function foo() { return 42; }");
        functionRewriter.process(null, root);
        Node foo = root.getChildAt(0);
        assertEquals(Token.CALL, foo.getType());
        assertEquals("JSCompiler_returnArg", foo.getFirstChild().getString());
        assertEquals(42, ((Node) foo.getLastChild()).getNumber().intValue());
    }

    @Test
    public void testGetterReducer() {
        Compiler compiler = new Compiler();
        FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
        Node root = compiler.parseJs("function foo() { return this.a; }");
        functionRewriter.process(null, root);
        Node foo = root.getChildAt(0);
        assertEquals(Token.CALL, foo.getType());
        assertEquals("JSCompiler_get", foo.getFirstChild().getString());
        assertEquals("a", foo.getChildAt(1).getString());
    }

    @Test
    public void testSetterReducer() {
        Compiler compiler = new Compiler();
        FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
        Node root = compiler.parseJs("function foo(x) { this.a = x; }");
        functionRewriter.process(null, root);
        Node foo = root.getChildAt(0);
        assertEquals(Token.CALL, foo.getType());
        assertEquals("JSCompiler_set", foo.getFirstChild().getString());
        assertEquals("a", foo.getChildAt(1).getString());
    }

    @Test
    public void testReductionGatherer() {
        Compiler compiler = new Compiler();
        FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
        Node root = compiler.parseJs(
            "function foo() {" +
            "  return function() {" +
            "    return 1;" +
            "  }();" +
            "  return 2;" +
            "}" +
            "function bar() {" +
            "  return 3;" +
            "}" +
            "function baz() {" +
            "  return function() {" +
            "    return 4;" +
            "  }();" +
            "  return 5;" +
            "}"
        );
        functionRewriter.process(null, root);
        List<Reducer> reducers = ImmutableList.of(
            new EmptyFunctionReducer(),
            new IdentityReducer(),
            new ReturnConstantReducer(),
            new GetterReducer(),
            new SetterReducer()
        );
        Multimap<Reducer, Reduction> reductionMap = HashMultimap.create();
        NodeTraversal.traverse(compiler, root, new ReductionGatherer(reducers, reductionMap));
        int count = 0;
        for (Reducer reducer : reducers) {
            Collection<Reduction> reductions = reductionMap.get(reducer);
            assertEquals(1, reductions.size());
            Reduction reduction = reductions.iterator().next();
            assertEquals(root, reduction.parent);
            assertEquals(count++, reduction.oldChild.getIndex());
        }
    }

    @Test
    public void testIsReduceableFunctionExpression() {
        Compiler compiler = new Compiler();
        Node root = compiler.parseJs("function foo() {}", "foo.js");
        assertTrue(FunctionRewriter.isReduceableFunctionExpression(root.getLastChild()));
        Node root2 = compiler.parseJs("function foo(x) {}", "foo.js");
        assertFalse(FunctionRewriter.isReduceableFunctionExpression(root2.getLastChild()));
    }

    @Test
    public void testGetHelperCode() {
        FunctionRewriter functionRewriter = new FunctionRewriter(null);
        assertEquals("function JSCompiler_emptyFn() {" +
                     "  return function() {}" +
                     "}", functionRewriter.parseHelperCode(EmptyFunctionReducer.class).getString());
        assertEquals("function JSCompiler_identityFn() {" +
                     "  function " + EmptyFunctionReducer.FACTORY_METHOD_NAME + "_value(value) {" +
                     "    return value" +
                     "  }" +
                     "  return " + EmptyFunctionReducer.FACTORY_METHOD_NAME + "_value" +
                     "}", functionRewriter.parseHelperCode(IdentityReducer.class).getString());
        assertEquals("function JSCompiler_returnArg(value) {" +
                     "  return function() {return value}" +
                     "}", functionRewriter.parseHelperCode(ReturnConstantReducer.class).getString());
        assertEquals("function JSCompiler_get(name) {" +
                     "  return function() {return this[" + name + "]}" +
                     "}", functionRewriter.parseHelperCode(GetterReducer.class).getString());
        assertEquals("function JSCompiler_set(name) {" +
                     "  return function(" + EmptyFunctionReducer.FACTORY_METHOD_NAME + "_value) {" +
                     "this[" + name + "] = " + EmptyFunctionReducer.FACTORY_METHOD_NAME + "_value" +
                     "}", functionRewriter.parseHelperCode(SetterReducer.class).getString());
    }

    @Test
    public void testBuildCallNode() {
        FunctionRewriter functionRewriter = new FunctionRewriter(null);
        Node helperCode = functionRewriter.parseHelperCode(IdentityReducer.class);
        Node call = functionRewriter.buildCallNode("JSCompiler_identityFn", null, 1, 1);
        assertEquals(Token.CALL, call.getType());
        assertEquals(helperCode.getLastChild().getString(), call.getFirstChild().getString());
        assertEquals(1, call.getChildCount());
        assertEquals(0, call.getChildAt(0).getIndex());
    }
}