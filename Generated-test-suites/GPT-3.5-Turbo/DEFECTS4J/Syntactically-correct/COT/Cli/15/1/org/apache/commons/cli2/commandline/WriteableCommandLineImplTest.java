
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        // Test addOption() functionality
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new OptionImpl("test");
        cmdLine.addOption(option);

        assertTrue(cmdLine.hasOption(option));
        assertEquals(option, cmdLine.getOption("test"));
    }

    @Test
    public void testAddValue() {
        // Test addValue() functionality
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new OptionImpl("test");
        cmdLine.addValue(option, "value");

        List<String> values = cmdLine.getValues(option, null);
        assertEquals("value", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        // Test addSwitch() functionality
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new OptionImpl("test");
        cmdLine.addSwitch(option, true);

        Boolean switchValue = cmdLine.getSwitch(option, null);
        assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        // Test hasOption() functionality
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new OptionImpl("test");
        cmdLine.addOption(option);

        assertTrue(cmdLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        // Test getOption() functionality
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new OptionImpl("test");
        cmdLine.addOption(option);

        Option retrievedOption = cmdLine.getOption("test");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        // Test getValues() functionality
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new OptionImpl("test");
        List<String> defaultValues = Arrays.asList("default1", "default2");
        cmdLine.setDefaultValues(option, defaultValues);
        cmdLine.addValue(option, "value");

        List<String> values = cmdLine.getValues(option, null);
        assertEquals("value", values.get(0));

        values = cmdLine.getValues(option, Arrays.asList("default3"));
        assertEquals("value", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
      // Test getUndefaultedValues() functionality
      WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
      Option option = new OptionImpl("test");
      cmdLine.addValue(option, "value");

      List<String> values = cmdLine.getUndefaultedValues(option);
      assertEquals("value", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        // Test getSwitch() functionality
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new OptionImpl("test");
        cmdLine.addSwitch(option, true);

        Boolean switchValue = cmdLine.getSwitch(option, null);
        assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        // Test getProperty() functionality
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        String property = "test";
        String value = "value";
        cmdLine.addProperty(property, value);

        String retrievedValue = cmdLine.getProperty(property, null);
        assertEquals(value, retrievedValue);
    }

    @Test
    public void testAddProperty() {
        // Test addProperty() functionality
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new OptionImpl("test");
        String property = "test";
        String value = "value";
        cmdLine.addProperty(option, property, value);

        String retrievedValue = cmdLine.getProperty(option, property, null);
        assertEquals(value, retrievedValue);
    }

    @Test
    public void testGetProperties() {
        // Test getProperties() functionality
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new OptionImpl("test");
        String property = "test";
        String value = "value";
        cmdLine.addProperty(option, property, value);

        Set<String> properties = cmdLine.getProperties(option);
        assertEquals(Collections.singleton(property), properties);
    }

    @Test
    public void testLooksLikeOption() {
        // Test looksLikeOption() functionality
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        String trigger = "-t";

        assertTrue(cmdLine.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        // Test toString() functionality
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        List<String> arguments = Arrays.asList("arg1", "arg2");
        cmdLine = new WriteableCommandLineImpl(null, arguments);

        String expectedString = "arg1 arg2";
        assertEquals(expectedString, cmdLine.toString());
    }

    @Test
    public void testGetOptions() {
        // Test getOptions() functionality
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option1 = new OptionImpl("test1");
        Option option2 = new OptionImpl("test2");
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);

        List<Option> options = cmdLine.getOptions();
        assertEquals(Arrays.asList(option1, option2), options);
    }

    @Test
    public void testGetOptionTriggers() {
        // Test getOptionTriggers() functionality
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option1 = new OptionImpl("test1");
        Option option2 = new OptionImpl("test2");
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);

        Set<String> triggers = cmdLine.getOptionTriggers();
        assertEquals(new HashSet<String>(Arrays.asList("test1", "test2")), triggers);
    }

    @Test
    public void testSetDefaultValues() {
        // Test setDefaultValues() functionality
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new OptionImpl("test");
        List<String> defaultValues = Arrays.asList("default1", "default2");
        cmdLine.setDefaultValues(option, defaultValues);
        cmdLine.addValue(option, "value");

        List<String> values = cmdLine.getValues(option, Arrays.asList("default3"));
        assertEquals("value", values.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        // Test setDefaultSwitch() functionality
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        Option option = new OptionImpl("test");
        Boolean defaultSwitch = true;
        cmdLine.setDefaultSwitch(option, defaultSwitch);
        cmdLine.addSwitch(option, false);

        Boolean switchValue = cmdLine.getSwitch(option, null);
        assertFalse(switchValue);
    }

    @Test
    public void testGetNormalised() {
        // Test getNormalised() functionality
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(null, null);
        List<String> arguments = Arrays.asList("arg1", "arg2");
        cmdLine = new WriteableCommandLineImpl(null, arguments);

        List<String> normalised = cmdLine.getNormalised();
        assertEquals(arguments, normalised);
    }
}
