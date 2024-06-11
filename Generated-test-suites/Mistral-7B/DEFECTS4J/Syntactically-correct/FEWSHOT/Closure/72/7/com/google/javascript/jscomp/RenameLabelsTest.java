package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert. assertNull;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.HashSet;
import java.util.Map;

public class RenameLabelsTest {

    @Test
    public void testRenameLabels() {
        AbstractCompiler compiler = new Compiler();
        RenameLabels renameLabels = new RenameLabels(compiler);

        String input = "function foo() {\n" +
                "  a: {\n" +
                "    b: break a;\n" +
                "    c: break a;\n" +
                "  }\n" +
                "  d: {\n" +
                "    e: continue d;\n" +
                "    f: continue d;\n" +
                "  }\n" +
                "  g: break d;\n" +
                "}\n";

        Node externs = Node.parse(input);
        Node root = Node.parse("function bar(){}");

        renameLabels.process(externs, root);
        NodeTraversal nodeTraversal = NodeTraversal.create(compiler, root);
        NodeTraversal.traverse(compiler, root, new ProcessLabels(nodeTraversal));

        // Check renamed labels
        assertNotNull(getNodeByName("a", root));
        assertNotNull(getNodeByName("b", root));
        assertNotNull(getNodeByName("c", root));
        assertNotNull(getNodeByName("d", root));
        assertNotNull(getNodeByName("e", root));
        assertNotNull(getNodeByName("f", root));

        // Check renamed breaks and continues
        Node breakA = getNodeByName("break a", root);
        Node continueD = getNodeByName("continue d", root);
        assertNotNull(breakA);
        assertNotNull(continueD);
        assertNull(getNodeByName("g", root)); // removed

        // Check that there is no label "e" in the new code
        assertNull(getNodeByName("e", root));

        // Check that the order of the blocks is preserved
        Node blockA = getNodeByName("a", root).getLastChild();
        Node blockB = getNodeByName("b", root).getLastChild();
        Node blockC = getNodeByName("c", root).getLastChild();
        Node blockD = getNodeByName("d", root).getLastChild();

        assert (NodeUtil.getChildrenAsList(blockA).get(0).equals(blockB));
        assert (NodeUtil.getChildrenAsList(blockA).get(1).equals(blockC));
        assert (NodeUtil.getChildrenAsList(root).get(1).equals(blockD));
    }

    private Node getNodeByName(String name, Node node) {
        Node labelNode = NodeUtil.findFirstChildOfType(node, Token.LABEL);
        while (labelNode != null) {
            if (labelNode.getFirstChild().getString().equals(name)) {
                return labelNode;
            }
            labelNode = NodeUtil.findFirstChildOfType(labelNode, Token.BLOCK);
        }
        return null;
    }

    private static class ProcessLabels extends RenameLabels.ProcessLabels {
        ProcessLabels(NodeTraversal nodeTraversal) {
            super();
            this.nodeTraversal = nodeTraversal;
        }

        NodeTraversal nodeTraversal;

        @Override
        public void visit(NodeTraversal nodeTraversal, Node node, Node parent) {
            super.visit(nodeTraversal, node, parent);
            this.nodeTraversal = nodeTraversal;
        }

        @Override
        public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
            return super.shouldTraverse(nodeTraversal, node, parent) || this.nodeTraversal.shouldTraverse(node, parent);
        }
    }
}