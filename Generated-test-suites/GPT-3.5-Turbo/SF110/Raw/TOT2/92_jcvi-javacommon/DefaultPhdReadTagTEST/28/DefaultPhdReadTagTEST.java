import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Date;
import org.jcvi.jillion.core.Range;

public class DefaultPhdReadTagTest {

    DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free Form Data");

    @Test
    public void testGetType() {
        assertEquals("Type", tag.getType());
    }

    @Test
    public void testGetSource() {
        assertEquals("Source", tag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        assertEquals(new Range(1, 10), tag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        assertNotNull(tag.getDate());
    }

    @Test
    public void testGetComment() {
        assertEquals("Comment", tag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        assertEquals("Free Form Data", tag.getFreeFormData());
    }

    @Test
    public void testEquals() {
        DefaultPhdReadTag sameTag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free Form Data");
        assertTrue(tag.equals(sameTag));
        
        DefaultPhdReadTag differentTag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Different Comment", "Free Form Data");
        assertFalse(tag.equals(differentTag));
    }

    @Test
    public void testHashCode() {
        DefaultPhdReadTag sameTag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free Form Data");
        assertEquals(tag.hashCode(), sameTag.hashCode());
        
        DefaultPhdReadTag differentTag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Different Comment", "Free Form Data");
        assertNotEquals(tag.hashCode(), differentTag.hashCode());
    }

}