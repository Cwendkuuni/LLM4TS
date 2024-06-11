import org.apache.commons.cli2.*;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;
import org.junit.Test;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testCreateWriteableCommandLine() {
        Option option = Option.builder("o").longOpt("option")
                .desc("An option")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        assertEquals(option, commandLine.getOption("o"));
        assertEquals(option, commandLine.getOption("option"));
        assertEquals(Collections.emptyList(), commandLine.getOptions());
        assertEquals(Collections.emptySet(), commandLine.getOptionTriggers());
    }

    @Test
    public void testAddOption() {
        Option option = Option.builder("o").longOpt("option")
                .desc("An option")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);

        assertEquals(option, commandLine.getOption("o"));
        assertEquals(option, commandLine.getOption("option"));
        assertEquals(Collections.singletonList(option), commandLine.getOptions());
        assertEquals(Collections.singleton("o"), commandLine.getOptionTriggers());
    }

    @Test
    public void testAddValue() {
        Option option = Option.builder("o").longOpt("option")
                .desc("An option with a value")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addValue(option, "value1");

        assertEquals(Collections.singletonList("value1"), commandLine.getValues(option, null));
        assertEquals(Collections.singleton("value1"), commandLine.getUndefaultedValues(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddValueWithExistingValue() {
        Option option = Option.builder("o").longOpt("option")
                .desc("An option with a value")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.singletonList("value1"));
        commandLine.addValue(option, "value2");
    }

    @Test
    public void testAddSwitch() {
        Option option = Option.builder("o").longOpt("option")
                .desc("An option with a switch")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetSwitch() {
        Option option = Option.builder("o").longOpt("option")
                .desc("An option with a switch")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, null));
        assertFalse(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = Option.builder("o").longOpt("option")
                .desc("An option")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
        assertFalse(commandLine.hasOption(new Option("other", "other", false, "Other option")));
    }

    @Test
    public void testToString() {
        Option option = Option.builder("o").longOpt("option")
                .desc("An option")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option,
                Arrays.asList("arg1", "arg2"));

        assertEquals("arg1 arg2", commandLine.toString());
    }

    @Test
    public void testAddProperty() {
        Option option = Option.builder("o")
                .longOpt("option")
                .desc("An option")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addProperty(option, "property", "value");

        Map<String, String> properties = commandLine.getProperties(option);
        assertEquals("value", properties.get("property"));
    }

    @Test
    public void testGetProperty() {
        Option option = Option.builder("o")
                .longOpt("option")
                .desc("An option")
                .build();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addProperty(option, "property", "defaultValue");
        commandLine.addProperty(option, "property", "value");

        assertEquals("value", commandLine.getProperty(option, "property"));
        assertEquals("defaultValue", commandLine.getProperty(option, "otherProperty"));
        assertEquals("value", commandLine.getProperty(option, "property", "defaultValue"));
    }
}