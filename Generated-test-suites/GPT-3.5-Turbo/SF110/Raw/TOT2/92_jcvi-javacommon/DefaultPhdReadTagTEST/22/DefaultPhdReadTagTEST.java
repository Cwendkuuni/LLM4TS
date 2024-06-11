import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Date;
import org.jcvi.jillion.core.Range;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "data");
        assertEquals("type", tag.getType());
    }

    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "data");
        assertEquals("source", tag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "data");
        assertEquals(new Range(1, 5), tag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 5), date, "comment", "data");
        assertEquals(date, tag.getDate());
    }

    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "data");
        assertEquals("comment", tag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "data");
        assertEquals("data", tag.getFreeFormData());
    }

    @Test
    public void testHashCodeAndEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "data");

        assertEquals(true, tag1.equals(tag2));
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

}