import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("o", "opt", false, "Option test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        assertEquals(1, writeableCommandLine.getOptions().size());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        Option option = new Option("o", "opt", false, "Option test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addOption(option);
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "opt", false, "Option test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Option returnedOption = writeableCommandLine.getOption("opt");
        assertEquals(option, returnedOption);
    }

    @Test
    public void testGetOptionPrefixes() {
        Option option = new Option("g", "group", false, "Group option");
        Option groupOption = new Option("", "group", false, "Group");
        groupOption.addOption(option);
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(groupOption, new ArrayList<>());
        Option returnedOption = writeableCommandLine.getOption("group.opt");
        assertEquals(option, returnedOption);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "opt", false, "Option test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o", "opt", true, "Option with value");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("o", "opt", true, "Option with value");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        List values = writeableCommandLine.getUndefaultedValues(option);
        assertTrue(values.isEmpty());
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("o", "opt", false, "Option with switch");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        assertFalse(writeableCommandLine.getSwitch(option, null));
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("o", "opt", false, "Option with property");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addProperty(option, "prop", "value");
        assertEquals("value", writeableCommandLine.getProperty(option, "prop", null));
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("o", "opt", false, "Option with property");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addProperty(option, "prop1", "value1");
        writeableCommandLine.addProperty(option, "prop2", "value2");
        Set properties = writeableCommandLine.getProperties(option);
        assertEquals(2, properties.size());
        assertTrue(properties.contains("prop1"));
        assertTrue(properties.contains("prop2"));
    }

    @Test
    public void testLooksLikeOption() { {WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("o", "opt", false, "Option test"), new ArrayList<>());
        assertTrue(writeableCommandLine.looksLikeOption("opt"));
        assertFalse(writeableCommandLine.looksLikeOption("arg"));}
    }

    @Test
    public void testToString() {
        Option option = new Option("o", "opt", false, "Option test");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addValue(option, "arg1");
        writeableCommandLine.addValue(option, "arg2");
        String result = writeableCommandLine.toString();
        String expected = String.format("opt %s arg1 arg2", option.getLongOpt());
        assertEquals(expected, result);
    }

    @Test
    public void testGetOptionsList() {
        Option option1 = new Option("o1", "opt1", false, "Option 1");
        Option option2 = new Option("o2", "opt2", false, "Option 2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        writeableCommandLine.addOption(option2);
        List options = writeableCommandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggersSet() {
        Option option1 = new Option("o1", "opt1", false, "Option 1");
        Option option2 = new Option("o2", "opt2", false, "Option 2");
        Option option3 = new Option("o3", "opt3", false, "Option 3");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        writeableCommandLine.addOption(option3);
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        assertEquals(3, optionTriggers.size());
        assertTrue(optionTriggers.containsAll(Arrays.asList("o1", "o2", "o3")));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("o", "opt", true, "Option with value");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        List defaultValues = new ArrayList();
        defaultValues.add("value1");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("o", "opt", false, "Option with switch");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.setDefaultSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, null);
        assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        List normalised = new ArrayList();
        normalised.add("arg1");
        normalised.add("arg2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("o", "opt", false, "Option test"), normalised);
        List optionsList = writeableCommandLine.getOptions();
        assertEquals(0, optionsList.size());
        assertEquals(2, normalised.size());
        List getNormalised = writeableCommandLine.getNormalised();
        assertEquals(2, getNormalised.size());
        assertTrue(getNormalised.containsAll(normalised));
    }
}