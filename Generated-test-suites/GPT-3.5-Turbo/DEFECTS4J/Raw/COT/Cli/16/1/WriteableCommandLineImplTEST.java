
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setup() {
        // Create a root option
        Option rootOption = new OptionBuilder().withLongName("root").create();

        // Create a list of arguments
        List<String> arguments = new ArrayList<String>();
        arguments.add("argument1");
        arguments.add("argument2");

        // Create a WriteableCommandLineImpl instance
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new OptionBuilder().withLongName("option1").create();
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionBuilder().withLongName("option1").create();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List<String> values = commandLine.getValues(option, null);
        assertTrue(values.contains("value1"));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder().withLongName("option1").create();
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        Option option = new OptionBuilder().withLongName("option1").create();
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new OptionBuilder().withLongName("option1").create();
        commandLine.addOption(option);
        Option retrievedOption = commandLine.getOption("option1");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        Option option = new OptionBuilder().withLongName("option1").create();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List<String> values = commandLine.getValues(option, null);
        assertTrue(values.contains("value1"));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new OptionBuilder().withLongName("option1").create();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List<String> values = commandLine.getUndefaultedValues(option);
        assertTrue(values.contains("value1"));
    }

    @Test
    public void testGetSwitch() {
        Option option = new OptionBuilder().withLongName("option1").create();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, null);
        assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        Option option = new OptionBuilder().withLongName("option1").create();
        commandLine.addProperty(option, "property1", "value1");
        String propertyValue = commandLine.getProperty(option, "property1", null);
        assertEquals("value1", propertyValue);
    }

    @Test
    public void testAddProperty() {
        Option option = new OptionBuilder().withLongName("option1").create();
        commandLine.addProperty(option, "property1", "value1");
        String propertyValue = commandLine.getProperty(option, "property1", null);
        assertEquals("value1", propertyValue);
    }

    @Test
    public void testGetProperties() {
        Option option = new OptionBuilder().withLongName("option1").create();
        commandLine.addProperty(option, "property1", "value1");
        Set<String> properties = commandLine.getProperties(option);
        assertTrue(properties.contains("property1"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("--root"));
        assertTrue(commandLine.looksLikeOption("-r"));
        assertFalse(commandLine.looksLikeOption("argument1"));
    }

    @Test
    public void testToString() {
        assertEquals("\"argument1\" \"argument2\"", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new OptionBuilder().withLongName("option1").create();
        Option option2 = new OptionBuilder().withLongName("option2").create();
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List<Option> options = commandLine.getOptions();
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new OptionBuilder().withLongName("option1").create();
        Option option2 = new OptionBuilder().withLongName("option2").create();
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Set<String> triggers = commandLine.getOptionTriggers();
        assertTrue(triggers.contains("option1"));
        assertTrue(triggers.contains("option2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionBuilder().withLongName("option1").create();
        commandLine.setDefaultValues(option, Arrays.asList("default1", "default2"));
        List<String> defaults = commandLine.getValues(option, null);
        assertTrue(defaults.contains("default1"));
        assertTrue(defaults.contains("default2"));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionBuilder().withLongName("option1").create();
        commandLine.setDefaultSwitch(option, true);
        Boolean defaultSwitch = commandLine.getSwitch(option, null);
        assertTrue(defaultSwitch);
    }

    @Test
    public void testGetNormalised() {
        Option option = new OptionBuilder().withLongName("option1").create();
        commandLine.addOption(option);
        List<String> normalised = commandLine.getNormalised();
        assertTrue(normalised.contains("--root"));
        assertTrue(normalised.contains("argument1"));
        assertTrue(normalised.contains("argument2"));
    }

}
