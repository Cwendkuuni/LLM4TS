package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class RemoveConstantExpressionsTest {

    private static Compiler compiler;
    private static Node externs;
    private static Node root;

    @BeforeClass
    public static void setUp() {
        compiler = new Compiler();
        externs = new Node(Token.ROOT);
        root = new Node(Token.ROOT);
    }

    @Test
    public void testProcess() {
        // TODO: Add test case
    }

    @Test
    public void testRemoveConstantRValuesCallback() {
        // TODO: Add test case
    }

    @Test
    public void testVisit() {
        // TODO: Add test case
    }

    @Test
    public void testTrySimplify() {
        // TODO: Add test case
    }

    @Test
    public void testGetSideEffectNodes() {
        // TODO: Add test case
    }

    @Test
    public void testGetResult() {
        // TODO: Add test case
    }
}