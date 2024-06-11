import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option rootOption = new Option("root", "rootOption", false, "root description");
        List arguments = new ArrayList();
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(rootOption, arguments);

        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        Option option3 = new Option("o3", "option3", true, "Option 3");

        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        writeableCommandLine.addOption(option3);

        // test hasOption
        assertTrue(writeableCommandLine.hasOption(option1));
        assertTrue(writeableCommandLine.hasOption(option2));
        assertFalse(writeableCommandLine.hasOption(new Option("notPresent", "notPresent", false, "not present option")));

        // test getOption
        Option optionRetrieved = writeableCommandLine.getOption("o1");
        assertEquals(option1, optionRetrieved);

        // test getValues
        writeableCommandLine.addValue(option1, "value1");
        writeableCommandLine.addValue(option1, "value2");
        List values = writeableCommandLine.getValues(option1, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        // test getUndefaultedValues
        List unfaultedValues = writeableCommandLine.getUndefaultedValues(option1);
        assertEquals(0, unfaultedValues.size());

        // test getSwitch
        writeableCommandLine.addSwitch(option2, true);
        assertTrue(writeableCommandLine.getSwitch(option2, false));

        // test getProperty
        Option propertyOption = new Option("p", "propertyOption", false, "propertyOption");
        writeableCommandLine.addProperty(propertyOption, "property", "propertyValue");
        assertEquals("propertyValue", writeableCommandLine.getProperty(propertyOption, "defaultValue"));

        // test getProperties
        Option propertyOption2 = new Option("p2", "propertyOption2", false, "propertyOption2");
        writeableCommandLine.addProperty(propertyOption2, "property1", "propertyValue1");
        writeableCommandLine.addProperty(propertyOption2, "property2", "propertyValue2");
        Set properties = writeableCommandLine.getProperties(propertyOption2);
        assertEquals(2, properties.size());
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));

        // test looksLikeOption
        assertTrue(writeableCommandLine.looksLikeOption("o1"));
        assertFalse(writeableCommandLine.looksLikeOption("notAnOption"));

        // test toString
        String commandLineString = writeableCommandLine.toString();
        String expectedCommandLineString = "o1 value1 value2 o2";
        assertEquals(expectedCommandLineString, commandLineString);

        // test getOptions
        List options = writeableCommandLine.getOptions();
        assertEquals(3, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
        assertTrue(options.contains(option3));

        // test getOptionTriggers
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        assertEquals(3, optionTriggers.size());
        assertTrue(optionTriggers.contains("o1"));
        assertTrue(optionTriggers.contains("o2"));
        assertTrue(optionTriggers.contains("o3"));

        // test setDefaultValues
        writeableCommandLine.setDefaultValues(option1, Arrays.asList("defaultValue1", "defaultValue2"));
        List valuesDefault = writeableCommandLine.getValues(option1, null);
        assertEquals(2, valuesDefault.size());
        assertEquals("defaultValue1", valuesDefault.get(0));
        assertEquals("defaultValue2", valuesDefault.get(1));

        // test setDefaultSwitch
        writeableCommandLine.setDefaultSwitch(option2, false);
        assertFalse(writeableCommandLine.getSwitch(option2, null));
    }
}