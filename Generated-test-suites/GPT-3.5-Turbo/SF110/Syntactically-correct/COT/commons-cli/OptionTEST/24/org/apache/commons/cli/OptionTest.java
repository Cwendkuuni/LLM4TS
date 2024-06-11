import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testConstructor1() {
        Option option = new Option("a", "option with description");
        assertNotNull(option);
        assertEquals("a", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("option with description", option.getDescription());
    }

    @Test
    public void testConstructor2() {
        Option option = new Option("b", true, "option with arg");
        assertNotNull(option);
        assertEquals("b", option.getOpt());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
        assertEquals("option with arg", option.getDescription());
    }

    @Test
    public void testConstructor3() {
        Option option = new Option("c", "long", true, "option with longOpt");
        assertNotNull(option);
        assertEquals("c", option.getOpt());
        assertEquals("long", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
        assertEquals("option with longOpt", option.getDescription());
    }

    @Test
    public void testGetId() {
        Option option = new Option("d", "option with id");
        assertEquals('d', option.getId());
    }

    @Test
    public void testGetKey() {
        Option option1 = new Option("e", "option with key");
        assertEquals("e", option1.getKey());

        Option option2 = new Option(null, "option with long key");
        assertEquals("option with long key", option2.getKey());
    }

    @Test
    public void testGetType() {
        Option option = new Option("f", "option with type");
        assertNull(option.getType());
    }

    @Test
    public void testSetType() {
        Option option = new Option("g", "option with type");
        option.setType(Integer.class);
        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testGetLongOpt() {
        Option option = new Option("h", "longOpt");
        assertNull(option.getLongOpt());

        Option option2 = new Option("i", "longOpt2");
        option2.setLongOpt("long");
        assertEquals("long", option2.getLongOpt());
    }

    @Test
    public void testSetOptionalArg() {
        Option option = new Option("j", "option with optional arg");
        assertFalse(option.hasOptionalArg());
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasLongOpt() {
        Option option = new Option("k", "option with longOpt");
        assertFalse(option.hasLongOpt());

        Option option2 = new Option("l", "option with longOpt");
        option2.setLongOpt("long");
        assertTrue(option2.hasLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = new Option("m", "option with arg");
        assertFalse(option.hasArg());

        Option option2 = new Option("n", true, "option with arg");
        assertTrue(option2.hasArg());

        Option option3 = new Option("o", "longOpt", true, "option with arg");
        assertTrue(option3.hasArg());
    }

    @Test
    public void testGetDescription() {
        Option option = new Option("p", "option with description");
        assertEquals("option with description", option.getDescription());
    }

    @Test
    public void testSetDescription() {
        Option option = new Option("q", "option with description");
        option.setDescription("new description");
        assertEquals("new description", option.getDescription());
    }

    @Test
    public void testIsRequired() {
        Option option = new Option("r", "required option");
        assertFalse(option.isRequired());
        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testSetArgName() {
        Option option = new Option("s", "option with argName");
        option.setArgName("arg");
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testHasArgName() {
        Option option = new Option("t", "option with argName");
        assertFalse(option.hasArgName());
        option.setArgName("arg");
        assertTrue(option.hasArgName());
    }

    @Test
    public void testHasArgs() {
        Option option = new Option("u", "option with args");
        assertFalse(option.hasArgs());
        option.setArgs(2);
        assertTrue(option.hasArgs());
    }

    @Test
    public void testSetArgs() {
        Option option = new Option("v", "option with args");
        option.setArgs(3);
        assertEquals(3, option.getArgs());
    }

    @Test
    public void testSetValueSeparator() {
        Option option = new Option("w", "option with value separator");
        option.setValueSeparator(',');
        assertEquals(',', option.getValueSeparator());
    }

    @Test
    public void testHasValueSeparator() {
        Option option = new Option("x", "option with value separator");
        assertFalse(option.hasValueSeparator());
        option.setValueSeparator(',');
        assertTrue(option.hasValueSeparator());
    }

    @Test
    public void testGetArgs() {
        Option option = new Option("y", "option with args");
        assertEquals(-1, option.getArgs());
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingNoArgs() {
        Option option = new Option("z", "option with no args");
        option.addValueForProcessing("value");
    }

    @Test
    public void testAddValueForProcessingWithSeparator() {
        Option option = new Option("zz", "option with separator");
        option.setValueSeparator(',');
        option.setArgs(3);

        option.addValueForProcessing("value1,value2,value3");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
        assertEquals("value3", option.getValue(2));
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingListFull() {
        Option option = new Option("zzz", "option with list full");
        option.setArgs(2);

        option.addValueForProcessing("value1,value2,value3");
    }

    @Test
    public void testAddValue() {
        Option option = new Option("zzzz", "option with value");
        assertThrows(UnsupportedOperationException.class, () -> {
            option.addValue("value");
        });
    }

    @Test
    public void testGetValue() {
        Option option = new Option("zzzzz", "option with value");
        assertNull(option.getValue());

        option.addValueForProcessing("value");
        assertEquals("value", option.getValue());
    }

    @Test
    public void testGetValueWithIndex() {
        Option option = new Option("zzzzzz", "option with values");
        assertNull(option.getValue(0));

        option.addValueForProcessing("value1,value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test
    public void testGetValueWithDefault() {
        Option option = new Option("zzzzzzz", "option with default value");
        assertEquals("default", option.getValue("default"));

        option.addValueForProcessing("value");
        assertEquals("value", option.getValue("default"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("zzzzzzzz", "option with values");
        assertNull(option.getValues());

        option.addValueForProcessing("value1,value2");
        assertArrayEquals(new String[]{"value1", "value2"}, option.getValues());
    }

    @Test
    public void testGetValuesList() {
        Option option = new Option("zzzzzzzzz", "option with values");
        assertNotNull(option.getValuesList());
        assertTrue(option.getValuesList().isEmpty());

        option.addValueForProcessing("value1,value2");
        assertEquals(2, option.getValuesList().size());
        assertEquals("value1", option.getValuesList().get(0));
        assertEquals("value2", option.getValuesList().get(1));
    }

    @Test
    public void testToString() {
        Option option = new Option("zzzzzzzzzz", "option with description");
        option.setLongOpt("longOpt");
        option.setArgs(2);
        option.setArgName("arg");
        option.setType(Integer.class);

        String expected = "[ option: zzzzzzzzzz [ARG ARG] :: option with description :: java.lang.Integer ]";
        assertEquals(expected, option.toString());
    }

    @Test
    public void testEquals() {
        Option option1 = new Option("eq", "option with id");
        Option option2 = new Option("eq", "option with id");
        Option option3 = new Option("eqq", "option with id");

        assertEquals(option1, option2);
        assertNotEquals(option1, option3);
    }

    @Test
    public void testHashCode() {
        Option option1 = new Option("hc", "option with hashCode");
        Option option2 = new Option("hc", "option with hashCode");
        Option option3 = new Option("hcc", "option with hashCode");

        assertEquals(option1.hashCode(), option2.hashCode());
        assertNotEquals(option1.hashCode(), option3.hashCode());
    }

    @Test
    public void testClone() {
        Option option1 = new Option("clone", "option with clone");
        option1.setArgName("arg");
        option1.setType(Integer.class);

        Option option2 = (Option) option1.clone();

        assertEquals(option1, option2);
        assertNotSame(option1, option2);
        assertEquals(option1.getArgName(), option2.getArgName());
        assertEquals(option1.getType(), option2.getType());
    }

    @Test
    public void testClearValues() {
        Option option = new Option("clear", "option with clear values");
        option.addValueForProcessing("value1,value2,value3");
        assertEquals(3, option.getValuesList().size());

        option.clearValues();
        assertTrue(option.getValuesList().isEmpty());
    }

    @Test
    public void testAddValueUnsupportedOperationException() {
        Option option = new Option("add", "option with unsupported addValue");
        assertThrows(UnsupportedOperationException.class, () -> {
            option.addValue("value");
        });
    }
}