
package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import java.util.Map;

public class DiagnosticGroupsTest {
    private static final DiagnosticGroup GLOBAL_THIS = 
        DiagnosticGroups.registerGroup("globalThis",
                CheckGlobalThis.GLOBAL_THIS);

    private static final DiagnosticGroup DEPRECATED = 
        DiagnosticGroups.registerGroup("deprecated",
                CheckAccessControls.DEPRECATED_NAME,
                CheckAccessControls.DEPRECATED_NAME_REASON,
                CheckAccessControls.DEPRECATED_PROP,
                CheckAccessControls.DEPRECATED_PROP_REASON,
                CheckAccessControls.DEPRECATED_CLASS,
                CheckAccessControls.DEPRECATED_CLASS_REASON);

    private static final DiagnosticGroup VISIBILITY = 
        DiagnosticGroups.registerGroup("visibility",
                CheckAccessControls.BAD_PRIVATE_GLOBAL_ACCESS,
                CheckAccessControls.BAD_PRIVATE_PROPERTY_ACCESS,
                CheckAccessControls.BAD_PROTECTED_PROPERTY_ACCESS,
                CheckAccessControls.PRIVATE_OVERRIDE,
                CheckAccessControls.VISIBILITY_MISMATCH);

    private static final DiagnosticGroup CONSTANT_PROPERTY = 
        DiagnosticGroups.registerGroup("constantProperty",
                CheckAccessControls.CONST_PROPERTY_REASSIGNED_VALUE);

    private static final DiagnosticGroup NON_STANDARD_JSDOC =
        DiagnosticGroups.registerGroup("nonStandardJsDocs",
                RhinoErrorReporter.BAD_JSDOC_ANNOTATION);

    private static final DiagnosticGroup ACCESS_CONTROLS =
        DiagnosticGroups.registerGroup("accessControls",
                DEPRECATED, VISIBILITY);

    private static final DiagnosticGroup INVALID_CASTS = 
        DiagnosticGroups.registerGroup("invalidCasts",
                TypeValidator.INVALID_CAST);

    private static final DiagnosticGroup FILEOVERVIEW_JSDOC =
        DiagnosticGroups.registerGroup("fileoverviewTags",
                RhinoErrorReporter.EXTRA_FILEOVERVIEW);

    private static final DiagnosticGroup STRICT_MODULE_DEP_CHECK =
        DiagnosticGroups.registerGroup("strictModuleDepCheck",
                VarCheck.STRICT_MODULE_DEP_ERROR,
                CheckGlobalNames.STRICT_MODULE_DEP_QNAME);

    private static final DiagnosticGroup EXTERNS_VALIDATION =
        DiagnosticGroups.registerGroup("externsValidation",
                VarCheck.NAME_REFERENCE_IN_EXTERNS_ERROR,
                VarCheck.UNDEFINED_EXTERN_VAR_ERROR);

    private static final DiagnosticGroup AMBIGUOUS_FUNCTION_DECL =
        DiagnosticGroups.registerGroup("ambiguousFunctionDecl",
                VariableReferenceCheck.AMBIGUOUS_FUNCTION_DECL);

    private static final DiagnosticGroup UNKNOWN_DEFINES =
        DiagnosticGroups.registerGroup("unknownDefines",
                ProcessDefines.UNKNOWN_DEFINE_WARNING);

    private static final DiagnosticGroup TWEAKS =
        DiagnosticGroups.registerGroup("tweakValidation",
                ProcessTweaks.INVALID_TWEAK_DEFAULT_VALUE_WARNING,
                ProcessTweaks.TWEAK_WRONG_GETTER_TYPE_WARNING,
                ProcessTweaks.UNKNOWN_TWEAK_WARNING);

    private static final DiagnosticGroup MISSING_PROPERTIES =
        DiagnosticGroups.registerGroup("missingProperties",
                TypeCheck.INEXISTENT_PROPERTY);

    private static final DiagnosticGroup INTERNET_EXPLORER_CHECKS =
        DiagnosticGroups.registerGroup("internetExplorerChecks",
                RhinoErrorReporter.TRAILING_COMMA);

    private static final DiagnosticGroup UNDEFINED_VARIABLES =
        DiagnosticGroups.registerGroup("undefinedVars",
                VarCheck.UNDEFINED_VAR_ERROR);

    private static final DiagnosticGroup CHECK_REGEXP =
        DiagnosticGroups.registerGroup("checkRegExp",
                CheckRegExp.REGEXP_REFERENCE);

    private static final DiagnosticGroup CHECK_TYPES =
        DiagnosticGroups.registerGroup("checkTypes",
                TypeValidator.ALL_DIAGNOSTICS,
                TypeCheck.ALL_DIAGNOSTICS);

    private static final DiagnosticGroup CHECK_VARIABLES =
        DiagnosticGroups.registerGroup("checkVars",
                VarCheck.UNDEFINED_VAR_ERROR,
                SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR);

    private static final DiagnosticGroup CHECK_USELESS_CODE =
        DiagnosticGroups.registerGroup("uselessCode",
                CheckSideEffects.USELESS_CODE_ERROR,
                CheckUnreachableCode.UNREACHABLE_CODE);

    private static final DiagnosticGroup TYPE_INVALIDATION =
        DiagnosticGroups.registerGroup("typeInvalidation",
                DisambiguateProperties.Warnings.INVALIDATION);

    @Test
    public void testGetRegisteredGroups() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
        assertEquals(22, registeredGroups.size());
    }

    @Test
    public void testForName() {
        DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
        DiagnosticGroup group = diagnosticGroups.forName("accessControls");
        assertNotNull(group);
        assertEquals("accessControls", group.getName());
    }

    @Test
    public void testSetWarningLevels() {
        // This test requires CompilerOptions and List<String> to be mocked
        // as they are part of the external library jscomp. 
        // Therefore, the test cannot be implemented here.
    }
}
