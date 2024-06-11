package com.google.javascript.jscomp;

import org.junit.Test;

import static org.junit.Assert.*;

public class DiagnosticGroupsTest {

    @Test
    public void testRegisterGroup() {
        DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup",
                CheckGlobalThis.GLOBAL_THIS,
                CheckGlobalNames.STRICT_MODULE_DEP_QNAME);
        assertNotNull(group);
    }

    @Test
    public void testRegisterGroup_WithNameAndTypes() {
        DiagnosticType type1 = new DiagnosticType("type1");
        DiagnosticType type2 = new DiagnosticType("type2");

        DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup",
                type1, type2);
        assertNotNull(group);
    }

    @Test
    public void testRegisterGroup_WithNameAndGroups() {
        DiagnosticGroup group1 = DiagnosticGroups.registerGroup("group1",
                CheckGlobalThis.GLOBAL_THIS,
                CheckAccessControls.DEPRECATED_NAME);
        assertNotNull(group1);

        DiagnosticGroup group2 = DiagnosticGroups.registerGroup("group2",
                group1,
                CheckGlobalNames.STRICT_MODULE_DEP_QNAME);
        assertNotNull(group2);
    }

    @Test
    public void testGetRegisteredGroups() {
        DiagnosticGroups groups = new DiagnosticGroups();
        assertNotNull(groups.getRegisteredGroups());
    }

    @Test
    public void testForName() {
        DiagnosticGroup group1 = DiagnosticGroups.registerGroup("group1",
                CheckGlobalThis.GLOBAL_THIS,
                CheckAccessControls.DEPRECATED_NAME);
        assertNotNull(group1);

        DiagnosticGroup group2 = DiagnosticGroups.registerGroup("group2",
                group1,
                CheckGlobalNames.STRICT_MODULE_DEP_QNAME);
        assertNotNull(group2);

        DiagnosticGroups groups = new DiagnosticGroups();
        assertEquals(group1, groups.forName("group1"));
        assertEquals(group2, groups.forName("group2"));
        assertNull(groups.forName("nonExistingGroup"));
    }

    @Test
    public void testSetWarningLevels() {
        CompilerOptions options = new CompilerOptions();

        DiagnosticGroup group1 = DiagnosticGroups.registerGroup("group1",
                CheckGlobalThis.GLOBAL_THIS,
                CheckAccessControls.DEPRECATED_NAME);
        assertNotNull(group1);

        DiagnosticGroup group2 = DiagnosticGroups.registerGroup("group2",
                group1,
                CheckGlobalNames.STRICT_MODULE_DEP_QNAME);
        assertNotNull(group2);

        DiagnosticGroups groups = new DiagnosticGroups();
        groups.setWarningLevels(options, Arrays.asList("group1", "group2"), CheckLevel.WARNING);

        assertEquals(CheckLevel.WARNING, options.getWarningLevel(group1));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(group2));
    }

    @Test
    public void testConstants() {
        assertNotNull(DiagnosticGroups.GLOBAL_THIS);
        assertNotNull(DiagnosticGroups.DEPRECATED);
        assertNotNull(DiagnosticGroups.VISIBILITY);
        assertNotNull(DiagnosticGroups.CONSTANT_PROPERTY);
        assertNotNull(DiagnosticGroups.NON_STANDARD_JSDOC);
        assertNotNull(DiagnosticGroups.ACCESS_CONTROLS);
        assertNotNull(DiagnosticGroups.INVALID_CASTS);
        assertNotNull(DiagnosticGroups.FILEOVERVIEW_JSDOC);
        assertNotNull(DiagnosticGroups.STRICT_MODULE_DEP_CHECK);
        assertNotNull(DiagnosticGroups.EXTERNS_VALIDATION);
        assertNotNull(DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL);
        assertNotNull(DiagnosticGroups.UNKNOWN_DEFINES);
        assertNotNull(DiagnosticGroups.TWEAKS);
        assertNotNull(DiagnosticGroups.MISSING_PROPERTIES);
        assertNotNull(DiagnosticGroups.INTERNET_EXPLORER_CHECKS);
        assertNotNull(DiagnosticGroups.UNDEFINED_VARIABLES);
        assertNotNull(DiagnosticGroups.CHECK_REGEXP);
        assertNotNull(DiagnosticGroups.CHECK_TYPES);
        assertNotNull(DiagnosticGroups.CHECK_VARIABLES);
        assertNotNull(DiagnosticGroups.CHECK_USELESS_CODE);
        assertNotNull(DiagnosticGroups.TYPE_INVALIDATION);
    }
}