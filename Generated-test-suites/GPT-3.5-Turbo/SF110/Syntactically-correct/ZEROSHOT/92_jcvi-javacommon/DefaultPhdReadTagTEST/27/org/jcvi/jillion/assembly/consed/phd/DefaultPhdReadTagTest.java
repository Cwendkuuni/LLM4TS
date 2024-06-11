import org.jcvi.jillion.core.Range;
import org.junit.Test;
import java.util.Date;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {
    @Test
    public void testGetType() {
        String type = "testType";
        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, "", null, null, "", "");
        assertEquals(type, tag.getType());
    }
    
    @Test
    public void testGetSource() {
        String source = "testSource";
        DefaultPhdReadTag tag = new DefaultPhdReadTag("", source, null, null, "", "");
        assertEquals(source, tag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        Range range = new Range(1, 10);
        DefaultPhdReadTag tag = new DefaultPhdReadTag("", "", range, null, "", "");
        assertEquals(range, tag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("", "", null, date, "", "");
        assertEquals(date, tag.getDate());
    }
    
    @Test
    public void testGetComment() {
        String comment = "testComment";
        DefaultPhdReadTag tag = new DefaultPhdReadTag("", "", null, null, comment, "");
        assertEquals(comment, tag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        String freeFormData = "testFreeFormData";
        DefaultPhdReadTag tag = new DefaultPhdReadTag("", "", null, null, "", freeFormData);
        assertEquals(freeFormData, tag.getFreeFormData());
    }
    
    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("", "", null, null, "", "");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("", "", null, null, "", "");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("", "", null, null, "", "");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("", "", null, null, "", "");
        assertTrue(tag1.equals(tag2));
    }
}