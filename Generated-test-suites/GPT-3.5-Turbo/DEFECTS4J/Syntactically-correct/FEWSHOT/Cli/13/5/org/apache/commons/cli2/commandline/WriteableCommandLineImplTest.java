import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;

    @Before
    public void setup() {
        Option option1 = new OptionImpl("option1");
        Option option2 = new OptionImpl("option2");
        Argument argument = new ArgumentImpl("argument");

        commandLine = new WriteableCommandLineImpl(option1, Collections.singletonList(argument));
        commandLine.addOption(option1);
        commandLine.addOption(option2);
    }

    @Test
    public void testAddOption() {
        assertTrue(commandLine.hasOption(option1));
        assertTrue(commandLine.hasOption(option2));
    }

    @Test
    public void testAddValue() {
        Object value = "value";
        commandLine.addValue(option1, value);

        List values = commandLine.getValues(option1, Collections.emptyList());
        assertEquals(Collections.singletonList(value), values);
    }

    @Test
    public void testAddSwitch() {
        boolean value = true;
        commandLine.addSwitch(option1, value);

        Boolean switchValue = commandLine.getSwitch(option1, null);
        assertEquals(Boolean.valueOf(value), switchValue);
    }

    @Test
    public void testHasOption() {
        assertTrue(commandLine.hasOption(option1));
        assertFalse(commandLine.hasOption(new OptionImpl("option3")));
    }

    @Test
    public void testGetOption() {
        Option option = commandLine.getOption("option1");
        assertEquals(option1, option);
    }

    @Test
    public void testGetValues() {
        List defaultValues = Collections.singletonList("default");
        commandLine.setDefaulValues(option1, defaultValues);

        List values = commandLine.getValues(option1, Collections.emptyList());
        assertEquals(defaultValues, values);
    }

    @Test
    public void testGetSwitch() {
        Boolean defaultSwitch = Boolean.TRUE;
        commandLine.setDefaultSwitch(option1, defaultSwitch);

        Boolean switchValue = commandLine.getSwitch(option1, null);
        assertEquals(defaultSwitch, switchValue);
    }

    @Test
    public void testAddProperty() {
        String property = "key";
        String value = "value";
        commandLine.addProperty(property, value);

        String propertyValue = commandLine.getProperty(property, null);
        assertEquals(value, propertyValue);
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-o"));
        assertTrue(commandLine.looksLikeOption("--o"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testToString() {
        String argument = "argument";
        commandLine.addValue(argument);

        String expected = "argument";
        String result = commandLine.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testGetOptions() {
        List options = commandLine.getOptions();
        assertEquals(Arrays.asList(option1, option2), options);
    }

    @Test
    public void testGetOptionTriggers() {
        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(new HashSet<>(Arrays.asList("option1", "option2")), optionTriggers);
    }

    @Test
    public void testSetDefaultValues() {
        List defaultValues = Collections.singletonList("default");
        commandLine.setDefaultValues(option1, defaultValues);

        List values = commandLine.getValues(option1, Collections.emptyList());
        assertEquals(defaultValues, values);
    }

    @Test
    public void testSetDefaultSwitch() {
        Boolean defaultSwitch = Boolean.TRUE;
        commandLine.setDefaultSwitch(option1, defaultSwitch);

        Boolean switchValue = commandLine.getSwitch(option1, null);
        assertEquals(defaultSwitch, switchValue);
    }

    @Test
    public void testGetNormalised() {
        String argument = "argument";
        commandLine.addValue(argument);

        List normalised = commandLine.getNormalised();
        assertEquals(Collections.singletonList(argument), normalised);
    }
}