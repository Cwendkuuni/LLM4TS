
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RenameLabelsTest {
    @Test
    public void testEnterScope() {
        RenameLabels r = new RenameLabels(new AbstractCompiler());

        NodeTraversal t = new NodeTraversal(null, null);
        r.new ProcessLabels().enterScope(t);

        assertEquals(1, r.namespaceStack.size());
    }

    @Test
    public void testExitScope() {
        RenameLabels r = new RenameLabels(new AbstractCompiler());

        NodeTraversal t = new NodeTraversal(null, null);
        r.new ProcessLabels().exitScope(t);

        assertEquals(0, r.namespaceStack.size());
    }

    @Test
    public void testShouldTraverse_LabelNode() {
        RenameLabels r = new RenameLabels(new AbstractCompiler());
        r.namespaceStack.push(new RenameLabels.LabelNamespace());

        NodeTraversal t = new NodeTraversal(null, null);
        Node labelNode = new Node(Token.LABEL);
        Node nameNode = new Node(Token.NAME, "label");
        labelNode.addChildToBack(nameNode);
        assertTrue(r.new ProcessLabels().shouldTraverse(t, labelNode, null));
    }

    @Test
    public void testShouldTraverse_OtherNode() {
        RenameLabels r = new RenameLabels(new AbstractCompiler());
        r.namespaceStack.push(new RenameLabels.LabelNamespace());

        NodeTraversal t = new NodeTraversal(null, null);
        Node otherNode = new Node(Token.COMMENT);
        assertTrue(r.new ProcessLabels().shouldTraverse(t, otherNode, null));
    }

    @Test
    public void testVisit_LabelNode() {
        RenameLabels r = new RenameLabels(new AbstractCompiler());
        r.namespaceStack.push(new RenameLabels.LabelNamespace());
        r.names.add("a");

        NodeTraversal t = new NodeTraversal(null, null);
        Node labelNode = new Node(Token.LABEL);
        Node nameNode = new Node(Token.NAME, "label");
        labelNode.addChildToBack(nameNode);
        r.new ProcessLabels().visit(t, labelNode, null);

        assertEquals("a", nameNode.getString());
    }

    @Test
    public void testVisit_LabelNode_Unreferenced() {
        RenameLabels r = new RenameLabels(new AbstractCompiler());
        r.namespaceStack.push(new RenameLabels.LabelNamespace());

        NodeTraversal t = new NodeTraversal(null, null);
        Node labelNode = new Node(Token.LABEL);
        Node nameNode = new Node(Token.NAME, "label");
        labelNode.addChildToBack(nameNode);
        r.new ProcessLabels().visit(t, labelNode, null);

        assertEquals(0, nameNode.getChildCount());
    }

    @Test
    public void testVisit_BreakNode() {
        RenameLabels r = new RenameLabels(new AbstractCompiler());
        r.namespaceStack.push(new RenameLabels.LabelNamespace());
        r.namespaceStack.peek().renameMap.put("label", new RenameLabels.LabelInfo(1));
        r.names.add("a");

        NodeTraversal t = new NodeTraversal(null, null);
        Node breakNode = new Node(Token.BREAK);
        Node labelNode = new Node(Token.NAME, "label");
        breakNode.addChildToBack(labelNode);
        r.new ProcessLabels().visit(t, breakNode, null);

        assertEquals("a", labelNode.getString());
    }

    @Test
    public void testVisit_BreakNode_UndefinedLabel() {
        RenameLabels r = new RenameLabels(new AbstractCompiler());
        r.namespaceStack.push(new RenameLabels.LabelNamespace());
        r.names.add("a");

        NodeTraversal t = new NodeTraversal(null, null);
        Node breakNode = new Node(Token.BREAK);
        Node labelNode = new Node(Token.NAME, "label");
        breakNode.addChildToBack(labelNode);
        r.new ProcessLabels().visit(t, breakNode, null);

        assertEquals("label", labelNode.getString());
    }

    @Test
    public void testVisit_ContinueNode() {
        RenameLabels r = new RenameLabels(new AbstractCompiler());
        r.namespaceStack.push(new RenameLabels.LabelNamespace());
        r.namespaceStack.peek().renameMap.put("label", new RenameLabels.LabelInfo(1));
        r.names.add("a");

        NodeTraversal t = new NodeTraversal(null, null);
        Node continueNode = new Node(Token.CONTINUE);
        Node labelNode = new Node(Token.NAME, "label");
        continueNode.addChildToBack(labelNode);
        r.new ProcessLabels().visit(t, continueNode, null);

        assertEquals("a", labelNode.getString());
    }

    @Test
    public void testVisit_ContinueNode_UndefinedLabel() {
        RenameLabels r = new RenameLabels(new AbstractCompiler());
        r.namespaceStack.push(new RenameLabels.LabelNamespace());
        r.names.add("a");

        NodeTraversal t = new NodeTraversal(null, null);
        Node continueNode = new Node(Token.CONTINUE);
        Node labelNode = new Node(Token.NAME, "label");
        continueNode.addChildToBack(labelNode);
        r.new ProcessLabels().visit(t, continueNode, null);

        assertEquals("label", labelNode.getString());
    }
}
