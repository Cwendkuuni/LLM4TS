import org.junit.jupiter.api.Test;

class AttributesTest {

    @Test
    void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        
        assert(attributes.get("key").equals("value"));
    }
    
    @Test
    void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        
        assert(attributes.getIgnoreCase("kEy").equals("value"));
    }
    
    @Test
    void testPut() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        
        assert(attributes.get("key").equals("value"));
    }
    
    @Test
    void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.remove("key");
        
        assert(!attributes.hasKey("key"));
    }
    
    @Test
    void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        attributes.removeIgnoreCase("kEy");
        
        assert(!attributes.hasKey("key"));
    }
    
    @Test
    void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        
        assert(attributes.hasKey("key"));
    }
    
    @Test
    void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        
        assert(attributes.hasKeyIgnoreCase("kEy"));
    }
    
    @Test
    void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        
        assert(attributes.size() == 2);
    }
    
    @Test
    void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("key1", "value1");
        attributes1.put("key2", "value2");
        
        Attributes attributes2 = new Attributes();
        attributes2.put("key1", "value1");
        attributes2.put("key2", "value2");
        
        assert(attributes1.equals(attributes2));
    }
    
    @Test
    void testNormalize() {
        Attributes attributes = new Attributes();
        attributes.put("KeY", "value");
        attributes.normalize();
        
        assert(attributes.hasKey("key"));
    }
    
    @Test
    void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("key", "value");
        
        Attributes clone = attributes.clone();
        
        assert(clone.get("key").equals("value"));
    }
}