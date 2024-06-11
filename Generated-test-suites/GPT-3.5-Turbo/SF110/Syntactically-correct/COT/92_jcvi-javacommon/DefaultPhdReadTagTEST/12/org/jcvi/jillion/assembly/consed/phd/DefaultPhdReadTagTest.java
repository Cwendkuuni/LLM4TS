import org.jcvi.jillion.core.Range;
import org.junit.Assert;
import org.junit.Test;
import java.util.Date;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 5);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(type, phdReadTag.getType());
    }

    @Test
    public void testGetSource() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 5);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(source, phdReadTag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 5);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(ungappedRange, phdReadTag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 5);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(date, phdReadTag.getDate());
    }

    @Test
    public void testGetComment() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 5);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(comment, phdReadTag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 5);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 5);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(phdReadTag1.hashCode(), phdReadTag2.hashCode());
    }

    @Test
    public void testEquals() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 5);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";

        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(phdReadTag1, phdReadTag2);
    }

    

    // Additional test cases for potential edge cases and exception handling scenarios can be added here

    
}