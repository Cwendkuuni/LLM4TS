import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), new Date(), "Comment", "FreeFormData");
        assertEquals("Type", tag.getType());
    }

    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), new Date(), "Comment", "FreeFormData");
        assertEquals("Source", tag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        Range range = new Range(0, 10);
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", range, new Date(), "Comment", "FreeFormData");
        assertEquals(range, tag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), date, "Comment", "FreeFormData");
        assertEquals(date, tag.getDate());
    }

    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), new Date(), "Comment", "FreeFormData");
        assertEquals("Comment", tag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), new Date(), "Comment", "FreeFormData");
        assertEquals("FreeFormData", tag.getFreeFormData());
    }

    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), new Date(), "Comment", "FreeFormData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), new Date(), "Comment", "FreeFormData");
        DefaultPhdReadTag tag3 = new DefaultPhdReadTag("DifferentType", "Source", new Range(0, 10), new Date(), "Comment", "FreeFormData");
        assertTrue(tag1.equals(tag2));
        assertFalse(tag1.equals(tag3));
    }

    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), new Date(), "Comment", "FreeFormData");
        assertNotNull(tag.hashCode());
    }

}