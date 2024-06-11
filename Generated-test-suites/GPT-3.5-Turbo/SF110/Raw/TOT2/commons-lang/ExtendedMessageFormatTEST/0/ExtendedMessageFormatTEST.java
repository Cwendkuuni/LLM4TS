package org.apache.commons.lang3.text;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorWithoutRegistry() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0}");
        assertEquals("{0}", emf.toPattern());
    }

    @Test
    public void testConstructorWithRegistry() {
        Map<String, FormatFactory> registry = null; // initialize your registry
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0}", registry);
        assertNotNull(emf);
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0}");
        emf.applyPattern("{1}");
        assertEquals("{1}", emf.toPattern());
    }

    // Add more test cases for other methods as needed

}