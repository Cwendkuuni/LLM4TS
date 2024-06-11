import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        assertEquals("key", attrWithParent.getKey());
        assertEquals("value", attrWithParent.getValue());
        // Test if parent Attributes is updated when key or value is changed
        attrWithParent.setKey("newKey");
        Assert.assertEquals("newKey", attrWithParent.getKey());
        Assert.assertArrayNotEquals(new String[]{"key"}, attrWithParent.parent.keys);

        attrWithParent.setValue("newValue");
        Assert.assertEquals("newValue", attrWithParent.getValue());
        Assert.assertEquals("value", attrWithParent.parent.get("key"));
        Assert.assertArrayNotEquals(new String[]{"value"}, attrWithParent.parent.vals);
    }

    @Test
    public void testAttributeHtmlEncoding() {
        Attribute attr = new Attribute("<script>", "src=\"javascript:alert('XSS');\"");
        String encodedHtml = attr.html();
        String decodedHtml = Entities.unescape(encodedHtml, true);
        assertEquals("<script src=\"javascript:alert('XSS')\"", decodedHtml);
    }

    @Test(expected = SerializationException.class)
    public void testIOExceptionThrownDuringHtmlSerialization() {
        Attribute attr = new Attribute("key", "value");
        try {
            attr.html(StringUtil.borrowBuilder(), new Document("").outputSettings());
        } catch (IOException e) {
            throw new SerializationException(e);
        } finally {
            StringUtil.releaseBuilder(StringUtil.borrowBuilder());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionThrownWhenSettingEmptyKey() {
        Attribute attr = new Attribute("", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionThrownWhenSettingNullKey() {
        Attribute attr = new Attribute(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionThrownWhenSettingNullValue() {
        Attribute attr = new Attribute("key", null);
    }
}