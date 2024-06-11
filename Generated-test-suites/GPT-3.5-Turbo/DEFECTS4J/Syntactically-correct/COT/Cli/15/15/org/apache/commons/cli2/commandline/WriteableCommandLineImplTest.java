import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        List<String> arguments = new ArrayList<>();
        Option rootOption = new OptionImpl();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new OptionImpl();
        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new ArgumentImpl();
        Object value = "value";
        commandLine.addValue(option, value);

        List<Object> values = commandLine.getValues(option, null);

        Assert.assertEquals(1, values.size());
        Assert.assertEquals(value, values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionImpl();
        boolean value = true;
        commandLine.addSwitch(option, value);

        Boolean switchValue = commandLine.getSwitch(option, null);

        Assert.assertEquals(value, switchValue.booleanValue());
    }

    @Test
    public void testHasOption() {
        Option option = new OptionImpl();
        commandLine.addOption(option);

        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new OptionImpl();
        commandLine.addOption(option);

        Option foundOption = commandLine.getOption("trigger");

        Assert.assertEquals(option, foundOption);
    }

    @Test
    public void testGetValues() {
        Option option = new ArgumentImpl();
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("default");
        commandLine.addValue(option, "value");

        List<Object> values = commandLine.getValues(option, defaultValues);

        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new ArgumentImpl();
        commandLine.addValue(option, "value");

        List<Object> values = commandLine.getUndefaultedValues(option);

        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new OptionImpl();
        Boolean defaultValue = Boolean.TRUE;
        commandLine.addSwitch(option, false);

        Boolean switchValue = commandLine.getSwitch(option, defaultValue);

        Assert.assertEquals(Boolean.FALSE, switchValue);
    }

    @Test
    public void testGetProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);

        String propertyValue = commandLine.getProperty(property);

        Assert.assertEquals(value, propertyValue);
    }

    @Test
    public void testAddProperty() {
        Option option = new PropertyOptionImpl();
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);

        String propertyValue = commandLine.getProperty(option, property, "");

        Assert.assertEquals(value, propertyValue);
    }

    @Test
    public void testGetProperties() {
        Option option = new PropertyOptionImpl();
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);

        Set<String> properties = commandLine.getProperties(option);

        Assert.assertEquals(1, properties.size());
        Assert.assertTrue(properties.contains(property));
    }

    @Test
    public void testLooksLikeOption() {
        boolean looksLikeOption = commandLine.looksLikeOption("-t");
        Assert.assertTrue(looksLikeOption);
    }

    @Test
    public void testToString() {
        Option option = new OptionImpl();
        commandLine.addOption(option);
        commandLine.addValue(option, "value");

        String output = commandLine.toString();

        Assert.assertEquals("value", output);
    }

    @Test
    public void testGetOptions() {
        Option option = new OptionImpl();
        commandLine.addOption(option);

        List<Option> options = commandLine.getOptions();

        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option, options.get(0));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new OptionImpl();
        commandLine.addOption(option);

        Set<String> triggers = commandLine.getOptionTriggers();

        Assert.assertEquals(1, triggers.size());
        Assert.assertTrue(triggers.contains(option.getPreferredName()));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionImpl();
        List<String> defaults = new ArrayList<>();
        defaults.add("default");
        commandLine.setDefaultValues(option, defaults);

        List<String> defaultValues = commandLine.getValues(option, null);

        Assert.assertEquals(1, defaultValues.size());
        Assert.assertEquals("default", defaultValues.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionImpl();
        Boolean defaultSwitch = Boolean.TRUE;
        commandLine.setDefaultSwitch(option, defaultSwitch);

        Boolean switchValue = commandLine.getSwitch(option, null);

        Assert.assertEquals(defaultSwitch, switchValue);
    }

    @Test
    public void testGetNormalised() {
        List<String> normalised = commandLine.getNormalised();

        Assert.assertEquals(0, normalised.size());
    }

    private class OptionImpl implements Option {
        @Override
        public Set<String> getTriggers() {
            return null;
        }

        @Override
        public String getPreferredName() {
            return null;
        }

        @Override
        public Set<String> getPrefixes() {
            return null;
        }
    }

    private class ArgumentImpl extends OptionImpl implements Argument {
    }

    private class PropertyOptionImpl extends OptionImpl implements PropertyOption {
    }
}