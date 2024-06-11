import static org.junit.Assert.*;
import org.junit.Test;
import org.jcvi.jillion.core.Range;
import java.util.Date;

public class DefaultPhdReadTagTest {

    private DefaultPhdReadTag defaultPhdReadTag;

    @Before
    public void setUp() {
        defaultPhdReadTag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Test comment", "Free form data");
    }

    @Test
    public void testGetType() {
        assertEquals("Type1", defaultPhdReadTag.getType());
    }

    @Test
    public void testGetSource() {
        assertEquals("Source1", defaultPhdReadTag.getSource());
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
        assertEquals("Test comment", defaultPhdReadTag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        assertEquals("Free form data", defaultPhdReadTag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Test comment", "Free form data");
        assertEquals(defaultPhdReadTag.hashCode(), other.hashCode());
    }

    @Test
    public void testEquals() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Test comment", "Free form data");
        assertTrue(defaultPhdReadTag.equals(other));
    }

    @Test
    public void testNotEquals() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("Type2", "Source2", new Range(2, 20), new Date(), "Different comment", "Different free form data");
        assertFalse(defaultPhdReadTag.equals(other));
    }

}