import org.junit.*;
import static org.junit.Assert.*;
import java.util.Date;
import java.util.*;
import org.jcvi.jillion.core.*;
import org.jcvi.jillion.assembly.consed.phd.*;

public class DefaultPhdReadTagTest {
    private final String type = "Type";
    private final String source = "Source";
    private final Range ungappedRange = new Range(1, 10);
    private final Date date = new Date();
    private final String comment = "Comment";
    private final String freeFormData = "Free Form Data";

    DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    
    @Test
    public void testGetType() {
        String expected = "Type";
        String actual = defaultPhdReadTag.getType();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetSource() {
        String expected = "Source";
        String actual = defaultPhdReadTag.getSource();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetUngappedRange() {
        Range expected = new Range(1, 10);
        Range actual = defaultPhdReadTag.getUngappedRange();
        assertEquals(expected.getStart(), actual.getStart());
        assertEquals(expected.getEnd(), actual.getEnd());
    }
    
    @Test
    public void testGetDate() {
        Date expected = new Date();
        Date actual = defaultPhdReadTag.getDate();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetComment() {
        String expected = "Comment";
        String actual = defaultPhdReadTag.getComment();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetFreeFormData() {
        String expected = "Free Form Data";
        String actual = defaultPhdReadTag.getFreeFormData();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals() {
        DefaultPhdReadTag sameDefaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertEquals(defaultPhdReadTag, sameDefaultPhdReadTag);
    }
    
    @Test
    public void testHashCode() {
        DefaultPhdReadTag sameDefaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertEquals(defaultPhdReadTag.hashCode(), sameDefaultPhdReadTag.hashCode());
    }
    
    @Test
    public void testNotEquals() {
        DefaultPhdReadTag differentDefaultPhdReadTag = new DefaultPhdReadTag("Different Type", "Different Source", new Range(11,20), new Date(), "Different Comment", "Different Free Form Data");
        assertNotEquals(defaultPhdReadTag, differentDefaultPhdReadTag);
    }
}