import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test");
        cmd.addOption(option);
        assertTrue(cmd.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test");
        Object value = "value";
        cmd.addValue(option, value);
        assertEquals(value, cmd.getValues(option).get(0));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test");
        List defaultValues = new ArrayList();
        defaultValues.add("default");
        cmd.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, cmd.getDefaultValues(option));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test");
        boolean value = true;
        cmd.addSwitch(option, value);
        assertTrue(cmd.getSwitches().containsKey(option) && cmd.getSwitches().get(option) == value);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        Option option = new Option("test");
        Boolean defaultSwitch = true;
        cmd.setDefaultSwitch(option, defaultSwitch);
        assertEquals(defaultSwitch, cmd.getDefaultSwitch(option));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        String property = "key";
        String value = "value";
        cmd.addProperty(property, value);
        assertEquals(value, cmd.getProperty(property));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine cmd = new WriteableCommandLineImpl();
        assertTrue(cmd.looksLikeOption("-test"));
    }

    private class WriteableCommandLineImpl implements WriteableCommandLine {
        private List<Option> options = new ArrayList<>();
        private List<String> values = new ArrayList<>();
        private List defaultValues = new ArrayList();
        private List<Boolean> switches = new ArrayList<>();
        private Boolean defaultSwitch;
        private List<String> properties = new ArrayList<>();

        @Override
        public void addOption(Option option) {
            options.add(option);
        }

        @Override
        public void addValue(Option option, Object value) {
            values.add((String) value);
        }

        @Override
        public List getValues(Option option) {
            return values;
        }

        @Override
        public void setDefaultValues(Option option, List defaultValues) {
            this.defaultValues = defaultValues;
        }

        @Override
        public List getDefaultValues(Option option) {
            return defaultValues;
        }

        @Override
        public void addSwitch(Option option, boolean value) {
            switches.add(value);
        }

        @Override
        public Map getSwitches() {
            Map switchesMap = new HashMap<>();
            for(int i = 0; i < options.size(); i++) {
                switchesMap.put(options.get(i), switches.get(i));
            }
            return switchesMap;
        }

        @Override
        public void setDefaultSwitch(Option option, Boolean defaultSwitch) {
            this.defaultSwitch = defaultSwitch;
        }

        @Override
        public Boolean getDefaultSwitch(Option option) {
            return defaultSwitch;
        }

        @Override
        public void addProperty(String property, String value) {
            properties.add(value);
        }

        @Override
        public String getProperty(String property) {
            for(String prop : properties) {
                if(property.equals(prop)) {
                    return prop;
                }
            }
            return null;
        }

        @Override
        public boolean looksLikeOption(String argument) {
            return argument.startsWith("-");
        }
    }
}