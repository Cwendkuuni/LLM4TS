import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class DefaultPhdReadTagTest {

    private DefaultPhdReadTag defaultPhdReadTag;

    @Before
    public void setUp() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "comment";
        String freeFormData = "freeFormData";

        defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }

    @Test
    public void testGetType() {
        String expectedType = "type";
        String actualType = defaultPhdReadTag.getType();
        assertEquals(expectedType, actualType);
    }

    @Test
    public void testGetSource() {
        String expectedSource = "source";
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
        String expectedComment = "comment";
        String actualComment = defaultPhdReadTag.getComment();
        assertEquals(expectedComment, actualComment);
    }

    @Test
    public void testGetFreeFormData() {
        String expectedFreeFormData = "freeFormData";
        String actualFreeFormData = defaultPhdReadTag.getFreeFormData();
        assertEquals(expectedFreeFormData, actualFreeFormData);
    }

    @Test
    public void testHashCode() {
        int expectedHashCode = 901322503;
        int actualHashCode = defaultPhdReadTag.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testEquals() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertTrue(defaultPhdReadTag.equals(other));
    }
}