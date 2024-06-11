package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    assertTrue(registeredGroups.size() > 0);
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup deprecated = diagnosticGroups.forName("deprecated");
    assertNotNull(deprecated);
  }

  @Test
  public void testAccessControls() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup accessControls = diagnosticGroups.forName("accessControls");
    assertTrue(accessControls.contains(CheckAccessControls.DEPRECATED_NAME));
    assertTrue(accessControls.contains(CheckAccessControls.VISIBILITY_MISMATCH));
  }

  @Test
  public void testInvalidCasts() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup invalidCasts = diagnosticGroups.forName("invalidCasts");
    assertTrue(invalidCasts.contains(TypeValidator.INVALID_CAST));
  }

  @Test
  public void testFileoverviewJsdoc() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup fileoverviewJsdoc = diagnosticGroups.forName("fileoverviewJsdoc");
    assertTrue(fileoverviewJsdoc.contains(RhinoErrorReporter.EXTRA_FILEOVERVIEW));
  }

  @Test
  public void testStrictModuleDepCheck() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup strictModuleDepCheck = diagnosticGroups.forName("strictModuleDepCheck");
    assertTrue(strictModuleDepCheck.contains(VarCheck.STRICT_MODULE_DEP_ERROR));
  }

  @Test
  public void testExternsValidation() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup externsValidation = diagnosticGroups.forName("externsValidation");
    assertTrue(externsValidation.contains(VarCheck.NAME_REFERENCE_IN_EXTERNS_ERROR));
    assertTrue(externsValidation.contains(VarCheck.UNDEFINED_EXTERN_VAR_ERROR));
  }

  @Test
  public void testAmbiguousFunctionDecl() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup ambiguousFunctionDecl = diagnosticGroups.forName("ambiguousFunctionDecl");
    assertTrue(ambiguousFunctionDecl.contains(VariableReferenceCheck.AMBIGUOUS_FUNCTION_DECL));
  }

  @Test
  public void testUnknownDefines() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup unknownDefines = diagnosticGroups.forName("unknownDefines");
    assertTrue(unknownDefines.contains(ProcessDefines.UNKNOWN_DEFINE_WARNING));
  }

  @Test
  public void testTweaks() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup tweaks = diagnosticGroups.forName("tweakValidation");
    assertTrue(tweaks.contains(ProcessTweaks.INVALID_TWEAK_DEFAULT_VALUE_WARNING));
    assertTrue(tweaks.contains(ProcessTweaks.TWEAK_WRONG_GETTER_TYPE_WARNING));
    assertTrue(tweaks.contains(ProcessTweaks.UNKNOWN_TWEAK_WARNING));
  }

  @Test
  public void testMissingProperties() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup missingProperties = diagnosticGroups.forName("missingProperties");
    assertTrue(missingProperties.contains(TypeCheck.INEXISTENT_PROPERTY));
  }

  @Test
  public void testInternetExplorerChecks() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup internetExplorerChecks = diagnosticGroups.forName("internetExplorerChecks");
    assertTrue(internetExplorerChecks.contains(RhinoErrorReporter.TRAILING_COMMA));
  }

  @Test
  public void testUndefinedVariables() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup undefinedVariables = diagnosticGroups.forName("undefinedVars");
    assertTrue(undefinedVariables.contains(VarCheck.UNDEFINED_VAR_ERROR));
  }

  @Test
  public void testCheckRegExp() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup checkRegExp = diagnosticGroups.forName("checkRegExp");
    assertTrue(checkRegExp.contains(CheckRegExp.REGEXP_REFERENCE));
  }

  @Test
  public void testCheckTypes() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup checkTypes = diagnosticGroups.forName("checkTypes");
    assertTrue(checkTypes.contains(TypeValidator.ALL_DIAGNOSTICS));
    assertTrue(checkTypes.contains(TypeCheck.ALL_DIAGNOSTICS));
  }

  @Test
  public void testCheckVariables() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup checkVariables = diagnosticGroups.forName("checkVars");
    assertTrue(checkVariables.contains(VarCheck.UNDEFINED_VAR_ERROR));
    assertTrue(checkVariables.contains(SyntacticScopeCreator.VAR_MULTIPLY_DECLARED_ERROR));
  }

  @Test
  public void testCheckUsefulCode() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup checkUsefulCode = diagnosticGroups.forName("uselessCode");
    assertTrue(checkUsefulCode.contains(CheckSideEffects.USELESS_CODE_ERROR));
    assertTrue(checkUsefulCode.contains(CheckUnreachableCode.UNREACHABLE_CODE));
  }

  @Test
  public void testTypeInvalidation() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup typeInvalidation = diagnosticGroups.forName("typeInvalidation");
    assertTrue(typeInvalidation.contains(DisambiguateProperties.Warnings.INVALIDATION));
  }
}