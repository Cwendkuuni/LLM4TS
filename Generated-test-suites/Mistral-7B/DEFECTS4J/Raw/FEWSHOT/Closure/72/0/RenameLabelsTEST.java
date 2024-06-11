package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.ScopedCallback;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class RenameLabelsTest {
    @Test
    public void testRenameLabels() {
        AbstractCompiler compiler = new Compiler();
        RenameLabels renameLabels = new RenameLabels(compiler);

        String testCode = "function test() {" +
                "  a: {" +
                "    b: break a;" +
                "  }" +
                "  c: {}" +
                "  d: {" +
                "    e: break d;" +
                "  }" +
                "}";

        Node externs = NodeUtil.compileToNode(testCode);
        renameLabels.process(externs, null);

        Node root = externs;
        Deque<LabelNamespace> namespaceStack = new ArrayList<>();
        ScopedCallback callback = new RenameLabels.ProcessLabels();
        NodeTraversal.traverse(compiler, root, callback);

        Map<String, LabelInfo> renameMap = callback.namespaceStack.peek().renameMap;

        List<String> expectedNames = Collections.nCopies(3, "a");
        List<String> actualNames = new ArrayList<>();

        for (Map.Entry<String, LabelInfo> entry : renameMap.entrySet()) {
            LabelInfo labelInfo = entry.getValue();
            String name = entry.getKey();
            int id = labelInfo.id;
            actualNames.add(id > 0 ? getNameForId(id - 1) : name);
        }

        assertEquals(expectedNames, actualNames);
    }

    private String getNameForId(int id) {
        String name;
        switch (id) {
            case 1:
                name = "a";
                break;
            case 2:
                name = "b";
                break;
            case 3:
                name = "c";
                break;
            default:
                throw new IllegalArgumentException("Invalid id: " + id);
        }
        return name;
    }

    @Test
    public void testRenameLabelsWithReferencedLabels() {
        AbstractCompiler compiler = new Compiler();
        RenameLabels renameLabels = new RenameLabels(compiler);

        String testCode = "function test() {" +
                "  a: {" +
                "    b: break a;" +
                "    c: {" +
                "      d: break d;" +
                "      e: break c;" +
                "      f: break a;" +
                "    }" +
                "  }" +
                "}";

        Node externs = NodeUtil.compileToNode(testCode);
        renameLabels.process(externs, null);

        Node root = externs;
        Deque<LabelNamespace> namespaceStack = new ArrayList<>();
        ScopedCallback callback = new RenameLabels.ProcessLabels();
        NodeTraversal.traverse(compiler, root, callback);

        Map<String, LabelInfo> renameMap = callback.namespaceStack.peek().renameMap;

        // The first label 'a' should have been referenced by labels 'b', 'c' and 'f'
        LabelInfo labelA = renameMap.get("a");
        assertTrue(labelA.referenced);

        // Label 'b' should not be referenced, as it only breaks to 'a'
        LabelInfo labelB = renameMap.get("b");
        assertFalse(labelB.referenced);

        // Label 'c' should have been referenced by labels 'd' and 'f'
        LabelInfo labelC = renameMap.get("c");
        assertTrue(labelC.referenced);

        // Label 'd' should have been referenced by label 'c'
        LabelInfo labelD = renameMap.get("d");
        assertTrue(labelD.referenced);

        // Label 'e' should not be referenced, as it only breaks to 'd'
        LabelInfo labelE = renameMap.get("e");
        assertFalse(labelE.referenced);

        // Label 'f' should have been referenced by label 'c'
        LabelInfo labelF = renameMap.get("f");
        assertTrue(labelF.referenced);
    }

    @Test
    public void testRenameLabelsWithUnusedLabels() {
        AbstractCompiler compiler = new Compiler();
        RenameLabels renameLabels = new RenameLabels(compiler, new UnusedNameSupplier(), false);

        String testCode = "function test() {" +
                "  a: {}" +
                "  b: {}" +
                "  c: {" +
                "    d: break a;" +
                "  }" +
                "}";

        Node externs = NodeUtil.compileToNode(testCode);
        renameLabels.process(externs, null);

        Node root = externs;
        Deque<LabelNamespace> namespaceStack = new ArrayList<>();
        ScopedCallback callback = new RenameLabels.ProcessLabels();
        NodeTraversal.traverse(compiler, root, callback);

        Map<String, LabelInfo> renameMap = callback.namespaceStack.peek().renameMap;

        // The label 'a' is not referenced, so it should be removed
        assertNull(renameMap.get("a"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRenameLabelsWithConflictingLabels() {
        AbstractCompiler compiler = new Compiler();
        RenameLabels renameLabels = new RenameLabels(compiler);

        String testCode = "function test() {" +
                "  a: {" +
                "    b: break a;" +
                "  }" +
                "  function a() {}" +
                "}";

        Node externs = NodeUtil.compileToNode(testCode);
        renameLabels.process(externs, null);
    }

    @Test
    public void testRenameLabelsWithLabelsInsideFunction() {
        AbstractCompiler compiler = new Compiler();
        RenameLabels renameLabels = new RenameLabels(compiler);

        String testCode = "function test() {" +
                "  function a() {" +
                "    a: {" +
                "      b: break a;" +
                "    }" +
                "  }" +
                "}";

        Node externs = NodeUtil.compileToNode(testCode);
        renameLabels.process(externs, null);

        Node root = externs;
        Deque<LabelNamespace> namespaceStack = new ArrayList<>();
        ScopedCallback callback = new RenameLabels.ProcessLabels();
        NodeTraversal.traverse(compiler, root, callback);

        Map<String, LabelInfo> renameMap = callback.namespaceStack.peek().renameMap;

        // The inner label 'a' should have a different name than the outer function 'a'
        LabelInfo labelA = renameMap.get("a");
        assertNotNull(labelA);
        assertNotSame("a", labelA.id > 0 ? getNameForId(labelA.id - 1) : labelA.id.toString());
    }
}

class UnusedNameSupplier implements Supplier<String> {
    private final HashSet<String> usedNames = Sets.newHashSet();

    @Override
    public String get() {
        String name = getFreshName();
        usedNames.add(name);
        return name;
    }

    private String getFreshName() {
        for (int i = 0; ; i++) {
            String name = "unusedLabel" + i;
            if (!usedNames.contains(name)) {
                return name;
            }
        }
    }
}