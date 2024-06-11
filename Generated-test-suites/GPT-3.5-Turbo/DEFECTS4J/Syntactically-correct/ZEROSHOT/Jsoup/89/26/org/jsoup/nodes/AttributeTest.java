import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {
    private Attribute attribute;
    private Attributes parent;

    @Before
    public void setUp() {
        parent = new Attributes();
        attribute = new Attribute("key", "value", parent);
    }

    @Test
    public void testGetKey() {
        assertEquals("key", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
        assertEquals("newKey", parent.keys[0]);
    }

    @Test
    public void testGetValue() {
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        assertEquals("value", attribute.setValue("newValue"));
        assertEquals("newValue", attribute.getValue());
        assertEquals("newValue", parent.vals[0]);
    }

    @Test
    public void testHtml() {
        assertEquals("key=\"value\"", attribute.html());
    }

    @Test
    public void testToString() {
        assertEquals("key=\"value\"", attribute.toString());
    }

    @Test
    public void testEquals() {
        Attribute attribute2 = new Attribute("key", "value", parent);
        assertTrue(attribute.equals(attribute2));
    }

    @Test
    public void testHashCode() {
        Attribute attribute2 = new Attribute("key", "value", parent);
        assertEquals(attribute.hashCode(), attribute2.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clone = attribute.clone();
        assertEquals(attribute, clone);
        assertNotSame(attribute, clone);
    }
}