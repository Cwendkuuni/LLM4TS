import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

  @Test
  public void testAddOption() {
    Option option = new Option("test");
    WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    cmdLine.addOption(option);
    assertTrue(cmdLine.hasOption(option));
  }

  @Test
  public void testAddValue() {
    Option option = new Option("test");
    WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    cmdLine.addValue(option, "value1");
    assertEquals("value1", cmdLine.getValues(option, new ArrayList<>()).get(0));
  }

  @Test
  public void testAddSwitch() {
    Option option = new Option("test");
    WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    cmdLine.addSwitch(option, true);
    assertTrue(cmdLine.getSwitch(option, false));
  }

  @Test
  public void testHasOption() {
    Option option = new Option("test");
    WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    cmdLine.addOption(option);
    assertTrue(cmdLine.hasOption(option));
  }

  @Test
  public void testGetOption() {
    Option option = new Option("test");
    WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    cmdLine.addOption(option);
    assertEquals(option, cmdLine.getOption("test"));
  }

  @Test
  public void testGetValues() {
    Option option = new Option("test");
    WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    cmdLine.addValue(option, "value1");
    assertEquals("value1", cmdLine.getValues(option, new ArrayList<>()).get(0));
  }

  @Test
  public void testGetSwitch() {
    Option option = new Option("test");
    WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    cmdLine.addSwitch(option, true);
    assertTrue(cmdLine.getSwitch(option, false));
  }

  @Test
  public void testAddProperty() {
    Option option = new Option("test");
    WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    cmdLine.addProperty("property1", "value1");
    assertEquals("value1", cmdLine.getProperty("property1", ""));
  }

  @Test
  public void testGetProperty() {
    Option option = new Option("test");
    WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    cmdLine.addProperty("property1", "value1");
    assertEquals("value1", cmdLine.getProperty("property1", ""));
  }

  @Test
  public void testGetProperties() {
    Option option = new Option("test");
    WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    cmdLine.addProperty("property1", "value1");
    assertEquals(1, cmdLine.getProperties().size());
  }

  @Test
  public void testLooksLikeOption() {
    Option option = new Option("test");
    WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    assertTrue(cmdLine.looksLikeOption("-test"));
  }

  @Test
  public void testToString() {
    Option option = new Option("test");
    WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    cmdLine.addValue(option, "value1");
    assertEquals("value1", cmdLine.toString());
  }

  @Test
  public void testGetOptions() {
    Option option = new Option("test");
    WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    cmdLine.addOption(option);
    assertEquals(1, cmdLine.getOptions().size());
  }

  @Test
  public void testGetOptionTriggers() {
    Option option = new Option("test");
    WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    cmdLine.addOption(option);
    assertEquals(1, cmdLine.getOptionTriggers().size());
  }

  @Test
  public void testSetDefaultValues() {
    Option option = new Option("test");
    WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    cmdLine.setDefaultValues(option, new ArrayList<>());
    assertNull(cmdLine.getValues(option, new ArrayList<>()));
  }

  @Test
  public void testSetDefaultSwitch() {
    Option option = new Option("test");
    WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    cmdLine.setDefaultSwitch(option, true);
    assertTrue(cmdLine.getSwitch(option, false));
  }

  @Test
  public void testGetNormalised() {
    Option option = new Option("test");
    WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    assertEquals(0, cmdLine.getNormalised().size());
  }

}