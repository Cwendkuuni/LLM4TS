import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {

  @Test
  public void testGetPreferredName() {
    ArgumentImpl arg = new ArgumentImpl("", "", 0, 0, '\0', '\0', null, "", null, 0);
    assertEquals("arg", arg.getPreferredName());
  }

  @Test
  public void testGetPrefixes() {
    ArgumentImpl arg = new ArgumentImpl("", "", 0, 0, '\0', '\0', null, "", null, 0);
    assertEquals(Collections.EMPTY_SET, arg.getPrefixes());
  }

  @Test
  public void testGetInitialSeparator() {
    ArgumentImpl arg = new ArgumentImpl("", "", 0, 0, '\0', '\0', null, "", null, 0);
    assertEquals('\0', arg.getInitialSeparator());
  }

  @Test
  public void testGetSubsequentSeparator() {
    ArgumentImpl arg = new ArgumentImpl("", "", 0, 0, '\0', '\0', null, "", null, 0);
    assertEquals('\0', arg.getSubsequentSeparator());
  }

  @Test
  public void testGetTriggers() {
    ArgumentImpl arg = new ArgumentImpl("", "", 0, 0, '\0', '\0', null, "", null, 0);
    assertEquals(Collections.EMPTY_SET, arg.getTriggers());
  }

  @Test
  public void testGetConsumeRemaining() {
    ArgumentImpl arg = new ArgumentImpl("", "", 0, 0, '\0', '\0', null, "--", null, 0);
    assertEquals("--", arg.getConsumeRemaining());
  }

  @Test
  public void testGetValidator() {
    ArgumentImpl arg = new ArgumentImpl("", "", 0, 0, '\0', '\0', null, "", null, 0);
    assertNull(arg.getValidator());
  }

  @Test
  public void testGetDefaultValues() {
    ArgumentImpl arg = new ArgumentImpl("", "", 0, 0, '\0', '\0', null, "", null, 0);
    assertNull(arg.getDefaultValues());
  }

  @Test
  public void testValidate() {
    ArgumentImpl arg = new ArgumentImpl("", "", 0, 0, '\0', '\0', null, "", null, 0);
    //TODO: Add test case for validate()
  }

  @Test
  public void testAppendUsage() {
    ArgumentImpl arg = new ArgumentImpl("", "", 0, 0, '\0', '\0', null, "", null, 0);
    //TODO: Add test case for appendUsage()
  }

  @Test
  public void testGetDescription() {
    ArgumentImpl arg = new ArgumentImpl("", "description", 0, 0, '\0', '\0', null, "", null, 0);
    assertEquals("description", arg.getDescription());
  }

  @Test
  public void testGetMaximum() {
    ArgumentImpl arg = new ArgumentImpl("", "", 0, 5, '\0', '\0', null, "", null, 0);
    assertEquals(5, arg.getMaximum());
  }

  @Test
  public void testGetMinimum() {
    ArgumentImpl arg = new ArgumentImpl("", "", 2, 5, '\0', '\0', null, "", null, 0);
    assertEquals(2, arg.getMinimum());
  }

  @Test
  public void testDefaults() {
    ArgumentImpl arg = new ArgumentImpl("", "", 0, 0, '\0', '\0', null, "", null, 0);
    //TODO: Add test case for defaults()
  }

}