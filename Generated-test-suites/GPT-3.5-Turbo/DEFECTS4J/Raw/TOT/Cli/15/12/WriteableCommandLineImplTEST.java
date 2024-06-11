import org.apache.commons.cli2.option.ArgumentImpl;
import org.apache.commons.cli2.option.CommandImpl;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option1 = new ArgumentImpl("-s", Collections.emptyList(), "", 1, Integer.MAX_VALUE, false, ResourceHelper.getResourceHelper().getMessage(ResourceConstants.ARGUMENT_DESC));
        Option option2 = new CommandImpl("-r");
        Option option3 = new CommandImpl("-a");
        commandLine = new WriteableCommandLineImpl(option1, Arrays.asList("-s", "value1", "-r", "-a"));
        commandLine.addOption(option2);
        commandLine.addOption(option3);
        commandLine.addSwitch(option2, true);
        commandLine.addSwitch(option3, false);
    }

    @Test
    public void testAddOption() {
        Option option = new ArgumentImpl("-t", Collections.emptyList(), "", 1, 1, false, ResourceHelper.getResourceHelper().getMessage(ResourceConstants.ARGUMENT_DESC));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new ArgumentImpl("-t", Collections.emptyList(), "", 1, Integer.MAX_VALUE, false, ResourceHelper.getResourceHelper().getMessage(ResourceConstants.ARGUMENT_DESC));
        commandLine.addValue(option, "value1");
        List<String> values = commandLine.getValues(option, Collections.emptyList());
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new CommandImpl("-t");
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.hasOption(option));
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option1 = new ArgumentImpl("-s", Collections.emptyList(), "", 1, 1, false, ResourceHelper.getResourceHelper().getMessage(ResourceConstants.ARGUMENT_DESC));
        Option option2 = new CommandImpl("-r");
        assertTrue(commandLine.hasOption(option1));
        assertTrue(commandLine.hasOption(option2));
        assertFalse(commandLine.hasOption(new ArgumentImpl("-t", Collections.emptyList(), "", 1, 1, false, ResourceHelper.getResourceHelper().getMessage(ResourceConstants.ARGUMENT_DESC))));
    }

    @Test
    public void testGetOption() {
        Option option1 = new ArgumentImpl("-s", Collections.emptyList(), "", 1, 1, false, ResourceHelper.getResourceHelper().getMessage(ResourceConstants.ARGUMENT_DESC));
        Option option2 = new CommandImpl("-r");
        assertEquals(option1, commandLine.getOption("-s"));
        assertEquals(option2, commandLine.getOption("-r"));
        assertNull(commandLine.getOption("-t"));
    }

    @Test
    public void testGetValues() {
        Option option = new ArgumentImpl("-s", Collections.emptyList(), "", 1, Integer.MAX_VALUE, false, ResourceHelper.getResourceHelper().getMessage(ResourceConstants.ARGUMENT_DESC));
        List<String> values = commandLine.getValues(option, Collections.emptyList());
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new ArgumentImpl("-s", Collections.emptyList(), "", 1, Integer.MAX_VALUE, false, ResourceHelper.getResourceHelper().getMessage(ResourceConstants.ARGUMENT_DESC));
        List<String> values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option1 = new CommandImpl("-r");
        Option option2 = new CommandImpl("-t");
        assertTrue(commandLine.getSwitch(option1, false));
        assertFalse(commandLine.getSwitch(option2, true));
        assertTrue(commandLine.getSwitch(option2, true));
    }

    @Test
    public void testGetProperty() {
        Option option = new PropertyOption();
        commandLine.addProperty(option, "key1", "value1");
        String value = commandLine.getProperty(option, "key1", "");
        assertEquals("value1", value);
    }

    @Test
    public void testAddProperty() {
        Option option = new PropertyOption();
        commandLine.addProperty(option, "key1", "value1");
        assertEquals("value1", commandLine.getProperty(option, "key1", ""));
    }

    @Test
    public void testGetPropertyDefaultValue() {
        Option option = new PropertyOption();
        String value = commandLine.getProperty(option, "key2", "default");
        assertEquals("default", value);
    }

    @Test
    public void testGetProperties() {
        Option option = new PropertyOption();
        commandLine.addProperty(option, "key1", "value1");
        commandLine.addProperty(option, "key2", "value2");
        assertEquals(2, commandLine.getProperties(option).size());
    }

    @Test
    public void testGetPropertiesEmpty() {
        Option option = new PropertyOption();
        assertEquals(0, commandLine.getProperties(option).size());
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-s"));
        assertFalse(commandLine.looksLikeOption("value1"));
    }

    @Test
    public void testToString() {
        assertEquals("-s value1 -r -a", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        assertEquals(3, commandLine.getOptions().size());
    }

    @Test
    public void testGetOptionTriggers() {
        assertEquals(3, commandLine.getOptionTriggers().size());
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new ArgumentImpl("-s", Collections.emptyList(), "", 1, Integer.MAX_VALUE, false, ResourceHelper.getResourceHelper().getMessage(ResourceConstants.ARGUMENT_DESC));
        List<String> defaults = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaults);
        List<String> values = commandLine.getValues(option, Collections.emptyList());
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option1 = new CommandImpl("-r");
        Option option2 = new CommandImpl("-t");
        commandLine.setDefaultSwitch(option1, false);
        commandLine.setDefaultSwitch(option2, true);
        assertFalse(commandLine.getSwitch(option1, true));
        assertTrue(commandLine.getSwitch(option2, false));
    }

    @Test
    public void testGetNormalised() {
        assertEquals(Arrays.asList("-s", "value1", "-r", "-a"), commandLine.getNormalised());
    }
}