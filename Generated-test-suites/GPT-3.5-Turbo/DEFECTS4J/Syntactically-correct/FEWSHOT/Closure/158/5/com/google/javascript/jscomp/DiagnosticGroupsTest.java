
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class DiagnosticGroupsTest {
    @Test
    public void testRegisterGroup1() {
        DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup1",
                CheckGlobalThis.GLOBAL_THIS);
        assertEquals(group, DiagnosticGroups.forName("testGroup1"));
    }
    
    @Test
    public void testRegisterGroup2() {
        DiagnosticGroup group = DiagnosticGroups.registerGroup("testGroup2",
                CheckAccessControls.DEPRECATED_NAME,
                CheckAccessControls.DEPRECATED_NAME_REASON,
                CheckAccessControls.DEPRECATED_PROP,
                CheckAccessControls.DEPRECATED_PROP_REASON,
                CheckAccessControls.DEPRECATED_CLASS,
                CheckAccessControls.DEPRECATED_CLASS_REASON);
        assertEquals(group, DiagnosticGroups.forName("testGroup2"));
    }
    
    @Test
    public void testRegisterGroup3() {
        DiagnosticGroup group1 = DiagnosticGroups.registerGroup("subGroup1",
                CheckAccessControls.BAD_PRIVATE_GLOBAL_ACCESS,
                CheckAccessControls.BAD_PRIVATE_PROPERTY_ACCESS);
        DiagnosticGroup group2 = DiagnosticGroups.registerGroup("subGroup2",
                group1);
        assertEquals(group2, DiagnosticGroups.forName("subGroup2"));
    }
    
    @Test
    public void testGetRegisteredGroups() {
        DiagnosticGroups dg = new DiagnosticGroups();
        assertEquals(DiagnosticGroups.groupsByName, dg.getRegisteredGroups());
    }
    
    @Test
    public void testForName() {
        assertEquals(DiagnosticGroups.GLOBAL_THIS, DiagnosticGroups.forName("globalThis"));
        assertEquals(DiagnosticGroups.DEPRECATED, DiagnosticGroups.forName("deprecated"));
        assertEquals(DiagnosticGroups.VISIBILITY, DiagnosticGroups.forName("visibility"));
        assertEquals(DiagnosticGroups.CONSTANT_PROPERTY, DiagnosticGroups.forName("constantProperty"));
        assertEquals(DiagnosticGroups.NON_STANDARD_JSDOC, DiagnosticGroups.forName("nonStandardJsDocs"));
        assertEquals(DiagnosticGroups.ACCESS_CONTROLS, DiagnosticGroups.forName("accessControls"));
        assertEquals(DiagnosticGroups.INVALID_CASTS, DiagnosticGroups.forName("invalidCasts"));
        assertEquals(DiagnosticGroups.FILEOVERVIEW_JSDOC, DiagnosticGroups.forName("fileoverviewTags"));
        assertEquals(DiagnosticGroups.STRICT_MODULE_DEP_CHECK, DiagnosticGroups.forName("strictModuleDepCheck"));
        assertEquals(DiagnosticGroups.EXTERNS_VALIDATION, DiagnosticGroups.forName("externsValidation"));
        assertEquals(DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL, DiagnosticGroups.forName("ambiguousFunctionDecl"));
        assertEquals(DiagnosticGroups.UNKNOWN_DEFINES, DiagnosticGroups.forName("unknownDefines"));
        assertEquals(DiagnosticGroups.TWEAKS, DiagnosticGroups.forName("tweakValidation"));
        assertEquals(DiagnosticGroups.MISSING_PROPERTIES, DiagnosticGroups.forName("missingProperties"));
        assertEquals(DiagnosticGroups.INTERNET_EXPLORER_CHECKS, DiagnosticGroups.forName("internetExplorerChecks"));
        assertEquals(DiagnosticGroups.UNDEFINED_VARIABLES, DiagnosticGroups.forName("unsetVariable"));
        assertEquals(DiagnosticGroups.CHECK_REGEXP, DiagnosticGroups.forName("checkRegExp"));
        assertEquals(DiagnosticGroups.CHECK_TYPES, DiagnosticGroups.forName("checkTypes"));
        assertEquals(DiagnosticGroups.CHECK_VARIABLES, DiagnosticGroups.forName("checkVars"));
        assertEquals(DiagnosticGroups.CHECK_USELESS_CODE, DiagnosticGroups.forName("uselessCode"));
        assertEquals(DiagnosticGroups.TYPE_INVALIDATION, DiagnosticGroups.forName("typeInvalidation"));
    }
    
    @Test(expected=NullPointerException.class)
    public void testForNameNonExistent() {
        DiagnosticGroups.forName("nonExistent");
    }
    
    @Test
    public void testSetWarningLevels() {
        CompilerOptions options = new CompilerOptions();
        DiagnosticGroups dg = new DiagnosticGroups();
        dg.setWarningLevels(options, DiagnosticGroups.DIAGNOSTIC_GROUP_NAMES.split(","), CheckLevel.WARNING);
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.GLOBAL_THIS));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.DEPRECATED));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.VISIBILITY));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.CONSTANT_PROPERTY));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.NON_STANDARD_JSDOC));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.ACCESS_CONTROLS));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.INVALID_CASTS));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.FILEOVERVIEW_JSDOC));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.STRICT_MODULE_DEP_CHECK));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.EXTERNS_VALIDATION));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.UNKNOWN_DEFINES));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.TWEAKS));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.MISSING_PROPERTIES));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.INTERNET_EXPLORER_CHECKS));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.UNDEFINED_VARIABLES));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.CHECK_REGEXP));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.CHECK_TYPES));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.CHECK_VARIABLES));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.CHECK_USELESS_CODE));
        assertEquals(CheckLevel.WARNING, options.getWarningLevel(DiagnosticGroups.TYPE_INVALIDATION));
    }
}
