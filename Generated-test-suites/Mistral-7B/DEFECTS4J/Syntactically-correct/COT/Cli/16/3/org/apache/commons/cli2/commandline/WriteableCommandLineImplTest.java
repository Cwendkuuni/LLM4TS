import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("o", "opt", true, "Option test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionWithDuplicateTrigger() {
        Option option = new Option("o", "opt", true, "Option test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        Option option = new Option("o", "opt", true, "Option test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("s", "switch", false, "Switch test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "opt", true, "Option test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Option retrievedOption = writeableCommandLine.getOption("opt");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o", "opt", true, "Option test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("o", "opt", true, "Option test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");
        List values = writeableCommandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("s", "switch", false, "Switch test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
        writeableCommandLine.addSwitch(option, false);
        assertFalse(writeableCommandLine.getSwitch(option, false));
        writeableCommandLine.addSwitch(option, null);
        assertNull(writeableCommandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("p", "prop", true, "Property test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property", "value");
        assertEquals("value", writeableCommandLine.getProperty(option, "property", null));
    }

    @Test
    public void testLookupOption() {
        Option option = new Option("o", "opt", true, "Option test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.looksLikeOption("opt"));
    }

    @Test
    public void testToString() {
        Option option = new Option("o", "opt", false, "Option test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        String commandLineString = writeableCommandLine.toString();
        assertTrue(commandLineString.contains("opt"));
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o1", "opt1", true, "Option test1");
        Option option2 = new Option("o2", "opt2", false, "Option test2");
        WriteableCommandLineImpl writeableCommandLine
                = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        List options = writeableCommandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("o1", "opt1", true, "Option test1");
        Option option2 = new Option("o2", "opt2", false, "Option test2");
        WriteableCommandLineImpl writeableCommandLine
                = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("opt1"));
        assertTrue(optionTriggers.contains("opt2"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testModifyList() {
        Option option = new Option("o", "opt", false, "Option test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        List normalised = writeableCommandLine.getNormalised();
        normalised.add("newArgument");
    }
}