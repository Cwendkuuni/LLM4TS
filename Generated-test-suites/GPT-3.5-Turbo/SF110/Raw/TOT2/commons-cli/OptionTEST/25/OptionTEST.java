import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testOptionConstructorWithDescription() {
        Option option = new Option("o", "Description");
        assertEquals("o", option.getOpt());
        assertEquals("Description", option.getDescription());
    }

    @Test
    public void testOptionConstructorWithHasArgAndDescription() {
        Option option = new Option("o", true, "Description");
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testOptionConstructorWithLongOpt() {
        Option option = new Option("o", "option", true, "Description");
        assertEquals("option", option.getLongOpt());
    }

    @Test
    public void testAddValueForProcessingNoArgsAllowed() {
        Option option = new Option("o", "Description");
        try {
            option.addValueForProcessing("value");
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("NO_ARGS_ALLOWED", e.getMessage());
        }
    }

    @Test
    public void testAddValueForProcessingWithSeparator() {
        Option option = new Option("o", "Description");
        option.setValueSeparator('|');
        option.addValueForProcessing("value1|value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test
    public void testAddValueForProcessingListFull() {
        Option option = new Option("o", "Description");
        option.setArgs(1);
        option.addValueForProcessing("value1");
        try {
            option.addValueForProcessing("value2");
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("Cannot add value, list full.", e.getMessage());
        }
    }

    @Test
    public void testGetValueWithDefaultValue() {
        Option option = new Option("o", "Description");
        assertEquals("default", option.getValue("default"));
    }

    @Test
    public void testEqualsWithSameOption() {
        Option option1 = new Option("o", "Description");
        Option option2 = new Option("o", "Description");
        assertTrue(option1.equals(option2));
    }

    @Test
    public void testEqualsWithDifferentOptions() {
        Option option1 = new Option("o", "Description");
        Option option2 = new Option("x", "Description");
        assertFalse(option1.equals(option2));
    }

    @Test
    public void testHashCode() {
        Option option = new Option("o", "Description");
        assertEquals(option.getOpt().hashCode(), option.hashCode());
    }

    @Test
    public void testClone() {
        Option option1 = new Option("o", "Description");
        Option option2 = (Option) option1.clone();
        assertNotSame(option1, option2);
        assertEquals(option1.getOpt(), option2.getOpt());
    }

    @Test
    public void testAddValueUnsupportedOperationException() {
        Option option = new Option("o", "Description");
        try {
            option.addValue("value");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("The addValue method is not intended for client use. Subclasses should use the addValueForProcessing method instead. ", e.getMessage());
        }
    }
}