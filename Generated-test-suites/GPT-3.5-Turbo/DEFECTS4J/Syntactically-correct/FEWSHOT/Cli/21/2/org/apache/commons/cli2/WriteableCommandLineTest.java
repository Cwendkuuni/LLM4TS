import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("foo", "Foo option", false);
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("foo"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("foo", "Foo option", true);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals(2, commandLine.getValues(option).size());
        assertEquals("value1", commandLine.getValues(option).get(0));
        assertEquals("value2", commandLine.getValues(option).get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("foo", "Foo option", true);
        commandLine.addValue(option, "value1");
        commandLine.addDefault(option, "value2");
        assertEquals(1, commandLine.getUndefaultedValues(option).size());
        assertEquals("value1", commandLine.getUndefaultedValues(option).get(0));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("foo", "Foo option", true);
        commandLine.setDefaultValues(option, Arrays.asList("default1", "default2"));
        assertEquals(2, commandLine.getValues(option).size());
        assertEquals("default1", commandLine.getValues(option).get(0));
        assertEquals("default2", commandLine.getValues(option).get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("foo", "Foo option", false);
        commandLine.addSwitch(option, true);
        assertEquals(true, commandLine.getSwitch(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("foo", "Foo option", false);
        commandLine.setDefaultSwitch(option, true);
        assertEquals(true, commandLine.getSwitch(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("foo", "Foo option", true);
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        assertEquals("value1", commandLine.getProperty(option, "property1"));
        assertEquals("value2", commandLine.getProperty(option, "property2"));
    }

    @Test
    public void testAddDefaultProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        commandLine.addDefaultProperty("property1", "value1");
        commandLine.addDefaultProperty("property2", "value2");
        assertEquals("value1", commandLine.getDefaultProperty("property1"));
        assertEquals("value2", commandLine.getDefaultProperty("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        assertEquals(true, commandLine.looksLikeOption("-foo"));
        assertEquals(true, commandLine.looksLikeOption("--foo"));
        assertEquals(false, commandLine.looksLikeOption("foo"));
    }
}