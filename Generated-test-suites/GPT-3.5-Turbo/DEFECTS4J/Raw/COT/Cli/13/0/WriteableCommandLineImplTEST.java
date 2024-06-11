
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option1 = new Option("o1");
        Argument argument1 = new Argument("arg1");
        Option option2 = new Option("o2");
        Argument argument2 = new Argument("arg2");

        commandLine = new WriteableCommandLineImpl(option1, Arrays.asList("value1", "value2"));
        commandLine.addOption(option2);
        commandLine.addArgument(argument1, "argument1");
        commandLine.addSwitch(option1, true);
        commandLine.addSwitch(option2, false);
    }

    @Test
    public void testAddOption() {
        Option option3 = new Option("o3");
        commandLine.addOption(option3);

        Assert.assertTrue(commandLine.getOptions().contains(option3));
    }

    @Test
    public void testAddValue() {
        Option option3 = new Option("o3");
        commandLine.addValue(option3, "value3");

        List<String> values = commandLine.getValues(option3, null);
        Assert.assertTrue(values.contains("value3"));
    }

    @Test
    public void testAddSwitch() {
        Option option3 = new Option("o3");
        commandLine.addSwitch(option3, true);

        Boolean switchValue = commandLine.getSwitch(option3, null);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("o1");
        Option option3 = new Option("o3");

        Assert.assertTrue(commandLine.hasOption(option1));
        Assert.assertFalse(commandLine.hasOption(option3));
    }

    @Test
    public void testGetOption() {
        Option option1 = commandLine.getOption("o1");
        Option option3 = commandLine.getOption("o3");

        Assert.assertEquals("o1", option1.getPreferredName());
        Assert.assertNull(option3);
    }

    @Test
    public void testGetValues() {
        Option option1 = new Option("o1");
        List<String> defaultValues = Arrays.asList("default1", "default2");

        List<String> values = commandLine.getValues(option1, defaultValues);

        Assert.assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testGetSwitch() {
        Option option1 = new Option("o1");
        Boolean defaultValue = true;

        Boolean switchValue = commandLine.getSwitch(option1, defaultValue);

        Assert.assertTrue(switchValue);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property1", "value1");

        Assert.assertEquals("value1", commandLine.getProperty("property1", null));
    }

    @Test
    public void testLooksLikeOption() {
        Assert.assertTrue(commandLine.looksLikeOption("-o1"));
        Assert.assertFalse(commandLine.looksLikeOption("value1"));
    }

    @Test
    public void testToString() {
        String toString = commandLine.toString();

        Assert.assertEquals("value1 value2", toString);
    }

    @Test
    public void testSetDefaultValues() {
        Option option1 = new Option("o1");
        List<String> defaults = Arrays.asList("default1", "default2");

        commandLine.setDefaultValues(option1, defaults);

        List<String> values = commandLine.getValues(option1, null);

        Assert.assertEquals(defaults, values);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option1 = new Option("o1");
        Boolean defaultSwitch = true;

        commandLine.setDefaultSwitch(option1, defaultSwitch);

        Boolean switchValue = commandLine.getSwitch(option1, null);

        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        List<String> normalised = commandLine.getNormalised();

        Assert.assertEquals(Arrays.asList("value1", "value2"), normalised);
    }
}
