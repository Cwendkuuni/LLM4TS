package org.apache.commons.cli.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Reducer;
import com.google.javascript.jscomp.Reduction;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Util;

public class FunctionRewriterTest {

    @Test
    public void testEmptyFunctionReducer() {
        FunctionRewriter functionRewriter = new FunctionRewriter(Util.newAbstractCompiler());
        Node root = functionRewriter.parseHelperCode(FunctionRewriter.EmptyFunctionReducer.class);
        assertNotNull(root);

        Node emptyFunction = new Node(Token.FUNCTION, 0, 0);
        Node emptyFunctionExpression = new Node(Token.FUNCTION_EXPRESSION, 0, 0);
        emptyFunctionExpression.addChildToBack(emptyFunction);

        Reducer reducer = FunctionRewriter.EmptyFunctionReducer.class.newInstance();
        Reduction reduction = new Reduction(root, emptyFunctionExpression, root);
        assertSame(root, reduction.apply());
        assertEquals(FunctionRewriter.EmptyFunctionReducer.FACTORY_METHOD_NAME,
                emptyFunction.getString());
    }

    @Test
    public void testIdentityReducer() {
        FunctionRewriter functionRewriter = new FunctionRewriter(Util.newAbstractCompiler());
        Node helperCode = functionRewriter.parseHelperCode(FunctionRewriter.IdentityReducer.class);
        assertNotNull(helperCode);

        Node identityFunction = new Node(Token.FUNCTION, 1, 0);
        identityFunction.addChildToBack(new Node(Token.PARAMETER, 0, 0, "arg0"));
        identityFunction.addChildToBack(new Node(Token.RETURN, 0, 0, identityFunction.getFirstChild()));

        Node identityFunctionExpression = new Node(Token.FUNCTION_EXPRESSION, 0, 0);
        identityFunctionExpression.addChildToBack(identityFunction);

        Reducer reducer = FunctionRewriter.IdentityReducer.class.newInstance();
        Reduction reduction = new Reduction(identityFunctionExpression, identityFunction, identityFunctionExpression);
        assertSame(identityFunctionExpression, reduction.apply());
        assertEquals(FunctionRewriter.IdentityReducer.FACTORY_METHOD_NAME + "_value",
                identityFunction.getString());
    }

    @Test
    public void testReturnConstantReducer() {
        FunctionRewriter functionRewriter = new FunctionRewriter(Util.newAbstractCompiler());
        Node helperCode = functionRewriter.parseHelperCode(FunctionRewriter.ReturnConstantReducer.class);
        assertNotNull(helperCode);

        Node constantFunction = new Node(Token.FUNCTION, 0, 0);
        Node constant = new Node(Token.NUMBER, 0, 0, 42);
        constantFunction.addChildToBack(new Node(Token.RETURN, 0, 0, constant));

        Node constantFunctionExpression = new Node(Token.FUNCTION_EXPRESSION, 0, 0);
        constantFunctionExpression.addChildToBack(constantFunction);

        Reducer reducer = FunctionRewriter.ReturnConstantReducer.class.newInstance();
        Reduction reduction = new Reduction(constantFunctionExpression, constant, constantFunctionExpression);
        assertSame(constantFunctionExpression, reduction.apply());
        assertEquals("returnArg", constantFunction.getString());
        assertEquals(Integer.toString(42), constant.getString());
    }

    @Test
    public void testReductionGatherer() {
        FunctionRewriter functionRewriter = new FunctionRewriter(Util.newAbstractCompiler());

        List<Reducer> reducers = ImmutableList.of(
                FunctionRewriter.EmptyFunctionReducer.class,
                FunctionRewriter.IdentityReducer.class,
                FunctionRewriter.ReturnConstantReducer.class);

        Multimap<Reducer, Reduction> reductionMap = HashMultimap.create();
        Node root = new Node(Token.BLOCK, 0, 0);

        NodeTraversal.traverse(functionRewriter, root,
                new FunctionRewriter.ReductionGatherer(reducers, reductionMap));

        for (Reducer reducer : reducers) {
            Collection<Reduction> reductions = reductionMap.get(reducer);
            for (Reduction reduction : reductions) {
                assertNotNull(reduction);
            }
        }
    }

    @Test
    public void testProcess() {
        FunctionRewriter functionRewriter = new FunctionRewriter(Util.newAbstractCompiler());

        List<Reducer> reducers = ImmutableList.of(
                FunctionRewriter.EmptyFunctionReducer.class,
                FunctionRewriter.IdentityReducer.class,
                FunctionRewriter.ReturnConstantReducer.class);

        Node root = new Node(Token.BLOCK, 0, 0);

        Node a = new Node(Token.VAR, 0, 0, "a");
        Node b = new Node(Token.VAR, 0, 0, "b");
        Node c = new Node(Token.VAR, 0, 0, "c");

        root.addChildToBack(new Node(Token.VAR, 0, 0, "a"));
        root.addChildToBack(new Node(Token.VAR, 0, 0, "b"));
        root.addChildToBack(new Node(Token.VAR, 0, 0, "c"));

        root.addChildToBack(new Node(Token.ASSIGN, 0, 0, a,
                new Node(Token.NUMBER, 0, 0, 1)));

        root.addChildToBack(new Node(Token.FUNCTION_DECLARATION, 0, 0, "getA", null,
                new Node(Token.FUNCTION, 0, 0),
                new Node(Token.RETURN, 0, 0, new Node(Token.THIS, 0, 0, "a"))));

        root.addChildToBack(new Node(Token.FUNCTION_DECLARATION, 0, 0, "setA", null,
                new Node(Token.FUNCTION, 0, 0),
                new Node(Token.PARAMETER, 0, 0, "value"),
                new Node(Token.ASSIGN, 0, 0, new Node(Token.THIS, 0, 0, "a"), new Node(Token.NAME, 0, 0, "value"))));

        Node oldRoot = root;

        functionRewriter.process(null, root);

        Node newRoot = root;

        // Getters should be reduced to calls to helper functions.
        assertEquals(FunctionRewriter.IdentityReducer.FACTORY_METHOD_NAME,
                newRoot.getFirstChild().getLastChild().getString());
        assertEquals(FunctionRewriter.IdentityReducer.FACTORY_METHOD_NAME,
                newRoot.getChild(2).getLastChild().getString());

        // Setters should be reduced to calls to helper functions.
        assertEquals(FunctionRewriter.IdentityReducer.FACTORY_METHOD_NAME + "_value",
                newRoot.getChild(4).getChild(1).getString());

        // Helper functions should be inserted at the top level.
        Node helperNode = newRoot.getChild(0);
        assertEquals(FunctionRewriter.EmptyFunctionReducer.FACTORY_METHOD_NAME,
                helperNode.getLastChild().getString());
        assertEquals(FunctionRewriter.IdentityReducer.FACTORY_METHOD_NAME,
                helperNode.getChild(1).getString());
        assertEquals(FunctionRewriter.IdentityReducer.FACTORY_METHOD_NAME + "_value",
                helperNode.getChild(2).getString());
        assertEquals(FunctionRewriter.ReturnConstantReducer.FACTORY_METHOD_NAME,
                helperNode.getChild(3).getString());
        assertEquals("returnArg", helperNode.getChild(4).getString());
        assertEquals(Integer.toString(1),
                helperNode.getChild(5).getString());

        // Old root should not have been modified.
        assertEquals(oldRoot.getChildCount(), root.getChildCount());
        for (int i = 0; i < oldRoot.getChildCount(); i++) {
            Node oldChild = oldRoot.getChild(i);
            Node newChild = newRoot.getChild(i);
            assertEquals(oldChild.getType(), newChild.getType());
            assertEquals(oldChild.getString(), newChild.getString());
        }
    }
}