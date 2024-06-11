package org.apache.commons.cli.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;

public class FunctionRewriterTest {

    private AbstractCompiler compiler = mock(AbstractCompiler.class);
    private NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    private NodeTraversal.Callback reductionGatherer;
    private FunctionRewriter functionRewriter;

    @org.junit.Before
    public void setUp() {
        when(compiler.parseSyntheticCode(anyString(), anyString())).then(invocation -> invocation.getArgument(1));
        reductionGatherer = mock(NodeTraversal.Callback.class);

        functionRewriter = new FunctionRewriter(compiler);
    }

    @org.junit.Test
    public void testEmptyFunctionReducer() {
        Node emptyFunctionNode = NodeUtil.createFunctionExpression(null, null, null, null, null, null);
        when(nodeTraversal.shouldTraverse(any(NodeTraversal.class), eq(emptyFunctionNode), any(Node.class)))
                .thenReturn(true);
        when(nodeTraversal.traverse(any(NodeTraversal.class), any(Node.class), any(Node.class))).thenReturn(null);

        reductionGatherer = new FunctionRewriter.ReductionGatherer(ImmutableList.of(functionRewriter.new EmptyFunctionReducer()),
                HashMultimap.create());
        NodeTraversal.traverse(compiler, emptyFunctionNode, reductionGatherer);

        Node expectedHelperNode = NodeUtil.newCall("JSCompiler_emptyFn");
        assertEquals(expectedHelperNode, reductionGatherer.reductions.get(functionRewriter.new EmptyFunctionReducer()).get(0).newChild);
    }

    @org.junit.Test
    public void testIdentityReducer() {
        String functionSource = "function test() {return this}";
        Node functionNode = NodeUtil.parseExpression(functionSource);

        when(nodeTraversal.shouldTraverse(any(NodeTraversal.class), eq(functionNode), any(Node.class)))
                .thenReturn(true);
        when(nodeTraversal.traverse(any(NodeTraversal.class), any(Node.class), any(Node.class))).thenReturn(null);

        reductionGatherer = new FunctionRewriter.ReductionGatherer(ImmutableList.of(functionRewriter.new IdentityReducer()),
                HashMultimap.create());
        NodeTraversal.traverse(compiler, functionNode, reductionGatherer);

        Node expectedHelperNode = NodeUtil.newCall("JSCompiler_identityFn");
        assertEquals(expectedHelperNode, reductionGatherer.reductions.get(functionRewriter.new IdentityReducer()).get(0).newChild);
    }

    @org.junit.Test
    public void testReturnConstantReducer() {
        String functionSource = "function test() {return 10}";
        Node functionNode = NodeUtil.parseExpression(functionSource);

        when(nodeTraversal.shouldTraverse(any(NodeTraversal.class), eq(functionNode), any(Node.class)))
                .thenReturn(true);
        when(nodeTraversal.traverse(any(NodeTraversal.class), any(Node.class), any(Node.class))).thenReturn(null);

        reductionGatherer = new FunctionRewriter.ReductionGatherer(ImmutableList.of(functionRewriter.new ReturnConstantReducer()),
                HashMultimap.create());
        NodeTraversal.traverse(compiler, functionNode, reductionGatherer);

        Node expectedHelperNode = NodeUtil.newCall("JSCompiler_returnArg", 10);
        assertEquals(expectedHelperNode, reductionGatherer.reductions.get(functionRewriter.new ReturnConstantReducer()).get(0).newChild);
    }

    @org.junit.Test
    public void testGetterReducer() {
        String functionSource = "function test(a) {return this.a}";
        Node functionNode = NodeUtil.parseExpression(functionSource);

        Node propNameNode = NodeUtil.newString("a");

        when(nodeTraversal.shouldTraverse(any(NodeTraversal.class), eq(functionNode), any(Node.class)))
                .thenReturn(true);
        when(nodeTraversal.traverse(any(NodeTraversal.class), any(Node.class), any(Node.class)))
                .thenReturn(NodeUtil.newGetProp(NodeUtil.newThis(), propNameNode));

        reductionGatherer = new FunctionRewriter.ReductionGatherer(ImmutableList.of(functionRewriter.new GetterReducer()),
                HashMultimap.create());
        NodeTraversal.traverse(compiler, functionNode, reductionGatherer);

        Node expectedHelperNode = NodeUtil.newCall("JSCompiler_get", propNameNode);
        assertEquals(expectedHelperNode, reductionGatherer.reductions.get(functionRewriter.new GetterReducer()).get(0).newChild);
    }

    @org.junit.Test
    public void testSetterReducer() {
        String functionSource = "function test(value) {this.a = value}";
        Node functionNode = NodeUtil.parseExpression(functionSource);

        Node propNameNode = NodeUtil.newString("a");
        Node valueNode = NodeUtil.newNumber(10);

        when(nodeTraversal.shouldTraverse(any(NodeTraversal.class), eq(functionNode), any(Node.class)))
                .thenReturn(true);
        when(nodeTraversal.traverse(any(NodeTraversal.class), any(Node.class), any(Node.class)))
                .thenReturn(NodeUtil.newAssign(NodeUtil.newGetProp(NodeUtil.newThis(), propNameNode), valueNode));

        reductionGatherer = new FunctionRewriter.ReductionGatherer(ImmutableList.of(functionRewriter.new SetterReducer()),
                HashMultimap.create());
        NodeTraversal.traverse(compiler, functionNode, reductionGatherer);

        Node expectedHelperNode = NodeUtil.newCall("JSCompiler_set", propNameNode, valueNode);
        assertEquals(expectedHelperNode, reductionGatherer.reductions.get(functionRewriter.new SetterReducer()).get(0).newChild);
    }

    @org.junit.Test
    public void testNonReducibleFunction() {
        String functionSource = "function test() {return this.a + this.b}";
        Node functionNode = NodeUtil.parseExpression(functionSource);

        when(nodeTraversal.shouldTraverse(any(NodeTraversal.class), eq(functionNode), any(Node.class)))
                .thenReturn(true);
        when(nodeTraversal.traverse(any(NodeTraversal.class), any(Node.class), any(Node.class))).thenReturn(null);

        reductionGatherer = new FunctionRewriter.ReductionGatherer(ImmutableList.of(functionRewriter.new EmptyFunctionReducer(),
                functionRewriter.new IdentityReducer(), functionRewriter.new ReturnConstantReducer(),
                functionRewriter.new GetterReducer(), functionRewriter.new SetterReducer()),
                HashMultimap.create());
        NodeTraversal.traverse(compiler, functionNode, reductionGatherer);

        assertNull(reductionGatherer.reductions.get(functionRewriter.new SetterReducer()));
    }
}