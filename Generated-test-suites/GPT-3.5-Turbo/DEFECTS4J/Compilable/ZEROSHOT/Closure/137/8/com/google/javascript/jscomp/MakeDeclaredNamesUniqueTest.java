package com.google.javascript.jscomp;
import org.junit.Assert;
import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {

    @Test
    public void testEnterScope() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        renamer.addDeclaredName("name1");
        renamer.addDeclaredName("name2");
        Assert.assertEquals("name1", renamer.getReplacementName("name1"));
        Assert.assertEquals("name2", renamer.getReplacementName("name2"));
        Assert.assertNull(renamer.getReplacementName("name3"));
    }

    @Test
    public void testExitScope() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        renamer.addDeclaredName("name1");
        renamer.addDeclaredName("name2");
        MakeDeclaredNamesUnique.Renamer childRenamer = renamer.forChildScope();
        childRenamer.addDeclaredName("name3");
        childRenamer.addDeclaredName("name4");
        Assert.assertEquals("name1", renamer.getReplacementName("name1"));
        Assert.assertEquals("name2", renamer.getReplacementName("name2"));
        Assert.assertEquals("name3", childRenamer.getReplacementName("name3"));
        Assert.assertEquals("name4", childRenamer.getReplacementName("name4"));
        Assert.assertNull(childRenamer.getReplacementName("name1"));
        Assert.assertNull(childRenamer.getReplacementName("name2"));
    }

    @Test
    public void testGetReplacementName() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        renamer.addDeclaredName("name1");
        renamer.addDeclaredName("name2");
        Assert.assertEquals("name1$$0", renamer.getReplacementName("name1"));
        Assert.assertEquals("name2$$0", renamer.getReplacementName("name2"));
        Assert.assertNull(renamer.getReplacementName("name3"));
    }

    @Test
    public void testStripConstIfReplaced() {
        MakeDeclaredNamesUnique.Renamer renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
        Assert.assertFalse(renamer.stripConstIfReplaced());
    }
}