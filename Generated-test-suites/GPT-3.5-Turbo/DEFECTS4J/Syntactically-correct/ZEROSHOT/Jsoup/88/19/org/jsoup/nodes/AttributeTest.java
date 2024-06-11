import org.junit.*;
import static org.junit.Assert.*;
import org.jsoup.nodes.*;

public class AttributeTest {

  @Test
  public void testCreateAttribute() {
    Attribute attribute = new Attribute("key", "value");
    assertNotNull(attribute);
  }

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
    attribute.setValue("newValue");
    assertEquals("newValue", attribute.getValue());
  }

  @Test
  public void testHtml() {
    Attribute attribute = new Attribute("key", "value");
    String expected = "key=\"value\"";
    assertEquals(expected, attribute.html());
  }

  @Test
  public void testToString() {
    Attribute attribute = new Attribute("key", "value");
    String expected = "key=\"value\"";
    assertEquals(expected, attribute.toString());
  }

  @Test
  public void testCreateFromEncoded() {
    Attribute attribute = Attribute.createFromEncoded("key", "value");
    assertNotNull(attribute);
    assertEquals("key", attribute.getKey());
    assertEquals("value", attribute.getValue());
  }

  @Test
  public void testIsDataAttribute() {
    Attribute attribute = new Attribute("key", "value");
    assertFalse(attribute.isDataAttribute());
  }

  @Test
  public void testShouldCollapseAttribute() {
    Attribute attribute = new Attribute("key", "value");
    assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
  }

  @Test
  public void testIsBooleanAttribute() {
    Attribute attribute = new Attribute("key", "value");
    assertFalse(attribute.isBooleanAttribute());
  }

  @Test
  public void testEquals() {
    Attribute attribute1 = new Attribute("key", "value");
    Attribute attribute2 = new Attribute("key", "value");
    Attribute attribute3 = new Attribute("otherKey", "value");
    Attribute attribute4 = new Attribute("key", "otherValue");
    assertEquals(attribute2, attribute1);
    assertNotEquals(attribute3, attribute1);
    assertNotEquals(attribute4, attribute1);
  }

  @Test
  public void testHashCode() {
    Attribute attribute1 = new Attribute("key", "value");
    Attribute attribute2 = new Attribute("key", "value");
    Attribute attribute3 = new Attribute("otherKey", "value");
    Attribute attribute4 = new Attribute("key", "otherValue");
    assertEquals(attribute2.hashCode(), attribute1.hashCode());
    assertNotEquals(attribute3.hashCode(), attribute1.hashCode());
    assertNotEquals(attribute4.hashCode(), attribute1.hashCode());
  }

  @Test
  public void testClone() {
    Attribute attribute = new Attribute("key", "value");
    Attribute clonedAttribute = attribute.clone();
    assertNotSame(attribute, clonedAttribute);
    assertEquals(attribute, clonedAttribute);
  }
}