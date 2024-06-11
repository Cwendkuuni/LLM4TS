
import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Attributes;

public class AttributesTest {
    
    private final Attributes attributes = new Attributes();
    
    @Test
    public void testGet() {
        // Testing when attributes is null
        Assert.assertEquals("", attributes.get("key"));
        
        // Testing when attributes is not null
        attributes.put("key", "value");
        Assert.assertEquals("value", attributes.get("key"));
    }
    
    @Test
    public void testGetIgnoreCase() {
        // Testing when attributes is null
        Assert.assertEquals("", attributes.getIgnoreCase("key"));
        
        // Testing when attributes is not null
        attributes.put("key", "value");
        Assert.assertEquals("value", attributes.getIgnoreCase("KEY"));
    }
    
    @Test
    public void testPut() {
        // Testing when attribute is new
        attributes.put("key", "value");
        Assert.assertEquals("value", attributes.get("key"));
        
        // Testing when attribute already exists
        attributes.put("key", "new value");
        Assert.assertEquals("new value", attributes.get("key"));
    }
    
    @Test
    public void testPutBoolean() {
        // Testing when value is true
        attributes.put("key", true);
        Assert.assertEquals("key", attributes.get("key"));
        
        // Testing when value is false
        attributes.put("key", false);
        Assert.assertEquals("", attributes.get("key"));
    }
    
    @Test
    public void testPutAttribute() {
        // Testing when attribute is new
        Attributes.Attribute attr = new Attributes.Attribute("key", "value");
        attributes.put(attr);
        Assert.assertEquals("value", attributes.get("key"));
        
        // Testing when attribute already exists
        attr = new Attributes.Attribute("key", "new value");
        attributes.put(attr);
        Assert.assertEquals("new value", attributes.get("key"));
    }
    
    @Test
    public void testRemove() {
        // Testing when attributes is null
        Assert.assertEquals("", attributes.get("key"));
        
        // Testing when attributes is not null
        attributes.put("key", "value");
        attributes.remove("key");
        Assert.assertEquals("", attributes.get("key"));
    }
    
    @Test
    public void testRemoveIgnoreCase() {
        // Testing when attributes is null
        Assert.assertEquals("", attributes.get("key"));
        
        // Testing when attributes is not null
        attributes.put("key", "value");
        attributes.removeIgnoreCase("KEY");
        Assert.assertEquals("", attributes.get("key"));
    }
    
    @Test
    public void testHasKey() {
        // Testing when attributes is null
        Assert.assertFalse(attributes.hasKey("key"));
        
        // Testing when attributes is not null
        attributes.put("key", "value");
        Assert.assertTrue(attributes.hasKey("key"));
        Assert.assertFalse(attributes.hasKey("otherKey"));
    }
    
    @Test
    public void testHasKeyIgnoreCase() {
        // Testing when attributes is null
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key"));
        
        // Testing when attributes is not null
        attributes.put("key", "value");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("KEY"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("otherKey"));
    }
    
    @Test
    public void testSize() {
        // Testing when attributes is null
        Assert.assertEquals(0, attributes.size());
        
        // Testing when attributes is not null
        attributes.put("key", "value");
        Assert.assertEquals(1, attributes.size());
    }
    
    @Test
    public void testAddAll() {
        // Testing when attributes is empty
        Attributes incoming = new Attributes();
        incoming.put("key1", "value1");
        incoming.put("key2", "value2");
        attributes.addAll(incoming);
        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
        
        // Testing when attributes is not empty
        incoming = new Attributes();
        incoming.put("key3", "value3");
        attributes.addAll(incoming);
        Assert.assertEquals("value3", attributes.get("key3"));
    }
    
    @Test
    public void testIterator() {
        // Testing when attributes is null or empty
        for (Attributes.Attribute attr : attributes) {
            Assert.fail();
        }
        
        // Testing when attributes is not null or empty
        attributes.put("key", "value");
        for (Attributes.Attribute attr : attributes) {
            Assert.assertEquals("key", attr.getKey());
            Assert.assertEquals("value", attr.getValue());
        }
    }
    
    @Test
    public void testAsList() {
        // Testing when attributes is null
        Assert.assertEquals(0, attributes.asList().size());
        
        // Testing when attributes is not null
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        Assert.assertEquals(2, attributes.asList().size());
        Assert.assertEquals("key1", attributes.asList().get(0).getKey());
        Assert.assertEquals("value1", attributes.asList().get(0).getValue());
        Assert.assertEquals("key2", attributes.asList().get(1).getKey());
        Assert.assertEquals("value2", attributes.asList().get(1).getValue());
    }
    
    @Test
    public void testDataset() {
        // Testing when attributes is null or empty
        Assert.assertEquals(0, attributes.dataset().size());
        
        // Testing when attributes is not null or empty
        attributes.put("data-key1", "value1");
        attributes.put("data-key2", "value2");
        Assert.assertEquals(2, attributes.dataset().size());
        Assert.assertEquals("value1", attributes.dataset().get("key1"));
        Assert.assertEquals("value2", attributes.dataset().get("key2"));
    }
    
    @Test
    public void testHtml() {
        // Testing when attributes is null
        Assert.assertEquals("", attributes.html());
        
        // Testing when attributes is not null
        attributes.put("key", "value");
        Assert.assertEquals(" key=\"value\"", attributes.html());  
    }
    
    @Test
    public void testEquals() {
        // Testing when comparing with itself
        Assert.assertTrue(attributes.equals(attributes));
        
        // Testing when comparing with null object
        Assert.assertFalse(attributes.equals(null));
        
        // Testing when comparing with different class
        Assert.assertFalse(attributes.equals("string"));
        
        // Testing when comparing with different attributes
        Attributes other = new Attributes();
        Assert.assertFalse(attributes.equals(other));
        
        // Testing when comparing with same attributes
        other.put("key", "value");
        Assert.assertTrue(attributes.equals(other));
    }
    
    @Test
    public void testHashCode() {
        // Testing when attributes is null
        Assert.assertEquals(0, attributes.hashCode());
        
        // Testing when attributes is not null
        attributes.put("key", "value");
        Assert.assertNotEquals(0, attributes.hashCode());
    }
    
    @Test
    public void testClone() {
        // Testing when attributes is null
        Attributes clone = attributes.clone();
        Assert.assertNotSame(attributes, clone);
        Assert.assertEquals(0, clone.size());
        
        // Testing when attributes is not null
        attributes.put("key", "value");
        clone = attributes.clone();
        Assert.assertNotSame(attributes, clone);
        Assert.assertEquals("value", clone.get("key"));
    }
}
