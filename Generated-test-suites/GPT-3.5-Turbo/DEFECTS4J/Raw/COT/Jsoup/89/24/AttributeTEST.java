import org.junit.Test;
import org.junit.Assert;

public class AttributeTest {

  @Test
  public void testGetKey() {
    Attribute attribute = new Attribute("key", "value");
    String key = attribute.getKey();
    Assert.assertEquals("key", key);
  }

  @Test
  public void testSetKey() {
    Attribute attribute = new Attribute("key", "value");
    attribute.setKey("newKey");
    String key = attribute.getKey();
    Assert.assertEquals("newKey", key);
  }

  @Test
  public void testGetValue() {
    Attribute attribute = new Attribute("key", "value");
    String value = attribute.getValue();
    Assert.assertEquals("value", value);
  }

  @Test
  public void testSetValue() {
    Attribute attribute = new Attribute("key", "value");
    attribute.setValue("newValue");
    String value = attribute.getValue();
    Assert.assertEquals("newValue", value);
  }

  @Test
  public void testHtml() {
    Attribute attribute = new Attribute("key", "value");
    String html = attribute.html();
    Assert.assertEquals("key=\"value\"", html);
  }

  @Test
  public void testToString() {
    Attribute attribute = new Attribute("key", "value");
    String str = attribute.toString();
    Assert.assertEquals("key=\"value\"", str);
  }

  @Test
  public void testCreateFromEncoded() {
    Attribute attribute = Attribute.createFromEncoded("key", "val&amp;");
    String value = attribute.getValue();
    Assert.assertEquals("val&", value);
  }

  @Test
  public void testIsDataAttribute() {
    Attribute attribute = new Attribute("data-key", "value");
    boolean isDataAttribute = attribute.isDataAttribute();
    Assert.assertTrue(isDataAttribute);
  }

  @Test
  public void testShouldCollapseAttribute() {
    Attribute attribute = new Attribute("key", "value");
    Document.OutputSettings out = new Document.OutputSettings();
    boolean shouldCollapse = attribute.shouldCollapseAttribute(out);
    Assert.assertFalse(shouldCollapse);
  }

  @Test
  public void testEquals() {
    Attribute attribute1 = new Attribute("key", "value");
    Attribute attribute2 = new Attribute("key", "value");
    boolean equals = attribute1.equals(attribute2);
    Assert.assertTrue(equals);
  }

  @Test
  public void testHashCode() {
    Attribute attribute = new Attribute("key", "value");
    int hashCode = attribute.hashCode();
    Assert.assertTrue(hashCode != 0);
  }

  @Test
  public void testClone() {
    Attribute attribute = new Attribute("key", "value");
    Attribute clone = attribute.clone();
    Assert.assertEquals(attribute, clone);
  }
}