import static org.junit.Assert.*;

import java.util.Date;

import org.jcvi.jillion.core.Range;
import org.junit.BeforeClass;
import org.junit.Test;

public class DefaultPhdReadTagTest {

    private DefaultPhdReadTag tag;

    @BeforeClass
    public void setUp() {
        Range range = new Range(1, 5);
        Date date = new Date();
        tag = new DefaultPhdReadTag("Type", "Source", range, date, "Comment", "FreeFormData");
    }

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
        Range expectedRange = new Range(1, 5);
        assertEquals(expectedRange, tag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        Date expectedDate = new Date();
        assertEquals(expectedDate, tag.getDate());
    }

    @Test
    public void testGetComment() {
        assertEquals("Comment", tag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        assertEquals("FreeFormData", tag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        DefaultPhdReadTag otherTag = new DefaultPhdReadTag("Type", "Source", new Range(1, 5), new Date(), "Comment", "FreeFormData");
        assertEquals(otherTag.hashCode(), tag.hashCode());
    }

    @Test
    public void testEquals() {
        DefaultPhdReadTag otherTag = new DefaultPhdReadTag("Type", "Source", new Range(1, 5), new Date(), "Comment", "FreeFormData");
        assertTrue(tag.equals(otherTag));
    }

}