
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("test", "test option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }
    
    @Test
    public void testAddValue() {
        Argument argument = new Argument("filename", "file argument");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addValue(argument, "test.txt");
        Assert.assertEquals("test.txt", commandLine.getValues(argument, null).get(0));
    }
    
    @Test
    public void testAddSwitch() {
        Option option = new Option("switch", "test switch");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testHasOption() {
        Option option = new Option("test", "test option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
        Assert.assertFalse(commandLine.hasOption(new Option("notfound", "not found option")));
    }
    
    @Test
    public void testGetOption() {
        Option option = new Option("test", "test option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOption("test"));
        Assert.assertNull(commandLine.getOption("notfound"));
    }
    
    @Test
    public void testGetValues() {
        Argument argument = new Argument("filename", "file argument");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addValue(argument, "test.txt");
        Assert.assertEquals("test.txt", commandLine.getValues(argument, null).get(0));
        Assert.assertEquals(Collections.emptyList(), commandLine.getValues(new Argument("notfound", "not found argument"), null));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Argument argument = new Argument("filename", "file argument");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addValue(argument, "test.txt");
        Assert.assertEquals("test.txt", commandLine.getUndefaultedValues(argument).get(0));
        Assert.assertEquals(Collections.emptyList(), commandLine.getUndefaultedValues(new Argument("notfound", "not found argument")));
    }
    
    @Test
    public void testGetSwitch() {
        Option option = new Option("switch", "test switch");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, null));
        Assert.assertNull(commandLine.getSwitch(new Option("notfound", "not found switch"), null));
    }
    
    @Test
    public void testGetProperty() {
        Option option = new Option("property", "test property");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addProperty(option, "key", "value");
        Assert.assertEquals("value", commandLine.getProperty(option, "key", null));
        Assert.assertNull(commandLine.getProperty(new Option("notfound", "not found property"), null));
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option = new Option("test", "test option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.setDefaultValues(option, Arrays.asList("default1", "default2"));
        Assert.assertEquals(Arrays.asList("default1", "default2"), commandLine.getValues(option, null));
        commandLine.setDefaultValues(option, null);
        Assert.assertEquals(Collections.emptyList(), commandLine.getValues(option, null));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("switch", "test switch");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.setDefaultSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, null));
        commandLine.setDefaultSwitch(option, null);
        Assert.assertNull(commandLine.getSwitch(option, null));
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option("option1", "test option 1");
        Option option2 = new Option("option2", "test option 2");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option1, Collections.emptyList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Assert.assertEquals(Arrays.asList(option1, option2), commandLine.getOptions());
    }
    
    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("option1", "test option 1");
        Option option2 = new Option("option2", "test option 2");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option1, Collections.emptyList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Assert.assertEquals(new HashSet<>(Arrays.asList("option1", "option2")), commandLine.getOptionTriggers());
    }
    
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        Assert.assertTrue(commandLine.looksLikeOption("-option"));
        Assert.assertFalse(commandLine.looksLikeOption("notanoption"));
    }
    
    @Test
    public void testToString() {
        Option option = new Option("option", "test option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        commandLine.addOption(option);
        Assert.assertEquals("arg1 arg2", commandLine.toString());
    }
    
    @Test
    public void testGetProperties() {
        Option option = new Option("property", "test property");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addProperty(option, "key1", "value1");
        commandLine.addProperty(option, "key2", "value2");
        Assert.assertEquals(new HashSet<>(Arrays.asList("key1", "key2")), commandLine.getProperties(option));
    }
    
    @Test
    public void testGetProperties() {
        Option option = new Option("property", "test property");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addProperty(option, "key1", "value1");
        commandLine.addProperty(option, "key2", "value2");
        Assert.assertEquals(new HashSet<>(Arrays.asList("key1", "key2")), commandLine.getProperties());
    }
    
    @Test
    public void testGetNormalised() {
        Option option = new Option("option", "test option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        Assert.assertEquals(Arrays.asList("arg1", "arg2"), commandLine.getNormalised());
    }
}
