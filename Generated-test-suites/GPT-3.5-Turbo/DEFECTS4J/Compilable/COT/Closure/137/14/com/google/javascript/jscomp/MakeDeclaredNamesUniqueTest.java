package com.google.javascript.jscomp;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {
    
    private MakeDeclaredNamesUnique.Renamer renamer;
    
    @Before
    public void setUp() {
        renamer = new MakeDeclaredNamesUnique.ContextualRenamer();
    }
    
    @Test
    public void testAddDeclaredName() {
        renamer.addDeclaredName("name");
        assertEquals("name$$0", renamer.getReplacementName("name"));
    }
    
    @Test
    public void testGetReplacementName() {
        renamer.addDeclaredName("name");
        renamer.addDeclaredName("name");
        assertEquals("name$$0", renamer.getReplacementName("name"));
    }
    
    @Test
    public void testForChildScope() {
        MakeDeclaredNamesUnique.Renamer childRenamer = renamer.forChildScope();
        childRenamer.addDeclaredName("name");
        assertEquals("name$$0", childRenamer.getReplacementName("name"));
    }
    
    @Test
    public void testStripConstIfReplaced() {
        assertEquals(false, renamer.stripConstIfReplaced());
    }
    
}