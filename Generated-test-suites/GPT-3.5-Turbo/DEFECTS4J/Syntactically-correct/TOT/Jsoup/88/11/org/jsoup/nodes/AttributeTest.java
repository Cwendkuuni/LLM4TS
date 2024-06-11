
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.Before;

import org.jsoup.nodes.Attribute;

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
  public void testGetAndSetValue() {
    assertEquals("value", attribute.getValue());
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
  public void testEquals() {
    Attribute equalAttribute = new Attribute("key", "value");
    assertEquals(attribute, equalAttribute);
    assertEquals(attribute.hashCode(), equalAttribute.hashCode());

    Attribute differentAttribute = new Attribute("differentKey", "value");
    assertNotEquals(attribute, differentAttribute);
    assertNotEquals(attribute.hashCode(), differentAttribute.hashCode());

    Attribute nullValueAttribute = new Attribute("key", null);
    assertNotEquals(attribute, nullValueAttribute);
    assertNotEquals(attribute.hashCode(), nullValueAttribute.hashCode());
  }

  @Test
  public void testClone() {
    Attribute clonedAttribute = attribute.clone();
    assertEquals(attribute, clonedAttribute);
    assertEquals(attribute.hashCode(), clonedAttribute.hashCode());
  }

  @Test
  public void testCreateFromEncoded() {
    Attribute encodedAttribute = Attribute.createFromEncoded("encodedKey", "encodedValue");
    assertEquals("encodedKey", encodedAttribute.getKey());
    assertEquals("encodedValue", encodedAttribute.getValue());
  }

  @Test
  public void testIsDataAttribute() {
    assertFalse(attribute.isDataAttribute());

    Attribute dataAttribute = new Attribute("data-key", "value");
    assertTrue(dataAttribute.isDataAttribute());
  }

  @Test
  public void testShouldCollapseAttribute() {
    assertFalse(attribute.shouldCollapseAttribute());

    Attribute booleanAttribute = new Attribute("checked", "");
    assertTrue(booleanAttribute.shouldCollapseAttribute());
  }

  @Test
  public void testIsBooleanAttribute() {
    assertFalse(attribute.isBooleanAttribute());

    Attribute booleanAttribute = new Attribute("checked", "");
    assertTrue(booleanAttribute.isBooleanAttribute());
  }
}

