import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    WriteableCommandLine commandLine = new WriteableCommandLineImpl();

    @Test
    public void testAddOption() {
        Option option = new OptionImpl("opt");
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new OptionImpl("opt");
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        assertTrue(commandLine.hasValue(option, "value"));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new OptionImpl("opt");
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        assertEquals(1, commandLine.getUndefaultedValues(option).size());
        assertTrue(commandLine.hasValue(option, "value"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new OptionImpl("opt");
        commandLine.addOption(option);

        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default");
        commandLine.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        Option option = new OptionImpl("opt");
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        assertTrue(commandLine.hasSwitch(option));

        commandLine.addSwitch(option, false);
        assertFalse(commandLine.hasSwitch(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new OptionImpl("opt");
        commandLine.addOption(option);

        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getDefaultSwitch(option));

        commandLine.setDefaultSwitch(option, false);
        assertFalse(commandLine.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        Option option = new OptionImpl("opt");
        commandLine.addOption(option);

        commandLine.addProperty(option, "property", "value");
        assertEquals("value", commandLine.getProperty(option, "property"));
    }

    @Test
    public void testAddPropertyDefault() {
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testSetCurrentOption() {
        Option option = new OptionImpl("opt");
        commandLine.setCurrentOption(option);
        assertEquals(option, commandLine.getCurrentOption());
    }
}