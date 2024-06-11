package edu.mscd.cs.jclo;
import org.junit.*;
import static org.junit.Assert.*;

public class JCLOTest {

    // Test constructor with only object parameter
    @Test
    public void testConstructorWithObject() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        assertEquals(obj, jclo.getValue("object"));
    }

    // Test constructor with object and aliases parameter
    @Test
    public void testConstructorWithObjectAndAliases() {
        Object obj = new Object();
        String[][] aliases = {{"alias1", "value1"}, {"alias2", "value2"}};
        JCLO jclo = new JCLO(obj, aliases);
        assertEquals(obj, jclo.getValue("object"));
        assertEquals("value1", jclo.getValue("alias1"));
    }

    // Test constructor with prefix, object, and aliases parameters
    @Test
    public void testConstructorWithPrefixObjectAndAliases() {
        Object obj = new Object();
        String prefix = "prefix";
        String[][] aliases = {{"alias1", "value1"}, {"alias2", "value2"}};
        JCLO jclo = new JCLO(prefix, obj, aliases);
        assertEquals(obj, jclo.getValue("object"));
        assertEquals("value1", jclo.getValue("alias1"));
        assertEquals(prefix, jclo.getValue("prefix"));
    }

    // Test getting value of existing field
    @Test
    public void testGetValueExistingField() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        String key = "object";
        assertEquals(obj, jclo.getValue(key));
    }

    // Test getting value of non-existing field
    @Test
    public void testGetValueNonExistingField() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        String key = "nonExisting";
        assertNull(jclo.getValue(key));
    }

    // Add more test cases for edge cases, exceptions, and other methods...

}