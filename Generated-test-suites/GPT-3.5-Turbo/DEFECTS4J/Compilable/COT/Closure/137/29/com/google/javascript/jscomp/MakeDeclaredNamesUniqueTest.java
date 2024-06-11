package com.google.javascript.jscomp;

import org.junit.Assert;
import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {

    @Test
    public void testEnterScope() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        renamer.addDeclaredName("name");
        Assert.assertEquals("name", renamer.getReplacementName("name"));
    }

    @Test
    public void testExitScope() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        renamer.addDeclaredName("name");
        renamer.addDeclaredName("name");
        renamer.addDeclaredName("name");
        renamer.addDeclaredName("name");
        renamer.addDeclaredName("name");
        Assert.assertEquals("name$$3", renamer.getReplacementName("name"));
    }

    @Test
    public void testShouldTraverse() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        String name = "name";
        renamer.addDeclaredName(name);
        String newName = renamer.getReplacementName(name);
        Assert.assertNotEquals(newName, name);
        Assert.assertEquals(name, MakeDeclaredNamesUnique.ContextualRenameInverter.getOrginalName(newName));
    }

    @Test
    public void testVisit() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        String name = "name";
        renamer.addDeclaredName(name);
        renamer.addDeclaredName(name);
        String newName = renamer.getReplacementName(name);
        Assert.assertNotEquals(newName, name);
        Assert.assertEquals(name, MakeDeclaredNamesUnique.ContextualRenameInverter.getOrginalName(newName));
    }

    @Test
    public void testContextualRenameInverter() {
        Assert.fail("Not implemented");
    }

    @Test
    public void testContextualRenamer() {
        Assert.fail("Not implemented");
    }

    @Test
    public void testInlineRenamer() {
        Assert.fail("Not implemented");
    }
}
