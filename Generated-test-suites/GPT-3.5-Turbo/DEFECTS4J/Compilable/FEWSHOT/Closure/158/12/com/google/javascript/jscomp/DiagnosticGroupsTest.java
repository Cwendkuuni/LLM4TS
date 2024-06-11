package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

public class DiagnosticGroupsTest {
    private DiagnosticGroups diagnosticGroups;
    
    @Before
    public void setUp() {
        diagnosticGroups = new DiagnosticGroups();
    }
    
    @Test
    public void testGetRegisteredGroups() {
        assertEquals(DiagnosticGroups.GLOBAL_THIS, diagnosticGroups.getRegisteredGroups().get("globalThis"));
        assertEquals(DiagnosticGroups.DEPRECATED, diagnosticGroups.getRegisteredGroups().get("deprecated"));
        assertEquals(DiagnosticGroups.VISIBILITY, diagnosticGroups.getRegisteredGroups().get("visibility"));
        assertEquals(DiagnosticGroups.CONSTANT_PROPERTY, diagnosticGroups.getRegisteredGroups().get("constantProperty"));
        assertEquals(DiagnosticGroups.NON_STANDARD_JSDOC, diagnosticGroups.getRegisteredGroups().get("nonStandardJsDocs"));
        assertEquals(DiagnosticGroups.ACCESS_CONTROLS, diagnosticGroups.getRegisteredGroups().get("accessControls"));
        assertEquals(DiagnosticGroups.INVALID_CASTS, diagnosticGroups.getRegisteredGroups().get("invalidCasts"));
        assertEquals(DiagnosticGroups.FILEOVERVIEW_JSDOC, diagnosticGroups.getRegisteredGroups().get("fileoverviewTags"));
        assertEquals(DiagnosticGroups.STRICT_MODULE_DEP_CHECK, diagnosticGroups.getRegisteredGroups().get("strictModuleDepCheck"));
        assertEquals(DiagnosticGroups.EXTERNS_VALIDATION, diagnosticGroups.getRegisteredGroups().get("externsValidation"));
        assertEquals(DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL, diagnosticGroups.getRegisteredGroups().get("ambiguousFunctionDecl"));
        assertEquals(DiagnosticGroups.UNKNOWN_DEFINES, diagnosticGroups.getRegisteredGroups().get("unknownDefines"));
        assertEquals(DiagnosticGroups.TWEAKS, diagnosticGroups.getRegisteredGroups().get("tweakValidation"));
        assertEquals(DiagnosticGroups.MISSING_PROPERTIES, diagnosticGroups.getRegisteredGroups().get("missingProperties"));
        assertEquals(DiagnosticGroups.INTERNET_EXPLORER_CHECKS, diagnosticGroups.getRegisteredGroups().get("internetExplorerChecks"));
        assertEquals(DiagnosticGroups.UNDEFINED_VARIABLES, diagnosticGroups.getRegisteredGroups().get("undefinedVars"));
        assertEquals(DiagnosticGroups.CHECK_REGEXP, diagnosticGroups.getRegisteredGroups().get("checkRegExp"));
        assertEquals(DiagnosticGroups.CHECK_TYPES, diagnosticGroups.getRegisteredGroups().get("checkTypes"));
        assertEquals(DiagnosticGroups.CHECK_VARIABLES, diagnosticGroups.getRegisteredGroups().get("checkVars"));
        assertEquals(DiagnosticGroups.CHECK_USELESS_CODE, diagnosticGroups.getRegisteredGroups().get("uselessCode"));
        assertEquals(DiagnosticGroups.TYPE_INVALIDATION, diagnosticGroups.getRegisteredGroups().get("typeInvalidation"));
    }
    
    @Test
    public void testForName() {
        assertSame(DiagnosticGroups.GLOBAL_THIS, diagnosticGroups.forName("globalThis"));
        assertSame(DiagnosticGroups.DEPRECATED, diagnosticGroups.forName("deprecated"));
        assertSame(DiagnosticGroups.VISIBILITY, diagnosticGroups.forName("visibility"));
        assertSame(DiagnosticGroups.CONSTANT_PROPERTY, diagnosticGroups.forName("constantProperty"));
        assertSame(DiagnosticGroups.NON_STANDARD_JSDOC, diagnosticGroups.forName("nonStandardJsDocs"));
        assertSame(DiagnosticGroups.ACCESS_CONTROLS, diagnosticGroups.forName("accessControls"));
        assertSame(DiagnosticGroups.INVALID_CASTS, diagnosticGroups.forName("invalidCasts"));
        assertSame(DiagnosticGroups.FILEOVERVIEW_JSDOC, diagnosticGroups.forName("fileoverviewTags"));
        assertSame(DiagnosticGroups.STRICT_MODULE_DEP_CHECK, diagnosticGroups.forName("strictModuleDepCheck"));
        assertSame(DiagnosticGroups.EXTERNS_VALIDATION, diagnosticGroups.forName("externsValidation"));
        assertSame(DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL, diagnosticGroups.forName("ambiguousFunctionDecl"));
        assertSame(DiagnosticGroups.UNKNOWN_DEFINES, diagnosticGroups.forName("unknownDefines"));
        assertSame(DiagnosticGroups.TWEAKS, diagnosticGroups.forName("tweakValidation"));
        assertSame(DiagnosticGroups.MISSING_PROPERTIES, diagnosticGroups.forName("missingProperties"));
        assertSame(DiagnosticGroups.INTERNET_EXPLORER_CHECKS, diagnosticGroups.forName("internetExplorerChecks"));
        assertSame(DiagnosticGroups.UNDEFINED_VARIABLES, diagnosticGroups.forName("undefinedVars"));
        assertSame(DiagnosticGroups.CHECK_REGEXP, diagnosticGroups.forName("checkRegExp"));
        assertSame(DiagnosticGroups.CHECK_TYPES, diagnosticGroups.forName("checkTypes"));
        assertSame(DiagnosticGroups.CHECK_VARIABLES, diagnosticGroups.forName("checkVars"));
        assertSame(DiagnosticGroups.CHECK_USELESS_CODE, diagnosticGroups.forName("uselessCode"));
        assertSame(DiagnosticGroups.TYPE_INVALIDATION, diagnosticGroups.forName("typeInvalidation"));
        
        assertNull(diagnosticGroups.forName("invalidGroup"));
    }
}