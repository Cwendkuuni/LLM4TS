
import org.junit.Test;
import java.util.Date;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        String type = "test";
        String source = "";
        Range ungappedRange = new Range(0, 10);
        Date date = new Date();
        String comment = "";
        String freeFormData = "";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        
        assert(phdReadTag.getType().equals(type));
    }
    
    @Test
    public void testGetSource() {
        // Test case for an empty source
        String type = "";
        String source = "";
        Range ungappedRange = new Range(0, 10);
        Date date = new Date();
        String comment = "";
        String freeFormData = "";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        
        assert(phdReadTag.getSource().equals(source));
        
        // Test case for a non-empty source
        source = "test";
        phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        
        assert(phdReadTag.getSource().equals(source));
    }
    
    @Test
    public void testGetUngappedRange() {
        Range ungappedRange = new Range(0, 10);
        String type = "";
        String source = "";
        Date date = new Date();
        String comment = "";
        String freeFormData = "";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        
        assert(phdReadTag.getUngappedRange().equals(ungappedRange));
    }
    
    @Test
    public void testGetDate() {
        Date date = new Date();
        String type = "";
        String source = "";
        Range ungappedRange = new Range(0, 10);
        String comment = "";
        String freeFormData = "";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        
        assert(phdReadTag.getDate().equals(date));
    }
    
    @Test
    public void testGetComment() {
        String comment = "test";
        String type = "";
        String source = "";
        Range ungappedRange = new Range(0, 10);
        Date date = new Date();
        String freeFormData = "";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        
        assert(phdReadTag.getComment().equals(comment));
    }
    
    @Test
    public void testGetFreeFormData() {
        String freeFormData = "test";
        String type = "";
        String source = "";
        Range ungappedRange = new Range(0, 10);
        Date date = new Date();
        String comment = "";
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        
        assert(phdReadTag.getFreeFormData().equals(freeFormData));
    }
    
    @Test
    public void testHashCode() {
        Range ungappedRange = new Range(0, 10);
        String type = "";
        String source = "";
        Date date = new Date();
        String comment = "";
        String freeFormData = "";
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        
        assert(phdReadTag1.hashCode() == phdReadTag2.hashCode());
    }
    
    @Test
    public void testEquals() {
        Range ungappedRange = new Range(0, 10);
        String type = "";
        String source = "";
        Date date = new Date();
        String comment = "";
        String freeFormData = "";
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        
        assert(phdReadTag1.equals(phdReadTag2));
    }
}
