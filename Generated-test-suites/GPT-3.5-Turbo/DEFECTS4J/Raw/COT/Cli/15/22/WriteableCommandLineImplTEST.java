import org.junit.Test;
import org.junit.Assert;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("-v");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("argument1", "argument2"));

        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("-f");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("argument1", "argument2"));

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> values = commandLine.getValues(option, null);

        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("-s");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("argument1", "argument2"));

        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, null);

        Assert.assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("-o");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("argument1", "argument2"));

        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("-r");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("argument1", "argument2"));

        commandLine.addOption(option);

        Option retrievedOption = commandLine.getOption("-r");

        Assert.assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("-m");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("argument1", "argument2"));

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> values = commandLine.getValues(option, Arrays.asList("default1", "default2"));

        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("-n");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("argument1", "argument2"));

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> values = commandLine.getUndefaultedValues(option);

        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("-x");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("argument1", "argument2"));

        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, Boolean.FALSE);

        Assert.assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("-p");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("argument1", "argument2"));

        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");

        String propertyValue = commandLine.getProperty(option, "property1", null);

        Assert.assertEquals("value1", propertyValue);
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("-q");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("argument1", "argument2"));

        commandLine.addProperty(option, "property1", "value1");

        String propertyValue = commandLine.getProperty(option, "property1", null);

        Assert.assertEquals("value1", propertyValue);
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("-t");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("argument1", "argument2"));

        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");

        Set<String> properties = commandLine.getProperties(option);

        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains("property1"));
        Assert.assertTrue(properties.contains("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        Option option = new Option("-l");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("argument1", "argument2"));

        boolean looksLikeOption = commandLine.looksLikeOption("-l");

        Assert.assertTrue(looksLikeOption);
    }

    @Test
    public void testToString() {
        Option option = new Option("-z");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("argument1", "argument2"));

        String commandLineString = commandLine.toString();

        Assert.assertEquals("argument1 argument2", commandLineString);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("-o1");
        Option option2 = new Option("-o2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Arrays.asList("argument1", "argument2"));

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List<Option> options = commandLine.getOptions();

        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("-t1");
        Option option2 = new Option("-t2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Arrays.asList("argument1", "argument2"));

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set<String> optionTriggers = commandLine.getOptionTriggers();

        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("-t1"));
        Assert.assertTrue(optionTriggers.contains("-t2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("-d");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("argument1", "argument2"));

        commandLine.setDefaultValues(option, Arrays.asList("default1", "default2"));

        List<String> values = commandLine.getValues(option, null);

        Assert.assertEquals(2, values.size());
        Assert.assertEquals("default1", values.get(0));
        Assert.assertEquals("default2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("-us");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("argument1", "argument2"));

        commandLine.setDefaultSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, null);

        Assert.assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("-r");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("argument1", "argument2"));

        List<String> normalised = commandLine.getNormalised();

        Assert.assertEquals(2, normalised.size());
        Assert.assertEquals("argument1", normalised.get(0));
        Assert.assertEquals("argument2", normalised.get(1));
    }
}