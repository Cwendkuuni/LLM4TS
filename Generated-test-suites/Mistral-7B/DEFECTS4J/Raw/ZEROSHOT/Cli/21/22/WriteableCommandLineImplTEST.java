import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("b", true, "bValue");
        commandLine.addOption(option);
        List options = commandLine.getOptions();
        assertTrue(options.contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", true, "aValue");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", false, "aSwitch");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", true, "aValue");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", true, "aValue");
        commandLine.addOption(option);
        Option returnedOption = commandLine.getOption("a");
        assertEquals(option, returnedOption);
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", true, "aValue");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", true, "aValue");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", false, "aSwitch");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
        switchValue = commandLine.getSwitch(option, null);
        assertNull(switchValue);
    }

    @Test
    public void testgetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", true, "aValue");
        commandLine.addOption(option);
        commandLine.addProperty(option, "propName", "propValue");
        String propertyValue = commandLine.getProperty("propName");
        assertEquals("propValue", propertyValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", true, "aValue");
        commandLine.addOption(option);
        commandLine.addProperty(option, "propName", "propValue");
        Map<String, String> properties = commandLine.getProperties(option);
        assertEquals(1, properties.size());
        assertEquals("propValue", properties.get("propName"));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option = new Option("a", true, "aValue");
        commandLine.addOption(option);
        commandLine.addProperty(option, "propName1", "propValue1");
        commandLine.addProperty(option, "propName2", "propValue2");
        Map<String, String> properties = commandLine.getProperties(option);
        assertEquals(2, properties.size());
        assertEquals("propValue1", properties.get("propName1"));
        assertEquals("propValue2", properties.get("propName2"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        commandLine.addOption(new Option("--b", true, "bValue"));
        commandLine.addOption(new Option("--c", false, "cSwitch"));
        commandLine.addValue(new Option("--b"), "testValueB");
        commandLine.addValue(new Option("--b"), "testValueC");
        commandLine.addSwitch(new Option("--c"), true);
        String commandLineString = commandLine.toString();
        String expectedCommandLineString = "--a --b testValueB testValueC --c";
        assertEquals(expectedCommandLineString, commandLineString);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option1 = new Option("--b", true, "bValue");
        Option option2 = new Option("--c", false, "cSwitch");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List<Option> options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        Option option1 = new Option("--b", true, "bValue");
        Option option2 = new Option("--c", false, "cSwitch");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Set<String> optionTriggers = commandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("--b"));
        assertTrue(optionTriggers.contains("--c"));
    }
}