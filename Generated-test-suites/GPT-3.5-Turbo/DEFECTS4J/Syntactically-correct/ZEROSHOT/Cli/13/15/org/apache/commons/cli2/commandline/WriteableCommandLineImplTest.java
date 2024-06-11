import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
  
  @Test
  public void testAddOption() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
    Option option = new Option.Builder("test").create();
    commandLine.addOption(option);
    assertTrue(commandLine.getOptions().contains(option));
  }
  
  @Test
  public void testAddValue() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
    Option option = new Option.Builder("test").create();
    Object value = "value";
    commandLine.addValue(option, value);
    assertTrue(commandLine.getValues(option, null).contains(value));
  }
  
  @Test
  public void testAddSwitch() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
    Option option = new Option.Builder("test").create();
    boolean value = true;
    commandLine.addSwitch(option, value);
    assertEquals(value, commandLine.getSwitch(option, null));
  }
  
  @Test
  public void testHasOption() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
    Option option = new Option.Builder("test").create();
    commandLine.addOption(option);
    assertTrue(commandLine.hasOption(option));
  }
  
  @Test
  public void testGetOption() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
    Option option = new Option.Builder("test").create();
    commandLine.addOption(option);
    assertEquals(option, commandLine.getOption("test"));
  }
  
  @Test
  public void testGetValues() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
    Option option = new Option.Builder("test").create();
    List<String> values = new ArrayList<>();
    values.add("value1");
    values.add("value2");
    commandLine.addValue(option, "value1");
    commandLine.addValue(option, "value2");
    assertEquals(values, commandLine.getValues(option, null));
  }
  
  @Test
  public void testGetSwitch() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
    Option option = new Option.Builder("test").create();
    boolean value = true;
    commandLine.addSwitch(option, value);
    assertEquals(value, commandLine.getSwitch(option, null));
  }
  
  @Test
  public void testAddProperty() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
    String property = "testProperty";
    String value = "testValue";
    commandLine.addProperty(property, value);
    assertEquals(value, commandLine.getProperty(property, null));
  }
  
  @Test
  public void testLooksLikeOption() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
    commandLine.addOption(new Option.Builder("test").create());
    assertTrue(commandLine.looksLikeOption("test"));
  }
  
  @Test
  public void testToString() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
    commandLine.addOption(new Option.Builder("test").create());
    commandLine.addOption(new Option.Builder("test2").create());
    commandLine.addValue(new Option.Builder("test").create(), "value");
    commandLine.addValue(new Option.Builder("test2").create(), "value2");
    assertEquals("test value test2 value2", commandLine.toString());
  }
  
  @Test
  public void testGetOptionTriggers() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
    Option option = new Option.Builder("test").create();
    commandLine.addOption(option);
    assertTrue(commandLine.getOptionTriggers().contains("test"));
  }
  
  @Test
  public void testSetDefaultValues() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
    Option option = new Option.Builder("test").create();
    List<String> defaults = new ArrayList<>();
    defaults.add("default1");
    defaults.add("default2");
    commandLine.setDefaultValues(option, defaults);
    assertEquals(defaults, commandLine.getValues(option, null));
  }
  
  @Test
  public void testSetDefaultSwitch() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
    Option option = new Option.Builder("test").create();
    Boolean defaultSwitch = true;
    commandLine.setDefaultSwitch(option, defaultSwitch);
    assertEquals(defaultSwitch, commandLine.getSwitch(option, null));
  }
  
  @Test
  public void testGetNormalised() {
    WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
    List<String> normalised = new ArrayList<>();
    normalised.add("test");
    normalised.add("value");
    commandLine.addValue(new Option.Builder("test").create(), "value");
    assertEquals(normalised, commandLine.getNormalised());
  }
  
}