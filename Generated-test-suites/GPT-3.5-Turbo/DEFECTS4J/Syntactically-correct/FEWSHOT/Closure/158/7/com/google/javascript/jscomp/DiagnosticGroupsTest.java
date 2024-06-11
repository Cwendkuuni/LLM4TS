package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DiagnosticGroupsTest {
    @Test
    public void testRegisterGroup() {
        DiagnosticGroup group = new DiagnosticGroup("");
        DiagnosticGroup result = DiagnosticGroups.registerGroup("group", group);
        assertNotNull(result);
    }

    @Test
    public void testForName() {
        DiagnosticGroup group = new DiagnosticGroup("");
        DiagnosticGroups.registerGroup("group", group);
        
        DiagnosticGroup result = DiagnosticGroups.forName("group");
        assertNotNull(result);
        
        result = DiagnosticGroups.forName("nonexistent");
        assertNull(result);
    }

    @Test
    public void testGetRegisteredGroups() {
        DiagnosticGroup group = new DiagnosticGroup("");
        DiagnosticGroups.registerGroup("group", group);
        
        Map<String, DiagnosticGroup> result = DiagnosticGroups.getRegisteredGroups();
        assertEquals(1, result.size());
        assertEquals(group, result.get("group"));
    }

    @Test
    public void testSetWarningLevels() {
        CompilerOptions options = new CompilerOptions();
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        
        List<String> groups = Arrays.asList("group1", "group2");
        diagnosticGroups.setWarningLevels(options, groups, CheckLevel.WARNING);
        
        for (String group : groups) {
            DiagnosticGroup diagnosticGroup = diagnosticGroups.forName(group);
            assertNotNull(diagnosticGroup);
            assertEquals(CheckLevel.WARNING, options.getWarningLevel(diagnosticGroup));
        }
    }
}