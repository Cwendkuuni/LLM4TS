package edu.mscd.cs.jclo;

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
        String prefix = "prefix";
        JCLO jclo = new JCLO(prefix, new Object());
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
    public void testGetValue() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNull(jclo.getValue("field"));
    }
    
    @Test
    public void testToString() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertEquals("", jclo.toString());
    }
    
    @Test
    public void testUsage() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertEquals("", jclo.usage());
    }
    
    @Test
    public void testParse() {
        String[] args = {"-a", "value"};
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        jclo.parse(args);
    }
    
    @Test
    public void testGetBoolean() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertFalse(jclo.getBoolean("field"));
    }
    
    @Test
    public void testGetByte() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertEquals(0, jclo.getByte("field"));
    }
    
    @Test
    public void testGetChar() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertEquals('\u0000', jclo.getChar("field"));
    }
    
    @Test
    public void testGetShort() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertEquals(0, jclo.getShort("field"));
    }
    
    @Test
    public void testGetInt() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertEquals(0, jclo.getInt("field"));
    }
    
    @Test
    public void testGetFloat() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertEquals(0.0f, jclo.getFloat("field"), 0.0001);
    }
    
    @Test
    public void testGetDouble() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertEquals(0.0, jclo.getDouble("field"), 0.0001);
    }
    
    @Test
    public void testGetLong() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertEquals(0L, jclo.getLong("field"));
    }
    
    @Test
    public void testGetString() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNull(jclo.getString("field"));
    }
    
    @Test
    public void testGetBytes() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNull(jclo.getBytes("field"));
    }
    
    @Test
    public void testGetChars() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNull(jclo.getChars("field"));
    }
    
    @Test
    public void testGetShorts() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNull(jclo.getShorts("field"));
    }
    
    @Test
    public void testGetInts() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNull(jclo.getInts("field"));
    }
    
    @Test
    public void testGetFloats() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNull(jclo.getFloats("field"));
    }
    
    @Test
    public void testGetDoubles() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNull(jclo.getDoubles("field"));
    }
    
    @Test
    public void testGetLongs() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNull(jclo.getLongs("field"));
    }
    
    @Test
    public void testGetStrings() {
        Object object = new Object();
        JCLO jclo = new JCLO(object);
        assertNull(jclo.getStrings("field"));
    }
}