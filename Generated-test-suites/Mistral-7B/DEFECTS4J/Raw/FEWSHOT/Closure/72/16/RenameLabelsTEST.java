package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Supplier;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {
    @Test
    public void testRenameLabels() {
        AbstractCompiler compiler = new Compiler();
        RenameLabels renameLabels = new RenameLabels(compiler);
        String oldCode = "function bar() {\n" +
                "  a: {\n" +
                "    b: {\n" +
                "      foo();\n" +
                "    }\n" +
                "  }\n" +
                "  a: {\n" +
                "    b: break a;\n" +
                "  }\n" +
                "}";

        Node root = Parser.parseCompilationUnit(oldCode);
        renameLabels.process(null, root);

        Deque<Map<String, LabelInfo>> namespaceStack = renameLabels.getProcessLabels().namespaceStack;
        LabelNamespace current = namespaceStack.peek();
        assertEquals(2, current.renameMap.size());
        assertEquals("a", current.renameMap.get("a").getName());
        assertEquals("b", current.renameMap.get("b").getName());

        String newCode = Printer.printNode(NodePrinterPosition.TOP_LEVEL, root);
        assertEquals("function bar() {\n" +
                "  c: {\n" +
                "    d: {\n" +
                "      foo();\n" +
                "    }\n" +
                "  }\n" +
                "  c: {\n" +
                "    d: break c;\n" +
                "  }\n" +
                "}", newCode);

        oldCode = "function foo() {\n" +
                "  a: {\n" +
                "    break a;\n" +
                "  }\n" +
                "}";

        root = Parser.parseCompilationUnit(oldCode);
        renameLabels.process(null, root);

        namespaceStack = renameLabels.getProcessLabels().namespaceStack;
        current = namespaceStack.peek();
        assertEquals(1, current.renameMap.size());
        assertNull(current.renameMap.get("a"));

        newCode = Printer.printNode(NodePrinterPosition.TOP_LEVEL, root);
        assertEquals("function foo() {\n" +
                "}", newCode);
    }
}

final class Compiler {
    // Empty implementation for testing purposes
}

class ProcessLabels extends RenameLabels.ProcessLabels {
    RenameLabels getProcessLabels() {
        return (RenameLabels) compiler;
    }
}