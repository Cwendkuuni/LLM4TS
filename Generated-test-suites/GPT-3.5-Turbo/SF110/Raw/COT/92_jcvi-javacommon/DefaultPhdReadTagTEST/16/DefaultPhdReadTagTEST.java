import org.junit.Before;
import org.junit.Test;
import java.util.Date;
import org.jcvi.jillion.core.*;
import static org.junit.Assert.assertEquals;

public class DefaultPhdReadTagTest {
    
    private DefaultPhdReadTag phdReadTag;
    
    @Before
    public void setUp() {
        String type = "Type";
        String source = "Source";
        Range ungappedRange = new Range(0, 10);
        Date date = new Date();
        String comment = "Comment";
        String freeFormData = "Free Form Data";
        
        phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }
    
    @Test
    public void testGetType() {
        String expectedType = "Type";
        String actualType = phdReadTag.getType();
        assertEquals(expectedType, actualType);
    }
    
    @Test
    public void testGetSource() {
        String expectedSource = "Source";
        String actualSource = phdReadTag.getSource();
        assertEquals(expectedSource, actualSource);
    }
    
    @Test
    public void testGetUngappedRange() {
        Range expectedRange = new Range(0, 10);
        Range actualRange = phdReadTag.getUngappedRange();
        assertEquals(expectedRange, actualRange);
    }
    
    @Test
    public void testGetDate() {
        Date expectedDate = new Date();
        Date actualDate = phdReadTag.getDate();
        assertEquals(expectedDate, actualDate);
    }
    
    @Test
    public void testGetComment() {
        String expectedComment = "Comment";
        String actualComment = phdReadTag.getComment();
        assertEquals(expectedComment, actualComment);
    }
    
    @Test
    public void testGetFreeFormData() {
        String expectedFreeFormData = "Free Form Data";
        String actualFreeFormData = phdReadTag.getFreeFormData();
        assertEquals(expectedFreeFormData, actualFreeFormData);
    }
    
    @Test
    public void testHashCode() {
        int expectedHashCode = 123456789; // Replace with expected hash code
        int actualHashCode = phdReadTag.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }
    
    @Test
    public void testEquals() {
        DefaultPhdReadTag otherPhdReadTag = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), new Date(), "Comment", "Free Form Data");
        boolean expectedEquals = true;
        boolean actualEquals = phdReadTag.equals(otherPhdReadTag);
        assertEquals(expectedEquals, actualEquals);
    }
}