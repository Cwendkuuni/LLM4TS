import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
        assertEquals("key=\"value\"", attribute.html());
    }

    @Test
    public void testEquals() {
        Attribute otherAttribute = new Attribute("key", "value");
        assertTrue(attribute.equals(otherAttribute));
    }

    @Test
    public void testHashCode() {
        Attribute otherAttribute = new Attribute("key", "value");
        assertEquals(attribute.hashCode(), otherAttribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertNotSame(attribute, clonedAttribute);
        assertEquals(attribute, clonedAttribute);
    }
}