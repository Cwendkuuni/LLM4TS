import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option option1;
    private Option option2;

    @Before
    public void setup() {
        option1 = new Option("o1", "option1", false, "Option 1");
        option2 = new Option("o2", "option2", true, "Option 2");
        commandLine = new WriteableCommandLineImpl(option1, null);
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option1);
        assertTrue(commandLine.hasOption(option1));
        assertFalse(commandLine.hasOption(option2));
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        assertEquals(2, commandLine.getValues(option1, null).size());
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option1, true);
        commandLine.addSwitch(option2, false);
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option1, null));
        assertEquals(Boolean.FALSE, commandLine.getSwitch(option2, null));
    }

    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption(option2));
        commandLine.addOption(option2);
        assertTrue(commandLine.hasOption(option2));
    }

    @Test
    public void testGetOption() {
        commandLine.addOption(option1);
        assertEquals(option1, commandLine.getOption("o1"));
        assertNull(commandLine.getOption("o3"));
    }

    @Test
    public void testGetValues() {
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        assertEquals(2, commandLine.getValues(option1, null).size());
        assertNull(commandLine.getValues(option2, null));
    }

    @Test
    public void testGetUndefaultedValues() {
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        assertEquals(2, commandLine.getUndefaultedValues(option1).size());
        assertEquals(0, commandLine.getUndefaultedValues(option2).size());
    }

    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1, null));
        assertNull(commandLine.getSwitch(option2, null));
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty(option1, "property1", "value1");
        assertEquals("value1", commandLine.getProperty(option1, "property1", null));
        assertNull(commandLine.getProperty(option2, "property1", null));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty(option1, "property1", "value1");
        commandLine.addProperty(option1, "property2", "value2");
        assertEquals("value1", commandLine.getProperty(option1, "property1", null));
        assertEquals("value2", commandLine.getProperty(option1, "property2", null));
        assertNull(commandLine.getProperty(option1, "property3", null));
    }

    @Test
    public void testGetProperties() {
        commandLine.addProperty(option1, "property1", "value1");
        commandLine.addProperty(option1, "property2", "value2");
        assertEquals(2, commandLine.getProperties(option1).size());
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-o1"));
        assertFalse(commandLine.looksLikeOption("value1"));
    }

    @Test
    public void testToString() {
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option2, "value2");
        assertEquals("value1 value2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertEquals(2, commandLine.getOptions().size());
    }

    @Test
    public void testGetOptionTriggers() {
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertEquals(2, commandLine.getOptionTriggers().size());
    }

    @Test
    public void testSetDefaultValues() {
        commandLine.setDefaultValues(option1, Collections.singletonList("default1"));
        assertEquals(Collections.singletonList("default1"), commandLine.getValues(option1, null));
        assertNull(commandLine.getValues(option2, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option1, true);
        assertTrue(commandLine.getSwitch(option1, null));
        assertNull(commandLine.getSwitch(option2, null));
    }

    @Test
    public void testGetNormalised() {
        commandLine.addValue(option1, "value1");
        assertEquals(Collections.singletonList("value1"), commandLine.getNormalised());
    }
}