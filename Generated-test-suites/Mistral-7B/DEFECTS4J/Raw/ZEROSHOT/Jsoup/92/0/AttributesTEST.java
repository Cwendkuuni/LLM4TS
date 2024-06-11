import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AttributesTest {

    @Test
    public void testAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("key1", "value1");
        attributes.put("key2", "value2");
        attributes.put("key1", "value3"); // duplicates key1

        // test get method
        Assert.assertEquals("value1", attributes.get("key1"));
        Assert.assertEquals("value2", attributes.get("key2"));
        Assert.assertEquals("", attributes.get("key3")); // non-existent key

        // test getIgnoreCase method
        Assert.assertEquals("value1", attributes.getIgnoreCase("key1"));
        Assert.assertEquals("value2", attributes.getIgnoreCase("key2"));
        Assert.assertEquals("", attributes.getIgnoreCase("key3")); // non-existent key

        // test put method
        attributes.put("key1", "value4"); // replaces value1 with value4
        Assert.assertEquals("value4", attributes.get("key1"));

        // test putIgnoreCase method
        attributes.putIgnoreCase("key1", "value5"); // replaces value1 with value5 (case insensitive)
        Assert.assertEquals("value5", attributes.getIgnoreCase("key1"));

        // test put method with boolean value
        attributes.put("boolKey", true);
        Assert.assertFalse(attributes.get("boolKey").isEmpty());
        Assert.assertTrue(attributes.hasKey("boolKey"));
        Assert.assertFalse(attributes.get("boolKey").equals("true")); // not storing boolean as string "true"
        Assert.assertFalse(attributes.get("boolKey").equals("false"));

        // test put method with Attribute object
        Attribute attribute = new Attribute("key6", "value6");
        attributes.put(attribute);
        Assert.assertEquals("value6", attributes.get("key6"));

        // test remove method
        attributes.remove("key1");
        Assert.assertFalse(attributes.hasKey("key1"));
        Assert.assertEquals("", attributes.get("key1"));

        // test removeIgnoreCase method
        attributes.putIgnoreCase("key1", "value1");
        attributes.removeIgnoreCase("key1");
        Assert.assertFalse(attributes.hasKey("key1"));
        Assert.assertEquals("", attributes.get("key1"));

        // test hasKey method
        Assert.assertTrue(attributes.hasKey("key2"));
        Assert.assertFalse(attributes.hasKey("key3"));

        // test hasKeyIgnoreCase method
        Assert.assertTrue(attributes.hasKeyIgnoreCase("key2"));
        Assert.assertFalse(attributes.hasKeyIgnoreCase("key3"));

        // test size method
        Assert.assertEquals(4, attributes.size());

        // test addAll method
        Attributes attributes2 = new Attributes();
        attributes2.put("key7", "value7");
        attributes2.put("key8", "value8");
        attributes.addAll(attributes2);
        Assert.assertEquals(6, attributes.size());
        Assert.assertEquals("value7", attributes.get("key7"));
        Assert.assertEquals("value8", attributes.get("key8"));

        // test iterator method
        List<Attribute> attributesList = attributes.asList();
        for (Attribute attribute1 : attributesList) {
            System.out.println(attribute1.getKey() + "=" + attribute1.getValue());
        }

        // test dataset method
        Map<String, String> dataset = attributes.dataset();
        for (Map.Entry<String, String> entry : dataset.entrySet()) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }

        // test html method
        String html = attributes.html();
        System.out.println(html);

        // test equals method
        Attributes attributes3 = new Attributes();
        attributes3.put("key1", "value1");
        attributes3.put("key2", "value2");
        Assert.assertEquals(attributes, attributes3);

        // test clone method
        Attributes attributes4 = attributes.clone();
        Assert.assertEquals(attributes, attributes4);

        // test normalize method
        attributes.normalize();
        for (int i = 0; i < attributes.size(); i++) {
            Assert.assertEquals(lowerCase(attributes.keys[i]), attributes.keys[i]);
        }
    }
}