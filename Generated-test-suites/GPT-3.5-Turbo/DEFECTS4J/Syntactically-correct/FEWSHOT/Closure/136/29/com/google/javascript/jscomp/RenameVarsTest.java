package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class RenameVarsTest {

    private RenameVars renameVars;

    @Before
    public void setUp() {
        renameVars = new RenameVars(
                compiler,
                prefix,
                localRenamingOnly,
                preserveAnonymousFunctionNames,
                generatePseudoNames,
                prevUsedRenameMap,
                reservedCharacters,
                reservedNames
        );
    }

    @Test
    public void testProcess_forValidInput_returnValidOutput() {
        Node externs = null;
        Node root = null;

        renameVars.process(externs, root);
        // perform assertion here
    }
}