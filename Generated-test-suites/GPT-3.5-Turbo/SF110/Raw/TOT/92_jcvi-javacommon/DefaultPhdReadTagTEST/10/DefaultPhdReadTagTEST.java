import org.junit.Assert;
import org.junit.Test;
import java.util.Date;
import org.jcvi.jillion.core.Range;

public class DefaultPhdReadTagTest {
    
    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(0, 10), new Date(), "comment1", "data1");
        Assert.assertEquals("type1", tag.getType());
    }
    
    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(0, 10), new Date(), "comment1", "data1");
        Assert.assertEquals("source1", tag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        Range range = new Range(0, 10);
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", range, new Date(), "comment1", "data1");
        Assert.assertEquals(range, tag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(0, 10), date, "comment1", "data1");
        Assert.assertEquals(date, tag.getDate());
    }
    
    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(0, 10), new Date(), "comment1", "data1");
        Assert.assertEquals("comment1", tag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(0, 10), new Date(), "comment1", "data1");
        Assert.assertEquals("data1", tag.getFreeFormData());
    }
    
    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(0, 10), new Date(), "comment1", "data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", new Range(0, 10), new Date(), "comment1", "data1");
        Assert.assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(0, 10), new Date(), "comment1", "data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", new Range(0, 10), new Date(), "comment1", "data1");
        Assert.assertEquals(tag1, tag2);
    }
    
    @Test
    public void testEqualsDifferentType() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(0, 10), new Date(), "comment1", "data1");
        Object obj = new Object();
        Assert.assertNotEquals(tag1, obj);
    }
    
    @Test
    public void testEqualsDifferentComment() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(0, 10), new Date(), "comment1", "data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", new Range(0, 10), new Date(), "comment2", "data1");
        Assert.assertNotEquals(tag1, tag2);
    }
    
    @Test
    public void testEqualsDifferentDate() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(0, 10), new Date(), "comment1", "data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", new Range(0, 10), new Date(123456), "comment1", "data1");
        Assert.assertNotEquals(tag1, tag2);
    }
    
    @Test
    public void testEqualsDifferentFreeFormData() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(0, 10), new Date(), "comment1", "data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", new Range(0, 10), new Date(), "comment1", "data2");
        Assert.assertNotEquals(tag1, tag2);
    }
    
    @Test
    public void testEqualsDifferentSource() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(0, 10), new Date(), "comment1", "data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source2", new Range(0, 10), new Date(), "comment1", "data1");
        Assert.assertNotEquals(tag1, tag2);
    }
    
    @Test
    public void testEqualsDifferentType() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(0, 10), new Date(), "comment1", "data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type2", "source1", new Range(0, 10), new Date(), "comment1", "data1");
        Assert.assertNotEquals(tag1, tag2);
    }
    
    @Test
    public void testEqualsDifferentUngappedRange() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(0, 10), new Date(), "comment1", "data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", new Range(0, 11), new Date(), "comment1", "data1");
        Assert.assertNotEquals(tag1, tag2);
    }
}