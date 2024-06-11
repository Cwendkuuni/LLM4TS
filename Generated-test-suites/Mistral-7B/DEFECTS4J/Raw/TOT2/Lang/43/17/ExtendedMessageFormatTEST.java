package org.apache.commons.lang.text;

import org.apache.commons.lang.Validate;
import org.junit.Test;
import static org.junit.Assert.*;

import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ExtendedMessageFormatTest {

 // Test constructor with default locale and no registry
 @Test
 public void testDefaultConstructor() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, short} {1} {2, number, currency}");
 assertEquals("{0, date, short} {1} {2, number, currency}", format.toPattern());
 }

 // Test constructor with custom registry
 static class CustomFormatFactory implements FormatFactory {
 Format getFormat(String name, String arg, Locale locale) {
 throw new UnsupportedOperationException();
 }
 }

 @Test
 public void testCustomRegistryConstructor() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("CustomFormatFactory", new CustomFormatFactory());
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0, CustomFormatFactory} {1}", new Locale("en", "US"), registry);
 assertEquals("{0, CustomFormatFactory} {1}", format.toPattern());
 }

 // Test basic functionality of methods
 @Test
 public void testApplyPattern() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, short} {1} {2, number, currency}", new Locale("en", "US"));
 format.applyPattern("{0, date, short} {1} {2, number, currency}");
 Object[] args = {"12/25/2019", "Merry Christmas", 1234.56};
 Object[] expected = {"12/25/2019", "Merry Christmas", "$1,234.56"};
 assertArrayEquals(expected, format.format(args));
 }

 // Test edge cases for toPattern()
 @Test
 public void testToPatternEdgeCases() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, short} {1} {2, number, currency}", new Locale("en", "US"));
 format.applyPattern("{0, date, short} {1} {2, number, currency}");
 String pattern = format.toPattern();
 assertTrue(pattern.contains("{0, date, short}"));
 assertTrue(pattern.contains("{1}"));
 assertTrue(pattern.contains("{2, number, currency}"));
 }

 // Test edge cases for format() with null arguments
 @Test(expected = IllegalArgumentException.class)
 public void testFormatNullArguments() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, short} {1} {2, number, currency}", new Locale("en", "US"));
 format.applyPattern("{0, date, short} {1} {2, number, currency}");
 format.format(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatNullArgumentsFirst() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, short} {1} {2, number, currency}", new Locale("en", "US"));
 format.applyPattern("{0, date, short} {1} {2, number, currency}");
 Object[] args = {null, "Merry Christmas", 1234.56};
 format.format(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatNullArgumentsLast() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, short} {1} {2, number, currency}", new Locale("en", "US"));
 format.applyPattern("{0, date, short} {1} {2, number, currency}");
 Object[] args = {"Merry Christmas", null, 1234.56};
 format.format(args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFormatNullArgumentsMiddle() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, short} {1} {2, number, currency}", new Locale("en", "US"));
 format.applyPattern("{0, date, short} {1} {2, number, currency}");
 Object[] args = {"Merry Christmas", null, null};
 format.format(args);
 }

 // Test edge cases for format() with empty arguments
 @Test
 public void testFormatEmptyArguments() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, short} {1} {2, number, currency}", new Locale("en", "US"));
 format.applyPattern("{0, date, short} {1} {2, number, currency}");
 Object[] args = {};
 String expected = "";
 assertEquals(expected, format.format(args));
 }

 // Test edge cases for format() with zero-length arguments
 @Test
 public void testFormatZeroLengthArguments() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, short} {1} {2, number, currency}", new Locale("en", "US"));
 format.applyPattern("{0, date, short} {1} {2, number, currency}");
 String[] args0 = {"", "", ""};
 String expected0 = "";
 assertEquals(expected0, format.format(args0));
 Object[] args1 = {new String[0], "", ""};
 String expected1 = "";
 assertEquals(expected1, format.format(args1));
 Object[] args2 = {"", new Object[0], ""};
 String expected2 = "";
 assertEquals(expected2, format.format(args2));
 Object[] args3 = {"", "", new Object[0]};
 String expected3 = "";
 assertEquals(expected3, format.format(args3));
 Object[] args4 = {new Object[0], new Object[0], new Object[0]};
 String expected4 = "";
 assertEquals(expected4, format.format(args4));
 }

 // Test edge cases for format() with empty registry
 @Test
 public void testFormatEmptyRegistry() {
 Map<String, FormatFactory> registry = new HashMap<>();
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0, CustomFormatFactory} {1}", new Locale("en", "US"), registry);
 format.applyPattern("{0, CustomFormatFactory} {1}");
 Object[] args = {"Merry Christmas", "Happy New Year"};
 String expected = "{Merry Christmas, Happy New Year}";
 assertEquals(expected, format.format(args));
 }

 // Test edge cases for format() with null registry
 @Test(expected = NullPointerException.class)
 public void testFormatNullRegistry() {
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0, CustomFormatFactory} {1}", new Locale("en", "US"), null);
 format.applyPattern("{0, CustomFormatFactory} {1}");
 Object[] args = {"Merry Christmas", "Happy New Year"};
 format.format(args);
 }

 // Test edge cases for format() with non-existent format name in registry
 @Test(expected = IllegalArgumentException.class)
 public void testFormatNonExistentFormatName() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("CustomFormatFactory", new CustomFormatFactory());
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0, NonExistentFormatName} {1}", new Locale("en", "US"), registry);
 format.applyPattern("{0, NonExistentFormatName} {1}");
 Object[] args = {"Merry Christmas", "Happy New Year"};
 format.format(args);
 }

 // Test edge cases for format() with invalid format style in registry
 @Test(expected = IllegalArgumentException.class)
 public void testFormatInvalidFormatStyle() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("CustomFormatFactory", new CustomFormatFactory());
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0, CustomFormatFactory(InvalidStyle)} {1}", new Locale("en", "US"), registry);
 format.applyPattern("{0, CustomFormatFactory(InvalidStyle)} {1}");
 Object[] args = {"Merry Christmas", "Happy New Year"};
 format.format(args);
 }

 // Test edge cases for format() with empty format description in registry
 @Test
 public void testFormatEmptyFormatDescription() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("CustomFormatFactory", new CustomFormatFactory());
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0, CustomFormatFactory} {1}", new Locale("en", "US"), registry);
 format.applyPattern("{0, CustomFormatFactory} {1}");
 Object[] args = {"Merry Christmas", "Happy New Year"};
 String expected = "{Merry Christmas, Happy New Year}";
 assertEquals(expected, format.format(args));
 }

 // Test edge cases for format() with null format description in registry
 @Test(expected = NullPointerException.class)
 public void testFormatNullFormatDescription() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("CustomFormatFactory", new CustomFormatFactory());
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0, CustomFormatFactory} {1}", new Locale("en", "US"), registry);
 format.applyPattern("{0, CustomFormatFactory} {1}");
 Object[] args = {"Merry Christmas", "Happy New Year"};
 format.format(args);
 }

 // Test edge cases for format() with empty custom format in registry
 @Test
 public void testFormatEmptyCustomFormat() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("CustomFormatFactory", new CustomFormatFactory());
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0, CustomFormatFactory} {1}", new Locale("en", "US"), registry);
 format.applyPattern("{0, CustomFormatFactory} {1}");
 Object[] args = {"Merry Christmas", "Happy New Year"};
 String expected = "{Merry Christmas, Happy New Year}";
 assertEquals(expected, format.format(args));
 }

 // Test edge cases for format() with null custom format in registry
 @Test(expected = NullPointerException.class)
 public void testFormatNullCustomFormat() {
 Map<String, FormatFactory> registry = new HashMap<>();
 registry.put("CustomFormatFactory", new CustomFormatFactory());
 ExtendedMessageFormat format = new ExtendedMessageFormat("{0, CustomFormatFactory} {1}", new Locale("en", "US"), registry);
 format.applyPattern("{0, CustomFormatFactory} {1}");
 Object[] args = {"Merry Christmas", "Happy New Year"};
 format.format(args);
 }
}