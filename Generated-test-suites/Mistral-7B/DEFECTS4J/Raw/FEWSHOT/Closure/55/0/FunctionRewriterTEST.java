package org.apache.commons.cli;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class FunctionRewriterTest {

    private FunctionRewriter functionRewriter;
    private Node root;
    private Node originalFunctionNode;
    private Node reducedFunctionNode;

    public FunctionRewriterTest(Node originalFunctionNode, Node reducedFunctionNode) {
        this.originalFunctionNode = originalFunctionNode;
        this.reducedFunctionNode = reducedFunctionNode;
    }

    @Parameterized.Parameters(name = "{index}: originalFunctionNode={originalFunctionNode} reducedFunctionNode={reducedFunctionNode}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {
                        NodeUtil.createFunctionExpression("function() { return 1; }", 1),
                        NodeUtil.createFunctionExpression("JSCompiler_returnArg(1)", 1)
                },
                {
                        NodeUtil.createFunctionExpression("function() { return this.a; }", 1),
                        NodeUtil.createCall(NodeUtil.createIdentifier("JSCompiler_get"),
                                NodeUtil.createIdentifier("a"), 1)
                },
                {
                        NodeUtil.createFunctionExpression("function(a) { this.a = a; }", 1),
                        null
                },
                {
                        NodeUtil.createFunctionExpression("function(a) { this.a = a; }", 1),
                        NodeUtil.createCall(NodeUtil.createIdentifier("JSCompiler_set"),
                                NodeUtil.createIdentifier("a"), null, 1)
                },
                {
                        NodeUtil.createFunctionExpression("function(a, b) { return a + b; }", 1),
                        null
                },
                {
                        NodeUtil.createFunctionExpression("function(a, b) { return a + b; }", 1),
                        NodeUtil.createCall(NodeUtil.createIdentifier("JSCompiler_add"),
                                NodeUtil.createIdentifier("a"), NodeUtil.createIdentifier("b"), 1)
                }
        });
    }

    @Before
    public void setup() {
        AbstractCompiler compiler = new AbstractCompiler() {};
        functionRewriter = new FunctionRewriter(compiler);
        root = NodeUtil.createBlock(1);
        root.addChildToBack(originalFunctionNode);
    }

    @Test
    public void testFunctionRewriting() {
        functionRewriter.process(null, root);

        Node actualFunctionNode = findFunctionNode(originalFunctionNode.getLineno(), root);
        assertSame(reducedFunctionNode, actualFunctionNode);
    }

    private Node findFunctionNode(int lineno, Node node) {
        NodeTraversal traversal = new NodeTraversal(node, null) {
            @Override
            public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
                return node.getLineno() == lineno && node.getType() == Token.FUNCTION;
            }
        };

        if (traversal.nextNode() != null) {
            return traversal.getCurrentNode();
        } else {
            return null;
        }
    }

    @Test
    public void testEmptyFunctionRewriting() {
        Node originalFunctionNode = NodeUtil.createFunctionExpression("function() {}", 1);
        Node reducedFunctionNode = NodeUtil.createCall(NodeUtil.createIdentifier("JSCompiler_emptyFn"), null, 1);

        AbstractCompiler compiler = new AbstractCompiler() {};
        FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

        Node root = NodeUtil.createBlock(1);
        root.addChildToBack(originalFunctionNode);

        functionRewriter.process(null, root);

        Node actualFunctionNode = findFunctionNode(originalFunctionNode.getLineno(), root);
        assertSame(reducedFunctionNode, actualFunctionNode);
    }

    @Test
    public void testIdentityFunctionRewriting() {
        Node originalFunctionNode = NodeUtil.createFunctionExpression("function(x) { return x; }", 1);
        Node reducedFunctionNode = NodeUtil.createCall(NodeUtil.createIdentifier("JSCompiler_identityFn"),
                NodeUtil.createIdentifier("x"), 1);

        AbstractCompiler compiler = new AbstractCompiler() {};
        FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

        Node root = NodeUtil.createBlock(1);
        root.addChildToBack(originalFunctionNode);

        functionRewriter.process(null, root);

        Node actualFunctionNode = findFunctionNode(originalFunctionNode.getLineno(), root);
        assertSame(reducedFunctionNode, actualFunctionNode);
    }

    @Test
    public void testGetPropertyNameFromSetterFunction() {
        Node originalFunctionNode = NodeUtil.createFunctionExpression(
                "function(value) { this.a = value; }", 1);
        String propertyName = "a";

        AbstractCompiler compiler = new AbstractCompiler() {};
        FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

        Node reducer = new GetterSetterReducer();
        Multimap<Reducer, Reduction> reductionMap = HashMultimap.create();
        NodeTraversal.traverse(compiler, root, new ReductionGatherer(reducer, reductionMap));

        for (Reduction reduction : reductionMap.get(reducer)) {
            if (reduction.parent.getType() == Token.FUNCTION) {
                Node functionNode = reduction.parent;
                Node getPropertyNode = reducer.getPropertyName(functionNode);
                assertSame(NodeUtil.createIdentifier(propertyName), getPropertyNode);
                break;
            }
        }
    }

    @Test
    public void testSetPropertyNameFromGetterFunction() {
        Node originalFunctionNode = NodeUtil.createFunctionExpression("function() { return this.a; }", 1);
        String propertyName = "a";

        AbstractCompiler compiler = new AbstractCompiler() {};
        FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

        Node reducer = new GetterSetterReducer();
        Multimap<Reducer, Reduction> reductionMap = HashMultimap.create();
        NodeTraversal.traverse(compiler, root, new ReductionGatherer(reducer, reductionMap));

        for (Reduction reduction : reductionMap.get(reducer)) {
            if (reduction.parent.getType() == Token.FUNCTION) {
                Node functionNode = reduction.parent;
                Node setPropertyNode = reducer.getPropertyName(functionNode);
                assertSame(NodeUtil.createIdentifier(propertyName), setPropertyNode);
                break;
            }
        }
    }

    @Test
    public void testGetPropertyNameFromGetterFunctionReturnsNull() {
        Node originalFunctionNode = NodeUtil.createFunctionExpression("function() { return this.a.b; }", 1);

        AbstractCompiler compiler = new AbstractCompiler() {};
        FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

        Node reducer = new GetterReducer();
        Multimap<Reducer, Reduction> reductionMap = HashMultimap.create();
        NodeTraversal.traverse(compiler, root, new ReductionGatherer(reducer, reductionMap));

        for (Reduction reduction : reductionMap.get(reducer)) {
            if (reduction.parent.getType() == Token.FUNCTION) {
                Node functionNode = reduction.parent;
                Node getPropertyNode = reducer.getPropertyName(functionNode);
                assertNull(getPropertyNode);
                break;
            }
        }
    }

    @Test
    public void testSetPropertyNameFromSetterFunctionReturnsNull() {
        Node originalFunctionNode = NodeUtil.createFunctionExpression("function(value) { this.a.b = value; }", 1);

        AbstractCompiler compiler = new AbstractCompiler() {};
        FunctionRewriter functionRewriter = new FunctionRewriter(compiler);

        Node reducer = new SetterReducer();
        Multimap<Reducer, Reduction> reductionMap = HashMultimap.create();
        NodeTraversal.traverse(compiler, root, new ReductionGatherer(reducer, reductionMap));

        for (Reduction reduction : reductionMap.get(reducer)) {
            if (reduction.parent.getType() == Token.FUNCTION) {
                Node functionNode = reduction.parent;
                Node setPropertyNode = reducer.getPropertyName(functionNode);
                assertNull(setPropertyNode);
                break;
            }
        }
    }

    private static class GetterSetterReducer extends Reducer {
        @Override
        public String getHelperSource() {
            return "function getter(name) { return function() { return this[" + name + "]; } }" +
                   "function setter(name) { return function(value) { this[" + name + "] = value; } }";
        }

        @Override
        public Node reduce(Node node) {
            if (!isReduceableFunctionExpression(node)) {
                return node;
            }

            boolean isGetter = NodeUtil.isName(node.getLastChild())
                    && "getter".equals(node.getLastChild().getString());
            boolean isSetter = NodeUtil.isName(node.getLastChild())
                    && "setter".equals(node.getLastChild().getString());

            if (isGetter) {
                return NodeUtil.createCall(NodeUtil.createIdentifier("getter"),
                        NodeUtil.createIdentifier("getterName"), 1);
            }
            if (isSetter) {
                return NodeUtil.createCall(NodeUtil.createIdentifier("setter"),
                        NodeUtil.createIdentifier("getterName"), null, 1);
            }

            return NodeUtil.createFunctionExpression(node.toString(), node.getLineno());
        }

        private String getterName;

        private String getPropertyName(Node node) {
            if (NodeUtil.isFunctionExpression(node) && getterName != null) {
                Node lastChild = node.getLastChild();
                if (lastChild.getType() == Token.FUNCTION &&
                        "getter".equals(lastChild.getString())) {
                    return lastChild.getChildNodes().get(0).getString();
                }
            }
            return null;
        }
    }

    private static class ReductionGatherer implements NodeTraversal.Callback {
        private final Reducer reducer;
        private final Multimap<Reducer, Reduction> reductions;

        ReductionGatherer(Reducer reducer, Multimap<Reducer, Reduction> reductions) {
            this.reducer = reducer;
            this.reductions = reductions;
        }

        @Override
        public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
            return reducer.reduce(node) != node;
        }

        @Override
        public void visit(NodeTraversal traversal, Node node, Node parent) {
            Reduction reduction = new Reduction(parent, node, reducer.reduce(node));
            reductions.put(reducer, reduction);
        }
    }
}