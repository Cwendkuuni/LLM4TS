package com.google.javascript.jscomp;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.base.Supplier;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.ScopedCallback;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class RenameLabelsTest {

    @Test
    public void testProcessLabels() {
        AbstractCompiler compiler = new Compiler();
        RenameLabels renameLabels = new RenameLabels(compiler);

        String[] input = {
                "function bar() {",
                "  a: {",
                "    b: {",
                "      foo();",
                "    }",
                "  }",
                "  a: {",
                "    b: break a;",
                "  }",
                "}"};

        Node externs = NodeUtil.createExterns(input);
        Node root = NodeUtil.createRoot(input);

        renameLabels.process(externs, root);

        String[] expectedOutput = {
                "function bar() {",
                "  " + RenameLabelsTest.getNameForId(1) + ": {",
                "    " + RenameLabelsTest.getNameForId(2) + ": {",
                "      foo();",
                "    }",
                "  }",
                "  " + RenameLabelsTest.getNameForId(1) + ": {",
                "    " + RenameLabelsTest.getNameForId(2) + ": break " + RenameLabelsTest.getNameForId(1) + ";",
                "  }",
                "}"};

        Node actualOutput = NodeUtil.getRoot(compiler.getTranspiledOutput());
        NodeUtil.assertNodesEqual(expectedOutput, actualOutput);
    }

    @Test
    public void testStripLeadingHyphens() {
        assertEquals("a", Util.stripLeadingHyphens("--a"));
        assertEquals("--a", Util.stripLeadingHyphens("---a"));
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testGetNameForId() {
        assertEquals("a", getNameForId(1));
        assertEquals("b", getNameForId(2));
        assertEquals("c", getNameForId(3));
    }

    @Test
    public void testGetLabelInfo() {
        LabelNamespace labelNamespace = new LabelNamespace();
        LabelInfo labelInfo1 = new LabelInfo(1);
        LabelInfo labelInfo2 = new LabelInfo(2);
        labelNamespace.renameMap.put("a", labelInfo1);
        labelNamespace.renameMap.put("b", labelInfo2);

        LabelInfo actualLabelInfo = getLabelInfo("a");
        LabelInfo expectedLabelInfo = labelInfo1;
        assertEquals(expectedLabelInfo.id, actualLabelInfo.id);

        actualLabelInfo = getLabelInfo("b");
        expectedLabelInfo = labelInfo2;
        assertEquals(expectedLabelInfo.id, actualLabelInfo.id);

        assertNull(getLabelInfo("c"));
    }

    private static String getNameForId(int id) {
        return new ArrayList<String>(RenameLabelsTest.names.subList(0, id))
                .get(id - 1);
    }

    private static LabelInfo getLabelInfo(String name) {
        return new LabelNamespace().renameMap.get(name);
    }

    private static class LabelNamespace {
        final Map<String, LabelInfo> renameMap = new HashMap<String, LabelInfo>();
    }

    private static class LabelInfo {
        boolean referenced = false;
        final int id;

        LabelInfo(int id) {
            this.id = id;
        }
    }

    private static ArrayList<String> names = Lists.newArrayList();

    static {
        for (int i = 0; i < 10; i++) {
            names.add("label" + i);
        }
    }

    static Supplier<String> nameSupplier = new Supplier<String>() {
        @Override
        public String get() {
            return names.remove(0);
        }
    };

    static class Util {
        static final String[] EMPTY_STRING_ARRAY = {};

        static String stripLeadingHyphens(final String str) {
            if (str == null) {
                return null;
            }
            if (str.startsWith("--")) {
                return str.substring(2);
            }
            if (str.startsWith("-")) {
                return str.substring(1);
            }

            return str;
        }

        static String stripLeadingAndTrailingQuotes(String str) {
            final int length = str.length();
            if (length > 1 && str.startsWith("\"") && str.endsWith("\"")) {
                str = str.substring(1, length - 1);
            }

            return str;
        }
    }
}