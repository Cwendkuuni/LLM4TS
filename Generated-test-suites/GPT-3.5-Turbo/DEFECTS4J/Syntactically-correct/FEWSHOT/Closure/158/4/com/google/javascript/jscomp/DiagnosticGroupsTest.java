package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DiagnosticGroupsTest {
    
    @Test
    public void testGetRegisteredGroups() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        assertNull(diagnosticGroups.getRegisteredGroups());
    }
    
    @Test
    public void testForName() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        assertNull(diagnosticGroups.forName("name"));
    }
    
    @Test
    public void testSetWarningLevels() {
        CompilerOptions options = new CompilerOptions();
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        diagnosticGroups.setWarningLevels(options, null, CheckLevel.OFF);
        diagnosticGroups.setWarningLevels(options, new ArrayList<String>(), CheckLevel.OFF);
        diagnosticGroups.setWarningLevels(options, Arrays.asList("diagnosticGroup1", "diagnosticGroup2"), CheckLevel.ERROR);
    }
}