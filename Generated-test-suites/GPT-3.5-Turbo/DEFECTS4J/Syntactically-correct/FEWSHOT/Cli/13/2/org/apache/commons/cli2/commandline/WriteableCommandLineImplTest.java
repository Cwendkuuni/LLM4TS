import org.junit.Test;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Argument;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
    @Test
    public void testAddOption() {
        Option option = new Option("t", "test");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
        assertEquals(option, commandLine.getOption("t"));
        assertEquals(option, commandLine.getOption("test"));
    }

    @Test
    public void testAddValue() {
        Option option = new Argument();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> expected = new ArrayList<>();
        expected.add("value1");
        expected.add("value2");

        assertEquals(expected, commandLine.getValues(option, null));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("t", "test");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.hasOption(option));
        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("t", "test");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("t", "test");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option);

        assertEquals(option, commandLine.getOption("t"));
        assertEquals(option, commandLine.getOption("test"));
    }

    @Test
    public void testGetValues() {
        Option option = new Argument();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> expected = new ArrayList<>();
        expected.add("value1");
        expected.add("value2");

        assertEquals(expected, commandLine.getValues(option, null));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("t", "test");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");

        assertEquals("value1", commandLine.getProperty("property1", null));
        assertEquals("value2", commandLine.getProperty("property2", null));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(new Option("t", null));

        assertTrue(commandLine.looksLikeOption("-t"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(new Option("t", null));
        commandLine.addValue(new Argument(), "value");

        assertNotNull(commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("t", null);
        Option option2 = new Option("f", null);

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List<Option> expected = new ArrayList<>();
        expected.add(option1);
        expected.add(option2);

        assertEquals(expected, commandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("t", null);
        Option option2 = new Option("f", null);

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set<String> expected = new HashSet<>();
        expected.add("t");
        expected.add("f");

        assertEquals(expected, commandLine.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Argument();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.setDefaultValues(option, Arrays.asList("default1", "default2"));

        assertEquals(Arrays.asList("default1", "default2"), commandLine.getValues(option, null));

        commandLine.setDefaultValues(option, null);
        assertEquals(Collections.emptyList(), commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("t", "test");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, null));

        commandLine.setDefaultSwitch(option, null);
        assertNull(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Arrays.asList("arg1", "arg2"));

        assertEquals(Arrays.asList("arg1", "arg2"), commandLine.getNormalised());
    }
}