import org.junit.Test;

public class AttributeTest {

  @Test
  public void testGetKey() {
    Attribute attribute = new Attribute("key", "value");
    String key = attribute.getKey();
    assertEquals("key", key);
  }

  @Test
  public void testSetKey() {
    Attribute attribute = new Attribute("key", "value");
    attribute.setKey("newKey");
    String key = attribute.getKey();
    assertEquals("newKey", key);
  }

  @Test
  public void testGetValue() {
    Attribute attribute = new Attribute("key", "value");
    String value = attribute.getValue();
    assertEquals("value", value);
  }

  @Test
  public void testSetValue() {
    Attribute attribute = new Attribute("key", "value");
    attribute.setValue("newValue");
    String value = attribute.getValue();
    assertEquals("newValue", value);
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
    String string = attribute.toString();
    assertEquals("key=\"value\"", string);
  }

  @Test
  public void testIsDataAttribute() {
    Attribute attribute = new Attribute("data-key", "value");
    boolean isDataAttribute = attribute.isDataAttribute();
    assertTrue(isDataAttribute);
  }

  @Test
  public void testShouldCollapseAttribute() {
    Attribute attribute = new Attribute("key", "value");
    Document.OutputSettings outputSettings = new Document.OutputSettings();
    boolean shouldCollapseAttribute = attribute.shouldCollapseAttribute(outputSettings);
    assertFalse(shouldCollapseAttribute);
  }

  @Test
  public void testIsBooleanAttribute() {
    Attribute attribute = new Attribute("key", "value");
    boolean isBooleanAttribute = attribute.isBooleanAttribute();
    assertFalse(isBooleanAttribute);
  }

  @Test
  public void testEquals() {
    Attribute attribute1 = new Attribute("key", "value");
    Attribute attribute2 = new Attribute("key", "value");
    boolean equals = attribute1.equals(attribute2);
    assertTrue(equals);
  }

  @Test
  public void testHashCode() {
    Attribute attribute = new Attribute("key", "value");
    int hashCode = attribute.hashCode();
    assertEquals(961024926, hashCode);
  }

  @Test
  public void testClone() {
    Attribute attribute = new Attribute("key", "value");
    Attribute clonedAttribute = attribute.clone();
    assertEquals(attribute, clonedAttribute);
  }

}