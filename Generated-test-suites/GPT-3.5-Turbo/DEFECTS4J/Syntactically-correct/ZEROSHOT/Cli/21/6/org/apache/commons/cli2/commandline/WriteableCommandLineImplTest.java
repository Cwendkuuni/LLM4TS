import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option);
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testAddValue() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        commandLine.addValue(option, "value3");
        assertEquals(commandLine.getValues(option), Arrays.asList("value1", "value2", "value3"));
    }
    
    @Test
    public void testAddSwitch() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }
    
    @Test
    public void testHasOption() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option);
        assertFalse(commandLine.hasOption(option));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option);
        assertNull(commandLine.getOption("test"));
        commandLine.addOption(option);
        assertEquals(commandLine.getOption("test"), option);
    }
    
    @Test
    public void testGetValues() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        commandLine.addValue(option, "value3");
        assertEquals(commandLine.getValues(option), Arrays.asList("value1", "value2", "value3"));
        assertEquals(commandLine.getValues(option, Arrays.asList("default")), Arrays.asList("value1", "value2", "value3"));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        commandLine.addValue(option, "value3");
        assertEquals(commandLine.getUndefaultedValues(option), Arrays.asList("value1", "value2", "value3"));
    }
    
    @Test
    public void testGetSwitch() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option);
        assertNull(commandLine.getSwitch(option, false));
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }
    
    @Test
    public void testGetProperty() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option);
        assertNull(commandLine.getProperty("property"));
        commandLine.addProperty("property", "value");
        assertEquals(commandLine.getProperty("property"), "value");
    }
    
    @Test
    public void testAddProperty() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option);
        commandLine.addProperty("property", "value");
        assertEquals(commandLine.getProperty("property"), "value");
    }
    
    @Test
    public void testGetPropertyWithDefault() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option);
        assertNull(commandLine.getProperty(option, "property", "default"));
        commandLine.addProperty(option, "property", "value");
        assertEquals(commandLine.getProperty(option, "property", "default"), "value");
    }
    
    @Test
    public void testGetProperties() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option);
        assertTrue(commandLine.getProperties().isEmpty());
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        assertEquals(commandLine.getProperties(), Arrays.asList("property1", "property2"));
    }
    
    @Test
    public void testLooksLikeOption() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option);
        assertTrue(commandLine.looksLikeOption("--test"));
        assertFalse(commandLine.looksLikeOption("value"));
    }
    
    @Test
    public void testToString() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option);
        assertEquals(commandLine.toString(), "");
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        commandLine.addValue(option, "value3");
        assertEquals(commandLine.toString(), "value1 value2 value3");
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("test1");
        Option option2 = new Option("test2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1);
        assertEquals(commandLine.getOptions(), Collections.singletonList(option1));
        commandLine.addOption(option2);
        assertEquals(commandLine.getOptions(), Arrays.asList(option1, option2));
    }
    
    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("test1");
        Option option2 = new Option("test2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1);
        assertEquals(commandLine.getOptionTriggers(), Collections.singleton("test1"));
        commandLine.addOption(option2);
        assertEquals(commandLine.getOptionTriggers(), new HashSet<>(Arrays.asList("test1", "test2")));
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option);
        commandLine.setDefaultValues(option, Arrays.asList("default1", "default2"));
        assertEquals(commandLine.getValues(option, Arrays.asList("default")), Arrays.asList("default1", "default2"));
        commandLine.setDefaultValues(option, null);
        assertEquals(commandLine.getValues(option, Arrays.asList("default")), Arrays.asList("default"));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option);
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
        commandLine.setDefaultSwitch(option, null);
        assertFalse(commandLine.getSwitch(option, false));
    }
    
    @Test
    public void testGetNormalised() {
        Option option = new Option("test");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option);
        assertEquals(commandLine.getNormalised(), Collections.emptyList());
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        commandLine.addValue(option, "value3");
        assertEquals(commandLine.getNormalised(), Arrays.asList("value1", "value2", "value3"));
    }
}