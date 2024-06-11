import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    DefaultPhdReadTag defaultPhdReadTag;

    @Before
    public void setUp() {
        defaultPhdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
    }

    @Test
    public void testGetType() {
        assertEquals("type", defaultPhdReadTag.getType());
    }

    @Test
    public void testGetSource() {
        assertEquals("source", defaultPhdReadTag.getSource());
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
        assertEquals("comment", defaultPhdReadTag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        assertEquals("freeFormData", defaultPhdReadTag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        DefaultPhdReadTag samePhdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertEquals(defaultPhdReadTag.hashCode(), samePhdReadTag.hashCode());
    }

    @Test
    public void testEquals() {
        DefaultPhdReadTag samePhdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        DefaultPhdReadTag differentPhdReadTag = new DefaultPhdReadTag("diff_type", "diff_source", new Range(11, 20), new Date(), "diff_comment", "diff_freeFormData");

        assertFalse(defaultPhdReadTag.equals(null));
        assertTrue(defaultPhdReadTag.equals(defaultPhdReadTag));
        assertTrue(defaultPhdReadTag.equals(samePhdReadTag));
        assertFalse(defaultPhdReadTag.equals(differentPhdReadTag));
    }
}