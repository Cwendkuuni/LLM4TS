import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AttributeTest {

    private Attribute attribute;

    @Before
    public void setUp() {
        attribute = new Attribute("testKey", "testValue");
    }

    @Test
    public void testGetKey() {
        assertEquals("testKey", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        assertEquals("testValue", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        String oldValue = attribute.setValue("newValue");
        assertEquals("testValue", oldValue);
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        String html = attribute.html();
        assertEquals("testKey=\"testValue\"", html);
    }

    @Test
    public void testFromString() {
        Attribute newAttribute = Attribute.createFromEncoded("testKey", "test&amp;Value");
        assertEquals("testKey", newAttribute.getKey());
        assertEquals("test&Value", newAttribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());
        attribute.setKey("data-test");
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testIsBooleanAttribute() {
        assertFalse(attribute.isBooleanAttribute());
        attribute.setKey("checked");
        assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute newAttribute = new Attribute("testKey", "testValue");
        assertEquals(attribute, newAttribute);
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
        assertNotSame(attribute, clonedAttribute);
    }
}