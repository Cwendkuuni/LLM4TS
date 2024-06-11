import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.argument.MultiSwitchArgument;
import org.apache.commons.cli2.option.ArgumentImpl;
import org.apache.commons.cli2.option.PropertyOption;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    // setup test objects
    private Option option1;
    private Option option2;
    private Option option3;
    private Option option4;
    private Option option5;
    private List arguments;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        option1 = new ArgumentImpl("arg1");
        option2 = new ArgumentImpl("arg2");
        option3 = new ArgumentImpl("arg3");
        option4 = new ArgumentImpl("arg4");
        option5 = new PropertyOption();
        arguments = new ArrayList<>();
        commandLine = new WriteableCommandLineImpl(option1, arguments);
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option1);
        assertTrue(commandLine.hasOption(option1));
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(option1, "value1");
        assertEquals(1, commandLine.getValues(option1, null).size());
        assertEquals("value1", commandLine.getValues(option1, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option5, true);
        assertEquals(true, commandLine.getSwitch(option5, false));
    }

    @Test
    public void testHasOption() {
        commandLine.addOption(option1);
        assertTrue(commandLine.hasOption(option1));
    }

    @Test
    public void testGetOption() {
        commandLine.addOption(option1);
        assertEquals(option1, commandLine.getOption("arg1"));
    }

    @Test
    public void testGetValues() {
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        assertEquals(2, commandLine.getValues(option1, null).size());
        assertEquals("value1", commandLine.getValues(option1, null).get(0));
        assertEquals("value2", commandLine.getValues(option1, null).get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        assertEquals(2, commandLine.getUndefaultedValues(option1).size());
        assertEquals("value1", commandLine.getUndefaultedValues(option1).get(0));
        assertEquals("value2", commandLine.getUndefaultedValues(option1).get(1));
    }

    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option5, true);
        assertEquals(true, commandLine.getSwitch(option5, false));
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty(option5, "property1", "value1");
        assertEquals("value1", commandLine.getProperty(option5, "property1", null));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty(option5, "property1", "value1");
        assertEquals("value1", commandLine.getProperty(option5, "property1", null));
    }

    @Test
    public void testGetProperties() {
        commandLine.addProperty(option5, "property1", "value1");
        assertEquals(1, commandLine.getProperties(option5).size());
        assertTrue(commandLine.getProperties(option5).contains("property1"));
    }

    @Test
    public void testGetOptionTriggers() {
        commandLine.addOption(option1);
        assertEquals(1, commandLine.getOptionTriggers().size());
        assertTrue(commandLine.getOptionTriggers().contains("arg1"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-arg1"));
        assertFalse(commandLine.looksLikeOption("value1"));
    }

    @Test
    public void testToString() {
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        assertEquals("value1 value2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertEquals(2, commandLine.getOptions().size());
        assertTrue(commandLine.getOptions().contains(option1));
        assertTrue(commandLine.getOptions().contains(option2));
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option5, true);
        assertEquals(true, commandLine.getDefaultSwitch(option5));
    }

    @Test
    public void testGetNormalised() {
        arguments.add("argument1");
        commandLine = new WriteableCommandLineImpl(option1, arguments);
        assertEquals(1, commandLine.getNormalised().size());
        assertTrue(commandLine.getNormalised().contains("argument1"));
    }
}