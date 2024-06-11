import org.apache.commons.cli2.*;
import org.junit.*;
import java.util.List;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Argument();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, null);
        cmd.addOption(option);

        assertTrue(cmd.hasOption(option));
        assertEquals(option, cmd.getOption(option.getPreferredName()));
        assertTrue(cmd.getOptions().contains(option));
        assertTrue(cmd.getOptionTriggers().contains(option.getPreferredName()));
    }

    @Test
    public void testAddValue() {
        Option option = new Argument();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, null);
        cmd.addOption(option);

        String value1 = "value1";
        String value2 = "value2";
        cmd.addValue(option, value1);
        cmd.addValue(option, value2);

        List values = cmd.getValues(option, null);
        assertEquals(2, values.size());
        assertTrue(values.contains(value1));
        assertTrue(values.contains(value2));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Switch("switch");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, null);
        cmd.addOption(option);

        cmd.addSwitch(option, true);
        assertTrue(cmd.getSwitch(option, null));

        cmd.addSwitch(option, false);
        assertFalse(cmd.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        Option option = new PropertyOption();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, null);
        cmd.addProperty(option, "key", "value");

        String propertyValue = cmd.getProperty(option, "key", null);
        assertEquals("value", propertyValue);
    }

    @Test
    public void testLooksLikeOption() {
        Option option = new Argument();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, null);

        assertTrue(cmd.looksLikeOption("-option"));
        assertFalse(cmd.looksLikeOption("arg"));
    }

    @Test
    public void testToString() {
        Option option = new Argument();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, null);

        cmd.addOption(option);
        cmd.addValue(option, "value");

        String expectedOutput = "value";
        assertEquals(expectedOutput, cmd.toString());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Argument();
        Option option2 = new Switch("switch");
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option1, null);
        cmd.addOption(option1);
        cmd.addOption(option2);

        assertTrue(cmd.getOptionTriggers().contains(option1.getPreferredName()));
        assertTrue(cmd.getOptionTriggers().contains(option2.getPreferredName()));
    }
}