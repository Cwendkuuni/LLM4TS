import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_addOption() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addOption(option);

        List options = commandLine.getOptions();
        assertTrue(options.contains(option));
    }

    @Test
    public void testWriteableCommandLineImpl_addValue() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .valueSeparator(' ')
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_addSwitch() {
        Option option = Option.builder("s").longOpt("switch")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testWriteableCommandLineImpl_getOption() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addOption(option);

        Option retrievedOption = commandLine.getOption("o");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testWriteableCommandLineImpl_getValues() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .valueSeparator(' ')
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_getUndefaultedValues() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .valueSeparator(' ')
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_getSwitch() {
        Option option = Option.builder("s").longOpt("switch")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty() {
        Option option = Option.builder("p").longOpt("property")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addProperty(option, "property", "value");

        String propertyValue = commandLine.getProperty(option, "defaultValue");
        assertEquals("value", propertyValue);
    }

    @Test
    public void testWriteableCommandLineImpl_addProperty() {
        Option option = Option.builder("p").longOpt("property")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        Properties properties = new Properties();
        properties.setProperty("propertyKey", "propertyValue");
        commandLine.addProperty(option, properties);

        Map<String, String> retrievedProperties = commandLine.getProperties(option);
        assertEquals(1, retrievedProperties.size());
        assertEquals("propertyValue", retrievedProperties.get("propertyKey"));
    }

    @Test
    public void testWriteableCommandLineImpl_getProperties() {
        Option option = Option.builder("p").longOpt("property")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        Properties properties = new Properties();
        properties.setProperty("propertyKey1", "propertyValue1");
        properties.setProperty("propertyKey2", "propertyValue2");
        commandLine.addProperty(option, properties);

        Map<String, String> retrievedProperties = commandLine.getProperties(option);
        assertEquals(2, retrievedProperties.size());
        assertEquals("propertyValue1", retrievedProperties.get("propertyKey1"));
        assertEquals("propertyValue2", retrievedProperties.get("propertyKey2"));
    }

    @Test
    public void testWriteableCommandLineImpl_toString() {
        Option option = Option.builder("o").longOpt("option")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        String commandLineString = commandLine.toString();
        String expectedCommandLineString = "value1 value2";
        assertEquals(expectedCommandLineString, commandLineString);
    }

    @Test
    public void testWriteableCommandLineImpl_getOptions() {
        Option option1 = Option.builder("o1").longOpt("option1")
                .required(false)
                .build();
        Option option2 = Option.builder("o2").longOpt("option2")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());

        commandLine.addOption(option2);

        List options = commandLine.getOptions();
        assertEquals(1, options.size());
        assertTrue(options.contains(option2));
    }

    @Test
    public void testWriteableCommandLineImpl_getOptionTriggers() {
        Option option1 = Option.builder("o1").longOpt("option1")
                .required(false)
                .build();
        Option option2 = Option.builder("o2").longOpt("option2")
                .required(false)
                .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());

        commandLine.addOption(option2);

        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(1, optionTriggers.size());
        assertTrue(optionTriggers.contains("o2"));
    }
}