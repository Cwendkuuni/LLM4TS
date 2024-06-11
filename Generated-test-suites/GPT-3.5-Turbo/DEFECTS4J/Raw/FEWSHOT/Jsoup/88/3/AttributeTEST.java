import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AttributeTest {
    @Test
    public void testConstructor() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
    }
    
    @Test
    public void testSetKey() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        assertEquals("newKey", attr.getKey());
    }
    
    @Test
    public void testSetValue() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        assertEquals("newValue", attr.getValue());
    }
    
    @Test
    public void testHtml() {
        Attribute attr = new Attribute("href", "index.html");
        assertEquals("href=\"index.html\"", attr.html());
    }
    
    @Test
    public void testToString() {
        Attribute attr = new Attribute("href", "index.html");
        assertEquals("href=\"index.html\"", attr.toString());
    }
    
    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("unencodedKey", "&quot;encodedValue&quot;");
        assertEquals("unencodedKey", attr.getKey());
        assertEquals("\"encodedValue\"", attr.getValue());
    }
    
    @Test
    public void testIsDataAttribute() {
        Attribute attr1 = new Attribute("data-key", "value");
        Attribute attr2 = new Attribute("key", "value");
        assertEquals(true, attr1.isDataAttribute());
        assertEquals(false, attr2.isDataAttribute());
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        Attribute attr1 = new Attribute("key", "value");
        Attribute attr2 = new Attribute("checked", "value");
        Document.OutputSettings out = new Document.OutputSettings();
        assertEquals(false, attr1.shouldCollapseAttribute(out));
        assertEquals(true, attr2.shouldCollapseAttribute(out));
    }
    
    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key", "value");
        Attribute attr2 = new Attribute("key", "value");
        assertEquals(true, attr1.equals(attr2));
    }
    
    @Test
    public void testHashCode() {
        Attribute attr1 = new Attribute("key", "value");
        Attribute attr2 = new Attribute("key", "value");
        assertEquals(attr1.hashCode(), attr2.hashCode());
    }
    
    @Test
    public void testClone() {
        Attribute attr1 = new Attribute("key", "value");
        Attribute attr2 = attr1.clone();
        assertEquals(attr1, attr2);
    }
}