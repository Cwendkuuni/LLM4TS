import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option1 = new Option.Builder("o1").withDescription("Option 1").create();
        Option option2 = new Option.Builder("o2").withDescription("Option 2").create();
        Argument argument1 = new ArgumentBuilder("arg1").withDescription("Argument 1").create();

        commandLine = new WriteableCommandLineImpl(option1, Arrays.asList(argument1));
    }

    @Test
    public void testAddOption() {
        Option option = new Option.Builder("o3").withDescription("Option 3").create();
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option.Builder("o1").withDescription("Option 1").create();
        commandLine.addValue(option, "value");

        assertEquals("value", commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option.Builder("o1").withDescription("Option 1").create();
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = new Option.Builder("o1").withDescription("Option 1").create();
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
        assertFalse(commandLine.hasOption(new Option.Builder("o2").withDescription("Option 2").create()));
    }

    @Test
    public void testGetOption() {
        Option option = new Option.Builder("o1").withDescription("Option 1").create();
        commandLine.addOption(option);

        assertEquals(option, commandLine.getOption("o1"));
        assertNull(commandLine.getOption("o3"));
    }

    @Test
    public void testGetValues() {
        Option option1 = new Option.Builder("o1").withDescription("Option 1").create();
        Option option2 = new Option.Builder("o2").withDescription("Option 2").create();
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        commandLine.setDefaultValues(option2, Arrays.asList("default1", "default2"));

        assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option1, null));
        assertEquals(Arrays.asList("default1", "default2"), commandLine.getValues(option2, null));
        assertEquals(Arrays.asList("default1", "default2"), commandLine.getValues(option2, Arrays.asList("default3")));
    }

    @Test
    public void testGetSwitch() {
        Option option1 = new Option.Builder("o1").withDescription("Option 1").create();
        Option option2 = new Option.Builder("o2").withDescription("Option 2").create();
        commandLine.addSwitch(option1, true);
        commandLine.setDefaultSwitch(option2, false);

        assertTrue(commandLine.getSwitch(option1, false));
        assertFalse(commandLine.getSwitch(option2, true));
        assertNull(commandLine.getSwitch(new Option.Builder("o3").withDescription("Option 3").create(), null));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");

        assertEquals("value1", commandLine.getProperty("property1", null));
        assertEquals("value2", commandLine.getProperty("property2", null));
        assertEquals("default", commandLine.getProperty("property3", "default"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-o1"));
        assertTrue(commandLine.looksLikeOption("--o1"));
        assertTrue(commandLine.looksLikeOption("/o1"));
        assertFalse(commandLine.looksLikeOption("arg1"));
    }

    @Test
    public void testToString() {
        commandLine.addValue(new ArgumentBuilder("arg1").withDescription("Argument 1").create(), "value1");
        commandLine.addValue(new ArgumentBuilder("arg2").withDescription("Argument 2").create(), "value2");

        assertEquals("value1 value2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option.Builder("o1").withDescription("Option 1").create();
        Option option2 = new Option.Builder("o2").withDescription("Option 2").create();
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertEquals(Arrays.asList(option1, option2), commandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option.Builder("o1").withDescription("Option 1").create();
        Option option2 = new Option.Builder("o2").withDescription("Option 2").create();
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertEquals(Arrays.asSet("o1", "o2"), commandLine.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option.Builder("o1").withDescription("Option 1").create();
        commandLine.setDefaultValues(option, Arrays.asList("default1", "default2"));

        assertEquals(Arrays.asList("default1", "default2"), commandLine.getValues(option, null));

        commandLine.setDefaultValues(option, null);

        assertNull(commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option.Builder("o1").withDescription("Option 1").create();
        commandLine.setDefaultSwitch(option, false);

        assertFalse(commandLine.getSwitch(option, true));

        commandLine.setDefaultSwitch(option, null);

        assertNull(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        Argument argument1 = new ArgumentBuilder("arg1").withDescription("Argument 1").create();
        Argument argument2 = new ArgumentBuilder("arg2").withDescription("Argument 2").create();
        commandLine.addValue(argument1, "value1");
        commandLine.addValue(argument2, "value2");

        assertEquals(Arrays.asList("value1", "value2"), commandLine.getNormalised());
    }
}