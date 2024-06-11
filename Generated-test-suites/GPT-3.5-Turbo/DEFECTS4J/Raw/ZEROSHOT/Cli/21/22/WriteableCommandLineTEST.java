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
        Option option = new OptionImpl("testOption");
        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption("testOption"));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionImpl("testOption");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        Assert.assertEquals(1, commandLine.getValues(option).size());
        Assert.assertEquals("value1", commandLine.getValues(option).get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new OptionImpl("testOption");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> undefaultedValues = commandLine.getUndefaultedValues(option);

        Assert.assertEquals(2, undefaultedValues.size());
        Assert.assertEquals("value1", undefaultedValues.get(0));
        Assert.assertEquals("value2", undefaultedValues.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionImpl("testOption");
        commandLine.addOption(option);

        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");

        commandLine.setDefaultValues(option, defaultValues);

        Assert.assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionImpl("testOption");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        Assert.assertTrue(commandLine.getSwitchValue(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionImpl("testOption");
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);

        Assert.assertTrue(commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        Option option = new OptionImpl("testOption");
        commandLine.addOption(option);
        commandLine.addProperty(option, "property1", "value1");

        Assert.assertEquals("value1", commandLine.getProperty(option, "property1"));
    }

    @Test
    public void testAddDefaultProperty() {
        commandLine.addProperty("property1", "value1");

        Assert.assertEquals("value1", commandLine.getDefaultProperty("property1"));
    }

    @Test
    public void testLooksLikeOption() {
        Assert.assertTrue(commandLine.looksLikeOption("-option"));
        Assert.assertFalse(commandLine.looksLikeOption("value"));
    }
}