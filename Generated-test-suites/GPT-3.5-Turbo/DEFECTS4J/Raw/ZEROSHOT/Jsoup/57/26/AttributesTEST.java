import org.junit.Assert;
import org.junit.Test;

public class AttributesTest {
    
    @Test
    public void testGet() {
        Attributes attr = new Attributes();
        attr.put("key", "value");
        Assert.assertEquals("value", attr.get("key"));
    }
    
    @Test
    public void testGetIgnoreCase() {
        Attributes attr = new Attributes();
        attr.put("key", "value");
        Assert.assertEquals("value", attr.getIgnoreCase("KEY"));
    }
    
    @Test
    public void testPut() {
        Attributes attr = new Attributes();
        attr.put("key", "value");
        Assert.assertTrue(attr.hasKey("key"));
    }
    
    @Test
    public void testPutBoolean() {
        Attributes attr = new Attributes();
        attr.put("key", true);
        Assert.assertTrue(attr.hasKey("key"));
        attr.put("key", false);
        Assert.assertFalse(attr.hasKey("key"));
    }
    
    @Test
    public void testRemove() {
        Attributes attr = new Attributes();
        attr.put("key", "value");
        attr.remove("key");
        Assert.assertFalse(attr.hasKey("key"));
    }
    
    @Test
    public void testRemoveIgnoreCase() {
        Attributes attr = new Attributes();
        attr.put("key", "value");
        attr.removeIgnoreCase("KEY");
        Assert.assertFalse(attr.hasKey("key"));
    }
    
    @Test
    public void testHasKey() {
        Attributes attr = new Attributes();
        attr.put("key", "value");
        Assert.assertTrue(attr.hasKey("key"));
    }
    
    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attr = new Attributes();
        attr.put("key", "value");
        Assert.assertTrue(attr.hasKeyIgnoreCase("KEY"));
    }
    
    @Test
    public void testSize() {
        Attributes attr = new Attributes();
        attr.put("key1", "value1");
        attr.put("key2", "value2");
        Assert.assertEquals(2, attr.size());
    }
    
    @Test
    public void testAddAll() {
        Attributes attr1 = new Attributes();
        attr1.put("key1", "value1");
        
        Attributes attr2 = new Attributes();
        attr2.put("key2", "value2");
        
        attr1.addAll(attr2);
        Assert.assertTrue(attr1.hasKey("key2"));
    }
    
    @Test
    public void testIterator() {
        Attributes attr = new Attributes();
        attr.put("key1", "value1");
        attr.put("key2", "value2");
        
        int count = 0;
        for (Attribute attribute : attr) {
            count++;
        }
        Assert.assertEquals(2, count);
    }
    
    @Test
    public void testAsList() {
        Attributes attr = new Attributes();
        attr.put("key1", "value1");
        attr.put("key2", "value2");
        
        Assert.assertEquals(2, attr.asList().size());
        Assert.assertEquals("key1", attr.asList().get(0).getKey());
        Assert.assertEquals("value2", attr.asList().get(1).getValue());
    }
    
    @Test
    public void testDataset() {
        Attributes attr = new Attributes();
        attr.put("data-key", "value");
        
        Map<String, String> dataset = attr.dataset();
        Assert.assertEquals(1, dataset.size());
        Assert.assertEquals("value", dataset.get("key"));
    }
    
    @Test
    public void testHtml() {
        Attributes attr = new Attributes();
        attr.put("key1", "value1");
        attr.put("key2", "value2");
        
        Assert.assertEquals(" key1=\"value1\" key2=\"value2\"", attr.html());
    }
    
    @Test
    public void testEquals() {
        Attributes attr1 = new Attributes();
        attr1.put("key1", "value1");
        attr1.put("key2", "value2");
        
        Attributes attr2 = new Attributes();
        attr2.put("key1", "value1");
        attr2.put("key2", "value2");
        
        Assert.assertTrue(attr1.equals(attr2));
    }
    
    @Test
    public void testHashCode() {
        Attributes attr1 = new Attributes();
        attr1.put("key1", "value1");
        attr1.put("key2", "value2");
        
        Attributes attr2 = new Attributes();
        attr2.put("key1", "value1");
        attr2.put("key2", "value2");
        
        Assert.assertEquals(attr1.hashCode(), attr2.hashCode());
    }
    
    @Test
    public void testClone() {
        Attributes attr1 = new Attributes();
        attr1.put("key1", "value1");
        
        Attributes attr2 = attr1.clone();
        
        Assert.assertEquals(attr1, attr2);
    }
}