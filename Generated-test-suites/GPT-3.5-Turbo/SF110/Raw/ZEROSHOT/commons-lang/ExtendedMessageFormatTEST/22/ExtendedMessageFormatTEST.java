import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, the time is {1}");
        String result = format.toPattern();
        assertEquals("Hello {0}, the time is {1}", result);
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, the time is {1}");
        format.applyPattern("Greetings {0}, it is {2}");
        String result = format.toPattern();
        assertEquals("Greetings {0}, it is {2}", result);
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello {0}, the time is {1}");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello {0}, the time is {1}");
        assertTrue(format1.equals(format2));
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello {0}, the time is {1}");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello {0}, the time is {1}");
        assertEquals(format1.hashCode(), format2.hashCode());
    }

    @Test
    public void testGetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0,number}, the time is {1,date}");
        Format result1 = format.getFormat("number");
        Format result2 = format.getFormat("date");
        assertTrue(result1 instanceof NumberFormat);
        assertTrue(result2 instanceof DateFormat);
    }

    @Test
    public void testReadArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, the time is {1}");
        int result = format.readArgumentIndex("Hello {0}, the time is {1}", new ParsePosition(0));
        assertEquals(0, result);
    }

    @Test
    public void testParseFormatDescription() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0,number,###.##}, the time is {1,date,yyyy-MM-dd}");
        String result1 = format.parseFormatDescription("Hello {0,number,###.##}, the time is {1,date,yyyy-MM-dd}", new ParsePosition(0));
        String result2 = format.parseFormatDescription("Hello {0,number,###.##}, the time is {1,date,yyyy-MM-dd}", new ParsePosition(20));
        assertEquals("number,###.##", result1);
        assertEquals("date,yyyy-MM-dd", result2);
    }

    @Test
    public void testInsertFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, the time is {1}");
        ArrayList<String> customPatterns = new ArrayList<String>();
        customPatterns.add("number,###.##");
        customPatterns.add("date,yyyy-MM-dd");
        String result = format.insertFormats("Hello {0}, the time is {1}", customPatterns);
        assertEquals("Hello {0}, the time is {1,number,###.##}", result);
    }

    @Test
    public void testContainsElements() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello {0}, the time is {1}");
        ArrayList<String> collection1 = new ArrayList<String>();
        ArrayList<String> collection2 = new ArrayList<String>();
        collection2.add(null);
        assertFalse(format.containsElements(collection1));
        assertTrue(format.containsElements(collection2));
    }
}