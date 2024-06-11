
import org.junit.Test;
import java.util.Date;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        String expectedType = "testType";
        DefaultPhdReadTag tag = new DefaultPhdReadTag(expectedType, null, null, null, null, null);
        String actualType = tag.getType();
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetSource() {
        String expectedSource = "testSource";
        DefaultPhdReadTag tag = new DefaultPhdReadTag(null, expectedSource, null, null, null, null);
        String actualSource = tag.getSource();
        assertEquals(expectedSource, actualSource);
    }

    @Test
    public void testGetUngappedRange() {
        Range expectedRange = new Range(1, 10);
        DefaultPhdReadTag tag = new DefaultPhdReadTag(null, null, expectedRange, null, null, null);
        Range actualRange = tag.getUngappedRange();
        assertEquals(expectedRange, actualRange);
    }

    @Test
    public void testGetDate() {
        Date expectedDate = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag(null, null, null, expectedDate, null, null);
        Date actualDate = tag.getDate();
        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void testGetComment() {
        String expectedComment = "testComment";
        DefaultPhdReadTag tag = new DefaultPhdReadTag(null, null, null, null, expectedComment, null);
        String actualComment = tag.getComment();
        assertEquals(expectedComment, actualComment);
    }

    @Test
    public void testGetFreeFormData() {
        String expectedFreeFormData = "testFreeFormData";
        DefaultPhdReadTag tag = new DefaultPhdReadTag(null, null, null, null, null, expectedFreeFormData);
        String actualFreeFormData = tag.getFreeFormData();
        assertEquals(expectedFreeFormData, actualFreeFormData);
    }

    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(null, null, null, null, null, null);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(null, null, null, null, null, null);
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(null, null, null, null, null, null);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(null, null, null, null, null, null);
        assertEquals(tag1, tag2);
    }
}
