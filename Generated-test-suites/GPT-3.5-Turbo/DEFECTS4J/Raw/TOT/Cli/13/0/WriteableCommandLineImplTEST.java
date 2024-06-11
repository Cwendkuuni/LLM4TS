import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class WriteableCommandLineImplTest {

  @Test
  public void testAddOption() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<String>());

    writeableCommandLine.addOption(option);
  
    assertTrue(writeableCommandLine.hasOption(option));
  }

  @Test
  public void testAddValue() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<String>());

    writeableCommandLine.addValue(option, "value1");
  
    List<String> expected = new ArrayList<>(Arrays.asList("value1"));
    assertEquals(expected, writeableCommandLine.getValues(option, new ArrayList<>()));
  }

  @Test
  public void testAddSwitch() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<String>());

    writeableCommandLine.addSwitch(option, true);
  
    assertTrue(writeableCommandLine.getSwitch(option, false));
  }

  @Test
  public void testHasOption() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<String>());

    assertFalse(writeableCommandLine.hasOption(option));
  }

  @Test
  public void testGetOption() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<String>());

    assertNull(writeableCommandLine.getOption("testOption"));
  }

  @Test
  public void testGetValues() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<String>());

    List<String> expected = new ArrayList<>(Arrays.asList("value1", "value2"));
    writeableCommandLine.addValue(option, "value1");
    writeableCommandLine.addValue(option, "value2");
  
    assertEquals(expected, writeableCommandLine.getValues(option, new ArrayList<>()));
  }

  @Test
  public void testGetSwitch() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<String>());

    assertTrue(writeableCommandLine.getSwitch(option, true));
  }

  @Test
  public void testAddProperty() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<String>());

    writeableCommandLine.addProperty("property1", "value1");
  
    assertEquals("value1", writeableCommandLine.getProperty("property1", ""));
  }

  @Test
  public void testGetProperty() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<String>());

    assertEquals("defaultValue", writeableCommandLine.getProperty("property1", "defaultValue"));
  }

  @Test
  public void testGetProperties() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<String>());
    writeableCommandLine.addProperty("property1", "value1");

    Properties expected = new Properties();
    expected.setProperty("property1", "value1");
  
    assertEquals(expected.keySet(), writeableCommandLine.getProperties());
  }

  @Test
  public void testLooksLikeOption() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<String>());

    assertTrue(writeableCommandLine.looksLikeOption("-testOption"));
    assertFalse(writeableCommandLine.looksLikeOption("testOption"));
  }

  @Test
  public void testToString() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<String>());
    writeableCommandLine.addValue(option, "value1");
    writeableCommandLine.addValue(option, "value2");
    writeableCommandLine.addProperty("property1", "value1");

    String expected = "value1 value2";
  
    assertEquals(expected, writeableCommandLine.toString());
  }

  @Test
  public void testGetOptions() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<String>());
    writeableCommandLine.addOption(option);

    List<Option> expected = new ArrayList<>();
    expected.add(option);
  
    assertEquals(expected, writeableCommandLine.getOptions());
  }

  @Test
  public void testGetOptionTriggers() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<String>());
    writeableCommandLine.addOption(option);

    List<String> expected = new ArrayList<>(Arrays.asList("testOption"));
  
    assertEquals(expected, writeableCommandLine.getOptionTriggers());
  }

  @Test
  public void testSetDefaultValues() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<String>());
    List<String> defaults = new ArrayList<>(Arrays.asList("default1", "default2"));

    writeableCommandLine.setDefaultValues(option, defaults);
  
    assertEquals(defaults, writeableCommandLine.getValues(option, new ArrayList<>()));
  }

  @Test
  public void testSetDefaultSwitch() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<String>());

    writeableCommandLine.setDefaultSwitch(option, true);
  
    assertTrue(writeableCommandLine.getSwitch(option, false));
  }

  @Test
  public void testGetNormalised() {
    Option option = new Option("testOption");
    WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<String>());
    List<String> arguments = new ArrayList<>(Arrays.asList("arg1", "arg2"));

    List<String> expected = new ArrayList<>(Arrays.asList("arg1", "arg2"));
    writeableCommandLine = new WriteableCommandLineImpl(option, arguments);
  
    assertEquals(expected, writeableCommandLine.getNormalised());
  }
}