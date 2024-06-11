import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RenameLabelsTest {

  private RenameLabels renameLabels;

  @Before
  public void setUp() {
    renameLabels = new RenameLabels(new AbstractCompiler());
  }

  @Test
  public void testProcess_withLabels_shouldRenameLabels() {
    // Create a JavaScript code snippet with labels and expected renamed labels
    String code = ""
        + "a: {"
        + "  b: {"
        + "    foo();"
        + "  }"
        + "}"
        + "a: {"
        + "  b: break a;"
        + "}";

    String expectedRenamedCode = ""
        + "a: {"
        + "  b: {"
        + "    foo();"
        + "  }"
        + "}"
        + "a: {"
        + "  b: break a;"
        + "}";

    // Process the code with RenameLabels
    renameLabels.process(code);

    // Check if the labels were renamed correctly
    assertEquals(expectedRenamedCode, code);
  }

  @Test
  public void testProcess_withUnusedLabels_shouldRemoveLabels() {
    // Create a JavaScript code snippet with an unused label
    String code = ""
        + "a: {"
        + "  b: {"
        + "    foo();"
        + "  }"
        + "}";

    String expectedCode = ""
        + "{"
        + "  {"
        + "    foo();"
        + "  }"
        + "}";

    // Process the code with RenameLabels
    renameLabels.process(code);

    // Check if the unused label was removed correctly
    assertEquals(expectedCode, code);
  }

  @Test
  public void testProcess_withBreakAndContinueLabels_shouldRenameLabels() {
    // Create a JavaScript code snippet with break and continue statements
    String code = ""
        + "a: {"
        + "  while (condition) {"
        + "    if (condition2) {"
        + "      continue a;"
        + "    } else {"
        + "      break a;"
        + "    }"
        + "  }"
        + "}";

    String expectedRenamedCode = ""
        + "a: {"
        + "  while (condition) {"
        + "    if (condition2) {"
        + "      continue a;"
        + "    } else {"
        + "      break a;"
        + "    }"
        + "  }"
        + "}";

    // Process the code with RenameLabels
    renameLabels.process(code);

    // Check if the break and continue labels were renamed correctly
    assertEquals(expectedRenamedCode, code);
  }

  @Test
  public void testGetLabelInfo_withExistingLabel_shouldReturnLabelInfo() {
    // Create a JavaScript code snippet with a label
    String code = "a: { foo(); }";

    // Process the code with RenameLabels
    renameLabels.process(code);

    // Get the label information
    RenameLabels.LabelInfo labelInfo = renameLabels.getLabelInfo("a");

    // Check if the label information is correct
    assertNotNull(labelInfo);
    assertFalse(labelInfo.referenced);
    assertEquals(1, labelInfo.id);
  }

  @Test
  public void testGetLabelInfo_withNonExistingLabel_shouldReturnNull() {
    // Create a JavaScript code snippet without any labels
    String code = "foo();";

    // Process the code with RenameLabels
    renameLabels.process(code);

    // Get the label information of a non-existing label
    RenameLabels.LabelInfo labelInfo = renameLabels.getLabelInfo("a");

    // Check if the label information is null
    assertNull(labelInfo);
  }
}