import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.option.Command;
import org.apache.commons.cli2.option.DefaultOption;
import org.apache.commons.cli2.option.PropertyOption;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Argument argument1 = new Argument();
        Argument argument2 = new Argument();

        List<Argument> arguments = new ArrayList<>();
        arguments.add(argument1);
        arguments.add(argument2);

        DefaultOption option1 = new DefaultOption();
        DefaultOption option2 = new DefaultOption();

        option1.setName("option1");
        option2.setName("option2");

        List<DefaultOption> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);

        commandLine = new WriteableCommandLineImpl(option1, arguments);
    }

    @Test
    public void testAddOption() {
        DefaultOption option3 = new DefaultOption();
        option3.setName("option3");

        commandLine.addOption(option3);

        assertTrue(commandLine.getOptions().contains(option3));
        assertTrue(commandLine.getOptionTriggers().contains("option3"));
    }

    @Test
    public void testAddValue() {
        Argument argument1 = (Argument) commandLine.getOptions().get(0);
        Argument argument2 = (Argument) commandLine.getOptions().get(1);

        commandLine.addValue(argument1, "value1");
        commandLine.addValue(argument2, "value2");

        assertTrue(commandLine.getUndefaultedValues(argument1).contains("value1"));
        assertTrue(commandLine.getUndefaultedValues(argument2).contains("value2"));
    }

    @Test
    public void testAddSwitch() {
        DefaultOption option1 = (DefaultOption) commandLine.getOptions().get(0);
        DefaultOption option2 = (DefaultOption) commandLine.getOptions().get(1);

        commandLine.addSwitch(option1, true);
        commandLine.addSwitch(option2, false);

        assertTrue(commandLine.getSwitch(option1, false));
        assertFalse(commandLine.getSwitch(option2, true));
    }

    @Test
    public void testHasOption() {
        DefaultOption option1 = (DefaultOption) commandLine.getOptions().get(0);
        DefaultOption option2 = (DefaultOption) commandLine.getOptions().get(1);

        assertTrue(commandLine.hasOption(option1));
        assertTrue(commandLine.hasOption(option2));
    }

    @Test
    public void testGetOption() {
        DefaultOption option1 = (DefaultOption) commandLine.getOptions().get(0);
        DefaultOption option2 = (DefaultOption) commandLine.getOptions().get(1);

        assertEquals(option1, commandLine.getOption("option1"));
        assertEquals(option2, commandLine.getOption("option2"));
    }

    @Test
    public void testGetValues() {
        Argument argument1 = (Argument) commandLine.getOptions().get(0);
        Argument argument2 = (Argument) commandLine.getOptions().get(1);

        commandLine.addValue(argument1, "value1");
        commandLine.addValue(argument1, "value2");

        List<String> expected = new ArrayList<>();
        expected.add("value1");
        expected.add("value2");

        assertEquals(expected, commandLine.getValues(argument1, null));
        assertEquals(expected, commandLine.getValues(argument1, expected));
        assertEquals(expected, commandLine.getValues(argument1, new ArrayList<>()));
    }

    @Test
    public void testGetUndefaultedValues() {
        Argument argument1 = (Argument) commandLine.getOptions().get(0);
        Argument argument2 = (Argument) commandLine.getOptions().get(1);

        commandLine.addValue(argument1, "value1");
        commandLine.addValue(argument1, "value2");

        List<String> expected = new ArrayList<>();
        expected.add("value1");
        expected.add("value2");

        assertEquals(expected, commandLine.getUndefaultedValues(argument1));
        assertTrue(commandLine.getUndefaultedValues(argument2).isEmpty());
    }

    @Test
    public void testGetSwitch() {
        DefaultOption option1 = (DefaultOption) commandLine.getOptions().get(0);
        DefaultOption option2 = (DefaultOption) commandLine.getOptions().get(1);

        commandLine.addSwitch(option1, true);
        commandLine.addSwitch(option1, false);

        assertTrue(commandLine.getSwitch(option1, null));
        assertNull(commandLine.getSwitch(option2, null));
    }

    @Test
    public void testGetProperty() {
        PropertyOption propertyOption = new PropertyOption();

        commandLine.addProperty(propertyOption, "property1", "value1");
        commandLine.addProperty(propertyOption, "property2", "value2");

        assertEquals("value1", commandLine.getProperty(propertyOption, "property1", null));
        assertEquals("value2", commandLine.getProperty(propertyOption, "property2", null));
        assertNull(commandLine.getProperty(propertyOption, "property3", null));
    }

    @Test
    public void testAddProperty() {
        PropertyOption propertyOption = new PropertyOption();

        commandLine.addProperty(propertyOption, "property1", "value1");
        commandLine.addProperty(propertyOption, "property2", "value2");

        assertTrue(commandLine.getProperties(propertyOption).contains("property1"));
        assertTrue(commandLine.getProperties(propertyOption).contains("property2"));
        assertFalse(commandLine.getProperties(propertyOption).contains("property3"));
    }

    @Test
    public void testGetProperties() {
        PropertyOption propertyOption = new PropertyOption();

        commandLine.addProperty(propertyOption, "property1", "value1");
        commandLine.addProperty(propertyOption, "property2", "value2");

        assertTrue(commandLine.getProperties().contains("property1"));
        assertTrue(commandLine.getProperties().contains("property2"));
        assertFalse(commandLine.getProperties().contains("property3"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option1"));
        assertFalse(commandLine.looksLikeOption("value1"));
    }

    @Test
    public void testToString() {
        Argument argument1 = (Argument) commandLine.getOptions().get(0);
        Argument argument2 = (Argument) commandLine.getOptions().get(1);

        commandLine.addValue(argument1, "value1");
        commandLine.addValue(argument2, "value2");

        assertEquals("value1 value2", commandLine.toString());
    }

    @Test
    public void testGetNormalised() {
        Argument argument1 = (Argument) commandLine.getOptions().get(0);
        Argument argument2 = (Argument) commandLine.getOptions().get(1);

        commandLine.addValue(argument1, "value1");
        commandLine.addValue(argument2, "value2");

        List<String> expected = new ArrayList<>();
        expected.add("value1");
        expected.add("value2");

        assertEquals(expected, commandLine.getNormalised());
    }
}