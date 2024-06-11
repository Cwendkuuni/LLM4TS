import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("o"), Collections.emptyList());
        Option option = new Option("a", "a", true, "An option with a flag");
        cmdLine.addOption(option);
        assertTrue(cmdLine.hasOption(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("o"), Collections.emptyList());
        Option option = new Option("a", "a", true, "An option with a flag");
        cmdLine.addOption(option);
        cmdLine.addOption(option);
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("o"), Collections.emptyList());
        Option option = new Option("a", "a", true, "An option with a flag");
        cmdLine.addOption(option);
        Option result = cmdLine.getOption("a");
        assertEquals(option, result);
    }

    @Test
    public void testGetOptionNull() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("o"), Collections.emptyList());
        Option result = cmdLine.getOption("x");
        assertNull(result);
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("o"), Collections.emptyList());
        Option option = new Option("a", "a", true, "An option with a flag");
        cmdLine.addOption(option);
        cmdLine.addValue(option, "value1");
        List values = cmdLine.getValues(option, Collections.emptyList());
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        cmdLine.addValue(option, "value2");
        values = cmdLine.getValues(option, Collections.emptyList());
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("o"), Collections.emptyList());
        Option option = new Option("a", "a", true, "An option with a flag");
        cmdLine.addOption(option);
        cmdLine.addValue(option, "value1");
        List values = cmdLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("o"), Collections.emptyList());
        Option option = new Option("a", "a", false, "An option without a flag");
        cmdLine.addOption(option);
        cmdLine.addSwitch(option, false);
        Boolean switchValue = cmdLine.getSwitch(option, Boolean.FALSE);
        assertFalse(switchValue);

        cmdLine.addSwitch(option, true);
        switchValue = cmdLine.getSwitch(option, Boolean.FALSE);
        assertTrue(switchValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("o"), Collections.emptyList());
        Option option = new Option("a", "a", false, "An option without a flag");
        Properties properties = new Properties();
        properties.setProperty("prop1", "value1");
        cmdLine.addOption(option);
        cmdLine.addProperty(option, "prop1", "value1");
        Map<String, String> result = cmdLine.getProperties(option);
        assertEquals(1, result.size());
        assertEquals("value1", result.get("prop1"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("o"), Collections.asList("arg1", "arg2"));
        assertEquals("arg1 arg2", cmdLine.toString());
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("o"), Collections.emptyList());
        Option option = new Option("a", "a", true, "An option with a flag");
        cmdLine.addOption(option);
        assertTrue(cmdLine.hasOption(option));
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("o"), Collections.emptyList());
        Option option1 = new Option("a1", "a1", true, "Option 1");
        Option option2 = new Option("a2", "a2", true, "Option 2");
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);
        List<Option> options = cmdLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("o"), Collections.emptyList());
        Option option1 = new Option("a1", "a1", true, "Option 1");
        Option option2 = new Option("a2", "a2", true, "Option 2");
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);
        Set<String> optionTriggers = cmdLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("a1"));
        assertTrue(optionTriggers.contains("a2"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testModifyOptions() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("o"), Collections.emptyList());
        Option option = new Option("a", "a", true, "An option with a flag");
        cmdLine.addOption(option);
        List<Option> options = cmdLine.getOptions();
        options.remove(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionWithNullName() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("o"), Collections.emptyList());
        Option option = new Option(null, "a", true, "An option with a flag");
        cmdLine.addOption(option);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionWithNullValue() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("o"), Collections.emptyList());
        Option option = new Option("a", null, true, "An option with a flag");
        cmdLine.addOption(option);
    }
}