import static org.junit.Assert.*;
import org.junit.Test;
import org.jcvi.jillion.core.*;
import java.util.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "data");
        assertEquals("type", tag.getType());
    }

    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "data");
        assertEquals("source", tag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        Range range = new Range(1, 10);
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", range, new Date(), "comment", "data");
        assertEquals(range, tag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 10), date, "comment", "data");
        assertEquals(date, tag.getDate());
    }

    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "data");
        assertEquals("comment", tag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "data");
        assertEquals("data", tag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "data");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "data");
        assertEquals(tag1, tag2);
    }
}