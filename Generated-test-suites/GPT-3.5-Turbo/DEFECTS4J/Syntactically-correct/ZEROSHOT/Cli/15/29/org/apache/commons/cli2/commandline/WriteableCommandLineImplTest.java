import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Argument();

        Object value = "testValue";

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addValue(option, value);

        assertEquals(value, commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("testOption");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);

        assertEquals(option, commandLine.getOption("testOption"));
    }

    @Test
    public void testGetValues() {
        Option option = new Argument();
        List defaultValue = new ArrayList<>();
        defaultValue.add("default");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addValue(option, "value");

        assertEquals("value", commandLine.getValues(option, defaultValue).get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Argument();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addValue(option, "value");

        assertEquals("value", commandLine.getUndefaultedValues(option).get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("testOption");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new PropertyOption(), null);
        commandLine.addProperty("testProperty", "testValue");

        assertEquals("testValue", commandLine.getProperty("testProperty"));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new PropertyOption(), null);
        commandLine.addProperty("testProperty", "testValue");

        assertEquals("testValue", commandLine.getProperty("testProperty"));
    }

    @Test
    public void testGetPropertyDefaultValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new PropertyOption(), null);
        commandLine.addProperty("testProperty", "testValue");

        assertEquals("defaultValue", commandLine.getProperty("nonexistentProperty", "defaultValue"));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new PropertyOption(), null);
        commandLine.addProperty("testProperty", "testValue");

        assertEquals(1, commandLine.getProperties().size());
    }

    @Test
    public void testLooksLikeOption() {
        Option option = new Option("testOption");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);

        assertTrue(commandLine.looksLikeOption("testOption"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("testOption"), null);
        commandLine.addValue(new Argument(), "testValue");

        assertEquals("testValue", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);

        assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("testOption");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.addOption(option);

        assertEquals(1, commandLine.getOptionTriggers().size());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("testOption");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.setDefaultValues(option, null);

        assertEquals(Collections.EMPTY_LIST, commandLine.getValues(option, null));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("testOption");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, null);
        commandLine.setDefaultSwitch(option, null);

        assertNull(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("testOption"), null);
        commandLine.addValue(new Argument(), "testValue");

        assertEquals(1, commandLine.getNormalised().size());
    }

}