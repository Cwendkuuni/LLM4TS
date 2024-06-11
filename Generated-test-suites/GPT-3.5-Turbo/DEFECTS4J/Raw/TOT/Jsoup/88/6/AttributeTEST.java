import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import org.jsoup.nodes.Attribute;

import static org.junit.Assert.*;

public class AttributeTest {

    private Attribute attribute;

    @Before
    public void setUp() {
        attribute = new Attribute("name", "value");
    }

    @Test
    public void testGetKey() {
        assertEquals("name", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newName");
        assertEquals("newName", attribute.getKey());
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
    public void testHtml() throws IOException {
        String expectedHtml = "name=\"value\"";
        assertEquals(expectedHtml, attribute.html());
    }

    @Test
    public void testToString() {
        String expectedString = "name=\"value\"";
        assertEquals(expectedString, attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        String unencodedKey = "name";
        String encodedValue = "value";
        Attribute newAttribute = Attribute.createFromEncoded(unencodedKey, encodedValue);
        assertEquals(unencodedKey, newAttribute.getKey());
        assertEquals(encodedValue, newAttribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertFalse(attribute.shouldCollapseAttribute(Document.OutputSettings.html()));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertFalse(attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute sameAttribute = new Attribute("name", "value");
        Attribute differentAttribute = new Attribute("name", "differentValue");

        assertEquals(sameAttribute, attribute);
        assertNotEquals(differentAttribute, attribute);
    }

    @Test
    public void testHashCode() {
        int expectedHashCode = attribute.getKey().hashCode() ^ attribute.getValue().hashCode();
        assertEquals(expectedHashCode, attribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }
}