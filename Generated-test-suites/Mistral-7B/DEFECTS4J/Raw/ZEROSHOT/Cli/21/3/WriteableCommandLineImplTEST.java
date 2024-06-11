import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option rootOption = new Option("opt", "option", true, "Option description");
        List arguments = new ArrayList();
        arguments.add("--opt value1");
        arguments.add("--opt value2");
        arguments.add("arg1");
        arguments.add("arg2");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        // test adding options
        Option option1 = new Option("o1", "option1", false, "Option1 description");
        commandLine.addOption(option1);
        Option option2 = new Option("o2", "option2", true, "Option2 description");
        commandLine.addOption(option2);
        assertEquals(3, commandLine.getOptions().size());

        // test getting options
        Option retrievedOption1 = commandLine.getOption("o1");
        Option retrievedOption2 = commandLine.getOption("o2");
        assertEquals(option1, retrievedOption1);
        assertEquals(option2, retrievedOption2);

        // test getting values for options
        List valuesOption2 = commandLine.getValues(option2, null);
        assertEquals(2, valuesOption2.size());
        assertEquals("value1", valuesOption2.get(0));
        assertEquals("value2", valuesOption2.get(1));

        // test getting switch for options
        assertFalse(commandLine.getSwitch(option1, false));
        assertTrue(commandLine.getSwitch(option2, false));

        // test getting properties for options
        Option propertyOption = new Option("p", "property", true, "Property option");
        commandLine.addProperty(propertyOption, "myProperty", "value1");
        Map<String, String> properties = commandLine.getProperties(propertyOption);
        assertEquals("value1", properties.get("myProperty"));

        // test looking like an option
        assertTrue(commandLine.looksLikeOption("--opt"));
        assertFalse(commandLine.looksLikeOption("arg1"));

        // test getting normalised arguments
        List normalised = commandLine.getNormalised();
        assertEquals(4, normalised.size());
        assertEquals("--opt", normalised.get(0).toString());
        assertEquals("value1", normalised.get(1).toString());
        assertEquals("value2", normalised.get(2).toString());
        assertEquals("arg1", normalised.get(3).toString());

        // test getting option triggers
        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("o1"));
        assertTrue(optionTriggers.contains("o2"));

        // test getting root option
        Option rootOptionFromCommandLine = commandLine.getOption("opt");
        assertEquals(rootOption, rootOptionFromCommandLine);

        // test toString method
        String commandLineString = commandLine.toString();
        String expectedCommandLineString = "--opt value1 value2 arg1 arg2";
        assertEquals(expectedCommandLineString, commandLineString);
    }
}