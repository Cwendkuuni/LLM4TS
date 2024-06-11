import org.junit.*;
import static org.junit.Assert.*;

public class JCLOTest {
    
    @Test
    public void testJCLOConstructor1() {
        JCLO jclo = new JCLO(null);
        assertNotNull(jclo);
    }
    
    @Test
    public void testJCLOConstructor2() {
        String[][] aliases = {{"a", "b"}};
        JCLO jclo = new JCLO(null, aliases);
        assertNotNull(jclo);
    }
    
    @Test
    public void testJCLOConstructor3() {
        Object object = new Object();
        JCLO jclo = new JCLO("prefix", object);
        assertNotNull(jclo);
    }
    
    @Test
    public void testJCLOConstructor4() {
        Object object = new Object();
        String[][] aliases = {{"a", "b"}};
        JCLO jclo = new JCLO("prefix", object, aliases);
        assertNotNull(jclo);
    }

    @Test
    public void testGetValue() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNull(jclo.getValue("key"));
    }
    
    @Test
    public void testGetObject() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNull(jclo.getObject(null));
    }
    
    @Test
    public void testGetField() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNull(jclo.getField(null));
    }
    
    @Test
    public void testToString() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNotNull(jclo.toString());
    }
    
    @Test
    public void testGetArrayType() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNull(jclo.getArrayType(null));
    }
    
    @Test
    public void testUsage() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNotNull(jclo.usage());
    }
    
    @Test
    public void testParseAdditional() {
        String[] args = {"arg"};
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        jclo.parseAdditional(args, 0);
    }
    
    @Test
    public void testAddToArray() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNull(jclo.addToArray(null, null));
    }
    
    @Test
    public void testGetKey() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertEquals("key", jclo.getKey("--key"));
    }
    
    @Test
    public void testGetBooleanValue() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertEquals("true", jclo.getBooleanValue("--key=true"));
    }
    
    @Test
    public void testMakeObject() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNull(jclo.makeObject("type", "val"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetEqualsValue() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        jclo.getEqualsValue("arg");
    }
    
    @Test
    public void testParse() {
        String[] args = {"--key1=value1", "-key2", "value2"};
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        jclo.parse(args);
    }
    
    @Test
    public void testGetBoolean() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertFalse(jclo.getBoolean("key"));
    }
    
    @Test
    public void testGetByte() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertEquals(0, jclo.getByte("key"));
    }
    
    @Test
    public void testGetChar() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertEquals('\u0000', jclo.getChar("key"));
    }
    
    @Test
    public void testGetShort() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertEquals(0, jclo.getShort("key"));
    }
    
    @Test
    public void testGetInt() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertEquals(0, jclo.getInt("key"));
    }
    
    @Test
    public void testGetFloat() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertEquals(0.0f, jclo.getFloat("key"), 0);
    }
    
    @Test
    public void testGetDouble() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertEquals(0.0, jclo.getDouble("key"), 0);
    }
    
    @Test
    public void testGetLong() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertEquals(0L, jclo.getLong("key"));
    }
    
    @Test
    public void testGetString() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNull(jclo.getString("key"));
    }
    
    @Test
    public void testGetBytes() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNull(jclo.getBytes("key"));
    }
    
    @Test
    public void testGetChars() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNull(jclo.getChars("key"));
    }
    
    @Test
    public void testGetShorts() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNull(jclo.getShorts("key"));
    }
    
    @Test
    public void testGetInts() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNull(jclo.getInts("key"));
    }
    
    @Test
    public void testGetFloats() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNull(jclo.getFloats("key"));
    }
    
    @Test
    public void testGetDoubles() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNull(jclo.getDoubles("key"));
    }
    
    @Test
    public void testGetLongs() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNull(jclo.getLongs("key"));
    }
    
    @Test
    public void testGetStrings() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNull(jclo.getStrings("key"));
    }
    
    @Test
    public void testMain() {
        String[] args = {"--version"};
        JCLO.main(args);
    }
}