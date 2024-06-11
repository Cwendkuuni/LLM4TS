package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RenameVarsTest {

    @Test
    public void testProcessVars() {
        RenameVars renameVars = new RenameVars(null, null, false, false, false, null, null, null);
        Node root = createNode("function foo() { var a = 1; var b = 2; }");
        Node externs = createNode("var c = 1;");

        renameVars.process(externs, root);

        List<Node> globalNameNodes = renameVars.globalNameNodes;
        List<Node> localNameNodes = renameVars.localNameNodes;
        List<String> localTempNames = renameVars.localTempNames;

        assertEquals(0, globalNameNodes.size());
        assertEquals(1, localNameNodes.size());
        assertEquals(2, localTempNames.size());

        Node localNode = localNameNodes.get(0);
        assertEquals("a", localNode.getString());
        assertNull(renameVars.renameMap.get("a"));

        Node tempNode = localNameNodes.get(1);
        assertEquals("L 0", tempNode.getString());
        assertEquals("a", renameVars.renameMap.get("a"));

        Node tempNode2 = localNameNodes.get(1);
        assertEquals("L 1", tempNode2.getString());
        assertEquals("b", renameVars.renameMap.get("b"));
    }

    @Test
    public void testProcessVarsWithExterns() {
        RenameVars renameVars = new RenameVars(null, null, false, false, false, null, null, null);
        Node root = createNode("function foo() { var a = 1; var b = 2; } var c = 1;");

        renameVars.process(null, root);

        List<Node> globalNameNodes = renameVars.globalNameNodes;
        List<Node> localNameNodes = renameVars.localNameNodes;
        List<String> localTempNames = renameVars.localTempNames;

        assertEquals(1, globalNameNodes.size());
        assertEquals(0, localNameNodes.size());
        assertEquals(0, localTempNames.size());

        Node externNode = globalNameNodes.get(0);
        assertEquals("c", externNode.getString());
        assertEquals("c", renameVars.renameMap.get("c"));
    }

    @Test
    public void testProcessVarsWithLocalRenamingOnly() {
        RenameVars renameVars = new RenameVars(null, null, true, false, false, null, null, null);
        Node root = createNode("function foo() { var a = 1; var b = 2; }");
        Node externs = createNode("var c = 1;");

        renameVars.process(externs, root);

        List<Node> globalNameNodes = renameVars.globalNameNodes;
        List<Node> localNameNodes = renameVars.localNameNodes;
        List<String> localTempNames = renameVars.localTempNames;

        assertEquals(0, globalNameNodes.size());
        assertEquals(2, localNameNodes.size());
        assertEquals(2, localTempNames.size());

        Node aNode = localNameNodes.get(0);
        assertEquals("L 0", aNode.getString());
        assertEquals("a", renameVars.renameMap.get("a"));

        Node bNode = localNameNodes.get(1);
        assertEquals("L 1", bNode.getString());
        assertEquals("b", renameVars.renameMap.get("b"));

        assertNull(renameVars.renameMap.get("c"));
    }

    @Test
    public void testProcessVarsWithPreserveAnonymousFunctionNames() {
        RenameVars renameVars = new RenameVars(null, null, false, true, false, null, null, null);
        Node root = createNode("function foo() { function bar() {} }");

        renameVars.process(null, root);

        List<Node> globalNameNodes = renameVars.globalNameNodes;
        List<Node> localNameNodes = renameVars.localNameNodes;
        List<String> localTempNames = renameVars.localTempNames;

        assertEquals(0, globalNameNodes.size());
        assertEquals(0, localNameNodes.size());
        assertEquals(0, localTempNames.size());

        Node anonymousFunctionNode = localNameNodes.get(0);
        assertEquals("bar", anonymousFunctionNode.getString());
        assertEquals("bar", renameVars.renameMap.get("bar"));
    }

    @Test
    public void testProcessVarsWithGeneratePseudoNames() {
        RenameVars renameVars = new RenameVars(null, null, false, false, true, null, null, null);
        Node root = createNode("function foo() { var a = 1; }");

        renameVars.process(null, root);

        List<Node> globalNameNodes = renameVars.globalNameNodes;
        List<Node> localNameNodes = renameVars.localNameNodes;
        List<String> localTempNames = renameVars.localTempNames;

        assertEquals(0, globalNameNodes.size());
        assertEquals(1, localNameNodes.size());
        assertEquals(1, localTempNames.size());

        Node localNode = localNameNodes.get(0);
        assertEquals("$a$$", localNode.getString());
        assertEquals("a", renameVars.renameMap.get("a"));
    }

    @Test
    public void testProcessVarsWithReservedNames() {
        RenameVars renameVars = new RenameVars(null, null, false, false, false, null, null, new HashSet<String>("if,else"));
        Node root = createNode("function foo() { if (true) {} else {} }");

        renameVars.process(null, root);

        List<Node> globalNameNodes = renameVars.globalNameNodes;
        List<Node> localNameNodes = renameVars.localNameNodes;
        List<String> localTempNames = renameVars.localTempNames;

        assertEquals(0, globalNameNodes.size());
        assertEquals(0, localNameNodes.size());
        assertEquals(0, localTempNames.size());

        assertEquals("if", renameVars.renameMap.get("if"));
        assertEquals("else", renameVars.renameMap.get("else"));
    }

    @Test
    public void testProcessVarsWithReservedCharacters() {
        RenameVars renameVars = new RenameVars(null, null, false, false, false, null, new char[]{'.'}, null);
        Node root = createNode("function foo() { var a.b = 1; }");

        renameVars.process(null, root);

        List<Node> globalNameNodes = renameVars.globalNameNodes;
        List<Node> localNameNodes = renameVars.localNameNodes;
        List<String> localTempNames = renameVars.localTempNames;

        assertEquals(0, globalNameNodes.size());
        assertEquals(1, localNameNodes.size());
        assertEquals(1, localTempNames.size());

        Node localNode = localNameNodes.get(0);
        assertEquals("a_b", localNode.getString());
        assertEquals("a.b", renameVars.renameMap.get("a.b"));
    }

    @Test
    public void testProcessVarsWithPrevUsedRenameMap() {
        RenameVars renameVars = new RenameVars(null, null, false, false, false, new VariableMap(Map.of("a", "x")), null, null);
        Node root = createNode("function foo() { var a = 1; var b = 2; }");

        renameVars.process(null, root);

        List<Node> globalNameNodes = renameVars.globalNameNodes;
        List<Node> localNameNodes = renameVars.localNameNodes;
        List<String> localTempNames = renameVars.localTempNames;

        assertEquals(0, globalNameNodes.size());
        assertEquals(2, localNameNodes.size());
        assertEquals(2, localTempNames.size());

        Node aNode = localNameNodes.get(0);
        assertEquals("L 0", aNode.getString());
        assertEquals("x", renameVars.renameMap.get("a"));

        Node bNode = localNameNodes.get(1);
        assertEquals("L 1", bNode.getString());
        assertEquals("b", renameVars.renameMap.get("b"));
    }

    private Node createNode(String code) {
        CompilerContext compilerContext = new CompilerContext(null, null, null);
        AbstractCompiler abstractCompiler = new Compiler();
        Node node = abstractCompiler.compile(compilerContext, code, null, null, null, null);
        return node;
    }

    private static class VariableMap implements Map<String, String> {
        private final Map<String, String> map;

        VariableMap(Map<String, String> map) {
            this.map = map;
        }

        @Override
        public int size() {
            return map.size();
        }

        @Override
        public boolean isEmpty() {
            return map.isEmpty();
        }

        @Override
        public boolean containsKey(Object key) {
            return map.containsKey(key);
        }

        @Override
        public String get(Object key) {
            return map.get(key);
        }

        @Override
        public String put(String key, String value) {
            return map.put(key, value);
        }

        @Override
        public String remove(Object key) {
            return map.remove(key);
        }

        @Override
        public void putAll(Map<? extends String, ? extends String> m) {
            map.putAll(m);
        }

        @Override
        public String getOrDefault(Object key, String defaultValue) {
            return map.getOrDefault(key, defaultValue);
        }

        @Override
        public String putIfAbsent(String key, String value) {
            return map.putIfAbsent(key, value);
        }

        @Override
        public boolean remove(Object key, Object value) {
            return map.remove(key, value);
        }

        @Override
        public boolean containsValue(Object value) {
            return map.containsValue(value);
        }

        @Override
        public Set<String> keySet() {
            return map.keySet();
        }

        @Override
        public Collection<String> values() {
            return map.values();
        }

        @Override
        public Set<Entry<String, String>> entrySet() {
            return map.entrySet();
        }
    }
}