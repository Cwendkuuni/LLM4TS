import org.junit.Test;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DefaultPhdReadTagTest {
  
    @Test
    public void testGetType() {
        DefaultPhdReadTag testObj = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertEquals("type", testObj.getType());
    }
  
    @Test
    public void testGetSource() {
        DefaultPhdReadTag testObj = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertEquals("source", testObj.getSource());
    }
  
    @Test
    public void testGetUngappedRange() {
        Range ungappedRange = new Range(1, 10);
        DefaultPhdReadTag testObj = new DefaultPhdReadTag("type", "source", ungappedRange, new Date(), "comment", "freeFormData");
        assertEquals(ungappedRange, testObj.getUngappedRange());
    }
  
    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag testObj = new DefaultPhdReadTag("type", "source", new Range(1, 10), date, "comment", "freeFormData");
        assertEquals(date, testObj.getDate());
    }
  
    @Test
    public void testGetComment() {
        DefaultPhdReadTag testObj = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertEquals("comment", testObj.getComment());
    }
  
    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag testObj = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertEquals("freeFormData", testObj.getFreeFormData());
    }
  
    @Test
    public void testHashCode() {
        DefaultPhdReadTag obj1 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        DefaultPhdReadTag obj2 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertEquals(obj1.hashCode(), obj2.hashCode());
    }
  
    @Test
    public void testEquals() {
        DefaultPhdReadTag obj1 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        DefaultPhdReadTag obj2 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertEquals(obj1, obj2);
    }
  
    @Test
    public void testEquals_notEqual() {
        DefaultPhdReadTag obj1 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        DefaultPhdReadTag obj2 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "different", "freeFormData");
        assertNotEquals(obj1, obj2);
    }
  
    @Test
    public void testEquals_null() {
        DefaultPhdReadTag obj1 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertNotEquals(obj1, null);
    }
  
    @Test
    public void testEquals_differentClass() {
        DefaultPhdReadTag obj1 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertNotEquals(obj1, "string");
    }
  
    @Test
    public void testEquals_sameInstance() {
        DefaultPhdReadTag obj1 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertEquals(obj1, obj1);
    }
}

