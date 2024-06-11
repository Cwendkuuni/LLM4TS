import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WriteableCommandLineImplTest {

    WriteableCommandLineImpl cmdLine;

    @Before
    public void setUp() {
        Option rootOption = new Option();
        List arguments = new ArrayList<>();
        cmdLine = new WriteableCommandLineImpl(rootOption, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Option();
        cmdLine.addOption(option);
        assertTrue(cmdLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option();
        Object value = "value";
        cmdLine.addValue(option, value);
        assertEquals(value, cmdLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option();
        boolean value = true;
        cmdLine.addSwitch(option, value);
        assertEquals(value, cmdLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        String property = "key";
        String value = "value";
        cmdLine.addProperty(property, value);
        assertEquals(value, cmdLine.getProperty(property));
    }

    @Test
    public void testGetOption() {
        Option option = new Option();
        cmdLine.addOption(option);
        assertEquals(option, cmdLine.getOption(option.getPreferredName()));
    }

    @Test
    public void testLooksLikeOption() {
        String option = "-o";
        assertTrue(cmdLine.looksLikeOption(option));
    }

    // Add additional test cases for edge cases and exception handling

}