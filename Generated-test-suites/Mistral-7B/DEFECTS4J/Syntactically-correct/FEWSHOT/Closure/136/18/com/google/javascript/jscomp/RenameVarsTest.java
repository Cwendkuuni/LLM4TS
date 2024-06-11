package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RenameVarsTest {

    @Test
    public void testProcessVarsExternsPass() {
        String prefix = "test_";
        boolean localRenamingOnly = false;
        boolean preserveAnonymousFunctionNames = false;
        boolean generatePseudoNames = false;
        VariableMap prevUsedRenameMap = null;
        char[] reservedCharacters = null;
        Set<String> reservedNames = new HashSet<>();

        RenameVars renameVars = new RenameVars(null, prefix, localRenamingOnly,
                preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap,
                reservedCharacters, reservedNames);

        Node externsNode = createNode("function testFunction() { var a = 1; var b = 2; }");
        renameVars.process(externsNode, null);

        Set<String> expectedExternNames = new HashSet<>(Arrays.asList("a", "b"));
        assertEquals(expectedExternNames, renameVars.externNames);
        renameVars.externNames.clear();
    }

    @Test
    public void testProcessVarsSourcePass() {
        String prefix = "test_";
        boolean localRenamingOnly = false;
        boolean preserveAnonymousFunctionNames = false;
        boolean generatePseudoNames = false;
        VariableMap prevUsedRenameMap = null;
        char[] reservedCharacters = null;
        Set<String> reservedNames = new HashSet<>();

        RenameVars renameVars = new RenameVars(null, prefix, localRenamingOnly,
                preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap,
                reservedCharacters, reservedNames);

        Node externsNode = createNode("function testFunction() { var a = 1; var b = 2; }");
        Node rootNode = createNode("function mainFunction() { testFunction(); }");

        renameVars.process(externsNode, rootNode);

        // Check global name nodes
        List<Node> globalNameNodes = renameVars.globalNameNodes;
        assertEquals(2, globalNameNodes.size());
        String[] expectedGlobalNames = {"a", "b"};
        for (int i = 0; i < globalNameNodes.size(); i++) {
            Node node = globalNameNodes.get(i);
            assertEquals(expectedGlobalNames[i], node.getString());
        }

        // Check local name nodes
        List<Node> localNameNodes = renameVars.localNameNodes;
        assertEquals(0, localNameNodes.size());

        // Check local temp names
        List<String> localTempNames = renameVars.localTempNames;
        assertEquals(0, localTempNames.size());

        // Check assignments
        SortedMap<String, Assignment> assignments = renameVars.assignments;
        assertEquals(2, assignments.size());

        Assignment aAssignment = assignments.get("a");
        Assignment bAssignment = assignments.get("b");

        assertEquals("a", aAssignment.oldName);
        assertNull(aAssignment.newName);
        assertEquals(1, aAssignment.orderOfOccurrence);

        assertEquals("b", bAssignment.oldName);
        assertNull(bAssignment.newName);
        assertEquals(2, bAssignment.orderOfOccurrence);
    }

    @Test
    public void testProcessVarsWithReservedNames() {
        String prefix = "test_";
        boolean localRenamingOnly = false;
        boolean preserveAnonymousFunctionNames = false;
        boolean generatePseudoNames = false;
        VariableMap prevUsedRenameMap = null;
        char[] reservedCharacters = new char[]{'.'};
        Set<String> reservedNames = new HashSet<>();
        reservedNames.add("test");
        reservedNames.add("function");

        RenameVars renameVars = new RenameVars(null, prefix, localRenamingOnly,
                preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap,
                reservedCharacters, reservedNames);

        Node externsNode = createNode("function testFunction() { var test = 1; }");
        Node rootNode = createNode("function mainFunction() { testFunction(); }");

        renameVars.process(externsNode, rootNode);

        // Check global name nodes
        List<Node> globalNameNodes = renameVars.globalNameNodes;
        assertEquals(1, globalNameNodes.size());
        String newGlobalName = renameVars.getNewGlobalName(globalNameNodes.get(0));
        assertNotNull(newGlobalName);
        assertTrue(!newGlobalName.equals("test"));

        // Check local name nodes
        List<Node> localNameNodes = renameVars.localNameNodes;
        assertEquals(0, localNameNodes.size());

        // Check local temp names
        List<String> localTempNames = renameVars.localTempNames;
        assertEquals(0, localTempNames.size());

        // Check assignments
        SortedMap<String, Assignment> assignments = renameVars.assignments;
        assertEquals(1, assignments.size());

        Assignment aAssignment = assignments.get("test");
        assertEquals("test", aAssignment.oldName);
        assertNull(aAssignment.newName);
        assertEquals(1, aAssignment.orderOfOccurrence);
    }

    @Test
    public void testProcessVarsWithLocalRenamingOnly() {
        String prefix = "test_";
        boolean localRenamingOnly = true;
        boolean preserveAnonymousFunctionNames = false;
        boolean generatePseudoNames = false;
        VariableMap prevUsedRenameMap = null;
        char[] reservedCharacters = null;
        Set<String> reservedNames = new HashSet<>();

        RenameVars renameVars = new RenameVars(null, prefix, localRenamingOnly,
                preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap,
                reservedCharacters, reservedNames);

        Node externsNode = createNode("function testFunction() { var a = 1; var b = 2; }");
        Node rootNode = createNode("function mainFunction() { testFunction(); }");

        renameVars.process(externsNode, rootNode);

        // Check global name nodes
        List<Node> globalNameNodes = renameVars.globalNameNodes;
        assertEquals(2, globalNameNodes.size());
        String[] expectedGlobalNames = {"a", "b"};
        for (int i = 0; i < globalNameNodes.size(); i++) {
            Node node = globalNameNodes.get(i);
            assertEquals(expectedGlobalNames[i], node.getString());
        }

        // Check local name nodes
        List<Node> localNameNodes = renameVars.localNameNodes;
        assertEquals(0, localNameNodes.size());

        // Check local temp names
        List<String> localTempNames = renameVars.localTempNames;
        assertEquals(0, localTempNames.size());

        // Check assignments
        SortedMap<String, Assignment> assignments = renameVars.assignments;
        assertEquals(0, assignments.size());
    }

    @Test
    public void testProcessVarsWithPreserveAnonymousFunctionNames() {
        String prefix = "test_";
        boolean localRenamingOnly = false;
        boolean preserveAnonymousFunctionNames = true;
        boolean generatePseudoNames = false;
        VariableMap prevUsedRenameMap = null;
        char[] reservedCharacters = null;
        Set<String> reservedNames = new HashSet<>();

        RenameVars renameVars = new RenameVars(null, prefix, localRenamingOnly,
                preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap,
                reservedCharacters, reservedNames);

        Node externsNode = createNode("function testFunction() { function anonymousFunction() { return 1; } }");
        Node rootNode = createNode("function mainFunction() { testFunction(); }");

        renameVars.process(externsNode, rootNode);

        // Check global name nodes
        List<Node> globalNameNodes = renameVars.globalNameNodes;
        assertEquals(1, globalNameNodes.size());
        String newGlobalName = renameVars.getNewGlobalName(globalNameNodes.get(0));
        assertNotNull(newGlobalName);
        assertTrue(newGlobalName.contains("anonymousFunction"));

        // Check local name nodes
        List<Node> localNameNodes = renameVars.localNameNodes;
        assertEquals(0, localNameNodes.size());

        // Check local temp names
        List<String> localTempNames = renameVars.localTempNames;
        assertEquals(0, localTempNames.size());

        // Check assignments
        SortedMap<String, Assignment> assignments = renameVars.assignments;
        assertEquals(0, assignments.size());
    }

    @Test
    public void testProcessVarsWithGeneratePseudoNames() {
        String prefix = "test_";
        boolean localRenamingOnly = false;
        boolean preserveAnonymousFunctionNames = false;
        boolean generatePseudoNames = true;
        VariableMap prevUsedRenameMap = null;
        char[] reservedCharacters = null;
        Set<String> reservedNames = new HashSet<>();

        RenameVars renameVars = new RenameVars(null, prefix, localRenamingOnly,
                preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap,
                reservedCharacters, reservedNames);

        Node externsNode = createNode("function testFunction() { var a = 1; }");
        Node rootNode = createNode("function mainFunction() { testFunction(); }");

        renameVars.process(externsNode, rootNode);

        // Check global name nodes
        List<Node> globalNameNodes = renameVars.globalNameNodes;
        assertEquals(1, globalNameNodes.size());
        String newGlobalName = renameVars.getNewGlobalName(globalNameNodes.get(0));
        assertNotNull(newGlobalName);
        assertTrue(newGlobalName.contains("$"));

        // Check local name nodes
        List<Node> localNameNodes = renameVars.localNameNodes;
        assertEquals(0, localNameNodes.size());

        // Check local temp names
        List<String> localTempNames = renameVars.localTempNames;
        assertEquals(0, localTempNames.size());

        // Check assignments
        SortedMap<String, Assignment> assignments = renameVars.assignments;
        assertEquals(0, assignments.size());
    }

    private Node createNode(String code) {
        JsParser parser = new JsParser(Collections.emptyMap());
        AstNode ast = parser.parse(code, null);
        return (Node) ast.getFirstChild();
    }
}