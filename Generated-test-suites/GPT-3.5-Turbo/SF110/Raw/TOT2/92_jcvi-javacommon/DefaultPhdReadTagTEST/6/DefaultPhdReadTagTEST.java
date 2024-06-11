import static org.junit.Assert.*;

import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.jcvi.jillion.core.Range;
import org.junit.Test;
import java.util.Date;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "FreeFormData");
        assertEquals("Type", phdReadTag.getType());
    }

    @Test
    public void testGetSource() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "FreeFormData");
        assertEquals("Source", phdReadTag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "FreeFormData");
        assertEquals(new Range(1, 10), phdReadTag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), date, "Comment", "FreeFormData");
        assertEquals(date, phdReadTag.getDate());
    }

    @Test
    public void testGetComment() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "FreeFormData");
        assertEquals("Comment", phdReadTag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "FreeFormData");
        assertEquals("FreeFormData", phdReadTag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "FreeFormData");
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "FreeFormData");
        assertEquals(phdReadTag1.hashCode(), phdReadTag2.hashCode());
    }

    @Test
    public void testEquals() {
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "FreeFormData");
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "FreeFormData");
        assertTrue(phdReadTag1.equals(phdReadTag2));
    }
}