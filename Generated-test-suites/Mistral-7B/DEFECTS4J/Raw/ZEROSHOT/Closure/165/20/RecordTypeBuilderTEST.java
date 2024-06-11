package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;

public class RecordTypeBuilderTest {

  private JSTypeRegistry registry;
  private RecordTypeBuilder recordTypeBuilder;

  @Before
  public void setUp() {
    registry = mock(JSTypeRegistry.class);
    recordTypeBuilder = new RecordTypeBuilder(registry);
  }

  @Test
  public void testAddPropertyWithEmptyRecord() {
    JSType type = mock(JSType.class);
    Node propertyNode = mock(Node.class);

    RecordTypeBuilder builder = recordTypeBuilder.addProperty("name", type, propertyNode);

    assertNull(builder.build());
    assertNull(builder);
  }

  @Test
  public void testAddPropertyWithNonEmptyRecord() {
    JSType type1 = mock(JSType.class);
    Node propertyNode1 = mock(Node.class);
    JSType type2 = mock(JSType.class);
    Node propertyNode2 = mock(Node.class);

    recordTypeBuilder.addProperty("name1", type1, propertyNode1);
    RecordTypeBuilder builder = recordTypeBuilder.addProperty("name2", type2, propertyNode2);

    assertNotNull(builder.build());
    assertNotNull(builder);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddPropertyWithDuplicateName() {
    JSType type = mock(JSType.class);
    Node propertyNode = mock(Node.class);

    recordTypeBuilder.addProperty("name", type, propertyNode);
    RecordTypeBuilder builder = recordTypeBuilder.addProperty("name", type, propertyNode);
  }

  @Test
  public void testBuildWithEmptyRecord() {
    JSType result = recordTypeBuilder.build();

    assertNotNull(result);
    assertInstanceOf(NativeObjectType.class, result);
  }

  @Test
  public void testBuildWithNonEmptyRecord() {
    JSType type1 = mock(JSType.class);
    Node propertyNode1 = mock(Node.class);
    JSType type2 = mock(JSType.class);
    Node propertyNode2 = mock(Node.class);

    when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(mock(NativeObjectType.class));

    recordTypeBuilder.addProperty("name1", type1, propertyNode1);
    recordTypeBuilder.addProperty("name2", type2, propertyNode2);

    JSType result = recordTypeBuilder.build();

    assertNotNull(result);
    assertInstanceOf(RecordType.class, result);

    Map<String, RecordProperty> properties = ((RecordType) result).getProperties();
    assertEquals(2, properties.size());
    assertEquals("name1", properties.keySet().iterator().next());
    assertEquals("name2", properties.keySet().iterator().next());
  }

  private void assertEquals(Object expected, Object actual) {
    if (expected == null) {
      assertEquals(null, actual);
    } else if (expected.getClass().isInstance(actual)) {
      assertEquals(expected.toString(), actual.toString());
    } else {
      fail("Unexpected types");
    }
  }
}