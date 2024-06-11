import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("o");
        cmd.addOption(option);
        List<Option> options = cmd.getOptions();
        assertTrue(options.contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("o");
        Object value = "value";
        cmd.addValue(option, value);
        List<Object> values = cmd.getValues(option);
        assertTrue(values.contains(value));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("o");
        List<Object> values = cmd.getValues(option);
        assertEquals(values.size(), 0);
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("o");
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add("default");
        cmd.setDefaultValues(option, defaultValues);
        List<Object> values = cmd.getValues(option);
        assertEquals(values.size(), 1);
        assertEquals(values.get(0), "default");
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("o");
        cmd.addSwitch(option, true);
        boolean switchValue = cmd.getSwitchValue(option);
        assertTrue(switchValue);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("o");
        cmd.setDefaultSwitch(option, true);
        boolean switchValue = cmd.getSwitchValue(option);
        assertTrue(switchValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        String property = "key";
        String value = "value";
        cmd.addProperty(property, value);
        String retrievedValue = cmd.getProperty(property);
        assertEquals(retrievedValue, value);
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        String argument = "-o";
        boolean result = cmd.looksLikeOption(argument);
        assertTrue(result);
    }
}