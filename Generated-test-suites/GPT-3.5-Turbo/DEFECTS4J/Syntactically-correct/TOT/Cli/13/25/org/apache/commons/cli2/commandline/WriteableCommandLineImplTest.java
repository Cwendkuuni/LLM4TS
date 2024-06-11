import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DefaultOption;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineImplTest {

    private WriteableCommandLine commandLine;
    private Option option;

    @Before
    public void setup() {
        option = new DefaultOption.Builder("o").withDescription("Option").create();
        List<Argument> arguments = new ArrayList<>();
        arguments.add(new Argument("arg1"));
        commandLine = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void testAddOption() {
        Option option2 = new DefaultOption.Builder("o2").withDescription("Option 2").create();
        commandLine.addOption(option2);
        Assert.assertTrue(commandLine.hasOption(option2));
    }

    @Test
    public void testAddValue() {
        Object value = new Object();
        commandLine.addValue(option, value);
        List<Object> values = commandLine.getValues(option, new ArrayList<>());
        Assert.assertEquals(1, values.size());
        Assert.assertEquals(value, values.get(0));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Assert.assertEquals(option, commandLine.getOption("o"));
    }

    @Test
    public void testGetValues() {
        Object value1 = new Object();
        Object value2 = new Object();
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add(value1);
        defaultValues.add(value2);
        List<Object> values = commandLine.getValues(option, defaultValues);
        Assert.assertEquals(defaultValues, values);
    }

    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("key", "value");
        Assert.assertEquals("value", commandLine.getProperty("key", null));
    }

    @Test
    public void testLooksLikeOption() {
        Assert.assertTrue(commandLine.looksLikeOption("-o"));
        Assert.assertFalse(commandLine.looksLikeOption("arg1"));
    }

}