import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option1 = new Option("o1");
        Option option2 = new Option("o2");
        Option option3 = new Option("o3");
        Option option4 = new Option("o4");

        commandLine = new WriteableCommandLineImpl(option1, new ArrayList<String>());

        commandLine.addOption(option2);
        commandLine.addOption(option3);
        commandLine.addOption(option4);
    }

    @Test
    public void testAddOption() {
        Option newOption = new Option("new");
        commandLine.addOption(newOption);

        assertTrue(commandLine.hasOption(newOption));
    }

    @Test
    public void testAddValue() {
        Argument argument = new Argument();
        Option option = new Option("valueOption");
        option.addArgument(argument);

        commandLine.addValue(option, "value");

        List<String> values = commandLine.getValues(option, new ArrayList<String>());

        assertTrue(values.contains("value"));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("switchOption");

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o1");

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = commandLine.getOption("o2");

        assertNotNull(option);
        assertEquals("o2", option.getPreferredName());
    }

    @Test
    public void testGetValues() {
        Option option = new Option("valuesOption");
        List<String> defaultValues = new ArrayList<String>() {{
            add("default1");
            add("default2");
        }};

        commandLine.setOptionValues(option, defaultValues);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> values = commandLine.getValues(option, new ArrayList<String>());

        assertTrue(values.contains("default1"));
        assertTrue(values.contains("default2"));
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("switchOption");
        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property1", "value1");

        String property = commandLine.getProperty("property1", "default");

        assertEquals("value1", property);
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-o2"));

        assertFalse(commandLine.looksLikeOption("invalid"));
    }

    @Test
    public void testToString() {
        commandLine.addValue(commandLine.getOption("o2"), "value");

        String toString = commandLine.toString();

        assertEquals("-o2 value", toString);
    }

    @Test
    public void testGetOptions() {
        List<Option> options = commandLine.getOptions();

        assertEquals(4, options.size());
    }

    @Test
    public void testGetOptionTriggers() {
        Set<String> optionTriggers = commandLine.getOptionTriggers();

        assertTrue(optionTriggers.contains("o1"));
        assertTrue(optionTriggers.contains("o2"));
        assertTrue(optionTriggers.contains("o3"));
        assertTrue(optionTriggers.contains("o4"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("defaultValuesOption");
        List<String> defaultValues = new ArrayList<String>() {{
            add("default1");
            add("default2");
        }};

        commandLine.setDefaultValues(option, defaultValues);

        List<String> values = commandLine.getValues(option, new ArrayList<String>());

        assertTrue(values.contains("default1"));
        assertTrue(values.contains("default2"));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("defaultSwitchOption");

        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        List<String> normalised = commandLine.getNormalised();

        assertEquals(0, normalised.size());
    }
}

