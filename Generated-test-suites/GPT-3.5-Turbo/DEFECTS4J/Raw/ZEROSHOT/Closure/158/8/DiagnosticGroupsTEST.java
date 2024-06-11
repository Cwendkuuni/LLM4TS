import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticGroups;

public class DiagnosticGroupsTest {

    private DiagnosticGroups diagnosticGroups;

    @Before
    public void setUp() {
        diagnosticGroups = new DiagnosticGroups();
    }

    @Test
    public void testGetRegisteredGroups() {
        assertNotNull(diagnosticGroups.getRegisteredGroups());
    }

    @Test
    public void testForName() {
        assertNotNull(diagnosticGroups.forName("invalidCasts"));
    }

    @Test
    public void testGLOBAL_THIS() {
        DiagnosticGroup group = DiagnosticGroups.GLOBAL_THIS;
        assertNotNull(group);
        assertEquals("globalThis", group.getName());
        assertEquals(1, group.getMembers().size());
    }

    @Test
    public void testDEPRECATED() {
        DiagnosticGroup group = DiagnosticGroups.DEPRECATED;
        assertNotNull(group);
        assertEquals("deprecated", group.getName());
        assertEquals(6, group.getMembers().size());
    }

    @Test
    public void testVISIBILITY() {
        DiagnosticGroup group = DiagnosticGroups.VISIBILITY;
        assertNotNull(group);
        assertEquals("visibility", group.getName());
        assertEquals(5, group.getMembers().size());
    }

    @Test
    public void testCONSTANT_PROPERTY() {
        DiagnosticGroup group = DiagnosticGroups.CONSTANT_PROPERTY;
        assertNotNull(group);
        assertEquals("constantProperty", group.getName());
        assertEquals(1, group.getMembers().size());
    }

    @Test
    public void testNON_STANDARD_JSDOC() {
        DiagnosticGroup group = DiagnosticGroups.NON_STANDARD_JSDOC;
        assertNotNull(group);
        assertEquals("nonStandardJsDocs", group.getName());
        assertEquals(1, group.getMembers().size());
    }

    @Test
    public void testACCESS_CONTROLS() {
        DiagnosticGroup group = DiagnosticGroups.ACCESS_CONTROLS;
        assertNotNull(group);
        assertEquals("accessControls", group.getName());
        assertEquals(11, group.getMembers().size());
    }

    @Test
    public void testINVALID_CASTS() {
        DiagnosticGroup group = DiagnosticGroups.INVALID_CASTS;
        assertNotNull(group);
        assertEquals("invalidCasts", group.getName());
        assertEquals(1, group.getMembers().size());
    }

    @Test
    public void testFILEOVERVIEW_JSDOC() {
        DiagnosticGroup group = DiagnosticGroups.FILEOVERVIEW_JSDOC;
        assertNotNull(group);
        assertEquals("fileoverviewTags", group.getName());
        assertEquals(1, group.getMembers().size());
    }

    @Test
    public void testSTRICT_MODULE_DEP_CHECK() {
        DiagnosticGroup group = DiagnosticGroups.STRICT_MODULE_DEP_CHECK;
        assertNotNull(group);
        assertEquals("strictModuleDepCheck", group.getName());
        assertEquals(2, group.getMembers().size());
    }

    @Test
    public void testEXTERNS_VALIDATION() {
        DiagnosticGroup group = DiagnosticGroups.EXTERNS_VALIDATION;
        assertNotNull(group);
        assertEquals("externsValidation", group.getName());
        assertEquals(2, group.getMembers().size());
    }

    @Test
    public void testAMBIGUOUS_FUNCTION_DECL() {
        DiagnosticGroup group = DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL;
        assertNotNull(group);
        assertEquals("ambiguousFunctionDecl", group.getName());
        assertEquals(1, group.getMembers().size());
    }

    @Test
    public void testUNKNOWN_DEFINES() {
        DiagnosticGroup group = DiagnosticGroups.UNKNOWN_DEFINES;
        assertNotNull(group);
        assertEquals("unknownDefines", group.getName());
        assertEquals(1, group.getMembers().size());
    }

    @Test
    public void testTWEAKS() {
        DiagnosticGroup group = DiagnosticGroups.TWEAKS;
        assertNotNull(group);
        assertEquals("tweakValidation", group.getName());
        assertEquals(3, group.getMembers().size());
    }

    @Test
    public void testMISSING_PROPERTIES() {
        DiagnosticGroup group = DiagnosticGroups.MISSING_PROPERTIES;
        assertNotNull(group);
        assertEquals("missingProperties", group.getName());
        assertEquals(1, group.getMembers().size());
    }

    @Test
    public void testINTERNET_EXPLORER_CHECKS() {
        DiagnosticGroup group = DiagnosticGroups.INTERNET_EXPLORER_CHECKS;
        assertNotNull(group);
        assertEquals("internetExplorerChecks", group.getName());
        assertEquals(1, group.getMembers().size());
    }

    @Test
    public void testUNDEFINED_VARIABLES() {
        DiagnosticGroup group = DiagnosticGroups.UNDEFINED_VARIABLES;
        assertNotNull(group);
        assertEquals("undefinedVars", group.getName());
        assertEquals(1, group.getMembers().size());
    }

    @Test
    public void testCHECK_REGEXP() {
        DiagnosticGroup group = DiagnosticGroups.CHECK_REGEXP;
        assertNotNull(group);
        assertEquals("checkRegExp", group.getName());
        assertEquals(1, group.getMembers().size());
    }

    @Test
    public void testCHECK_TYPES() {
        DiagnosticGroup group = DiagnosticGroups.CHECK_TYPES;
        assertNotNull(group);
        assertEquals("checkTypes", group.getName());
        assertEquals(2, group.getMembers().size());
    }

    @Test
    public void testCHECK_VARIABLES() {
        DiagnosticGroup group = DiagnosticGroups.CHECK_VARIABLES;
        assertNotNull(group);
        assertEquals("checkVars", group.getName());
        assertEquals(2, group.getMembers().size());
    }

    @Test
    public void testCHECK_USELESS_CODE() {
        DiagnosticGroup group = DiagnosticGroups.CHECK_USELESS_CODE;
        assertNotNull(group);
        assertEquals("uselessCode", group.getName());
        assertEquals(2, group.getMembers().size());
    }

    @Test
    public void testTYPE_INVALIDATION() {
        DiagnosticGroup group = DiagnosticGroups.TYPE_INVALIDATION;
        assertNotNull(group);
        assertEquals("typeInvalidation", group.getName());
        assertEquals(1, group.getMembers().size());
    }

    @Test
    public void testSetWarningLevels() {
        // TODO: Implement test
    }
}