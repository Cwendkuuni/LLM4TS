import org.jsoup.nodes.Attribute;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

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
        assertEquals("value", attribute.setValue("newValue"));
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        assertEquals("key=\"value\"", attribute.html());
    }

    @Test
    public void testEquals() {
        Attribute sameAttribute = new Attribute("key", "value");
        assertEquals(attribute, sameAttribute);
        assertEquals(attribute.hashCode(), sameAttribute.hashCode());

        Attribute differentKeyAttribute = new Attribute("differentKey", "value");
        assertNotEquals(attribute, differentKeyAttribute);

        Attribute differentValueAttribute = new Attribute("key", "differentValue");
        assertNotEquals(attribute, differentValueAttribute);
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
        assertEquals(attribute.hashCode(), clonedAttribute.hashCode());
    }
}