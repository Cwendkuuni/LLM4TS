import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
    private Option option1;
    private Option option2;
    private Option option3;
    private Argument argument1;
    private Argument argument2;
    private List arguments;

    @Before
    public void setUp() {
        option1 = new OptionBuilder().withLongName("option1").create();
        option2 = new OptionBuilder().withLongName("option2").create();
        option3 = new OptionBuilder().withLongName("option3").create();
        argument1 = new ArgumentBuilder().withName("argument1").create();
        argument2 = new ArgumentBuilder().withName("argument2").create();
        arguments = Arrays.asList("argument1", "argument2");
    }

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);

        commandLine.addOption(option1);

        assertTrue(commandLine.getOptions().contains(option1));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, null);

        commandLine.addValue(option1, "value1");

        assertEquals(Collections.singletonList("value1"), commandLine.getValues(option1, null));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, null);

        commandLine.addSwitch(option1, true);

        assertTrue(commandLine.getSwitch(option1, false));
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option1);

        assertTrue(commandLine.hasOption(option1));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option1);

        assertEquals(option1, commandLine.getOption("option1"));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option1);
        commandLine.addValue(option1, "value1");

        assertEquals(Collections.singletonList("value1"), commandLine.getValues(option1, null));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, arguments);
        commandLine.addValue(option1, "value1");

        assertEquals(Collections.singletonList("value1"), commandLine.getUndefaultedValues(option1));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option1);
        commandLine.addSwitch(option1, true);

        assertTrue(commandLine.getSwitch(option1, false));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addProperty(option1, "property1", "value1");

        assertEquals("value1", commandLine.getProperty(option1, "property1", null));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, null);
        commandLine.addProperty("property1", "value1");

        assertEquals("value1", commandLine.getProperty(option1, "property1", null));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addProperty(option1, "property1", "value1");

        assertEquals(Collections.singleton("property1"), commandLine.getProperties(option1));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, arguments);

        assertEquals("argument1 argument2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertEquals(Arrays.asList(option1, option2), commandLine.getOptions());
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertEquals(Collections.unmodifiableSet(new HashSet<>(){
            {
                add("option1");
                add("option2");
            }
        }), commandLine.getOptionTriggers());
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, null);
        List defaults = Collections.singletonList("default1");

        commandLine.setDefaultValues(option1, defaults);

        assertEquals(defaults, commandLine.getValues(option1, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, null);
        
        commandLine.setDefaultSwitch(option1, true);

        assertTrue(commandLine.getSwitch(option1, false));
    }

    @Test
    public void testGetProperties1() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.addProperty(option1, "property1", "value1");

        assertEquals(Collections.singleton("property1"), commandLine.getProperties());
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);

        assertTrue(commandLine.looksLikeOption("--option1"));
    }
}