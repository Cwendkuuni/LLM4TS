package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableSet;

import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MakeDeclaredNamesUniqueTest {

    @Test
    public void testContextualRenamer() {
        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        NodeTraversal nodeTraversal = createNodeTraversalForTest();
        makeDeclaredNamesUnique.traverse(nodeTraversal, createRootNodeForTest());

        Map<String, String> expectedRenamings = Maps.newHashMap();
        expectedRenamings.put("a", "a$$0");
        expectedRenamings.put("b", "b$$1");
        expectedRenamings.put("c", "c$$2");
        expectedRenamings.put("d", "d$$3");
        expectedRenamings.put("e", "e$$4");

        checkRenamings(nodeTraversal, expectedRenamings);
    }

    @Test
    public void testInlineRenamer() {
        String idPrefix = "_renamed_";
        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique(
            new InlineRenamer(
                () -> idPrefix + String.valueOf(Integer.MAX_VALUE),
                idPrefix,
                true));
        NodeTraversal nodeTraversal = createNodeTraversalForTest();
        makeDeclaredNamesUnique.traverse(nodeTraversal, createRootNodeForTest());

        Map<String, String> expectedRenamings = Maps.newHashMap();
        expectedRenamings.put("a", "a_renamed_" + Long.toUnsignedString(1L, 36));
        expectedRenamings.put("b", "b_renamed_" + Long.toUnsignedString(2L, 36));
        expectedRenamings.put("c", "c_renamed_" + Long.toUnsignedString(3L, 36));
        expectedRenamings.put("d", "d_renamed_" + Long.toUnsignedString(4L, 36));
        expectedRenamings.put("e", "e_renamed_" + Long.toUnsignedString(5L, 36));

        checkRenamings(nodeTraversal, expectedRenamings);
    }

    @Test
    public void testContextualRenamerWithFunction() {
        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        NodeTraversal nodeTraversal = createNodeTraversalForTestWithFunction();
        makeDeclaredNamesUnique.traverse(nodeTraversal, createRootNodeForTestWithFunction());

        Map<String, String> expectedRenamings = Maps.newHashMap();
        expectedRenamings.put("a", "a$$0");
        expectedRenamings.put("b", "b$$1");
        expectedRenamings.put("c", "c$$2");
        expectedRenamings.put("d", "d$$3");
        expectedRenamings.put("e", "e$$4");
        expectedRenamings.put("f", "f$$5");

        checkRenamings(nodeTraversal, expectedRenamings);
    }

    private void checkRenamings(NodeTraversal nodeTraversal,
            Map<String, String> expectedRenamings) {
        ImmutableSet<String> declaredNames = getDeclaredNames(nodeTraversal);
        assertEquals(expectedRenamings.size(), declaredNames.size());

        Iterator<String> iterator = expectedRenamings.keySet().iterator();
        while (iterator.hasNext()) {
            String oldName = iterator.next();
            String newName = expectedRenamings.get(oldName);
            Node node = nodeTraversal.getTopScope().getVariableByName(oldName);
            assertNotNull(node);
            assertEquals(newName, node.getString());
        }
    }

    private Node createRootNodeForTest() {
        TokenStream tokenStream = new TokenStream();
        Node rootNode = tokenStream.parse("{a: 1, b: 2, c: 3, d: 4, e: 5}");
        return rootNode;
    }

    private Node createRootNodeForTestWithFunction() {
        TokenStream tokenStream = new TokenStream();
        Node rootNode = tokenStream.parse(
            "function bar() { var a = 1; } " +
            "{a: 1, b: 2, c: 3, d: 4, e: 5}");
        return rootNode;
    }

    private NodeTraversal createNodeTraversalForTest() {
        Node rootNode = createRootNodeForTest();
        NodeTraversal nodeTraversal = new NodeTraversal(rootNode);
        nodeTraversal.setCompiler(new Compiler());
        return nodeTraversal;
    }

    private NodeTraversal createNodeTraversalForTestWithFunction() {
        Node rootNode = createRootNodeForTestWithFunction();
        NodeTraversal nodeTraversal = new NodeTraversal(rootNode);
        nodeTraversal.setCompiler(new Compiler());
        return nodeTraversal;
    }

    private ImmutableSet<String> getDeclaredNames(NodeTraversal nodeTraversal) {
        Map<String, Node> variables = Maps.newHashMap();
        nodeTraversal.traverse(
            new AbstractPostOrderCallback() {
                @Override
                public void visit(NodeTraversal t, Node n, Node parent) {
                    if (n.getType() == Token.NAME && NodeUtil.isVarDeclaration(n)) {
                        String name = n.getString();
                        variables.put(name, n);
                    }
                }
            });
        return ImmutableSet.copyOf(variables.keySet());
    }
}