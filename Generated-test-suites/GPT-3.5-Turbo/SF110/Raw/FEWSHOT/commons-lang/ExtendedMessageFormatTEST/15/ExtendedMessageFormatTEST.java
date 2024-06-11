package org.apache.commons.lang3.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.Format;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.junit.Test;

public class ExtendedMessageFormatTest {
    @Test
    public void testConstructor1() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("test");
        assertEquals("test", emf.toPattern());
    }

    @Test
    public void testConstructor2() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("test", Locale.US);
        assertEquals("test", emf.toPattern());
        assertEquals(Locale.US, emf.getLocale());
    }

    @Test
    public void testConstructor3() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        registry.put("number", new NumberFormatFactory());

        ExtendedMessageFormat emf = new ExtendedMessageFormat("test", registry);
        assertEquals("test", emf.toPattern());
        assertEquals(registry, emf.registry);
    }

    @Test
    public void testConstructor4() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        registry.put("number", new NumberFormatFactory());

        ExtendedMessageFormat emf = new ExtendedMessageFormat("test", Locale.US, registry);
        assertEquals("test", emf.toPattern());
        assertEquals(Locale.US, emf.getLocale());
        assertEquals(registry, emf.registry);
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("test");
        assertEquals("test", emf.toPattern());
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("test");
        emf.applyPattern("new");

        assertEquals("new", emf.toPattern());
    }

    @Test
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("test");
        emf.setFormat(0, new DateFormat());

        assertEquals("test", emf.toPattern());
    }

    @Test
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("test");
        emf.setFormatByArgumentIndex(0, new DateFormat());

        assertEquals("test", emf.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("test");
        emf.setFormats(new Format[0]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("test");
        emf.setFormatsByArgumentIndex(new Format[0]);
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("test");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("test");
        ExtendedMessageFormat emf3 = new ExtendedMessageFormat("test2");
        ExtendedMessageFormat emf4 = new ExtendedMessageFormat("test", Locale.US);
        ExtendedMessageFormat emf5 = new ExtendedMessageFormat("test", Locale.UK);

        assertTrue(emf1.equals(emf2));
        assertFalse(emf1.equals(emf3));
        assertFalse(emf1.equals(emf4));
        assertFalse(emf1.equals(emf5));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("test");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("test");
        ExtendedMessageFormat emf3 = new ExtendedMessageFormat("test2");
        ExtendedMessageFormat emf4 = new ExtendedMessageFormat("test", Locale.US);
        ExtendedMessageFormat emf5 = new ExtendedMessageFormat("test", Locale.UK);

        assertEquals(emf1.hashCode(), emf2.hashCode());
        assertNotEquals(emf1.hashCode(), emf3.hashCode());
        assertNotEquals(emf1.hashCode(), emf4.hashCode());
        assertNotEquals(emf1.hashCode(), emf5.hashCode());
    }

    @Test
    public void testGetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("test", Locale.US, new HashMap<>());
        assertNull(emf.getFormat("date"));
    }

    @Test
    public void testReadArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("test");
        assertEquals(1, emf.readArgumentIndex("{0}", new ParsePosition(0)));
    }

    private class DateFormat extends Format {}

    private class NumberFormat extends Format {}

    private class DateFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String arguments, Locale locale) {
            return new DateFormat();
        }
    }

    private class NumberFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String arguments, Locale locale) {
            return new NumberFormat();
        }
    }
}