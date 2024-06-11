package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class RenamePrototypesTest {
    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean aggressiveRenaming = true;
        char[] reservedCharacters = {'_', '-'};
        VariableMap prevUsedRenameMap = new VariableMap();
        RenamePrototypes rp = new RenamePrototypes(compiler, aggressiveRenaming, reservedCharacters, prevUsedRenameMap);

        // TODO: Add test cases here

        //assertEquals(expected, actual);
        //assertNull(obj);
    }
}