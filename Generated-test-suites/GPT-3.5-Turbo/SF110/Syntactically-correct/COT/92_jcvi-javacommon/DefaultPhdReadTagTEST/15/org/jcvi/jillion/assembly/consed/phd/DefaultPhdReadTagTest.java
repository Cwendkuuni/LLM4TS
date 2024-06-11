
import org.junit.Test;
import java.util.Date;

public class DefaultPhdReadTagTest {

    @Test
    public void getTypeTest() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(0, 10);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertSame(type, tag.getType());
    }

    @Test
    public void getSourceTest() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(0, 10);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertSame(source, tag.getSource());
    }

    @Test
    public void getUngappedRangeTest() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(0, 10);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertSame(ungappedRange, tag.getUngappedRange());
    }

    @Test
    public void getDateTest() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(0, 10);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertSame(date, tag.getDate());
    }

    @Test
    public void getCommentTest() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(0, 10);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertSame(comment, tag.getComment());
    }

    @Test
    public void getFreeFormDataTest() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(0, 10);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertSame(freeFormData, tag.getFreeFormData());
    }

    @Test
    public void hashCodeTest() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(0, 10);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void equalsTest() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(0, 10);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        assertTrue(tag1.equals(tag2));
    }
}
