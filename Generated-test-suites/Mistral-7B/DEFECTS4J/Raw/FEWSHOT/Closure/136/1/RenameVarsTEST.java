package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class RenameVarsTest {

    @Test
    public void testProcess() {
        AbstractCompiler compiler = MockCompiler.getInstance();
        String prefix = "test_";
        boolean localRenamingOnly = true;
        boolean preserveAnonymousFunctionNames = false;
        boolean generatePseudoNames = false;
        VariableMap prevUsedRenameMap = null;
        @Nullable char[] reservedCharacters = null;
        @Nullable Set<String> reservedNames = new HashSet<String>();

        RenameVars renameVars = new RenameVars(compiler, prefix, localRenamingOnly, preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap, reservedCharacters, reservedNames);

        // Test empty input
        Node externs = null;
        Node root = null;
        renameVars.process(externs, root);
        assertTrue(renameVars.assignments.isEmpty());

        // Test simple globals and locals
        String[] names = { "a", "b", "c" };
        Node externs1 = NodeOperations.createList(names.length);
        Node root1 = NodeOperations.createFunction("test", NodeOperations.createList("a", "b", "c"));

        renameVars.process(externs1, root1);
        List<Assignment> assignments1 = new ArrayList<>(renameVars.assignments.values());
        assertEquals(names.length, assignments1.size());
        for (int i = 0; i < names.length; i++) {
            Assignment a = assignments1.get(i);
            String oldName = a.oldName;
            String newName = a.newName;
            String expectedNewName = prefix + i;
            assertEquals(expectedNewName, newName);
            assertEquals(oldName, a.oldName);
            assertEquals(a.input, null);
            assertEquals(1, a.count);
            assertEquals(assignments1.get(i).orderOfOccurrence, i);
        }

        // Test globals and locals with same names
        String[] names2 = { "a", "a" };
        Node externs2 = NodeOperations.createList(names2.length);
        Node root2 = NodeOperations.createFunction("test", NodeOperations.createList("a", "a"));

        renameVars.process(externs2, root2);
        List<Assignment> assignments2 = new ArrayList<>(renameVars.assignments.values());
        assertEquals(names2.length, assignments2.size());
        for (int i = 0; i < names2.length; i++) {
            Assignment a = assignments2.get(i);
            String oldName = a.oldName;
            String newName = a.newName;
            String expectedNewName = prefix + i;
            assertEquals(expectedNewName, newName);
            assertEquals(oldName, a.oldName);
            assertEquals(null, a.input);
            assertEquals(2, a.count);
            assertEquals(assignments2.get(i).orderOfOccurrence, i);
        }

        // Test globals and locals with same names and prefix
        String[] names3 = { "test_a", "test_a" };
        Node externs3 = NodeOperations.createList(names3.length);
        Node root3 = NodeOperations.createFunction("test", NodeOperations.createList(names3));

        renameVars = new RenameVars(compiler, null, localRenamingOnly, preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap, reservedCharacters, reservedNames);
        renameVars.process(externs3, root3);
        List<Assignment> assignments3 = new ArrayList<>(renameVars.assignments.values());
        assertEquals(names3.length, assignments3.size());
        for (int i = 0; i < names3.length; i++) {
            Assignment a = assignments3.get(i);
            String oldName = a.oldName;
            String newName = a.newName;
            String expectedNewName = String.valueOf(i + 1);
            assertEquals(expectedNewName, newName);
            assertEquals(oldName, a.oldName);
            assertEquals(null, a.input);
            assertEquals(2, a.count);
            assertEquals(assignments3.get(i).orderOfOccurrence, i);
        }

        // Test preserving anonymous function names
        String[] names4 = { "anonymous", "a" };
        Node externs4 = NodeOperations.createList(names4.length);
        Node root4 = NodeOperations.createFunction("test", NodeOperations.createList("anonymous", NodeOperations.createAnonymousFunction()));

        renameVars = new RenameVars(compiler, prefix, localRenamingOnly, true, generatePseudoNames, prevUsedRenameMap, reservedCharacters, reservedNames);
        renameVars.process(externs4, root4);
        List<Assignment> assignments4 = new ArrayList<>(renameVars.assignments.values());
        assertEquals(names4.length, assignments4.size());
        for (int i = 0; i < names4.length; i++) {
            Assignment a = assignments4.get(i);
            String oldName = a.oldName;
            String newName = a.newName;
            String expectedNewName = i == 0 ? "anonymous" : prefix + i;
            assertEquals(expectedNewName, newName);
            assertEquals(oldName, a.oldName);
            assertEquals(null, a.input);
            assertEquals(1, a.count);
            assertEquals(assignments4.get(i).orderOfOccurrence, i);
        }

        // Test renaming locals only
        boolean localRenamingOnly2 = false;
        RenameVars renameVars2 = new RenameVars(compiler, prefix, localRenamingOnly2, preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap, reservedCharacters, reservedNames);
        Node root5 = NodeOperations.createFunction("test", NodeOperations.createList("a", "b", NodeOperations.createFunction("foo", NodeOperations.createList("c", "d"))));
        renameVars2.process(externs1, root5);
        List<Assignment> assignments5 = new ArrayList<>(renameVars2.assignments.values());
        assertEquals(names.length + 2, assignments5.size());
        for (int i = 0; i < names.length; i++) {
            Assignment a = assignments5.get(i);
            String oldName = a.oldName;
            String newName = a.newName;
            String expectedNewName = prefix + i;
            assertEquals(expectedNewName, newName);
            assertEquals(oldName, a.oldName);
            assertEquals(null, a.input);
            assertEquals(3, a.count);
            assertEquals(assignments5.get(i).orderOfOccurrence, i);
        }
        for (int i = names.length; i < assignments5.size(); i++) {
            Assignment a = assignments5.get(i);
            String oldName = a.oldName;
            String newName = a.newName;
            String expectedNewName = "L" + (i - names.length);
            assertEquals(expectedNewName, newName);
            assertEquals(oldName, a.oldName);
            assertTrue(a.input instanceof CompilerInput);
            assertEquals(1, a.count);
            assertEquals(assignments5.get(i).orderOfOccurrence, i);
        }
    }

    @Test
    public void testGetVariableMap() {
        AbstractCompiler compiler = MockCompiler.getInstance();
        String prefix = "test_";
        boolean localRenamingOnly = true;
        boolean preserveAnonymousFunctionNames = false;
        boolean generatePseudoNames = false;
        VariableMap prevUsedRenameMap = null;
        @Nullable char[] reservedCharacters = null;
        @Nullable Set<String> reservedNames = new HashSet<String>();

        RenameVars renameVars = new RenameVars(compiler, prefix, localRenamingOnly, preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap, reservedCharacters, reservedNames);
        Node externs = NodeOperations.createList();
        Node root = NodeOperations.createFunction("test", NodeOperations.createList("a", "b"));
        renameVars.process(externs, root);
        VariableMap variableMap = renameVars.getVariableMap();
        Map<String, String> expectedVariableMap = new HashMap<>();
        expectedVariableMap.put("a", prefix + "0");
        expectedVariableMap.put("b", prefix + "1");
        assertEquals(expectedVariableMap, variableMap);
    }
}

class MockCompiler implements Compiler {
    private static MockCompiler instance;

    private MockCompiler() {}

    public static MockCompiler getInstance() {
        if (instance == null) {
            instance = new MockCompiler();
        }
        return instance;
    }

    @Override
    public void reportCodeChange() {}

    @Override
    public void addToDebugLog(String message) {}

    @Override
    public CompilerInput getInput(Node node) {
        return new CompilerInput() {
            @Override
            public Node getNode() {
                return node;
            }
        };
    }
}

class NodeOperations {
    public static Node createList(int length) {
        Node node = new Node();
        node.setType(Token.LIST);
        for (int i = 0; i < length; i++) {
            node.addChild(new Node());
        }
        return node;
    }

    public static Node createFunction(String name, Node body) {
        Node node = new Node();
        node.setType(Token.FUNCTION);
        node.setString(name);
        node.setChild(0, body);
        return node;
    }

    public static Node createAnonymousFunction() {
        Node node = new Node();
        node.setType(Token.FUNCTION);
        node.setString("anonymous");
        return node;
    }
}

class CompilerInput {
    Node node;

    public Node getNode() {
        return node;
    }
}

class Assignment {
    String oldName;
    int orderOfOccurrence;
    String newName;
    int count;

    public Assignment(String name, CompilerInput input) {
        oldName = name;
        orderOfOccurrence = 0;
        newName = null;
        count = 0;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}

class VariableMap {
    Map<String, String> map;

    VariableMap(Map<String, String> map) {
        this.map = map;
    }
}