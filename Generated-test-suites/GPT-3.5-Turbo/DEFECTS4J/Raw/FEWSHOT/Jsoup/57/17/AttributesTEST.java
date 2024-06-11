package org.jsoup.nodes;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributesTest {
  
  private Attributes attributes;

  @Before
  public void setup() {
    attributes = new Attributes();
  }

  @Test
  public void testGet() {
    String key = "key";
    String value = "value";

    attributes.put(key, value);

    assertEquals(value, attributes.get(key));
  }

  @Test
  public void testGetIgnoreCase() {
    String key = "key";
    String value = "value";

    attributes.put(key, value);

    assertEquals(value, attributes.getIgnoreCase(key.toUpperCase()));
  }

  @Test
  public void testPut() {
    String key = "key";
    String value = "value";

    attributes.put(key, value);

    assertTrue(attributes.hasKey(key));
    assertEquals(value, attributes.get(key));
  }

  @Test
  public void testPutBoolean_True() {
    String key = "key";

    attributes.put(key, true);

    assertTrue(attributes.hasKey(key));
    assertEquals("", attributes.get(key));
  }

  @Test
  public void testPutBoolean_False() {
    String key = "key";

    attributes.put(key, false);

    assertFalse(attributes.hasKey(key));
  }

  @Test
  public void testPutAttribute() {
    String key = "key";
    String value = "value";
    Attribute attribute = new Attribute(key, value);

    attributes.put(attribute);

    assertTrue(attributes.hasKey(key));
    assertEquals(value, attributes.get(key));
  }

  @Test
  public void testRemove() {
    String key = "key";
    String value = "value";

    attributes.put(key, value);
    attributes.remove(key);

    assertFalse(attributes.hasKey(key));
    assertEquals("", attributes.get(key));
  }

  @Test
  public void testRemoveIgnoreCase() {
    String key = "key";
    String value = "value";

    attributes.put(key, value);
    attributes.removeIgnoreCase(key.toUpperCase());

    assertFalse(attributes.hasKey(key));
    assertEquals("", attributes.get(key));
  }

  @Test
  public void testHasKey() {
    String key = "key";
    String value = "value";

    attributes.put(key, value);

    assertTrue(attributes.hasKey(key));
    assertFalse(attributes.hasKey("nonexistent"));
  }

  @Test
  public void testHasKeyIgnoreCase() {
    String key = "key";
    String value = "value";

    attributes.put(key, value);

    assertTrue(attributes.hasKeyIgnoreCase(key.toUpperCase()));
    assertFalse(attributes.hasKeyIgnoreCase("nonexistent"));
  }

  @Test
  public void testSize() {
    String key1 = "key1";
    String value1 = "value1";
    String key2 = "key2";
    String value2 = "value2";

    attributes.put(key1, value1);
    attributes.put(key2, value2);

    assertEquals(2, attributes.size());
  }

  @Test
  public void testAddAll() {
    String key1 = "key1";
    String value1 = "value1";
    String key2 = "key2";
    String value2 = "value2";

    Attributes otherAttributes = new Attributes();
    otherAttributes.put(key1, value1);
    otherAttributes.put(key2, value2);

    attributes.addAll(otherAttributes);

    assertEquals(value1, attributes.get(key1));
    assertEquals(value2, attributes.get(key2));
    assertEquals(2, attributes.size());
  }

  @Test
  public void testIterator() {
    String key1 = "key1";
    String value1 = "value1";
    String key2 = "key2";
    String value2 = "value2";

    attributes.put(key1, value1);
    attributes.put(key2, value2);

    int count = 0;
    for (Attribute attribute : attributes) {
      count++;
    }

    assertEquals(2, count);
  }

  @Test
  public void testAsList() {
    String key1 = "key1";
    String value1 = "value1";
    String key2 = "key2";
    String value2 = "value2";

    attributes.put(key1, value1);
    attributes.put(key2, value2);

    assertEquals(value1, attributes.asList().get(0).getValue());
    assertEquals(value2, attributes.asList().get(1).getValue());
  }

  @Test
  public void testDataset() {
    String key1 = "key1";
    String value1 = "value1";
    String key2 = "key2";
    String value2 = "value2";

    attributes.put(key1, value1);
    attributes.put(key2, value2);

    assertTrue(attributes.dataset().containsKey(key1));
    assertTrue(attributes.dataset().containsKey(key2));
    assertEquals(value1, attributes.dataset().get(key1));
    assertEquals(value2, attributes.dataset().get(key2));
  }

  @Test
  public void testHtml() {
    String key1 = "key1";
    String value1 = "value1";
    String key2 = "key2";
    String value2 = "value2";

    attributes.put(key1, value1);
    attributes.put(key2, value2);

    String expectedHtml = " " + key1 + "=\"" + value1 + "\" " + key2 + "=\"" + value2 + "\"";
    assertEquals(expectedHtml, attributes.html());
  }

  @Test
  public void testEquals_SameObject() {
    assertEquals(attributes, attributes);
  }

  @Test
  public void testEquals_DifferentClass() {
    assertNotEquals(attributes, new Object());
  }

  @Test
  public void testEquals_NullAttributes() {
    Attributes otherAttributes = new Attributes();
    assertNotEquals(attributes, otherAttributes);
  }

  @Test
  public void testEquals_DifferentAttributes() {
    String key1 = "key1";
    String value1 = "value1";

    attributes.put(key1, value1);

    Attributes otherAttributes = new Attributes();

    assertNotEquals(attributes, otherAttributes);
  }

  @Test
  public void testEquals_SameAttributes() {
    String key1 = "key1";
    String value1 = "value1";

    attributes.put(key1, value1);

    Attributes otherAttributes = new Attributes();
    otherAttributes.put(key1, value1);

    assertEquals(attributes, otherAttributes);
  }

  @Test
  public void testHashCode_SameAttributes() {
    String key1 = "key1";
    String value1 = "value1";

    attributes.put(key1, value1);

    Attributes otherAttributes = new Attributes();
    otherAttributes.put(key1, value1);

    assertEquals(attributes.hashCode(), otherAttributes.hashCode());
  }

  @Test
  public void testClone() {
    String key1 = "key1";
    String value1 = "value1";

    attributes.put(key1, value1);

    Attributes cloneAttributes = attributes.clone();

    assertEquals(attributes, cloneAttributes);
    assertNotSame(attributes, cloneAttributes);
    assertSame(attributes.asList().get(0), cloneAttributes.asList().get(0));
  }
}