import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class WriteableCommandLineImplTest {

    // Test addOption method
    @Test
    public void testAddOption() {
        Option option1 = new OptionBuilder().withPreferredName("option1").withDescription("Test option 1").create();
        Option option2 = new OptionBuilder().withPreferredName("option2").withDescription("Test option 2").create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.addOption(option2);

        assertTrue(commandLine.hasOption(option2));
        assertEquals(option2, commandLine.getOption("option2"));
        // test if parent option is also added
        assertTrue(commandLine.hasOption(option1));
        assertEquals(option1, commandLine.getOption("option1"));
    }

    // Test addValue method with Argument option
    @Test
    public void testAddValueWithArgumentOption() {
        Option option = new ArgumentBuilder().withPreferredName("arg").create();
        List<String> expectedValues = Arrays.asList("value1", "value2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> actualValues = commandLine.getValues(option, null);

        assertEquals(expectedValues, actualValues);
    }

    // Test addValue method with non-Argument option
    @Test
    public void testAddValueWithNonArgumentOption() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        List<Object> expectedValues = Arrays.asList("value1", "value2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<Object> actualValues = commandLine.getValues(option, null);

        assertEquals(expectedValues, actualValues);
    }

    // Test addSwitch method
    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder().withPreferredName("option").create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.hasOption(option));
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
    }

    // Test getOption method
    @Test
    public void testGetOption() {
        Option option = new OptionBuilder().withPreferredName("option").create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        assertEquals(option, commandLine.getOption("option"));
    }

    // Test getValues method with default values
    @Test
    public void testGetValuesWithDefaultValues() {
        Option option = new ArgumentBuilder().withPreferredName("arg").create();
        List<String> expectedValues = Arrays.asList("value1", "value2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.setDefaultValues(option, expectedValues);

        List<String> actualValues = commandLine.getValues(option, null);

        assertEquals(expectedValues, actualValues);
    }

    // Test getValues method without default values
    @Test
    public void testGetValuesWithoutDefaultValues() {
        Option option = new ArgumentBuilder().withPreferredName("arg").create();
        List<String> expectedValues = new ArrayList<>();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        List<String> actualValues = commandLine.getValues(option, null);

        assertEquals(expectedValues, actualValues);
    }

    // Test getValues method with more default values than command line values
    @Test
    public void testGetValuesWithMoreDefaultValues() {
        Option option = new ArgumentBuilder().withPreferredName("arg").create();
        List<String> defaultValues = Arrays.asList("value1", "value2", "value3");
        List<String> commandLineValues = Arrays.asList("value1", "value2");
        List<String> expectedValues = Arrays.asList("value1", "value2", "value3");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.setDefaultValues(option, defaultValues);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> actualValues = commandLine.getValues(option, null);

        assertEquals(expectedValues, actualValues);
    }

    // Test getUndefaultedValues method
    @Test
    public void testGetUndefaultedValues() {
        Option option = new ArgumentBuilder().withPreferredName("arg").create();
        List<String> expectedValues = Arrays.asList("value1", "value2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> actualValues = commandLine.getUndefaultedValues(option);

        assertEquals(expectedValues, actualValues);
    }

    // Test getSwitch method with default switch value
    @Test
    public void testGetSwitchWithDefaultSwitch() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        Boolean expectedSwitch = Boolean.TRUE;

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.setDefaultSwitch(option, expectedSwitch);

        Boolean actualSwitch = commandLine.getSwitch(option, null);

        assertEquals(expectedSwitch, actualSwitch);
    }

    // Test getSwitch method without default switch value
    @Test
    public void testGetSwitchWithoutDefaultSwitch() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        Boolean expectedSwitch = Boolean.FALSE;

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        Boolean actualSwitch = commandLine.getSwitch(option, false);

        assertEquals(expectedSwitch, actualSwitch);
    }

    // Test getProperty method
    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new ArgumentBuilder().withPreferredName("arg").create(), new ArrayList<>());
        commandLine.addProperty("prop", "value");

        assertEquals("value", commandLine.getProperty("prop"));
    }

    // Test addProperty method
    @Test
    public void testAddProperty() {
        Option option = new OptionBuilder().withPreferredName("option").create();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addProperty(option, "prop", "value");

        Set<String> expectedProperties = new HashSet<>(Collections.singletonList("prop"));

        assertEquals(expectedProperties, commandLine.getProperties(option));
        assertEquals("value", commandLine.getProperty(option, "prop", null));
    }

    // Test looksLikeOption method
    @Test
    public void testLooksLikeOption() {
        Option option = new OptionBuilder().withPrefixes(Arrays.asList("-", "--")).create();
        Set<String> prefixes = new HashSet<>(Arrays.asList("-", "--"));

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        assertTrue(commandLine.looksLikeOption("-option"));
        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("option"));
    }

    // Test toString method
    @Test
    public void testToString() {
        Option option1 = new OptionBuilder().withPreferredName("option1").create();
        Option option2 = new OptionBuilder().withPreferredName("option2").create();
        List<String> arguments = Arrays.asList("arg1", "arg2");
        String expectedString = "arg1 arg2";

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, arguments);
        commandLine.addOption(option2);

        assertEquals(expectedString, commandLine.toString());
    }

    // Test getOptions method
    @Test
    public void testGetOptions() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        List<Option> expectedOptions = Collections.singletonList(option);

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        assertEquals(expectedOptions, commandLine.getOptions());
    }

    // Test getOptionTriggers method
    @Test
    public void testGetOptionTriggers() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        Set<String> expectedTriggers = new HashSet<>(Collections.singletonList("option"));

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        assertEquals(expectedTriggers, commandLine.getOptionTriggers());
    }

    // Test setDefaultValues method
    @Test
    public void testSetDefaultValues() {
        Option option = new ArgumentBuilder().withPreferredName("arg").create();
        List<String> defaultValues = Arrays.asList("value1", "value2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.setDefaultValues(option, defaultValues);

        List<String> actualValues = commandLine.getValues(option, null);

        assertEquals(defaultValues, actualValues);
    }

    // Test setDefaultSwitch method
    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        Boolean defaultSwitch = Boolean.TRUE;

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.setDefaultSwitch(option, defaultSwitch);

        Boolean actualSwitch = commandLine.getSwitch(option, null);

        assertEquals(defaultSwitch, actualSwitch);
    }

    // Test getNormalised method
    @Test
    public void testGetNormalised() {
        Option option = new OptionBuilder().withPreferredName("option").create();
        List<String> arguments = Arrays.asList("arg1", "arg2");
        List<String> expectedNormalised = Arrays.asList("arg1", "arg2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        assertEquals(expectedNormalised, commandLine.getNormalised());
    }
}