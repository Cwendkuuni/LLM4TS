import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.CheckAccessControls;
import com.google.javascript.jscomp.CheckRegExp;
import com.google.javascript.jscomp.CheckTypes;
import com.google.javascript.jscomp.CheckVars;
import com.google.javascript.jscomp.ConstantProperty;
import com.google.javascript.jscomp.Deprecated;
import com.google.javascript.jscomp.ExternsValidation;
import com.google.javascript.jscomp.FileoverviewTags;
import com.google.javascript.jscomp.GlobalThis;
import com.google.javascript.jscomp.InternetExplorerChecks;
import com.google.javascript.jscomp.InvalidCasts;
import com.google.javascript.jscomp.InvalidFunctionDecl;
import com.google.javascript.jscomp.MissingProperties;
import com.google.javascript.jscomp.NonStandardJsDocs;
import com.google.javascript.jscomp.StrictModuleDepCheck;
import com.google.javascript.jscomp.TypeInvalidation;
import com.google.javascript.jscomp.UndefinedVars;
import com.google.javascript.jscomp.UnknownDefines;
import com.google.javascript.jscomp.UselessCode;
import com.google.javascript.jscomp.Visibility;

public class DiagnosticGroupsTest {

    private DiagnosticGroups diagnosticGroups;

    @Before
    public void setUp() {
        diagnosticGroups = new DiagnosticGroups();
    }

    @Test
    public void testRegisterGroupWithDiagnosticGroup() {
        DiagnosticGroup group = new DiagnosticGroup("testGroup");
        DiagnosticGroup registeredGroup = diagnosticGroups.registerGroup("testGroup", group);
        assertEquals(group, registeredGroup);
        assertEquals(group, diagnosticGroups.getRegisteredGroups().get("testGroup"));
    }

    @Test
    public void testRegisterGroupWithNameAndTypes() {
        DiagnosticType type1 = new DiagnosticType("testType1");
        DiagnosticType type2 = new DiagnosticType("testType2");
        DiagnosticGroup group = diagnosticGroups.registerGroup("testGroup", type1, type2);
        assertEquals(group, diagnosticGroups.getRegisteredGroups().get("testGroup"));
        assertTrue(group.getAllTypes().contains(type1));
        assertTrue(group.getAllTypes().contains(type2));
    }

    @Test
    public void testRegisterGroupWithNameAndGroups() {
        DiagnosticGroup group1 = new DiagnosticGroup("testGroup1");
        DiagnosticGroup group2 = new DiagnosticGroup("testGroup2");
        DiagnosticGroup group = diagnosticGroups.registerGroup("testGroup", group1, group2);
        assertEquals(group, diagnosticGroups.getRegisteredGroups().get("testGroup"));
        assertTrue(group.getGroups().contains(group1));
        assertTrue(group.getGroups().contains(group2));
    }

    @Test
    public void testGetRegisteredGroups() {
        Map<String, DiagnosticGroup> groups = diagnosticGroups.getRegisteredGroups();
        assertEquals(groups, DiagnosticGroups.groupsByName);
    }

    @Test
    public void testForName() {
        String name = "testGroup";
        DiagnosticGroup group = new DiagnosticGroup(name);
        DiagnosticGroup registeredGroup = diagnosticGroups.registerGroup(name, group);
        assertEquals(registeredGroup, diagnosticGroups.forName(name));
    }

    @Test
    public void testGLOBAL_THIS() {
        assertNotNull(DiagnosticGroups.GLOBAL_THIS);
        assertEquals(CheckGlobalThis.GLOBAL_THIS, DiagnosticGroups.GLOBAL_THIS);
    }

    @Test
    public void testDEPRECATED() {
        assertNotNull(DiagnosticGroups.DEPRECATED);
        assertEquals(CheckAccessControls.DEPRECATED_NAME, DiagnosticGroups.DEPRECATED.getTypes().get(0));
        assertEquals(CheckAccessControls.DEPRECATED_NAME_REASON, DiagnosticGroups.DEPRECATED.getTypes().get(1));
        assertEquals(CheckAccessControls.DEPRECATED_PROP, DiagnosticGroups.DEPRECATED.getTypes().get(2));
        assertEquals(CheckAccessControls.DEPRECATED_PROP_REASON, DiagnosticGroups.DEPRECATED.getTypes().get(3));
        assertEquals(CheckAccessControls.DEPRECATED_CLASS, DiagnosticGroups.DEPRECATED.getTypes().get(4));
        assertEquals(CheckAccessControls.DEPRECATED_CLASS_REASON, DiagnosticGroups.DEPRECATED.getTypes().get(5));
    }

    @Test
    public void testVISIBILITY() {
        assertNotNull(DiagnosticGroups.VISIBILITY);
        assertEquals(CheckAccessControls.BAD_PRIVATE_GLOBAL_ACCESS, DiagnosticGroups.VISIBILITY.getTypes().get(0));
        assertEquals(CheckAccessControls.BAD_PRIVATE_PROPERTY_ACCESS, DiagnosticGroups.VISIBILITY.getTypes().get(1));
        assertEquals(CheckAccessControls.BAD_PROTECTED_PROPERTY_ACCESS, DiagnosticGroups.VISIBILITY.getTypes().get(2));
        assertEquals(CheckAccessControls.PRIVATE_OVERRIDE, DiagnosticGroups.VISIBILITY.getTypes().get(3));
        assertEquals(CheckAccessControls.VISIBILITY_MISMATCH, DiagnosticGroups.VISIBILITY.getTypes().get(4));
    }

    @Test
    public void testCONSTANT_PROPERTY() {
        assertNotNull(DiagnosticGroups.CONSTANT_PROPERTY);
        assertEquals(CheckAccessControls.CONST_PROPERTY_REASSIGNED_VALUE, DiagnosticGroups.CONSTANT_PROPERTY.getTypes().get(0));
    }

    @Test
    public void testNON_STANDARD_JSDOC() {
        assertNotNull(DiagnosticGroups.NON_STANDARD_JSDOC);
        assertEquals(RhinoErrorReporter.BAD_JSDOC_ANNOTATION, DiagnosticGroups.NON_STANDARD_JSDOC.getTypes().get(0));
    }

    @Test
    public void testACCESS_CONTROLS() {
        assertNotNull(DiagnosticGroups.ACCESS_CONTROLS);
        assertNotNull(DiagnosticGroups.ACCESS_CONTROLS.getTypes().contains(DiagnosticGroups.DEPRECATED));
        assertNotNull(DiagnosticGroups.ACCESS_CONTROLS.getTypes().contains(DiagnosticGroups.VISIBILITY));
    }

    @Test
    public void testINVALID_CASTS() {
        assertNotNull(DiagnosticGroups.INVALID_CASTS);
        assertEquals(TypeValidator.INVALID_CAST, DiagnosticGroups.INVALID_CASTS.getTypes().get(0));
    }

    @Test
    public void testFILEOVERVIEW_JSDOC() {
        assertNotNull(DiagnosticGroups.FILEOVERVIEW_JSDOC);
        assertEquals(RhinoErrorReporter.EXTRA_FILEOVERVIEW, DiagnosticGroups.FILEOVERVIEW_JSDOC.getTypes().get(0));
    }

    @Test
    public void testSTRICT_MODULE_DEP_CHECK() {
        assertNotNull(DiagnosticGroups.STRICT_MODULE_DEP_CHECK);
        assertEquals(VarCheck.STRICT_MODULE_DEP_ERROR, DiagnosticGroups.STRICT_MODULE_DEP_CHECK.getTypes().get(0));
        assertEquals(CheckGlobalNames.STRICT_MODULE_DEP_QNAME, DiagnosticGroups.STRICT_MODULE_DEP_CHECK.getTypes().get(1));
    }

    @Test
    public void testEXTERNS_VALIDATION() {
        assertNotNull(DiagnosticGroups.EXTERNS_VALIDATION);
        assertEquals(VarCheck.NAME_REFERENCE_IN_EXTERNS_ERROR, DiagnosticGroups.EXTERNS_VALIDATION.getTypes().get(0));
        assertEquals(VarCheck.UNDEFINED_EXTERN_VAR_ERROR, DiagnosticGroups.EXTERNS_VALIDATION.getTypes().get(1));
    }

    @Test
    public void testAMBIGUOUS_FUNCTION_DECL() {
        assertNotNull(DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL);
        assertEquals(VariableReferenceCheck.AMBIGUOUS_FUNCTION_DECL, DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL.getTypes().get(0));
    }

    @Test
    public void testUNKNOWN_DEFINES() {
        assertNotNull(DiagnosticGroups.UNKNOWN_DEFINES);
        assertEquals(ProcessDefines.UNKNOWN_DEFINE_WARNING, DiagnosticGroups.UNKNOWN_DEFINES.getTypes().get(0));
    }

    @Test
    public void testMISSING_PROPERTIES() {
        assertNotNull(DiagnosticGroups.MISSING_PROPERTIES);
        assertEquals(TypeCheck.INEXISTENT_PROPERTY, DiagnosticGroups.MISSING_PROPERTIES.getTypes().get(0));
    }

    @Test
    public void testINTERNET_EXPLORER_CHECKS() {
        assertNotNull(DiagnosticGroups.INTERNET_EXPLORER_CHECKS);
        assertEquals(RhinoErrorReporter.TRAILING_COMMA, DiagnosticGroups.INTERNET_EXPLORER_CHECKS.getTypes().get(0));
    }

    @Test
    public void testUNDEFINED_VARIABLES() {
        assertNotNull(DiagnosticGroups.UNDEFINED_VARIABLES);
        assertEquals(VarCheck.UNDEFINED_VAR_ERROR, DiagnosticGroups.UNDEFINED_VARIABLES.getTypes().get(0));
    }

    @Test
    public void testCHECK_REGEXP() {
        assertNotNull(DiagnosticGroups.CHECK_REGEXP);
        assertEquals(CheckRegExp.REGEXP_REFERENCE, DiagnosticGroups.CHECK_REGEXP.getTypes().get(0));
    }

    @Test
    public void testCHECK_TYPES() {
        assertNotNull(DiagnosticGroups.CHECK_TYPES);
        assertTrue(DiagnosticGroups.CHECK_TYPES.getTypes().containsAll(TypeValidator.ALL_DIAGNOSTICS.getTypes()));
        assertTrue(DiagnosticGroups.CHECK_TYPES.getTypes().containsAll(TypeCheck.ALL_DIAGNOSTICS.getTypes()));
    }

    @Test
    public void testCHECK_VARIABLES() {
        assertNotNull(DiagnosticGroups.CHECK_VARIABLES);
        assertEquals(VarCheck.UNDEFINED_VAR_ERROR, DiagnosticGroups.CHECK_VARIABLES.getTypes().get(0));
        assertEquals(SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR, DiagnosticGroups.CHECK_VARIABLES.getTypes().get(1));
    }

    @Test
    public void testCHECK_USELESS_CODE() {
        assertNotNull(DiagnosticGroups.CHECK_USELESS_CODE);
        assertEquals(CheckSideEffects.USELESS_CODE_ERROR, DiagnosticGroups.CHECK_USELESS_CODE.getTypes().get(0));
        assertEquals(CheckUnreachableCode.UNREACHABLE_CODE, DiagnosticGroups.CHECK_USELESS_CODE.getTypes().get(1));
    }

    @Test
    public void testTYPE_INVALIDATION() {
        assertNotNull(DiagnosticGroups.TYPE_INVALIDATION);
        assertEquals(DisambiguateProperties.Warnings.INVALIDATION, DiagnosticGroups.TYPE_INVALIDATION.getTypes().get(0));
    }

    @Test
    public void testSetWarningLevels() {
        CompilerOptions options = new CompilerOptions();
        List<String> diagnosticGroups = Arrays.asList("testGroup1", "testGroup2");
        CheckLevel level = CheckLevel.WARNING;
        diagnosticGroups.setWarningLevels(options, diagnosticGroups, level);
        for (String name : diagnosticGroups) {
            DiagnosticGroup group = diagnosticGroups.forName(name);
            assertNotNull(group);
            assertTrue(options.getWarningLevel(group) == level);
        }
    }
}