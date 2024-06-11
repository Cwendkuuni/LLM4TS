package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.collect.Sets;

import java.util.*;

public class RenameVarsTest {

    @Test
    public void testProcessVarsExternsPass() {
        String prefix = "prefix_";
        RenameVars renamer = new RenameVars(null, prefix, false, false, false, null, null, null);

        Node externs = NodeFactory.createBlock();
        Node root = NodeFactory.createBlock();

        renamer.process(externs, root);

        Set<String> expectedExterns = Sets.newHashSet("extern1", "extern2");
        assertEquals(expectedExterns, renamer.externNames);
    }

    @Test
    public void testProcessVarsSourcePass() {
        String prefix = "prefix_";
        RenameVars renamer = new RenameVars(null, prefix, false, false, false, null, null, null);

        Node externs = NodeFactory.createBlock();
        Node root = NodeFactory.createBlock();

        root.addChild(NodeFactory.createName("extern1"));
        root.addChild(NodeFactory.createName("extern1"));
        root.addChild(NodeFactory.createName("local"));
        root.addChild(NodeFactory.createName("local"));
        root.addChild(NodeFactory.createName("global"));
        root.addChild(NodeFactory.createName("global"));

        NodeTraversal.traverse(renamer.compiler, externs, new RenameVars.ProcessVars(true));
        NodeTraversal.traverse(renamer.compiler, root, new RenameVars.ProcessVars(false));

        List<String> expectedGlobalNames = Arrays.asList("global1", "global2");
        List<String> expectedLocalNames = Arrays.asList("local1", "local2", "local3");

        assertEquals(expectedGlobalNames, getRenamedNames(renamer.globalNameNodes));
        assertEquals(expectedLocalNames, getRenamedNames(renamer.localNameNodes));
    }

    @Test
    public void testProcessVarsLocalRenamingOnly() {
        String prefix = "prefix_";
        RenameVars renamer = new RenameVars(null, prefix, true, false, false, null, null, null);

        Node externs = NodeFactory.createBlock();
        Node root = NodeFactory.createBlock();

        root.addChild(NodeFactory.createVar("var1", NodeFactory.createName("extern1")));
        root.addChild(NodeFactory.createVar("var2", NodeFactory.createName("extern1")));
        root.addChild(NodeFactory.createFunction(NodeFactory.createName("func"), Arrays.asList(), NodeFactory.createBlock()));
        root.addChild(NodeFactory.createFunction(NodeFactory.createName("func2"), Arrays.asList(), NodeFactory.createBlock()));
        root.addChild(NodeFactory.createBlock());
        root.addChild(NodeFactory.createBlock());

        NodeTraversal.traverse(renamer.compiler, externs, new RenameVars.ProcessVars(true));
        NodeTraversal.traverse(renamer.compiler, root, new RenameVars.ProcessVars(false));

        List<String> expectedLocalNames = Arrays.asList("L0", "L1", "L2", "L3", "L4");

        assertEquals(expectedLocalNames, getRenamedNames(renamer.localNameNodes));
    }

    @Test
    public void testProcessVarsPreserveAnonymousFunctionNames() {
        String prefix = "prefix_";
        RenameVars renamer = new RenameVars(null, prefix, false, true, false, null, null, null);

        Node externs = NodeFactory.createBlock();
        Node root = NodeFactory.createBlock();

        root.addChild(NodeFactory.createFunction(NodeFactory.createName("func"), Arrays.asList(), NodeFactory.createBlock()));
        root.addChild(NodeFactory.createFunction(NodeFactory.createName("func2"), Arrays.asList(), NodeFactory.createBlock()));
        root.addChild(NodeFactory.createName("global"));
        root.addChild(NodeFactory.createName("global"));

        NodeTraversal.traverse(renamer.compiler, externs, new RenameVars.ProcessVars(true));
        NodeTraversal.traverse(renamer.compiler, root, new RenameVars.ProcessVars(false));

        List<String> expectedGlobalNames = Arrays.asList("global1", "global2");
        List<String> expectedAnonymousFunctionNames = Arrays.asList("func$", "func2$");

        assertEquals(expectedGlobalNames, getRenamedNames(renamer.globalNameNodes));
        assertEquals(expectedAnonymousFunctionNames, getRenamedNames(renamer.anonymousFunctionNodes));
    }

    @Test
    public void testProcessVarsGeneratePseudoNames() {
        String prefix = "prefix_";
        RenameVars renamer = new RenameVars(null, prefix, false, false, true, null, null, null);

        Node externs = NodeFactory.createBlock();
        Node root = NodeFactory.createBlock();

        root.addChild(NodeFactory.createName("var1"));
        root.addChild(NodeFactory.createName("var2"));

        NodeTraversal.traverse(renamer.compiler, externs, new RenameVars.ProcessVars(true));
        NodeTraversal.traverse(renamer.compiler, root, new RenameVars.ProcessVars(false));

        List<String> expectedNames = Arrays.asList("$var1$", "$var2$");

        assertEquals(expectedNames, getRenamedNames(renamer.localNameNodes));
    }

    @Test
    public void testAssignNames() {
        String prefix = "prefix_";
        RenameVars renamer = new RenameVars(null, prefix, false, false, false, null, null, null);

        List<Assignment> assignmentsToRename = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            assignmentsToRename.add(new Assignment("var" + i, null));
        }

        renamer.assignNames(assignmentsToRename);

        List<String> expectedNames = Arrays.asList("var0", "var1", "var2", "var3", "var4", "var5", "var6", "var7", "var8", "var9");

        assertEquals(expectedNames, getNamesFromAssignments(assignmentsToRename));
    }

    @Test
    public void testAssignNamesReusePreviouslyUsedVariableMap() {
        String prefix = "prefix_";
        RenameVars renamer = new RenameVars(null, prefix, false, false, false, new VariableMap(Map.of("var1", "newName1")), null, null);

        List<Assignment> assignmentsToRename = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            assignmentsToRename.add(new Assignment("var" + i, null));
        }

        renamer.assignNames(assignmentsToRename);

        List<String> expectedNames = Arrays.asList("newName1", "var2", "var3", "var4", "var5", "var6", "var7", "var8", "var9");

        assertEquals(expectedNames, getNamesFromAssignments(assignmentsToRename));
    }

    private List<String> getRenamedNames(List<Node> nameNodes) {
        List<String> names = new ArrayList<>();
        for (Node node : nameNodes) {
            names.add(node.getString());
        }
        return names;
    }

    private List<String> getNamesFromAssignments(List<Assignment> assignments) {
        List<String> names = new ArrayList<>();
        for (Assignment assignment : assignments) {
            names.add(assignment.newName);
        }
        return names;
    }

    @Test
    public void testOkToRenameVar() {
        RenameVars renamer = new RenameVars(null, null, false, false, false, null, null, null);

        assertFalse(renamer.okToRenameVar("var_", false));
        assertFalse(renamer.okToRenameVar("extern", false));
        assertFalse(renamer.okToRenameVar("func", false));
        assertFalse(renamer.okToRenameVar("import", false));
        assertFalse(renamer.okToRenameVar("export", false));
        assertFalse(renamer.okToRenameVar("class", false));
        assertTrue(renamer.okToRenameVar("var", false));
        assertTrue(renamer.okToRenameVar("local", true));
    }
}