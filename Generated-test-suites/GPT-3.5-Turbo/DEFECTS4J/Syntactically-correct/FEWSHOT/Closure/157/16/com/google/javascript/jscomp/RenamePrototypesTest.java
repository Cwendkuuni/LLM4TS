/*
 * Test cases for RenamePrototypes class.
 */
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RenamePrototypesTest {
    @Test
    public void testProcess() {
        JSSourceFile extern = JSSourceFile.fromCode("externs.js",
                "function externFunc() {}");
        JSSourceFile input = JSSourceFile.fromCode("input.js",
                "var obj = {}; " + 
                "obj.prototype = { foo: 1 }; " +
                "obj.foo = 2;");

        Compiler compiler = new Compiler();
        compiler.initOptions(new CompilerOptions());
        compiler.compile(extern, input);

        Node root = compiler.getJsRoot();
        Node externs = compiler.getExternsRoot();

        RenamePrototypes rename = new RenamePrototypes(compiler, true, null, null);
        rename.process(externs, root);
    }
}