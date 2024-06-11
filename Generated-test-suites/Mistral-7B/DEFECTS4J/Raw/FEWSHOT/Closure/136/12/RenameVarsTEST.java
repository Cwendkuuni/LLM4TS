package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.jscomp.VariableMap;

import java.util.*;

public class RenameVarsTest {

    @Test
    public void testRenameVars() {
        String prefix = "a_";
        RenameVars renameVars = new RenameVars(null, prefix, false, false, false, null, null, null);

        // Test processing with empty input
        Node root = Node.EMPTY;
        renameVars.process(Node.EMPTY, root);
        assertNull(renameVars.getVariableMap());

        // Test processing with local variables only
        List<Node> nodes = new ArrayList<>();
        nodes.add(Node.newName("x"));
        nodes.add(Node.newName("y"));
        Node input = Node.newBlock(nodes);
        renameVars.process(Node.newBlock(nodes), input);
        VariableMap variableMap = renameVars.getVariableMap();
        assertEquals(2, variableMap.size());
        assertEquals("x", variableMap.getName("x"));
        assertEquals("y", variableMap.getName("y"));

        // Test processing with global and local variables
        nodes.clear();
        nodes.add(Node.newName("x"));
        nodes.add(Node.newName("y"));
        input = Node.newBlock(nodes);
        nodes.add(Node.newName("z"));
        Node globals = Node.newBlock(nodes);
        renameVars.process(globals, input);
        variableMap = renameVars.getVariableMap();
        assertEquals(3, variableMap.size());
        assertEquals("x", variableMap.getName("x"));
        assertEquals("y", variableMap.getName("y"));
        assertNull(variableMap.getName("z"));

        // Test processing with reserved variables
        Set<String> reservedNames = Sets.newHashSet("console", "alert");
        renameVars = new RenameVars(null, prefix, false, false, false, null, null, reservedNames);
        nodes.clear();
        nodes.add(Node.newName("console"));
        nodes.add(Node.newName("alert"));
        nodes.add(Node.newName("x"));
        input = Node.newBlock(nodes);
        renameVars.process(Node.newBlock(nodes), input);
        variableMap = renameVars.getVariableMap();
        assertEquals(3, variableMap.size());
        assertNull(variableMap.getName("console"));
        assertNull(variableMap.getName("alert"));
        assertEquals("x", variableMap.getName("x"));

        // Test processing with long variable names
        prefix = "long_";
        renameVars = new RenameVars(null, prefix, false, false, false, null, null, null);
        nodes.clear();
        nodes.add(Node.newName("aVeryLongVariableNameThatCanBeRenamedShorter"));
        input = Node.newBlock(nodes);
        renameVars.process(Node.newBlock(nodes), input);
        variableMap = renameVars.getVariableMap();
        assertEquals(1, variableMap.size());
        String newName = variableMap.getName("aVeryLongVariableNameThatCanBeRenamedShorter");
        assertNotNull(newName);
        assertTrue(newName.length() < "aVeryLongVariableNameThatCanBeRenamedShorter".length());

        // Test processing with renamed variables from previous compilation
        Set<String> prevUsedRenameMapVars = Sets.newHashSet("x1", "x2");
        VariableMap prevUsedRenameMap = new VariableMap(prevUsedRenameMapVars);
        renameVars = new RenameVars(null, prefix, false, false, false, prevUsedRenameMap, null, null);
        nodes.clear();
        nodes.add(Node.newName("x1"));
        nodes.add(Node.newName("x2"));
        nodes.add(Node.newName("x"));
        input = Node.newBlock(nodes);
        renameVars.process(Node.newBlock(nodes), input);
        variableMap = renameVars.getVariableMap();
        assertEquals(3, variableMap.size());
        assertNull(variableMap.getName("x1"));
        assertNull(variableMap.getName("x2"));
        assertEquals("x", variableMap.getName("x"));
    }
}