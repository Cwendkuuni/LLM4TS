import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GroupImplTest {

  private GroupImpl group;
  
  @Before
  public void setUp() {
    group = new GroupImpl(null, "group", "description", 0, 1, false);
  }

  @Test
  public void testCanProcess() {
    assertFalse(group.canProcess(null, null));
    assertFalse(group.canProcess(null, "arg"));
    assertFalse(group.canProcess(null, "-opt"));
    
    assertTrue(group.canProcess(null, "group"));
  }

  @Test
  public void testGetPrefixes() {
    assertNotNull(group.getPrefixes());
  }

  @Test
  public void testGetTriggers() {
    assertNotNull(group.getTriggers());
  }

  @Test
  public void testValidate() {
    try {
      group.validate(null);
    } catch (OptionException e) {
      fail("Unexpected exception: " + e);
    }
  }

  @Test
  public void testGetPreferredName() {
    assertEquals("group", group.getPreferredName());
  }

  @Test
  public void testGetDescription() {
    assertEquals("description", group.getDescription());
  }

  @Test
  public void testAppendUsage() {
    StringBuffer buffer = new StringBuffer();
    group.appendUsage(buffer, null, null);
    assertEquals("[group]", buffer.toString());
  }

  @Test
  public void testHelpLines() {
    assertNotNull(group.helpLines(0, null, null));
  }

  @Test
  public void testGetOptions() {
    assertNotNull(group.getOptions());
  }

  @Test
  public void testGetAnonymous() {
    assertNotNull(group.getAnonymous());
  }

  @Test
  public void testFindOption() {
    assertNull(group.findOption("trigger"));
  }

  @Test
  public void testGetMinimum() {
    assertEquals(0, group.getMinimum());
  }

  @Test
  public void testGetMaximum() {
    assertEquals(1, group.getMaximum());
  }

  @Test
  public void testIsRequired() {
    assertFalse(group.isRequired());
  }

  @Test
  public void testDefaults() {
    group.defaults(null);
  }
}