import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JCLOTest {
    private JCLO jclo;

    @Before
    public void setup() {
        Object object = new Object();
        String[][] aliases = {{"alias1", "alias2"}, {"alias3", "alias4"}};
        jclo = new JCLO("prefix", object, aliases);
    }

    @Test
    public void testGetObject() {
        Field f = jclo.getClass().getDeclaredField("object");
        f.setAccessible(true);
        f.set(jclo, new Object());

        Assert.assertNotNull(jclo.getObject(f));
    }

    @Test
    public void testGetField() {
        String key = "TestKey";
        Field f = jclo.getField(key);

        Assert.assertNull(f);
    }

    @Test
    public void testGetValue() {
        String key = "TestKey";
        Object value = jclo.getValue(key);

        Assert.assertNull(value);
    }

    @Test
    public void testSetObject() {
        Field f = jclo.getClass().getDeclaredField("object");

        jclo.setObject(f, new Object());
    }

    @Test
    public void testGetArrayType() {
        Class type = String.class;
        String arrayType = jclo.getArrayType(type);

        Assert.assertEquals("", arrayType);
    }

    @Test
    public void testToString() {
        String result = jclo.toString();

        Assert.assertEquals("", result);
    }

    @Test
    public void testUsageType() {
        Class type = String.class;
        String usageType = jclo.getUsageType(type);

        Assert.assertEquals(" String", usageType);
    }

    @Test
    public void testUsage() {
        String result = jclo.usage();

        Assert.assertEquals("", result);
    }

    @Test
    public void testParseAdditional() {
        String[] args = {};
        int i = 0;

        jclo.parseAdditional(args, i);
    }

    @Test
    public void testAddToArray() {
        Field field = jclo.getClass().getDeclaredField("object");
        Object o = new Object();

        Object result = jclo.addToArray(field, o);

        Assert.assertNotNull(result);
    }

    @Test
    public void testGetKey() {
        String arg = "--arg=value";

        String key = jclo.getKey(arg);

        Assert.assertEquals("arg", key);
    }

    @Test
    public void testGetBooleanValue() {
        String arg = "--arg=true";

        String value = jclo.getBooleanValue(arg);

        Assert.assertEquals("true", value);
    }

    @Test
    public void testMakeObject() {
        String type = "boolean";
        String val = "true";

        Object result = jclo.makeObject(type, val);

        Assert.assertNotNull(result);
        Assert.assertEquals(true, result);
    }

    @Test
    public void testGetEqualsValue() {
        String arg = "--arg=value";

        String value = jclo.getEqualsValue(arg);

        Assert.assertEquals("value", value);
    }

    @Test
    public void testParse() {
        String[] args = {"--arg=value"};

        jclo.parse(args);
    }

    @Test
    public void testGetBoolean() {
        String key = "TestKey";

        boolean result = jclo.getBoolean(key);

        Assert.assertEquals(false, result);
    }

    @Test
    public void testGetByte() {
        String key = "TestKey";

        byte result = jclo.getByte(key);

        Assert.assertEquals(0, result);
    }

    @Test
    public void testGetChar() {
        String key = "TestKey";

        char result = jclo.getChar(key);

        Assert.assertEquals('\u0000', result);
    }

    @Test
    public void testGetShort() {
        String key = "TestKey";

        short result = jclo.getShort(key);

        Assert.assertEquals(0, result);
    }

    @Test
    public void testGetInt() {
        String key = "TestKey";

        int result = jclo.getInt(key);

        Assert.assertEquals(0, result);
    }

    @Test
    public void testGetFloat() {
        String key = "TestKey";

        float result = jclo.getFloat(key);

        Assert.assertEquals(0.0f, result, 0.0001);
    }

    @Test
    public void testGetDouble() {
        String key = "TestKey";

        double result = jclo.getDouble(key);

        Assert.assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testGetLong() {
        String key = "TestKey";

        long result = jclo.getLong(key);

        Assert.assertEquals(0, result);
    }

    @Test
    public void testGetString() {
        String key = "TestKey";

        String result = jclo.getString(key);

        Assert.assertNull(result);
    }

    @Test
    public void testGetBytes() {
        String key = "TestKey";

        byte[] result = jclo.getBytes(key);

        Assert.assertNull(result);
    }

    @Test
    public void testGetChars() {
        String key = "TestKey";

        char[] result = jclo.getChars(key);

        Assert.assertNull(result);
    }

    @Test
    public void testGetShorts() {
        String key = "TestKey";

        short[] result = jclo.getShorts(key);

        Assert.assertNull(result);
    }

    @Test
    public void testGetInts() {
        String key = "TestKey";

        int[] result = jclo.getInts(key);

        Assert.assertNull(result);
    }

    @Test
    public void testGetFloats() {
        String key = "TestKey";

        float[] result = jclo.getFloats(key);

        Assert.assertNull(result);
    }

    @Test
    public void testGetDoubles() {
        String key = "TestKey";

        double[] result = jclo.getDoubles(key);

        Assert.assertNull(result);
    }

    @Test
    public void testGetLongs() {
        String key = "TestKey";

        long[] result = jclo.getLongs(key);

        Assert.assertNull(result);
    }

    @Test
    public void testGetStrings() {
        String key = "TestKey";

        String[] result = jclo.getStrings(key);

        Assert.assertNull(result);
    }

    @Test
    public void testMain() {
        String[] args = {"--version"};

        JCLO.main(args);
    }
}