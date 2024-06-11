import org.junit.Test;

import static org.junit.Assert.*;

public class OptionImplTest {

    // Test for canProcess method when command line and arguments are not empty
    @Test
    public void testCanProcessNotEmpty() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> arguments = Arrays.asList("testArgument").listIterator();
        
        OptionImpl option = new OptionImpl(1, false);
        
        assertTrue(option.canProcess(commandLine, arguments));
    }
    
    // Test for canProcess method when command line is not empty but arguments are empty
    @Test
    public void testCanProcessEmptyArguments() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> arguments = Arrays.asList().listIterator();
        
        OptionImpl option = new OptionImpl(1, false);
        
        assertFalse(option.canProcess(commandLine, arguments));
    }
    
    // Test for canProcess method when command line and arguments are empty
    @Test
    public void testCanProcessEmptyCommandLineAndArguments() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        ListIterator<String> arguments = Arrays.asList().listIterator();
        
        OptionImpl option = new OptionImpl(1, false);
        
        assertFalse(option.canProcess(commandLine, arguments));
    }
    
    // Test for toString method
    @Test
    public void testToString() {
        OptionImpl option = new OptionImpl(1, false);
        
        assertEquals("", option.toString());
    }
    
    // Test for equals method when the objects are equal
    @Test
    public void testEqualsSameObject() {
        OptionImpl option1 = new OptionImpl(1, false);
        OptionImpl option2 = new OptionImpl(1, false);
        
        assertTrue(option1.equals(option2));
    }
    
    // Test for equals method when the objects are not equal
    @Test
    public void testEqualsDifferentObject() {
        OptionImpl option1 = new OptionImpl(1, false);
        OptionImpl option2 = new OptionImpl(2, true);
        
        assertFalse(option1.equals(option2));
    }
    
    // Test for findOption method when the trigger exists
    @Test
    public void testFindOptionTriggerExists() {
        OptionImpl option = new OptionImpl(1, false);
        option.addTrigger("testTrigger");
        
        assertEquals(option, option.findOption("testTrigger"));
    }
    
    // Test for findOption method when the trigger doesn't exist
    @Test
    public void testFindOptionTriggerNotExists() {
        OptionImpl option = new OptionImpl(1, false);
        option.addTrigger("testTrigger");
        
        assertNull(option.findOption("invalidTrigger"));
    }
    
    // Test for isRequired method when the option is required
    @Test
    public void testIsRequired() {
        OptionImpl option = new OptionImpl(1, true);
        
        assertTrue(option.isRequired());
    }
    
    // Test for defaults method when the command line is empty
    @Test
    public void testDefaultsEmptyCommandLine() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl();
        
        OptionImpl option = new OptionImpl(1, false);
        
        option.defaults(commandLine);
        
        // Assert that nothing has changed in the command line
        assertEquals(commandLine, new WriteableCommandLineImpl());
    }
    
    // Test for checkPrefixes method when prefixes are empty
    @Test
    public void testCheckPrefixesEmpty() {
        OptionImpl option = new OptionImpl(1, false);
        option.addTrigger("testTrigger");
        
        // No exception should be thrown when prefixes are empty
        option.checkPrefixes(new HashSet<>());
    }
    
    // Test for checkPrefixes method when prefixes do not match any trigger
    @Test(expected = IllegalArgumentException.class)
    public void testCheckPrefixesPrefixesDoNotMatch() {
        OptionImpl option = new OptionImpl(1, false);
        option.addTrigger("testTrigger");
        
        option.checkPrefixes(new HashSet<>(Arrays.asList("prefix1", "prefix2")));
    }
    
    // Test for checkPrefixes method when prefixes match the preferred name
    @Test
    public void testCheckPrefixesPrefixesMatchPreferredName() {
        OptionImpl option = new OptionImpl(1, false);
        option.setPreferredName("testTrigger");
        
        // No exception should be thrown if any prefix matches the preferred name
        option.checkPrefixes(new HashSet<>(Arrays.asList("pre", "t")));
    }
    
    // Test for checkPrefixes method when prefixes match any trigger
    @Test
    public void testCheckPrefixesPrefixesMatchAnyTrigger() {
        OptionImpl option = new OptionImpl(1, false);
        option.addTrigger("testTrigger");
        
        // No exception should be thrown if any prefix matches any trigger
        option.checkPrefixes(new HashSet<>(Arrays.asList("pre", "t")));
    }
}