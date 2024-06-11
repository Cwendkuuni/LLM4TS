package org.apache.commons.lang3.text;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0} pattern", Locale.US);
        assertNotNull(emf);
        assertEquals("Test {0} pattern", emf.toPattern());
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0} pattern", registry);
        assertNotNull(emf);
        assertEquals("Test {0} pattern", emf.toPattern());
    }

    @Test
    public void testApplyPatternWithoutRegistry() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("");
        emf.applyPattern("Test {0} pattern");
        assertEquals("Test {0} pattern", emf.toPattern());
    }

    // Add more test cases for each method, edge cases, and exception handling scenarios

}