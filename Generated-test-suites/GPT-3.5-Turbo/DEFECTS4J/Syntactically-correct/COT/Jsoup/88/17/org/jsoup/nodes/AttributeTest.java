import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AttributeTest {

    private Attribute attribute;

    @Before
    public void setUp() {
        attribute = new Attribute("key", "value");
    }

    @Test
    public void testGetKey() {
        assertEquals("key", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        String html = attribute.html();
        assertNotNull(html);
        assertTrue(html.startsWith("key=\"value\""));
    }

    @Test
    public void testToString() {
        String str = attribute.toString();
        assertNotNull(str);
        assertTrue(str.startsWith("key=\"value\""));
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertNotNull(clonedAttribute);
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
        assertFalse(attribute == clonedAttribute);
    }

    @Test
    public void testEquals() {
        Attribute sameAttribute = new Attribute("key", "value");
        assertTrue(attribute.equals(sameAttribute));
    }

    @Test
    public void testHashCode() {
        Attribute sameAttribute = new Attribute("key", "value");
        assertEquals(attribute.hashCode(), sameAttribute.hashCode());
    }
}