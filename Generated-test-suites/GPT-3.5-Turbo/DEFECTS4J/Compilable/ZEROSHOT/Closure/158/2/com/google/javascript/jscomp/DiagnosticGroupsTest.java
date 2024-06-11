package com.google.javascript.jscomp;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DiagnosticGroupsTest {
  
  DiagnosticGroups groups;

  @Before
  public void setUp() {
    groups = new DiagnosticGroups();
  }

  @Test
  public void testGetRegisteredGroups() {
    assertNotNull(groups.getRegisteredGroups());
  }

  @Test
  public void testForName() {
    assertNotNull(groups.forName("accessControls"));
    assertNotNull(groups.forName("deprecated"));
    assertNotNull(groups.forName("visibility"));
    assertNotNull(groups.forName("constantProperty"));
    assertNotNull(groups.forName("nonStandardJsDocs"));
    assertNotNull(groups.forName("invalidCasts"));
    assertNotNull(groups.forName("fileoverviewTags"));
    assertNotNull(groups.forName("strictModuleDepCheck"));
    assertNotNull(groups.forName("externsValidation"));
    assertNotNull(groups.forName("ambiguousFunctionDecl"));
    assertNotNull(groups.forName("unknownDefines"));
    assertNotNull(groups.forName("tweakValidation"));
    assertNotNull(groups.forName("missingProperties"));
    assertNotNull(groups.forName("internetExplorerChecks"));
    assertNotNull(groups.forName("undefinedVars"));
    assertNotNull(groups.forName("checkRegExp"));
    assertNotNull(groups.forName("checkTypes"));
    assertNotNull(groups.forName("checkVars"));
    assertNotNull(groups.forName("uselessCode"));
    assertNotNull(groups.forName("typeInvalidation"));
  }
}