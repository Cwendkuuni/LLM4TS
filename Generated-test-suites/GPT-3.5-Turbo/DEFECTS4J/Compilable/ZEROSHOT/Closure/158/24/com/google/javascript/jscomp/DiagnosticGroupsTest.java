package com.google.javascript.jscomp;
import static org.junit.Assert.*;
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
    assertEquals(diagnosticGroups.getRegisteredGroups().size(), 23);
  }

  @Test
  public void testForName() {
    assertEquals(diagnosticGroups.forName("accessControls"), DiagnosticGroups.ACCESS_CONTROLS);
    assertEquals(diagnosticGroups.forName("constantProperty"), DiagnosticGroups.CONSTANT_PROPERTY);
    assertEquals(diagnosticGroups.forName("deprecated"), DiagnosticGroups.DEPRECATED);
    assertEquals(diagnosticGroups.forName("visibility"), DiagnosticGroups.VISIBILITY);
    assertEquals(diagnosticGroups.forName("nonStandardJsDocs"), DiagnosticGroups.NON_STANDARD_JSDOC);
    assertEquals(diagnosticGroups.forName("invalidCasts"), DiagnosticGroups.INVALID_CASTS);
    assertEquals(diagnosticGroups.forName("fileoverviewTags"), DiagnosticGroups.FILEOVERVIEW_JSDOC);
    assertEquals(diagnosticGroups.forName("strictModuleDepCheck"), DiagnosticGroups.STRICT_MODULE_DEP_CHECK);
    assertEquals(diagnosticGroups.forName("externsValidation"), DiagnosticGroups.EXTERNS_VALIDATION);
    assertEquals(diagnosticGroups.forName("ambiguousFunctionDecl"), DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL);
    assertEquals(diagnosticGroups.forName("unknownDefines"), DiagnosticGroups.UNKNOWN_DEFINES);
    assertEquals(diagnosticGroups.forName("tweakValidation"), DiagnosticGroups.TWEAKS);
    assertEquals(diagnosticGroups.forName("missingProperties"), DiagnosticGroups.MISSING_PROPERTIES);
    assertEquals(diagnosticGroups.forName("internetExplorerChecks"), DiagnosticGroups.INTERNET_EXPLORER_CHECKS);
    assertEquals(diagnosticGroups.forName("undefinedVars"), DiagnosticGroups.UNDEFINED_VARIABLES);
    assertEquals(diagnosticGroups.forName("checkRegExp"), DiagnosticGroups.CHECK_REGEXP);
    assertEquals(diagnosticGroups.forName("checkTypes"), DiagnosticGroups.CHECK_TYPES);
    assertEquals(diagnosticGroups.forName("checkVars"), DiagnosticGroups.CHECK_VARIABLES);
    assertEquals(diagnosticGroups.forName("uselessCode"), DiagnosticGroups.CHECK_USELESS_CODE);
    assertEquals(diagnosticGroups.forName("typeInvalidation"), DiagnosticGroups.TYPE_INVALIDATION);
    assertNull(diagnosticGroups.forName("fakeGroup"));
  }

  @Test
  public void testGLOBAL_THISShouldBeRegistered() {
    assertEquals(DiagnosticGroups.GLOBAL_THIS, diagnosticGroups.getRegisteredGroups().get("globalThis"));
  }

  @Test
  public void testDEPRECATEDShouldBeRegistered() {
    assertEquals(DiagnosticGroups.DEPRECATED, diagnosticGroups.getRegisteredGroups().get("deprecated"));
  }

  @Test
  public void testVISIBILITYShouldBeRegistered() {
    assertEquals(DiagnosticGroups.VISIBILITY, diagnosticGroups.getRegisteredGroups().get("visibility"));
  }

  @Test
  public void testCONSTANT_PROPERTYShouldBeRegistered() {
    assertEquals(DiagnosticGroups.CONSTANT_PROPERTY, diagnosticGroups.getRegisteredGroups().get("constantProperty"));
  }

  @Test
  public void testNON_STANDARD_JSDOCShouldBeRegistered() {
    assertEquals(DiagnosticGroups.NON_STANDARD_JSDOC, diagnosticGroups.getRegisteredGroups().get("nonStandardJsDocs"));
  }

  @Test
  public void testACCESS_CONTROLSShouldBeRegistered() {
    assertEquals(DiagnosticGroups.ACCESS_CONTROLS, diagnosticGroups.getRegisteredGroups().get("accessControls"));
  }

  @Test
  public void testINVALID_CASTSShouldBeRegistered() {
    assertEquals(DiagnosticGroups.INVALID_CASTS, diagnosticGroups.getRegisteredGroups().get("invalidCasts"));
  }

  @Test
  public void testFILEOVERVIEW_JSDOCShouldBeRegistered() {
    assertEquals(DiagnosticGroups.FILEOVERVIEW_JSDOC, diagnosticGroups.getRegisteredGroups().get("fileoverviewTags"));
  }

  @Test
  public void testSTRICT_MODULE_DEP_CHECKShouldBeRegistered() {
    assertEquals(DiagnosticGroups.STRICT_MODULE_DEP_CHECK, diagnosticGroups.getRegisteredGroups().get("strictModuleDepCheck"));
  }

  @Test
  public void testEXTERNS_VALIDATIONShouldBeRegistered() {
    assertEquals(DiagnosticGroups.EXTERNS_VALIDATION, diagnosticGroups.getRegisteredGroups().get("externsValidation"));
  }

  @Test
  public void testAMBIGUOUS_FUNCTION_DECLShouldBeRegistered() {
    assertEquals(DiagnosticGroups.AMBIGUOUS_FUNCTION_DECL, diagnosticGroups.getRegisteredGroups().get("ambiguousFunctionDecl"));
  }

  @Test
  public void testUNKNOWN_DEFINESShouldBeRegistered() {
    assertEquals(DiagnosticGroups.UNKNOWN_DEFINES, diagnosticGroups.getRegisteredGroups().get("unknownDefines"));
  }

  @Test
  public void testTWEAKSShouldBeRegistered() {
    assertEquals(DiagnosticGroups.TWEAKS, diagnosticGroups.getRegisteredGroups().get("tweakValidation"));
  }

  @Test
  public void testMISSING_PROPERTIESShouldBeRegistered() {
    assertEquals(DiagnosticGroups.MISSING_PROPERTIES, diagnosticGroups.getRegisteredGroups().get("missingProperties"));
  }

  @Test
  public void testINTERNET_EXPLORER_CHECKSShouldBeRegistered() {
    assertEquals(DiagnosticGroups.INTERNET_EXPLORER_CHECKS, diagnosticGroups.getRegisteredGroups().get("internetExplorerChecks"));
  }

  @Test
  public void testUNDEFINED_VARIABLESShouldBeRegistered() {
    assertEquals(DiagnosticGroups.UNDEFINED_VARIABLES, diagnosticGroups.getRegisteredGroups().get("undefinedVars"));
  }

  @Test
  public void testCHECK_REGEXPShouldBeRegistered() {
    assertEquals(DiagnosticGroups.CHECK_REGEXP, diagnosticGroups.getRegisteredGroups().get("checkRegExp"));
  }

  @Test
  public void testCHECK_TYPESShouldBeRegistered() {
    assertEquals(DiagnosticGroups.CHECK_TYPES, diagnosticGroups.getRegisteredGroups().get("checkTypes"));
  }

  @Test
  public void testCHECK_VARIABLESShouldBeRegistered() {
    assertEquals(DiagnosticGroups.CHECK_VARIABLES, diagnosticGroups.getRegisteredGroups().get("checkVars"));
  }

  @Test
  public void testCHECK_USELESS_CODEShouldBeRegistered() {
    assertEquals(DiagnosticGroups.CHECK_USELESS_CODE, diagnosticGroups.getRegisteredGroups().get("uselessCode"));
  }

  @Test
  public void testTYPE_INVALIDATIONShouldBeRegistered() {
    assertEquals(DiagnosticGroups.TYPE_INVALIDATION, diagnosticGroups.getRegisteredGroups().get("typeInvalidation"));
  }
}