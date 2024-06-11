import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
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
        String freeFormData = "Free Form Data";
        
        defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }
    
    @Test
    public void testGetType() {
        String expectedType = "Type";
        String actualType = defaultPhdReadTag.getType();
        assertEquals(expectedType, actualType);
    }
    
    @Test
    public void testGetSource() {
        String expectedSource = "Source";
        String actualSource = defaultPhdReadTag.getSource();
        assertEquals(expectedSource, actualSource);
    }
    
    @Test
    public void testGetUngappedRange() {
        Range expectedRange = new Range(1, 10);
        Range actualRange = defaultPhdReadTag.getUngappedRange();
        assertEquals(expectedRange, actualRange);
    }
    
    @Test
    public void testGetDate() {
        Date expectedDate = new Date();
        Date actualDate = defaultPhdReadTag.getDate();
        assertEquals(expectedDate, actualDate);
    }
    
    @Test
    public void testGetComment() {
        String expectedComment = "Comment";
        String actualComment = defaultPhdReadTag.getComment();
        assertEquals(expectedComment, actualComment);
    }
    
    @Test
    public void testGetFreeFormData() {
        String expectedFreeFormData = "Free Form Data";
        String actualFreeFormData = defaultPhdReadTag.getFreeFormData();
        assertEquals(expectedFreeFormData, actualFreeFormData);
    }
    
    @Test
    public void testEquals() {
        DefaultPhdReadTag sameTag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free Form Data");
        assertTrue(defaultPhdReadTag.equals(sameTag));
    }
    
    @Test
    public void testNotEquals() {
        DefaultPhdReadTag differentTag = new DefaultPhdReadTag("Different Type", "Different Source", new Range(0, 5), new Date(), "Different Comment", "Different Free Form Data");
        assertFalse(defaultPhdReadTag.equals(differentTag));
    }
    
    @Test
    public void testHashCode() {
        DefaultPhdReadTag sameTag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free Form Data");
        assertEquals(defaultPhdReadTag.hashCode(), sameTag.hashCode());
    }
    
    @Test
    public void testHashCodeDifferent() {
        DefaultPhdReadTag differentTag = new DefaultPhdReadTag("Different Type", "Different Source", new Range(0, 5), new Date(), "Different Comment", "Different Free Form Data");
        assertNotEquals(defaultPhdReadTag.hashCode(), differentTag.hashCode());
    }
}