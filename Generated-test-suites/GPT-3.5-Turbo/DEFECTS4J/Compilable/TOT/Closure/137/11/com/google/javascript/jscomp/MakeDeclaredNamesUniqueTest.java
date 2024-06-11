package com.google.javascript.jscomp;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MakeDeclaredNamesUniqueTest {
    private MakeDeclaredNamesUnique.Renamer renamer;

    @Before
    public void setUp() {
        this.renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
    }

    @Test
    public void testAddDeclaredName() {
        renamer.addDeclaredName("name");
        String replacementName = renamer.getReplacementName("name");

        assertEquals("name", replacementName);
    }

    @Test
    public void testGetReplacementName() {
        renamer.addDeclaredName("name");
        String replacementName = renamer.getReplacementName("name");

        assertEquals("name", replacementName);
    }

    @Test
    public void testGetReplacementName_NotDeclared() {
        String replacementName = renamer.getReplacementName("name");

        assertNull(replacementName);
    }

    @Test
    public void testForChildScope() {
        MakeDeclaredNamesUnique.Renamer childRenamer = renamer.forChildScope();

        assertNotSame(renamer, childRenamer);
    }

    @Test
    public void testStripConstIfReplaced() {
        assertFalse(renamer.stripConstIfReplaced());
    }
}