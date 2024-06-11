import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        assertEquals(new ImmutableMap.Builder<String, DiagnosticGroup>()
                .put("accessControls", DiagnosticGroups.ACCESS_CONTROLS)
                .put("ambiguousFunctionDecl", DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL)
                .put("checkRegExp", DiagnosticGroups.CHECK_REGEXP)
                .put("checkTypes", DiagnosticGroups.CHECK_TYPES)
                .put("checkVars", DiagnosticGroups.CHECK_VARIABLES)
                .put("constantProperty", DiagnosticGroups.CONSTANT_PROPERTY)
                .put("deprecated", DiagnosticGroups.DEPRECATED)
                .put("externsValidation", DiagnosticGroups.EXTERNS_VALIDATION)
                .put("fileoverviewTags", DiagnosticGroups.FILEOVERVIEW_JSDOC)
                .put("globalThis", DiagnosticGroups.GLOBAL_THIS)
                .put("internetExplorerChecks", DiagnosticGroups.INTERNET_EXPLORER_CHECKS)
                .put("invalidCasts", DiagnosticGroups.INVALID_CASTS)
                .put("missingProperties", DiagnosticGroups.MISSING_PROPERTIES)
                .put("nonStandardJsDocs", DiagnosticGroups.NON_STANDARD_JSDOC)
                .put("strictModuleDepCheck", DiagnosticGroups.STRICT_MODULE_DEP_CHECK)
                .put("typeInvalidation", DiagnosticGroups.TYPE_INVALIDATION)
                .put("undefinedVars", DiagnosticGroups.UNDEFINED_VARIABLES)
                .put("unknownDefines", DiagnosticGroups.UNKNOWN_DEFINES)
                .put("uselessCode", DiagnosticGroups.CHECK_USELESS_CODE)
                .put("visibility", DiagnosticGroups.VISIBILITY)
                .build(), diagnosticGroups.getRegisteredGroups());
    }

    @Test
    public void testForName() {
        assertEquals(DiagnosticGroups.ACCESS_CONTROLS, diagnosticGroups.forName("accessControls"));
        assertEquals(DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL, diagnosticGroups.forName("ambiguousFunctionDecl"));
        assertEquals(DiagnosticGroups.CHECK_REGEXP, diagnosticGroups.forName("checkRegExp"));
        assertEquals(DiagnosticGroups.CHECK_TYPES, diagnosticGroups.forName("checkTypes"));
        assertEquals(DiagnosticGroups.CHECK_VARIABLES, diagnosticGroups.forName("checkVars"));
        assertEquals(DiagnosticGroups.CONSTANT_PROPERTY, diagnosticGroups.forName("constantProperty"));
        assertEquals(DiagnosticGroups.DEPRECATED, diagnosticGroups.forName("deprecated"));
        assertEquals(DiagnosticGroups.EXTERNS_VALIDATION, diagnosticGroups.forName("externsValidation"));
        assertEquals(DiagnosticGroups.FILEOVERVIEW_JSDOC, diagnosticGroups.forName("fileoverviewTags"));
        assertEquals(DiagnosticGroups.GLOBAL_THIS, diagnosticGroups.forName("globalThis"));
        assertEquals(DiagnosticGroups.INTERNET_EXPLORER_CHECKS, diagnosticGroups.forName("internetExplorerChecks"));
        assertEquals(DiagnosticGroups.INVALID_CASTS, diagnosticGroups.forName("invalidCasts"));
        assertEquals(DiagnosticGroups.MISSING_PROPERTIES, diagnosticGroups.forName("missingProperties"));
        assertEquals(DiagnosticGroups.NON_STANDARD_JSDOC, diagnosticGroups.forName("nonStandardJsDocs"));
        assertEquals(DiagnosticGroups.STRICT_MODULE_DEP_CHECK, diagnosticGroups.forName("strictModuleDepCheck"));
        assertEquals(DiagnosticGroups.TYPE_INVALIDATION, diagnosticGroups.forName("typeInvalidation"));
        assertEquals(DiagnosticGroups.UNDEFINED_VARIABLES, diagnosticGroups.forName("undefinedVars"));
        assertEquals(DiagnosticGroups.UNKNOWN_DEFINES, diagnosticGroups.forName("unknownDefines"));
        assertEquals(DiagnosticGroups.CHECK_USELESS_CODE, diagnosticGroups.forName("uselessCode"));
        assertEquals(DiagnosticGroups.VISIBILITY, diagnosticGroups.forName("visibility"));
        assertNull(diagnosticGroups.forName("nonexistentGroup"));
    }

    @Test
    public void testRegisterGroup() {
        assertEquals(DiagnosticGroups.VISIBILITY, DiagnosticGroups.registerGroup("testGroup1", DiagnosticGroups.VISIBILITY));
        assertEquals(DiagnosticGroups.GLOBAL_THIS, DiagnosticGroups.registerGroup("testGroup2", DiagnosticGroups.GLOBAL_THIS));
        assertNull(DiagnosticGroups.registerGroup("testGroup3", (DiagnosticType[]) null));
        assertNull(DiagnosticGroups.registerGroup("testGroup4", (DiagnosticGroup) null));
        assertNull(DiagnosticGroups.registerGroup("testGroup5", (DiagnosticGroup[]) null));
    }

}