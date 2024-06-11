import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.commandline.OptionImpl;
import org.apache.commons.cli2.WriteableCommandLine;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionImpl(), new ArrayList<String>());
        Option option = new OptionImpl();
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionImpl(), new ArrayList<String>());
        Option option = new OptionImpl();
        String value = "value";
        commandLine.addValue(option, value);
        assertTrue(commandLine.getValues(option, null).contains(value));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionImpl(), new ArrayList<String>());
        Option option = new OptionImpl();
        boolean bool = true;
        commandLine.addSwitch(option, bool);
        assertEquals(bool, commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionImpl(), new ArrayList<String>());
        Option option = new OptionImpl();
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionImpl(), new ArrayList<String>());
        Option option = new OptionImpl();
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption(option.getPreferredName()));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionImpl(), new ArrayList<String>());
        Option option = new OptionImpl();
        String value1 = "value1";
        String value2 = "value2";
        List<String> defaultValues = new ArrayList<String>();
        defaultValues.add("defaultValue");
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);

        List<String> values = commandLine.getValues(option, defaultValues);
        assertTrue(values.contains(value1));
        assertTrue(values.contains(value2));
        assertFalse(values.contains("defaultValue"));

    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionImpl(), new ArrayList<String>());
        Option option = new OptionImpl();
        String value1 = "value1";
        String value2 = "value2";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);

        List<String> values = commandLine.getUndefaultedValues(option);
        assertTrue(values.contains(value1));
        assertTrue(values.contains(value2));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionImpl(), new ArrayList<String>());
        Option option = new OptionImpl();
        boolean defaultValue = false;
        commandLine.addSwitch(option, true);

        assertEquals(true, commandLine.getSwitch(option, defaultValue));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionImpl(), new ArrayList<String>());
        Option option = new OptionImpl();
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);

        assertEquals(value, commandLine.getProperty(option, property, null));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionImpl(), new ArrayList<String>());
        Option option = new OptionImpl();
        String property = "property";
        String value = "value";
        commandLine.addProperty(option, property, value);

        assertTrue(commandLine.getProperties(option).contains(property));
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionImpl(), new ArrayList<String>());
        Option option = new OptionImpl();
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionImpl(), new ArrayList<String>());
        Option option = new OptionImpl();
        commandLine.addOption(option);
        assertTrue(commandLine.getOptionTriggers().contains(option.getPreferredName()));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionImpl(), new ArrayList<String>());
        Option option = new OptionImpl();
        List<String> defaultValues = new ArrayList<String>();
        defaultValues.add("defaultValue");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getValues(option, null));

    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionImpl(), new ArrayList<String>());
        Option option = new OptionImpl();
        Boolean defaultSwitch = true;
        commandLine.setDefaultSwitch(option, defaultSwitch);
        assertEquals(defaultSwitch, commandLine.getSwitch(option, null));

    }

    @Test
    public void testGetNormalised() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new OptionImpl(), new ArrayList<String>());
        List<String> normalised = new ArrayList<String>();
        normalised.add("arg1");
        normalised.add("arg2");
        commandLine.addOption(new OptionImpl());
        commandLine.addOption(new OptionImpl());
        assertEquals(normalised, commandLine.getNormalised());
    }
}