import org.jsoup.nodes.Attribute;
import org.jsoup.helper.Validate;
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
        assertEquals("testValue", attribute.setValue("newValue"));
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        assertEquals("testKey=\"testValue\"", attribute.html());
    }

    @Test(expected = SerializationException.class)
    public void testHtmlException() {
        attribute = new Attribute("testKey", "testValue", null);
        attribute.html();
    }

    @Test
    public void testToString() {
        assertEquals("testKey=\"testValue\"", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute newAttribute = Attribute.createFromEncoded("unencodedKey", "encodedValue");
        assertEquals("unencodedKey", newAttribute.getKey());
        assertEquals("encodedValue", newAttribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());
        assertTrue(Attribute.isDataAttribute("dataTest"));
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertFalse(attribute.isBooleanAttribute());
        assertTrue(Attribute.isBooleanAttribute("async"));
    }

    @Test
    public void testEquals() {
        Attribute sameAttribute = new Attribute("testKey", "testValue");
        Attribute differentAttribute = new Attribute("key", "value");

        assertTrue(attribute.equals(sameAttribute));
        assertFalse(attribute.equals(differentAttribute));
    }

    @Test
    public void testHashCode() {
        Attribute sameAttribute = new Attribute("testKey", "testValue");
        assertEquals(sameAttribute.hashCode(), attribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}