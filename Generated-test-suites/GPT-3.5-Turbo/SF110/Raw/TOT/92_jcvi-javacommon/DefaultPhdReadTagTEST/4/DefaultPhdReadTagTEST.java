import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

class DefaultPhdReadTagTest {

    @Test
    void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertEquals("type", tag.getType());
    }
    
    @Test
    void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertEquals("source", tag.getSource());
    }
    
    @Test
    void testGetUngappedRange() {
        Range range = new Range(1, 10);
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", range, new Date(), "comment", "freeFormData");
        assertEquals(range, tag.getUngappedRange());
    }
    
    @Test
    void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 10), date, "comment", "freeFormData");
        assertEquals(date, tag.getDate());
    }
    
    @Test
    void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertEquals("comment", tag.getComment());
    }
    
    @Test
    void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertEquals("freeFormData", tag.getFreeFormData());
    }
    
    @Test
    void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertEquals(tag1, tag2);
    }
    
    @Test
    void testNotEqual() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("diffType", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertNotEquals(tag1, tag2);
    }
    
    @Test
    void testHashCodeEqual() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    void testHashCodeNotEqual() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("diffType", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertNotEquals(tag1.hashCode(), tag2.hashCode());
    }
    
}