package org.apache.commons.lang.test;

import static org.junit.Assert.*;

import java.text.Format;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;

public class ExtendedMessageFormatTest {

    /**
     * Test constructor ExtendedMessageFormat(String pattern).
     */
    @Test
    public void testConstructorStringPattern() {
        String pattern = "Hello {0,choice,0#World|1#Hello}!";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        assertEquals(pattern, format.toPattern());
    }

    /**
     * Test constructor ExtendedMessageFormat(String pattern, Locale locale).
     */
    @Test
    public void testConstructorStringPatternLocale() {
        String pattern = "Hello {0,choice,0#World|1#Hello}!";
        Locale locale = Locale.US;
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale);
        assertEquals(pattern, format.toPattern());
    }

    /**
     * Test constructor ExtendedMessageFormat(String pattern, Map<String, FormatFactory> registry).
     */
    @Test
    public void testConstructorStringPatternMap() {
        String pattern = "{0,number,##0.00} {1,choice,0#Euros|1#Dollars}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("currencyFormatter", new FormatFactory() {

            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                if (name.equals("currencyFormatter")) {
                    return new MessageFormat("#,##0.00") {
                        @Override
                        public String format(Object obj, Locale locale) {
                            if (obj instanceof Number) {
                                Number number = (Number) obj;
                                return String.format("%s %s", number, "EUR");
                            }
                        }
                    };
                }
                return null;
            }
        });
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        assertEquals(pattern, format.toPattern());
    }

    /**
     * Test method applyPattern(String pattern).
     */
    @Test
    public void testApplyPatternString() {
        String pattern = "{0,number,##0.00} {1,choice,0#Euros|1#Dollars}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("currencyFormatter", new FormatFactory() {

            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                if (name.equals("currencyFormatter")) {
                    return new MessageFormat("#,##0.00") {
                        @Override
                        public String format(Object obj, Locale locale) {
                            if (obj instanceof Number) {
                                Number number = (Number) obj;
                                return String.format("%s %s", number, "EUR");
                            }
                        }
                    };
                }
                return null;
            }
        });
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern(pattern);
        String testString = format.format(new Object[] { new Float(10.5f), "Euros" });
        assertEquals("10.50 Euros", testString);
    }

    /**
     * Test method toPattern().
     */
    @Test
    public void testToPattern() {
        String pattern = "{0,number,##0.00} {1,choice,0#Euros|1#Dollars}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("currencyFormatter", new FormatFactory() {

            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                if (name.equals("currencyFormatter")) {
                    return new MessageFormat("#,##0.00") {
                        @Override
                        public String format(Object obj, Locale locale) {
                            if (obj instanceof Number) {
                                Number number = (Number) obj;
                                return String.format("%s %s", number, "EUR");
                            }
                        }
                    };
                }
                return null;
            }
        });
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern(pattern);
        String testPattern = format.toPattern();
        assertTrue(testPattern.contains("{0,number,##0.00}"));
        assertTrue(testPattern.contains("{1,choice,0#Euros|1#Dollars}"));
    }

    /**
     * Test method getFormat(String formatName).
     */
    @Test
    public void testGetFormatStringFormatName() {
        String pattern = "{0,number,##0.00} {1,choice,0#Euros|1#Dollars}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("currencyFormatter", new FormatFactory() {

            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                if (name.equals("currencyFormatter")) {
                    return new MessageFormat("#,##0.00") {
                        @Override
                        public String format(Object obj, Locale locale) {
                            if (obj instanceof Number) {
                                Number number = (Number) obj;
                                return String.format("%s %s", number, "EUR");
                            }
                        }
                    };
                }
                return null;
            }
        });
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern(pattern);
        Format formatObject = format.getFormat("currencyFormatter");
        assertNotNull(formatObject);
        assertTrue(formatObject instanceof MessageFormat);
    }

    /**
     * Test method getFormat(String formatName, String formatStyle).
     */
    @Test
    public void testGetFormatStringFormatNameFormatStyle() {
        String pattern = "{0,number,##0.00} {1,choice,0#Euros|1#Dollars}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("currencyFormatter", new FormatFactory() {

            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                if (name.equals("currencyFormatter")) {
                    return new MessageFormat("#,##0.00") {
                        @Override
                        public String format(Object obj, Locale locale) {
                            if (obj instanceof Number) {
                                Number number = (Number) obj;
                                return String.format("%s %s", number, "EUR");
                            }
                        }
                    };
                }
                return null;
            }
        });
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern(pattern);
        Format formatObject = format.getFormat("currencyFormatter", "percent");
        assertNull(formatObject);
    }

    /**
     * Test method format(Object[] arguments).
     */
    @Test
    public void testFormatObjectArray() {
        String pattern = "{0,number,##0.00} {1,choice,0#Euros|1#Dollars}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("currencyFormatter", new FormatFactory() {

            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                if (name.equals("currencyFormatter")) {
                    return new MessageFormat("#,##0.00") {
                        @Override
                        public String format(Object obj, Locale locale) {
                            if (obj instanceof Number) {
                                Number number = (Number) obj;
                                return String.format("%s %s", number, "EUR");
                            }
                        }
                    };
                }
                return null;
            }
        });
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern(pattern);
        Object[] arguments = new Object[] { new Float(10.5f), "Euros" };
        String testString = format.format(arguments);
        assertEquals("10.50 Euros", testString);
    }

    /**
     * Test method format(Object[] arguments, Locale locale).
     */
    @Test
    public void testFormatObjectArrayLocale() {
        String pattern = "{0,number,##0.00} {1,choice,0#Euros|1#Dollars}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("currencyFormatter", new FormatFactory() {

            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                if (name.equals("currencyFormatter")) {
                    return new MessageFormat("#,##0.00") {
                        @Override
                        public String format(Object obj, Locale locale) {
                            if (obj instanceof Number) {
                                Number number = (Number) obj;
                                return String.format("%s %s", number,
                                        locale.getDisplayCountry());
                            }
                        }
                    };
                }
                return null;
            }
        });
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern(pattern);
        Object[] arguments = new Object[] { new Float(10.5f), null };
        String testString = format.format(arguments, Locale.GERMAN);
        assertEquals("10,50 Euro", testString);
    }

    /**
     * Test method parse(String source, ParsePosition pos).
     */
    @Test
    public void testParseStringParsePosition() {
        String pattern = "{0,number,##0.00} {1,choice,0#Euros|1#Dollars}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("currencyFormatter", new FormatFactory() {

            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                if (name.equals("currencyFormatter")) {
                    return new MessageFormat("#,##0.00") {
                        @Override
                        public Object parse(String source, ParsePosition pos) {
                            if (pos.getIndex() < source.length()) {
                                Double number = Double.parseDouble(
                                        source.substring(pos.getIndex()));
                                pos.setIndex(pos.getIndex() + source.length());
                                return number;
                            }
                            pos.setErrorIndex(pos.getIndex());
                            return null;
                        }
                    };
                }
                return null;
            }
        });
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern(pattern);
        String testString = "10.50 Dollars";
        ParsePosition pos = new ParsePosition(0);
        Object parsedObject = format.parse(testString, pos);
        assertNotNull(parsedObject);
        assertEquals(10.5, parsedObject);
    }

    /**
     * Test method parse(String source, ParsePosition pos, Locale locale).
     */
    @Test
    public void testParseStringParsePositionLocale() {
        String pattern = "{0,number,##0.00} {1,choice,0#Euros|1#Dollars}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("currencyFormatter", new FormatFactory() {

            @Override
            public Format getFormat(String name, String arguments, Locale locale) {
                if (name.equals("currencyFormatter")) {
                    return new MessageFormat("#,##0.00") {
                        @Override
                        public Object parse(String source, ParsePosition pos) {
                            if (pos.getIndex() < source.length()) {
                                Double number = Double.parseDouble(
                                        source.substring(pos.getIndex()));
                                pos.setIndex(pos.getIndex() + source.length());
                                return number;
                            }
                            pos.setErrorIndex(pos.getIndex());
                            return null;
                        }
                    };
                }
                return null;
            }
        });
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        format.applyPattern(pattern);
        String testString = "10,50 €";
        ParsePosition pos = new ParsePosition(0);
        Object parsedObject = format.parse(testString, pos, Locale.GERMAN);
        assertNotNull(parsedObject);
        assertEquals(10.5, parsedObject);
    }
}