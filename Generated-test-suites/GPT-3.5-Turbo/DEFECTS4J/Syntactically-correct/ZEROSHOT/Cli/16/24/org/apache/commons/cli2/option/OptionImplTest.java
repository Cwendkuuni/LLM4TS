import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.HashSet;
import java.util.Set;
import java.util.ListIterator;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;

public class OptionImplTest {
    
    private OptionImpl option;
    
    @Before
    public void setUp() {
        option = Mockito.mock(OptionImpl.class, Mockito.CALLS_REAL_METHODS);
    }
    
    @Test
    public void testCanProcess() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        ListIterator arguments = Mockito.mock(ListIterator.class);
        Mockito.when(arguments.hasNext()).thenReturn(true);
        Mockito.when(arguments.next()).thenReturn("test");
        Mockito.when(arguments.previous()).thenReturn(null);
        
        boolean result = option.canProcess(commandLine, arguments);
        
        assertTrue(result);
        Mockito.verify(option).canProcess(commandLine, "test");
        Mockito.verify(arguments).previous();
    }
    
    @Test
    public void testToString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Test");
        
        Mockito.when(option.appendUsage(Mockito.any(StringBuffer.class), Mockito.eq(DisplaySetting.ALL), Mockito.any())).thenCallRealMethod();
        Mockito.when(option.toString()).thenReturn("Test");
        
        String result = option.toString();
        
        assertEquals(buffer.toString(), result);
        Mockito.verify(option).appendUsage(Mockito.any(StringBuffer.class), Mockito.eq(DisplaySetting.ALL), Mockito.any());
    }
    
    @Test
    public void testEquals() {
        OptionImpl that = Mockito.mock(OptionImpl.class);
        
        Mockito.when(option.getId()).thenReturn(1);
        Mockito.when(option.getPreferredName()).thenReturn("test");
        Mockito.when(option.getDescription()).thenReturn("description");
        Mockito.when(option.getPrefixes()).thenReturn(new HashSet<>());
        Mockito.when(option.getTriggers()).thenReturn(new HashSet<>());
        
        Mockito.when(that.getId()).thenReturn(1);
        Mockito.when(that.getPreferredName()).thenReturn("test");
        Mockito.when(that.getDescription()).thenReturn("description");
        Mockito.when(that.getPrefixes()).thenReturn(new HashSet<>());
        Mockito.when(that.getTriggers()).thenReturn(new HashSet<>());
        
        boolean result = option.equals(that);
        
        assertTrue(result);
        Mockito.verify(option, Mockito.times(2)).getId();
        Mockito.verify(option, Mockito.times(2)).getPreferredName();
        Mockito.verify(option, Mockito.times(2)).getDescription();
        Mockito.verify(option, Mockito.times(2)).getPrefixes();
        Mockito.verify(option, Mockito.times(2)).getTriggers();
    }
    
    @Test
    public void testHashCode() {
        Set<String> prefixes = new HashSet<>();
        prefixes.add("prefix");
        Set<String> triggers = new HashSet<>();
        triggers.add("trigger");
        
        Mockito.when(option.getId()).thenReturn(1);
        Mockito.when(option.getPreferredName()).thenReturn("test");
        Mockito.when(option.getDescription()).thenReturn("description");
        Mockito.when(option.getPrefixes()).thenReturn(prefixes);
        Mockito.when(option.getTriggers()).thenReturn(triggers);
        
        int result = option.hashCode();
        
        int expectedHashCode = 1;
        expectedHashCode = (expectedHashCode * 37) + "test".hashCode();
        expectedHashCode = (expectedHashCode * 37) + "description".hashCode();
        expectedHashCode = (expectedHashCode * 37) + prefixes.hashCode();
        expectedHashCode = (expectedHashCode * 37) + triggers.hashCode();
        
        assertEquals(expectedHashCode, result);
        Mockito.verify(option, Mockito.times(2)).getId();
        Mockito.verify(option, Mockito.times(2)).getPreferredName();
        Mockito.verify(option, Mockito.times(2)).getDescription();
        Mockito.verify(option, Mockito.times(2)).getPrefixes();
        Mockito.verify(option, Mockito.times(2)).getTriggers();
    }
    
    @Test
    public void testFindOption() {
        Option result = option.findOption("trigger");
        
        assertEquals(option, result);
        Mockito.verify(option).getTriggers();
    }
    
    @Test
    public void testIsRequired() {
        boolean result = option.isRequired();
        
        assertFalse(result);
    }
    
    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        
        option.defaults(commandLine);
        
        Mockito.verify(option).defaults(commandLine);
    }
    
    @Test
    public void testCheckPrefixes() {
        Set<String> prefixes = new HashSet<>();
        prefixes.add("prefix");
        
        Mockito.when(option.getPreferredName()).thenReturn("test");
        Mockito.when(option.getTriggers()).thenReturn(new HashSet<>());
        
        option.checkPrefixes(prefixes);
        
        Mockito.verify(option).checkPrefixes(prefixes);
        Mockito.verify(option).getPreferredName();
        Mockito.verify(option).getTriggers();
    }
    
    @Test
    public void testCheckPrefix() {
        Set<String> prefixes = new HashSet<>();
        prefixes.add("prefix");
        
        Mockito.when(option.checkPrefix(Mockito.eq(prefixes), Mockito.eq("trigger"))).thenCallRealMethod();
        
        option.checkPrefix(prefixes, "trigger");
        
        Mockito.verify(option).checkPrefix(prefixes, "trigger");
    }
}