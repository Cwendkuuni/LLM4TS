import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    private Option rootOption;
    private List arguments;
    private Option option1;
    private Option option2;
    private Option option3;
    private Argument argument1;
    private Argument argument2;

    @Before
    public void setUp() {
        rootOption = new Option("root");
        arguments = new ArrayList();
        option1 = new Option("option1");
        option2 = new Option("option2");
        option3 = new Option("option3");
        argument1 = new Argument("argument1");
        argument2 = new Argument("argument2");

        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option1);

        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertTrue(commandLine.getOptions().contains(option1));
    }

    @Test
    public void testAddValue() {
        commandLine.addOption(argument1);
        commandLine.addValue(argument1, "value1");

        Assert.assertEquals(1, commandLine.getValues(argument1, null).size());
        Assert.assertEquals("value1", commandLine.getValues(argument1, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option1, true);

        Assert.assertTrue(commandLine.hasOption(option1));
        Assert.assertTrue(commandLine.getSwitch(option1, false));
    }

    @Test
    public void testHasOption() {
        Assert.assertFalse(commandLine.hasOption(option1));

        commandLine.addOption(option1);

        Assert.assertTrue(commandLine.hasOption(option1));
    }

    @Test
    public void testGetOption() {
        commandLine.addOption(option1);

        Assert.assertEquals(option1, commandLine.getOption("option1"));
    }

    @Test
    public void testGetValues() {
        commandLine.addOption(argument1);
        commandLine.addValue(argument1, "value1");

        List values = new ArrayList();
        values.add("value2");
        commandLine.setDefaultValues(argument1, values);

        Assert.assertEquals(1, commandLine.getValues(argument1, null).size());
        Assert.assertEquals("value1", commandLine.getValues(argument1, null).get(0));

        Assert.assertEquals(2, commandLine.getValues(argument1, values).size());
        Assert.assertEquals("value1", commandLine.getValues(argument1, values).get(0));
        Assert.assertEquals("value2", commandLine.getValues(argument1, values).get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        commandLine.addOption(argument1);
        commandLine.addValue(argument1, "value1");

        Assert.assertEquals(1, commandLine.getUndefaultedValues(argument1).size());
        Assert.assertEquals("value1", commandLine.getUndefaultedValues(argument1).get(0));
    }

    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option1, true);

        Assert.assertTrue(commandLine.getSwitch(option1, false));
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty(option1, "property1", "value1");

        Assert.assertEquals("value1", commandLine.getProperty(option1, "property1", null));
        Assert.assertEquals(null, commandLine.getProperty(option1, "property2", null));
    }

    @Test
    public void testLooksLikeOption() {
        Assert.assertFalse(commandLine.looksLikeOption("test"));

        rootOption.addPrefix("-");

        Assert.assertTrue(commandLine.looksLikeOption("-test"));
    }

    @Test
    public void testToString() {
        arguments.add("argument1");
        arguments.add("argument2");

        Assert.assertEquals("argument1 argument2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List options = new ArrayList();
        options.add(option1);
        options.add(option2);

        Assert.assertEquals(options, commandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List triggers = new ArrayList();
        triggers.add("option1");
        triggers.add("option2");

        Assert.assertEquals(triggers, commandLine.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues() {
        commandLine.addOption(argument1);

        List defaults = new ArrayList();
        defaults.add("default1");
        defaults.add("default2");
        commandLine.setDefaultValues(argument1, defaults);

        Assert.assertEquals(defaults, commandLine.getValues(argument1, null));

        commandLine.setDefaultValues(argument1, null);

        Assert.assertEquals(null, commandLine.getValues(argument1, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.addOption(option1);

        commandLine.setDefaultSwitch(option1, true);

        Assert.assertTrue(commandLine.getSwitch(option1, false));

        commandLine.setDefaultSwitch(option1, false);

        Assert.assertFalse(commandLine.getSwitch(option1, true));
    }

    @Test
    public void testGetNormalised() {
        arguments.add("argument1");
        arguments.add("argument2");

        Assert.assertEquals(arguments, commandLine.getNormalised());
    }
}