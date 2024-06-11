import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
    }

    @Test
    public void testAttributeCreationWithParent() {
        Attributes parent = new Attributes();
        Attribute attr = new Attribute("key", "value", parent);
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
        assertEquals(attr, parent.get(attr.getKey()));
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
        String newValue = "newValue";
        String oldValue = attr.getValue();
        attr.setValue(newValue);
        assertEquals(newValue, attr.getValue());
        assertEquals(oldValue, attr.parent.get(attr.getKey()));
    }

    @Test
    public void testHtml() throws IOException {
        Document doc = new Document("");
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, doc.outputSettings());
        assertEquals("key=\"value\"", sb.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        String encodedValue = "key=\"value\"";
        Attribute attr = Attribute.createFromEncoded("key", encodedValue);
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attr = new Attribute("data-key", null);
        assertTrue(attr.isDataAttribute());

        attr = new Attribute("key", null);
        assertFalse(attr.isDataAttribute());
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attr = new Attribute("checked", null);
        assertTrue(attr.isBooleanAttribute());

        attr = new Attribute("key", null);
        assertFalse(attr.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attr1 = new Attribute("key1", "value1");
        Attribute attr2 = new Attribute("key1", "value1");
        Attribute attr3 = new Attribute("key2", "value2");
        assertTrue(attr1.equals(attr1));
        assertTrue(attr1.equals(attr2));
        assertFalse(attr1.equals(attr3));

        attr1.setKey("newKey");
        assertFalse(attr1.equals(attr2));

        attr2.setValue("newValue");
        assertFalse(attr1.equals(attr2));
    }

    @Test
    public void testClone() {
        Attribute attr = new Attribute("key", "value");
        Attribute clonedAttr = attr.clone();
        assertEquals(attr, clonedAttr);

        attr.setValue("newValue");
        assertNotSame(attr, clonedAttr);
    }
}