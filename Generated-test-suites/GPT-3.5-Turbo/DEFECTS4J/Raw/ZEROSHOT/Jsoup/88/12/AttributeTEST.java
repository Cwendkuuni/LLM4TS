import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {

  @Test
  public void testGetKey() {
    Attribute attribute = new Attribute("key", "value");
    assertEquals("key", attribute.getKey());
  }

  @Test
  public void testSetKey() {
    Attribute attribute = new Attribute("key", "value");
    attribute.setKey("newKey");
    assertEquals("newKey", attribute.getKey());
  }

  @Test
  public void testGetValue() {
    Attribute attribute = new Attribute("key", "value");
    assertEquals("value", attribute.getValue());
  }

  @Test
  public void testSetValue() {
    Attribute attribute = new Attribute("key", "value");
    String oldValue = attribute.setValue("newValue");
    assertEquals("value", oldValue);
    assertEquals("newValue", attribute.getValue());
  }

  @Test
  public void testHtml() {
    Attribute attribute = new Attribute("key", "value");
    String html = attribute.html();
    assertEquals("key=\"value\"", html);
  }

  @Test
  public void testToString() {
    Attribute attribute = new Attribute("key", "value");
    String html = attribute.toString();
    assertEquals("key=\"value\"", html);
  }

  @Test
  public void testCreateFromEncoded() {
    Attribute attribute = Attribute.createFromEncoded("key", "&quot;value&quot;");
    assertEquals("key", attribute.getKey());
    assertEquals("\"value\"", attribute.getValue());
  }

  @Test
  public void testIsDataAttribute() {
    Attribute attribute = new Attribute("data-key", "value");
    assertTrue(attribute.isDataAttribute());
  }

  @Test
  public void testShouldCollapseAttribute() {
    Attribute attribute = new Attribute("key", "");
    assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
  }

  @Test
  public void testIsBooleanAttribute() {
    Attribute attribute = new Attribute("allowfullscreen", "");
    assertTrue(attribute.isBooleanAttribute());
  }

  @Test
  public void testEquals() {
    Attribute attribute1 = new Attribute("key", "value");
    Attribute attribute2 = new Attribute("key", "value");
    Attribute attribute3 = new Attribute("key", "newValue");
    assertTrue(attribute1.equals(attribute2));
    assertFalse(attribute1.equals(attribute3));
  }

  @Test
  public void testHashCode() {
    Attribute attribute = new Attribute("key", "value");
    int hashCode = attribute.hashCode();
    assertEquals(54319667, hashCode);
  }

  @Test
  public void testClone() {
    Attribute attribute = new Attribute("key", "value");
    Attribute clonedAttribute = attribute.clone();
    assertEquals(attribute.getKey(), clonedAttribute.getKey());
    assertEquals(attribute.getValue(), clonedAttribute.getValue());
  }
}