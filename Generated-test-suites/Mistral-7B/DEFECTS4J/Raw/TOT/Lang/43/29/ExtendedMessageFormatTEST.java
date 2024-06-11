package org.apache.commons.lang.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;

/**
 * Test class for {@link ExtendedMessageFormat}.
 */
public class ExtendedMessageFormatTest {

    /** Test registry. */
    private static final Map<String, FormatFactory> registry = new HashMap<>();

    static {
        registry.put("testFormat1", new TestFormatFactory("{0, testFormat1}"));
        registry.put("testFormat2", new TestFormatFactory("{0, choice, 0#Test choice 0|1#Test choice 1}({1, testFormat1})"));
    }

    /**
     * Test method for {@link ExtendedMessageFormat#ExtendedMessageFormat(String, Map)}.
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testExtendedMessageFormat() throws Exception {
        // Test with valid patterns
        ExtendedMessageFormat format = new ExtendedMessageFormat(
                "{0, testFormat1}", registry);
        assertEquals("Test choice 0", format.format(new Object[]{"Test choice 0"}));
        format = new ExtendedMessageFormat(
                "{0, choice, 0#Test choice 0|1#Test choice 1}({1, testFormat1})", registry);
        assertEquals("Test choice 0Test choice 0", format.format(new Object[]{"Test choice 0", "Test choice 0"}));
        assertEquals("Test choice 1Test choice 0", format.format(new Object[]{"Test choice 1", "Test choice 0"}));

        // Test with invalid patterns
        assertThrows(IllegalArgumentException.class, () -> new ExtendedMessageFormat(
                "{0, testFormat1}({1, testFormat1})", registry)); // missing format name
        assertThrows(IllegalArgumentException.class, () -> new ExtendedMessageFormat(
                "{0, testFormat1}({1, testFormat1}testFormat2)", registry)); // wrong format style
        assertThrows(IllegalArgumentException.class, () -> new ExtendedMessageFormat(
                "{0, testFormat1}({1, testFormat2})", registry)); // unknown format factory

        // Test with null arguments
        assertNull(format.format(null));
        assertNull(format.format(new Object[0]));

        // Test with invalid arguments
        assertNull(format.format(new Object[]{null}));
        assertNull(format.format(new Object[]{"Test choice 0", null}));

        // Test with empty format factory
        registry.clear();
        assertThrows(IllegalArgumentException.class, () -> new ExtendedMessageFormat(
                "{0, testFormat1}", registry));
    }

    /**
     * Test format factory.
     */
    private static class TestFormatFactory implements FormatFactory {

        /** Pattern. */
        private final String pattern;

        /**
         * Constructor.
         * @param pattern pattern
         */
        TestFormatFactory(String pattern) {
            this.pattern = pattern;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Format getFormat(String formatName, String formatStyle, Locale locale) {
            if (!formatName.equals("testFormat1")) {
                throw new IllegalArgumentException("Unknown format name: " + formatName);
            }
            return new TestFormat(pattern);
        }

        /** Test format. */
        private static class TestFormat extends Format {

            /** Pattern. */
            private final String pattern;

            /**
             * Constructor.
             * @param pattern pattern
             */
            TestFormat(String pattern) {
                super();
                this.pattern = pattern;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public Object format(Object obj, Locale locale, String format, ParsePosition parsePosition)
                    throws ParseException, IllegalFormatException {
                throw new UnsupportedOperationException("Not implemented.");
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public String format(Object obj, Locale locale) {
                return format(obj, locale, null);
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public ParsePosition parseObject(String source, ParsePosition pos) {
                throw new UnsupportedOperationException("Not implemented.");
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public String toPattern() {
                return pattern;
            }
        }
    }
}