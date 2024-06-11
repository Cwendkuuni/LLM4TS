import org.jsoup.nodes.Attribute;
import org.jsoup.SerializationException;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AttributeTest {

    private Attribute attribute;

    @Before
    public void setup() {
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

    @Test
    public void testToString() {
        assertEquals("testKey=\"testValue\"", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute newAttribute = Attribute.createFromEncoded("encodedKey", "encodedValue");
        assertEquals("encodedKey", newAttribute.getKey());
        assertEquals("encodedValue", newAttribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute sameAttribute = new Attribute("testKey", "testValue");
        assertEquals(attribute, sameAttribute);

        Attribute differentAttribute = new Attribute("differentKey", "differentValue");
        assertNotEquals(attribute, differentAttribute);
    }

    @Test
    public void testHashCode() {
        Attribute sameAttribute = new Attribute("testKey", "testValue");
        assertEquals(attribute.hashCode(), sameAttribute.hashCode());

        Attribute differentAttribute = new Attribute("differentKey", "differentValue");
        assertNotEquals(attribute.hashCode(), differentAttribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }

}