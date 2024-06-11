import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import static com.google.javascript.rhino.jstype.RecordTypeTest.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;

import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testRecordTypeConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", intType()));
    RecordType recordType = new RecordType(registry, properties);
    assertEquals(intType(), recordType.getPropertyType("prop1"));
  }

  @Test
  public void testIsEquivalentTo_SameRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", intType()));
    RecordType recordType1 = new RecordType(registry, properties);
    properties.put("prop2", new RecordProperty("prop2", intType()));
    RecordType recordType2 = new RecordType(registry, properties);
    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsEquivalentTo_DifferentRecordTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", new RecordProperty("prop1", intType()));
    RecordType recordType1 = new RecordType(registry, properties1);
    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop2", new RecordProperty("prop2", intType()));
    RecordType recordType2 = new RecordType(registry, properties2);
    assertFalse(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsEquivalentTo_DifferentRecordTypesWithSameProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", new RecordProperty("prop1", intType()));
    RecordType recordType1 = new RecordType(registry, properties1);
    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop1", new RecordProperty("prop1", stringType()));
    RecordType recordType2 = new RecordType(registry, properties2);
    assertFalse(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void testIsEquivalentTo_RecordAndObjectType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", intType()));
    RecordType recordType = new RecordType(registry, properties);
    ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
    assertFalse(recordType.isEquivalentTo(objectType));
  }

  @Test
  public void testGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", intType()));
    RecordType recordType = new RecordType(registry, properties);
    ObjectType expectedPrototype = registry.getNativeObjectType(
        JSTypeNative.OBJECT_TYPE);
    ObjectType actualPrototype = recordType.getImplicitPrototype();
    assertEquals(expectedPrototype, actualPrototype);
  }

  @Test(expected = IllegalStateException.class)
  public void testDefineProperty_FrozenRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", intType()));
    RecordType recordType = new RecordType(registry, properties);
    recordType.isFrozen = true;
    recordType.defineProperty("prop1", intType(), false, null);
  }

  @Test
  public void testGetLeastSupertype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", new RecordProperty("prop1", intType()));
    RecordType recordType1 = new RecordType(registry, properties1);
    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop1", new RecordProperty("prop1", intType()));
    properties2.put("prop2", new RecordProperty("prop2", stringType()));
    RecordType recordType2 = new RecordType(registry, properties2);
    RecordType expectedLeastSupertype = new RecordType(registry, properties1);
    JSType actualLeastSupertype = recordType1.getLeastSupertype(recordType2);
    assertEquals(expectedLeastSupertype, actualLeastSupertype);
  }

  @Test
  public void testGetLeastSupertype_RecordAndObjectType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", intType()));
    RecordType recordType = new RecordType(registry, properties);
    ObjectType expectedLeastSupertype = registry.getNativeObjectType(
        JSTypeNative.OBJECT_TYPE);
    JSType actualLeastSupertype = recordType.getLeastSupertype(null);
    assertEquals(expectedLeastSupertype, actualLeastSupertype);
  }

  @Test
  public void testGetLeastSupertype_DifferentRecordTypesWithSameProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", new RecordProperty("prop1", intType()));
    RecordType recordType1 = new RecordType(registry, properties1);
    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop1", new RecordProperty("prop1", stringType()));
    RecordType recordType2 = new RecordType(registry, properties2);
    RecordType expectedLeastSupertype = new RecordType(registry, properties1);
    JSType actualLeastSupertype = recordType1.getLeastSupertype(recordType2);
    assertEquals(expectedLeastSupertype, actualLeastSupertype);
  }

  @Test
  public void testGetGreatestSubtypeHelper_SameRecordTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", new RecordProperty("prop1", intType()));
    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType expectedGreatestSubtype = recordType1;
    JSType actualGreatestSubtype = recordType1.getGreatestSubtypeHelper(recordType1);
    assertEquals(expectedGreatestSubtype, actualGreatestSubtype);
  }

  @Test
  public void testGetGreatestSubtypeHelper_RecordAndObjectType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", intType()));
    RecordType recordType = new RecordType(registry, properties);
    ObjectType expectedGreatestSubtype = registry.getNativeObjectType(
        JSTypeNative.OBJECT_TYPE);
    JSType actualGreatestSubtype = recordType.getGreatestSubtypeHelper(null);
    assertEquals(expectedGreatestSubtype, actualGreatestSubtype);
  }

  @Test
  public void testGetGreatestSubtypeHelper_RecordWithProperty_Null() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", intType()));
    RecordType recordType = new RecordType(registry, properties);
    JSType expectedGreatestSubtype = registry.getNativeObjectType(
        JSTypeNative.NO_OBJECT_TYPE);
    JSType actualGreatestSubtype = recordType.getGreatestSubtypeHelper(null);
    assertEquals(expectedGreatestSubtype, actualGreatestSubtype);
  }

  @Test
  public void testGetGreatestSubtypeHelper_RecordWithProperty_DifferentRecordTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", new RecordProperty("prop1", intType()));
    RecordType recordType1 = new RecordType(registry, properties1);
    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop1", new RecordProperty("prop1", stringType()));
    properties2.put("prop2", new RecordProperty("prop2", intType()));
    RecordType recordType2 = new RecordType(registry, properties2);
    JSType expectedGreatestSubtype = new RecordType(registry, properties1);
    JSType actualGreatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertEquals(expectedGreatestSubtype, actualGreatestSubtype);
  }

  @Test
  public void testGetGreatestSubtypeHelper_RecordWithProperty_SameRecordTypeWithDifferentOrder() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop2", new RecordProperty("prop2", intType()));
    properties1.put("prop1", new RecordProperty("prop1", intType()));
    RecordType recordType1 = new RecordType(registry, properties1);
    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop1", new RecordProperty("prop1", intType()));
    properties2.put("prop2", new RecordProperty("prop2", intType()));
    RecordType recordType2 = new RecordType(registry, properties2);
    JSType expectedGreatestSubtype = new RecordType(registry, properties1);
    JSType actualGreatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertEquals(expectedGreatestSubtype, actualGreatestSubtype);
  }

  @Test
  public void testGetGreatestSubtypeHelper_RecordWithProperty_DifferentRecordTypesWithDifferentProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("prop1", new RecordProperty("prop1", intType()));
    RecordType recordType1 = new RecordType(registry, properties1);
    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("prop2", new RecordProperty("prop2", intType()));
    RecordType recordType2 = new RecordType(registry, properties2);
    JSType expectedGreatestSubtype = registry.getNativeObjectType(
        JSTypeNative.NO_OBJECT_TYPE);
    JSType actualGreatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertEquals(expectedGreatestSubtype, actualGreatestSubtype);
  }

  @Test
  public void testResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("prop1", new RecordProperty("prop1", intType()));
    RecordType recordType = new RecordType(registry, properties);
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>(registry, null);
    recordType.resolveInternal(errorReporter, scope);
  }
}