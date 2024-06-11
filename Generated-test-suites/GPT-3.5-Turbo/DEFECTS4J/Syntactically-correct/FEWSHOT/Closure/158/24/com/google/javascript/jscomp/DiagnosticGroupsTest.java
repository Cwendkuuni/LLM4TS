package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class DiagnosticGroupsTest {

    private DiagnosticGroups diagnosticGroups;

    @Before
    public void setUp() {
        diagnosticGroups = new DiagnosticGroups();
    }

    @Test
    public void testGetRegisteredGroups() {
        Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
        assertNotNull(registeredGroups);
        assertTrue(registeredGroups.size() > 0);
    }

    @Test
    public void testForName() {
        DiagnosticGroup group = diagnosticGroups.forName("accessControls");
        assertNotNull(group);
        assertEquals("accessControls", group.getName());

        group = diagnosticGroups.forName("checkRegExp");
        assertNotNull(group);
        assertEquals("checkRegExp", group.getName());

        group = diagnosticGroups.forName("checkTypes");
        assertNotNull(group);
        assertEquals("checkTypes", group.getName());

        group = diagnosticGroups.forName("checkVars");
        assertNotNull(group);
        assertEquals("checkVars", group.getName());

        group = diagnosticGroups.forName("constantProperty");
        assertNotNull(group);
        assertEquals("constantProperty", group.getName());

        group = diagnosticGroups.forName("deprecated");
        assertNotNull(group);
        assertEquals("deprecated", group.getName());

        group = diagnosticGroups.forName("externsValidation");
        assertNotNull(group);
        assertEquals("externsValidation", group.getName());

        group = diagnosticGroups.forName("fileoverviewTags");
        assertNotNull(group);
        assertEquals("fileoverviewTags", group.getName());

        group = diagnosticGroups.forName("globalThis");
        assertNotNull(group);
        assertEquals("globalThis", group.getName());

        group = diagnosticGroups.forName("internetExplorerChecks");
        assertNotNull(group);
        assertEquals("internetExplorerChecks", group.getName());

        group = diagnosticGroups.forName("invalidCasts");
        assertNotNull(group);
        assertEquals("invalidCasts", group.getName());

        group = diagnosticGroups.forName("missingProperties");
        assertNotNull(group);
        assertEquals("missingProperties", group.getName());

        group = diagnosticGroups.forName("nonStandardJsDocs");
        assertNotNull(group);
        assertEquals("nonStandardJsDocs", group.getName());

        group = diagnosticGroups.forName("strictModuleDepCheck");
        assertNotNull(group);
        assertEquals("strictModuleDepCheck", group.getName());

        group = diagnosticGroups.forName("typeInvalidation");
        assertNotNull(group);
        assertEquals("typeInvalidation", group.getName());

        group = diagnosticGroups.forName("undefinedVars");
        assertNotNull(group);
        assertEquals("undefinedVars", group.getName());

        group = diagnosticGroups.forName("unknownDefines");
        assertNotNull(group);
        assertEquals("unknownDefines", group.getName());

        group = diagnosticGroups.forName("uselessCode");
        assertNotNull(group);
        assertEquals("uselessCode", group.getName());

        group = diagnosticGroups.forName("visibility");
        assertNotNull(group);
        assertEquals("visibility", group.getName());
    }

    @Test
    public void testRegisterGroup() {
        DiagnosticGroup group = diagnosticGroups.registerGroup("testGroup");
        assertNotNull(group);
        assertEquals("testGroup", group.getName());

        group = diagnosticGroups.forName("testGroup");
        assertNotNull(group);
        assertEquals("testGroup", group.getName());

        DiagnosticType[] types = new DiagnosticType[]{ DiagnosticType.error("TEST_ERROR") };
        group = diagnosticGroups.registerGroup("testGroup2", types);
        assertNotNull(group);
        assertEquals("testGroup2", group.getName());

        group = diagnosticGroups.forName("testGroup2");
        assertNotNull(group);
        assertEquals("testGroup2", group.getName());
    }

    @Test
    public void testRegisterGroupWithGroups() {
        DiagnosticGroup group1 = diagnosticGroups.forName("accessControls");
        assertNotNull(group1);
        assertEquals("accessControls", group1.getName());

        DiagnosticGroup group2 = diagnosticGroups.forName("deprecated");
        assertNotNull(group2);
        assertEquals("deprecated", group2.getName());

        DiagnosticGroup group = diagnosticGroups.registerGroup("testGroup3", group1, group2);
        assertNotNull(group);
        assertEquals("testGroup3", group.getName());

        group = diagnosticGroups.forName("testGroup3");
        assertNotNull(group);
        assertEquals("testGroup3", group.getName());
    }

    @Test
    public void testWarningLevels() {
        List<String> diagnosticGroups = List.of("accessControls", "checkRegExp");
        CheckLevel level = CheckLevel.WARNING;

        diagnosticGroups.setWarningLevels(options, diagnosticGroups, level);
        // Assert that the warning levels have been set correctly
    }
}