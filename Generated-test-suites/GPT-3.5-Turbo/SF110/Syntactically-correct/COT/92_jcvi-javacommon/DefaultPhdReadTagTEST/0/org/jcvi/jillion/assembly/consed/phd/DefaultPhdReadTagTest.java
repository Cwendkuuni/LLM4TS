
import org.jcvi.jillion.core.Range;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    private DefaultPhdReadTag defaultPhdReadTag;

    private final String type = "Type";
    private final String source = "Source";
    private final Range ungappedRange = new Range(0, 10);
    private final Date date = new Date();
    private final String comment = "Comment";
    private final String freeFormData = "Free Form Data";

    @Before
    public void setUp() {
        defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test
    public void testGetType() {
        assertEquals(type, defaultPhdReadTag.getType());
    }

    @Test
    public void testGetSource() {
        assertEquals(source, defaultPhdReadTag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        assertEquals(ungappedRange, defaultPhdReadTag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        assertEquals(date, defaultPhdReadTag.getDate());
    }

    @Test
    public void testGetComment() {
        assertEquals(comment, defaultPhdReadTag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        assertEquals(freeFormData, defaultPhdReadTag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        DefaultPhdReadTag otherPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertEquals(defaultPhdReadTag.hashCode(), otherPhdReadTag.hashCode());
    }

    @Test
    public void testEquals() {
        DefaultPhdReadTag otherPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertTrue(defaultPhdReadTag.equals(otherPhdReadTag));
    }
}
