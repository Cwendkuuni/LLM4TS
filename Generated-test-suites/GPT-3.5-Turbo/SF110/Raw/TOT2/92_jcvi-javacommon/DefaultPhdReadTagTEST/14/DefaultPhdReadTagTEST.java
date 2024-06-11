import static org.junit.Assert.*;
import org.junit.Test;
import org.jcvi.jillion.core.Range;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "free");
        assertEquals("type", tag.getType());
    }

    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "free");
        assertEquals("source", tag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "free");
        assertEquals(new Range(0, 10), tag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), date, "comment", "free");
        assertEquals(date, tag.getDate());
    }

    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "free");
        assertEquals("comment", tag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "free");
        assertEquals("free", tag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "free");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "free");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "free");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "free");
        assertTrue(tag1.equals(tag2));
    }

}