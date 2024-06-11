import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.TypeRegistry;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

  private TypeRegistry registry;

  @Before
  public void setUp() {
    registry = new TypeRegistry();
  }

  @Test
  public void testRecordTypeCreation() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNumberType()));
    properties.put("b", new RecordProperty("b", registry.getBooleanType()));

    RecordType recordType = new RecordType(registry, properties);
    assertEquals(recordType.getClass(), RecordType.class);
    assertEquals(recordType.getName(), "{ a: Number, b: Boolean }");
    assertEquals(recordType.getPrototype(), registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    assertEquals(recordType.getImplicitPrototype(), registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    assertEquals(recordType.getPropertyCount(), 2);
  }

  @Test
  public void testRecordTypeEquals() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getNumberType()));
    properties1.put("b", new RecordProperty("b", registry.getBooleanType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getNumberType()));
    properties2.put("b", new RecordProperty("b", registry.getBooleanType()));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertEquals(recordType1, recordType2);
  }

  @Test
  public void testRecordTypeNotEqualsDifferentPropertyOrder() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("b", new RecordProperty("b", registry.getBooleanType()));
    properties1.put("a", new RecordProperty("a", registry.getNumberType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getNumberType()));
    properties2.put("b", new RecordProperty("b", registry.getBooleanType()));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse(recordType1.equals(recordType2));
  }

  @Test
  public void testRecordTypeNotEqualsDifferentPropertyTypes() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getNumberType()));
    properties1.put("b", new RecordProperty("b", registry.getBooleanType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getStringType()));
    properties2.put("b", new RecordProperty("b", registry.getBooleanType()));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse(recordType1.equals(recordType2));
  }

  @Test
  public void testRecordTypeSubtyping() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getNumberType()));
    properties1.put("b", new RecordProperty("b", registry.getBooleanType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testRecordTypeSubtypingFailed() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getNumberType()));
    properties1.put("b", new RecordProperty("b", registry.getBooleanType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getStringType()));
    properties2.put("c", new RecordProperty("c", registry.getBooleanType()));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testRecordTypeSubtypingTwoWay() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getNumberType()));
    properties1.put("b", new RecordProperty("b", registry.getBooleanType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("b", new RecordProperty("b", registry.getBooleanType()));
    properties2.put("a", new RecordProperty("a", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType1.isSubtype(recordType2));
    assertTrue(recordType2.isSubtype(recordType1));
  }

  @Test
  public void testRecordTypeGreatestSubtype() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getNumberType()));
    properties1.put("b", new RecordProperty("b", registry.getBooleanType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getNumberType()));
    properties2.put("c", new RecordProperty("c", registry.getBooleanType()));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);

    assertEquals(recordType1.getClass(), greatestSubtype.getClass());
    assertEquals(recordType1.getName(), greatestSubtype.getName());
  }

  @Test
  public void testRecordTypeGreatestSubtypeFailed() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getNumberType()));
    properties1.put("b", new RecordProperty("b", registry.getBooleanType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", registry.getStringType()));
    properties2.put("c", new RecordProperty("c", registry.getBooleanType()));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);

    assertNull(greatestSubtype);
  }

  @Test
  public void testRecordTypeGreatestSubtypeTwoWay() {
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", registry.getNumberType()));
    properties1.put("b", new RecordProperty("b", registry.getBooleanType()));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("b", new RecordProperty("b", registry.getBooleanType()));
    properties2.put("a", new RecordProperty("a", registry.getNumberType()));

    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);

    JSType greatestSubtype = recordType1.getGreatestSubtype(recordType2);

    assertEquals(recordType1.getClass(), greatestSubtype.getClass());
    assertEquals(recordType1.getName(), greatestSubtype.getName());

    greatestSubtype = recordType2.getGreatestSubtype(recordType1);

    assertEquals(recordType2.getClass(), greatestSubtype.getClass());
    assertEquals(recordType2.getName(), greatestSubtype.getName());
  }

  @Test
  public void testRecordTypeGreatestSubtypeRecordAndObject() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties);
    JSType greatestSubtype = recordType.getGreatestSubtype(
        registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));

    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
                     .getClass(), greatestSubtype.getClass());
    assertEquals(
        registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).getName(),
        greatestSubtype.getName());
  }

  @Test
  public void testRecordTypeGreatestSubtypeObjectAndRecord() {
    RecordType recordType = new RecordType(registry, Maps.<String, RecordProperty>newHashMap());

    JSType greatestSubtype = recordType.getGreatestSubtype(
        registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));

    assertEquals(recordType.getClass(), greatestSubtype.getClass());
    assertEquals(recordType.getName(), greatestSubtype.getName());
  }

  @Test
  public void testRecordTypeGreatestSubtypeRecordAndEmptyRecord() {
    Map<String, RecordProperty> properties = Maps.newHashMap();

    RecordType recordType = new RecordType(registry, properties);
    JSType greatestSubtype = recordType.getGreatestSubtype(
        registry.getRecordTypeBuilder(registry, properties).build());

    assertEquals(recordType.getClass(), greatestSubtype.getClass());
    assertEquals(recordType.getName(), greatestSubtype.getName());
  }

  @Test
  public void testRecordTypeGreatestSubtypeEmptyRecordAndRecord() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties);
    JSType greatestSubtype = registry.getRecordTypeBuilder(registry, properties)
        .build()
        .getGreatestSubtype(recordType);

    assertEquals(recordType.getClass(), greatestSubtype.getClass());
    assertEquals(recordType.getName(), greatestSubtype.getName());
  }

  @Test
  public void testRecordTypeGreatestSubtypeRecordAndUnionType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNumberType()));

    RecordType recordType = new RecordType(registry, properties);
    UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
    unionTypeBuilder.addAlternate(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    unionTypeBuilder.addAlternate(registry.getNativeObjectType(JSTypeNative.NULL_TYPE));
    JSType unionType = unionTypeBuilder.build();

    JSType greatestSubtype = recordType.getGreatestSubtype(unionType);

    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)
                     .getClass(), greatestSubtype.getClass());
    assertEquals(
        registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).getName(),
        greatestSubtype.getName());
  }

  @Test
  public void testRecordTypeGreatestSubtypeUnionTypeAndRecord() {
    RecordType recordType = new RecordType(registry, Maps.<String, RecordProperty>newHashMap());
    UnionTypeBuilder unionTypeBuilder = new UnionTypeBuilder(registry);
    unionTypeBuilder.addAlternate(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    unionTypeBuilder.addAlternate(registry.getNativeObjectType(JSTypeNative.NULL_TYPE));
    JSType unionType = unionTypeBuilder.build();

    JSType greatestSubtype = unionType.getGreatestSubtype(recordType);

    assertEquals(recordType.getClass(), greatestSubtype.getClass());
    assertEquals(recordType.getName(), greatestSubtype.getName());
  }

  @Test
  public void testRecordTypeGetProperty() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNumberType()));
    properties.put("b", new RecordProperty("b", registry.getBooleanType()));

    RecordType recordType = new RecordType(registry, properties);

    JSType propertyTypeA = recordType.getPropertyType("a");
    JSType propertyTypeB = recordType.getPropertyType("b");

    assertEquals(registry.getNumberType(), propertyTypeA);
    assertEquals(registry.getBooleanType(), propertyTypeB);
  }

  @Test
  public void testRecordTypeGetPropertyNull() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", registry.getNumberType()));
    properties.put("b", new RecordProperty("b", registry.getBooleanType()));

    RecordType recordType = new RecordType(registry, properties);

    assertNull(recordType.getPropertyType("c"));
  }
}