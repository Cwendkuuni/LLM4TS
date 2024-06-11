import org.junit.Test;
import static org.junit.Assert.*;

public class OptionImplTest {
    
    @Test
    public void testCanProcess() {
        OptionImpl option = new OptionImpl(1, true) {
            public void appendUsage(StringBuffer buffer, DisplaySetting setting, String separator) {}
            public String getPreferredName() { return null; }
            public String getDescription() { return null; }
            public Set getPrefixes() { return null; }
            public Set getTriggers() { return null; }
            public boolean canProcess(WriteableCommandLine commandLine, String argument) { return false; }
            public Option findOption(String trigger) { return null; }
            public void defaults(WriteableCommandLine commandLine) {}
        };
        
        WriteableCommandLine commandLine = null;
        ListIterator arguments = null;
        
        assertFalse(option.canProcess(commandLine, arguments));
    }
    
    @Test
    public void testToString() {
        OptionImpl option = new OptionImpl(1, true) {
            public void appendUsage(StringBuffer buffer, DisplaySetting setting, String separator) {}
            public String getPreferredName() { return null; }
            public String getDescription() { return null; }
            public Set getPrefixes() { return null; }
            public Set getTriggers() { return null; }
            public boolean canProcess(WriteableCommandLine commandLine, String argument) { return false; }
            public Option findOption(String trigger) { return null; }
            public void defaults(WriteableCommandLine commandLine) {}
        };
        
        String expected = "";
        assertEquals(expected, option.toString());
    }
    
    @Test
    public void testEquals() {
        OptionImpl option1 = new OptionImpl(1, true) {
            public void appendUsage(StringBuffer buffer, DisplaySetting setting, String separator) {}
            public String getPreferredName() { return null; }
            public String getDescription() { return null; }
            public Set getPrefixes() { return null; }
            public Set getTriggers() { return null; }
            public boolean canProcess(WriteableCommandLine commandLine, String argument) { return false; }
            public Option findOption(String trigger) { return null; }
            public void defaults(WriteableCommandLine commandLine) {}
        };
        
        OptionImpl option2 = new OptionImpl(2, false) {
            public void appendUsage(StringBuffer buffer, DisplaySetting setting, String separator) {}
            public String getPreferredName() { return null; }
            public String getDescription() { return null; }
            public Set getPrefixes() { return null; }
            public Set getTriggers() { return null; }
            public boolean canProcess(WriteableCommandLine commandLine, String argument) { return false; }
            public Option findOption(String trigger) { return null; }
            public void defaults(WriteableCommandLine commandLine) {}
        };
        
        assertFalse(option1.equals(option2));
        assertFalse(option2.equals(option1));
    }
    
    @Test
    public void testHashCode() {
        OptionImpl option = new OptionImpl(1, true) {
            public void appendUsage(StringBuffer buffer, DisplaySetting setting, String separator) {}
            public String getPreferredName() { return null; }
            public String getDescription() { return null; }
            public Set getPrefixes() { return null; }
            public Set getTriggers() { return null; }
            public boolean canProcess(WriteableCommandLine commandLine, String argument) { return false; }
            public Option findOption(String trigger) { return null; }
            public void defaults(WriteableCommandLine commandLine) {}
        };
        
        int expected = 37 + 1;
        assertEquals(expected, option.hashCode());
    }
    
    @Test
    public void testFindOption() {
        OptionImpl option = new OptionImpl(1, true) {
            public void appendUsage(StringBuffer buffer, DisplaySetting setting, String separator) {}
            public String getPreferredName() { return null; }
            public String getDescription() { return null; }
            public Set getPrefixes() { return null; }
            public Set getTriggers() { return null; }
            public boolean canProcess(WriteableCommandLine commandLine, String argument) { return false; }
            public Option findOption(String trigger) { return null; }
            public void defaults(WriteableCommandLine commandLine) {}
        };
        
        String trigger = null;
        Option expected = null;
        assertEquals(expected, option.findOption(trigger));
    }
    
    @Test
    public void testIsRequired() {
        OptionImpl option = new OptionImpl(1, true) {
            public void appendUsage(StringBuffer buffer, DisplaySetting setting, String separator) {}
            public String getPreferredName() { return null; }
            public String getDescription() { return null; }
            public Set getPrefixes() { return null; }
            public Set getTriggers() { return null; }
            public boolean canProcess(WriteableCommandLine commandLine, String argument) { return false; }
            public Option findOption(String trigger) { return null; }
            public void defaults(WriteableCommandLine commandLine) {}
        };
        
        assertTrue(option.isRequired());
    }
    
    @Test
    public void testDefaults() {
        OptionImpl option = new OptionImpl(1, true) {
            public void appendUsage(StringBuffer buffer, DisplaySetting setting, String separator) {}
            public String getPreferredName() { return null; }
            public String getDescription() { return null; }
            public Set getPrefixes() { return null; }
            public Set getTriggers() { return null; }
            public boolean canProcess(WriteableCommandLine commandLine, String argument) { return false; }
            public Option findOption(String trigger) { return null; }
            public void defaults(WriteableCommandLine commandLine) {}
        };
        
        WriteableCommandLine commandLine = null;
        option.defaults(commandLine);
    }
    
    @Test
    public void testCheckPrefixes() {
        OptionImpl option = new OptionImpl(1, true) {
            public void appendUsage(StringBuffer buffer, DisplaySetting setting, String separator) {}
            public String getPreferredName() { return null; }
            public String getDescription() { return null; }
            public Set getPrefixes() { return null; }
            public Set getTriggers() { return null; }
            public boolean canProcess(WriteableCommandLine commandLine, String argument) { return false; }
            public Option findOption(String trigger) { return null; }
            public void defaults(WriteableCommandLine commandLine) {}
        };
        
        Set prefixes = null;
        option.checkPrefixes(prefixes);
    }
    
    @Test
    public void testCheckPrefix() {
        OptionImpl option = new OptionImpl(1, true) {
            public void appendUsage(StringBuffer buffer, DisplaySetting setting, String separator) {}
            public String getPreferredName() { return null; }
            public String getDescription() { return null; }
            public Set getPrefixes() { return null; }
            public Set getTriggers() { return null; }
            public boolean canProcess(WriteableCommandLine commandLine, String argument) { return false; }
            public Option findOption(String trigger) { return null; }
            public void defaults(WriteableCommandLine commandLine) {}
        };
        
        Set prefixes = null;
        String trigger = null;
        option.checkPrefix(prefixes, trigger);
    }
}