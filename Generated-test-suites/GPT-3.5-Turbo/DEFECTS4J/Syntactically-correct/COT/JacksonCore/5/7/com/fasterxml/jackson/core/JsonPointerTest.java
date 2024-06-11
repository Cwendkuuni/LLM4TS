
import org.junit.Assert;
import org.junit.Test;

public class JsonPointerTest {
    
    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer();
        Assert.assertTrue(pointer.matches());
        
        pointer = new JsonPointer("test", "segment", null);
        Assert.assertFalse(pointer.matches());
    }
    
    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer();
        Assert.assertEquals("", pointer.getMatchingProperty());
        
        pointer = new JsonPointer("test", "segment", null);
        Assert.assertEquals("segment", pointer.getMatchingProperty());
    }
    
    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer();
        Assert.assertEquals(-1, pointer.getMatchingIndex());
        
        pointer = new JsonPointer("test", "[0]", null);
        Assert.assertEquals(0, pointer.getMatchingIndex());
    }
    
    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        Assert.assertTrue(pointer.mayMatchProperty());
        
        pointer = new JsonPointer("test", "segment", null);
        Assert.assertTrue(pointer.mayMatchProperty());
    }
    
    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer();
        Assert.assertFalse(pointer.mayMatchElement());
        
        pointer = new JsonPointer("test", "[0]", null);
        Assert.assertTrue(pointer.mayMatchElement());
    }
    
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = new JsonPointer("test", "segment", new JsonPointer());
        JsonPointer result = pointer.matchProperty("segment");
        Assert.assertNotNull(result);
        Assert.assertEquals("", result.toString());
        
        result = pointer.matchProperty("other");
        Assert.assertNull(result);
    }
    
    @Test
    public void testMatchElement() {
        JsonPointer pointer = new JsonPointer("test", "[0]", new JsonPointer());
        JsonPointer result = pointer.matchElement(0);
        Assert.assertNotNull(result);
        Assert.assertEquals("", result.toString());
        
        result = pointer.matchElement(1);
        Assert.assertNull(result);
    }
    
    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer("test", "segment", new JsonPointer());
        JsonPointer result = pointer.tail();
        Assert.assertNotNull(result);
        Assert.assertEquals("", result.toString());
    }
    
    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer();
        Assert.assertEquals("", pointer.toString());
        
        pointer = new JsonPointer("test", "segment", null);
        Assert.assertEquals("segment", pointer.toString());
    }
    
    @Test
    public void testHashCode() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        Assert.assertEquals(pointer1.hashCode(), pointer2.hashCode());
        
        pointer1 = new JsonPointer("test", "segment", null);
        pointer2 = new JsonPointer("test", "segment", null);
        Assert.assertEquals(pointer1.hashCode(), pointer2.hashCode());
    }
    
    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer();
        Assert.assertTrue(pointer1.equals(pointer1));
        
        JsonPointer pointer2 = new JsonPointer();
        Assert.assertTrue(pointer1.equals(pointer2));
        Assert.assertTrue(pointer2.equals(pointer1));
        
        pointer2 = new JsonPointer("test", "segment", null);
        Assert.assertFalse(pointer1.equals(pointer2));
        Assert.assertFalse(pointer2.equals(pointer1));
    }
}
