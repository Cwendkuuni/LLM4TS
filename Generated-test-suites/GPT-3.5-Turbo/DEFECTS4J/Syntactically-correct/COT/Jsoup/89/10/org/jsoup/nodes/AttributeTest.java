import org.junit.*;
import static org.junit.Assert.*;

public class AttributeTest {

  private Attribute attribute;

  @Before
  public void setup() {
    attribute = new Attribute("key", "value");
  }

  @Test
  public void testGetKey() {
    assertEquals("key", attribute.getKey());
  }

  @Test
  public void testSetKey() {
    attribute.setKey("newKey");
    assertEquals("newKey", attribute.getKey());
  }

  @Test
  public void testGetValue() {
    assertEquals("value", attribute.getValue());
  }

  @Test
  public void testSetValue() {
    attribute.setValue("newValue");
    assertEquals("newValue", attribute.getValue());
  }

  @Test
  public void testHtml() {
    assertEquals("key=\"value\"", attribute.html());
  }

  @Test
  public void testToString() {
    assertEquals("key=\"value\"", attribute.toString());
  }

  @Test
  public void testCreateFromEncoded() {
    Attribute newAttribute = Attribute.createFromEncoded("key", "value");
    assertEquals("key", newAttribute.getKey());
    assertEquals("value", newAttribute.getValue());
  }

  @Test
  public void testIsDataAttribute() {
    assertFalse(attribute.isDataAttribute());
  }

  @Test
  public void testShouldCollapseAttribute() {
    assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
  }

  @Test
  public void testIsBooleanAttribute() {
    assertFalse(attribute.isBooleanAttribute());
  }

  @Test
  public void testEquals() {
    Attribute sameAttribute = new Attribute("key", "value");
    assertTrue(attribute.equals(sameAttribute));
  }

  @Test
  public void testHashCode() {
    Attribute sameAttribute = new Attribute("key", "value");
    assertEquals(attribute.hashCode(), sameAttribute.hashCode());
  }

  @Test
  public void testClone() {
    Attribute clonedAttribute = attribute.clone();
    assertEquals(attribute.getKey(), clonedAttribute.getKey());
    assertEquals(attribute.getValue(), clonedAttribute.getValue());
  }

  @Test
  public void testSetKeyUpdateParent() {
    Attributes parent = new Attributes();
    parent.put("key", "value");
    attribute = new Attribute("key", "value", parent);
    attribute.setKey("newKey");
    assertNull(parent.get("key"));
    assertEquals("value", parent.get("newKey"));
  }

  @Test
  public void testSetValueUpdateParent() {
    Attributes parent = new Attributes();
    parent.put("key", "value");
    attribute = new Attribute("key", "value", parent);
    attribute.setValue("newValue");
    assertEquals("value", parent.get("key"));
    assertEquals("newValue", parent.get("key"));
  }

}