
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setup() {
        commandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new OptionImpl();
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOptions().get(0));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionImpl();
        Object value = new Object();
        commandLine.addValue(option, value);
        Assert.assertEquals(value, commandLine.getValues().get(option));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new OptionImpl();
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        commandLine.getUndefaultedValues(option).addAll(values);
        Assert.assertEquals(values, commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionImpl();
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);
        Assert.assertEquals(defaultValues, commandLine.getDefaultValues().get(option));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionImpl();
        boolean value = true;
        commandLine.addSwitch(option, value);
        Assert.assertEquals(value, commandLine.getSwitches().get(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = new OptionImpl();
        boolean value1 = true;
        boolean value2 = false;
        commandLine.addSwitch(option, value1);
        commandLine.addSwitch(option, value2);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionImpl();
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        Assert.assertEquals(defaultSwitch, commandLine.getDefaultSwitches().get(option));
    }

    @Test
    public void testAddProperty() {
        Option option = new OptionImpl();
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);
        Assert.assertEquals(value, commandLine.getProperties().get(option).get(property));
    }

    @Test
    public void testAddPropertyToDefaultSet() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        Assert.assertEquals(value, commandLine.getDefaultProperties().get(property));
    }

    @Test
    public void testLooksLikeOption() {
        String argument = "-o";
        Assert.assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testSetCurrentOption() {
        Option option = new OptionImpl();
        commandLine.setCurrentOption(option);
        Assert.assertEquals(option, commandLine.getCurrentOption());
    }
}
