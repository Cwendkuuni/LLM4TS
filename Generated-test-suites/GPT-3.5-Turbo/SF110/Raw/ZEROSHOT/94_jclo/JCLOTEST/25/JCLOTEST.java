import org.junit.*;
import static org.junit.Assert.*;

public class JCLOTest {

    private JCLO jclo;

    @Before
    public void setUp() {
        Object object = new Object();
        jclo = new JCLO(object);
    }

    @Test
    public void testGetValue_fieldNotFound() {
        assertNull(jclo.getValue("fieldNotFound"));
    }

    @Test
    public void testGetValue() {
        assertNotNull(jclo.getValue("fields"));
    }

    @Test
    public void testGetObject() {
        Field field = new Field();
        assertNotNull(jclo.getObject(field));
    }

    @Test
    public void testGetField() {
        assertNotNull(jclo.getField("key"));
    }

    @Test
    public void testToString() {
        assertNotNull(jclo.toString());
    }

    @Test
    public void testGetUsageType() {
        Class type = new Class();
        assertNotNull(jclo.getUsageType(type));
    }

    @Test
    public void testUsage() {
        assertNotNull(jclo.usage());
    }

    @Test
    public void testParseAdditional() {
        String[] args = new String[4];
        args[0] = "arg1";
        args[1] = "arg2";
        args[2] = "arg3";
        args[3] = "arg4";
        jclo.parseAdditional(args, 1);
    }

    @Test
    public void testAddToArray() {
        Field field = new Field();
        Object o = new Object();
        assertNotNull(jclo.addToArray(field, o));
    }

    @Test
    public void testGetKey() {
        String arg = "--arg";
        assertNotNull(jclo.getKey(arg));
    }

    @Test
    public void testGetBooleanValue() {
        String arg = "--arg=true";
        assertNotNull(jclo.getBooleanValue(arg));
    }

    @Test
    public void testMakeObject() {
        String type = "java.lang.String";
        String val = "value";
        assertNotNull(jclo.makeObject(type, val));
    }

    @Test
    public void testGetEqualsValue() {
        String arg = "--arg=value";
        assertNotNull(jclo.getEqualsValue(arg));
    }

    @Test
    public void testParse() {
        String[] args = new String[2];
        args[0] = "--arg=value";
        args[1] = "--arg2";
        jclo.parse(args);
    }

    @Test
    public void testGetBoolean() {
        String key = "key";
        assertTrue(jclo.getBoolean(key));
    }

    @Test
    public void testGetByte() {
        String key = "key";
        assertEquals((byte)0, jclo.getByte(key));
    }

    @Test
    public void testGetChar() {
        String key = "key";
        assertEquals((char)0, jclo.getChar(key));
    }

    @Test
    public void testGetShort() {
        String key = "key";
        assertEquals((short)0, jclo.getShort(key));
    }

    @Test
    public void testGetInt() {
        String key = "key";
        assertEquals(0, jclo.getInt(key));
    }

    @Test
    public void testGetFloat() {
        String key = "key";
        assertEquals(0.0f, jclo.getFloat(key), 0.0f);
    }

    @Test
    public void testGetDouble() {
        String key = "key";
        assertEquals(0.0, jclo.getDouble(key), 0.0);
    }

    @Test
    public void testGetLong() {
        String key = "key";
        assertEquals(0L, jclo.getLong(key));
    }

    @Test
    public void testGetString() {
        String key = "key";
        assertNull(jclo.getString(key));
    }

    @Test
    public void testGetBytes() {
        String key = "key";
        assertNull(jclo.getBytes(key));
    }

    @Test
    public void testGetChars() {
        String key = "key";
        assertNull(jclo.getChars(key));
    }

    @Test
    public void testGetShorts() {
        String key = "key";
        assertNull(jclo.getShorts(key));
    }

    @Test
    public void jtestGetInts() {
        String key = "key";
        assertNull(jclo.getInts(key));
    }

    @Test
    public void testGetFloats() {
        String key = "key";
        assertNull(jclo.getFloats(key));
    }

    @Test
    public void testGetDoubles() {
        String key = "key";
        assertNull(jclo.getDoubles(key));
    }

    @Test
    public void testGetLongs() {
        String key = "key";
        assertNull(jclo.getLongs(key));
    }

    @Test
    public void testGetStrings() {
        String key = "key";
        assertNull(jclo.getStrings(key));
    }

    @Test
    public void testMain() {
        String[] args = new String[1];
        args[0] = "--version";
        jclo.main(args);
    }

}