import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        String expected = "Hello {0}";
        assertEquals(expected, format.toPattern());
    }
  
    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        format.applyPattern("Hi {0}");
        String expected = "Hi {0}";
        assertEquals(expected, format.toPattern());
    }
  
    @Test
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        try {
            format.setFormat(0, null);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
  
    @Test
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        try {
            format.setFormatByArgumentIndex(0, null);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
  
    @Test
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        try {
            format.setFormats(new Format[1]);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
  
    @Test
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        try {
            format.setFormatsByArgumentIndex(new Format[1]);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
  
    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello {0}");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello {0}");
    
        assertEquals(format1, format2);
    }
  
    @Test
    public void testHashCode() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello {0}");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello {0}");
    
        assertEquals(format1.hashCode(), format2.hashCode());
    }
  
    @Test
    public void testGetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        Format result = format.getFormat("date");
    
        assertNull(result);
    }
  
    @Test
    public void testReadArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        ParsePosition pos = new ParsePosition(7);
        int result = format.readArgumentIndex("Hello {0}", pos);
    
        assertEquals(0, result);
    }
  
    @Test
    public void testParseFormatDescription() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        ParsePosition pos = new ParsePosition(7);
        String result = format.parseFormatDescription("Hello {0,date,yyyy-MM-dd}", pos);
    
        assertEquals("date,yyyy-MM-dd", result);
    }
  
    @Test
    public void testInsertFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        ArrayList<String> customPatterns = new ArrayList<>();
        customPatterns.add("date,yyyy-MM-dd");
        
        String result = format.insertFormats("Hello {0}", customPatterns);
        String expected = "Hello {0,date,yyyy-MM-dd}";
      
        assertEquals(expected, result);
    }
  
    @Test
    public void testSeekNonWs() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        ParsePosition pos = new ParsePosition(6);
        String pattern = "Hello {0}";
    
        format.seekNonWs(pattern, pos);
    
        int expected = 7;
        assertEquals(expected, pos.getIndex());
    }
  
    @Test
    public void testNext() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        ParsePosition pos = new ParsePosition(6);
    
        format.next(pos);
    
        int expected = 7;
        assertEquals(expected, pos.getIndex());
    }
  
    @Test
    public void testAppendQuotedString() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        ParsePosition pos = new ParsePosition(7);
        StringBuilder appendTo = new StringBuilder();
    
        StringBuilder result = format.appendQuotedString("Hello {0}", pos, appendTo, true);
        StringBuilder expected = new StringBuilder();
        expected.append("'");
      
        assertEquals(expected.toString(), result.toString());
    }
  
    @Test
    public void testGetQuotedString() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        ParsePosition pos = new ParsePosition(7);
        
        format.getQuotedString("Hello {0}", pos, true);
      
        int expected = 7;
        assertEquals(expected, pos.getIndex());
    }
  
    @Test
    public void testContainsElements() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}");
        ArrayList<String> coll1 = new ArrayList<>();
        ArrayList<String> coll2 = new ArrayList<>();
        coll2.add("date,yyyy-MM-dd");
    
        assertFalse(format.containsElements(coll1));
        assertTrue(format.containsElements(coll2));
    }
}