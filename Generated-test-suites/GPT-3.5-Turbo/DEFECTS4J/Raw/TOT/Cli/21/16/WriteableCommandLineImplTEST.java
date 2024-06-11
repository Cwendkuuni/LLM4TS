import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setup() {
        Option option1 = new OptionBuilder().withPreferredName("option1").create();
        Argument argument1 = new ArgumentBuilder().create();
        commandLine = new WriteableCommandLineImpl(option1, Collections.singletonList(argument1));
    }

    @Test
    public void testAddOption() {
        Option option2 = new OptionBuilder().withPreferredName("option2").create();
        commandLine.addOption(option2);
        assertTrue(commandLine.hasOption(option2));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        Object value = "value";
        commandLine.addValue(option, value);
        assertTrue(commandLine.getValues(option, null).contains(value));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("option"));
    }

    @Test
    public void testGetValues() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        Object value1 = "value1";
        Object value2 = "value2";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);
        assertTrue(commandLine.getValues(option, null).contains(value1));
        assertTrue(commandLine.getValues(option, null).contains(value2));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        Object value1 = "value1";
        Object value2 = "value2";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);
        assertTrue(commandLine.getUndefaultedValues(option).contains(value1));
        assertTrue(commandLine.getUndefaultedValues(option).contains(value2));
    }

    @Test
    public void testGetSwitch() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void testAddProperty() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        assertEquals(value, commandLine.getProperty(option, property, null));
    }

    @Test
    public void testGetPropertyWithDefaultValue() {
        String property = "property";
        String value = "value";
        assertEquals(value, commandLine.getProperty(property, value));
    }

    @Test
    public void testGetProperties() {
        String property1 = "property1";
        String value1 = "value1";
        commandLine.addProperty(property1, value1);
        assertTrue(commandLine.getProperties().contains(property1));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        List<String> defaults = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaults);
        assertEquals(defaults, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        Object value = "value";
        commandLine.addValue(option, value);
        assertEquals("-option value", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new OptionBuilder().withPreferredName("option1").create();
        Option option2 = new OptionBuilder().withPreferredName("option2").create();
        Option option3 = new OptionBuilder().withPreferredName("option3").create();
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.addOption(option3);
        assertTrue(commandLine.getOptions().contains(option1));
        assertTrue(commandLine.getOptions().contains(option2));
        assertTrue(commandLine.getOptions().contains(option3));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new OptionBuilder().withPreferredName("option1").create();
        Option option2 = new OptionBuilder().withPreferredName("option2").create();
        Option option3 = new OptionBuilder().withPreferredName("option3").create();
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.addOption(option3);
        assertTrue(commandLine.getOptionTriggers().contains("option1"));
        assertTrue(commandLine.getOptionTriggers().contains("option2"));
        assertTrue(commandLine.getOptionTriggers().contains("option3"));
    }

    @Test
    public void testGetNormalised() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        Argument argument = new ArgumentBuilder().create();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.singletonList(argument));
        assertTrue(commandLine.getNormalised().contains(argument));
    }
}