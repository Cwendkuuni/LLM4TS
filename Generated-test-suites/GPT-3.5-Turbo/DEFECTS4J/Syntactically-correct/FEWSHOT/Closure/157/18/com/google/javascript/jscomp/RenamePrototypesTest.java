package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class RenamePrototypesTest {
    
    private Compiler compiler;
    private RenamePrototypes renamePrototypes;
    
    @Before
    public void setUp() {
        compiler = new Compiler();
        renamePrototypes = new RenamePrototypes(compiler, true, null, null);
    }

    @Test
    public void testProcess() {
        Node externs = new Node(Token.EXTERN_RESULT);
        Node root = new Node(Token.ROOT);
        renamePrototypes.process(externs, root);
        
        VariableMap propertyMap = renamePrototypes.getPropertyMap();
        assertEquals(0, propertyMap.getSize());
        assertEquals(LifeCycleStage.NORMALIZED_OBFUSCATED, compiler.getLifeCycleStage());
    }
}