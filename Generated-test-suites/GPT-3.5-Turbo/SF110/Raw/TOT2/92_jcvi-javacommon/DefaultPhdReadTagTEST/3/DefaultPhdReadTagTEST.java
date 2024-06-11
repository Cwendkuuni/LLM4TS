import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free Form Data");
        assertEquals("Type", phdReadTag.getType());
    }

    @Test
    public void testGetSource() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free Form Data");
        assertEquals("Source", phdReadTag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free Form Data");
        assertEquals(new Range(1, 10), phdReadTag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), date, "Comment", "Free Form Data");
        assertEquals(date, phdReadTag.getDate());
    }

    @Test
    public void testGetComment() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free Form Data");
        assertEquals("Comment", phdReadTag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free Form Data");
        assertEquals("Free Form Data", phdReadTag.getFreeFormData());
    }

    // Test for hashCode method

    @Test
    public void testHashCode() {
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free Form Data");
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free Form Data");
        assertEquals(phdReadTag1.hashCode(), phdReadTag2.hashCode());
    }

    // Test for equals method

    @Test
    public void testEquals() {
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free Form Data");
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free Form Data");
        assertTrue(phdReadTag1.equals(phdReadTag2));
    }
}