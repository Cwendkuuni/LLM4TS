package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DiagnosticGroupsTest {
    @Test
    public void testRegisterGroup() {
        DiagnosticGroup group1 = DiagnosticGroups.registerGroup("test1", new DiagnosticType());
        assertEquals("test1", group1.getName());
        
        DiagnosticGroup group2 = DiagnosticGroups.registerGroup("test2", DiagnosticGroups.DEPRECATED);
        assertEquals("test2", group2.getName());
    }

    @Test
    public void testGetRegisteredGroups() {
        DiagnosticGroups groups = new DiagnosticGroups();
        assertEquals(groups.getRegisteredGroups(), DiagnosticGroups.groupsByName);
    }

    @Test
    public void testForName() {
        DiagnosticGroups groups = new DiagnosticGroups();
        DiagnosticGroup group1 = DiagnosticGroups.registerGroup("test1", new DiagnosticType());
        assertEquals(group1, groups.forName("test1"));
        assertNull(groups.forName("test2"));
    }

    @Test
    public void testSetWarningLevels() {
        CompilerOptions options = new CompilerOptions();
        List<String> groups = Arrays.asList("test1", "test2", "test3");
        DiagnosticGroups groupsObj = new DiagnosticGroups();
        groupsObj.setWarningLevels(options, groups, CheckLevel.WARNING);
        
        DiagnosticGroup group1 = groupsObj.forName("test1");
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(group1));
        
        DiagnosticGroup group2 = groupsObj.forName("test2");
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(group2));
        
        DiagnosticGroup group3 = groupsObj.forName("test3");
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(group3));
    }
}