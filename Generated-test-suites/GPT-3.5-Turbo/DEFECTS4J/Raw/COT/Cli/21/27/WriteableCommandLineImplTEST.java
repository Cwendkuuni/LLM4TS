import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Test;

public class WriteableCommandLineImplTest {
  
    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Option option = new Option();
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }
  
    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Option option = new Option();
        commandLine.addValue(option, "value");
        assertTrue(commandLine.getValues(option, null).contains("value"));
    }
  
    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Option option = new Option();
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }
  
    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Option option = new Option();
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
  
    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Option option = new Option();
        option.setPreferredName("option");
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("option"));
    }
  
    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Option option = new Option();
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals(2, commandLine.getValues(option, null).size());
    }
  
    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Option option = new Option();
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals(2, commandLine.getUndefaultedValues(option).size());
    }
  
    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Option option = new Option();
        commandLine.addSwitch(option, false);
        assertFalse(commandLine.getSwitch(option, null));
    }
  
    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        assertEquals(null, commandLine.getProperty("property"));
    }
  
    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property"));
    }
  
    @Test
    public void testGetPropertyWithDefault() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        assertEquals("default", commandLine.getProperty("property", "default"));
    }
  
    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        assertEquals(0, commandLine.getProperties().size());
    }
  
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        assertTrue(commandLine.looksLikeOption("-option"));
    }
  
    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        commandLine.addValue(new Argument(), "value");
        assertEquals("value", commandLine.toString());
    }
  
    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Option option = new Option();
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }
  
    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Option option = new Option();
        option.setPreferredName("option");
        commandLine.addOption(option);
        assertTrue(commandLine.getOptionTriggers().contains("option"));
    }
  
    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Option option = new Option();
        List defaults = new ArrayList();
        defaults.add("default1");
        defaults.add("default2");
        commandLine.setDefaultValues(option, defaults);
        assertEquals(2, commandLine.getValues(option, null).size());
    }
  
    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        Option option = new Option();
        commandLine.setDefaultSwitch(option, false);
        assertFalse(commandLine.getSwitch(option, null));
    }
  
    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList());
        List normalisedList = new ArrayList();
        normalisedList.add("value1");
        normalisedList.add("value2");
        assertEquals(normalisedList, commandLine.getNormalised());
    }
}