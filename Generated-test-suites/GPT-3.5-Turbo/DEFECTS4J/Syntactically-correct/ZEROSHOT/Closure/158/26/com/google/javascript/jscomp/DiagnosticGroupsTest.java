import org.junit.Test;
import static org.junit.Assert.*;

public class DiagnosticGroupsTest {

    @Test
    public void testGetRegisteredGroups() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        assertNotNull(diagnosticGroups.getRegisteredGroups());
    }

    @Test
    public void testForName() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        assertNull(diagnosticGroups.forName("nonExistentGroup"));
        assertNotNull(diagnosticGroups.forName("accessControls"));
    }

    @Test
    public void testGLOBAL_THIS() {
        assertEquals("globalThis", DiagnosticGroups.GLOBAL_THIS.getName());
    }

    @Test
    public void testDEPRECATED() {
        assertEquals("deprecated", DiagnosticGroups.DEPRECATED.getName());
    }

    @Test
    public void testVISIBILITY() {
        assertEquals("visibility", DiagnosticGroups.VISIBILITY.getName());
    }

    @Test
    public void testCONSTANT_PROPERTY() {
        assertEquals("constantProperty", DiagnosticGroups.CONSTANT_PROPERTY.getName());
    }

    @Test
    public void testNON_STANDARD_JSDOC() {
        assertEquals("nonStandardJsDocs", DiagnosticGroups.NON_STANDARD_JSDOC.getName());
    }

    @Test
    public void testACCESS_CONTROLS() {
        assertEquals("accessControls", DiagnosticGroups.ACCESS_CONTROLS.getName());
    }

    @Test
    public void testINVALID_CASTS() {
        assertEquals("invalidCasts", DiagnosticGroups.INVALID_CASTS.getName());
    }

    @Test
    public void testFILEOVERVIEW_JSDOC() {
        assertEquals("fileoverviewTags", DiagnosticGroups.FILEOVERVIEW_JSDOC.getName());
    }

    @Test
    public void testSTRICT_MODULE_DEP_CHECK() {
        assertEquals("strictModuleDepCheck", DiagnosticGroups.STRICT_MODULE_DEP_CHECK.getName());
    }

    @Test
    public void testEXTERNS_VALIDATION() {
        assertEquals("externsValidation", DiagnosticGroups.EXTERNS_VALIDATION.getName());
    }

    @Test
    public void testAMBIGUOUS_FUNCTION_DECL() {
        assertEquals("ambiguousFunctionDecl", DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL.getName());
    }

    @Test
    public void testUNKNOWN_DEFINES() {
        assertEquals("unknownDefines", DiagnosticGroups.UNKNOWN_DEFINES.getName());
    }

    @Test
    public void testTWEAKS() {
        assertEquals("tweakValidation", DiagnosticGroups.TWEAKS.getName());
    }

    @Test
    public void testMISSING_PROPERTIES() {
        assertEquals("missingProperties", DiagnosticGroups.MISSING_PROPERTIES.getName());
    }

    @Test
    public void testINTERNET_EXPLORER_CHECKS() {
        assertEquals("internetExplorerChecks", DiagnosticGroups.INTERNET_EXPLORER_CHECKS.getName());
    }

    @Test
    public void testUNDEFINED_VARIABLES() {
        assertEquals("undefinedVars", DiagnosticGroups.UNDEFINED_VARIABLES.getName());
    }

    @Test
    public void testCHECK_REGEXP() {
        assertEquals("checkRegExp", DiagnosticGroups.CHECK_REGEXP.getName());
    }

    @Test
    public void testCHECK_TYPES() {
        assertEquals("checkTypes", DiagnosticGroups.CHECK_TYPES.getName());
    }

    @Test
    public void testCHECK_VARIABLES() {
        assertEquals("checkVars", DiagnosticGroups.CHECK_VARIABLES.getName());
    }

    @Test
    public void testCHECK_USELESS_CODE() {
        assertEquals("uselessCode", DiagnosticGroups.CHECK_USELESS_CODE.getName());
    }

    @Test
    public void testTYPE_INVALIDATION() {
        assertEquals("typeInvalidation", DiagnosticGroups.TYPE_INVALIDATION.getName());
    }

    @Test
    public void testSetWarningLevels() {
        // TODO: Implement test for setWarningLevels method
    }
}