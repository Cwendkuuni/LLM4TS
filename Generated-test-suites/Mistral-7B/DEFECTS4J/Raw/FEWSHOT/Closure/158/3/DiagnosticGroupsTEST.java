package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    ImmutableMap<String, DiagnosticGroup> expectedGroups = ImmutableMap.<String, DiagnosticGroup>builder()
        .put("accessControls", ACCESS_CONTROLS)
        .put("ambiguousFunctionDecl", AMBIGUOUS_FUNCTION_DECL)
        .put("checkRegExp", CHECK_REGEXP)
        .put("checkTypes", CHECK_TYPES)
        .put("checkVars", CHECK_VARIABLES)
        .put("constantProperty", CONSTANT_PROPERTY)
        .put("deprecated", DEPRECATED)
        .put("externsValidation", EXTERNS_VALIDATION)
        .put("fileoverviewTags", FILEOVERVIEW_JSDOC)
        .put("globalThis", GLOBAL_THIS)
        .put("invalidCasts", INVALID_CASTS)
        .put("internetExplorerChecks", INTERNET_EXPLORER_CHECKS)
        .put("missingProperties", MISSING_PROPERTIES)
        .put("nonStandardJsDocs", NON_STANDARD_JSDOC)
        .put("strictModuleDepCheck", STRICT_MODULE_DEP_CHECK)
        .put("typeInvalidation", TYPE_INVALIDATION)
        .put("unknownDefines", UNKNOWN_DEFINES)
        .put("uselessCode", CHECK_USELESS_CODE)
        .put("visibility", VISIBILITY)
        .build();

    assertTrue(registeredGroups.equals(expectedGroups));
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup accessControls = diagnosticGroups.forName("accessControls");
    DiagnosticGroup deprecated = diagnosticGroups.forName("deprecated");

    assertNotNull(accessControls);
    assertNotNull(deprecated);
  }
}