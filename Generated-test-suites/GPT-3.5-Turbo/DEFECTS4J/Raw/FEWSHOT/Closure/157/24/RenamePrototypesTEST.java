package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

public class RenamePrototypesTest {

    private static Compiler compiler;
    private static CompilerInput input;
    private static char[] reservedCharacters;
    private static VariableMap prevUsedRenameMap;

    @BeforeClass
    public static void setUp() {
        compiler = new Compiler();
        input = new CompilerInput("");
        reservedCharacters = new char[] {'_', '-'};
        prevUsedRenameMap = new VariableMap();
        prevUsedRenameMap.put("oldName", "newName");
    }

    @Test
    public void testProcess() {
        RenamePrototypes renamePrototypes = new RenamePrototypes(compiler, true, reservedCharacters, prevUsedRenameMap);
        renamePrototypes.process(null, null);
        VariableMap propertyMap = renamePrototypes.getPropertyMap();
        assertEquals("newName", propertyMap.lookupNewName("oldName"));
    }
}