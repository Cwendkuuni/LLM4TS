import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.util.PropertyUtil;
        
public class WriteableCommandLineImplTest {
    
    private WriteableCommandLineImpl commandLine;
    private Option option1;
    private Option option2;
    private Argument argument;
    
    @Before
    public void setUp() {
        option1 = new Option();
        option2 = new Option();
        argument = new Argument();
        commandLine = new WriteableCommandLineImpl(option1, argumentList);
    }
    
    @Test
    public void testAddOption() {
        commandLine.addOption(option2);
        assertTrue(commandLine.hasOption(option1));
        assertTrue(commandLine.hasOption(option2));
    }
    
    @Test
    public void testAddValue() {
        commandLine.addValue(option1, "value1");
        commandLine.addValue(option1, "value2");
        assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option1, Collections.emptyList()));
    }
    
    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option1, true);
        commandLine.addSwitch(option1, true);
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option1, Boolean.FALSE));
    }
    
    @Test
    public void testHasOption() {
        assertFalse(commandLine.hasOption(option1));
        commandLine.addOption(option1);
        assertTrue(commandLine.hasOption(option1));
    }
    
    @Test
    public void testGetOption() {
        assertNull(commandLine.getOption("option1"));
        commandLine.addOption(option1);
        assertEquals(option1, commandLine.getOption("option1"));
    }
    
    @Test
    public void testGetValues() {
        commandLine.addValue(option1, "value1");
        assertEquals(Arrays.asList("value1"), commandLine.getValues(option1, Collections.emptyList()));
        assertEquals(Collections.emptyList(), commandLine.getValues(option2, Collections.emptyList()));
    }
    
    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option1, true);
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option1, Boolean.FALSE));
        assertNull(commandLine.getSwitch(option2, null));
    }
    
    @Test
    public void testAddProperty() {
        commandLine.addProperty("key1", "value1");
        commandLine.addProperty("key2", "value2");
        assertEquals("value1", commandLine.getProperty("key1", null));
        assertEquals("value2", commandLine.getProperty("key2", null));
    }
    
    @Test
    public void testGetProperty() {
        assertEquals("default", commandLine.getProperty("key", "default"));
    }
    
    @Test
    public void testGetProperties() {
        commandLine.addProperty("key1", "value1");
        commandLine.addProperty("key2", "value2");
        Set<String> properties = commandLine.getProperties();
        assertTrue(properties.contains("key1"));
        assertTrue(properties.contains("key2"));
        assertEquals(2, properties.size());
    }
    
    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-option1"));
        assertTrue(commandLine.looksLikeOption("--option1"));
        assertFalse(commandLine.looksLikeOption("value"));
    }
    
    @Test
    public void testToString() {
        argument.addPreferredPrefix("-");
        argument.setDefaultValues(Collections.singletonList("value1"));
        commandLine.addOption(argument);
        commandLine.addValue(argument, "value2");
        assertEquals("value1 value2", commandLine.toString());
    }
    
    @Test
    public void testGetOptions() {
        assertTrue(commandLine.getOptions().isEmpty());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertEquals(Arrays.asList(option1, option2), commandLine.getOptions());
    }
    
    @Test
    public void testGetOptionTriggers() {
        assertTrue(commandLine.getOptionTriggers().isEmpty());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertEquals(new HashSet<>(Arrays.asList(option1.getPreferredName(), option2.getPreferredName())),
                commandLine.getOptionTriggers());
    }
    
    @Test
    public void testSetDefaultValues() {
        List<String> defaults = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option1, defaults);
        assertEquals(defaults, commandLine.getValues(option1, Collections.emptyList()));
        commandLine.setDefaultValues(option1, null);
        assertEquals(Collections.emptyList(), commandLine.getValues(option1, Collections.emptyList()));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option1, true);
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option1, Boolean.FALSE));
        commandLine.setDefaultSwitch(option1, null);
        assertNull(commandLine.getSwitch(option1, null));
    }
    
    @Test
    public void testGetNormalised() {
        List<String> normalised = Arrays.asList("value1", "value2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, normalised);
        assertEquals(normalised, commandLine.getNormalised());
    }
}