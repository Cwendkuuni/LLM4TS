
import org.apache.commons.cli2.util.HelpFormatter;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setup() {
        Option rootOption = new OptionBuilder().withPreferredName("rootOption").create();
        List arguments = new ArrayList<String>();

        commandLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new OptionBuilder().withPreferredName("option1").create();
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionBuilder().withPreferredName("option1").create();
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");

        List<String> values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionBuilder().withPreferredName("option1").create();
        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, null);
        assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    public void testHasOption() {
        Option option = new OptionBuilder().withPreferredName("option1").create();

        assertFalse(commandLine.hasOption(option));

        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new OptionBuilder().withPreferredName("option1").create();
        commandLine.addOption(option);

        Option retrievedOption = commandLine.getOption("option1");

        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        Option option1 = new OptionBuilder().withPreferredName("option1").create();
        Option option2 = new OptionBuilder().withPreferredName("option2").create();

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        commandLine.addValue(option1, "value1");
        commandLine.addValue(option2, "value2");

        List<String> values1 = commandLine.getValues(option1, null);
        List<String> values2 = commandLine.getValues(option2, null);

        assertEquals(1, values1.size());
        assertEquals("value1", values1.get(0));

        assertEquals(1, values2.size());
        assertEquals("value2", values2.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option1 = new OptionBuilder().withPreferredName("option1").create();
        Option option2 = new OptionBuilder().withPreferredName("option2").create();

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        commandLine.addSwitch(option1, true);
        commandLine.addSwitch(option2, false);

        Boolean switchValue1 = commandLine.getSwitch(option1, null);
        Boolean switchValue2 = commandLine.getSwitch(option2, null);

        assertEquals(Boolean.TRUE, switchValue1);
        assertEquals(Boolean.FALSE, switchValue2);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property1", "value1");

        String propertyValue = commandLine.getProperty("property1", null);

        assertEquals("value1", propertyValue);
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option1"));
        assertFalse(commandLine.looksLikeOption("value1"));
    }

    @Test
    public void testToString() {
        Option option1 = new OptionBuilder().withPreferredName("option1").create();
        Option option2 = new OptionBuilder().withPreferredName("option2").withPrefixes("-").create();

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        commandLine.addValue(option1, "value1");
        commandLine.addValue(option2, "value2");

        String output = commandLine.toString();

        assertEquals("option1=value1 -option2 value2", output);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new OptionBuilder().withPreferredName("option1").create();
        Option option2 = new OptionBuilder().withPreferredName("option2").create();

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List<Option> options = commandLine.getOptions();

        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new OptionBuilder().withPreferredName("option1").create();
        Option option2 = new OptionBuilder().withPreferredName("option2").create();

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set<String> triggers = commandLine.getOptionTriggers();

        assertEquals(2, triggers.size());
        assertTrue(triggers.contains("option1"));
        assertTrue(triggers.contains("option2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionBuilder().withPreferredName("option1").create();

        List<String> defaults = new ArrayList<String>();
        defaults.add("default1");

        commandLine.setDefaultValues(option, defaults);

        List<String> defaultValues = commandLine.getValues(option, null);

        assertEquals(1, defaultValues.size());
        assertEquals("default1", defaultValues.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionBuilder().withPreferredName("option1").create();

        commandLine.setDefaultSwitch(option, true);

        Boolean defaultSwitchValue = commandLine.getSwitch(option, null);

        assertEquals(Boolean.TRUE, defaultSwitchValue);
    }

    @Test
    public void testGetNormalised() {
        List<String> arguments = new ArrayList<String>();
        arguments.add("arg1");
        arguments.add("arg2");

        commandLine = new WriteableCommandLineImpl(null, arguments);

        List<String> normalised = commandLine.getNormalised();

        assertEquals(2, normalised.size());
        assertEquals("arg1", normalised.get(0));
        assertEquals("arg2", normalised.get(1));
    }
}
