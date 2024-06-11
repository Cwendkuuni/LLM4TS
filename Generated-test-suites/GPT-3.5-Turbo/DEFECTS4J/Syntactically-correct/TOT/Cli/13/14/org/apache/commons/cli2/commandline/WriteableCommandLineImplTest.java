import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import org.apache.commons.cli2.Option;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option1 = new Option("op1");
        Option option2 = new Option("op2");

        commandLine = new WriteableCommandLineImpl(option1, Collections.emptyList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
    }

    @Test
    public void testAddOption() {
        assertTrue(commandLine.hasOption("op1"));
        assertTrue(commandLine.hasOption("op2"));
        assertFalse(commandLine.hasOption("op3"));
    }

    @Test
    public void testAddValue() {
        Option option3 = new Option("op3");
        commandLine.addValue(option3, "value");

        assertEquals("value", commandLine.getValues(option3, Collections.emptyList()).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option4 = new Option("op4");
        commandLine.addSwitch(option4, true);

        assertTrue(commandLine.getSwitch(option4, false));
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty("key", "value");

        assertEquals("value", commandLine.getProperty("key", "defaultValue"));
        assertEquals("defaultValue", commandLine.getProperty("notExist", "defaultValue"));
    }

}