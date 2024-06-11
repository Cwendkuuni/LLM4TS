import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GroupImplTest {
    
    private GroupImpl group;
    
    @Before
    public void setup() {
        group = new GroupImpl(new ArrayList(), "testGroup", "Test group", 0, 1, false);
    }

    @Test
    public void testCanProcessOption() {
        assertEquals(true, group.canProcess(null, "test"));
    }
    
    @Test
    public void testGetPrefixes() {
        assertEquals(Collections.emptySet(), group.getPrefixes());
    }
    
    @Test
    public void testGetTriggers() {
        assertEquals(Collections.emptySet(), group.getTriggers());
    }
    
    @Test
    public void testProcess() {
        WriteableCommandLine commandLine = null;
        ListIterator arguments = null;
        try {
            group.process(commandLine, arguments);
        } catch (OptionException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testValidate() {
        WriteableCommandLine commandLine = null;
        try {
            group.validate(commandLine);
        } catch (OptionException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testGetPreferredName() {
        assertEquals("testGroup", group.getPreferredName());
    }
    
    @Test
    public void testGetDescription() {
        assertEquals("Test group", group.getDescription());
    }
    
    @Test
    public void testAppendUsage() {
        StringBuffer buffer = new StringBuffer();
        Set helpSettings = Collections.emptySet();
        Comparator comp = null;
        group.appendUsage(buffer, helpSettings, comp);
        assertEquals("", buffer.toString());
    }
    
    @Test
    public void testGetOptions() {
        assertEquals(Collections.emptyList(), group.getOptions());
    }
    
    @Test
    public void testGetAnonymous() {
        assertEquals(Collections.emptyList(), group.getAnonymous());
    }
    
    @Test
    public void testFindOption() {
        assertNull(group.findOption("test"));
    }
    
    @Test
    public void testGetMinimum() {
        assertEquals(0, group.getMinimum());
    }
    
    @Test
    public void testGetMaximum() {
        assertEquals(1, group.getMaximum());
    }
    
    @Test
    public void testIsRequired() {
        assertEquals(false, group.isRequired());
    }
    
    @Test
    public void testDefaults() {
        WriteableCommandLine commandLine = null;
        group.defaults(commandLine);
    }
}