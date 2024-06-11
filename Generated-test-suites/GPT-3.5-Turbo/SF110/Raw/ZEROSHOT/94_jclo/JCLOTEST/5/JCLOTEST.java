package edu.mscd.cs.jclo;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import static org.junit.Assert.*;

public class JCLOTest {

    private JCLO jclo;

    @Before
    public void setUp() {
        Object object = new Object();
        String[][] aliases = {{"arg1", "alias1"}, {"arg2", "alias2"}};
        jclo = new JCLO(object, aliases);
    }

    @Test
    public void testJCLO() {
        // Test constructor 1
        JCLO jclo1 = new JCLO(null);
        assertNull(jclo1.prefix);
        assertNull(jclo1.aliases);

        // Test constructor 2
        String[][] aliases = {{"arg1", "alias1"}, {"arg2", "alias2"}};
        JCLO jclo2 = new JCLO(null, null, aliases);
        assertNull(jclo2.object);
        assertNull(jclo2.prefix);

        // Test constructor 3
        Object object = new Object();
        JCLO jclo3 = new JCLO("prefix", object);
        assertEquals("prefix", jclo3.prefix);
        assertNull(jclo3.aliases);
    }

    @Test
    public void testGetObject() {
        // Test getField method
        Field field1 = jclo.getField("prefix_key1");
        assertEquals(jclo.fields[0], field1);

        Field field2 = jclo.getField("key5");
        assertEquals(jclo.fields[4], field2);

        // Test getObject method
        Object object1 = jclo.getObject(jclo.fields[0]);
        assertEquals(jclo.object, object1);

        Object object2 = jclo.getObject(jclo.fields[4]);
        assertNull(object2);
    }

    @Test
    public void testGetField() {
        Field field1 = jclo.getField("prefix_key1");
        assertEquals(jclo.fields[0], field1);

        Field field2 = jclo.getField("key5");
        assertEquals(jclo.fields[4], field2);

        Field field3 = jclo.getField("nonexistent");
        assertNull(field3);
    }

    @Test
    public void testGetValue() {
        Object value1 = jclo.getValue("prefix_key1");
        assertEquals(jclo.object, value1);

        Object value2 = jclo.getValue("key5");
        assertNull(value2);

        Object value3 = jclo.getValue("nonexistent");
        assertNull(value3);
    }

    @Test
    public void testSetObject() {
        Field field = jclo.fields[0];
        Object object = new Object();
        jclo.setObject(field, object);
        assertEquals(object, jclo.getObject(field));
    }

    // Add tests for other methods in JCLO class

}