package com.google.javascript.jscomp;
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
    public void testAddDeclaredName() {
        renamer.addDeclaredName("var");
        renamer.addDeclaredName("obj");
        renamer.addDeclaredName("func");
        renamer.addDeclaredName("exception");

        assertNotNull(renamer.getReplacementName("var"));
        assertNotNull(renamer.getReplacementName("obj"));
        assertNotNull(renamer.getReplacementName("func"));
        assertNotNull(renamer.getReplacementName("exception"));
    }

    @Test
    public void testGetReplacementName() {
        renamer.addDeclaredName("var");
        renamer.addDeclaredName("obj");
        renamer.addDeclaredName("func");
        renamer.addDeclaredName("exception");

        assertEquals("var$$0", renamer.getReplacementName("var"));
        assertEquals("obj$$0", renamer.getReplacementName("obj"));
        assertEquals("func$$0", renamer.getReplacementName("func"));
        assertEquals("exception$$0", renamer.getReplacementName("exception"));
    }

    @Test
    public void testStripConstIfReplaced() {
        renamer.addDeclaredName("const1");
        assertTrue(renamer.stripConstIfReplaced());
    }

    @Test
    public void testForChildScope() {
        MakeDeclaredNamesUnique.Renamer childRenamer = renamer.forChildScope();
        assertNotSame(renamer, childRenamer);
    }

}