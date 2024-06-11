import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addValue(option, "value");

        List values = commandLine.getValues(option, new ArrayList<>());
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, null);
        assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    public void testHasOption() {
        Option option = new Option();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        assertFalse(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addOption(option);

        Option retrievedOption = commandLine.getOption("test");
        assertNotNull(retrievedOption);
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        Option option = new Option();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        List defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);

        List values = commandLine.getValues(option, null);

        assertEquals(defaultValues, values);
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.setDefaultSwitch(option, Boolean.TRUE);

        Boolean switchValue = commandLine.getSwitch(option, null);

        assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    public void testAddProperty() {
        Option option = new Option();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addProperty("property", "value");

        String propertyValue = commandLine.getProperty("property", null);
        assertEquals("value", propertyValue);
    }

    @Test
    public void testLooksLikeOption() {
        Option option = new Option();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(new Option("-t"));

        boolean looksLikeOption = commandLine.looksLikeOption("-t");

        assertTrue(looksLikeOption);
    }

    @Test
    public void testToString() {
        Option option = new Option();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));

        String toStringResult = commandLine.toString();

        assertEquals("arg1 arg2", toStringResult);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option();
        Option option2 = new Option();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.addOption(option2);

        List<Option> options = commandLine.getOptions();

        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option();
        Option option2 = new Option();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.addOption(option2);

        int optionTriggersSize = commandLine.getOptionTriggers().size();

        assertEquals(2, optionTriggersSize);
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.setDefaultValues(option, Arrays.asList("default1", "default2"));

        List values = commandLine.getValues(option, null);
        assertEquals(Arrays.asList("default1", "default2"), values);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.setDefaultSwitch(option, Boolean.TRUE);

        Boolean switchValue = commandLine.getSwitch(option, null);
        assertEquals(Boolean.TRUE, switchValue);
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));

        List normalised = commandLine.getNormalised();

        assertEquals(Arrays.asList("arg1", "arg2"), normalised);
    }
}