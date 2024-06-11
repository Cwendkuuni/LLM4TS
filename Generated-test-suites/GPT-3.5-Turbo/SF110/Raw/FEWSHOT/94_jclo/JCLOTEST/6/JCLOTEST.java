package edu.mscd.cs.jclo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JCLOTest {
    
    private JCLO jclo;
    
    @Test
    public void testConstructor1() {
        jclo = new JCLO(null);
        assertEquals("", jclo.prefix);
        assertNull(jclo.aliases);
        assertEquals(0, jclo.fields.length);
    }
    
    @Test
    public void testConstructor2() {
        String[][] aliases = {{"a", "b"}, {"c", "d"}};
        jclo = new JCLO(null, aliases);
        assertEquals("", jclo.prefix);
        assertEquals(2, jclo.aliases.length);
        assertEquals("a", jclo.aliases[0][0]);
        assertEquals("b", jclo.aliases[0][1]);
        assertEquals("c", jclo.aliases[1][0]);
        assertEquals("d", jclo.aliases[1][1]);
        assertEquals(0, jclo.fields.length);
    }
    
    @Test
    public void testConstructor3() {
        jclo = new JCLO("prefix", null);
        assertEquals("prefix", jclo.prefix);
        assertNull(jclo.aliases);
        assertEquals(0, jclo.fields.length);
    }
    
    @Test
    public void testConstructor4() {
        String[][] aliases = {{"a", "b"}, {"c", "d"}};
        jclo = new JCLO("prefix", null, aliases);
        assertEquals("prefix", jclo.prefix);
        assertEquals(2, jclo.aliases.length);
        assertEquals("a", jclo.aliases[0][0]);
        assertEquals("b", jclo.aliases[0][1]);
        assertEquals("c", jclo.aliases[1][0]);
        assertEquals("d", jclo.aliases[1][1]);
        assertEquals(0, jclo.fields.length);
    }
    
    @Test
    public void testGetObject() throws Exception {
        Object object = new Object();
        jclo = new JCLO(null, object, null);
        Field field = object.getClass().getDeclaredField("object");
        field.setAccessible(true);
        field.set(object, "test");
        assertEquals("test", jclo.getObject(field));
    }
    
    @Test
    public void testGetField() throws Exception {
        Object object = new Object();
        jclo = new JCLO(null, object, null);
        Field field = object.getClass().getDeclaredField("object");
        field.setAccessible(true);
        field.set(object, "test");
        assertEquals(field, jclo.getField("object"));
        assertNull(jclo.getField("foo"));
    }
    
    @Test
    public void testGetValue() throws Exception {
        Object object = new Object();
        jclo = new JCLO(null, object, null);
        Field field = object.getClass().getDeclaredField("object");
        field.setAccessible(true);
        field.set(object, "test");
        assertEquals("test", jclo.getValue("object"));
        assertNull(jclo.getValue("foo"));
    }
    
    @Test
    public void testSetObject() throws Exception {
        Object object = new Object();
        jclo = new JCLO(null, object, null);
        Field field = object.getClass().getDeclaredField("object");
        field.setAccessible(true);
        jclo.setObject(field, "test");
        assertEquals("test", field.get(object));
    }
    
    @Test
    public void testGetArrayType() {
        Object object = new Object();
        jclo = new JCLO(null, object, null);
        Field field = object.getClass().getDeclaredField("object");
        assertEquals("Object", jclo.getArrayType(field.getType()));
        field = object.getClass().getDeclaredField("EMPTY_STRING_ARRAY");
        assertEquals("String", jclo.getArrayType(field.getType()));
    }
    
    // toString method
    
    // getUsageType method
    
    // usage method
    
    // parseAdditional method
    
    // addToArray method
    
    // getKey method
    
    // getBooleanValue method
    
    // makeObject method
    
    // getEqualsValue method
    
    // parse method
    
    // getBoolean method
    
    // getByte method
    
    // getChar method
    
    // getShort method
    
    // getInt method
    
    // getFloat method
    
    // getDouble method
    
    // getLong method
    
    // getString method
    
    // getBytes method
    
    // getChars method
    
    // getShorts method
    
    // getInts method
    
    // getFloats method
    
    // getDoubles method
    
    // getLongs method
    
    // getStrings method
    
    // main method
}