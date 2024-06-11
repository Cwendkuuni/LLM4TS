import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option rootOption = new Option("root", "Root Option");
        List arguments = new ArrayList();
        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("option", "Test Option");
        commandLine.addOption(option);
        List options = commandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option, options.get(0));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("option", "Test Option");
        Object value = "Value";
        commandLine.addValue(option, value);
        List values = commandLine.getValues(option, Collections.emptyList());
        Assert.assertEquals(1, values.size());
        Assert.assertEquals(value, values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("option", "Test Option");
        boolean value = true;
        commandLine.addSwitch(option, value);
        Boolean switchValue = commandLine.getSwitch(option, null);
        Assert.assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("option", "Test Option");
        commandLine.addOption(option);
        boolean hasOption = commandLine.hasOption(option);
        Assert.assertTrue(hasOption);
    }

    @Test
    public void testGetOption() {
        Option option = new Option("option", "Test Option");
        commandLine.addOption(option);
        Option result = commandLine.getOption("option");
        Assert.assertEquals(option, result);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("option", "Test Option");
        List defaultValues = new ArrayList();
        defaultValues.add("Default");
        List values = new ArrayList();
        values.add("Value");
        commandLine.addValue(option, values.get(0));
        List result = commandLine.getValues(option, defaultValues);
        Assert.assertEquals(values, result);
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("option", "Test Option");
        Boolean defaultValue = true;
        commandLine.addSwitch(option, false);
        Boolean result = commandLine.getSwitch(option, defaultValue);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testAddProperty() {
        String property = "property";
        String value = "value";
        commandLine.addProperty(property, value);
        String result = commandLine.getProperty(property, null);
        Assert.assertEquals(value, result);
    }
    
    @Test
    public void testLooksLikeOption() {
        String trigger = "-option";
        boolean result = commandLine.looksLikeOption(trigger);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testToString() {
        List arguments = new ArrayList();
        arguments.add("argument1");
        arguments.add("argument2");
        commandLine = new WriteableCommandLineImpl(new Option("root", "Root Option"), arguments);
        String result = commandLine.toString();
        Assert.assertEquals("argument1 argument2", result);
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("option", "Test Option");
        List defaults = new ArrayList();
        defaults.add("Default");
        commandLine.setDefaultValues(option, defaults);
        List result = commandLine.getValues(option, null);
        Assert.assertEquals(defaults, result);
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("option", "Test Option");
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        Boolean result = commandLine.getSwitch(option, null);
        Assert.assertEquals(defaultSwitch, result);
    }
    
    @Test
    public void testGetNormalised() {
        List arguments = new ArrayList();
        arguments.add("-option");
        arguments.add("argument");
        commandLine = new WriteableCommandLineImpl(new Option("root", "Root Option"), arguments);
        List result = commandLine.getNormalised();
        Assert.assertEquals(arguments, result);
    }
}