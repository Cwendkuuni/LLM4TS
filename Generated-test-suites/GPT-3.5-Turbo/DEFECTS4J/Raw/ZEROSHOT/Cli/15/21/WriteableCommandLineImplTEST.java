import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Argument;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addOption(option);

        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addValue(option, "value");

        assertEquals("value", commandLine.getValues(option, new ArrayList()));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addOption(option);

        assertEquals(option, commandLine.getOption("test"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addValue(option, "value");

        assertEquals("value", commandLine.getValues(option, new ArrayList()));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addValue(option, "value");

        assertEquals("value", commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList());
        assertEquals("", commandLine.getProperty("property"));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList());
        commandLine.addProperty(new PropertyOption(), "property", "value");

        assertEquals("value", commandLine.getProperty(new PropertyOption(), "property", ""));
    }

    @Test
    public void testGetPropertyDefault() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList());
        commandLine.addProperty(new PropertyOption(), "property", "value");

        assertEquals("default", commandLine.getProperty(new PropertyOption(), "nonexisting", "default"));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList());
        commandLine.addProperty(new PropertyOption(), "property", "value");

        assertEquals(1, commandLine.getProperties().size());
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList());

        assertTrue(commandLine.looksLikeOption("-test"));
        assertFalse(commandLine.looksLikeOption("test"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList());
        commandLine.addValue(new Argument(), "value");

        assertEquals("value", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList());
        commandLine.addOption(new Option("test2"));

        assertEquals(2, commandLine.getOptions().size());
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList());
        commandLine.addOption(new Option("test2"));

        assertEquals(2, commandLine.getOptionTriggers().size());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.setDefaultValues(option, new ArrayList());

        assertTrue(commandLine.getDefaultValues(option).isEmpty());
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getDefaultSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("test"), new ArrayList());
        commandLine.addValue(new Argument(), "value");
        List<String> normalised = commandLine.getNormalised();

        assertEquals("value", normalised.get(0));
    }

}