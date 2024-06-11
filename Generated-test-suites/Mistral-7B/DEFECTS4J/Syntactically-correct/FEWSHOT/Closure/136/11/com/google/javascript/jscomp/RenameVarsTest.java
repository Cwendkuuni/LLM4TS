package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.*;

public class RenameVarsTest {

    @Test
    public void testRenameVars() {
        String prefix = "test_";
        boolean localRenamingOnly = false;
        boolean preserveAnonymousFunctionNames = false;
        boolean generatePseudoNames = false;
        VariableMap prevUsedRenameMap = null;
        char[] reservedCharacters = null;
        Set<String> reservedNames = Sets.newHashSet();

        RenameVars renameVars = new RenameVars(null, prefix, localRenamingOnly, preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap, reservedCharacters, reservedNames);

        Node root = Node.newNode("function test() {}");
        Node externs = Node.newNode("var a = 1, b = 2, c = 3;");

        renameVars.process(externs, root);

        // Test global variables
        List<Node> globalNameNodes = renameVars.globalNameNodes;
        for (Node node : globalNameNodes) {
            String oldName = node.getString();
            String newName = renameVars.renameMap.get(oldName);
            assertEquals(newName, getNewGlobalName(node, renameVars));
        }

        // Test local variables
        List<Node> localNameNodes = renameVars.localNameNodes;
        List<String> localTempNames = renameVars.localTempNames;
        for (int i = 0; i < localNameNodes.size(); i++) {
            Node node = localNameNodes.get(i);
            String oldTempName = localTempNames.get(i);
            String newName = renameVars.renameMap.get(oldTempName);
            assertEquals(newName, getNewLocalName(node, i, renameVars));
        }

        // Test extern variables
        assertEquals(Sets.newHashSet("a", "b", "c"), renameVars.externNames);

        // Test reserved names
        assertEquals(Sets.newHashSet("function", "var"), renameVars.reservedNames);

        // Test reusing previously used variable map
        prevUsedRenameMap = new VariableMap();
        prevUsedRenameMap.put("a", "newNameA");
        prevUsedRenameMap.put("b", "newNameB");
        renameVars = new RenameVars(null, prefix, localRenamingOnly, preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap, reservedCharacters, reservedNames);
        root = Node.newNode("function test() { var a = 1; var b = 2; }");
        renameVars.process(externs, root);
        assertEquals("newNameA", renameVars.renameMap.get("a"));
        assertEquals("newNameB", renameVars.renameMap.get("b"));

        // Test preserving anonymous function names
        preserveAnonymousFunctionNames = true;
        renameVars = new RenameVars(null, prefix, localRenamingOnly, preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap, reservedCharacters, reservedNames);
        root = Node.newNode("function test() { var a = function() { }; }");
        renameVars.process(externs, root);
        assertNull(renameVars.renameMap.get("function"));

        // Test generating pseudo names
        generatePseudoNames = true;
        renameVars = new RenameVars(null, prefix, localRenamingOnly, preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap, reservedCharacters, reservedNames);
        root = Node.newNode("function test() { var a = 1, b = 2; }");
        renameVars.process(externs, root);
        assertEquals("$test_a$", renameVars.renameMap.get("a"));
        assertEquals("$test_b$", renameVars.renameMap.get("b"));

        // Test local renaming only
        localRenamingOnly = true;
        renameVars = new RenameVars(null, prefix, localRenamingOnly, preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap, reservedCharacters, reservedNames);
        root = Node.newNode("function test() { var a = 1, b = 2; }");
        renameVars.process(externs, root);
        assertNull(renameVars.renameMap.get("a"));
        assertNull(renameVars.renameMap.get("b"));
    }

    private String getNewGlobalName(Node node, RenameVars renameVars) {
        String oldName = node.getString();
        Assignment assignment = renameVars.assignments.get(oldName);
        if (assignment != null && assignment.newName != null) {
            return assignment.newName;
        }
        return null;
    }

    private String getNewLocalName(Node node, int index, RenameVars renameVars) {
        String oldTempName = renameVars.localTempNames.get(index);
        Assignment assignment = renameVars.assignments.get(oldTempName);
        if (assignment != null && assignment.newName != null) {
            return assignment.newName;
        }
        return null;
    }
}