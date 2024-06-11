import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testGetOpt() {
        Option option = new Option("o", "Option for testing");
        assertEquals("o", option.getOpt());
    }

    @Test
    public void testSetLongOpt() {
        Option option = new Option("o", "Option for testing");
        option.setLongOpt("longOption");
        assertEquals("longOption", option.getLongOpt());
    }

    @Test
    public void testSetRequired() {
        Option option = new Option("o", "Option for testing");
        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testAddValueForProcessing() {
        Option option = new Option("o", "Option for testing");
        option.setArgs(2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");

        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessing_ThrowsException() {
        Option option = new Option("o", "Option for testing");
        option.setArgs(-1);
        option.addValueForProcessing("value");
    }

    @Test
    public void testClone() {
        Option option = new Option("o", "Option for testing");
        Option clonedOption = (Option) option.clone();
        assertNotSame(option, clonedOption);
        assertEquals(option.getOpt(), clonedOption.getOpt());
    }

    @Test
    public void testEquals() {
        Option option1 = new Option("o", "Option for testing");
        Option option2 = new Option("o", "Another option for testing");
        assertTrue(option1.equals(option2));
    }

    @Test
    public void testHashCode() {
        Option option = new Option("o", "Option for testing");
        Option sameOption = new Option("o", "Another option for testing");
        assertEquals(option.hashCode(), sameOption.hashCode());
    }

}