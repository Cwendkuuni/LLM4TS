import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option option1;
    private Option option2;

    @Before
    public void setUp() {
        option1 = new Option("a", "apple", false, "");
        option2 = new Option("b", "banana", false, "");

        List arguments = new ArrayList();
        arguments.add("arg1");
        arguments.add("arg2");

        commandLine = new WriteableCommandLineImpl(option1, arguments);
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option2);
        assertTrue(commandLine.hasOption(option2));
        assertNotNull(commandLine.getOption("b"));
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(option1, "value");
        List values = commandLine.getValues(option1, null);
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    // Add more test cases for other methods

}