import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new OptionBuilder().create();
        commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList<>());
    }

    @Test
    public void testAddOption() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        commandLine.addOption(option);
        
        assertTrue(commandLine.hasOption(option));
        assertEquals(option, commandLine.getOption("test"));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");

        assertEquals(expectedValues, commandLine.getValues(option, null));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_ThrowsExceptionIfSwitchAlreadySet() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
        assertFalse(commandLine.hasOption(new OptionBuilder().create()));
    }

    @Test
    public void testGetOption() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        commandLine.addOption(option);
        
        assertEquals(option, commandLine.getOption("test"));
        assertNull(commandLine.getOption("invalid"));
    }

    @Test
    public void testGetValues() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");

        assertEquals(expectedValues, commandLine.getValues(option, null));
        assertEquals(Collections.emptyList(), commandLine.getValues(new OptionBuilder().create(), null));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");

        assertEquals(expectedValues, commandLine.getUndefaultedValues(option));
        assertEquals(Collections.emptyList(), commandLine.getUndefaultedValues(new OptionBuilder().create()));
    }

    @Test
    public void testGetSwitch() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
        assertFalse(commandLine.getSwitch(new OptionBuilder().create(), false));
    }

    @Test
    public void testGetProperty() {
        Option option = new OptionBuilder().create();
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");

        assertEquals("value1", commandLine.getProperty(option, "property1", null));
        assertEquals("value2", commandLine.getProperty(option, "property2", null));
        assertNull(commandLine.getProperty(option, "invalid", null));
    }

    @Test
    public void testAddProperty() {
        Option option = new OptionBuilder().create();
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");

        assertEquals("value1", commandLine.getProperty(option, "property1", null));
        assertEquals("value2", commandLine.getProperty(option, "property2", null));
    }

    @Test
    public void testGetProperties() {
        Option option = new OptionBuilder().create();
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");

        Set<String> expectedProperties = new HashSet<>();
        expectedProperties.add("property1");
        expectedProperties.add("property2");
        assertEquals(expectedProperties, commandLine.getProperties(option));
        assertEquals(Collections.EMPTY_SET, commandLine.getProperties(new OptionBuilder().create()));
    }

    @Test
    public void testLooksLikeOption_True() {
        assertTrue(commandLine.looksLikeOption("-option"));
        assertTrue(commandLine.looksLikeOption("--option"));
    }

    @Test
    public void testLooksLikeOption_False() {
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        commandLine.addValue(new OptionBuilder().create(), "value1");
        commandLine.addValue(new OptionBuilder().create(), "value2");

        assertEquals("value1 value2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new OptionBuilder().withPreferredName("test1").create();
        Option option2 = new OptionBuilder().withPreferredName("test2").create();
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List<Option> expectedOptions = new ArrayList<>();
        expectedOptions.add(option1);
        expectedOptions.add(option2);

        assertEquals(expectedOptions, commandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new OptionBuilder().withPreferredName("test1").create();
        Option option2 = new OptionBuilder().withPreferredName("test2").create();
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set<String> expectedTriggers = new HashSet<>();
        expectedTriggers.add("test1");
        expectedTriggers.add("test2");

        assertEquals(expectedTriggers, commandLine.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionBuilder().withPreferredName("test").create();
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        commandLine.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, commandLine.getValues(option, null));
        assertNull(commandLine.getDefaultValues(new OptionBuilder().create()));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionBuilder().withPreferredName("test").create();

        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, null));
        assertNull(commandLine.getDefaultSwitch(new OptionBuilder().create()));
    }

    @Test
    public void testGetNormalised() {
        List<String> arguments = new ArrayList<>();
        arguments.add("arg1");
        arguments.add("arg2");
        commandLine = new WriteableCommandLineImpl(new OptionBuilder().create(), arguments);

        List<String> expectedArguments = new ArrayList<>();
        expectedArguments.add("arg1");
        expectedArguments.add("arg2");

        assertEquals(expectedArguments, commandLine.getNormalised());
    }
}