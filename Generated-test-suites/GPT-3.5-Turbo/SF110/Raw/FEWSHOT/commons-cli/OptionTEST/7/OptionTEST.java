import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testOption_Constructor1() {
        Option option = new Option("opt1", "desc1");
        assertEquals("opt1", option.getOpt());
        assertEquals("desc1", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
    }

    @Test
    public void testOption_Constructor2() {
        Option option = new Option("opt2", true, "desc2");
        assertEquals("opt2", option.getOpt());
        assertEquals("desc2", option.getDescription());
        assertNull(option.getLongOpt());
        assertTrue(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
    }

    @Test
    public void testOption_Constructor3() {
        Option option = new Option("opt3", "longOpt3", true, "desc3");
        assertEquals("opt3", option.getOpt());
        assertEquals("longOpt3", option.getLongOpt());
        assertEquals("desc3", option.getDescription());
        assertFalse(option.hasOptionalArg());
        assertTrue(option.hasLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
    }

    @Test
    public void testOption_getId() {
        Option option = new Option("opt", "desc");
        assertEquals('o', option.getId());
    }

    @Test
    public void testOption_getKey() {
        Option option1 = new Option("opt", "desc");
        assertEquals("opt", option1.getKey());

        Option option2 = new Option(null, "longOpt", true, "desc");
        assertEquals("longOpt", option2.getKey());
    }

    @Test
    public void testOption_setType() {
        Option option = new Option("opt", "desc");
        assertNull(option.getType());

        option.setType(String.class);
        assertEquals(String.class, option.getType());
    }

    @Test
    public void testOption_setLongOpt() {
        Option option = new Option("opt", "desc");
        assertNull(option.getLongOpt());

        option.setLongOpt("longOpt");
        assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testOption_setOptionalArg() {
        Option option = new Option("opt", "desc");
        assertFalse(option.hasOptionalArg());

        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testOption_hasLongOpt() {
        Option option1 = new Option("opt", "desc");
        assertFalse(option1.hasLongOpt());

        Option option2 = new Option("opt", true, "desc");
        assertFalse(option2.hasLongOpt());

        Option option3 = new Option("opt", "longOpt", true, "desc");
        assertTrue(option3.hasLongOpt());
    }

    @Test
    public void testOption_hasArg() {
        Option option1 = new Option("opt", "desc");
        assertFalse(option1.hasArg());

        Option option2 = new Option("opt", true, "desc");
        assertTrue(option2.hasArg());

        Option option3 = new Option("opt", "longOpt", true, "desc");
        assertTrue(option3.hasArg());
    }

    @Test
    public void testOption_getDescription() {
        Option option = new Option("opt", "desc");
        assertEquals("desc", option.getDescription());

        option.setDescription("newDesc");
        assertEquals("newDesc", option.getDescription());
    }

    @Test
    public void testOption_isRequired() {
        Option option = new Option("opt", "desc");
        assertFalse(option.isRequired());

        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testOption_getArgName() {
        Option option = new Option("opt", "desc");
        assertFalse(option.hasArgName());

        option.setArgName("argName");
        assertTrue(option.hasArgName());
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testOption_hasArgs() {
        Option option1 = new Option("opt", "desc");
        assertFalse(option1.hasArgs());

        Option option2 = new Option("opt", true, "desc");
        assertFalse(option2.hasArgs());

        Option option3 = new Option("opt", "longOpt", true, "desc");
        assertFalse(option3.hasArgs());

        Option option4 = new Option("opt", "desc");
        option4.setArgs(3);
        assertTrue(option4.hasArgs());
    }

    @Test
    public void testOption_setArgs() {
        Option option = new Option("opt", "desc");
        assertEquals(-1, option.getArgs());

        option.setArgs(3);
        assertEquals(3, option.getArgs());
    }

    @Test
    public void testOption_hasValueSeparator() {
        Option option1 = new Option("opt", "desc");
        assertFalse(option1.hasValueSeparator());

        Option option2 = new Option("opt", "desc");
        option2.setValueSeparator(':');
        assertTrue(option2.hasValueSeparator());
    }

    @Test
    public void testOption_getArgs() {
        Option option = new Option("opt", "desc");
        assertEquals(-1, option.getArgs());

        option.setArgs(3);
        assertEquals(3, option.getArgs());
    }

    @Test
    public void testOption_clearValues() {
        Option option = new Option("opt", "desc");
        option.addValue("value1");
        option.addValue("value2");
        assertFalse(option.getValuesList().isEmpty());

        option.clearValues();
        assertTrue(option.getValuesList().isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOption_addValue() {
        Option option = new Option("opt", "desc");
        option.addValue("value");
    }

    // Additional tests for Option class can be added here
    
}