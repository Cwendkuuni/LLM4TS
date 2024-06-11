package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RenameVarsTest {

    @Test
    public void testProcessVarsExternsPass() {
        String prefix = null;
        boolean localRenamingOnly = false;
        boolean preserveAnonymousFunctionNames = false;
        boolean generatePseudoNames = false;
        Set<String> reservedNames = new HashSet<>();
        VariableMap prevUsedRenameMap = null;
        char[] reservedCharacters = null;

        RenameVars renameVars = new RenameVars(null, prefix, localRenamingOnly, preserveAnonymousFunctionNames, generatePseudoNames, prevUsedRenameMap, reservedCharacters, reservedNames);

        List<String> inputNames = Arrays.asList("a", "b", "c", "d", "e");
        List<Node> nodes = new ArrayList<>();

        for (String name : inputNames) {
            Node node = new Node();
            node.setString(name);
            nodes.add(node);
        }

        renameVars.process(null, null);

        for (String name : inputNames) {
            Assignment assignment = renameVars.assignments.get(name);
            assertTrue(assignment != null);
            assertTrue(assignment.newName != null);
            String newName = assignment.newName;
            assertTrue(newName != null);
            assertTrue(newName.length() > 0);
            assertFalse(reservedNames.contains(newName));
        }
    }

    @Test
    public void testProcessVarsSourcePass() {
        String prefix = null;
        boolean localRenamingOnly = false;
        boolean preserveAnonymousFunctionNames = false;
        boolean generatePseudoNames = false;
        Set<String> externNames = new HashSet<>();
        Set<String> reservedNames = new HashSet<>();
        VariableMap prevUsedRenameMap = null;
        char[] reservedCharacters = null;

        RenameVars renameVars = new RenameVars(null, prefix, localRenamingOnly, preserveAnonymousFunctionNames, generatePseudoNames, externNames, reservedNames, prevUsedRenameMap, reservedCharacters);

        List<String> inputNames = Arrays.asList("a", "b", "c", "d", "e");
        List<Node> globals = new ArrayList<>();
        List<Node> locals = new ArrayList<>();

        for (String name : inputNames) {
            Node node = new Node();
            node.setString(name);
            Node parent = new Node();
            parent.setType(Token.BLOCK);
            if (Math.random() < 0.5) {
                globals.add(node);
            } else {
                locals.add(node);
            }
            node.setParentNode(parent);
        }

        renameVars.process(new Node(), new Node());

        for (Node node : globals) {
            String oldName = node.getString();
            Assignment assignment = renameVars.assignments.get(oldName);
            assertTrue(assignment != null);
            assertTrue(assignment.newName != null);
            String newName = assignment.newName;
            assertTrue(newName != null);
            assertTrue(newName.length() > 0);
            assertTrue(newName.charAt(0) == '_');
            assertTrue(reservedNames.contains(newName));
        }

        for (Node node : locals) {
            String oldName = node.getString();
            Assignment assignment = renameVars.assignments.get(oldName);
            assertTrue(assignment != null);
            assertTrue(assignment.newName != null);
            String newName = assignment.newName;
            assertTrue(newName != null);
            assertTrue(newName.length() > 0);
            assertFalse(newName.charAt(0) == '_');
            assertTrue(reservedNames.contains(newName));
        }
    }

    @Test
    public void testGetNewGlobalName() {
        String prefix = null;
        boolean localRenamingOnly = false;
        boolean preserveAnonymousFunctionNames = false;
        boolean generatePseudoNames = false;
        Set<String> externNames = new HashSet<>();
        Set<String> reservedNames = new HashSet<>();
        VariableMap prevUsedRenameMap = null;
        char[] reservedCharacters = null;

        RenameVars renameVars = new RenameVars(null, prefix, localRenamingOnly, preserveAnonymousFunctionNames, generatePseudoNames, externNames, reservedNames, prevUsedRenameMap, reservedCharacters);

        List<String> inputNames = Arrays.asList("a", "b", "c", "d", "e");
        List<Node> globals = new ArrayList<>();

        for (String name : inputNames) {
            Node node = new Node();
            node.setString(name);
            globals.add(node);
        }

        renameVars.process(new Node(), new Node());

        for (Node node : globals) {
            String oldName = node.getString();
            String newName = renameVars.getNewGlobalName(node);
            assertTrue(newName != null);
            assertTrue(newName.length() > 0);
            assertTrue(reservedNames.contains(newName));
        }
    }

    @Test
    public void testGetNewLocalName() {
        String prefix = null;
        boolean localRenamingOnly = false;
        boolean preserveAnonymousFunctionNames = false;
        boolean generatePseudoNames = false;
        Set<String> externNames = new HashSet<>();
        Set<String> reservedNames = new HashSet<>();
        VariableMap prevUsedRenameMap = null;
        char[] reservedCharacters = null;

        RenameVars renameVars = new RenameVars(null, prefix, localRenamingOnly, preserveAnonymousFunctionNames, generatePseudoNames, externNames, reservedNames, prevUsedRenameMap, reservedCharacters);

        List<String> inputNames = Arrays.asList("a", "b", "c", "d", "e");
        List<Node> locals = new ArrayList<>();
        int index = 0;

        for (String name : inputNames) {
            Node node = new Node();
            node.setString(name);
            locals.add(node);
        }

        renameVars.process(new Node(), new Node());

        for (Node node : locals) {
            String oldTempName = renameVars.localTempNames.get(index);
            String newName = renameVars.getNewLocalName(node, index);
            assertTrue(newName != null);
            assertTrue(newName.length() > 0);
            assertFalse(newName.charAt(0) == '_');
            assertFalse(reservedNames.contains(newName));
            index++;
        }
    }
}