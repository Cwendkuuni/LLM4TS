import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setup() {
        Option option1 = new Option("o1", "Option 1", false);
        Option option2 = new Option("o2", "Option 2", false);
        Argument argument1 = new Argument("arg1", "Argument 1", false);

        List<Option> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);

        List<Argument> arguments = new ArrayList<>();
        arguments.add(argument1);

        commandLine = new WriteableCommandLineImpl(options, arguments);
    }

    @Test
    public void testAddOption() {
        Option option3 = new Option("o3", "Option 3", false);
        commandLine.addOption(option3);
        Assert.assertTrue(commandLine.getOptions().contains(option3));
    }

    @Test
    public void testAddValue() {
        Option option1 = commandLine.getOption("o1");
        commandLine.addValue(option1, "Value 1");
        List<String> values = commandLine.getValues(option1, new ArrayList<>());
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("Value 1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option2 = commandLine.getOption("o2");
        commandLine.addSwitch(option2, true);
        Boolean switchValue = commandLine.getSwitch(option2, null);
        Assert.assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    public void testHasOption() {
        Option option1 = commandLine.getOption("o1");
        Assert.assertTrue(commandLine.hasOption(option1));
    }

    @Test
    public void testGetOption() {
        Option option2 = commandLine.getOption("o2");
        Assert.assertEquals(option2, commandLine.getOption("o2"));
    }

    @Test
    public void testGetValues() {
        Option option1 = commandLine.getOption("o1");
        commandLine.addValue(option1, "Value 1");
        commandLine.addValue(option1, "Value 2");
        List<String> values = commandLine.getValues(option1, new ArrayList<>());
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("Value 1", values.get(0));
        Assert.assertEquals("Value 2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Argument argument1 = commandLine.getArgument("arg1");
        commandLine.addValue(argument1, "Value 1");
        List<String> values = commandLine.getUndefaultedValues(argument1);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("Value 1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option2 = commandLine.getOption("o2");
        commandLine.addSwitch(option2, true);
        Boolean switchValue = commandLine.getSwitch(option2, null);
        Assert.assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    public void testGetProperty() {
        String property = commandLine.getProperty("property");
        Assert.assertNull(property);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property", "value");
        String property = commandLine.getProperty("property");
        Assert.assertEquals("value", property);
    }

    @Test
    public void testGetPropertyWithDefault() {
        String property = commandLine.getProperty("property", "default");
        Assert.assertEquals("default", property);
    }

    @Test
    public void testGetProperties() {
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        Set<String> properties = commandLine.getProperties();
        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains("property1"));
        Assert.assertTrue(properties.contains("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        boolean result = commandLine.looksLikeOption("-o1");
        Assert.assertTrue(result);
    }

    @Test
    public void testToString() {
        Option option1 = commandLine.getOption("o1");
        commandLine.addValue(option1, "Value 1");
        commandLine.addValue(option1, "Value 2");
        Argument argument1 = commandLine.getArgument("arg1");
        commandLine.addValue(argument1, "Argument Value");
        String string = commandLine.toString();
        Assert.assertEquals("-o1 Value 1 Value 2 arg1 Argument Value", string);
    }
}