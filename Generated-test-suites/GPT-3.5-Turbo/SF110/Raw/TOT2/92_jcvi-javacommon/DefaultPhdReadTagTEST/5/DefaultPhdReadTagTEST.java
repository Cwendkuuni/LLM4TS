import static org.junit.Assert.*;
import org.junit.Test;
import org.jcvi.jillion.core.Range;
import java.util.Date;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free Form Data");
        assertEquals("Type", tag.getType());
    }

    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free Form Data");
        assertEquals("Source", tag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free Form Data");
        assertEquals(new Range(1, 10), tag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), date, "Comment", "Free Form Data");
        assertEquals(date, tag.getDate());
    }

    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free Form Data");
        assertEquals("Comment", tag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free Form Data");
        assertEquals("Free Form Data", tag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free Form Data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free Form Data");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free Form Data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free Form Data");
        assertTrue(tag1.equals(tag2));
    }

}