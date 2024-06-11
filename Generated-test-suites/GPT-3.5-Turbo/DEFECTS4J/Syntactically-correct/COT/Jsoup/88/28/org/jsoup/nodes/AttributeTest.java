import org.jsoup.nodes.*;
import org.jsoup.SerializationException;
import org.jsoup.helper.Validate;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class AttributeTest {
    
    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        assert attribute.getKey().equals("key");
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assert attribute.getKey().equals("newKey");
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        assert attribute.getValue().equals("value");
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assert attribute.getValue().equals("newValue");
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        assert attribute.html().equals("key=\"value\"");
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        assert attribute.toString().equals("key=\"value\"");
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "value");
        assert attribute.getKey().equals("key");
        assert attribute.getValue().equals("value");
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        assert attribute.isDataAttribute();
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "", null);
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assert attribute.shouldCollapseAttribute(outputSettings);

        Attribute attribute2 = new Attribute("key", "key", null);
        assert attribute2.shouldCollapseAttribute(outputSettings);

        Attribute attribute3 = new Attribute("key", "value", null);
        assert !attribute3.shouldCollapseAttribute(outputSettings);
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("checked", null);
        assert attribute.isBooleanAttribute();

        Attribute attribute2 = new Attribute("key", "value");
        assert !attribute2.isBooleanAttribute();
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assert attribute1.equals(attribute2);

        Attribute attribute3 = new Attribute("key", null);
        Attribute attribute4 = new Attribute("key", null);
        assert attribute3.equals(attribute4);

        Attribute attribute5 = new Attribute("key1", "value1");
        Attribute attribute6 = new Attribute("key2", "value2");
        assert !attribute5.equals(attribute6);
    }

    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assert attribute1.hashCode() == attribute2.hashCode();

        Attribute attribute3 = new Attribute("key", null);
        Attribute attribute4 = new Attribute("key", null);
        assert attribute3.hashCode() == attribute4.hashCode();

        Attribute attribute5 = new Attribute("key1", "value1");
        Attribute attribute6 = new Attribute("key2", "value2");
        assert attribute5.hashCode() != attribute6.hashCode();
    }

    // Additional test cases for edge cases and exception handling scenarios
    
    @Test
    public void testEmptyKey() {
        Attribute attribute = new Attribute("", "value");
        assert attribute.getKey().equals("");
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullKey() {
        Attribute attribute = new Attribute(null, "value");
    }
    
    @Test
    public void testEmptyValue() {
        Attribute attribute = new Attribute("key", "");
        assert attribute.getValue().equals("");
    }
    
    @Test
    public void testSetValueWithParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        attribute.setValue("newValue");
        assert attribute.getValue().equals("newValue");
        assert parent.get("key").equals("newValue");
    }
    
    @Test
    public void testSetKeyWithParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        attribute.setKey("newKey");
        assert attribute.getKey().equals("newKey");
        assert parent.containsKey("newKey");
    }
}