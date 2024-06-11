import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList());
        cmdLine.addOption(new Option("option2", "o2", true));

        assertEquals(2, cmdLine.getOptions().size());
        Option option1 = cmdLine.getOption("option1");
        Option option2 = cmdLine.getOption("option2");
        assertTrue(option1.isRequired());
        assertFalse(option2.isRequired());
        assertTrue(option2.hasArg());
        assertEquals("o2", option2.getLongOpt());
        assertEquals("o", option2.getOpt());
        assertEquals("option2", option2.getShortName());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList());
        cmdLine.addOption(new Option("option1", "o1"));
        cmdLine.addOption(new Option("option1", "o1"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("option", "o"), new ArrayList());
        cmdLine.addOption(new Option("option", "o", true, "value1", "v1"));
        cmdLine.addValue(cmdLine.getOption("option"), "value2");

        assertEquals(1, cmdLine.getOptions().size());
        Option option = cmdLine.getOption("option");
        assertEquals("value1", option.getDefaultValue());
        assertEquals(2, option.getValues().size());
        assertEquals("value1", option.getValues().get(0));
        assertEquals("value2", option.getValues().get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("option", "o"), new ArrayList());
        cmdLine.addOption(new Option("option", "o", false, null, 's'));
        cmdLine.addSwitch(cmdLine.getOption("option"), true);

        assertEquals(1, cmdLine.getOptions().size());
        Option option = cmdLine.getOption("option");
        assertEquals(Boolean.FALSE, option.getDefaultValue());
        assertEquals(Boolean.TRUE, cmdLine.getSwitch(option, Boolean.FALSE));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("option", "o"), new ArrayList());
        cmdLine.addOption(new Option("option", "o", false, null, 's'));
        cmdLine.addSwitch(cmdLine.getOption("option"), true);
        cmdLine.addSwitch(cmdLine.getOption("option"), true);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("option1"), new ArrayList());
        cmdLine.addOption(new Option("option2", "o2", true));

        assertTrue(cmdLine.hasOption(cmdLine.getOption("option1")));
        assertTrue(cmdLine.hasOption(cmdLine.getOption("option2")));
        assertFalse(cmdLine.hasOption(new Option("unknownOption")));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
        cmdLine.addOption(new Option("option", "o", true, "value1", "v1"));

        Option option = cmdLine.getOption("option");
        assertEquals("option", option.getShortName());
        assertEquals("o", option.getOpt());
        assertEquals("v1", option.getLongOpt());
        assertEquals(true, option.isRequired());
        assertEquals("value1", option.getDefaultValue());
        assertEquals(0, option.getValues().size());
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
        cmdLine.addOption(new Option("option", "o", false, null, 'v'));
        cmdLine.addValue(cmdLine.getOption("option"), "value1");
        cmdLine.addValue(cmdLine.getOption("option"), "value2");

        List values = cmdLine.getValues(cmdLine.getOption("option"), null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
        cmdLine.addOption(new Option("option", "o", false, null, 'v'));
        cmdLine.addValue(cmdLine.getOption("option"), "value1");

        List values = cmdLine.getUndefaultedValues(cmdLine.getOption("option"));
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
        cmdLine.addOption(new Option("option", "o", false, null, 's'));
        cmdLine.addSwitch(cmdLine.getOption("option"), true);

        assertTrue(cmdLine.getSwitch(cmdLine.getOption("option"), Boolean.FALSE));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
        cmdLine.addOption(new PropertyOption("property", "p", "default"));
        cmdLine.addProperty(cmdLine.getOption("property"), "value1");

        assertEquals("default", cmdLine.getProperty("property"));
        assertEquals("value1", cmdLine.getProperty("property", "default"));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
        cmdLine.addOption(new PropertyOption("property1", "p1"));
        cmdLine.addOption(new PropertyOption("property2", "p2"));
        cmdLine.addProperty(cmdLine.getOption("property1"), "property1Value1");
        cmdLine.addProperty(cmdLine.getOption("property1"), "property1Value2");
        cmdLine.addProperty(cmdLine.getOption("property2"), "property2Value");

        Set properties = cmdLine.getProperties(cmdLine.getOption("option"));
        assertEquals(2, properties.size());
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
        cmdLine.addOption(new Option("option1", "o1", true, "value1", "v1"));
        cmdLine.addOption(new Option("option2", "o2", false, null, 's'));
        cmdLine.addValue(cmdLine.getOption("option1"), "value1Value1");
        cmdLine.addValue(cmdLine.getOption("option1"), "value1Value2");
        cmdLine.addSwitch(cmdLine.getOption("option2"), true);

        assertEquals("-o1 value1Value1 value1Value2 -o2", cmdLine.toString());
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
        cmdLine.addOption(new Option("option1", "o1", true, "value1", "v1"));
        cmdLine.addOption(new Option("option2", "o2", false, null, 's'));

        List options = cmdLine.getOptions();
        assertEquals(2, options.size());
        assertEquals(Option.class, options.get(0).getClass());
        assertEquals(Option.class, options.get(1).getClass());
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl cmdLine = new WriteableCommandLineImpl(new Option("option"), new ArrayList());
        cmdLine.addOption(new Option("option1", "o1", true, "value1", "v1"));
        cmdLine.addOption(new Option("option2", "o2", false, null, 's'));

        Set triggers = cmdLine.getOptionTriggers();
        assertEquals(2, triggers.size());
        assertTrue(triggers.contains("o1"));
        assertTrue(triggers.contains("o2"));
    }
}