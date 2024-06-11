package com.google.javascript.jscomp;
import org.junit.Test;
import static org.junit.Assert.*;

public class MakeDeclaredNamesUniqueTest {

    @Test
    public void testEnterScope() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        renamer.addDeclaredName("x");
        renamer.addDeclaredName("y");
        String newNameX = renamer.getReplacementName("x");
        String newNameY = renamer.getReplacementName("y");
        assertEquals("x$$0", newNameX);
        assertEquals("y$$0", newNameY);
    }

    @Test
    public void testGetReplacementName() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        renamer.addDeclaredName("x");
        renamer.addDeclaredName("y");
        String newNameX = renamer.getReplacementName("x");
        String newNameY = renamer.getReplacementName("y");
        assertEquals("x$$0", newNameX);
        assertEquals("y$$0", newNameY);
    }

    @Test
    public void testShouldTraverse() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        renamer.addDeclaredName("x");
        renamer.addDeclaredName("y");
        String newNameX = renamer.getReplacementName("x");
        String newNameY = renamer.getReplacementName("y");
        assertEquals("x$$0", newNameX);
        assertEquals("y$$0", newNameY);
    }

    @Test
    public void testExitScope() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        renamer.addDeclaredName("x");
        renamer.addDeclaredName("y");
        String newNameX = renamer.getReplacementName("x");
        String newNameY = renamer.getReplacementName("y");
        assertEquals("x$$0", newNameX);
        assertEquals("y$$0", newNameY);
    }

    @Test
    public void testVisit() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        renamer.addDeclaredName("x");
        renamer.addDeclaredName("y");
        String newNameX = renamer.getReplacementName("x");
        String newNameY = renamer.getReplacementName("y");
        assertEquals("x$$0", newNameX);
        assertEquals("y$$0", newNameY);
    }

}