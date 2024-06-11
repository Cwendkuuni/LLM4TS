import org.junit.*;
import java.util.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option = new OptionBuilder().withLongName("input").withShortName("i").create();
        List<String> arguments = new ArrayList<>();
        commandLine = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void testAddOption() {
        Option newOption = new OptionBuilder().withLongName("output").withShortName("o").create();
        commandLine.addOption(newOption);
        Assert.assertTrue(commandLine.hasOption(newOption));
    }

    @Test
    public void testAddValue() {
        Argument argument = new ArgumentBuilder().withName("filename").create();
        String value = "testFile.txt";
        commandLine.addValue(argument, value);
        List<String> values = commandLine.getUndefaultedValues(argument);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals(value, values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder().withLongName("verbose").withShortName("v").create();
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        Option option = new OptionBuilder().withLongName("input").withShortName("i").create();
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = commandLine.getOption("input");
        Assert.assertEquals("input", option.getLongName());
        Assert.assertEquals("i", option.getShortName());
    }

    @Test
    public void testGetValues() {
        Argument argument = new ArgumentBuilder().withName("filename").create();
        List<String> defaultValues = Arrays.asList("default.txt");
        commandLine.setDefaultValues(argument, defaultValues);
        String value = "testFile.txt";
        commandLine.addValue(argument, value);
        List<String> values = commandLine.getValues(argument, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals(defaultValues.get(0), values.get(0));
        Assert.assertEquals(value, values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Argument argument = new ArgumentBuilder().withName("filename").create();
        String value = "testFile.txt";
        commandLine.addValue(argument, value);
        List<String> values = commandLine.getUndefaultedValues(argument);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals(value, values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new OptionBuilder().withLongName("verbose").withShortName("v").create();
        Boolean defaultValue = false;
        commandLine.setDefaultSwitch(option, defaultValue);
        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertEquals(defaultValue, switchValue);
    }

    @Test
    public void testGetProperty() {
        String property = "key";
        String value = "value";
        Option option = new OptionBuilder().withLongName("property").create();
        commandLine.addProperty(option, property, value);
        String retrievedValue = commandLine.getProperty(option, property, null);
        Assert.assertEquals(value, retrievedValue);
    }

    @Test
    public void testAddAndGetProperties() {
        String property1 = "key1";
        String value1 = "value1";
        String property2 = "key2";
        String value2 = "value2";
        Option option = new OptionBuilder().withLongName("property").create();
        commandLine.addProperty(option, property1, value1);
        commandLine.addProperty(option, property2, value2);
        Set<String> properties = commandLine.getProperties(option);
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains(property1));
        Assert.assertTrue(properties.contains(property2));
    }

    @Test
    public void testToString() {
        Option option = new OptionBuilder().withLongName("input").withShortName("i").create();
        List<String> arguments = Arrays.asList("testFile.txt");
        commandLine = new WriteableCommandLineImpl(option, arguments);
        String expectedString = "testFile.txt";
        Assert.assertEquals(expectedString, commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new OptionBuilder().withLongName("input").withShortName("i").create();
        Option option2 = new OptionBuilder().withLongName("output").withShortName("o").create();
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List<Option> options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new OptionBuilder().withLongName("input").withShortName("i").create();
        Option option2 = new OptionBuilder().withLongName("output").withShortName("o").create();
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Set<String> triggers = commandLine.getOptionTriggers();
        Assert.assertEquals(2, triggers.size());
        Assert.assertTrue(triggers.contains("input"));
        Assert.assertTrue(triggers.contains("i"));
        Assert.assertTrue(triggers.contains("output"));
        Assert.assertTrue(triggers.contains("o"));
    }

    @Test
    public void testLooksLikeOption() {
        String optionTrigger = "-i";
        Assert.assertTrue(commandLine.looksLikeOption(optionTrigger));
    }

    @Test
    public void testGetNormalised() {
        Option option = new OptionBuilder().withLongName("input").withShortName("i").create();
        List<String> arguments = Arrays.asList("testFile.txt");
        commandLine = new WriteableCommandLineImpl(option, arguments);
        List<String> normalised = commandLine.getNormalised();
        Assert.assertEquals(arguments, normalised);
    }

    @After
    public void tearDown() {
        commandLine = null;
    }

}