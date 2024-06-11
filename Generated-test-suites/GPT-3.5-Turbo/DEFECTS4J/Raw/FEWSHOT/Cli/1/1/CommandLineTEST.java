
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CommandLineTest {
    @Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        
        assertFalse(cmd.hasOption("-a"));
        
        Option option = new Option("a", "test");
        cmd.addOption(option);
        
        assertTrue(cmd.hasOption("-a"));
        assertTrue(cmd.hasOption("test"));
    }
    
    @Test
    public void testGetOptionObject() {
        CommandLine cmd = new CommandLine();
        
        assertNull(cmd.getOptionObject("-a"));
        assertNull(cmd.getOptionObject("test"));
        
        Option option = new Option("a", "test");
        option.setType(Integer.class);
        option.addValue("1");
        cmd.addOption(option);
        
        assertEquals(1, cmd.getOptionObject("-a"));
        assertEquals(1, cmd.getOptionObject("test"));
    }
    
    @Test
    public void testGetOptionValue() {
        CommandLine cmd = new CommandLine();
        
        assertNull(cmd.getOptionValue("-a"));
        assertNull(cmd.getOptionValue("test"));
        
        Option option = new Option("a", "test");
        option.addValue("value");
        cmd.addOption(option);
        
        assertEquals("value", cmd.getOptionValue("-a"));
        assertEquals("value", cmd.getOptionValue("test"));
    }
    
    @Test
    public void testGetOptionValues() {
        CommandLine cmd = new CommandLine();
        
        assertNull(cmd.getOptionValues("-a"));
        assertNull(cmd.getOptionValues("test"));
        
        Option option = new Option("a", "test");
        option.addValue("value1");
        option.addValue("value2");
        cmd.addOption(option);
        
        String[] values = cmd.getOptionValues("-a");
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
        
        values = cmd.getOptionValues("test");
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
    }
    
    @Test
    public void testGetOptionValueWithDefault() {
        CommandLine cmd = new CommandLine();
        
        assertEquals("default", cmd.getOptionValue("-a", "default"));
        assertEquals("default", cmd.getOptionValue("test", "default"));
        
        Option option = new Option("a", "test");
        option.addValue("value");
        cmd.addOption(option);
        
        assertEquals("value", cmd.getOptionValue("-a", "default"));
        assertEquals("value", cmd.getOptionValue("test", "default"));
    }
    
    @Test
    public void testGetArgs() {
        CommandLine cmd = new CommandLine();
        
        String[] args = cmd.getArgs();
        assertEquals(0, args.length);
        
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        
        args = cmd.getArgs();
        assertEquals(2, args.length);
        assertEquals("arg1", args[0]);
        assertEquals("arg2", args[1]);
    }
    
    @Test
    public void testGetArgList() {
        CommandLine cmd = new CommandLine();
        
        List argList = cmd.getArgList();
        assertEquals(0, argList.size());
        
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        
        argList = cmd.getArgList();
        assertEquals(2, argList.size());
        assertEquals("arg1", argList.get(0));
        assertEquals("arg2", argList.get(1));
    }
    
    @Test
    public void testIterator() {
        CommandLine cmd = new CommandLine();
        
        assertFalse(cmd.iterator().hasNext());
        
        Option option1 = new Option("a", "test1");
        Option option2 = new Option("b", "test2");
        Option option3 = new Option("c", "test3");
        cmd.addOption(option1);
        cmd.addOption(option2);
        cmd.addOption(option3);
        
        Iterator<Option> iterator = cmd.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(option1, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(option2, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(option3, iterator.next());
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testGetOptions() {
        CommandLine cmd = new CommandLine();
        
        Option[] options = cmd.getOptions();
        assertEquals(0, options.length);
        
        Option option1 = new Option("a", "test1");
        Option option2 = new Option("b", "test2");
        Option option3 = new Option("c", "test3");
        cmd.addOption(option1);
        cmd.addOption(option2);
        cmd.addOption(option3);
        
        options = cmd.getOptions();
        assertEquals(3, options.length);
        assertEquals(option1, options[0]);
        assertEquals(option2, options[1]);
        assertEquals(option3, options[2]);
    }
}
