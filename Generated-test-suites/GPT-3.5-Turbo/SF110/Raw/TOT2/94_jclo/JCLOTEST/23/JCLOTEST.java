import static org.junit.Assert.*;
import org.junit.Test;

public class JCLOTest {
    
    @Test
    public void testConstructor1() {
        JCLO jclo = new JCLO(new Object());
        assertNotNull(jclo);
    }
    
    @Test
    public void testConstructor2() {
        String[][] aliases = {{"a", "b"}, {"c", "d"}};
        JCLO jclo = new JCLO(new Object(), aliases);
        assertNotNull(jclo);
    }
    
    @Test
    public void testConstructor3() {
        JCLO jclo = new JCLO("prefix", new Object());
        assertNotNull(jclo);
    }
    
    @Test
    public void testConstructor4() {
        String prefix = "prefix";
        String[][] aliases = {{"a", "b"}, {"c", "d"}};
        JCLO jclo = new JCLO(prefix, new Object(), aliases);
        assertNotNull(jclo);
    }
    
    @Test
    public void testGetter() {
        JCLO jclo = new JCLO(new Object());
        String key = "key";
        assertNull(jclo.getValue(key));
    }
    
    @Test
    public void testParse() {
        JCLO jclo = new JCLO(new Object());
        String[] args = {"-option", "value"};
        jclo.parse(args);
    }
    
    @Test
    public void testGetters() {
        JCLO jclo = new JCLO(new Object());
        String key = "key";
        assertNull(jclo.getBoolean(key));
        assertEquals(0, jclo.getByte(key));
        assertEquals('\u0000', jclo.getChar(key));
        assertEquals(0, jclo.getShort(key));
        assertEquals(0, jclo.getInt(key));
        assertEquals(0.0f, jclo.getFloat(key), 0.0);
        assertEquals(0.0, jclo.getDouble(key), 0.0);
        assertEquals(0L, jclo.getLong(key));
        assertNull(jclo.getString(key));
        assertNull(jclo.getBytes(key));
        assertNull(jclo.getChars(key));
        assertNull(jclo.getShorts(key));
        assertNull(jclo.getInts(key));
        assertNull(jclo.getFloats(key));
        assertNull(jclo.getDoubles(key));
        assertNull(jclo.getLongs(key));
        assertNull(jclo.getStrings(key));
    }
}