package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;

import java.util.List;

public class CheckSideEffectsTest {

    private AbstractCompiler compiler;
    private Node externsNode;
    private Node rootNode;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        externsNode = new Node(Token.EXTERN_RESULT);
        rootNode = new Node(Token.ROOT);
    }

    @Test
    public void testProcess() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        checkSideEffects.process(externsNode, rootNode);

        // Verify that the process method calls the traverse method of NodeTraversal with the correct arguments
        verify(compiler).traverse(compiler, rootNode, checkSideEffects);
    }

    @Test
    public void testHotSwapScript() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        checkSideEffects.hotSwapScript(rootNode, rootNode);

        // Verify that the hotSwapScript method calls the traverse method of NodeTraversal with the correct arguments
        verify(compiler).traverse(compiler, rootNode, checkSideEffects);
    }

    @Test
    public void testVisit_withEmptyNode_shouldDoNothing() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        NodeTraversal traversal = mock(NodeTraversal.class);
        Node node = new Node(Token.EMPTY);

        checkSideEffects.visit(traversal, node, null);

        // Verify that nothing happens when visiting an empty node
        verifyNoInteractions(compiler);
    }

    @Test
    public void testVisit_withNonSideEffectNode_shouldDoNothing() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        NodeTraversal traversal = mock(NodeTraversal.class);
        Node node = new Node(Token.STRING);

        checkSideEffects.visit(traversal, node, null);

        // Verify that nothing happens when visiting a non-side-effect node with no JSDocInfo
        verifyNoInteractions(compiler);
    }

    @Test
    public void testVisit_withNonResultUsedNode_shouldReportWarning() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        NodeTraversal traversal = mock(NodeTraversal.class);
        Node node = new Node(Token.PLUS,
                             new Node(Token.STRING, IR.string("foo")),
                             new Node(Token.STRING, IR.string("bar")));
        node.putBooleanProp(NodeUtil.NO_SIDE_EFFECTS, true);

        checkSideEffects.visit(traversal, node, null);

        // Verify that the visit method reports a warning for the non-result used node with no JSDocInfo
        verify(compiler).report(traversal.makeError(node, CheckLevel.WARNING, CheckSideEffects.USELESS_CODE_ERROR, "This code lacks side-effects. Is there a bug?"));
    }

    @Test
    public void testVisit_withResultUsedStringNode_shouldReportWarning() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        NodeTraversal traversal = mock(NodeTraversal.class);
        Node node = new Node(Token.STRING, IR.string("foo"));
        node.putBooleanProp(NodeUtil.NO_SIDE_EFFECTS, true);

        checkSideEffects.visit(traversal, node, null);

        // Verify that the visit method reports a warning for the result used string node with no JSDocInfo
        verify(compiler).report(traversal.makeError(node, CheckLevel.WARNING, CheckSideEffects.USELESS_CODE_ERROR, "This code lacks side-effects. Is there a bug?"));
    }

    @Test
    public void testVisit_withResultUsedSimpleOpNode_shouldReportWarning() {
        CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
        NodeTraversal traversal = mock(NodeTraversal.class);
        Node node = new Node(Token.ADD,
                             new Node(Token.NUMBER, IR.number(1)),
                             new Node(Token.NUMBER, IR.number(2)));
        node.putBooleanProp(NodeUtil.NO_SIDE_EFFECTS, true);

        checkSideEffects.visit(traversal, node, null);

        // Verify that the visit method reports a warning for the result used simple operator node with no JSDocInfo
        verify(compiler).report(traversal.makeError(node, CheckLevel.WARNING, CheckSideEffects.USELESS_CODE_ERROR, "The result of the '+' operator is not being used."));
    }

    // Other tests for the CheckSideEffects class...

}