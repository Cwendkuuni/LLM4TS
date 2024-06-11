import static org.junit.Assert.*;
import org.junit.Test;
import java.text.Format;
import java.text.FormatFactory;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.lang3.text.ExtendedMessageFormat;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructor1() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        assertEquals(Locale.getDefault(), emf.getLocale());
        assertNull(emf.registry);
        assertEquals("", emf.toPattern());
    }
    
    @Test
    public void testConstructor2() {
        Locale locale = Locale.CANADA;
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", locale);
        assertEquals(locale, emf.getLocale());
        assertNull(emf.registry);
        assertEquals("", emf.toPattern());
    }
    
    @Test
    public void testConstructor3() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", registry);
        assertEquals(Locale.getDefault(), emf.getLocale());
        assertEquals(registry, emf.registry);
        assertEquals("", emf.toPattern());
    }
    
    @Test
    public void testConstructor4() {
        Locale locale = Locale.CANADA;
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", locale, registry);
        assertEquals(locale, emf.getLocale());
        assertEquals(registry, emf.registry);
        assertEquals("", emf.toPattern());
    }
    
    @Test
    public void testApplyPatternWithoutRegistry() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        emf.applyPattern("Hello {0} {1}");
        assertEquals("Hello {0} {1}", emf.toPattern());
    }
    
    @Test
    public void testApplyPatternWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", null);
        registry.put("number", null);
        
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}, {1}");
        emf.applyPattern("Hello {0,date}, {1,number}");
        assertEquals("Hello {0,date}, {1,number}", emf.toPattern());
    }
    
    @Test
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        assertThrows(UnsupportedOperationException.class, () -> {
            emf.setFormat(0, null);
        });
    }
    
    @Test
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        assertThrows(UnsupportedOperationException.class, () -> {
            emf.setFormatByArgumentIndex(0, null);
        });
    }
    
    @Test
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        assertThrows(UnsupportedOperationException.class, () -> {
            emf.setFormats(new Format[1]);
        });
    }
    
    @Test
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        assertThrows(UnsupportedOperationException.class, () -> {
            emf.setFormatsByArgumentIndex(new Format[1]);
        });
    }
    
    @Test
    public void testEquals() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Hello {0}");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Hello {0}");
        assertTrue(emf1.equals(emf2));
    }
    
    @Test
    public void testHashCode() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Hello {0}");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Hello {0}");
        assertEquals(emf1.hashCode(), emf2.hashCode());
    }
    
    @Test
    public void testGetFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        FormatFactory factory = new FormatFactory() {
            @Override
            public Format getFormat(String name, String arg, Locale locale) {
                return null;
            }
        };
        registry.put("test", factory);
        
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}", registry);
        Format format = emf.getFormat("test");
        assertNull(format);
    }
    
    @Test
    public void testReadArgumentIndexValid() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        ParsePosition pos = new ParsePosition(7);
        int index = emf.readArgumentIndex("Hello {10}", pos);
        assertEquals(10, index);
        assertEquals(9, pos.getIndex());
    }
    
    @Test
    public void testReadArgumentIndexInvalid() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        ParsePosition pos = new ParsePosition(0);
        assertThrows(IllegalArgumentException.class, () -> {
            emf.readArgumentIndex("Hello {a}", pos);
        });
    }
    
    @Test
    public void testParseFormatDescription() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        ParsePosition pos = new ParsePosition(7);
        String desc = emf.parseFormatDescription("Hello {0,number}", pos);
        assertEquals("number", desc);
    }
    
    @Test
    public void testInsertFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        String pattern = emf.insertFormats("Hello {0}", null);
        assertEquals("Hello {0}", pattern);
    }
    
    @Test
    public void testSeekNonWs() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        ParsePosition pos = new ParsePosition(0);
        emf.seekNonWs("Hello {0}", pos);
        assertEquals(6, pos.getIndex());
    }
    
    @Test
    public void testNext() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        ParsePosition pos = new ParsePosition(0);
        emf.next(pos);
        assertEquals(1, pos.getIndex());
    }
    
    @Test
    public void testAppendQuotedString() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        ParsePosition pos = new ParsePosition(7);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = emf.appendQuotedString("Hello ''{0}", pos, sb, true);
        assertEquals("Hello '", result.toString());
        assertNull(sb);
        assertEquals(9, pos.getIndex());
    }
    
    @Test
    public void testGetQuotedString() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        ParsePosition pos = new ParsePosition(7);
        emf.getQuotedString("Hello '{0}'", pos, true);
        assertEquals(9, pos.getIndex());
    }
    
    @Test
    public void testContainsElementsEmptyCollection() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        
        assertFalse(emf.containsElements(null));
        
        assertTrue(emf.containsElements(new ArrayList<>()));
    }
    
    @Test
    public void testContainsElementsNoNullElements() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        
        List<String> list = Arrays.asList("a", "b", "c");
        assertTrue(emf.containsElements(list));
    }
    
    @Test
    public void testContainsElementsWithNullElements() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Hello {0}");
        
        List<String> list = Arrays.asList("a", null, "c");
        assertTrue(emf.containsElements(list));
    }

}