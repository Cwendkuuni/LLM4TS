import static org.junit.Assert.*;
import org.junit.Test;

public class JCLOTest {
    
    // Test constructor with prefix, object, and aliases
    @Test
    public void testConstructorWithPrefixObjectAndAliases() {
        JCLO jclo = new JCLO("prefix", new Object(), new String[][]{{"alias1", "value1"}, {"alias2", "value2"}});
        assertNotNull(jclo);
    }
    
    // Test getObject method
    @Test
    public void testGetObject() {
        JCLO jclo = new JCLO(new Object());
        assertNotNull(jclo.getObject("fieldName"));
    }
    
    // Test getField method
    @Test
    public void testGetField() {
        JCLO jclo = new JCLO("prefix", new Object());
        assertNotNull(jclo.getField("fieldName"));
    }
    
    // Test getValue method
    @Test
    public void testGetValue() {
        JCLO jclo = new JCLO(new Object());
        assertNull(jclo.getValue("nonExistentField"));
    }
    
    // Test setObject method
    @Test
    public void testSetObject() {
        JCLO jclo = new JCLO(new Object());
        jclo.setObject("fieldName", "value");
        assertEquals(jclo.getValue("fieldName"), "value");
    }
    
    // Test usage method
    @Test
    public void testUsage() {
        JCLO jclo = new JCLO(new Object());
        assertEquals(jclo.usage(), "");
    }
    
    // Test parseAdditional method
    @Test
    public void testParseAdditional() {
        JCLO jclo = new JCLO("prefix", new Object());
        jclo.parseAdditional(new String[]{"arg1", "arg2"}, 1);
        assertNotNull(jclo.getValue("additional"));
    }
    
    // Test key conversion in getKey method
    @Test
    public void testGetKey() {
        JCLO jclo = new JCLO(new Object());
        assertEquals(jclo.getKey("-arg"), "arg");
    }
    
    // Test makeObject method
    @Test
    public void testMakeObject() {
        JCLO jclo = new JCLO(new Object());
        assertEquals(jclo.makeObject("java.lang.String", "value"), "value");
    }
    
    // Test parse method
    @Test
    public void testParse() {
        JCLO jclo = new JCLO(new Object());
        jclo.parse(new String[]{"-arg1", "-arg2=value2"});
        assertNotNull(jclo.getValue("arg1"));
        assertNotNull(jclo.getValue("arg2"));
    }
    
    // Test getting boolean value
    @Test
    public void testGetBooleanValue() {
        JCLO jclo = new JCLO(new Object());
        assertEquals(jclo.getBooleanValue("-arg=true"), "true");
    }
    
    // Test getting equals value
    @Test
    public void testGetEqualsValue() {
        JCLO jclo = new JCLO(new Object());
        assertEquals(jclo.getEqualsValue("-arg=value"), "value");
    }
    
    // Test getBoolean method
    @Test
    public void testGetBoolean() {
        JCLO jclo = new JCLO(new Object());
        jclo.setObject("key", true);
        assertTrue(jclo.getBoolean("key"));
    }
    
    // Test getByte method
    @Test
    public void testGetByte() {
        JCLO jclo = new JCLO(new Object());
        jclo.setObject("key", (byte)1);
        assertEquals(jclo.getByte("key"), (byte)1);
    }
    
    // Test getChar method
    @Test
    public void testGetChar() {
        JCLO jclo = new JCLO(new Object());
        jclo.setObject("key", 'a');
        assertEquals(jclo.getChar("key"), 'a');
    }
    
    // Test getShort method
    @Test
    public void testGetShort() {
        JCLO jclo = new JCLO(new Object());
        jclo.setObject("key", (short)10);
        assertEquals(jclo.getShort("key"), (short)10);
    }
    
    // Test getInt method
    @Test
    public void testGetInt() {
        JCLO jclo = new JCLO(new Object());
        jclo.setObject("key", 100);
        assertEquals(jclo.getInt("key"), 100);
    }
    
    // Test getFloat method
    @Test
    public void testGetFloat() {
        JCLO jclo = new JCLO(new Object());
        jclo.setObject("key", 10.5f);
        assertEquals(jclo.getFloat("key"), 10.5f, 0);
    }
    
    // Test getDouble method
    @Test
    public void testGetDouble() {
        JCLO jclo = new JCLO(new Object());
        jclo.setObject("key", 10.5);
        assertEquals(jclo.getDouble("key"), 10.5, 0);
    }
    
    // Test getLong method
    @Test
    public void testGetLong() {
        JCLO jclo = new JCLO(new Object());
        jclo.setObject("key", 1000L);
        assertEquals(jclo.getLong("key"), 1000L);
    }
    
    // Test getString method
    @Test
    public void testGetString() {
        JCLO jclo = new JCLO(new Object());
        jclo.setObject("key", "value");
        assertEquals(jclo.getString("key"), "value");
    }
    
    // Test getBytes method
    @Test
    public void testGetBytes() {
        JCLO jclo = new JCLO(new Object());
        jclo.setObject("key", new byte[]{1});
        assertArrayEquals(jclo.getBytes("key"), new byte[]{1});
    }
    
    // Test getChars method
    @Test
    public void testGetChars() {
        JCLO jclo = new JCLO(new Object());
        jclo.setObject("key", new char[]{'a'});
        assertArrayEquals(jclo.getChars("key"), new char[]{'a'});
    }
    
    // Test getShorts method
    @Test
    public void testGetShorts() {
        JCLO jclo = new JCLO(new Object());
        jclo.setObject("key", new short[]{1});
        assertArrayEquals(jclo.getShorts("key"), new short[]{1});
    }
    
    // Test getInts method
    @Test
    public void testGetInts() {
        JCLO jclo = new JCLO(new Object());
        jclo.setObject("key", new int[]{1});
        assertArrayEquals(jclo.getInts("key"), new int[]{1});
    }
    
    // Test getFloats method
    @Test
    public void testGetFloats() {
        JCLO jclo = new JCLO(new Object());
        jclo.setObject("key", new float[]{1.0f});
        assertArrayEquals(jclo.getFloats("key"), new float[]{1.0f}, 0);
    }
    
    // Test getDoubles method
    @Test
    public void testGetDoubles() {
        JCLO jclo = new JCLO(new Object());
        jclo.setObject("key", new double[]{1.0});
        assertArrayEquals(jclo.getDoubles("key"), new double[]{1.0}, 0);
    }
    
    // Test getLongs method
    @Test
    public void testGetLongs() {
        JCLO jclo = new JCLO(new Object());
        jclo.setObject("key", new long[]{1000L});
        assertArrayEquals(jclo.getLongs("key"), new long[]{1000L});
    }

}