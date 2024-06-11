import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import com.google.javascript.rhino.jstype.record.RecordType;
import com.google.javascript.rhino.jstype.record.RecordTypeBuilder;

import java.util.Map;

public class RecordTypeTest {

  private static JSTypeRegistry registry;

  @org.junit.BeforeClass
  public static void setUp() {
    registry = new JSTypeRegistry();
  }

  @org.junit.Test
  public void testCreateRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));
    RecordType recordType = new RecordType(registry, properties);
    assertNotNull(recordType);
  }

  @org.junit.Test
  public void testEqualsSuperType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    RecordType recordType = new RecordType(registry, properties);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", registry.getStringType()));
    properties2.put("age", new RecordProperty("age", registry.getNumberType()));
    RecordType recordType2 = new RecordType(registry, properties2);

    assertEquals(recordType, recordType2);
  }

  @org.junit.Test
  public void testEqualsSameObject() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    RecordType recordType = new RecordType(registry, properties);

    assertEquals(recordType, recordType);
  }

  @org.junit.Test
  public void testEqualsDifferentType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    RecordType recordType = new RecordType(registry, properties);

    JSTypeRegistry registry2 = new JSTypeRegistry();
    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", registry2.getStringType()));
    RecordType recordType2 = new RecordType(registry2, properties2);

    assertFalse(recordType.equals(recordType2));
  }

  @org.junit.Test
  public void testIsEquivalentToSelf() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    RecordType recordType = new RecordType(registry, properties);

    assertTrue(recordType.isEquivalentTo(recordType));
  }

  @org.junit.Test
  public void testIsEquivalentToOtherRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));
    RecordType recordType = new RecordType(registry, properties);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", registry.getStringType()));
    properties2.put("age", new RecordProperty("age", registry.getNumberType()));
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType.isEquivalentTo(recordType2));
  }

  @org.junit.Test
  public void testIsEquivalentToObjectType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    RecordType recordType = new RecordType(registry, properties);

    assertFalse(recordType.isEquivalentTo(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
  }

  @org.junit.Test
  public void testGetImplicitPrototype() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    RecordType recordType = new RecordType(registry, properties);

    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
  }

  @org.junit.Test
  public void testDefineProperty() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    RecordType recordType = new RecordType(registry, properties);

    assertFalse(recordType.defineProperty("age", registry.getNumberType(), false, null));
    assertTrue(recordType.isFrozen());
  }

  @org.junit.Test
  public void testLeastSupertype() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));
    RecordType recordType = new RecordType(registry, properties);

    RecordType recordType2 = new RecordType(registry, Maps.newHashMap());
    recordType2.defineProperty("name", registry.getStringType(), false, null);
    recordType2.defineProperty("age", registry.getNumberType(), false, null);
    recordType2.defineProperty("gender", registry.getStringType(), false, null);

    JSType leastSupertype = recordType.getLeastSupertype(recordType2);
    assertEquals(recordType, leastSupertype);
  }

  @org.junit.Test
  public void testGreatestSubtype() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));
    RecordType recordType = new RecordType(registry, properties);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", registry.getStringType()));
    properties2.put("age", new RecordProperty("age", registry.getNumberType()));
    properties2.put("gender", new RecordProperty("gender", registry.getStringType()));
    RecordType recordType2 = new RecordType(registry, properties2);

    JSType greatestSubtype = recordType.getGreatestSubtype(recordType2);
    assertEquals(recordType2, greatestSubtype);
  }

  @org.junit.Test
  public void testGreatestSubtypeWithNullType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));
    RecordType recordType = new RecordType(registry, properties);

    UnionTypeBuilder builder = new UnionTypeBuilder(registry);
    builder.addAlternate(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    JSType greatestSubtype = recordType.getGreatestSubtype(builder.build());

    assertNull(greatestSubtype);
  }

  @org.junit.Test
  public void testGetPropertyType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));
    RecordType recordType = new RecordType(registry, properties);

    assertEquals(registry.getStringType(), recordType.getPropertyType("name"));
    assertEquals(registry.getNumberType(), recordType.getPropertyType("age"));
    assertNull(recordType.getPropertyType("gender"));
  }

  @org.junit.Test
  public void testHasProperty() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));
    RecordType recordType = new RecordType(registry, properties);

    assertTrue(recordType.hasProperty("name"));
    assertTrue(recordType.hasProperty("age"));
    assertFalse(recordType.hasProperty("gender"));
  }

  @org.junit.Test
  public void testIsSubtype() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));
    RecordType recordType = new RecordType(registry, properties);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", registry.getStringType()));
    properties2.put("age", new RecordProperty("age", registry.getNumberType()));
    properties2.put("gender", new RecordProperty("gender", registry.getStringType()));
    RecordType recordType2 = new RecordType(registry, properties2);

    assertTrue(recordType.isSubtype(recordType));
    assertTrue(recordType.isSubtype(recordType2));
    assertFalse(recordType2.isSubtype(recordType));
  }

  @org.junit.Test
  public void testIsSubtypeWithUnionType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));
    RecordType recordType = new RecordType(registry, properties);

    UnionTypeBuilder builder = new UnionTypeBuilder(registry);
    builder.addAlternate(recordType);
    builder.addAlternate(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    JSType unionType = builder.build();

    assertTrue(recordType.isSubtype(unionType));
  }

  @org.junit.Test
  public void testIsSubtypeWithOtherRecordType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));
    RecordType recordType = new RecordType(registry, properties);

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("name", new RecordProperty("name", registry.getStringType()));
    properties2.put("age", new RecordProperty("age", registry.getNumberType()));
    properties2.put("gender", new RecordProperty("gender", registry.getStringType()));
    RecordType recordType2 = new RecordType(registry, properties2);

    assertFalse(recordType2.isSubtype(recordType));
  }

  @org.junit.Test
  public void testIsSubtypeWithObjectType() {
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("name", new RecordProperty("name", registry.getStringType()));
    properties.put("age", new RecordProperty("age", registry.getNumberType()));
    RecordType recordType = new RecordType(registry, properties);

    assertFalse(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).isSubtype(recordType));
  }
}