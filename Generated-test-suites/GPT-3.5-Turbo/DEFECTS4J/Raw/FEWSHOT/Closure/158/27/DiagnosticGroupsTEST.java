
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.List;
import java.util.Map;

public class DiagnosticGroupsTest {
    @Test
    public void testRegisterGroupWithDiagnosticGroup() {
        DiagnosticGroup group = new DiagnosticGroup("testGroup");
        DiagnosticGroup registeredGroup = DiagnosticGroups.registerGroup("testGroup", group);
        assertEquals(group, registeredGroup);
    }

    @Test
    public void testRegisterGroupWithNameAndDiagnosticTypes() {
        DiagnosticType type1 = new DiagnosticType("testType1");
        DiagnosticType type2 = new DiagnosticType("testType2");
        DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup", type1, type2);
        assertEquals(2, group.getTypes().size());
        assertTrue(group.getTypes().contains(type1));
        assertTrue(group.getTypes().contains(type2));
    }

    @Test
    public void testRegisterGroupWithNameAndDiagnosticGroups() {
        DiagnosticGroup group1 = new DiagnosticGroup("testGroup1");
        DiagnosticGroup group2 = new DiagnosticGroup("testGroup2");
        DiagnosticGroup group3 = new DiagnosticGroup("testGroup3");
        DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup", group1, group2, group3);
        assertEquals(3, group.getSubgroups().size());
        assertTrue(group.getSubgroups().contains(group1));
        assertTrue(group.getSubgroups().contains(group2));
        assertTrue(group.getSubgroups().contains(group3));
    }

    @Test
    public void testGetRegisteredGroups() {
        DiagnosticGroup group1 = new DiagnosticGroup("testGroup1");
        DiagnosticGroup group2 = new DiagnosticGroup("testGroup2");
        DiagnosticGroup group3 = new DiagnosticGroup("testGroup3");
        DiagnosticGroups.registerGroup("testGroup1", group1);
        DiagnosticGroups.registerGroup("testGroup2", group2);
        DiagnosticGroups.registerGroup("testGroup3", group3);

        Map<String, DiagnosticGroup> registeredGroups = DiagnosticGroups.getRegisteredGroups();
        assertEquals(3, registeredGroups.size());
        assertEquals(group1, registeredGroups.get("testGroup1"));
        assertEquals(group2, registeredGroups.get("testGroup2"));
        assertEquals(group3, registeredGroups.get("testGroup3"));
    }

    @Test
    public void testForNameWithRegisteredGroup() {
        DiagnosticGroup group = new DiagnosticGroup("testGroup");
        DiagnosticGroups.registerGroup("testGroup", group);

        DiagnosticGroup retrievedGroup = DiagnosticGroups.forName("testGroup");
        assertEquals(group, retrievedGroup);
    }

    @Test
    public void testForNameWithUnregisteredGroup() {
        DiagnosticGroup retrievedGroup = DiagnosticGroups.forName("testGroup");
        assertNull(retrievedGroup);
    }

    @Test
    public void testSetWarningLevels() {
        CompilerOptions options = new CompilerOptions();
        List<String> diagnosticGroups = Arrays.asList("testGroup1", "testGroup2");
        CheckLevel level = CheckLevel.WARNING;

        DiagnosticGroup group1 = new DiagnosticGroup("testGroup1");
        DiagnosticGroup group2 = new DiagnosticGroup("testGroup2");
        DiagnosticGroups.registerGroup("testGroup1", group1);
        DiagnosticGroups.registerGroup("testGroup2", group2);

        DiagnosticGroups.setWarningLevels(options, diagnosticGroups, level);

        assertTrue(options.getWarningsGuard().hasDiagnosticGroup(group1));
        assertTrue(options.getWarningsGuard().hasDiagnosticGroup(group2));
        assertEquals(level, options.getWarningsGuard().getDiagnosticGroupLevel(group1));
        assertEquals(level, options.getWarningsGuard().getDiagnosticGroupLevel(group2));
    }
}
