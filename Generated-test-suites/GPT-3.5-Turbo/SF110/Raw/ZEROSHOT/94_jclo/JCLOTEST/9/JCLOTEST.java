import static org.junit.Assert.*;
import org.junit.Test;

public class JCLOTest {
    
    @Test
    public void testConstructor1() {
        JCLO jclo = new JCLO(null);
        assertNotNull(jclo);
    }
    
    @Test
    public void testConstructor2() {
        JCLO jclo = new JCLO(null, null);
        assertNotNull(jclo);
    }
    
    @Test
    public void testConstructor3() {
        JCLO jclo = new JCLO(null, null, null);
        assertNotNull(jclo);
    }
    
    @Test
    public void testConstructor4() {
        JCLO jclo = new JCLO("", null);
        assertNotNull(jclo);
    }
    
    @Test
    public void testConstructor5() {
        JCLO jclo = new JCLO("", null, null);
        assertNotNull(jclo);
    }
    
    @Test
    public void testGetField() {
        JCLO jclo = new JCLO(null, null);
        assertNull(jclo.getField("field1"));
    }
    
    @Test
    public void testGetValue() {
        JCLO jclo = new JCLO(null, null);
        assertNull(jclo.getValue("field1"));
    }
    
    @Test
    public void testSetObject() {
        JCLO jclo = new JCLO("", null);
        jclo.setObject(null, null);
    }
    
    @Test
    public void testGetArrayType() {
        JCLO jclo = new JCLO(null, null);
        assertNull(jclo.getArrayType(null));
    }
    
    @Test
    public void testToString() {
        JCLO jclo = new JCLO("", null);
        assertNotNull(jclo.toString());
    }
    
    @Test
    public void testGetUsageType() {
        JCLO jclo = new JCLO(null, null);
        assertNull(jclo.getUsageType(null));
    }
    
    @Test
    public void testUsage() {
        JCLO jclo = new JCLO(null, null);
        assertNotNull(jclo.usage());
    }
    
    @Test
    public void testParseAdditional() {
        JCLO jclo = new JCLO("", null);
        jclo.parseAdditional(null, 0);
    }
    
    @Test
    public void testAddToArray() {
        JCLO jclo = new JCLO("", null);
        assertNull(jclo.addToArray(null, null));
    }
    
    @Test
    public void testGetKey() {
        JCLO jclo = new JCLO("", null);
        assertNotNull(jclo.getKey(""));
    }
    
    @Test
    public void testGetBooleanValue() {
        JCLO jclo = new JCLO("", null);
        assertNotNull(jclo.getBooleanValue(""));
    }
    
    @Test
    public void testMakeObject() {
        JCLO jclo = new JCLO("", null);
        assertNull(jclo.makeObject("", ""));
    }
    
    @Test
    public void testGetEqualsValue() {
        JCLO jclo = new JCLO("", null);
        assertNotNull(jclo.getEqualsValue(""));
    }
    
    @Test
    public void testParse() {
        JCLO jclo = new JCLO("", null);
        jclo.parse(null);
    }
    
    @Test
    public void testGetBoolean() {
        JCLO jclo = new JCLO("", null);
        assertEquals(false, jclo.getBoolean("key"));
    }
    
    @Test
    public void testGetByte() {
        JCLO jclo = new JCLO("", null);
        assertEquals((byte)0, jclo.getByte("key"));
    }
    
    @Test
    public void testGetChar() {
        JCLO jclo = new JCLO("", null);
        assertEquals('\u0000', jclo.getChar("key"));
    }
    
    @Test
    public void testGetShort() {
        JCLO jclo = new JCLO("", null);
        assertEquals((short)0, jclo.getShort("key"));
    }
    
    @Test
    public void testGetInt() {
        JCLO jclo = new JCLO("", null);
        assertEquals(0, jclo.getInt("key"));
    }
    
    @Test
    public void testGetFloat() {
        JCLO jclo = new JCLO("", null);
        assertEquals(0.0, jclo.getFloat("key"), 0.001);
    }
    
    @Test
    public void testGetDouble() {
        JCLO jclo = new JCLO("", null);
        assertEquals(0.0, jclo.getDouble("key"), 0.001);
    }
    
    @Test
    public void testGetLong() {
        JCLO jclo = new JCLO("", null);
        assertEquals(0L, jclo.getLong("key"));
    }
    
    @Test
    public void testGetString() {
        JCLO jclo = new JCLO("", null);
        assertNull(jclo.getString("key"));
    }
    
    @Test
    public void testGetBytes() {
        JCLO jclo = new JCLO("", null);
        assertNull(jclo.getBytes("key"));
    }
    
    @Test
    public void testGetChars() {
        JCLO jclo = new JCLO("", null);
        assertNull(jclo.getChars("key"));
    }
    
    @Test
    public void testGetShorts() {
        JCLO jclo = new JCLO("", null);
        assertNull(jclo.getShorts("key"));
    }
    
    @Test
    public void testGetInts() {
        JCLO jclo = new JCLO("", null);
        assertNull(jclo.getInts("key"));
    }
    
    @Test
    public void testGetFloats() {
        JCLO jclo = new JCLO("", null);
        assertNull(jclo.getFloats("key"));
    }
    
    @Test
    public void testGetDoubles() {
        JCLO jclo = new JCLO("", null);
        assertNull(jclo.getDoubles("key"));
    }
    
    @Test
    public void testGetLongs() {
        JCLO jclo = new JCLO("", null);
        assertNull(jclo.getLongs("key"));
    }
    
    @Test
    public void testGetStrings() {
        JCLO jclo = new JCLO("", null);
        assertNull(jclo.getStrings("key"));
    }
    
    @Test
    public void testMain() {
        JCLO.main(new String[] {});
    }
}