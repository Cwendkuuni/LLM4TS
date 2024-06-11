import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.cli2.*;
import org.apache.commons.cli2.commandline.*;

public class WriteableCommandLineImplTest {
    private Option option;
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        option = OptionBuilder.withLongName("option")
                              .withDescription("Test Option")
                              .withArgument(
                                   ArgumentBuilder
                                       .withName("argument")
                                       .withDescription("Test Argument")
                                       .withMinimum(1)
                                       .withMaximum(1)
                                       .create())
                              .create();

        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List<String> expectedValues = Arrays.asList("value1", "value2");
        assertEquals(expectedValues, 
                     commandLine.getValues(option, Collections.emptyList()));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("option"));
    }

    @Test
    public void testGetValues() {
        List<String> expectedValues = Arrays.asList("value1", "value2");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals(expectedValues, 
                     commandLine.getValues(option, Collections.emptyList()));
    }

    @Test
    public void testGetUndefaultedValues() {
        List<String> expectedValues = Arrays.asList("value1", "value2");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        assertEquals(expectedValues, commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testGetPropertyWithDefault() {
        assertEquals("defaultValue", 
                     commandLine.getProperty("property", "defaultValue"));
    }

    @Test
    public void testGetProperties() {
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        assertEquals(2, commandLine.getProperties().size());
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        option.buildValueType(Option.STRING_VALUE);
        commandLine.addValue(option, "value");
        assertEquals("value", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testGetOptionTriggers() {
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptionTriggers().size());
    }

    @Test
    public void testSetDefaultValues() {
        commandLine.setDefaultValues(option, Arrays.asList("default1", "default2"));
        List<String> expectedDefaults = Arrays.asList("default1", "default2");
        assertEquals(expectedDefaults, 
                     commandLine.getValues(option, Collections.emptyList()));
        commandLine.setDefaultValues(option, null);
        assertEquals(Collections.emptyList(), 
                     commandLine.getValues(option, Collections.emptyList()));
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
        commandLine.setDefaultSwitch(option, null);
        assertNull(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetNormalised() {
        assertEquals(Collections.emptyList(), commandLine.getNormalised());
    }
}