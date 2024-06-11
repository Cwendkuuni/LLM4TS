import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;

public class WriteableCommandLineTest {

    WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl();
    }

    @Test
    public void testAddOption() {
        Option option = new Option("test");
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test");
        Object value = "value";
        commandLine.addValue(option, value);
        assertTrue(commandLine.getValues(option).contains(value));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("test");
        Object value = "value";
        commandLine.addValue(option, value);
        List values = commandLine.getUndefaultedValues(option);
        assertTrue(values.contains(value));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test");
        List defaultValues = Arrays.asList("default");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(commandLine.getDefaultValues(option), defaultValues);
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitchValue(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test");
        commandLine.setDefaultSwitch(option, false);
        assertFalse(commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("test");
        String property = "key";
        String value = "value";
        commandLine.addProperty(option, property, value);
        assertEquals(commandLine.getProperty(option, property), value);
    }

    @Test
    public void testAddDefaultProperty() {
        String property = "key";
        String value = "value";
        commandLine.addProperty(property, value);
        assertEquals(commandLine.getDefaultProperty(property), value);
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-test"));
        assertFalse(commandLine.looksLikeOption("test"));
    }

}