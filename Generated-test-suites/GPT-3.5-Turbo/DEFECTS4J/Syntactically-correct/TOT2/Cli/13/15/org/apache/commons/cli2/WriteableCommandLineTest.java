import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption", "Test option");
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption", "Test option");
        writeableCommandLine.addOption(option);
        Object value = "testValue";
        writeableCommandLine.addValue(option, value);
        assertEquals(value, writeableCommandLine.getValues(option).get(0));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption", "Test option");
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        writeableCommandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, writeableCommandLine.getValues(option));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption", "Test option");
        writeableCommandLine.addOption(option);
        boolean value = true;
        writeableCommandLine.addSwitch(option, value);
        assertEquals(value, writeableCommandLine.getSwitches().get(option));
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption", "Test option");
        writeableCommandLine.setDefaultSwitch(option, true);
        assertEquals(true, writeableCommandLine.getSwitches().get(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl();
        String property = "testProperty";
        String value = "testValue";
        writeableCommandLine.addProperty(property, value);
        assertEquals(value, writeableCommandLine.getProperty(property));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine writeableCommandLine = new WriteableCommandLineImpl();
        assertTrue(writeableCommandLine.looksLikeOption("-testOption"));
    }
}

class WriteableCommandLineImpl implements WriteableCommandLine {
    private List<Option> options = new ArrayList<>();
    private List<Option> switches = new ArrayList<>();
    private List<Object> values = new ArrayList<>();
    private List<String> properties = new ArrayList<>();

    @Override
    public void addOption(Option option) {
        options.add(option);
    }

    @Override
    public void addValue(Option option, Object value) {
        values.add(value);
    }

    @Override
    public List<Object> getValues(Option option) {
        return values;
    }

    @Override
    public void setDefaultValues(Option option, List defaultValues) {
        values.addAll(defaultValues);
    }

    @Override
    public void addSwitch(Option option, boolean value) {
        switches.add(option);
    }

    @Override
    public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
        if (defaultSwitch != null) {
            switches.add(option);
        }
    }

    @Override
    public void addProperty(String property, String value) {
        properties.add(value);
    }

    @Override
    public boolean looksLikeOption(String argument) {
        return argument.startsWith("-");
    }

    @Override
    public List<Option> getOptions() {
        return options;
    }

    @Override
    public List<Option> getSwitches() {
        return switches;
    }

    @Override
    public String getProperty(String property) {
        for (String prop : properties) {
            if (prop.equals(property)) {
                return prop;
            }
        }
        return null;
    }
}