

import org.junit.Test;

import static org.junit.Assert.*;

public class JCLOTest {

    @Test
    public void testJCLOObject() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNotNull(jclo);
    }

    @Test
    public void testJCLOObjectStringArrayArray() {
        Object object = new Object();
        String[][] aliases = {{"alias1", "alias2"}};
        JCLO jclo = new JCLO(object, aliases);
        assertNotNull(jclo);
    }

    @Test
    public void testJCLOStringObject() {
        Object object = new Object();
        String prefix = "prefix";
        JCLO jclo = new JCLO(prefix, object);
        assertNotNull(jclo);
    }

    @Test
    public void testJCLOStringObjectStringArrayArray() {
        Object object = new Object();
        String prefix = "prefix";
        String[][] aliases = {{"alias1", "alias2"}};
        JCLO jclo = new JCLO(prefix, object, aliases);
        assertNotNull(jclo);
    }

    @Test
    public void testGetValue() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNull(jclo.getValue("key"));
    }

    @Test
    public void testToString() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNotNull(jclo.toString());
    }

    @Test
    public void testUsage() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNotNull(jclo.usage());
    }

    @Test
    public void testParse() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        String[] args = {"-arg1 value1", "-arg2=value2"};
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
        assertEquals(0.0f, jclo.getFloat("key"), 0.0001);
    }

    @Test
    public void testGetDouble() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertEquals(0.0, jclo.getDouble("key"), 0.0001);
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

