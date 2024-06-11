package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DiagnosticGroupsTest {
    
    @Test
    public void testRegisterGroupWithDiagnosticGroup() {
        DiagnosticGroup group = new DiagnosticGroup("group");
        DiagnosticGroup registeredGroup = DiagnosticGroups.registerGroup("name", group);
        assertNotNull(registeredGroup);
        assertEquals(group, registeredGroup);
    }
    
    @Test
    public void testRegisterGroupWithNameAndTypes() {
        DiagnosticType type1 = new DiagnosticType();
        DiagnosticType type2 = new DiagnosticType();
        DiagnosticGroup group = DiagnosticGroups.registerGroup("name", type1, type2);
        assertNotNull(group);
        assertEquals(2, group.getTypes().size());
        assertTrue(group.getTypes().contains(type1));
        assertTrue(group.getTypes().contains(type2));
    }
    
    @Test
    public void testRegisterGroupWithNameAndGroups() {
        DiagnosticType type1 = new DiagnosticType();
        DiagnosticType type2 = new DiagnosticType();
        DiagnosticGroup group1 = DiagnosticGroups.registerGroup("group1", type1);
        DiagnosticGroup group2 = DiagnosticGroups.registerGroup("group2", type2);
        DiagnosticGroup group = DiagnosticGroups.registerGroup("name", group1, group2);
        assertNotNull(group);
        assertEquals(2, group.getSubgroups().size());
        assertTrue(group.getSubgroups().contains(group1));
        assertTrue(group.getSubgroups().contains(group2));
    }
    
    @Test
    public void testGetRegisteredGroups() {
        DiagnosticGroup group1 = new DiagnosticGroup("group1");
        DiagnosticGroup group2 = new DiagnosticGroup("group2");
        DiagnosticGroups.registerGroup("name1", group1);
        DiagnosticGroups.registerGroup("name2", group2);
        Map<String, DiagnosticGroup> groups = new DiagnosticGroups().getRegisteredGroups();
        assertEquals(2, groups.size());
        assertTrue(groups.containsKey("name1"));
        assertTrue(groups.containsKey("name2"));
        assertEquals(group1, groups.get("name1"));
        assertEquals(group2, groups.get("name2"));
    }
    
    @Test
    public void testForName() {
        DiagnosticGroup group1 = new DiagnosticGroup("group1");
        DiagnosticGroup group2 = new DiagnosticGroup("group2");
        DiagnosticGroups.registerGroup("name1", group1);
        DiagnosticGroups.registerGroup("name2", group2);
        DiagnosticGroup foundGroup1 = new DiagnosticGroups().forName("name1");
        DiagnosticGroup foundGroup2 = new DiagnosticGroups().forName("name2");
        assertNotNull(foundGroup1);
        assertNotNull(foundGroup2);
        assertEquals(group1, foundGroup1);
        assertEquals(group2, foundGroup2);
    }
    
    @Test
    public void testSetWarningLevels() {
        CompilerOptions options = new CompilerOptions();
        List<String> groups = Arrays.asList("group1", "group2");
        DiagnosticGroup group1 = new DiagnosticGroup("group1");
        DiagnosticGroup group2 = new DiagnosticGroup("group2");
        DiagnosticGroups.registerGroup("name1", group1);
        DiagnosticGroups.registerGroup("name2", group2);
        new DiagnosticGroups().setWarningLevels(options, groups, CheckLevel.WARNING);
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(group1));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(group2));
    }
}