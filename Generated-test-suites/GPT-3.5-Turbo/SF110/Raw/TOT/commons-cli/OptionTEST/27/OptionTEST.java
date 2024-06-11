import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class OptionTest {
    private Option option;
    
    @Before
    public void setUp() {
        option = new Option("t", true, "Test option");
    }
    
    @After
    public void tearDown() {
        option = null;
    }
    
    @Test
    public void testGetOpt() {
        assertEquals("t", option.getOpt());
    }
    
    @Test
    public void testGetLongOpt() {
        assertNull(option.getLongOpt());
    }
    
    @Test
    public void testGetArgName() {
        assertEquals("arg", option.getArgName());
    }
    
    @Test
    public void testSetArgName() {
        option.setArgName("value");
        assertEquals("value", option.getArgName());
    }
    
    @Test
    public void testHasArgName() {
        assertFalse(option.hasArgName());
    }
    
    @Test
    public void testHasLongOpt() {
        assertFalse(option.hasLongOpt());
    }
    
    @Test
    public void testIsRequired() {
        assertFalse(option.isRequired());
    }
    
    @Test
    public void testSetRequired() {
        option.setRequired(true);
        assertTrue(option.isRequired());
    }
    
    @Test
    public void testGetDescription() {
        assertEquals("Test option", option.getDescription());
    }
    
    @Test
    public void testSetDescription() {
        option.setDescription("New description");
        assertEquals("New description", option.getDescription());
    }
    
    @Test
    public void testHasArg() {
        assertTrue(option.hasArg());
    }
    
    @Test
    public void testHasArgs() {
        assertFalse(option.hasArgs());
    }
    
    @Test
    public void testSetArgs() {
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }
    
    @Test
    public void testGetValueSeparator() {
        assertEquals('\0', option.getValueSeparator());
    }
    
    @Test
    public void testSetOptionalArg() {
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }
    
    @Test
    public void testSetValueSeparator() {
        option.setValueSeparator(',');
        assertEquals(',', option.getValueSeparator());
    }
    
    @Test
    public void testEqualsSameObject() {
        assertTrue(option.equals(option));
    }
    
    @Test
    public void testEqualsNull() {
        assertFalse(option.equals(null));
    }
    
    @Test
    public void testEqualsDifferentClass() {
        assertFalse(option.equals(new Object()));
    }
    
    @Test
    public void testEqualsDifferentOpt() {
        Option other = new Option("o", true, "Other option");
        assertFalse(option.equals(other));
    }
    
    @Test
    public void testEqualsDifferentLongOpt() {
        Option other = new Option("t", "test", true, "Other option");
        assertFalse(option.equals(other));
    }
    
    @Test
    public void testHashCodeSameObject() {
        assertEquals(option.hashCode(), option.hashCode());
    }
    
    @Test
    public void testHashCodeDifferentObject() {
        Option other = new Option("t", true, "Test option");
        assertEquals(option.hashCode(), other.hashCode());
    }
    
    @Test
    public void testClone() {
        Option clone = (Option) option.clone();
        assertEquals(option.getOpt(), clone.getOpt());
        assertEquals(option.getDescription(), clone.getDescription());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        option.addValue("value");
    }
    
    @Test
    public void testAddValueForProcessing() {
        option.setArgName("value");
        option.addValueForProcessing("123");
        option.addValueForProcessing("456");
        String[] values = option.getValues();
        assertArrayEquals(new String[]{"123", "456"}, values);
    }
}