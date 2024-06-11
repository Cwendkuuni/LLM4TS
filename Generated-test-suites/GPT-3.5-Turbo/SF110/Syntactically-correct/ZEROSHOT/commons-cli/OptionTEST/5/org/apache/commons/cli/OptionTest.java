import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class OptionTest {

    @Test
    public void testOptionConstructorWithDescription() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertTrue(option.hasArg());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
    }

    @Test
    public void testOptionConstructorWithHasArg() {
        Option option = new Option("opt", true, "description");
        assertEquals("opt", option.getOpt());
        assertNull(option.getLongOpt());
        assertTrue(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertTrue(option.hasArg());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
    }

    @Test
    public void testOptionConstructorWithLongOpt() {
        Option option = new Option("opt", "longOpt", true, "description");
        assertEquals("opt", option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertTrue(option.hasOptionalArg());
        assertTrue(option.hasLongOpt());
        assertTrue(option.hasArg());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
    }

    @Test
    public void testOptionGetId() {
        Option option = new Option("opt", "description");
        assertEquals('o', option.getId());
    }

    @Test
    public void testOptionGetKey() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getKey());
    }

    @Test
    public void testOptionGetKeyWithLongOpt() {
        Option option = new Option(null, "longOpt", true, "description");
        assertEquals("longOpt", option.getKey());
    }

    @Test
    public void testOptionGetOpt() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());
    }

    @Test
    public void testOptionGetType() {
        Option option = new Option("opt", "description");
        assertNull(option.getType());
    }

    @Test
    public void testOptionSetType() {
        Option option = new Option("opt", "description");
        option.setType(new Object());
        assertNotNull(option.getType());
    }

    @Test
    public void testOptionGetLongOpt() {
        Option option = new Option("opt", "longOpt", true, "description");
        assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testOptionSetLongOpt() {
        Option option = new Option("opt", "description");
        option.setLongOpt("longOpt");
        assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testOptionSetOptionalArg() {
        Option option = new Option("opt", "description");
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testOptionHasOptionalArg() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasOptionalArg());
    }

    @Test
    public void testOptionHasLongOpt() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasLongOpt());
    }

    @Test
    public void testOptionHasArg() {
        Option option = new Option("opt", "description");
        assertTrue(option.hasArg());
    }

    @Test
    public void testOptionGetDescription() {
        Option option = new Option("opt", "description");
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testOptionSetDescription() {
        Option option = new Option("opt", "description");
        option.setDescription("newDescription");
        assertEquals("newDescription", option.getDescription());
    }

    @Test
    public void testOptionIsRequired() {
        Option option = new Option("opt", "description");
        assertFalse(option.isRequired());
    }

    @Test
    public void testOptionSetRequired() {
        Option option = new Option("opt", "description");
        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testOptionSetArgName() {
        Option option = new Option("opt", "description");
        option.setArgName("argName");
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testOptionGetArgName() {
        Option option = new Option("opt", "description");
        assertNull(option.getArgName());
    }

    @Test
    public void testOptionHasArgName() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasArgName());
    }

    @Test
    public void testOptionHasArgs() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasArgs());
    }

    @Test
    public void testOptionSetArgs() {
        Option option = new Option("opt", "description");
        option.setArgs(2);
        assertTrue(option.hasArgs());
    }

    @Test
    public void testOptionSetValueSeparator() {
        Option option = new Option("opt", "description");
        option.setValueSeparator(';');
        assertTrue(option.hasValueSeparator());
    }

    @Test
    public void testOptionGetValueSeparator() {
        Option option = new Option("opt", "description");
        assertEquals('\0', option.getValueSeparator());
    }

    @Test
    public void testOptionHasValueSeparator() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasValueSeparator());
    }

    @Test
    public void testOptionGetArgs() {
        Option option = new Option("opt", "description");
        assertEquals(-1, option.getArgs());
    }

    @Test(expected = RuntimeException.class)
    public void testOptionAddValueForProcessingNoArgs() {
        Option option = new Option("opt", "description");
        option.addValueForProcessing("value");
    }

    @Test
    public void testOptionProcessValue() {
        Option option = new Option("opt", "description");
        option.setArgs(2);
        option.processValue("value1,value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test(expected = RuntimeException.class)
    public void testOptionAddValueListFull() {
        Option option = new Option("opt", "description");
        option.setArgs(2);
        option.addValue("value1");
        option.addValue("value2");
        option.addValue("value3");
    }

    @Test
    public void testOptionGetValue() {
        Option option = new Option("opt", "description");
        assertNull(option.getValue());
    }

    @Test
    public void testOptionGetValueWithIndex() {
        Option option = new Option("opt", "description");
        assertNull(option.getValue(0));
    }

    @Test
    public void testOptionGetValueWithDefault() {
        Option option = new Option("opt", "description");
        assertNull(option.getValue("default"));
    }

    @Test
    public void testOptionGetValues() {
        Option option = new Option("opt", "description");
        assertNull(option.getValues());
    }

    @Test
    public void testOptionGetValuesList() {
        Option option = new Option("opt", "description");
        assertTrue(option.getValuesList().isEmpty());
    }

    @Test
    public void testOptionToString() {
        Option option = new Option("opt", "description");
        assertEquals("[ option: opt  [ARG] :: description ]", option.toString());
    }

    @Test
    public void testOptionHasNoValues() {
        Option option = new Option("opt", "description");
        assertTrue(option.hasNoValues());
    }

    @Test
    public void testOptionEqualsWithSameObject() {
        Option option = new Option("opt", "description");
        assertTrue(option.equals(option));
    }

    @Test
    public void testOptionEqualsWithNull() {
        Option option = new Option("opt", "description");
        assertFalse(option.equals(null));
    }

    @Test
    public void testOptionEqualsWithDifferentClass() {
        Option option = new Option("opt", "description");
        assertFalse(option.equals(new Object()));
    }

    @Test
    public void testOptionEqualsWithDifferentOpt() {
        Option option1 = new Option("opt", "description1");
        Option option2 = new Option("opt2", "description2");
        assertFalse(option1.equals(option2));
    }

    @Test
    public void testOptionEqualsWithDifferentLongOpt() {
        Option option1 = new Option("opt", "longOpt1", true, "description1");
        Option option2 = new Option("opt", "longOpt2", true, "description2");
        assertFalse(option1.equals(option2));
    }

    @Test
    public void testOptionEqualsWithEqualOptions() {
        Option option1 = new Option("opt", "description");
        Option option2 = new Option("opt", "description");
        assertTrue(option1.equals(option2));
    }

    @Test
    public void testOptionHashCode() {
        Option option = new Option("opt", "description");
        assertEquals(111174, option.hashCode());
    }

    @Test
    public void testOptionClone() {
        Option option = new Option("opt", "description");
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
        assertEquals(option.getArgs(), clonedOption.getArgs());
        assertEquals(option.getValueSeparator(), clonedOption.getValueSeparator());
        assertEquals(option.hasValueSeparator(), clonedOption.hasValueSeparator());
        assertEquals(option.getValuesList(), clonedOption.getValuesList());
    }

    @Test
    public void testOptionClearValues() {
        Option option = new Option("opt", "description");
        option.addValue("value");
        option.addValue("value");
        option.clearValues();
        assertTrue(option.getValuesList().isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOptionAddValue() {
        Option option = new Option("opt", "description");
        option.addValue("value");
    }
}