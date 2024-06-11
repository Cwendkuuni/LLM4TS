package org.apache.commons.cli;
import org.junit.Test;
import java.util.Collection;
import static org.junit.Assert.*;

public class OptionGroupTest {
    
    @Test
    public void testAddOption() {
        OptionGroup optionGroup = new OptionGroup();
        Option option = new Option("f", "file", false, "Specify a file");
        optionGroup.addOption(option);
        
        Collection<Option> options = optionGroup.getOptions();
        assertTrue(options.contains(option));
    }
    
    @Test
    public void testGetNames() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("f", "file", false, "Specify a file");
        Option option2 = new Option("d", "directory", false, "Specify a directory");
        Option option3 = new Option("o", "output", false, "Specify an output file");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        
        Collection<String> names = optionGroup.getNames();
        
        assertEquals(3, names.size());
        assertTrue(names.contains("f"));
        assertTrue(names.contains("d"));
        assertTrue(names.contains("o"));
    }
    
    @Test
    public void testGetOptions() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("f", "file", false, "Specify a file");
        Option option2 = new Option("d", "directory", false, "Specify a directory");
        Option option3 = new Option("o", "output", false, "Specify an output file");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        
        Collection<Option> options = optionGroup.getOptions();
        
        assertEquals(3, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
        assertTrue(options.contains(option3));
    }
    
    @Test
    public void testSetSelected() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("f", "file", false, "Specify a file");
        Option option2 = new Option("d", "directory", false, "Specify a directory");
        Option option3 = new Option("o", "output", false, "Specify an output file");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        
        optionGroup.setSelected(option1);
        assertEquals("f", optionGroup.getSelected());
        
        optionGroup.setSelected(option2);
        assertEquals("d", optionGroup.getSelected());
        
        optionGroup.setSelected(null);
        assertNull(optionGroup.getSelected());
    }
    
    @Test(expected = AlreadySelectedException.class)
    public void testSetSelectedThrowsAlreadySelectedException() throws AlreadySelectedException {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("f", "file", false, "Specify a file");
        Option option2 = new Option("d", "directory", false, "Specify a directory");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        
        optionGroup.setSelected(option1);
        optionGroup.setSelected(option2);
    }
    
    @Test
    public void testToString() {
        OptionGroup optionGroup = new OptionGroup();
        Option option1 = new Option("f", "file", false, "Specify a file");
        Option option2 = new Option("d", "directory", false, "Specify a directory");
        Option option3 = new Option("o", "output", false, "Specify an output file");
        optionGroup.addOption(option1);
        optionGroup.addOption(option2);
        optionGroup.addOption(option3);
        
        String expected = "[ -f Specify a file, -d Specify a directory, -o Specify an output file ]";
        assertEquals(expected, optionGroup.toString());
    }
    
}