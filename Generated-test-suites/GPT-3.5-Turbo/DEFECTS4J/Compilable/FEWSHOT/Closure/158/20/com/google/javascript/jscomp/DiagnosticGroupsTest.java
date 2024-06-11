package com.google.javascript.jscomp;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
public class DiagnosticGroupsTest {
    @Test
    public void testGetRegisteredGroups() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        assertNotNull(diagnosticGroups.getRegisteredGroups());
    }
    @Test
    public void testForName() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        assertNotNull(diagnosticGroups.forName("accessControls"));
        assertNotNull(diagnosticGroups.forName("ambiguousFunctionDecl"));
        assertNotNull(diagnosticGroups.forName("checkRegExp"));
        assertNotNull(diagnosticGroups.forName("checkTypes"));
        assertNotNull(diagnosticGroups.forName("checkVars"));
        assertNotNull(diagnosticGroups.forName("constantProperty"));
        assertNotNull(diagnosticGroups.forName("deprecated"));
        assertNotNull(diagnosticGroups.forName("externsValidation"));
        assertNotNull(diagnosticGroups.forName("fileoverviewTags"));
        assertNotNull(diagnosticGroups.forName("globalThis"));
        assertNotNull(diagnosticGroups.forName("internetExplorerChecks"));
        assertNotNull(diagnosticGroups.forName("invalidCasts"));
        assertNotNull(diagnosticGroups.forName("missingProperties"));
        assertNotNull(diagnosticGroups.forName("nonStandardJsDocs"));
        assertNotNull(diagnosticGroups.forName("strictModuleDepCheck"));
        assertNotNull(diagnosticGroups.forName("typeInvalidation"));
        assertNotNull(diagnosticGroups.forName("undefinedVars"));
        assertNotNull(diagnosticGroups.forName("unknownDefines"));
        assertNotNull(diagnosticGroups.forName("uselessCode"));
        assertNotNull(diagnosticGroups.forName("visibility"));
    }
}