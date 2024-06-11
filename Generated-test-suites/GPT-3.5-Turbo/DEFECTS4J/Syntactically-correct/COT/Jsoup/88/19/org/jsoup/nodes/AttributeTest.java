import org.junit.Test;

public class AttributeTest {

    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("key", "value");
        assert(attribute.getKey().equals("key"));
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assert(attribute.getKey().equals("newKey"));
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("key", "value");
        assert(attribute.getValue().equals("value"));
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assert(attribute.getValue().equals("newValue"));
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        assert(attribute.html().equals("key=\"value\""));
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        assert(attribute1.equals(attribute2));

        Attribute attribute3 = new Attribute("key", "value");
        Attribute attribute4 = new Attribute("newKey", "value");
        assert(!attribute3.equals(attribute4));

        Attribute attribute5 = new Attribute("key", "value");
        Attribute attribute6 = new Attribute("key", "newValue");
        assert(!attribute5.equals(attribute6));

        Attribute attribute7 = new Attribute("key", "value");
        assert(!attribute7.equals(null));
    }

    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key", "value");
        attribute1.hashCode();

        Attribute attribute2 = new Attribute("key", null);
        attribute2.hashCode();

        Attribute attribute3 = new Attribute(null, "value");
        attribute3.hashCode();

        Attribute attribute4 = new Attribute(null, null);
        attribute4.hashCode();
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        assert(clonedAttribute.equals(attribute));
        assert(clonedAttribute != attribute);
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute1 = new Attribute("data-key", "value");
        assert(attribute1.isDataAttribute());

        Attribute attribute2 = new Attribute("key", "value");
        assert(!attribute2.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute1 = new Attribute("key", "value");
        Document.OutputSettings out = (new Document("")).outputSettings();
        assert(!attribute1.shouldCollapseAttribute(out));

        Attribute attribute2 = new Attribute("key", "key");
        assert(attribute2.shouldCollapseAttribute(out));

        Attribute attribute3 = new Attribute("booleanAttribute", null);
        assert(attribute3.shouldCollapseAttribute(out));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute1 = new Attribute("booleanAttribute", "value");
        assert(attribute1.isBooleanAttribute());

        Attribute attribute2 = new Attribute("key", "value");
        assert(!attribute2.isBooleanAttribute());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "encodedValue");
        assert(attribute.getKey().equals("key"));
        assert(attribute.getValue().equals("encodedValue"));
    }
}