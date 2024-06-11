import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attr = new Attribute("key", "value");
        Assert.assertEquals("key", attr.getKey());
        Assert.assertEquals("value", attr.getValue());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        Assert.assertEquals("key", attrWithParent.getKey());
        Assert.assertEquals("value", attrWithParent.getValue());
    }

    @Test
    public void testAttributeKeySetter() {
        Attribute attr = new Attribute("key", "value");
        attr.setKey("newKey");
        Assert.assertEquals("newKey", attr.getKey());
    }

    @Test
    public void testAttributeValueSetter() {
        Attribute attr = new Attribute("key", "value");
        attr.setValue("newValue");
        Assert.assertEquals("newValue", attr.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testNullKeyAttributeCreation() {
        new Attribute(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void testNullValueAttributeCreation() {
        new Attribute("key", null);
    }

    @Test
    public void testHtmlAttribute() throws IOException {
        Attribute attr = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attr.html(sb, new Document("").outputSettings());
        String htmlOutput = StringUtil.releaseBuilder(sb);
        String expected = "key=\"value\"";
        Assert.assertEquals(expected, htmlOutput);
    }

    @Test(expected = SerializationException.class)
    public void testIOExceptionDuringHtmlAttribute() throws IOException {
        StringBuilder sb = StringUtil.borrowBuilder();
        new Attribute("key", "value").html(sb, new Document("").outputSettings());
    }
}