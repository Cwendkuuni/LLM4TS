package org.apache.commons.lang3.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.text.Format;
import java.text.Format.Field;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.text.ExtendedMessageFormat;
import org.junit.Before;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    private static final String MESSAGE_PATTERN = "Hello {0}! Your account balance is {1,number,currency}.";

    private ExtendedMessageFormat messageFormat;

    @Before
    public void setUp() {
        messageFormat = new ExtendedMessageFormat(MESSAGE_PATTERN);
    }

    @Test
    public void testConstructor_default() {
        ExtendedMessageFormat format = new ExtendedMessageFormat(MESSAGE_PATTERN);
        assertEquals(MESSAGE_PATTERN, format.toPattern());
        assertEquals(Locale.getDefault(), format.getLocale());
        assertEquals(null, format.getFormats());
        assertEquals(null, format.getFormatsByArgumentIndex());
        assertEquals(null, format.getRegistry());
    }

    @Test
    public void testConstructor_withLocale() {
        Locale locale = Locale.US;
        ExtendedMessageFormat format = new ExtendedMessageFormat(MESSAGE_PATTERN, locale);
        assertEquals(locale, format.getLocale());
    }

    @Test
    public void testConstructor_withRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("currency", new CurrencyFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat(MESSAGE_PATTERN, registry);
        assertEquals(registry, format.getRegistry());
    }

    @Test
    public void testConstructor_withLocaleAndRegistry() {
        Locale locale = Locale.US;
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("currency", new CurrencyFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat(MESSAGE_PATTERN, locale, registry);
        assertEquals(locale, format.getLocale());
        assertEquals(registry, format.getRegistry());
    }

    @Test
    public void testToPattern() {
        String pattern = messageFormat.toPattern();
        assertEquals(MESSAGE_PATTERN, pattern);
    }

    @Test
    public void testApplyPattern() {
        String newPattern = "Hello {0}! Your account balance is {1,number,percent}.";
        messageFormat.applyPattern(newPattern);
        assertEquals(newPattern, messageFormat.toPattern());
    }

    @Test
    public void testEquals_sameInstance() {
        assertTrue(messageFormat.equals(messageFormat));
    }

    @Test
    public void testEquals_differentInstance() {
        assertFalse(messageFormat.equals(null));

        MessageFormat other = new MessageFormat(MESSAGE_PATTERN);
        assertFalse(messageFormat.equals(other));

        ExtendedMessageFormat differentPattern = new ExtendedMessageFormat("Goodbye {0}!");
        assertFalse(messageFormat.equals(differentPattern));

        ExtendedMessageFormat differentLocale = new ExtendedMessageFormat(MESSAGE_PATTERN, Locale.US);
        assertFalse(messageFormat.equals(differentLocale));

        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("currency", new CurrencyFormatFactory());
        ExtendedMessageFormat differentRegistry = new ExtendedMessageFormat(MESSAGE_PATTERN, registry);
        assertFalse(messageFormat.equals(differentRegistry));
    }

    @Test
    public void testHashCode() {
        String otherPattern = "Hello {0}! Your account balance is {1,number,currency}.";
        ExtendedMessageFormat other = new ExtendedMessageFormat(otherPattern);

        assertEquals(messageFormat.hashCode(), messageFormat.hashCode());
        assertEquals(other.hashCode(), other.hashCode());
        assertNotEquals(messageFormat.hashCode(), other.hashCode());
    }

    // TODO: add tests for other methods in ExtendedMessageFormat class

    // custom format factory for currency format
    private static class CurrencyFormatFactory implements FormatFactory {

        @Override
        public Format getFormat(String name, String arguments, Locale locale) {
            return new CurrencyFormat(locale);
        }

    }

    // custom currency format implementation
    private static class CurrencyFormat extends Format {

        private static final long serialVersionUID = 1L;
        private final Locale locale;

        public CurrencyFormat(Locale locale) {
            this.locale = locale;
        }

        @Override
        public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
            if (obj instanceof Number) {
                Number number = (Number) obj;
                DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
                DecimalFormat format = new DecimalFormat("¤#,##0.00", symbols);
                toAppendTo.append(format.format(number));
            }
            return toAppendTo;
        }

        @Override
        public Object parseObject(String source, ParsePosition pos) {
            return null;
        }

    }
}