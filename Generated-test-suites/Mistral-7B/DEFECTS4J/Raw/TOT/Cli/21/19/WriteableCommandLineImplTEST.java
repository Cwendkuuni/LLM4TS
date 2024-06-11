import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option optionWithValue;
    private Option optionWithSwitch;
    private Option optionWithProperty;

    @Before
    public void setUp() {
        Option rootOption = new Option("root", "rootOption", true, "root description");
        commandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        optionWithValue = new Option("value", "valueOption", true, "option with value");
        optionWithSwitch = new Option("switch", "switchOption", false, "option with switch");
        optionWithProperty = new Option("property", "propertyOption", false, "option with property");

        commandLine.addOption(optionWithValue);
        commandLine.addOption(optionWithSwitch);
        commandLine.addOption(optionWithProperty);
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(optionWithValue, "value1");
        commandLine.addValue(optionWithValue, "value2");

        CommandLine parsedCommandLine = new DefaultParser().parse(commandLine.getParser(), new String[]{"--value", "value1", "--value", "value2"});
        assertEquals(Arrays.asList("value1", "value2"), parsedCommandLine.getOptionValues("valueOption"));
        assertEquals(2, commandLine.getValues(optionWithValue).size());
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(optionWithSwitch, true);

        CommandLine parsedCommandLine = new DefaultParser().parse(commandLine.getParser(), new String[]{"--switch"});
        assertTrue(parsedCommandLine.hasOption("switchOption"));
        assertEquals(true, parsedCommandLine.getSwitch(optionWithSwitch, false));

        commandLine.addSwitch(optionWithSwitch, false);

        parsedCommandLine = new DefaultParser().parse(commandLine.getParser(), new String[]{"--switch"});
        assertTrue(parsedCommandLine.hasOption("switchOption"));
        assertEquals(false, parsedCommandLine.getSwitch(optionWithSwitch, true));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty(optionWithProperty, "property", "propertyValue");

        CommandLine parsedCommandLine = new DefaultParser().parse(commandLine.getParser(), new String[]{"--property", "propertyValue"});
        Properties parsedProperties = parsedCommandLine.getProperties();
        assertEquals("propertyValue", parsedProperties.getProperty("propertyOption"));

        commandLine.addProperty(optionWithProperty, "property", "newPropertyValue");

        parsedCommandLine = new DefaultParser().parse(commandLine.getParser(), new String[]{"--property", "newPropertyValue"});
        parsedProperties = parsedCommandLine.getProperties();
        assertEquals("newPropertyValue", parsedProperties.getProperty("propertyOption"));
    }
}