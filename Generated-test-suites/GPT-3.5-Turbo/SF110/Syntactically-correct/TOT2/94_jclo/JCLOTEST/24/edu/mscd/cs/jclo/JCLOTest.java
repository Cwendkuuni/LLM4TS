package edu.mscd.cs.jclo;
import static org.junit.Assert.*;
import org.junit.Test;

public class JCLOTest {

    // Test constructor with object parameter
    @Test
    public void testConstructorObject() {
        JCLO jclo = new JCLO(new Object());
        assertNotNull(jclo);
    }

    // Test constructor with object and aliases parameters
    @Test
    public void testConstructorObjectAliases() {
        JCLO jclo = new JCLO(new Object(), new String[][]{{"key1", "alias1"}});
        assertNotNull(jclo);
    }

    // Test constructor with prefix and object parameters
    @Test
    public void testConstructorPrefixObject() {
        JCLO jclo = new JCLO("prefix", new Object());
        assertNotNull(jclo);
    }

    // Test constructor with prefix, object, and aliases parameters
    @Test
    public void testConstructorPrefixObjectAliases() {
        JCLO jclo = new JCLO("prefix", new Object(), new String[][]{{"key1", "alias1"}});
        assertNotNull(jclo);
    }

    // Test getValue method
    @Test
    public void testGetValue() {
        JCLO jclo = new JCLO(new Object());
        assertNull(jclo.getValue("nonexistent"));
    }

    // Test toString method
    @Test
    public void testToString() {
        JCLO jclo = new JCLO(new Object());
        assertNotNull(jclo.toString());
    }

    // Test usage method
    @Test
    public void testUsage() {
        JCLO jclo = new JCLO(new Object());
        assertNotNull(jclo.usage());
    }

    // Test parsing additional arguments
    @Test
    public void testParseAdditional() {
        JCLO jclo = new JCLO(new Object());
        jclo.parse(new String[]{"arg1"});
    }

    // Test getBoolean method
    @Test
    public void testGetBoolean() {
        JCLO jclo = new JCLO(new Object());
        assertFalse(jclo.getBoolean("key"));
    }

    // Test getByte method
    @Test
    public void testGetByte() {
        JCLO jclo = new JCLO(new Object());
        assertEquals(0, jclo.getByte("key"));
    }

    // Test getChar method
    @Test
    public void testGetChar() {
        JCLO jclo = new JCLO(new Object());
        assertEquals('\u0000', jclo.getChar("key"));
    }

    // Test exception case for unspecified option key
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidKeyException() {
        JCLO jclo = new JCLO(new Object());
        jclo.parse(new String[]{"--unknown"});
    }

}