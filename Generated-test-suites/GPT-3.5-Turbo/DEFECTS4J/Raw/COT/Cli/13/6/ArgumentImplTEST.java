import static org.junit.Assert.*;
import org.junit.Test;

public class ArgumentImplTest {

  @Test
  public void testGetPreferredName() {
    ArgumentImpl arg = new ArgumentImpl("name", "description", 1, 1, '=', ',', null, null, null, 0);
    assertEquals("name", arg.getPreferredName());
  }

  @Test
  public void testGetInitialSeparator() {
    ArgumentImpl arg = new ArgumentImpl("name", "description", 1, 1, '=', ',', null, null, null, 0);
    assertEquals('=', arg.getInitialSeparator());
  }

  @Test
  public void testGetSubsequentSeparator() {
    ArgumentImpl arg = new ArgumentImpl("name", "description", 1, 1, '=', ',', null, null, null, 0);
    assertEquals(',', arg.getSubsequentSeparator());
  }

  @Test
  public void testGetConsumeRemaining() {
    ArgumentImpl arg = new ArgumentImpl("name", "description", 1, 1, '=', ',', null, "--", null, 0);
    assertEquals("--", arg.getConsumeRemaining());
  }

  @Test
  public void testGetDefaultValues() {
    List<String> defaultValues = new ArrayList<>();
    defaultValues.add("default1");
    defaultValues.add("default2");
    ArgumentImpl arg = new ArgumentImpl("name", "description", 1, 1, '=', ',', null, null, defaultValues, 0);
    assertEquals(defaultValues, arg.getDefaultValues());
  }

  @Test
  public void testIsRequired() {
    ArgumentImpl arg1 = new ArgumentImpl("name", "description", 1, 1, '=', ',', null, null, null, 0);
    assertTrue(arg1.isRequired());

    ArgumentImpl arg2 = new ArgumentImpl("name", "description", 0, 1, '=', ',', null, null, null, 0);
    assertFalse(arg2.isRequired());
  }
}