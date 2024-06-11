import org.junit.Test;
import org.apache.commons.lang3.text.ExtendedMessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.text.Format;
import java.text.FormatFactory;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {
    
    @Test
    public void testConstructor1_withValidPattern() {
        ExtendedMessageFormat formatter = new ExtendedMessageFormat("Hello, {0}!");
        assertNotNull(formatter);
    }

    @Test
    public void testConstructor1_withEmptyPattern() {
        ExtendedMessageFormat formatter = new ExtendedMessageFormat("");
        assertNotNull(formatter);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor1_withNullPattern() {
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(null);
    }

    @Test
    public void testConstructor2_withValidPatternAndLocale() {
        ExtendedMessageFormat formatter = new ExtendedMessageFormat("Hello, {0}!", Locale.US);
        assertNotNull(formatter);
    }

    @Test
    public void testConstructor2_withEmptyPatternAndLocale() {
        ExtendedMessageFormat formatter = new ExtendedMessageFormat("", Locale.US);
        assertNotNull(formatter);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor2_withNullPatternAndLocale() {
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(null, Locale.US);
    }

    @Test
    public void testConstructor3_withValidPatternAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        registry.put("number", new NumberFormatFactory());
        ExtendedMessageFormat formatter = new ExtendedMessageFormat("Today is {0,date}, and 5 is {1,number}", registry);
        assertNotNull(formatter);
    }

    @Test
    public void testConstructor3_withEmptyPatternAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        registry.put("number", new NumberFormatFactory());
        ExtendedMessageFormat formatter = new ExtendedMessageFormat("", registry);
        assertNotNull(formatter);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor3_withNullPatternAndRegistry() {
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(null, null);
    }

    @Test
    public void testConstructor4_withValidPatternAndLocaleAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        registry.put("number", new NumberFormatFactory());
        ExtendedMessageFormat formatter = new ExtendedMessageFormat("Today is {0,date}, and 5 is {1,number}", Locale.US, registry);
        assertNotNull(formatter);
    }

    @Test
    public void testConstructor4_withEmptyPatternAndLocaleAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new DateFormatFactory());
        registry.put("number", new NumberFormatFactory());
        ExtendedMessageFormat formatter = new ExtendedMessageFormat("", Locale.US, registry);
        assertNotNull(formatter);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor4_withNullPatternAndLocaleAndRegistry() {
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(null, null, null);
    }

    @Test
    public void testToPattern_withValidPattern() {
        String pattern = "Hello, {0}!";
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern);
        assertEquals(pattern, formatter.toPattern());
    }

    @Test
    public void testToPattern_withEmptyPattern() {
        String pattern = "";
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern);
        assertEquals(pattern, formatter.toPattern());
    }

    @Test
    public void testApplyPattern_withValidPattern() {
        String pattern = "Hello, {0}!";
        ExtendedMessageFormat formatter = new ExtendedMessageFormat("");
        formatter.applyPattern(pattern);
        assertEquals(pattern, formatter.toPattern());
    }

    @Test
    public void testApplyPattern_withEmptyPattern() {
        String pattern = "";
        ExtendedMessageFormat formatter = new ExtendedMessageFormat("");
        formatter.applyPattern(pattern);
        assertEquals(pattern, formatter.toPattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPattern_withInvalidPattern() {
        String pattern = "Hello, {0";
        ExtendedMessageFormat formatter = new ExtendedMessageFormat("");
        formatter.applyPattern(pattern);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat_withIndexAndFormat() {
        ExtendedMessageFormat formatter = new ExtendedMessageFormat("");
        formatter.setFormat(0, new DateFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex_withIndexAndFormat() {
        ExtendedMessageFormat formatter = new ExtendedMessageFormat("");
        formatter.setFormatByArgumentIndex(0, new DateFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats_withFormats() {
        ExtendedMessageFormat formatter = new ExtendedMessageFormat("");
        formatter.setFormats(new Format[]{new DateFormat()});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex_withFormats() {
        ExtendedMessageFormat formatter = new ExtendedMessageFormat("");
        formatter.setFormatsByArgumentIndex(new Format[]{new DateFormat()});
    }

    @Test
    public void testEquals_withSameObject() {
        ExtendedMessageFormat formatter = new ExtendedMessageFormat("");
        assertTrue(formatter.equals(formatter));
    }

    @Test
    public void testEquals_withEqualObjects() {
        ExtendedMessageFormat formatter1 = new ExtendedMessageFormat("Hello, {0}!");
        ExtendedMessageFormat formatter2 = new ExtendedMessageFormat("Hello, {0}!");
        assertTrue(formatter1.equals(formatter2));
    }

    @Test
    public void testEquals_withDifferentObjects() {
        ExtendedMessageFormat formatter1 = new ExtendedMessageFormat("Hello, {0}!");
        ExtendedMessageFormat formatter2 = new ExtendedMessageFormat("Today is {0,date}, and 5 is {1,number}");
        assertFalse(formatter1.equals(formatter2));
    }
    
    @Test
    public void testToString_withValidPattern() {
        String pattern = "Hello, {0}!";
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern);
        assertEquals("ExtendedMessageFormat: pattern='" + pattern + "'", formatter.toString());
    }

    @Test
    public void testToString_withEmptyPattern() {
        String pattern = "";
        ExtendedMessageFormat formatter = new ExtendedMessageFormat(pattern);
        assertEquals("ExtendedMessageFormat: pattern='" + pattern + "'", formatter.toString());
    }
}