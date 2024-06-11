import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import static com.google.javascript.rhino.jstype.RecordType.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.*;
import com.google.common.collect.Maps;
import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testRecordTypeConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty(intType(), null));
    properties.put("b", new RecordProperty(booleanType(), null));

    RecordType recordType = new RecordType(registry, properties, true);

    assertEquals(recordType.getClass(), RecordType.class);
    assertEquals(recordType.isSynthetic(), false);
    assertEquals(recordType.getProperties().size(), 2);
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty(intType(), null));
    properties1.put("b", new RecordProperty(booleanType(), null));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty(intType(), null));
    properties2.put("b", new RecordProperty(booleanType(), null));
    properties2.put("c", new RecordProperty(booleanType(), null));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));
  }

  @Test
  public void testCheckRecordEquivalenceHelper_false() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty(intType(), null));
    properties1.put("b", new RecordProperty(booleanType(), null));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty(intType(), null));
    properties2.put("c", new RecordProperty(booleanType(), null));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, false));
  }

  @Test
  public void testCheckRecordEquivalenceHelper_tolerateUnknowns() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty(intType(), null));
    properties1.put("b", new RecordProperty(booleanType(), null));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty(intType(), null));
    properties2.put("b", new RecordProperty(booleanType(), null));
    properties2.put("c", new RecordProperty(unknownType(), null));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, true));
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordTypeConstructor_nullProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", null);
    properties.put("b", new RecordProperty(booleanType(), null));

    new RecordType(registry, properties, true);
  }

  @Test
  public void testGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, Maps.newHashMap(), true);

    ObjectType implicitPrototype = recordType.getImplicitPrototype();
    assertEquals(implicitPrototype.getClass(), ObjectType.class);
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, Maps.newHashMap(), true);

    assertFalse(recordType.defineProperty("propertyName", intType(), false, null));
  }

  @Test
  public void testDefineProperty_frozen() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, Maps.newHashMap(), true);
    recordType.setFrozen(true);

    assertFalse(recordType.defineProperty("propertyName", intType(), false, null));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Type A
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty(intType(), null));
    RecordType recordTypeA = new RecordType(registry, propertiesA, true);

    // Type B
    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty(intType(), null));
    propertiesB.put("b", new RecordProperty(booleanType(), null));
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    // Type C
    Map<String, RecordProperty> propertiesC = Maps.newHashMap();
    propertiesC.put("b", new RecordProperty(booleanType(), null));
    RecordType recordTypeC = new RecordType(registry, propertiesC, true);

    // Type D
    Map<String, RecordProperty> propertiesD = Maps.newHashMap();
    propertiesD.put("c", new RecordProperty(booleanType(), null));
    RecordType recordTypeD = new RecordType(registry, propertiesD, true);

    // Test empty record type as greatest subtype
    assertEquals(recordTypeA.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)),
        registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));

    // Test record type A as greatest subtype of record type B
    assertEquals(recordTypeA.getGreatestSubtypeHelper(recordTypeB), recordTypeA);

    // Test record type B as greatest subtype of record type A
    assertEquals(recordTypeB.getGreatestSubtypeHelper(recordTypeA), recordTypeB);

    // Test record type A as greatest subtype of record type C
    assertEquals(recordTypeA.getGreatestSubtypeHelper(recordTypeC), recordTypeA);

    // Test record type C as greatest subtype of record type A
    assertEquals(recordTypeC.getGreatestSubtypeHelper(recordTypeA), recordTypeC);

    // Test record type B as greatest subtype of record type C
    assertEquals(recordTypeB.getGreatestSubtypeHelper(recordTypeC), recordTypeB);

    // Test record type C as greatest subtype of record type B (should return NO_TYPE)
    assertEquals(recordTypeC.getGreatestSubtypeHelper(recordTypeB), registry.getNativeType(JSTypeNative.NO_TYPE));

    // Test record type B as greatest subtype of record type D (should return NO_TYPE)
    assertEquals(recordTypeB.getGreatestSubtypeHelper(recordTypeD), registry.getNativeType(JSTypeNative.NO_TYPE));

    // Test record type D as greatest subtype of record type B
    assertEquals(recordTypeD.getGreatestSubtypeHelper(recordTypeB), recordTypeD);
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Type A
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty(intType(), null));
    RecordType recordTypeA = new RecordType(registry, propertiesA, true);

    // Type B
    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty(intType(), null));
    propertiesB.put("b", new RecordProperty(booleanType(), null));
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    // Type C
    Map<String, RecordProperty> propertiesC = Maps.newHashMap();
    propertiesC.put("a", new RecordProperty(intType(), null));
    RecordType recordTypeC = new RecordType(registry, propertiesC, true);

    // Test record type A is subtype of record type A
    assertTrue(recordTypeA.isSubtype(recordTypeA));

    // Test record type A is subtype of record type B
    assertTrue(recordTypeA.isSubtype(recordTypeB));

    // Test record type B is subtype of record type A
    assertTrue(recordTypeB.isSubtype(recordTypeA));

    // Test record type A is subtype of record type C
    assertTrue(recordTypeA.isSubtype(recordTypeC));

    // Test record type C is subtype of record type A
    assertTrue(recordTypeC.isSubtype(recordTypeA));

    // Test record type B is subtype of record type C (should return false)
    assertFalse(recordTypeB.isSubtype(recordTypeC));
  }

  @Test
  public void testIsSubtype_null() {
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, Maps.newHashMap(), true);

    assertFalse(recordType.isSubtype(null));
  }

  @Test
  public void testToString() {
    JSTypeRegistry registry = new JSTypeRegistry();

    // Type A
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty(intType(), null));
    RecordType recordTypeA = new RecordType(registry, propertiesA, true);

    assertEquals(recordTypeA.toString(), "RecordType{[a: int]}");
  }
}