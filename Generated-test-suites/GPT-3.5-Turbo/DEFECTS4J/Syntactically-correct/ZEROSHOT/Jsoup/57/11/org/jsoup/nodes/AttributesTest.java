import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AttributesTest {
    
    private Attributes attributes;
    
    @Before
    public void setUp() {
        attributes = new Attributes();
    }
    
    @Test
    public void testGet() {
        attributes.put("key1", "value1");
        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("", attributes.get("key2"));
    }
    
    @Test
    public void testGetIgnoreCase() {
        attributes.put("key1", "value1");
        Assert.assertEquals("value1", attributes.getIgnoreCase("key1"));
        Assert.assertEquals("value1", attributes.getIgnoreCase("KEY1"));
        Assert.assertEquals("", attributes.getIgnoreCase("key2"));
    }
    
    @Test
    public void testPut() {
        attributes.put("key1", "value1");
        Assert.assertEquals("value1", attributes.get("key1"));
    }
    
    @Test
    public void testPut_Bool() {
        attributes.put("key1", true);
        Assert.assertEquals("key1=\"key1\"", attributes.get("key1"));
        
        attributes.put("key1", false);
        Assert.assertEquals("", attributes.get("key1"));
    }
    
    @Test
    public void testPut_Attribute() {
        Attribute attribute1 = new Attribute("key1", "value1");
        attributes.put(attribute1);
        Assert.assertEquals("value1", attributes.get("key1"));
    }
    
    @Test
    public void testRemove() {
        attributes.put("key1", "value1");
        attributes.remove("key1");
        Assert.assertEquals("", attributes.get("key1"));
    }
    
    @Test
    public void testRemoveIgnoreCase() {
        attributes.put("key1", "value1");
        attributes.removeIgnoreCase("KEY1");
        Assert.assertEquals("", attributes.get("key1"));
    }
    
    @Test
    public void testHasKey() {
        attributes.put("key1", "value1");
        Assert.assertTrue(attributes.hasKey("key1"));
        Assert.assertFalse(attributes.hasKey("key2"));
    }
    
    @Test
    public void testHasKeyIgnoreCase() {
        attributes.put("key1", "value1");
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key1"));
        Assert.assertTrue(attributes.hasKeyIgnoreCase("KEY1"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key2"));
    }
    
    @Test
    public void testSize() {
        Assert.assertEquals(0, attributes.size());
        attributes.put("key1", "value1");
        Assert.assertEquals(1, attributes.size());
    }
    
    @Test
    public void testAddAll() {
        Attributes incoming = new Attributes();
        incoming.put("key1", "value1");
        incoming.put("key2", "value2");
        
        attributes.addAll(incoming);
        Assert.assertEquals(2, attributes.size());
        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
    }
    
    @Test
    public void testIterator() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        Iterator<Attribute> iterator = attributes.iterator();
        Assert.assertTrue(iterator.hasNext());
        Attribute attribute1 = iterator.next();
        Assert.assertEquals("key1", attribute1.getKey());
        Assert.assertEquals("value1", attribute1.getValue());
        
        Assert.assertTrue(iterator.hasNext());
        Attribute attribute2 = iterator.next();
        Assert.assertEquals("key2", attribute2.getKey());
        Assert.assertEquals("value2", attribute2.getValue());
        
        Assert.assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testAsList() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        List<Attribute> list = attributes.asList();
        Assert.assertEquals(2, list.size());
        
        Attribute attribute1 = list.get(0);
        Assert.assertEquals("key1", attribute1.getKey());
        Assert.assertEquals("value1", attribute1.getValue());
        
        Attribute attribute2 = list.get(1);
        Assert.assertEquals("key2", attribute2.getKey());
        Assert.assertEquals("value2", attribute2.getValue());
    }
    
    @Test
    public void testDataset() {
        attributes.put("data-key1", "value1");
        Map<String, String> dataset = attributes.dataset();
        
        Assert.assertEquals(1, dataset.size());
        Assert.assertEquals("value1", dataset.get("key1"));
    }
    
    @Test
    public void testHtml() {
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        String html = attributes.html();
        Assert.assertEquals(" key1=\"value1\" key2=\"value2\"", html);
    }
    
    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        
        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value2");
        
        Assert.assertTrue(attributes1.equals(attributes1));
        Assert.assertTrue(attributes1.equals(attributes2));
        Assert.assertFalse(attributes1.equals(attributes3));
        Assert.assertFalse(attributes1.equals(null));
    }
    
    @Test
    public void testHashCode() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        
        Assert.assertEquals(attributes1.hashCode(), attributes2.hashCode());
    }
    
    @Test
    public void testClone() {
        attributes.put("key1", "value1");
        Attributes clone = attributes.clone();
        
        Assert.assertTrue(attributes.equals(clone));
        Assert.assertNotSame(attributes, clone);
    }
    
}