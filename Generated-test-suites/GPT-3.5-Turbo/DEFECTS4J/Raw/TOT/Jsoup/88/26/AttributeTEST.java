import org.junit.Test;
import org.junit.Before;
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
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        assertEquals("testKey=\"testValue\"", attribute.html());
    }

    @Test
    public void testEquals() {
        Attribute sameAttribute = new Attribute("testKey", "testValue");
        assertEquals(sameAttribute, attribute);
    }

    @Test
    public void testHashCode() {
        Attribute sameAttribute = new Attribute("testKey", "testValue");
        assertEquals(sameAttribute.hashCode(), attribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clone = attribute.clone();
        assertEquals(attribute, clone);
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute encodedAttribute = Attribute.createFromEncoded("testKey", "test&amp;Value");
        assertEquals("testKey", encodedAttribute.getKey());
        assertEquals("test&Value", encodedAttribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());
        Attribute dataAttribute = new Attribute("data-key", "testValue");
        assertTrue(dataAttribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
        Attribute collapsibleAttribute = new Attribute("input", "input");
        assertTrue(collapsibleAttribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertFalse(attribute.isBooleanAttribute());
        Attribute booleanAttribute = new Attribute("async", null);
        assertTrue(booleanAttribute.isBooleanAttribute());
    }

}