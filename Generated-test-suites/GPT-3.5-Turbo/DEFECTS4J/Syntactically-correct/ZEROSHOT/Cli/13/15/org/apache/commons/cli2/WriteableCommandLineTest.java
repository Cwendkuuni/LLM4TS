import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WriteableCommandLineTest {
    
    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption");
        
        commandLine.addOption(option);
        
        Assert.assertTrue(commandLine.getOptions().contains(option));
    }
    
    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption");
        Object value = new Object();
        
        commandLine.addValue(option, value);
        
        Assert.assertEquals(value, commandLine.getValue(option));
    }
    
    @Test
    public void testGetArgumentValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption");
        Object value1 = new Object();
        Object value2 = new Object();
        List<Object> argumentValues = new ArrayList<>();
        argumentValues.add(value1);
        argumentValues.add(value2);
        commandLine.setArgumentValues(option, argumentValues);
        
        List<Object> result = commandLine.getArgumentValues(option);
        
        Assert.assertEquals(argumentValues, result);
    }
    
    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption");
        List<Object> defaultValues = new ArrayList<>();
        defaultValues.add(new Object());
        defaultValues.add(new Object());
        
        commandLine.setDefaultValues(option, defaultValues);
        
        Assert.assertEquals(defaultValues, commandLine.getDefaultValues(option));
    }
    
    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption");
        boolean value = true;
        
        commandLine.addSwitch(option, value);
        
        Assert.assertTrue(commandLine.getSwitch(option));
    }
    
    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        Option option = new Option("testOption");
        boolean defaultSwitch = false;
        
        commandLine.setDefaultSwitch(option, defaultSwitch);
        
        Assert.assertEquals(defaultSwitch, commandLine.getDefaultSwitch(option));
    }
    
    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String property = "testProperty";
        String value = "testValue";
        
        commandLine.addProperty(property, value);
        
        Assert.assertEquals(value, commandLine.getProperty(property));
    }
    
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        String argument = "-testArgument";
        
        boolean result = commandLine.looksLikeOption(argument);
        
        Assert.assertTrue(result);
    }
}