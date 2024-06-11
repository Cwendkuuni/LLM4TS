import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_addOption() {
        // Given
        Option rootOption = new Option("root", "Root option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        // When
        Option option = new Option("test", "Test option");
        writeableCommandLine.addOption(option);

        // Then
        List options = writeableCommandLine.getOptions();
        assertTrue(options.contains(option));

        Map nameToOption = writeableCommandLine.getNameToOption();
        Option optionFromMap = (Option) nameToOption.get("test");
        assertEquals(option, optionFromMap);

        // Verify parent option is also added
        Option parent = option.getParent();
        assertTrue(options.contains(parent));
    }

    @Test
    public void testWriteableCommandLineImpl_addValue() {
        // Given
        Option option = new Option("test", "Test option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "Root option"), Collections.emptyList());
        writeableCommandLine.addOption(option);

        // When
        writeableCommandLine.addValue(option, "value1");

        // Then
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_addSwitch() {
        // Given
        Option option = new Option("test", "Test switch option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "Root option"), Collections.emptyList());
        writeableCommandLine.addOption(option);

        // When
        writeableCommandLine.addSwitch(option, true);

        // Then
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption() {
        // Given
        Option option = new Option("test", "Test option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "Root option"), Collections.emptyList());
        writeableCommandLine.addOption(option);

        // When
        boolean result = writeableCommandLine.hasOption(option);

        // Then
        assertTrue(result);
    }

    @Test
    public void testWriteableCommandLineImpl_getOption() {
        // Given
        Option option = new Option("test", "Test option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "Root option"), Collections.emptyList());
        writeableCommandLine.addOption(option);

        // When
        Option result = writeableCommandLine.getOption("test");

        // Then
        assertEquals(option, result);
    }

    @Test
    public void testWriteableCommandLineImpl_getValues() {
        // Given
        Option option = new Option("test", "Test option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "Root option"), Collections.emptyList());
        writeableCommandLine.addOption(option);

        // When
        List values = writeableCommandLine.getValues(option, null);

        // Then
        assertTrue(values.isEmpty());

        // When
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        // Then
        values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        // When
        List defaultValues = Arrays.asList("default1", "default2");
        values = writeableCommandLine.getValues(option, defaultValues);

        // Then
        assertEquals(3, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        assertEquals("default1", values.get(2));
        assertEquals("default2", values.get(3));
    }

    @Test
    public void testWriteableCommandLineImpl_getUndefaultedValues() {
        // Given
        Option option = new Option("test", "Test option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "Root option"), Collections.emptyList());
        writeableCommandLine.addOption(option);

        // When
        List values = writeableCommandLine.getUndefaultedValues(option);

        // Then
        assertTrue(values.isEmpty());

        // When
        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        // Then
        values = writeableCommandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_getSwitch() {
        // Given
        Option option = new Option("test", "Test switch option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "Root option"), Collections.emptyList());
        writeableCommandLine.addOption(option);

        // When
        Boolean switchValue = writeableCommandLine.getSwitch(option, null);

        // Then
        assertNull(switchValue);

        // When
        writeableCommandLine.addSwitch(option, true);

        // Then
        switchValue = writeableCommandLine.getSwitch(option, false);
        assertTrue(switchValue);

        // When
        writeableCommandLine.addSwitch(option, false);

        // Then
        switchValue = writeableCommandLine.getSwitch(option, false);
        assertFalse(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty() {
        // Given
        Option option = new Option("test", "Test property option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "Root option"), Collections.emptyList());
        writeableCommandLine.addOption(option);

        // When
        writeableCommandLine.addProperty(option, "test.property", "value1");

        // Then
        String propertyValue = writeableCommandLine.getProperty("test.property");
        assertEquals("value1", propertyValue);
    }

    @Test
    public void testWriteableCommandLineImpl_addProperty() {
        // Given
        Option option = new Option("test", "Test property option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "Root option"), Collections.emptyList());
        writeableCommandLine.addOption(option);

        // When
        writeableCommandLine.addProperty(option, "test.property1", "value1");
        writeableCommandLine.addProperty(option, "test.property2", "value2");

        // Then
        Properties properties = writeableCommandLine.getProperties(option);
        assertEquals(2, properties.size());
        assertEquals("value1", properties.getProperty("test.property1"));
        assertEquals("value2", properties.getProperty("test.property2"));
    }

    @Test
    public void testWriteableCommandLineImpl_getProperties() {
        // Given
        Option option = new Option("test", "Test property option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "Root option"), Collections.emptyList());
        writeableCommandLine.addOption(option);

        // When
        writeableCommandLine.addProperty(option, "test.property1", "value1");
        writeableCommandLine.addProperty(option, "test.property2", "value2");

        // Then
        Set properties = writeableCommandLine.getProperties();
        assertEquals(2, properties.size());
        assertTrue(properties.contains("test.property1"));
        assertTrue(properties.contains("test.property2"));
    }

    @Test
    public void testWriteableCommandLineImpl_looksLikeOption() {
        // Given
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "Root option"), Collections.emptyList());

        // When
        boolean result = writeableCommandLine.looksLikeOption("test");

        // Then
        assertFalse(result);

        // When
        writeableCommandLine.addPrefix("test.");

        // Then
        result = writeableCommandLine.looksLikeOption("test.option");

        // Then
        assertTrue(result);
    }

    @Test
    public void testWriteableCommandLineImpl_toString() {
        // Given
        Option option = new Option("test", "Test option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "Root option"), Arrays.asList("arg1", "arg2"));
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value1");

        // Then
        String result = writeableCommandLine.toString();

        // Then
        String expected = "arg1 arg2 --test value1 --root";
        assertEquals(expected, result);
    }

    @Test
    public void testWriteableCommandLineImpl_getOptions() {
        // Given
        Option option1 = new Option("test1", "Test option 1");
        Option option2 = new Option("test2", "Test option 2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "Root option"), Collections.emptyList());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        // Then
        List options = writeableCommandLine.getOptions();

        // Then
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testWriteableCommandLineImpl_getOptionTriggers() {
        // Given
        Option option1 = new Option("test1", "Test option 1");
        Option option2 = new Option("test2", "Test option 2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "Root option"), Collections.emptyList());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);

        // Then
        Set optionTriggers = writeableCommandLine.getOptionTriggers();

        // Then
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("test1"));
        assertTrue(optionTriggers.contains("test2"));
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultValues() {
        // Given
        Option option = new Option("test", "Test option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "Root option"), Collections.emptyList());
        writeableCommandLine.addOption(option);

        // When
        List defaultValues = Arrays.asList("default1", "default2");
        writeableCommandLine.setDefaultValues(option, defaultValues);

        // Then
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_setDefaultSwitch() {
        // Given
        Option option = new Option("test", "Test switch option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "Root option"), Collections.emptyList());
        writeableCommandLine.addOption(option);

        // When
        Boolean defaultSwitch = true;
        writeableCommandLine.setDefaultSwitch(option, defaultSwitch);

        // Then
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getNormalised() {
        // Given
        Option option = new Option("test", "Test option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("root", "Root option"), Arrays.asList("arg1", "arg2"));
        writeableCommandLine.addOption(option);

        // Then
        List normalised = writeableCommandLine.getNormalised();

        // Then
        assertEquals(3, normalised.size());
        assertTrue(normalised.contains("arg1"));
        assertTrue(normalised.contains("arg2"));
        assertTrue(normalised.contains("--test"));
    }
}