import org.junit.Test;

public class AttributeTest {

    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        String key = attribute.getKey();
        assert key.equals("key");
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        String key = attribute.getKey();
        assert key.equals("newKey");
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        String value = attribute.getValue();
        assert value.equals("value");
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        String value = attribute.getValue();
        assert value.equals("newValue");
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        String html = attribute.html();
        assert html.equals("key=\"value\"");
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "&quot;value&quot;");
        String value = attribute.getValue();
        assert value.equals("\"value\"");
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-key", "value");
        boolean isDataAttribute = attribute.isDataAttribute();
        assert isDataAttribute;
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("key", "key");
        boolean shouldCollapse = attribute.shouldCollapseAttribute(new Document.OutputSettings());
        assert shouldCollapse;
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assert attribute1.equals(attribute2);
    }

    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("key", "value");
        int hashCode = attribute.hashCode();
        assert hashCode == "key".hashCode() + "value".hashCode();
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clone = attribute.clone();
        assert attribute.equals(clone);
        assert attribute != clone;
    }
}