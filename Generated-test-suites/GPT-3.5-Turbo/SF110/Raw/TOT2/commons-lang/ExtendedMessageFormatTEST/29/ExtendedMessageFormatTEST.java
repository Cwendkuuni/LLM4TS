package org.apache.commons.lang3.text;

import org.junit.Test;
import java.util.Locale;
import java.util.HashMap;

import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructor1() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertNotNull(emf);
    }

    @Test
    public void testConstructor2() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("", Locale.getDefault());
        assertNotNull(emf);
    }

    @Test
    public void testConstructor3() {
        HashMap<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("", registry);
        assertNotNull(emf);
    }

    @Test
    public void testConstructor4() {
        HashMap<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("", Locale.getDefault(), registry);
        assertNotNull(emf);
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        assertEquals("", emf.toPattern());
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.applyPattern("Test {0}");
        assertEquals("Test {0}", emf.toPattern());
    }

    // Add more test cases for the remaining methods

}