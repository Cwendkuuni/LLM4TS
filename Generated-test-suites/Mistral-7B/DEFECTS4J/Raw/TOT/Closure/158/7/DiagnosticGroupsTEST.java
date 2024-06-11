import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class DiagnosticGroupsTest {

  @Test
  public void testGetRegisteredGroups() {
    DiagnosticGroups diagnosticGroups = new DiagnosticGroups();
    Map<String, DiagnosticGroup> registeredGroups = diagnosticGroups.getRegisteredGroups();

    // Check that the registered groups are present in the map
    List<String> groupNames = java.util.Arrays.asList(
        "ACCESS_CONTROLS", "INVALID_CASTS", "FILEOVERVIEW_JSDOC",
        "STRICT_MODULE_DEP_CHECK", "EXTERNS_VALIDATION", "AMBIGUOUS_FUNCTION_DECL",
        "UNKNOWN_DEFINES", "TWEAKS", "MISSING_PROPERTIES", "INTERNET_EXPLORER_CHECKS",
        "UNDEFINED_VARIABLES", "CHECK_REGEXP", "CHECK_TYPES", "CHECK_VARIABLES",
        "CHECK_USELESS_CODE", "TYPE_INVALIDATION", "GLOBAL_THIS", "DEPRECATED",
        "VISIBILITY", "CONSTANT_PROPERTY", "NON_STANDARD_JSDOC"
    );

    for (String groupName : groupNames) {
      DiagnosticGroup group = registeredGroups.get(groupName);
      assertNotNull("Missing registered group: " + groupName, group);
    }
  }
}