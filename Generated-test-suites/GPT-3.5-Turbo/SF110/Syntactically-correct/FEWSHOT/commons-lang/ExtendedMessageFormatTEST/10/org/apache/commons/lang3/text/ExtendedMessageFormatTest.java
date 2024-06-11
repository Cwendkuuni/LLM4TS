package org.apache.commons.lang3.text;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;

public class ExtendedMessageFormatTest {
    
    @Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, {1}", Locale.US);
        assertEquals("Hello John Doe", format.format(new Object[] {"John Doe"}));
        assertEquals("Hello Jane Smith", format.format(new Object[] {"Jane Smith"}));
    }
    
    @Test
    public void testConstructorWithPatternAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormatFactory("MM/dd/yyyy"));
        registry.put("currency", new DecimalFormatFactory("$###,###.00"));
        
        ExtendedMessageFormat format = new ExtendedMessageFormat("Today's date is: {0,date} and the amount is: {1,currency}", registry);
        assertEquals("Today's date is: 01/01/2022 and the amount is: $1,000.50", format.format(new Object[] {new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-01"), 1000.50}));
    }
    
    @Test
    public void testConstructorWithPatternAndLocaleAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", new SimpleDateFormatFactory("MM/dd/yyyy"));
        registry.put("currency", new DecimalFormatFactory("$###,###.00"));
        
        ExtendedMessageFormat format = new ExtendedMessageFormat("Today's date is: {0,date} and the amount is: {1,currency}", Locale.US, registry);
        assertEquals("Today's date is: 01/01/2022 and the amount is: $1,000.50", format.format(new Object[] {new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-01"), 1000.50}));
    }
    
    @Test
    public void testGetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,date}");
        SimpleDateFormat expectedFormat = new SimpleDateFormat("MM/dd/yyyy");
        Format actualFormat = format.getFormat("date");
        assertEquals(expectedFormat, actualFormat);
    }
    
    @Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, {1}");
        String expectedPattern = "Hello {0}, {1}";
        assertEquals(expectedPattern, format.toPattern());
    }
    
    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, {1}");
        format.applyPattern("Goodbye {0}, {1}");
        String expectedPattern = "Goodbye {0}, {1}";
        assertEquals(expectedPattern, format.toPattern());
    }
    
    @Test
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, {1}");
        assertThrows(UnsupportedOperationException.class, () -> {
            format.setFormat(0, new SimpleDateFormat("MM/dd/yyyy"));
        });
    }
    
    @Test
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, {1}");
        assertThrows(UnsupportedOperationException.class, () -> {
            format.setFormatByArgumentIndex(0, new SimpleDateFormat("MM/dd/yyyy"));
        });
    }
    
    @Test
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, {1}");
        assertThrows(UnsupportedOperationException.class, () -> {
            format.setFormats(new Format[] {new SimpleDateFormat("MM/dd/yyyy"), null});
        });
    }
    
    @Test
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, {1}");
        assertThrows(UnsupportedOperationException.class, () -> {
            format.setFormatsByArgumentIndex(new Format[] {new SimpleDateFormat("MM/dd/yyyy"), null});
        });
    }
    
    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello {0}, {1}");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello {0}, {1}");
        assertEquals(format1, format2);
    }
    
    @Test
    public void testHashCode() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello {0}, {1}");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello {0}, {1}");
        assertEquals(format1.hashCode(), format2.hashCode());
    }
    
    private static class SimpleDateFormatFactory implements FormatFactory {
        
        private String pattern;
        
        public SimpleDateFormatFactory(String pattern) {
            this.pattern = pattern;
        }

        @Override
        public Format getFormat(String name, String arguments, Locale locale) {
            return new SimpleDateFormat(pattern, locale);
        }
    }
    
    private static class DecimalFormatFactory implements FormatFactory {
        
        private String pattern;
        
        public DecimalFormatFactory(String pattern) {
            this.pattern = pattern;
        }

        @Override
        public Format getFormat(String name, String arguments, Locale locale) {
            return new DecimalFormat(pattern);
        }
    }
}