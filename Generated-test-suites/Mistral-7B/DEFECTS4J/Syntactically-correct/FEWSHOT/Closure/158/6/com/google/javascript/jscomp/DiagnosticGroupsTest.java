package com.google.javascript.jscomp;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    List<String> groupNames = ImmutableList.of("accessControls", "deprecated", "visibility", "constantProperty",
        "nonStandardJsDocs", "strictModuleDepCheck", "externsValidation", "ambiguousFunctionDecl", "unknownDefines",
        "tweakValidation", "missingProperties", "internetExplorerChecks", "undefinedVars", "checkRegExp", "checkTypes",
        "checkVars", "checkUselessCode", "typeInvalidation");
    for (String groupName : groupNames) {
      DiagnosticGroup group = registeredGroups.get(groupName);
      checkNotNull(group, "Missing diagnostic group for name: " + groupName);
    }
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup accessControls = diagnosticGroups.forName("accessControls");
    DiagnosticGroup deprecated = diagnosticGroups.forName("deprecated");
    DiagnosticGroup visibility = diagnosticGroups.forName("visibility");
    DiagnosticGroup constantProperty = diagnosticGroups.forName("constantProperty");
    DiagnosticGroup nonStandardJsDocs = diagnosticGroups.forName("nonStandardJsDocs");
    DiagnosticGroup strictModuleDepCheck = diagnosticGroups.forName("strictModuleDepCheck");
    DiagnosticGroup externsValidation = diagnosticGroups.forName("externsValidation");
    DiagnosticGroup ambiguousFunctionDecl = diagnosticGroups.forName("ambiguousFunctionDecl");
    DiagnosticGroup unknownDefines = diagnosticGroups.forName("unknownDefines");
    DiagnosticGroup tweakValidation = diagnosticGroups.forName("tweakValidation");
    DiagnosticGroup missingProperties = diagnosticGroups.forName("missingProperties");
    DiagnosticGroup internetExplorerChecks = diagnosticGroups.forName("internetExplorerChecks");
    DiagnosticGroup undefinedVars = diagnosticGroups.forName("undefinedVars");
    DiagnosticGroup checkRegExp = diagnosticGroups.forName("checkRegExp");
    DiagnosticGroup checkTypes = diagnosticGroups.forName("checkTypes");
    DiagnosticGroup checkVars = diagnosticGroups.forName("checkVars");
    DiagnosticGroup checkUselessCode = diagnosticGroups.forName("checkUselessCode");
    DiagnosticGroup typeInvalidation = diagnosticGroups.forName("typeInvalidation");

    assertEquals(AccessControls.class, accessControls.getClass());
    assertEquals(Deprecated.class, deprecated.getClass());
    assertEquals(Visibility.class, visibility.getClass());
    assertEquals(ConstantProperty.class, constantProperty.getClass());
    assertEquals(NonStandardJsDocs.class, nonStandardJsDocs.getClass());
    assertEquals(StrictModuleDepCheck.class, strictModuleDepCheck.getClass());
    assertEquals(ExternsValidation.class, externsValidation.getClass());
    assertEquals(AmbiguousFunctionDecl.class, ambiguousFunctionDecl.getClass());
    assertEquals(UnknownDefines.class, unknownDefines.getClass());
    assertEquals(TweakValidation.class, tweakValidation.getClass());
    assertEquals(MissingProperties.class, missingProperties.getClass());
    assertEquals(InternetExplorerChecks.class, internetExplorerChecks.getClass());
    assertEquals(UndefinedVars.class, undefinedVars.getClass());
    assertEquals(CheckRegExp.class, checkRegExp.getClass());
    assertEquals(CheckTypes.class, checkTypes.getClass());
    assertEquals(CheckVars.class, checkVars.getClass());
    assertEquals(CheckUselessCode.class, checkUselessCode.getClass());
    assertEquals(TypeInvalidation.class, typeInvalidation.getClass());
  }
}