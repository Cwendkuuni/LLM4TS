import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option rootOption = new Option("t", "test", false, "Test Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, Arrays.asList("--test", "-a", "1", "-b", "2"));

        // test hasOption
        assertTrue(commandLine.hasOption(rootOption));

        // test getOption
        Option option = commandLine.getOption("t");
        assertEquals(rootOption, option);

        // test getValues
        List values = commandLine.getValues(rootOption, Collections.emptyList());
        assertEquals(Arrays.asList("1", "2"), values);

        // test getUndefaultedValues
        List unfaultedValues = commandLine.getUndefaultedValues(rootOption);
        assertTrue(unfaultedValues.isEmpty());

        // test getSwitch
        Boolean switchValue = commandLine.getSwitch(rootOption, false);
        assertFalse(switchValue);

        // test addProperty
        commandLine.addProperty(rootOption, "property1", "value1");
        Map properties = commandLine.getProperties(rootOption);
        assertEquals(1, properties.size());
        assertEquals("value1", properties.get("property1"));

        // test getProperty
        String propertyValue = commandLine.getProperty(rootOption, "property1", "defaultValue");
        assertEquals("value1", propertyValue);

        // test getProperties
        Set propertiesSet = commandLine.getProperties(rootOption);
        assertEquals(1, propertiesSet.size());
        assertTrue(propertiesSet.contains("property1"));

        // test looksLikeOption
        assertTrue(commandLine.looksLikeOption("--test"));

        // test toString
        String commandLineString = commandLine.toString();
        assertEquals("--test -a 1 -b 2", commandLineString);

        // test getOptions
        List options = commandLine.getOptions();
        assertEquals(1, options.size());
        assertEquals(rootOption, options.get(0));

        // test getOptionTriggers
        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(1, optionTriggers.size());
        assertTrue(optionTriggers.contains("t"));

        // test setDefaultValues
        List defaultValues = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(rootOption, defaultValues);
        List commandLineValues = commandLine.getValues(rootOption, defaultValues);
        assertEquals(Arrays.asList("1", "2", "default1", "default2"), commandLineValues);

        // test setDefaultSwitch
        commandLine.setDefaultSwitch(rootOption, true);
        Boolean defaultSwitchValue = commandLine.getSwitch(rootOption, null);
        assertTrue(defaultSwitchValue);
    }
}