import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.DiagnosticGroup;
import com.google.javascript.jscomp.DiagnosticGroups;
import com.google.javascript.jscomp.CompilerOptions;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();
    ImmutableMap<String, DiagnosticGroup> expectedGroups = ImmutableMap.<String, DiagnosticGroup> builder()
        .put("accessControls", AccessControlsDiagnosticGroup.INSTANCE)
        .put("ambiguousFunctionDecl", AmbiguousFunctionDeclDiagnosticGroup.INSTANCE)
        .put("checkRegExp", CheckRegExpDiagnosticGroup.INSTANCE)
        .put("checkTypes", CheckTypesDiagnosticGroup.INSTANCE)
        .put("constantProperty", ConstantPropertyDiagnosticGroup.INSTANCE)
        .put("deprecated", DeprecatedDiagnosticGroup.INSTANCE)
        .put("externsValidation", ExternsValidationDiagnosticGroup.INSTANCE)
        .put("fileoverviewTags", FileoverviewTagsDiagnosticGroup.INSTANCE)
        .put("globalThis", GlobalThisDiagnosticGroup.INSTANCE)
        .put("invalidCasts", InvalidCastsDiagnosticGroup.INSTANCE)
        .put("internetExplorerChecks", InternetExplorerChecksDiagnosticGroup.INSTANCE)
        .put("missingProperties", MissingPropertiesDiagnosticGroup.INSTANCE)
        .put("nonStandardJsDocs", NonStandardJsDocsDiagnosticGroup.INSTANCE)
        .put("strictModuleDepCheck", StrictModuleDepCheckDiagnosticGroup.INSTANCE)
        .put("typeInvalidation", TypeInvalidationDiagnosticGroup.INSTANCE)
        .put("undefinedVars", UndefinedVarsDiagnosticGroup.INSTANCE)
        .put("uselessCode", UselessCodeDiagnosticGroup.INSTANCE)
        .build();

    assertEquals(expectedGroups, registeredGroups);
  }

  @Test
  public void testForName() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    DiagnosticGroup deprecated = diagnosticGroups.forName("deprecated");
    assertNotNull(deprecated);
    assertEquals("deprecated", deprecated.getName());
  }

  @Test
  public void testRegisterGroup() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    String groupName = "testGroup";
    DiagnosticGroup testGroup = new DiagnosticGroup(groupName, Collections.emptyList());
    diagnosticGroups.registerGroup(groupName, testGroup);
    DiagnosticGroup registeredTestGroup = diagnosticGroups.forName(groupName);
    assertNotNull(registeredTestGroup);
    assertEquals(groupName, registeredTestGroup.getName());
  }

  @Test
  public void testSetWarningLevels() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    CompilerOptions options = new CompilerOptions();
    List<String> diagnosticGroupsList = Arrays.asList("accessControls", "checkVars");
    diagnosticGroups.setWarningLevels(options, diagnosticGroupsList, CheckLevel.WARNING);
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(AccessControlsDiagnosticGroup.INSTANCE));
    assertEquals(CheckLevel.WARNING, options.getWarningLevel(CheckVarsDiagnosticGroup.INSTANCE));
  }

  private static class AccessControlsDiagnosticGroup extends DiagnosticGroup {
    public static final AccessControlsDiagnosticGroup INSTANCE = new AccessControlsDiagnosticGroup();

    private AccessControlsDiagnosticGroup() {
      super("accessControls");
    }
  }

  private static class AmbiguousFunctionDeclDiagnosticGroup extends DiagnosticGroup {
    public static final AmbiguousFunctionDeclDiagnosticGroup INSTANCE = new AmbiguousFunctionDeclDiagnosticGroup();

    private AmbiguousFunctionDeclDiagnosticGroup() {
      super("ambiguousFunctionDecl");
    }
  }

  private static class CheckRegExpDiagnosticGroup extends DiagnosticGroup {
    public static final CheckRegExpDiagnosticGroup INSTANCE = new CheckRegExpDiagnosticGroup();

    private CheckRegExpDiagnosticGroup() {
      super("checkRegExp");
    }
  }

  private static class CheckTypesDiagnosticGroup extends DiagnosticGroup {
    public static final CheckTypesDiagnosticGroup INSTANCE = new CheckTypesDiagnosticGroup();

    private CheckTypesDiagnosticGroup() {
      super("checkTypes");
    }
  }

  private static class ConstantPropertyDiagnosticGroup extends DiagnosticGroup {
    public static final ConstantPropertyDiagnosticGroup INSTANCE = new ConstantPropertyDiagnosticGroup();

    private ConstantPropertyDiagnosticGroup() {
      super("constantProperty");
    }
  }

  private static class DeprecatedDiagnosticGroup extends DiagnosticGroup {
    public static final DeprecatedDiagnosticGroup INSTANCE = new DeprecatedDiagnosticGroup();

    private DeprecatedDiagnosticGroup() {
      super("deprecated");
    }
  }

  private static class ExternsValidationDiagnosticGroup extends DiagnosticGroup {
    public static final ExternsValidationDiagnosticGroup INSTANCE = new ExternsValidationDiagnosticGroup();

    private ExternsValidationDiagnosticGroup() {
      super("externsValidation");
    }
  }

  private static class FileoverviewTagsDiagnosticGroup extends DiagnosticGroup {
    public static final FileoverviewTagsDiagnosticGroup INSTANCE = new FileoverviewTagsDiagnosticGroup();

    private FileoverviewTagsDiagnosticGroup() {
      super("fileoverviewTags");
    }
  }

  private static class GlobalThisDiagnosticGroup extends DiagnosticGroup {
    public static final GlobalThisDiagnosticGroup INSTANCE = new GlobalThisDiagnosticGroup();

    private GlobalThisDiagnosticGroup() {
      super("globalThis");
    }
  }

  private static class InvalidCastsDiagnosticGroup extends DiagnosticGroup {
    public static final InvalidCastsDiagnosticGroup INSTANCE = new InvalidCastsDiagnosticGroup();

    private InvalidCastsDiagnosticGroup() {
      super("invalidCasts");
    }
  }

  private static class InternetExplorerChecksDiagnosticGroup extends DiagnosticGroup {
    public static final InternetExplorerChecksDiagnosticGroup INSTANCE = new InternetExplorerChecksDiagnosticGroup();

    private InternetExplorerChecksDiagnosticGroup() {
      super("internetExplorerChecks");
    }
  }

  private static class MissingPropertiesDiagnosticGroup extends DiagnosticGroup {
    public static final MissingPropertiesDiagnosticGroup INSTANCE = new MissingPropertiesDiagnosticGroup();

    private MissingPropertiesDiagnosticGroup() {
      super("missingProperties");
    }
  }

  private static class NonStandardJsDocsDiagnosticGroup extends DiagnosticGroup {
    public static final NonStandardJsDocsDiagnosticGroup INSTANCE = new NonStandardJsDocsDiagnosticGroup();

    private NonStandardJsDocsDiagnosticGroup() {
      super("nonStandardJsDocs");
    }
  }

  private static class StrictModuleDepCheckDiagnosticGroup extends DiagnosticGroup {
    public static final StrictModuleDepCheckDiagnosticGroup INSTANCE = new StrictModuleDepCheckDiagnosticGroup();

    private StrictModuleDepCheckDiagnosticGroup() {
      super("strictModuleDepCheck");
    }
  }

  private static class TypeInvalidationDiagnosticGroup extends DiagnosticGroup {
    public static final TypeInvalidationDiagnosticGroup INSTANCE = new TypeInvalidationDiagnosticGroup();

    private TypeInvalidationDiagnosticGroup() {
      super("typeInvalidation");
    }
  }

  private static class UndefinedVarsDiagnosticGroup extends DiagnosticGroup {
    public static final UndefinedVarsDiagnosticGroup INSTANCE = new UndefinedVarsDiagnosticGroup();

    private UndefinedVarsDiagnosticGroup() {
      super("undefinedVars");
    }
  }

  private static class UselessCodeDiagnosticGroup extends DiagnosticGroup {
    public static final UselessCodeDiagnosticGroup INSTANCE = new UselessCodeDiagnosticGroup();

    private UselessCodeDiagnosticGroup() {
      super("uselessCode");
    }
  }

}