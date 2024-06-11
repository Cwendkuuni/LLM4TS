import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void addOptionTest() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("name", "description");
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void addValueTest() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("name", "description");
        Object value = "value";
        commandLine.addValue(option, value);
        assertTrue(commandLine.getValues(option).contains(value));
    }

    @Test
    public void getArgumentValuesTest() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("name", "description");
        Object value1 = "value1";
        Object value2 = "value2";
        commandLine.addValue(option, value1);
        commandLine.addValue(option, value2);
        List<ArgumentValue> argumentValues = commandLine.getArgumentValues(option);
        assertTrue(argumentValues.contains(new ArgumentValue(option, value1)));
        assertTrue(argumentValues.contains(new ArgumentValue(option, value2)));
    }

    @Test
    public void setDefaultValuesTest() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("name", "description");
        List<Object> defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }

    @Test
    public void addSwitchTest() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("name", "description");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitchState(option));
    }

    @Test
    public void setDefaultSwitchTest() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("name", "description");
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getDefaultSwitch(option));
    }

    @Test
    public void addPropertyTest() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String property = "key";
        String value = "value";
        commandLine.addProperty(property, value);
        assertEquals(value, commandLine.getProperty(property));
    }

    @Test
    public void looksLikeOptionTest() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String option1 = "--option1";
        String option2 = "-o";
        assertTrue(commandLine.looksLikeOption(option1));
        assertTrue(commandLine.looksLikeOption(option2));
    }
}