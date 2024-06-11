package com.google.javascript.jscomp;

import com.google.common.base.Supplier;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MakeDeclaredNamesUniqueTest {
    private MakeDeclaredNamesUnique.Renamer renamer;

    @Before
    public void setUp() {
        renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
    }

    @Test
    public void testAddDeclaredName_HandlesEmptyName() {
        renamer.addDeclaredName("");
        assertFalse(renamer.getReplacementName("").isEmpty());
    }

    @Test
    public void testAddDeclaredName_AddsNameToDeclarations() {
        renamer.addDeclaredName("testName");
        assertTrue(renamer.getReplacementName("testName").startsWith("testName"));
    }

    @Test
    public void testGetReplacementName_ReturnsNullForUnknownName() {
        assertNull(renamer.getReplacementName("unknownName"));
    }

    @Test
    public void testStripConstIfReplaced_ReturnsFalse() {
        assertFalse(renamer.stripConstIfReplaced());
    }
}