import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Assert;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl cmd;

    @Before
    public void setup() {
        Option option = new Argument();
        List arguments = new ArrayList();
        cmd = new WriteableCommandLineImpl(option, arguments);
    }

    @Test
    public void testAddOption() {
        Option option = new Argument();
        cmd.addOption(option);
        assertTrue(cmd.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Argument();
        Object value = "test";
        cmd.addValue(option, value);
        assertTrue(cmd.getValues(option, null).contains(value));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Argument();
        boolean value = true;
        cmd.addSwitch(option, value);
        assertTrue(cmd.getSwitch(option, null));
    }

    @Test
    public void testGetOption() {
        Option option = new Argument();
        String trigger = "test";
        cmd.addOption(option);
        assertEquals(option, cmd.getOption(trigger));
    }

    @Test
    public void testGetProperty() {
        String property = "test";
        String value = "value";
        cmd.addProperty(property, value);
        assertEquals(value, cmd.getProperty(property, null));
    }

    @Test
    public void testLooksLikeOption() {
        String trigger = "--test";
        assertTrue(cmd.looksLikeOption(trigger));
    }

    @Test
    public void testToString() {
        List arguments = new ArrayList();
        arguments.add("test");
        cmd = new WriteableCommandLineImpl(new Argument(), arguments);
        assertEquals("test", cmd.toString());
    }

    @Test
    public void testGetProperties() {
        String property = "test";
        cmd.addProperty(property, "value");
        Set properties = cmd.getProperties();
        assertTrue(properties.contains(property));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new Argument();
        String trigger = "test";
        cmd.addOption(option);
        Set triggers = cmd.getOptionTriggers();
        assertTrue(triggers.contains(trigger));
    }
}