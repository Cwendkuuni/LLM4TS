import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option1 = new Option("option1");
        Option option2 = new Option("option2");
        Argument argument = new Argument();

        commandLine = new WriteableCommandLineImpl(option1, Arrays.asList("arg1", "arg2"));
        commandLine.addOption(option2);
        commandLine.addOption(argument);
        commandLine.addValue(option1, "value1");
        commandLine.addSwitch(option2, true);
    }

    @Test
    public void testAddOption() {
        Option option3 = new Option("option3");
        assertFalse(commandLine.hasOption(option3));

        commandLine.addOption(option3);
        assertTrue(commandLine.hasOption(option3));
    }

    @Test
    public void testAddValue() {
        Option option1 = new Option("option1");
        Option option3 = new Option("option3");

        commandLine.addValue(option3, "value3");

        assertNull(commandLine.getValues(option1, Collections.emptyList()));
        assertEquals(Collections.singletonList("value3"), commandLine.getValues(option3, Collections.emptyList()));
    }

    @Test
    public void testAddSwitch() {
        Option option4 = new Option("option4");

        assertFalse(commandLine.getSwitch(option4, false));

        commandLine.addSwitch(option4, true);
        assertTrue(commandLine.getSwitch(option4, false));
    }

    @Test
    public void testHasOption() {
        Option option1 = new Option("option1");
        Option option3 = new Option("option3");

        assertTrue(commandLine.hasOption(option1));
        assertFalse(commandLine.hasOption(option3));
    }

    @Test
    public void testGetOption() {
        Option option1 = new Option("option1");
        Option option3 = new Option("option3");

        assertEquals(option1, commandLine.getOption("option1"));
        assertNull(commandLine.getOption("option3"));
    }

    @Test
    public void testGetValues() {
        Option option1 = new Option("option1");
        Option option3 = new Option("option3");

        assertEquals(Collections.singletonList("value1"), commandLine.getValues(option1, Collections.emptyList()));
        assertEquals(Collections.emptyList(), commandLine.getValues(option3, Collections.emptyList()));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option1 = new Option("option1");
        Option option3 = new Option("option3");

        assertEquals(Collections.singletonList("value1"), commandLine.getUndefaultedValues(option1));
        assertEquals(Collections.emptyList(), commandLine.getUndefaultedValues(option3));
    }

    @Test
    public void testGetSwitch() {
        Option option2 = new Option("option2");
        Option option4 = new Option("option4");

        assertTrue(commandLine.getSwitch(option2, false));
        assertFalse(commandLine.getSwitch(option4, false));
    }

    @Test
    public void testGetProperty() {
        assertEquals("", commandLine.getProperty("property"));

        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testAddProperty() {
        assertEquals("", commandLine.getProperty("property"));

        commandLine.addProperty("option", "property", "value");
        assertEquals("value", commandLine.getProperty("option", "property", ""));
    }

    @Test
    public void testGetProperty_DefaultValue() {
        assertEquals("default", commandLine.getProperty("property", "default"));
    }

    @Test
    public void testAddProperty_DefaultValue() {
        assertEquals("default", commandLine.getProperty("option", "property", "default"));
    }

    @Test
    public void testGetProperties() {
        assertEquals(Collections.emptySet(), commandLine.getProperties());

        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");

        assertEquals(Collections.singleton("property1"), commandLine.getProperties());
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option"));
        assertFalse(commandLine.looksLikeOption("arg"));
    }

    @Test
    public void testToString() {
        assertEquals("arg1 arg2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        assertEquals(Arrays.asList("option2", "argument"), commandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        assertEquals(Arrays.asList("option1", "option2", "argument"), commandLine.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues() {
        Option option1 = new Option("option1");
        Option option3 = new Option("option3");

        commandLine.setDefaultValues(option1, Collections.singletonList("default"));
        commandLine.setDefaultValues(option3, null);

        assertEquals(Collections.singletonList("default"), commandLine.getValues(option1, Collections.emptyList()));
        assertEquals(Collections.emptyList(), commandLine.getValues(option3, Collections.emptyList()));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option2 = new Option("option2");
        Option option4 = new Option("option4");

        commandLine.setDefaultSwitch(option2, false);
        commandLine.setDefaultSwitch(option4, null);

        assertFalse(commandLine.getSwitch(option2, false));
        assertFalse(commandLine.getSwitch(option4, false));
    }

    @Test
    public void testGetNormalised() {
        assertEquals(Arrays.asList("arg1", "arg2"), commandLine.getNormalised());
    }
}