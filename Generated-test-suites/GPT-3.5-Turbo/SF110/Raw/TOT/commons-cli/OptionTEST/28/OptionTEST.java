import org.junit.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OptionTest {

    private Option option;

    @Before
    public void setUp() {
        option = new Option("t", "test", true, "Test option");
    }

    @Test
    public void testGetOpt() {
        assertEquals("t", option.getOpt());
    }

    @Test
    public void testGetLongOpt() {
        assertEquals("test", option.getLongOpt());
    }

    @Test
    public void testSetLongOpt() {
        option.setLongOpt("newlongopt");
        assertEquals("newlongopt", option.getLongOpt());
    }

    @Test
    public void testSetOptionalArg() {
        assertFalse(option.hasOptionalArg());
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArg() {
        assertFalse(option.hasOptionalArg());
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasLongOpt() {
        assertTrue(option.hasLongOpt());
    }

    @Test
    public void testHasArg() {
        assertTrue(option.hasArg());
    }

    @Test
    public void testGetDescription() {
        assertEquals("Test option", option.getDescription());
    }

    @Test
    public void testSetDescription() {
        option.setDescription("New description");
        assertEquals("New description", option.getDescription());
    }

    @Test
    public void testIsRequired() {
        assertFalse(option.isRequired());
    }

    @Test
    public void testSetRequired() {
        assertFalse(option.isRequired());
        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testSetArgName() {
        assertEquals("arg", option.getArgName());
        option.setArgName("newargname");
        assertEquals("newargname", option.getArgName());
    }

    @Test
    public void testHasArgName() {
        assertFalse(option.hasArgName());
        option.setArgName("argname");
        assertTrue(option.hasArgName());
    }

    @Test
    public void testHasArgs() {
        assertFalse(option.hasArgs());
        option.setArgs(2);
        assertTrue(option.hasArgs());
    }

    @Test
    public void testSetArgs() {
        assertEquals(-1, option.getArgs());
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }

    @Test
    public void testSetValueSeparator() {
        assertEquals('\0', option.getValueSeparator());
        option.setValueSeparator(';');
        assertEquals(';', option.getValueSeparator());
    }

    @Test
    public void testHasValueSeparator() {
        assertFalse(option.hasValueSeparator());
        option.setValueSeparator(':');
        assertTrue(option.hasValueSeparator());
    }

    @Test
    public void testGetArgs() {
        assertEquals(-1, option.getArgs());
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingNoArgsAllowed() {
        option.setArgs(Option.UNINITIALIZED);
        option.addValueForProcessing("value");
    }

    @Test
    public void testAddValueForProcessingNormal() {
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertEquals(Arrays.asList("value1", "value2"), option.getValuesList());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingListFull() {
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.addValueForProcessing("value3");
    }

    @Test
    public void testGetValue() {
        assertNull(option.getValue());
        option.setArgs(2);
        option.addValueForProcessing("value1");
        assertEquals("value1", option.getValue());
    }

    @Test
    public void testGetValueWithIndex() {
        assertNull(option.getValue(0));
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test
    public void testGetValueWithDefault() {
        assertEquals("default", option.getValue("default"));

        option.setArgs(2);
        option.addValueForProcessing("value1");
        assertEquals("value1", option.getValue("default"));
    }

    @Test
    public void testGetValues() {
        assertNull(option.getValues());
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertArrayEquals(new String[]{"value1", "value2"}, option.getValues());
    }

    @Test
    public void testGetValuesList() {
        assertEquals(new ArrayList<String>(), option.getValuesList());
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertEquals(Arrays.asList("value1", "value2"), option.getValuesList());
    }

    @Test
    public void testToString() {
        assertEquals("[ option: t test [ARG] :: Test option ]", option.toString());
    }

    @Test
    public void testEquals() {
        assertTrue(option.equals(option));
        assertFalse(option.equals(null));
        assertFalse(option.equals("string"));

        Option otherOption = new Option("t", "test", true, "Test option");
        Option differentOpt = new Option("d", "test", true, "Test option");
        Option differentLongOpt = new Option("t", "different", true, "Test option");

        assertTrue(option.equals(otherOption));
        assertFalse(option.equals(differentOpt));
        assertFalse(option.equals(differentLongOpt));
    }

    @Test
    public void testHashCode() {
        Option otherOption = new Option("t", "test", true, "Test option");
        assertEquals(option.hashCode(), otherOption.hashCode());

        Option differentOpt = new Option("d", "test", true, "Test option");
        assertNotEquals(option.hashCode(), differentOpt.hashCode());

        Option differentLongOpt = new Option("t", "different", true, "Test option");
        assertNotEquals(option.hashCode(), differentLongOpt.hashCode());
    }

    @Test
    public void testClone() {
        Option clonedOption = (Option) option.clone();
        assertEquals(option.getOpt(), clonedOption.getOpt());
        assertEquals(option.getLongOpt(), clonedOption.getLongOpt());
        assertEquals(option.hasOptionalArg(), clonedOption.hasOptionalArg());
        assertEquals(option.hasLongOpt(), clonedOption.hasLongOpt());
        assertEquals(option.hasArg(), clonedOption.hasArg());
        assertEquals(option.getDescription(), clonedOption.getDescription());
        assertEquals(option.isRequired(), clonedOption.isRequired());
        assertEquals(option.getArgName(), clonedOption.getArgName());
        assertEquals(option.hasArgName(), clonedOption.hasArgName());
        assertEquals(option.hasArgs(), clonedOption.hasArgs());
        assertEquals(option.getValueSeparator(), clonedOption.getValueSeparator());
        assertEquals(option.getArgs(), clonedOption.getArgs());
        assertEquals(option.getValuesList(), clonedOption.getValuesList());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        option.addValue("value");
    }

    @After
    public void tearDown() {
        option = null;
    }
}