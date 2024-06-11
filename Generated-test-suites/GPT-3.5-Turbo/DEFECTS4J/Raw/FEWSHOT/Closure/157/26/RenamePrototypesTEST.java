package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

public class RenamePrototypesTest {
    
    @Test
    public void testProcess() {
        RenamePrototypes rp = new RenamePrototypes(compiler, true, null, null);
        rp.process(externs, root);
        
        VariableMap map = rp.getPropertyMap();
        assertNotNull(map);
    }
    
    @Test
    public void testProcess_withReservedCharacters() {
        char[] reservedCharacters = {'a', 'b', 'c'};
        RenamePrototypes rp = new RenamePrototypes(compiler, true, reservedCharacters, null);
        rp.process(externs, root);
        
        VariableMap map = rp.getPropertyMap();
        assertNotNull(map);
    }
    
    @Test
    public void testProcess_withPrevUsedRenameMap() {
        VariableMap prevUsedRenameMap = new VariableMap();
        prevUsedRenameMap.addMapping("foo", "bar");
        
        RenamePrototypes rp = new RenamePrototypes(compiler, true, null, prevUsedRenameMap);
        rp.process(externs, root);
        
        VariableMap map = rp.getPropertyMap();
        assertNotNull(map);
    }
    
    @Test
    public void testProcess_withPrevUsedRenameMapAndReservedCharacters() {
        VariableMap prevUsedRenameMap = new VariableMap();
        prevUsedRenameMap.addMapping("foo", "bar");
        
        char[] reservedCharacters = {'a', 'b', 'c'};
        RenamePrototypes rp = new RenamePrototypes(compiler, true, reservedCharacters, prevUsedRenameMap);
        rp.process(externs, root);
        
        VariableMap map = rp.getPropertyMap();
        assertNotNull(map);
    }
}