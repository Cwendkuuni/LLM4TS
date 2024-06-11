import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    
    @Test
    public void testAddOption() {
        Option option = new Option.Builder("testOption").create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, new ArrayList());
        
        cmd.addOption(option);
        
        Assert.assertTrue(cmd.hasOption(option));
    }
    
    @Test
    public void testAddValue() {
        Argument argument = new Argument.Builder("testArg").create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(argument, new ArrayList());
        
        String value = "testValue";
        cmd.addValue(argument, value);
        
        Assert.assertEquals(value, cmd.getValues(argument, null).get(0));
    }
    
    @Test
    public void testAddSwitch() {
        Option option = new Option.Builder("testOption").create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, new ArrayList());
        
        cmd.addSwitch(option, true);
        
        Assert.assertTrue(cmd.getSwitch(option, null));
    }
    
    @Test
    public void testHasOption() {
        Option option = new Option.Builder("testOption").create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, new ArrayList());
        
        Assert.assertFalse(cmd.hasOption(option));
        
        cmd.addOption(option);
        
        Assert.assertTrue(cmd.hasOption(option));
    }
    
    @Test
    public void testGetOption() {
        Option option = new Option.Builder("testOption").create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, new ArrayList());
        
        Assert.assertEquals(option, cmd.getOption("testOption"));
        Assert.assertNull(cmd.getOption("invalidOption"));
    }
    
    @Test
    public void testGetValues() {
        Option option = new Option.Builder("testOption").create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, new ArrayList());
        
        String value = "testValue";
        cmd.addValue(option, value);
        
        Assert.assertEquals(value, cmd.getValues(option, null).get(0));
    }
    
    @Test
    public void testGetUndefaultedValues() {
        Argument argument = new Argument.Builder("testArg").create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(argument, new ArrayList());
        
        String value = "testValue";
        cmd.addValue(argument, value);
        
        Assert.assertEquals(value, cmd.getUndefaultedValues(argument).get(0));
    }
    
    @Test
    public void testGetSwitch() {
        Option option = new Option.Builder("testOption").create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, new ArrayList());
        
        cmd.addSwitch(option, true);
        
        Assert.assertTrue(cmd.getSwitch(option, false));
    }
    
    @Test
    public void testGetProperty() {
        Option option = new Option.Builder("testOption").create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, new ArrayList());
        
        String property = "testProperty";
        String value = "testValue";
        cmd.addProperty(option, property, value);
        
        Assert.assertEquals(value, cmd.getProperty(option, property, null));
    }
    
    @Test
    public void testAddProperty() {
        Option option = new Option.Builder("testOption").create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, new ArrayList());
        
        String property = "testProperty";
        String value = "testValue";
        cmd.addProperty(option, property, value);
        
        Assert.assertEquals(value, cmd.getProperty(option, property, null));
    }
    
    @Test
    public void testGetProperties() {
        Option option = new Option.Builder("testOption").create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, new ArrayList());
        
        String property = "testProperty";
        String value = "testValue";
        cmd.addProperty(option, property, value);
        
        Assert.assertTrue(cmd.getProperties(option).contains(property));
    }
    
    @Test
    public void testLooksLikeOption() {
        Option option = new Option.Builder("testOption").create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, new ArrayList());
        
        String trigger = "-testOption";
        
        Assert.assertTrue(cmd.looksLikeOption(trigger));
    }
    
    @Test
    public void testToString() {
        Option option = new Option.Builder("testOption").create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, new ArrayList());
        
        String arg1 = "arg1";
        String arg2 = "arg2";
        cmd.getNormalised().add(arg1);
        cmd.getNormalised().add(arg2);
        
        String expected = arg1 + " " + arg2;
        
        Assert.assertEquals(expected, cmd.toString());
    }
    
    @Test
    public void testGetOptions() {
        Option option1 = new Option.Builder("testOption1").create();
        Option option2 = new Option.Builder("testOption2").create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option1, new ArrayList());
        
        cmd.addOption(option1);
        cmd.addOption(option2);
        
        Assert.assertEquals(2, cmd.getOptions().size());
        Assert.assertTrue(cmd.getOptions().contains(option1));
        Assert.assertTrue(cmd.getOptions().contains(option2));
    }
    
    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option.Builder("testOption1").create();
        Option option2 = new Option.Builder("testOption2").create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option1, new ArrayList());
        
        cmd.addOption(option1);
        cmd.addOption(option2);
        
        Assert.assertEquals(2, cmd.getOptionTriggers().size());
        Assert.assertTrue(cmd.getOptionTriggers().contains("testOption1"));
        Assert.assertTrue(cmd.getOptionTriggers().contains("testOption2"));
    }
    
    @Test
    public void testSetDefaultValues() {
        Option option = new Option.Builder("testOption").create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, new ArrayList());
        
        String defaultValue = "testDefaultValue";
        cmd.setDefaultValues(option, Collections.singletonList(defaultValue));
        
        Assert.assertEquals(defaultValue, cmd.getValues(option, null).get(0));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option.Builder("testOption").create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, new ArrayList());
        
        cmd.setDefaultSwitch(option, true);
        
        Assert.assertTrue(cmd.getSwitch(option, null));
    }
    
    @Test
    public void testGetNormalised() {
        Option option = new Option.Builder("testOption").create();
        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(option, new ArrayList());
        
        String arg1 = "arg1";
        String arg2 = "arg2";
        cmd.getNormalised().add(arg1);
        cmd.getNormalised().add(arg2);
        
        Assert.assertEquals(2, cmd.getNormalised().size());
        Assert.assertEquals(arg1, cmd.getNormalised().get(0));
        Assert.assertEquals(arg2, cmd.getNormalised().get(1));
    }
    
}