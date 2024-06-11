

import org.apache.commons.lang3.text.*;
import java.util.*;
import java.text.*;
import org.junit.*;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructor1() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Hello");
        assertEquals("Hello", extendedMessageFormat.toPattern());
    }

    @Test
    public void testConstructor2() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Hello", Locale.getDefault());
        assertEquals("Hello", extendedMessageFormat.toPattern());
        assertEquals(Locale.getDefault(), extendedMessageFormat.getLocale());
    }

    @Test
    public void testConstructor3() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Hello", registry);
        assertEquals("Hello", extendedMessageFormat.toPattern());
        assertEquals(registry, extendedMessageFormat.registry);
    }

    @Test
    public void testConstructor4() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("Hello", Locale.getDefault(), registry);
        assertEquals("Hello", extendedMessageFormat.toPattern());
        assertEquals(Locale.getDefault(), extendedMessageFormat.getLocale());
        assertEquals(registry, extendedMessageFormat.registry);
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("");
        extendedMessageFormat.applyPattern("Hello");
        assertEquals("Hello", extendedMessageFormat.toPattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidFormatElement() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat("");
        extendedMessageFormat.applyPattern("Hello {0, invalidFormat}");
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat extendedMessageFormat1 = new ExtendedMessageFormat("Hello");
        ExtendedMessageFormat extendedMessageFormat2 = new ExtendedMessageFormat("Hello");
        assertTrue(extendedMessageFormat1.equals(extendedMessageFormat2));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat extendedMessageFormat1 = new ExtendedMessageFormat("Hello");
        ExtendedMessageFormat extendedMessageFormat2 = new ExtendedMessageFormat("Hello");
        assertEquals(extendedMessageFormat1.hashCode(), extendedMessageFormat2.hashCode());
    }

}

