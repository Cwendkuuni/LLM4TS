import org.junit.*;
import static org.junit.Assert.*;
import java.lang.reflect.*;

public class JCLOTest {

    private JCLO jclo;

    @Before
    public void setup() {
        jclo = new JCLO(new Object());
    }

    @Test
    public void testGetField() {
        Field expectedField = null;
        Field actualField = jclo.getField("key");
        assertEquals(expectedField, actualField);
    }

    @Test
    public void testGetValue() {
        Object expectedValue = null;
        Object actualValue = jclo.getValue("key");
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testSetObject() {
        Field field = null;
        Object o = new Object();
        jclo.setObject(field, o);
        Field expectedField = null;
        Field actualField = jclo.getField("key");
        assertEquals(expectedField, actualField);
    }

    @Test
    public void testGetArrayType() {
        String expectedType = "";
        Class type = null;
        String actualType = jclo.getArrayType(type);
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testToString() {
        String expectedString = "";
        String actualString = jclo.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testUsage() {
        String expectedUsage = "";
        String actualUsage = jclo.usage();
        assertEquals(expectedUsage, actualUsage);
    }

    @Test
    public void testGetObject() {
        Field f = null;
        Object expectedObject = null;
        Object actualObject = jclo.getObject(f);
        assertEquals(expectedObject, actualObject);
    }

    @Test
    public void testParseAdditional() {
        String[] args = null;
        int i = 0;
        jclo.parseAdditional(args, i);
        Object expectedObject = null;
        Object actualObject = jclo.getValue("additional");
        assertEquals(expectedObject, actualObject);
    }

    @Test
    public void testAddToArray() {
        Field field = null;
        Object o = new Object();
        Object expectedArray = null;
        Object actualArray = jclo.addToArray(field, o);
        assertEquals(expectedArray, actualArray);
    }

    @Test
    public void testGetKey() {
        String arg = "";
        String expectedKey = "";
        String actualKey = jclo.getKey(arg);
        assertEquals(expectedKey, actualKey);
    }

    @Test
    public void testGetBooleanValue() {
        String arg = "";
        String expectedValue = "";
        String actualValue = jclo.getBooleanValue(arg);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testMakeObject() {
        String type = "";
        String val = "";
        Object expectedObject = null;
        Object actualObject = jclo.makeObject(type, val);
        assertEquals(expectedObject, actualObject);
    }

    @Test
    public void testGetEqualsValue() {
        String arg = "";
        String expectedValue = "";
        String actualValue = jclo.getEqualsValue(arg);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testParse() {
        String[] args = null;
        jclo.parse(args);
    }

    @Test
    public void testGetBoolean() {
        String key = "";
        boolean expectedValue = false;
        boolean actualValue = jclo.getBoolean(key);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetByte() {
        String key = "";
        byte expectedValue = 0;
        byte actualValue = jclo.getByte(key);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetChar() {
        String key = "";
        char expectedValue = '\u0000';
        char actualValue = jclo.getChar(key);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetShort() {
        String key = "";
        short expectedValue = 0;
        short actualValue = jclo.getShort(key);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetInt() {
        String key = "";
        int expectedValue = 0;
        int actualValue = jclo.getInt(key);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetFloat() {
        String key = "";
        float expectedValue = 0.0f;
        float actualValue = jclo.getFloat(key);
        assertEquals(expectedValue, actualValue, 0.0f);
    }

    @Test
    public void testGetDouble() {
        String key = "";
        double expectedValue = 0.0;
        double actualValue = jclo.getDouble(key);
        assertEquals(expectedValue, actualValue, 0.0);
    }

    @Test
    public void testGetLong() {
        String key = "";
        long expectedValue = 0L;
        long actualValue = jclo.getLong(key);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetString() {
        String key = "";
        String expectedValue = null;
        String actualValue = jclo.getString(key);
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetBytes() {
        String key = "";
        byte[] expectedValue = null;
        byte[] actualValue = jclo.getBytes(key);
        assertArrayEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetChars() {
        String key = "";
        char[] expectedValue = null;
        char[] actualValue = jclo.getChars(key);
        assertArrayEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetShorts() {
        String key = "";
        short[] expectedValue = null;
        short[] actualValue = jclo.getShorts(key);
        assertArrayEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetInts() {
        String key = "";
        int[] expectedValue = null;
        int[] actualValue = jclo.getInts(key);
        assertArrayEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetFloats() {
        String key = "";
        float[] expectedValue = null;
        float[] actualValue = jclo.getFloats(key);
        assertArrayEquals(expectedValue, actualValue, 0.0f);
    }

    @Test
    public void testGetDoubles() {
        String key = "";
        double[] expectedValue = null;
        double[] actualValue = jclo.getDoubles(key);
        assertArrayEquals(expectedValue, actualValue, 0.0);
    }

    @Test
    public void testGetLongs() {
        String key = "";
        long[] expectedValue = null;
        long[] actualValue = jclo.getLongs(key);
        assertArrayEquals(expectedValue, actualValue);
    }

    @Test
    public void testGetStrings() {
        String key = "";
        String[] expectedValue = null;
        String[] actualValue = jclo.getStrings(key);
        assertArrayEquals(expectedValue, actualValue);
    }

    @Test
    public void testMain() {
        String[] args = null;
        jclo.main(args);
    }

}