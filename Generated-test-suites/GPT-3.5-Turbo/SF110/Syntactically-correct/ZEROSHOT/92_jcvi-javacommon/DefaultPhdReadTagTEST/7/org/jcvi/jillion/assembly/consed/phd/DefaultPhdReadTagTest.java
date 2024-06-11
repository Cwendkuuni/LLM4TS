import org.jcvi.jillion.core.Range;
import org.junit.Assert;
import org.junit.Test;
import java.util.Date;

public class DefaultPhdReadTagTest {
    
    @Test
    public void testGetType() {
        String type = "Test Type";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, null, null, null, null, null);
        Assert.assertEquals(type, phdReadTag.getType());
    }
    
    @Test
    public void testGetSource() {
        String source = "Test Source";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, source, null, null, null, null);
        Assert.assertEquals(source, phdReadTag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        Range ungappedRange = new Range(1, 10);
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, null, ungappedRange, null, null, null);
        Assert.assertEquals(ungappedRange, phdReadTag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, null, null, date, null, null);
        Assert.assertEquals(date, phdReadTag.getDate());
    }
    
    @Test
    public void testGetComment() {
        String comment = "Test Comment";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, null, null, null, comment, null);
        Assert.assertEquals(comment, phdReadTag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        String freeFormData = "Test Free Form Data";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, null, null, null, null, freeFormData);
        Assert.assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }
    
    @Test
    public void testHashCodeSameObject() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, null, null, null, null, null);
        Assert.assertEquals(phdReadTag.hashCode(), phdReadTag.hashCode());
    }
    
    @Test
    public void testHashCodeEqualObjects() {
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Data");
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Data");
        Assert.assertEquals(phdReadTag1.hashCode(), phdReadTag2.hashCode());
    }
    
    @Test
    public void testEqualsSameObject() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(null, null, null, null, null, null);
        Assert.assertTrue(phdReadTag.equals(phdReadTag));
    }
    
    @Test
    public void testEqualsEqualObjects() {
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Data");
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Data");
        Assert.assertTrue(phdReadTag1.equals(phdReadTag2));
    }
    
    @Test
    public void testEqualsDifferentObjects() {
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(null, null, null, null, null, null);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Data");
        Assert.assertFalse(phdReadTag1.equals(phdReadTag2));
    }
    
    // 
    // Additional test methods can be added here for comprehensive testing
    // 
}