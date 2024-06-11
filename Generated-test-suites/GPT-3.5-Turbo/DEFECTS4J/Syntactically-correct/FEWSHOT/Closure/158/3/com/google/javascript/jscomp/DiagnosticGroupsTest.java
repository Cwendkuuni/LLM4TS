package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class DiagnosticGroupsTest {
    
    @Test
    public void testRegisterGroup1() {
        DiagnosticGroup group = new DiagnosticGroup();
        DiagnosticGroup result = DiagnosticGroups.registerGroup("testGroup", group);
        assertNotNull(result);
        assertEquals(group, result);
    }
    
    @Test
    public void testRegisterGroup2() {
        DiagnosticType[] types = {DiagnosticType.TYPE_A, DiagnosticType.TYPE_B};
        DiagnosticGroup group = new DiagnosticGroup(types);
        DiagnosticGroup result = DiagnosticGroups.registerGroup("testGroup", types);
        assertNotNull(result);
        assertEquals(group, result);
    }
    
    @Test
    public void testRegisterGroup3() {
        DiagnosticGroup[] groups = {
            new DiagnosticGroup(new DiagnosticType[] {DiagnosticType.TYPE_A}),
            new DiagnosticGroup(new DiagnosticType[] {DiagnosticType.TYPE_B})
        };
        DiagnosticGroup group = new DiagnosticGroup(groups);
        DiagnosticGroup result = DiagnosticGroups.registerGroup("testGroup", groups);
        assertNotNull(result);
        assertEquals(group, result);
    }
    
    @Test
    public void testGetRegisteredGroups() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        assertEquals(diagnosticGroups.getRegisteredGroups(), DiagnosticGroups.groupsByName);
    }
    
    @Test
    public void testForName() {
        DiagnosticGroup group = new DiagnosticGroup();
        DiagnosticGroups.groupsByName.put("testGroup", group);
        DiagnosticGroup result = new DiagnosticGroups().forName("testGroup");
        assertEquals(group, result);
    }
    
    @Test
    public void testGLOBAL_THIS() {
        DiagnosticGroup group = DiagnosticGroups.GLOBAL_THIS;
        assertNotNull(group);
        assertEquals("globalThis", group.getName());
        assertEquals(CheckGlobalThis.GLOBAL_THIS, group.getTypes().get(0));
    }
    
    @Test
    public void testDEPRECATED() {
        DiagnosticGroup group = DiagnosticGroups.DEPRECATED;
        assertNotNull(group);
        assertEquals("deprecated", group.getName());
        assertEquals(CheckAccessControls.DEPRECATED_NAME, group.getTypes().get(0));
        assertEquals(CheckAccessControls.DEPRECATED_NAME_REASON, group.getTypes().get(1));
        assertEquals(CheckAccessControls.DEPRECATED_PROP, group.getTypes().get(2));
        assertEquals(CheckAccessControls.DEPRECATED_PROP_REASON, group.getTypes().get(3));
        assertEquals(CheckAccessControls.DEPRECATED_CLASS, group.getTypes().get(4));
        assertEquals(CheckAccessControls.DEPRECATED_CLASS_REASON, group.getTypes().get(5));
    }
    
    @Test
    public void testVISIBILITY() {
        DiagnosticGroup group = DiagnosticGroups.VISIBILITY;
        assertNotNull(group);
        assertEquals("visibility", group.getName());
        assertEquals(CheckAccessControls.BAD_PRIVATE_GLOBAL_ACCESS, group.getTypes().get(0));
        assertEquals(CheckAccessControls.BAD_PRIVATE_PROPERTY_ACCESS, group.getTypes().get(1));
        assertEquals(CheckAccessControls.BAD_PROTECTED_PROPERTY_ACCESS, group.getTypes().get(2));
        assertEquals(CheckAccessControls.PRIVATE_OVERRIDE, group.getTypes().get(3));
        assertEquals(CheckAccessControls.VISIBILITY_MISMATCH, group.getTypes().get(4));
    }
    
    @Test
    public void testCONSTANT_PROPERTY() {
        DiagnosticGroup group = DiagnosticGroups.CONSTANT_PROPERTY;
        assertNotNull(group);
        assertEquals("constantProperty", group.getName());
        assertEquals(CheckAccessControls.CONST_PROPERTY_REASSIGNED_VALUE, group.getTypes().get(0));
    }
    
    @Test
    public void testNON_STANDARD_JSDOC() {
        DiagnosticGroup group = DiagnosticGroups.NON_STANDARD_JSDOC;
        assertNotNull(group);
        assertEquals("nonStandardJsDocs", group.getName());
        assertEquals(RhinoErrorReporter.BAD_JSDOC_ANNOTATION, group.getTypes().get(0));
    }
    
    @Test
    public void testACCESS_CONTROLS() {
        DiagnosticGroup group = DiagnosticGroups.ACCESS_CONTROLS;
        assertNotNull(group);
        assertEquals("accessControls", group.getName());
        assertEquals(DiagnosticGroups.DEPRECATED, group.getTypes().get(0));
        assertEquals(DiagnosticGroups.VISIBILITY, group.getTypes().get(1));
    }
    
    @Test
    public void testINVALID_CASTS() {
        DiagnosticGroup group = DiagnosticGroups.INVALID_CASTS;
        assertNotNull(group);
        assertEquals("invalidCasts", group.getName());
        assertEquals(TypeValidator.INVALID_CAST, group.getTypes().get(0));
    }
    
    @Test
    public void testFILEOVERVIEW_JSDOC() {
        DiagnosticGroup group = DiagnosticGroups.FILEOVERVIEW_JSDOC;
        assertNotNull(group);
        assertEquals("fileoverviewTags", group.getName());
        assertEquals(RhinoErrorReporter.EXTRA_FILEOVERVIEW, group.getTypes().get(0));
    }
    
    @Test
    public void testSTRICT_MODULE_DEP_CHECK() {
        DiagnosticGroup group = DiagnosticGroups.STRICT_MODULE_DEP_CHECK;
        assertNotNull(group);
        assertEquals("strictModuleDepCheck", group.getName());
        assertEquals(VarCheck.STRICT_MODULE_DEP_ERROR, group.getTypes().get(0));
        assertEquals(CheckGlobalNames.STRICT_MODULE_DEP_QNAME, group.getTypes().get(1));
    }
    
    @Test
    public void testEXTERNS_VALIDATION() {
        DiagnosticGroup group = DiagnosticGroups.EXTERNS_VALIDATION;
        assertNotNull(group);
        assertEquals("externsValidation", group.getName());
        assertEquals(VarCheck.NAME_REFERENCE_IN_EXTERNS_ERROR, group.getTypes().get(0));
        assertEquals(VarCheck.UNDEFINED_EXTERN_VAR_ERROR, group.getTypes().get(1));
    }
    
    @Test
    public void testAMBIGUOUS_FUNCTION_DECL() {
        DiagnosticGroup group = DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL;
        assertNotNull(group);
        assertEquals("ambiguousFunctionDecl", group.getName());
        assertEquals(VariableReferenceCheck.AMBIGUOUS_FUNCTION_DECL, group.getTypes().get(0));
    }
    
    @Test
    public void testUNKNOWN_DEFINES() {
        DiagnosticGroup group = DiagnosticGroups.UNKNOWN_DEFINES;
        assertNotNull(group);
        assertEquals("unknownDefines", group.getName());
        assertEquals(ProcessDefines.UNKNOWN_DEFINE_WARNING, group.getTypes().get(0));
    }
    
    @Test
    public void testTWEAKS() {
        DiagnosticGroup group = DiagnosticGroups.TWEAKS;
        assertNotNull(group);
        assertEquals("tweakValidation", group.getName());
        assertEquals(ProcessTweaks.INVALID_TWEAK_DEFAULT_VALUE_WARNING, group.getTypes().get(0));
        assertEquals(ProcessTweaks.TWEAK_WRONG_GETTER_TYPE_WARNING, group.getTypes().get(1));
        assertEquals(ProcessTweaks.UNKNOWN_TWEAK_WARNING, group.getTypes().get(2));
    }
    
    @Test
    public void testMISSING_PROPERTIES() {
        DiagnosticGroup group = DiagnosticGroups.MISSING_PROPERTIES;
        assertNotNull(group);
        assertEquals("missingProperties", group.getName());
        assertEquals(TypeCheck.INEXISTENT_PROPERTY, group.getTypes().get(0));
    }
    
    @Test
    public void testINTERNET_EXPLORER_CHECKS() {
        DiagnosticGroup group = DiagnosticGroups.INTERNET_EXPLORER_CHECKS;
        assertNotNull(group);
        assertEquals("internetExplorerChecks", group.getName());
        assertEquals(RhinoErrorReporter.TRAILING_COMMA, group.getTypes().get(0));
    }
    
    @Test
    public void testUNDEFINED_VARIABLES() {
        DiagnosticGroup group = DiagnosticGroups.UNDEFINED_VARIABLES;
        assertNotNull(group);
        assertEquals("undefinedVars", group.getName());
        assertEquals(VarCheck.UNDEFINED_VAR_ERROR, group.getTypes().get(0));
    }
    
    @Test
    public void testCHECK_REGEXP() {
        DiagnosticGroup group = DiagnosticGroups.CHECK_REGEXP;
        assertNotNull(group);
        assertEquals("checkRegExp", group.getName());
        assertEquals(CheckRegExp.REGEXP_REFERENCE, group.getTypes().get(0));
    }
    
    @Test
    public void testCHECK_TYPES() {
        DiagnosticGroup group = DiagnosticGroups.CHECK_TYPES;
        assertNotNull(group);
        assertEquals("checkTypes", group.getName());
        assertEquals(TypeValidator.ALL_DIAGNOSTICS, group.getTypes().get(0));
        assertEquals(TypeCheck.ALL_DIAGNOSTICS, group.getTypes().get(1));
    }
    
    @Test
    public void testCHECK_VARIABLES() {
        DiagnosticGroup group = DiagnosticGroups.CHECK_VARIABLES;
        assertNotNull(group);
        assertEquals("checkVars", group.getName());
        assertEquals(VarCheck.UNDEFINED_VAR_ERROR, group.getTypes().get(0));
        assertEquals(SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR, group.getTypes().get(1));
    }
    
    @Test
    public void testCHECK_USELESS_CODE() {
        DiagnosticGroup group = DiagnosticGroups.CHECK_USELESS_CODE;
        assertNotNull(group);
        assertEquals("uselessCode", group.getName());
        assertEquals(CheckSideEffects.USELESS_CODE_ERROR, group.getTypes().get(0));
        assertEquals(CheckUnreachableCode.UNREACHABLE_CODE, group.getTypes().get(1));
    }
    
    @Test
    public void testTYPE_INVALIDATION() {
        DiagnosticGroup group = DiagnosticGroups.TYPE_INVALIDATION;
        assertNotNull(group);
        assertEquals("typeInvalidation", group.getName());
        assertEquals(DisambiguateProperties.Warnings.INVALIDATION, group.getTypes().get(0));
    }
    
    @Test
    public void testSetWarningLevels() {
        CompilerOptions options = new CompilerOptions();
        List<String> diagnosticGroups = Arrays.asList("group1", "group2");
        CheckLevel level = CheckLevel.WARNING;
        new DiagnosticGroups().setWarningLevels(options, diagnosticGroups, level);
    }
}