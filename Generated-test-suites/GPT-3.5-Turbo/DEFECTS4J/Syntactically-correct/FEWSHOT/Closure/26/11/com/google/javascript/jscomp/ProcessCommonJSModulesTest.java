package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

    @Test
    public void testProcess() {
        // Test 1
        AbstractCompiler compiler1 = new AbstractCompiler();
        ProcessCommonJSModules process1 = new ProcessCommonJSModules(compiler1, ".");
        Node externs1, root1;
        NodeTraversal
            .traverse(compiler1, root1, process1);
        assertNull(process1.getModule());

        // Test 2
        AbstractCompiler compiler2 = new AbstractCompiler();
        ProcessCommonJSModules process2 = new ProcessCommonJSModules(compiler2, ".", false);
        Node externs2, root2;
        NodeTraversal
            .traverse(compiler2, root2, process2);
        assertNull(process2.getModule());

        // Test 3
        AbstractCompiler compiler3 = new AbstractCompiler();
        ProcessCommonJSModules process3 = new ProcessCommonJSModules(compiler3, ".");
        Node externs3, root3;
        NodeTraversal
            .traverse(compiler3, root3, process3);
        assertNull(process3.getModule());

        // Test 4
        AbstractCompiler compiler4 = new AbstractCompiler();
        ProcessCommonJSModules process4 = new ProcessCommonJSModules(compiler4, ".", false);
        Node externs4, root4;
        NodeTraversal
            .traverse(compiler4, root4, process4);
        assertNull(process4.getModule());

        // Test 5
        AbstractCompiler compiler5 = new AbstractCompiler();
        ProcessCommonJSModules process5 = new ProcessCommonJSModules(compiler5, ".");
        Node externs5, root5;
        NodeTraversal
            .traverse(compiler5, root5, process5);
        assertNull(process5.getModule());
    }

    @Test
    public void testGuessCJSModuleName() {
        // Test 1
        AbstractCompiler compiler1 = new AbstractCompiler();
        ProcessCommonJSModules process1 = new ProcessCommonJSModules(compiler1, ".");
        String filename1 = "test1.js";
        String expectedResult1 = "module$test1";
        String actualResult1 = process1.guessCJSModuleName(filename1);
        assertEquals(expectedResult1, actualResult1);

        // Test 2
        AbstractCompiler compiler2 = new AbstractCompiler();
        ProcessCommonJSModules process2 = new ProcessCommonJSModules(compiler2, ".");
        String filename2 = "test2.js";
        String expectedResult2 = "module$test2";
        String actualResult2 = process2.guessCJSModuleName(filename2);
        assertEquals(expectedResult2, actualResult2);

        // Test 3
        AbstractCompiler compiler3 = new AbstractCompiler();
        ProcessCommonJSModules process3 = new ProcessCommonJSModules(compiler3, ".");
        String filename3 = "test3.js";
        String expectedResult3 = "module$test3";
        String actualResult3 = process3.guessCJSModuleName(filename3);
        assertEquals(expectedResult3, actualResult3);

        // Test 4
        AbstractCompiler compiler4 = new AbstractCompiler();
        ProcessCommonJSModules process4 = new ProcessCommonJSModules(compiler4, ".");
        String filename4 = "test4.js";
        String expectedResult4 = "module$test4";
        String actualResult4 = process4.guessCJSModuleName(filename4);
        assertEquals(expectedResult4, actualResult4);

        // Test 5
        AbstractCompiler compiler5 = new AbstractCompiler();
        ProcessCommonJSModules process5 = new ProcessCommonJSModules(compiler5, ".");
        String filename5 = "test5.js";
        String expectedResult5 = "module$test5";
        String actualResult5 = process5.guessCJSModuleName(filename5);
        assertEquals(expectedResult5, actualResult5);
    }

    @Test
    public void testToModuleName() {
        // Test 1
        String filename1 = "test1.js";
        String expectedResult1 = "module$test1";
        String actualResult1 = ProcessCommonJSModules.toModuleName(filename1);
        assertEquals(expectedResult1, actualResult1);

        // Test 2
        String filename2 = "test2.js";
        String expectedResult2 = "module$test2";
        String actualResult2 = ProcessCommonJSModules.toModuleName(filename2);
        assertEquals(expectedResult2, actualResult2);

        // Test 3
        String filename3 = "test3.js";
        String expectedResult3 = "module$test3";
        String actualResult3 = ProcessCommonJSModules.toModuleName(filename3);
        assertEquals(expectedResult3, actualResult3);

        // Test 4
        String filename4 = "test4.js";
        String expectedResult4 = "module$test4";
        String actualResult4 = ProcessCommonJSModules.toModuleName(filename4);
        assertEquals(expectedResult4, actualResult4);

        // Test 5
        String filename5 = "test5.js";
        String expectedResult5 = "module$test5";
        String actualResult5 = ProcessCommonJSModules.toModuleName(filename5);
        assertEquals(expectedResult5, actualResult5);
    }

    @Test
    public void testToModuleName_withCurrentFilename() {
        // Test 1
        String requiredFilename1 = "./test1";
        String currentFilename1 = "./";
        String expectedResult1 = "module$test1";
        String actualResult1 = ProcessCommonJSModules.toModuleName(requiredFilename1, currentFilename1);
        assertEquals(expectedResult1, actualResult1);

        // Test 2
        String requiredFilename2 = "../test2";
        String currentFilename2 = "./";
        String expectedResult2 = "module$test2";
        String actualResult2 = ProcessCommonJSModules.toModuleName(requiredFilename2, currentFilename2);
        assertEquals(expectedResult2, actualResult2);

        // Test 3
        String requiredFilename3 = "./test3";
        String currentFilename3 = "../";
        String expectedResult3 = "module$test3";
        String actualResult3 = ProcessCommonJSModules.toModuleName(requiredFilename3, currentFilename3);
        assertEquals(expectedResult3, actualResult3);

        // Test 4
        String requiredFilename4 = "../test4";
        String currentFilename4 = "../";
        String expectedResult4 = "module$test4";
        String actualResult4 = ProcessCommonJSModules.toModuleName(requiredFilename4, currentFilename4);
        assertEquals(expectedResult4, actualResult4);

        // Test 5
        String requiredFilename5 = "../../test5";
        String currentFilename5 = "../../";
        String expectedResult5 = "module$test5";
        String actualResult5 = ProcessCommonJSModules.toModuleName(requiredFilename5, currentFilename5);
        assertEquals(expectedResult5, actualResult5);
    }
}