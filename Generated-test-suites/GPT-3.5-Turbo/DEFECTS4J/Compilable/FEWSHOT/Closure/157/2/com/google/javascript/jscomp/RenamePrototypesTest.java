package com.google.javascript.jscomp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class RenamePrototypesTest {
    
    private RenamePrototypes renamePrototypes;
    
    @Before
    public void setUp() {
        renamePrototypes = new RenamePrototypes(null, true, null, null);
    }
    
    @Test
    public void testProcess() {
        assertNull(renamePrototypes.getPropertyMap());
    }
}