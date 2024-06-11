
import org.jcvi.jillion.core.Range;
import org.junit.Assert;
import org.junit.Test;
import java.util.Date;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "FreeFormData");
        Assert.assertEquals("Type", tag.getType());
    }

    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "FreeFormData");
        Assert.assertEquals("Source", tag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        Range range = new Range(1, 10);
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", range, new Date(), "Comment", "FreeFormData");
        Assert.assertEquals(range, tag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), date, "Comment", "FreeFormData");
        Assert.assertEquals(date, tag.getDate());
    }

    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "FreeFormData");
        Assert.assertEquals("Comment", tag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "FreeFormData");
        Assert.assertEquals("FreeFormData", tag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "FreeFormData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "FreeFormData");
        Assert.assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "FreeFormData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "FreeFormData");
        Assert.assertEquals(tag1, tag2);
    }
}
