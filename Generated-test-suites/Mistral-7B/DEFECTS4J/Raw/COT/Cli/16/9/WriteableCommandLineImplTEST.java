import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

  @Test
  public void testAddOption() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option().withLongOpt("option1"), new ArrayList());
    cmd.addOption(new Option().withLongOpt("option2"));

    assertEquals(2, cmd.getOptions().size());
  }

  @Test
  public void testAddValue() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option().withLongOpt("option"), new ArrayList());
    cmd.addOption(new Option().withLongOpt("option"));
    cmd.addValue(cmd.getOption("option"), "value1");
    cmd.addValue(cmd.getOption("option"), "value2");

    List values = cmd.getValues(cmd.getOption("option"), null);
    assertEquals(2, values.size());
    assertTrue(values.contains("value1"));
    assertTrue(values.contains("value2"));
  }

  @Test
  public void testAddSwitch() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option().withLongOpt("option"), new ArrayList());
    cmd.addOption(new Option().withLongOpt("option"));
    cmd.addSwitch(cmd.getOption("option"), false);
    cmd.addSwitch(cmd.getOption("option"), true);

    assertEquals(Boolean.FALSE, cmd.getSwitch(cmd.getOption("option"), Boolean.TRUE));
    assertEquals(Boolean.TRUE, cmd.getSwitch(cmd.getOption("option"), Boolean.FALSE));
  }

  @Test(expected = IllegalStateException.class)
  public void testAddSwitchAlreadySet() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option().withLongOpt("option"), new ArrayList());
    cmd.addOption(new Option().withLongOpt("option"));
    cmd.addSwitch(cmd.getOption("option"), false);
    cmd.addSwitch(cmd.getOption("option"), false);
  }

  @Test
  public void testHasOption() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option().withLongOpt("option"), new ArrayList());
    cmd.addOption(new Option().withLongOpt("option"));

    assertTrue(cmd.hasOption(cmd.getOption("option")));
  }

  @Test
  public void testGetOption() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option().withLongOpt("option"), new ArrayList());
    cmd.addOption(new Option().withLongOpt("option"));

    Option option = cmd.getOption("option");
    assertEquals("option", option.getLongOpt());
  }

  @Test
  public void testGetValues() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option().withLongOpt("option"), new ArrayList());
    cmd.addOption(new Option().withLongOpt("option"));
    cmd.addValue(cmd.getOption("option"), "value1");
    cmd.addValue(cmd.getOption("option"), "value2");

    List values = cmd.getValues(cmd.getOption("option"), null);
    assertEquals(2, values.size());
  }

  @Test
  public void testGetUndefaultedValues() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option().withLongOpt("option"), new ArrayList());
    cmd.addOption(new Option().withLongOpt("option"));
    cmd.addValue(cmd.getOption("option"), "value1");

    List values = cmd.getUndefaultedValues(cmd.getOption("option"));
    assertEquals(0, values.size());
  }

  @Test
  public void testGetSwitch() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option().withLongOpt("option"), new ArrayList());
    cmd.addOption(new Option().withLongOpt("option"));
    cmd.addSwitch(cmd.getOption("option"), true);

    assertTrue(cmd.getSwitch(cmd.getOption("option"), false));
    assertTrue(cmd.getSwitch(cmd.getOption("option"), true));
  }

  @Test
  public void testGetProperty() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option().withLongOpt("option"), new ArrayList());
    cmd.addOption(new Option().withLongOpt("option"));
    cmd.addProperty(cmd.getOption("option"), "property", "value");

    assertEquals("value", cmd.getProperty(cmd.getOption("option"), "value", null));
  }

  @Test
  public void testAddProperty() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option().withLongOpt("option"), new ArrayList());
    cmd.addOption(new Option().withLongOpt("option"));
    cmd.addProperty(cmd.getOption("option"), "property1", "value1");
    cmd.addProperty(cmd.getOption("option"), "property2", "value2");

    Set properties = cmd.getProperties(cmd.getOption("option"));
    assertEquals(2, properties.size());
  }

  @Test
  public void testGetProperties() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option().withLongOpt("option"), new ArrayList());
    cmd.addOption(new Option().withLongOpt("option"));
    cmd.addProperty(cmd.getOption("option"), "property1", "value1");
    cmd.addProperty(cmd.getOption("option"), "property2", "value2");

    Set properties = cmd.getProperties();
    assertEquals(0, properties.size());
  }

  @Test
  public void testToString() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option().withLongOpt("option"), new ArrayList());
    cmd.addOption(new Option().withLongOpt("option"));
    cmd.addValue(cmd.getOption("option"), "value1");

    String str = cmd.toString();
    String expected = "value1";

    assertTrue(str.contains(expected));
  }

  @Test
  public void testLooksLikeOption() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option().withLongOpt("option"), new ArrayList());
    cmd.addOption(new Option().withLongOpt("option"));

    assertTrue(cmd.looksLikeOption("option"));
  }

  @Test
  public void testGetOptions() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option().withLongOpt("option"), new ArrayList());
    cmd.addOption(new Option().withLongOpt("option"));

    List options = cmd.getOptions();
    assertEquals(1, options.size());
  }

  @Test
  public void testGetOptionTriggers() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option().withLongOpt("option"), new ArrayList());
    cmd.addOption(new Option().withLongOpt("option"));

    Set triggers = cmd.getOptionTriggers();
    assertEquals(1, triggers.size());
  }

  @Test
  public void testSetDefaultValues() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option().withLongOpt("option"), new ArrayList());
    cmd.addOption(new Option().withLongOpt("option"));
    List defaults = new ArrayList();
    defaults.add("default1");
    defaults.add("default2");
    cmd.setDefaultValues(cmd.getOption("option"), defaults);

    List values = cmd.getValues(cmd.getOption("option"), null);
    assertEquals(2, values.size());
    assertTrue(values.contains("default1"));
    assertTrue(values.contains("default2"));
  }

  @Test
  public void testSetDefaultSwitch() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option().withLongOpt("option"), new ArrayList());
    cmd.addOption(new Option().withLongOpt("option"));
    cmd.setDefaultSwitch(cmd.getOption("option"), Boolean.TRUE);

    assertTrue(cmd.getSwitch(cmd.getOption("option"), Boolean.FALSE));
    assertTrue(cmd.getSwitch(cmd.getOption("option"), Boolean.TRUE));
  }

  @Test
  public void testGetNormalised() {
    WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(new Option().withLongOpt("option"), new ArrayList());
    cmd.addOption(new Option().withLongOpt("option"));
    cmd.addValue(cmd.getOption("option"), "value1");

    List normalised = cmd.getNormalised();
    assertEquals(1, normalised.size());
    assertEquals("value1", normalised.get(0));
  }

}