import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jcvi.jillion.core.Range;
import java.util.Date;

public class DefaultPhdReadTagTest {

    private DefaultPhdReadTag defaultPhdReadTag;

    @Before
    public void setUp() {
        String type = "Type";
        String source = "Source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "Comment";
        String freeFormData = "FreeForm";
        defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test
    public void testGetType() {
        assertEquals("Type", defaultPhdReadTag.getType());
    }

    @Test
    public void testGetSource() {
        assertEquals("Source", defaultPhdReadTag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        assertEquals(new Range(1, 10), defaultPhdReadTag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        assertNotNull(defaultPhdReadTag.getDate());
    }

    @Test
    public void testGetComment() {
        assertEquals("Comment", defaultPhdReadTag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        assertEquals("FreeForm", defaultPhdReadTag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        assertNotNull(defaultPhdReadTag.hashCode());
    }

    @Test
    public void testEquals() {
        assertTrue(defaultPhdReadTag.equals(defaultPhdReadTag));
    }
}