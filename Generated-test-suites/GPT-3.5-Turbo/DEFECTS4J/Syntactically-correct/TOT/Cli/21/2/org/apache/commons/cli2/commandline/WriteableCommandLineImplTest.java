import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new OptionBuilder()
                .withPreferredName("root")
                .withDescription("Root option")
                .create();
        List arguments = Collections.emptyList();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new OptionBuilder()
                .withPreferredName("option1")
                .withDescription("Option 1")
                .create();

        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionBuilder()
                .withPreferredName("option1")
                .withDescription("Option 1")
                .create();

        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        List<String> values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder()
                .withPreferredName("option1")
                .withDescription("Option 1")
                .create();

        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertEquals(true, switchValue);
    }

    @Test
    public void testHasOption() {
        Option option = new OptionBuilder()
                .withPreferredName("option1")
                .withDescription("Option 1")
                .create();

        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option1 = new OptionBuilder()
                .withPreferredName("option1")
                .withDescription("Option 1")
                .create();
        Option option2 = new OptionBuilder()
                .withPreferredName("option2")
                .withDescription("Option 2")
                .create();

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Option retrievedOption = commandLine.getOption("option1");
        Assert.assertEquals(option1, retrievedOption);
    }

    @Test
    public void testGetValues() {
        Option option = new OptionBuilder()
                .withPreferredName("option1")
                .withDescription("Option 1")
                .create();

        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> values = commandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new OptionBuilder()
                .withPreferredName("option1")
                .withDescription("Option 1")
                .create();

        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        Option option = new OptionBuilder()
                .withPreferredName("option1")
                .withDescription("Option 1")
                .create();

        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertEquals(true, switchValue);
    }

    @Test
    public void testGetProperty() {
        Option option = new OptionBuilder()
                .withPreferredName("option1")
                .withDescription("Option 1")
                .create();

        commandLine.addProperty(option, "property1", "value1");

        String propertyValue = commandLine.getProperty(option, "property1", null);
        Assert.assertEquals("value1", propertyValue);
    }

    @Test
    public void testAddProperty() {
        Option option = new OptionBuilder()
                .withPreferredName("option1")
                .withDescription("Option 1")
                .create();

        commandLine.addProperty(option, "property1", "value1");

        String propertyValue = commandLine.getProperty(option, "property1", null);
        Assert.assertEquals("value1", propertyValue);
    }

    @Test
    public void testGetProperties() {
        Option option = new OptionBuilder()
                .withPreferredName("option1")
                .withDescription("Option 1")
                .create();

        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");

        Set<String> properties = commandLine.getProperties(option);
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains("property1"));
        Assert.assertTrue(properties.contains("property2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionBuilder()
                .withPreferredName("option1")
                .withDescription("Option 1")
                .create();

        List<String> defaults = new ArrayList<>();
        defaults.add("default1");

        commandLine.setDefaultValues(option, defaults);

        List<String> values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("default1", values.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionBuilder()
                .withPreferredName("option1")
                .withDescription("Option 1")
                .create();

        commandLine.setDefaultSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertEquals(true, switchValue);
    }

    @Test
    public void testToString() {
        Option option1 = new OptionBuilder()
                .withPreferredName("option1")
                .withDescription("Option 1")
                .create();
        Option option2 = new OptionBuilder()
                .withPreferredName("option2")
                .withDescription("Option 2")
                .create();

        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.addValue(option1, "value1");

        String stringRepresentation = commandLine.toString();
        Assert.assertEquals("option1: value1 option2", stringRepresentation);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new OptionBuilder()
                .withPreferredName("option1")
                .withDescription("Option 1")
                .create();
        Option option2 = new OptionBuilder()
                .withPreferredName("option2")
                .withDescription("Option 2")
                .create();

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List<Option> options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new OptionBuilder()
                .withPreferredName("option1")
                .withDescription("Option 1")
                .create();
        Option option2 = new OptionBuilder()
                .withPreferredName("option2")
                .withDescription("Option 2")
                .create();

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set<String> triggers = commandLine.getOptionTriggers();
        Assert.assertEquals(2, triggers.size());
        Assert.assertTrue(triggers.contains("option1"));
        Assert.assertTrue(triggers.contains("option2"));
    }

    @Test
    public void testLooksLikeOption() {
        Option option1 = new OptionBuilder()
                .withPreferredName("option1")
                .withDescription("Option 1")
                .create();
        Option option2 = new OptionBuilder()
                .withPreferredName("option2")
                .withDescription("Option 2")
                .withPrefixes("-", "--")
                .create();

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Assert.assertTrue(commandLine.looksLikeOption("-option1"));
        Assert.assertTrue(commandLine.looksLikeOption("--option2"));
        Assert.assertFalse(commandLine.looksLikeOption("option1"));
        Assert.assertFalse(commandLine.looksLikeOption("option2"));
    }

    @Test
    public void testGetNormalised() {
        Option option1 = new OptionBuilder()
                .withPreferredName("option1")
                .withDescription("Option 1")
                .create();
        Option option2 = new OptionBuilder()
                .withPreferredName("option2")
                .withDescription("Option 2")
                .create();

        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.addValue(option1, "value1");

        List<String> normalisedArguments = commandLine.getNormalised();
        Assert.assertEquals(2, normalisedArguments.size());
        Assert.assertEquals("option1", normalisedArguments.get(0));
        Assert.assertEquals("value1", normalisedArguments.get(1));
    }
}