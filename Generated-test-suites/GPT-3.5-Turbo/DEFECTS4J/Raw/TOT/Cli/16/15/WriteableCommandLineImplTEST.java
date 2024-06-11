import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.Command;
import org.apache.commons.cli2.option.Definition;
import org.apache.commons.cli2.option.PropertyOption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option1 = new Option("-a");
        Option option2 = new Option("-b");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Assert.assertTrue(commandLine.hasOption(option1));
        Assert.assertTrue(commandLine.hasOption(option2));

        Assert.assertEquals(option1, commandLine.getOption("-a"));
        Assert.assertEquals(option2, commandLine.getOption("-b"));
    }

    @Test
    public void testAddValue() {
        Argument argument = new Argument("arg");
        Option option = new Option("-a", "", argument);

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> expectedValues = Arrays.asList("value1", "value2");

        Assert.assertEquals(expectedValues, commandLine.getValues(option, null));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("-s");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addSwitch(option, true);

        Assert.assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_DuplicateOption() {
        Option option = new Option("-s");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("-a");
        Option option2 = new Option("-b");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());

        commandLine.addOption(option1);

        Assert.assertTrue(commandLine.hasOption(option1));
        Assert.assertFalse(commandLine.hasOption(option2));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("-a");
        Option option2 = new Option("-b");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Assert.assertEquals(option1, commandLine.getOption("-a"));
        Assert.assertEquals(option2, commandLine.getOption("-b"));
    }

    @Test
    public void testGetValues() {
        Argument argument1 = new Argument("arg1");
        Argument argument2 = new Argument("arg2");

        Option option1 = new Option("-a", "", argument1);
        Option option2 = new Option("-b", "", argument2);

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());

        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        commandLine.addValue(option2, "value3");

        List<String> expectedValues1 = Arrays.asList("value1", "value2");
        List<String> expectedValues2 = Arrays.asList("value3");

        Assert.assertEquals(expectedValues1, commandLine.getValues(option1, null));
        Assert.assertEquals(expectedValues2, commandLine.getValues(option2, null));
    }

    @Test
    public void testGetUndefaultedValues() {
        Argument argument1 = new Argument("arg1");
        Argument argument2 = new Argument("arg2");

        Option option1 = new Option("-a", "", argument1);
        Option option2 = new Option("-b", "", argument2);

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());

        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        commandLine.addValue(option2, "value3");

        List<String> expectedValues = Arrays.asList("value1", "value2");

        Assert.assertEquals(expectedValues, commandLine.getUndefaultedValues(option1));
    }

    @Test
    public void testGetSwitch() {
        Option option1 = new Option("-a");
        Option option2 = new Option("-b");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());

        commandLine.addSwitch(option1, true);
        commandLine.addSwitch(option2, false);

        Assert.assertEquals(Boolean.TRUE, commandLine.getSwitch(option1, null));
        Assert.assertEquals(Boolean.FALSE, commandLine.getSwitch(option2, null));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new PropertyOption(), new ArrayList<>());

        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");

        Assert.assertEquals("value1", commandLine.getProperty("property1"));
        Assert.assertEquals("value2", commandLine.getProperty("property2"));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new PropertyOption(), new ArrayList<>());

        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property1", "value2");

        Assert.assertEquals("value2", commandLine.getProperty("property1"));
    }

    @Test
    public void testGetProperty_DefaultValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new PropertyOption(), new ArrayList<>());

        commandLine.addProperty("property1", "value1");

        Assert.assertEquals("default", commandLine.getProperty("property2", "default"));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new PropertyOption(), new ArrayList<>());

        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property1", "value2");

        List<String> expectedProperties = Arrays.asList("property1");

        Assert.assertEquals(expectedProperties, new ArrayList<>(commandLine.getProperties()));
    }

    @Test
    public void testLooksLikeOption() {
        Option option1 = new Option("-a");
        Option option2 = new Option("-b");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());

        Assert.assertTrue(commandLine.looksLikeOption("-a"));
        Assert.assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("-a"), Arrays.asList("arg1", "arg2"));

        Assert.assertEquals("arg1 arg2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("-a");
        Option option2 = new Option("-b");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List<Option> expectedOptions = Arrays.asList(option1, option2);

        Assert.assertEquals(expectedOptions, commandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("-a");
        Option option2 = new Option("-b");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List<String> expectedTriggers = Arrays.asList("-a", "-b");

        Assert.assertEquals(expectedTriggers, new ArrayList<>(commandLine.getOptionTriggers()));
    }

    @Test
    public void testSetDefaultValues() {
        Argument argument = new Argument("arg");
        Option option = new Option("-a", "", argument);

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.setDefaultValues(option, Arrays.asList("default1", "default2"));

        List<String> expectedDefaults = Arrays.asList("default1", "default2");

        Assert.assertEquals(expectedDefaults, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultValues_Null() {
        Argument argument = new Argument("arg");
        Option option = new Option("-a", "", argument);

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.setDefaultValues(option, null);

        Assert.assertEquals(Collections.emptyList(), commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("-s");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.setDefaultSwitch(option, false);

        Assert.assertEquals(Boolean.FALSE, commandLine.getSwitch(option, null));
    }

    @Test
    public void testSetDefaultSwitch_Null() {
        Option option = new Option("-s");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.setDefaultSwitch(option, null);

        Assert.assertNull(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("-a");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));

        List<String> expectedNormalised = Arrays.asList("arg1", "arg2");

        Assert.assertEquals(expectedNormalised, commandLine.getNormalised());
    }
}