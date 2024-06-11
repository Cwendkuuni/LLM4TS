package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DiagnosticGroupsTest {

    @Test
    public void testRegisterGroup1() {
        DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup1");
        assertEquals("testGroup1", group.getName());
        assertNull(group.getTypes());
        assertNull(group.getGroups());
    }

    @Test
    public void testRegisterGroup2() {
        DiagnosticType type1 = new DiagnosticType();
        DiagnosticType type2 = new DiagnosticType();
        DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup2", type1, type2);
        assertEquals("testGroup2", group.getName());
        assertEquals(2, group.getTypes().length);
        assertEquals(type1, group.getTypes()[0]);
        assertEquals(type2, group.getTypes()[1]);
        assertNull(group.getGroups());
    }

    @Test
    public void testRegisterGroup3() {
        DiagnosticGroup group1 = new DiagnosticGroup("group1");
        DiagnosticGroup group2 = new DiagnosticGroup("group2");
        DiagnosticGroup group3 = DiagnosticGroups.registerGroup("testGroup3", group1, group2);
        assertEquals("testGroup3", group3.getName());
        assertNull(group3.getTypes());
        assertEquals(2, group3.getGroups().length);
        assertEquals(group1, group3.getGroups()[0]);
        assertEquals(group2, group3.getGroups()[1]);
    }

    @Test
    public void testGetRegisteredGroups() {
        Map<String, DiagnosticGroup> groups = DiagnosticGroups.getRegisteredGroups();
        assertTrue(groups.containsKey("globalThis"));
        assertTrue(groups.containsKey("deprecated"));
        assertTrue(groups.containsKey("visibility"));
        assertTrue(groups.containsKey("constantProperty"));
        assertTrue(groups.containsKey("nonStandardJsDocs"));
        assertTrue(groups.containsKey("accessControls"));
        assertTrue(groups.containsKey("invalidCasts"));
        assertTrue(groups.containsKey("fileoverviewTags"));
        assertTrue(groups.containsKey("strictModuleDepCheck"));
        assertTrue(groups.containsKey("externsValidation"));
        assertTrue(groups.containsKey("ambiguousFunctionDecl"));
        assertTrue(groups.containsKey("unknownDefines"));
        assertTrue(groups.containsKey("tweakValidation"));
        assertTrue(groups.containsKey("missingProperties"));
        assertTrue(groups.containsKey("internetExplorerChecks"));
        assertTrue(groups.containsKey("undefinedVars"));
        assertTrue(groups.containsKey("checkRegExp"));
        assertTrue(groups.containsKey("checkTypes"));
        assertTrue(groups.containsKey("checkVars"));
        assertTrue(groups.containsKey("uselessCode"));
        assertTrue(groups.containsKey("typeInvalidation"));
    }

    @Test
    public void testForName() {
        DiagnosticGroup group1 = DiagnosticGroups.forName("globalThis");
        assertNotNull(group1);
        assertEquals("globalThis", group1.getName());

        DiagnosticGroup group2 = DiagnosticGroups.forName("invalidGroup");
        assertNull(group2);
    }

    @Test
    public void testSetWarningLevels() {
        CompilerOptions options = new CompilerOptions();
        List<String> diagnosticGroups = new ArrayList<>();
        diagnosticGroups.add("globalThis");
        diagnosticGroups.add("deprecated");
        diagnosticGroups.add("visibility");
        DiagnosticGroups.setWarningLevels(options, diagnosticGroups, CheckLevel.WARNING);

        assertTrue(options.getWarningLevel(DiagnosticGroups.GLOBAL_THIS) == CheckLevel.WARNING);
        assertTrue(options.getWarningLevel(DiagnosticGroups.DEPRECATED) == CheckLevel.WARNING);
        assertTrue(options.getWarningLevel(DiagnosticGroups.VISIBILITY) == CheckLevel.WARNING);
    }
}