package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {
    @Test
    public void testAddDeclaredName() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();

        renamer.addDeclaredName("foo");
        renamer.addDeclaredName("bar");
        renamer.addDeclaredName("foo");

        assertEquals("foo$$1", renamer.getReplacementName("foo"));
        assertEquals("bar$$1", renamer.getReplacementName("bar"));
    }

    @Test
    public void testRenameVars() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.InlineRenamer(() -> "0", "prefix", false);

        renamer.addDeclaredName("foo");
        renamer.addDeclaredName("foo");
        renamer.addDeclaredName("");

        assertEquals("foo$$prefix0", renamer.getReplacementName("foo"));
        assertEquals("", renamer.getReplacementName(""));
    }
}