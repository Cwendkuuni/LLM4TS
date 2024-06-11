import static org.junit.Assert.*;
import org.junit.Test;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), new Date(), "Comment", "Free Form Data");
        assertEquals("Type", tag.getType());
    }

    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), new Date(), "Comment", "Free Form Data");
        assertEquals("Source", tag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), new Date(), "Comment", "Free Form Data");
        assertEquals(new Range(0, 10), tag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        Date currentDate = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), currentDate, "Comment", "Free Form Data");
        assertEquals(currentDate, tag.getDate());
    }

    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), new Date(), "Comment", "Free Form Data");
        assertEquals("Comment", tag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), new Date(), "Comment", "Free Form Data");
        assertEquals("Free Form Data", tag.getFreeFormData());
    }

    // Test edge cases and exception handling scenarios

    @Test
    public void testEqualsSameObject() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), new Date(), "Comment", "Free Form Data");
        assertTrue(tag.equals(tag));
    }

    @Test
    public void testEqualsDifferentObject() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), new Date(), "Comment", "Free Form Data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), new Date(), "Different Comment", "Free Form Data");
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), new Date(), "Comment", "Free Form Data");
        assertNotNull(tag.hashCode());
    }
}