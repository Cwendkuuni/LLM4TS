package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class RenamePrototypesTest {
    @Test
    public void testProcess() {
        // Initialize the compiler and the input.
        Compiler compiler = new Compiler();
        CompilerOptions options = new CompilerOptions();
        String js = "function() { var foo = new Foo(); foo.bar(); }";
        SourceFile input = SourceFile.fromCode("input.js", js);

        // Run the rename prototypes pass.
        RenamePrototypes renamer = new RenamePrototypes(compiler, true, new char[]{}, null);
        renamer.process(null, Node.newNode(Token.ROOT));

        // Verify the results.
        VariableMap map = renamer.getPropertyMap();
        assertNotNull(map);
        assertEquals("string", map.lookupNewName("oldName"));
        assertEquals("string", map.lookupNewName("newName"));
    }
}