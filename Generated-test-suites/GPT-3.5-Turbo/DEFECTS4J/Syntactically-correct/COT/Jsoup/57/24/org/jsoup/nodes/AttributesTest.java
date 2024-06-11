
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.junit.Assert;

public class AttributesTest {

    // Test get method
    @Test
    public void testGet() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        Assert.assertEquals("John", attributes.get("name"));
    }

    // Test getIgnoreCase method
    @Test
    public void testGetIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("NAME", "John");
        Assert.assertEquals("John", attributes.getIgnoreCase("name"));
    }

    // Test put method with String parameter
    @Test
    public void testPutString() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        Assert.assertEquals("John", attributes.get("name"));
    }

    // Test put method with boolean parameter
    @Test
    public void testPutBoolean() {
        Attributes attributes = new Attributes();
        attributes.put("isStudent", true);
        Assert.assertEquals(true, attributes.get("isStudent").equals("true"));
    }

    // Test put method with Attribute parameter
    @Test
    public void testPutAttribute() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("name", "John"));
        Assert.assertEquals("John", attributes.get("name"));
    }

    // Test remove method
    @Test
    public void testRemove() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.remove("name");
        Assert.assertEquals("", attributes.get("name"));
    }

    // Test removeIgnoreCase method
    @Test
    public void testRemoveIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("NAME", "John");
        attributes.removeIgnoreCase("name");
        Assert.assertEquals("", attributes.get("name"));
    }

    // Test hasKey method
    @Test
    public void testHasKey() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        Assert.assertEquals(true, attributes.hasKey("name"));
    }

    // Test hasKeyIgnoreCase method
    @Test
    public void testHasKeyIgnoreCase() {
        Attributes attributes = new Attributes();
        attributes.put("NAME", "John");
        Assert.assertEquals(true, attributes.hasKeyIgnoreCase("name"));
    }

    // Test size method
    @Test
    public void testSize() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        Assert.assertEquals(1, attributes.size());
    }

    // Test addAll method
    @Test
    public void testAddAll() {
        Attributes attributes1 = new Attributes();
        attributes1.put("name", "John");
        Attributes attributes2 = new Attributes();
        attributes2.put("age", "20");
        attributes1.addAll(attributes2);
        Assert.assertEquals(2, attributes1.size());
        Assert.assertEquals("John", attributes1.get("name"));
        Assert.assertEquals("20", attributes1.get("age"));
    }

    // Test iterator method
    @Test
    public void testIterator() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "20");
        for (Attribute attribute : attributes) {
            if (attribute.getKey().equals("name")) {
                Assert.assertEquals("John", attribute.getValue());
            } else if (attribute.getKey().equals("age")) {
                Assert.assertEquals("20", attribute.getValue());
            }
        }
    }

    // Test asList method
    @Test
    public void testAsList() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "20");
        List<Attribute> attributeList = attributes.asList();
        for (Attribute attribute : attributeList) {
            if (attribute.getKey().equals("name")) {
                Assert.assertEquals("John", attribute.getValue());
            } else if (attribute.getKey().equals("age")) {
                Assert.assertEquals("20", attribute.getValue());
            }
        }
    }

    // Test dataset method
    @Test
    public void testDataset() {
        Attributes attributes = new Attributes();
        attributes.put("data-name", "John");
        attributes.put("data-age", "20");
        Map<String, String> dataset = attributes.dataset();
        for (Map.Entry<String, String> entry : dataset.entrySet()) {
            if (entry.getKey().equals("name")) {
                Assert.assertEquals("John", entry.getValue());
            } else if (entry.getKey().equals("age")) {
                Assert.assertEquals("20", entry.getValue());
            }
        }
    }

    // Test html method
    @Test
    public void testHtml() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        attributes.put("age", "20");
        Assert.assertEquals(" name=\"John\" age=\"20\"", attributes.html());
    }

    // Test equals method
    @Test
    public void testEquals() {
        Attributes attributes1 = new Attributes();
        attributes1.put("name", "John");
        Attributes attributes2 = new Attributes();
        attributes2.put("name", "John");
        Assert.assertEquals(true, attributes1.equals(attributes2));
    }

    // Test hashCode method
    @Test
    public void testHashCode() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        Assert.assertEquals(attributes.hashCode(), attributes.hashCode());
    }

    // Test clone method
    @Test
    public void testClone() {
        Attributes attributes = new Attributes();
        attributes.put("name", "John");
        Attributes clonedAttributes = attributes.clone();
        Assert.assertEquals(attributes.get("name"), clonedAttributes.get("name"));
        Assert.assertNotSame(attributes, clonedAttributes);
    }
}

