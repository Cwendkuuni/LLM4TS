
import org.jcvi.jillion.core.*;
import org.junit.Assert;
import org.junit.Test;
import java.util.Date;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        Assert.assertEquals("type", tag.getType());
    }

    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        Assert.assertEquals("source", tag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        Assert.assertEquals(new Range(0, 10), tag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        Date now = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), now, "comment", "freeFormData");
        Assert.assertEquals(now, tag.getDate());
    }

    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        Assert.assertEquals("comment", tag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        Assert.assertEquals("freeFormData", tag.getFreeFormData());
    }

    //Potential edge cases and exception handling scenarios

    @Test(expected = NullPointerException.class)
    public void testConstructor_nullType() {
        new DefaultPhdReadTag(null, "source", new Range(0, 10), new Date(), "comment", "freeFormData");
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_nullSource() {
        new DefaultPhdReadTag("type", null, new Range(0, 10), new Date(), "comment", "freeFormData");
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_nullUngappedRange() {
        new DefaultPhdReadTag("type", "source", null, new Date(), "comment", "freeFormData");
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_nullDate() {
        new DefaultPhdReadTag("type", "source", new Range(0, 10), null, "comment", "freeFormData");
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_nullComment() {
        new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), null, "freeFormData");
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_nullFreeFormData() {
        new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", null);
    }
}
