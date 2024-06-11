import org.junit.Assert;
import org.junit.Test;
import org.jcvi.jillion.core.Range;

import java.util.Date;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        String type = "Type";
        String source = "Source";
        Range ungappedRange = new Range(1, 5);
        Date date = new Date();
        String comment = "Comment";
        String freeFormData = "Free Form Data";

        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        Assert.assertEquals(type, defaultPhdReadTag.getType());
    }

    @Test
    public void testGetSource() {
        String type = "Type";
        String source = "Source";
        Range ungappedRange = new Range(1, 5);
        Date date = new Date();
        String comment = "Comment";
        String freeFormData = "Free Form Data";

        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        Assert.assertEquals(source, defaultPhdReadTag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        String type = "Type";
        String source = "Source";
        Range ungappedRange = new Range(1, 5);
        Date date = new Date();
        String comment = "Comment";
        String freeFormData = "Free Form Data";

        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        Assert.assertEquals(ungappedRange, defaultPhdReadTag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        String type = "Type";
        String source = "Source";
        Range ungappedRange = new Range(1, 5);
        Date date = new Date();
        String comment = "Comment";
        String freeFormData = "Free Form Data";

        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        Assert.assertEquals(date, defaultPhdReadTag.getDate());
    }

    @Test
    public void testGetComment() {
        String type = "Type";
        String source = "Source";
        Range ungappedRange = new Range(1, 5);
        Date date = new Date();
        String comment = "Comment";
        String freeFormData = "Free Form Data";

        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        Assert.assertEquals(comment, defaultPhdReadTag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        String type = "Type";
        String source = "Source";
        Range ungappedRange = new Range(1, 5);
        Date date = new Date();
        String comment = "Comment";
        String freeFormData = "Free Form Data";

        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        Assert.assertEquals(freeFormData, defaultPhdReadTag.getFreeFormData());
    }

    @Test
    public void testEquals() {
        String type = "Type";
        String source = "Source";
        Range ungappedRange = new Range(1, 5);
        Date date = new Date();
        String comment = "Comment";
        String freeFormData = "Free Form Data";

        DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(defaultPhdReadTag1, defaultPhdReadTag2);
    }

    @Test
    public void testHashCode() {
        String type = "Type";
        String source = "Source";
        Range ungappedRange = new Range(1, 5);
        Date date = new Date();
        String comment = "Comment";
        String freeFormData = "Free Form Data";

        DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        Assert.assertEquals(defaultPhdReadTag1.hashCode(), defaultPhdReadTag2.hashCode());
    }
}