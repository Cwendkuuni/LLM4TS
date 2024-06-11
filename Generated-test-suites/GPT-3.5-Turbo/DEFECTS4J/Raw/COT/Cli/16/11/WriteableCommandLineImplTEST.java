
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setup() {
        Option rootOption = new Option("rootOption");
        List<String> arguments = new ArrayList<>();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("testOption");
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
        Assert.assertEquals(option, commandLine.getOption("testOption"));
        Assert.assertTrue(commandLine.getOptions().contains(option));
        Assert.assertTrue(commandLine.getOptionTriggers().contains("testOption"));
    }

    @Test
    public void testAddValue() {
        Argument argument = new Argument();
        Object value = "testValue";
        commandLine.addValue(argument, value);
        Assert.assertEquals(value, commandLine.getValues(argument, null).get(0));
        Assert.assertEquals(value, commandLine.getUndefaultedValues(argument).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("testOption");
        boolean value = true;
        commandLine.addSwitch(option, value);
        Assert.assertEquals(value, commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        PropertyOption option = new PropertyOption();
        String property = "testProperty";
        String value = "testValue";
        commandLine.addProperty(option, property, value);
        Assert.assertEquals(value, commandLine.getProperty(option, property, null));
    }
}
