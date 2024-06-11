import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import java.util.HashMap;
import java.util.Map;

public class AttributeTest {
    private final String key = "testKey";
    private final String value = "testValue";
    private final Attributes parent = new Attributes();
    private final Attribute attribute = new Attribute(key, value, parent);

    @Test
    public void testGetKey() {
        assertEquals(key, attribute.getKey());
    }

    @Test
    public void testSetKey() {
        String newKey = "newKey";
        attribute.setKey(newKey);
        assertEquals(newKey, attribute.getKey());
        assertEquals(newKey, parent.get(0));
    }

    @Test
    public void testGetValue() {
        assertEquals(value, attribute.getValue());
    }

    @Test
    public void testSetValue() {
        String newValue = "newValue";
        String oldValue = attribute.setValue(newValue);
        assertEquals(newValue, attribute.getValue());
        assertEquals(newValue, parent.get(0));
        assertEquals(value, oldValue);
    }

    @Test
    public void testHtml() {
        String html = attribute.html();
        assertTrue(html.contains(key));
        assertTrue(html.contains(value));
    }

    @Test
    public void testToString() {
        String str = attribute.toString();
        assertTrue(str.contains(key));
        assertTrue(str.contains(value));
    }

    @Test
    public void testCreateFromEncoded() {
        String unencodedKey = "unencodedKey";
        String encodedValue = "encodedValue";
        Attribute newAttribute = Attribute.createFromEncoded(unencodedKey, encodedValue);
        assertEquals(unencodedKey, newAttribute.getKey());
        assertEquals(encodedValue, newAttribute.getValue());
        assertNull(newAttribute.parent);
    }

    @Test
    public void testIsDataAttribute() {
        assertTrue(attribute.isDataAttribute());
        String nonDataKey = "nonDataKey";
        Attribute nonDataAttribute = new Attribute(nonDataKey, value, parent);
        assertFalse(nonDataAttribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
        attribute.setValue("");
        assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testEquals() {
        Attribute sameAttribute = new Attribute(key, value, parent);
        assertTrue(attribute.equals(sameAttribute));
        sameAttribute.setValue("differentValue");
        assertFalse(attribute.equals(sameAttribute));
    }

    @Test
    public void testHashCode() {
        Attribute sameAttribute = new Attribute(key, value, parent);
        assertEquals(attribute.hashCode(), sameAttribute.hashCode());
        sameAttribute.setValue("differentValue");
        assertNotEquals(attribute.hashCode(), sameAttribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
        assertNotEquals(attribute, clonedAttribute);
    }
}