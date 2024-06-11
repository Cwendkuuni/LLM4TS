import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
        String expected = "testKey=\"testValue\"";
        assertEquals(expected, attribute.html());
    }

    @Test
    public void testToString() {
        String expected = "testKey=\"testValue\"";
        assertEquals(expected, attribute.toString());
    }

    @Test
    public void testEquals() {
        Attribute sameAttribute = new Attribute("testKey", "testValue");
        Attribute differentAttribute = new Attribute("differentKey", "differentValue");

        assertTrue(attribute.equals(sameAttribute));
        assertFalse(attribute.equals(differentAttribute));
    }

    @Test
    public void testHashCode() {
        Attribute sameAttribute = new Attribute("testKey", "testValue");

        assertEquals(attribute.hashCode(), sameAttribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();

        assertEquals(attribute, clonedAttribute);
        assertNotSame(attribute, clonedAttribute);
    }
}