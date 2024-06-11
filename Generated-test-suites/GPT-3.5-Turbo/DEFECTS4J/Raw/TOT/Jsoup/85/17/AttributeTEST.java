import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {
    
    private Attribute attribute;
    private Attributes parent;
    
    @Before
    public void setup() {
        parent = new Attributes();
        attribute = new Attribute("key", "value", parent);
    }
    
    @Test
    public void testGetKey() {
        assertEquals("key", attribute.getKey());
    }
    
    @Test
    public void testSetKey() {
        attribute.setKey("newkey");
        assertEquals("newkey", attribute.getKey());
    }
    
    @Test
    public void testSetKeyWithParent() {
        attribute.setKey("newkey");
        assertEquals("newkey", attribute.getKey());
        assertEquals("newkey", parent.get(0));
    }
    
    @Test
    public void testGetValue() {
        assertEquals("value", attribute.getValue());
    }
    
    @Test
    public void testSetValue() {
        String oldValue = attribute.setValue("newvalue");
        assertEquals("newvalue", attribute.getValue());
        assertEquals("value", oldValue);
    }
    
    @Test
    public void testSetValueWithParent() {
        String oldValue = attribute.setValue("newvalue");
        assertEquals("newvalue", attribute.getValue());
        assertEquals("value", oldValue);
        assertEquals("newvalue", parent.get("key"));
    }
    
    @Test
    public void testHtml() {
        String html = attribute.html();
        assertEquals("key=\"value\"", html);
    }
    
    @Test
    public void testToString() {
        String str = attribute.toString();
        assertEquals("key=\"value\"", str);
    }
    
    @Test
    public void testCreateFromEncoded() {
        Attribute attr = Attribute.createFromEncoded("unencodedKey", "encodedValue");
        assertEquals("unencodedKey", attr.getKey());
        assertEquals("encodedValue", attr.getValue());
    }
    
    @Test
    public void testIsDataAttribute() {
        boolean isDataAttr = attribute.isDataAttribute();
        assertFalse(isDataAttr);
    }
    
    @Test
    public void testIsDataAttributeWithValidDataAttr() {
        attribute.setKey("data-custom");
        boolean isDataAttr = attribute.isDataAttribute();
        assertTrue(isDataAttr);
    }
    
    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        boolean shouldCollapse = attribute.shouldCollapseAttribute(outputSettings);
        assertFalse(shouldCollapse);
    }
    
    @Test
    public void testShouldCollapseAttributeWithEmptyValue() {
        attribute.setValue("");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        boolean shouldCollapse = attribute.shouldCollapseAttribute(outputSettings);
        assertTrue(shouldCollapse);
    }
    
    @Test
    public void testShouldCollapseAttributeWithSameValueAsKey() {
        attribute.setValue("key");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        boolean shouldCollapse = attribute.shouldCollapseAttribute(outputSettings);
        assertTrue(shouldCollapse);
    }
    
    @Test
    public void testIsBooleanAttribute() {
        boolean isBoolean = attribute.isBooleanAttribute();
        assertFalse(isBoolean);
    }
    
    @Test
    public void testIsBooleanAttributeWithBooleanKey() {
        attribute.setKey("checked");
        boolean isBoolean = attribute.isBooleanAttribute();
        assertTrue(isBoolean);
    }
    
    @Test
    public void testEqualsSameObject() {
        assertTrue(attribute.equals(attribute));
    }
    
    @Test
    public void testEqualsNullObject() {
        assertFalse(attribute.equals(null));
    }
    
    @Test
    public void testEqualsDifferentClass() {
        assertFalse(attribute.equals("not_attribute"));
    }
    
    @Test
    public void testEqualsDifferentKey() {
        Attribute other = new Attribute("different", "value", null);
        assertFalse(attribute.equals(other));
    }
    
    @Test
    public void testEqualsDifferentValue() {
        Attribute other = new Attribute("key", "different", null);
        assertFalse(attribute.equals(other));
    }
    
    @Test
    public void testEqualsSameKeyAndValue() {
        Attribute other = new Attribute("key", "value", null);
        assertTrue(attribute.equals(other));
    }
    
    @Test
    public void testHashCode() {
        assertEquals("key".hashCode() + "value".hashCode(), attribute.hashCode());
    }
    
    @Test
    public void testClone() throws CloneNotSupportedException {
        Attribute clone = attribute.clone();
        assertEquals(attribute, clone);
    }
}