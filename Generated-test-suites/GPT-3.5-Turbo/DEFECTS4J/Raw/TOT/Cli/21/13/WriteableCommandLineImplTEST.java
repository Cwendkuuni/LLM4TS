import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import static org.mockito.Mockito.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option mockOption;
    private Argument mockArgument;

    @Before
    public void setUp() {
        mockOption = mock(Option.class);
        mockArgument = mock(Argument.class);
        List arguments = new ArrayList();
        arguments.add("argument1");
        arguments.add("argument2");
        when(mockOption.getPrefixes()).thenReturn(Set.of("--"));
        when(mockOption.getPreferredName()).thenReturn("option1");
        commandLine = new WriteableCommandLineImpl(mockOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = mock(Option.class);
        when(option.getPreferredName()).thenReturn("subOption1");
        when(option.getTriggers()).thenReturn(Set.of("--subOption1"));

        commandLine.addOption(option);

        Assert.assertTrue(commandLine.getOptions().contains(option));
        Assert.assertEquals(option, commandLine.getOption("subOption1"));
    }

    @Test
    public void testAddValue() {
        String value = "value1";

        commandLine.addValue(mockArgument, value);

        Assert.assertTrue(commandLine.hasOption(mockArgument));
        List values = commandLine.getValues(mockArgument, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals(value, values.get(0));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(mockOption, true);

        Assert.assertTrue(commandLine.hasOption(mockOption));
        Assert.assertEquals(true, commandLine.getSwitch(mockOption, null));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_Exception() {
        commandLine.addSwitch(mockOption, true);
        commandLine.addSwitch(mockOption, false);
    }

    @Test
    public void testHasOption() {
        Assert.assertTrue(commandLine.hasOption(mockOption));
        Assert.assertFalse(commandLine.hasOption(mockArgument));
    }

    @Test
    public void testGetOption() {
        Option option = commandLine.getOption("option1");
        Assert.assertEquals(mockOption, option);
    }

    @Test
    public void testGetValues() {
        String value1 = "value1";
        String value2 = "value2";
        when(mockOption.getParent()).thenReturn(mockArgument);
        commandLine.addValue(mockOption, value1);
        List<String> defaults = new ArrayList<>();
        defaults.add(value2);

        List values = commandLine.getValues(mockOption, defaults);

        Assert.assertEquals(2, values.size());
        Assert.assertEquals(value1, values.get(0));
        Assert.assertEquals(value2, values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        String value1 = "value1";
        String value2 = "value2";
        when(mockOption.getParent()).thenReturn(mockArgument);
        commandLine.addValue(mockOption, value1);
        List<String> defaults = new ArrayList<>();
        defaults.add(value2);

        List values = commandLine.getUndefaultedValues(mockOption);

        Assert.assertEquals(1, values.size());
        Assert.assertEquals(value1, values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Boolean defaultValue = false;

        Boolean bool = commandLine.getSwitch(mockOption, defaultValue);

        Assert.assertEquals(defaultValue, bool);
    }

    @Test
    public void testGetProperty() {
        String property = "property1";
        String value = "value1";
        commandLine.addProperty(property, value);

        String retrievedValue = commandLine.getProperty(property);

        Assert.assertEquals(value, retrievedValue);
    }

    @Test
    public void testAddProperty() {
        String property = "property1";
        String value = "value1";
        Option option = mock(Option.class);
        commandLine.addProperty(option, property, value);

        String retrievedValue = commandLine.getProperty(option, property, null);

        Assert.assertEquals(value, retrievedValue);
    }

    @Test
    public void testGetProperty_DefaultValue() {
        String property = "property1";
        String defaultValue = "defaultValue";

        String retrievedValue = commandLine.getProperty(property, defaultValue);

        Assert.assertEquals(defaultValue, retrievedValue);
    }

    @Test
    public void testGetProperties() {
        String property1 = "property1";
        String value1 = "value1";
        String property2 = "property2";
        String value2 = "value2";
        commandLine.addProperty(property1, value1);
        commandLine.addProperty(property2, value2);

        Set properties = commandLine.getProperties();

        Assert.assertEquals(2, properties.size());
        Assert.assertTrue(properties.contains(property1));
        Assert.assertTrue(properties.contains(property2));
    }

    @Test
    public void testGetProperties_SpecificOption() {
        String property1 = "property1";
        String value1 = "value1";
        when(mockOption.getPreferredName()).thenReturn("option2");
        commandLine.addProperty(mockOption, property1, value1);

        Set properties = commandLine.getProperties(mockOption);

        Assert.assertEquals(1, properties.size());
        Assert.assertTrue(properties.contains(property1));
        Assert.assertFalse(properties.contains("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "--trigger";

        boolean result = commandLine.looksLikeOption(trigger);

        Assert.assertTrue(result);
    }

    @Test
    public void testToString() {
        String expected = "argument1 argument2";

        String result = commandLine.toString();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetNormalised() {
        List normalised = commandLine.getNormalised();

        Assert.assertEquals(2, normalised.size());
        Assert.assertEquals("argument1", normalised.get(0));
        Assert.assertEquals("argument2", normalised.get(1));
    }
}