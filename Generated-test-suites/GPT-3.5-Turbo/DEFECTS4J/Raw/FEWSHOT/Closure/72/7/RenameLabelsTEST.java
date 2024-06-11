
package com.google.javascript.jscomp;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.javascript.rhino.Node;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RenameLabelsTest {
    private RenameLabels renameLabels;

    @Before
    public void setUp() {
        renameLabels = new RenameLabels(new AbstractCompiler(), new DefaultNameSupplier(), true);
    }

    @Test
    public void testProcessLabelsShouldTraverse() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.LABEL);
        Node parent = new Node(Token.LABEL);
        Deque<LabelNamespace> namespaceStack = Lists.newLinkedList();
        LabelNamespace labelNamespace = new LabelNamespace();
        labelNamespace.renameMap.put("label", new LabelInfo(1));
        namespaceStack.push(labelNamespace);
        renameLabels.new ProcessLabels().namespaceStack = namespaceStack;
        renameLabels.new ProcessLabels().shouldTraverse(nodeTraversal, node, parent);
        assertEquals("a", renameLabels.new ProcessLabels().getNameForId(1));
    }

    @Test
    public void testProcessLabelsVisit() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.LABEL);
        Node parent = new Node(Token.LABEL);
        renameLabels.new ProcessLabels().namespaceStack.push(new LabelNamespace());
        LabelNamespace labelNamespace = new LabelNamespace();
        labelNamespace.renameMap.put("label", new LabelInfo(1));
        renameLabels.new ProcessLabels().namespaceStack.push(labelNamespace);
        Node nameNode = new Node(Token.NAME, "label");
        node.addChildToBack(nameNode);
        renameLabels.new ProcessLabels().visit(nodeTraversal, node, parent);
        assertEquals("a", nameNode.getString());
    }

    @Test
    public void testProcessLabelsVisitBreakOrContinue() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.BREAK);
        Node parent = new Node(Token.BREAK);
        renameLabels.new ProcessLabels().namespaceStack.push(new LabelNamespace());
        LabelNamespace labelNamespace = new LabelNamespace();
        labelNamespace.renameMap.put("label", new LabelInfo(1));
        renameLabels.new ProcessLabels().namespaceStack.push(labelNamespace);
        Node nameNode = new Node(Token.NAME, "label");
        node.addChildToBack(nameNode);
        renameLabels.new ProcessLabels().visit(nodeTraversal, node, parent);
        assertEquals("a", nameNode.getString());
    }

    @Test
    public void testProcessLabelsVisitLabel() {
        NodeTraversal nodeTraversal = new NodeTraversal();
        Node node = new Node(Token.LABEL);
        Node parent = new Node(Token.LABEL);
        renameLabels.new ProcessLabels().namespaceStack.push(new LabelNamespace());
        LabelNamespace labelNamespace = new LabelNamespace();
        labelNamespace.renameMap.put("label", new LabelInfo(1));
        renameLabels.new ProcessLabels().namespaceStack.push(labelNamespace);
        Node nameNode = new Node(Token.NAME, "label");
        node.addChildToBack(nameNode);
        renameLabels.new ProcessLabels().visit(nodeTraversal, node, parent);
        assertEquals("a", nameNode.getString());
    }

    @Test
    public void testGetLabelInfo() {
        renameLabels.new ProcessLabels().namespaceStack.push(new LabelNamespace());
        LabelInfo labelInfo = new LabelInfo(1);
        renameLabels.new ProcessLabels().namespaceStack.peek().renameMap.put("label", labelInfo);
        assertEquals(labelInfo, renameLabels.new ProcessLabels().getLabelInfo("label"));
    }

    @Test
    public void testGetNameForId() {
        ArrayList<String> names = new ArrayList<String>();
        names.add("a");
        renameLabels.new ProcessLabels().names = names;
        assertEquals("a", renameLabels.new ProcessLabels().getNameForId(1));
    }

    @Test
    public void testProcess() {
        Node externs = new Node(Token.EXTERN);
        Node root = new Node(Token.ROOT);
        renameLabels.process(externs, root);
    }

    @Test
    public void testDefaultNameSupplier() {
        RenameLabels.DefaultNameSupplier defaultNameSupplier = new RenameLabels.DefaultNameSupplier();
        assertEquals("", defaultNameSupplier.nameGenerator.prefix);
    }
}
