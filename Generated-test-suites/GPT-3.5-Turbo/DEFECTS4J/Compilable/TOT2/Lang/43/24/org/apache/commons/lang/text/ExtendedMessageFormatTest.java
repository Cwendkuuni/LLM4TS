package org.apache.commons.lang.text;
import static org.junit.Assert.*;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructor() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        assertNotNull(emf);
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.applyPattern("New Test {0}");
        assertEquals("New Test {0}", emf.toPattern());
    }

    @Test
    public void testApplyPatternWithRegistry() {
        // Add test for applying pattern with registry
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Invalid {pattern}");
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        assertEquals("Test {0}", emf.toPattern());
    }

    @Test
    public void testGetFormat() {
        // Add test for getting format
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.setFormat(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.setFormatByArgumentIndex(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.setFormats(null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.setFormatsByArgumentIndex(null);
    }

    // Add more test cases for other methods in ExtendedMessageFormat class

}