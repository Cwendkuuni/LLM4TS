package edu.mscd.cs.jclo;
import org.junit.Test;
import static org.junit.Assert.*;

public class JCLOTest {

    @Test
    public void testConstructor1() {
        // Testing JCLO constructor with only object input
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        assertEquals(obj, jclo.getValue("object"));
    }

    @Test
    public void testConstructor2() {
        // Testing JCLO constructor with object and aliases input
        Object obj = new Object();
        String[][] aliases = {{"alias1", "alias2"}};
        JCLO jclo = new JCLO(obj, aliases);
        assertEquals(obj, jclo.getValue("object"));
        assertEquals(aliases, jclo.getValue("aliases"));
    }

    @Test
    public void testConstructor3() {
        // Testing JCLO constructor with prefix and object input
        Object obj = new Object();
        String prefix = "prefix";
        JCLO jclo = new JCLO(prefix, obj);
        assertEquals(obj, jclo.getValue("object"));
        assertEquals(prefix, jclo.getValue("prefix"));
    }

    @Test
    public void testConstructor4() {
        // Testing JCLO constructor with prefix, object, and aliases input
        Object obj = new Object();
        String prefix = "prefix";
        String[][] aliases = {{"alias1", "alias2"}};
        JCLO jclo = new JCLO(prefix, obj, aliases);
        assertEquals(obj, jclo.getValue("object"));
        assertEquals(prefix, jclo.getValue("prefix"));
        assertEquals(aliases, jclo.getValue("aliases"));
    }

    @Test
    public void testGetValue() {
        // Testing getValue method
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        assertNull(jclo.getValue("nonExistingField"));
    }

    @Test
    public void testToString() {
        // Testing toString method
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        assertNotNull(jclo.toString());
    }

    // Add more test cases for other methods if necessary based on edge cases and error scenarios

}