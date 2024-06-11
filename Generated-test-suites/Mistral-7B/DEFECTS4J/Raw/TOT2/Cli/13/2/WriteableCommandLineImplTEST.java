import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_addOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
        Option option = new Option("testOption", "Test option");
        commandLine.addOption(option);

        Option addedOption = commandLine.getOption("testOption");
        assertEquals(option, addedOption);
    }

    @Test
    public void testWriteableCommandLineImpl_addValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
        Option option = new Option("testOption", "Test option", true);
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getValues(option, null);
        assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testWriteableCommandLineImpl_addSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("testSwitch"), new ArrayList<>());
        Option option = new Option("testSwitch", true, "s");
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImpl_addSwitch_alreadySet() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("testSwitch"), new ArrayList<>());
        Option option = new Option("testSwitch", true, "s");
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
        Option option = new Option("testOption", "Test option");
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testWriteableCommandLineImpl_getOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
        Option option = new Option("testOption", "Test option");
        commandLine.addOption(option);

        Option retrievedOption = commandLine.getOption("testOption");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testWriteableCommandLineImpl_getValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
        Option option = new Option("testOption", "Test option");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getValues(option, null);
        assertEquals(Arrays.asList("value1", "value2"), values);
    }

    @Test
    public void testWriteableCommandLineImpl_getSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("testSwitch"), new ArrayList<>());
        Option option = new Option("testSwitch", true, "s");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_addProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
        commandLine.addProperty("testProperty", "testValue");

        assertEquals("testValue", commandLine.getProperty("testProperty", ""));
    }

    @Test
    public void testWriteableCommandLineImpl_toString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("testOption"), new ArrayList<>());
        commandLine.addOption(new Option("testOption", "Test option"));
        commandLine.addValue(commandLine.getOption("testOption"), "testValue");

        assertEquals("testValue testOption", commandLine.toString());
    }
}