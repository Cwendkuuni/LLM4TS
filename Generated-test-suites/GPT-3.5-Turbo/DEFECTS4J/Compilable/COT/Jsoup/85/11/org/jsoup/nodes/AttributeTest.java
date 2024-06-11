package org.jsoup.nodes;
import org.junit.Test;

public class AttributeTest {

    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("name", "value");
        assert(attribute.getKey().equals("name"));
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("name", "value");
        attribute.setKey("newKey");
        assert(attribute.getKey().equals("newKey"));
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("name", "value");
        assert(attribute.getValue().equals("value"));
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("name", "value");
        attribute.setValue("newValue");
        assert(attribute.getValue().equals("newValue"));
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("name", "value");
        String html = attribute.html();
        assert(html.equals("name=\"value\""));
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("name", "value");
        String str = attribute.toString();
        assert(str.equals("name=\"value\""));
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("name", "value");
        assert(attribute.getKey().equals("name"));
        assert(attribute.getValue().equals("value"));
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-name", "value");
        assert(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("name", "value");
        Document.OutputSettings out = new Document.OutputSettings();
        assert(attribute.shouldCollapseAttribute(out) == false);
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute1 = new Attribute("checked", null);
        Attribute attribute2 = new Attribute("name", "value");
        assert(attribute1.isBooleanAttribute());
        assert(!attribute2.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("name", "value");
        Attribute attribute2 = new Attribute("name", "value");
        Attribute attribute3 = new Attribute("name", "newValue");
        assert(attribute1.equals(attribute2));
        assert(!attribute1.equals(attribute3));
    }

    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("name", "value");
        Attribute attribute2 = new Attribute("name", "value");
        Attribute attribute3 = new Attribute("name", "newValue");
        assert(attribute1.hashCode() == attribute2.hashCode());
        assert(attribute1.hashCode() != attribute3.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attribute1 = new Attribute("name", "value");
        Attribute attribute2 = attribute1.clone();
        assert(attribute1.equals(attribute2));
    }
}