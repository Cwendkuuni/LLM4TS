package com.google.javascript.jscomp;

import org.junit.Test;
import static org.junit.Assert.*;

public class DiagnosticGroupsTest {
    @Test
    public void testRegisterGroup1() {
        DiagnosticGroup group = new DiagnosticGroup("test1");
        DiagnosticGroup result = DiagnosticGroups.registerGroup("test1", group);
        assertEquals(group, result);
    }
    
    @Test
    public void testRegisterGroup2() {
        DiagnosticType type1 = new DiagnosticType("type1", null);
        DiagnosticType type2 = new DiagnosticType("type2", null);
        DiagnosticGroup group = DiagnosticGroups.registerGroup("test2", type1, type2);
        DiagnosticGroup result = DiagnosticGroups.forName("test2");
        assertEquals(group, result);
    }
    
    @Test
    public void testRegisterGroup3() {
        DiagnosticGroup group1 = new DiagnosticGroup("group1");
        DiagnosticGroup group2 = new DiagnosticGroup("group2");
        DiagnosticGroup group = DiagnosticGroups.registerGroup("test3", group1, group2);
        DiagnosticGroup result = DiagnosticGroups.forName("test3");
        assertEquals(group, result);
    }
    
    @Test
    public void testGetRegisteredGroups() {
        DiagnosticGroup group = new DiagnosticGroup("group");
        DiagnosticGroups.registerGroup("test", group);
        Map<String, DiagnosticGroup> expected = new HashMap<>();
        expected.put("test", group);
        Map<String, DiagnosticGroup> result = new DiagnosticGroups().getRegisteredGroups();
        assertEquals(expected, result);
    }
    
    @Test
    public void testForName() {
        DiagnosticGroup group = new DiagnosticGroup("group");
        DiagnosticGroups.registerGroup("test", group);
        DiagnosticGroup result = new DiagnosticGroups().forName("test");
        assertEquals(group, result);
    }
    
    @Test
    public void testSetWarningLevels() {
        CompilerOptions options = new CompilerOptions();
        List<String> groups = Arrays.asList("test1", "test2");
        CheckLevel level = CheckLevel.WARNING;
        DiagnosticGroup group1 = DiagnosticGroups.registerGroup("test1");
        DiagnosticGroup group2 = DiagnosticGroups.registerGroup("test2");
        new DiagnosticGroups().setWarningLevels(options, groups, level);
        assertEquals(level, options.getWarningLevel(group1));
        assertEquals(level, options.getWarningLevel(group2));
    }
}