package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.CodeConsumer;
import com.google.javascript.rhino.Node;

public class CodeConsumerTest {

    @Test
    public void testShouldPreserveExtraBlocks() {
        CodeConsumer consumer = new StubCodeConsumer();
        assertTrue(consumer.shouldPreserveExtraBlocks());
        consumer = new AnotherStubCodeConsumer();
        assertFalse(consumer.shouldPreserveExtraBlocks());
    }

    @Test
    public void testBreakAfterBlockFor() {
        CodeConsumer consumer = new StubCodeConsumer();
        Node node = Node.newIntLiteral(1);
        assertTrue(consumer.breakAfterBlockFor(node, true));
        assertTrue(consumer.breakAfterBlockFor(node, false));

        node = Node.newLiteral("hello");
        assertFalse(consumer.breakAfterBlockFor(node, true));
        assertTrue(consumer.breakAfterBlockFor(node, false));
    }

    private static class StubCodeConsumer extends CodeConsumer {
        @Override
        boolean shouldPreserveExtraBlocks() {
            return true;
        }

        @Override
        boolean breakAfterBlockFor(Node n, boolean statementContext) {
            return true;
        }
    }

    private static class AnotherStubCodeConsumer extends CodeConsumer {
        @Override
        boolean shouldPreserveExtraBlocks() {
            return false;
        }

        @Override
        boolean breakAfterBlockFor(Node n, boolean statementContext) {
            return true;
        }
    }
}