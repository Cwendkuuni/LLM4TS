import static org.junit.Assert.*;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.RecordType;
import com.google.javascript.jscomp.type.RecordTypeBuilder;
import com.google.javascript.jscomp.type.TypeUtils;
import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordTypeBuilder.RecordProperty("a", JSType.INT));
    RecordType recordType = new RecordType(registry, properties);
    assertEquals("RecordType{properties=[a: int], isFrozen=false}", recordType.toString());
  }

  @Test
  public void testRecordTypeEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordTypeBuilder.RecordProperty("a", JSType.INT));
    RecordType recordTypeA = new RecordType(registry, propertiesA);

    Map<String, RecordTypeBuilder.RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordTypeBuilder.RecordProperty("a", JSType.INT));
    propertiesB.put("b", new RecordTypeBuilder.RecordProperty("b", JSType.INT));
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    RecordType recordTypeC = new RecordType(registry, propertiesB);

    assertTrue(recordTypeA.isEquivalentTo(recordTypeA));
    assertTrue(recordTypeA.isEquivalentTo(recordTypeB));
    assertTrue(recordTypeA.isEquivalentTo(recordTypeC));

    assertFalse(recordTypeA.isEquivalentTo(new ObjectType(registry, null, null)));
  }

  @Test
  public void testRecordTypeFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordTypeBuilder.RecordProperty("a", JSType.INT));
    RecordType recordType = new RecordType(registry, properties);
    recordType.freeze();
    assertFalse(recordType.isFrozen());
    recordType.freeze();
    assertTrue(recordType.isFrozen());
  }

  @Test
  public void testRecordTypeSubtyping() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordTypeBuilder.RecordProperty> recordPropertiesA = Maps.newHashMap();
    recordPropertiesA.put("a", new RecordTypeBuilder.RecordProperty("a", JSType.INT));
    RecordType recordTypeA = new RecordType(registry, recordPropertiesA);

    Map<String, RecordTypeBuilder.RecordProperty> recordPropertiesB = Maps.newHashMap();
    recordPropertiesB.put("a", new RecordTypeBuilder.RecordProperty("a", JSType.INT));
    recordPropertiesB.put("b", new RecordTypeBuilder.RecordProperty("b", JSType.INT));
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB);

    Map<String, RecordTypeBuilder.RecordProperty> recordPropertiesC = Maps.newHashMap();
    recordPropertiesC.put("a", new RecordTypeBuilder.RecordProperty("a", JSType.INT));
    recordPropertiesC.put("b", new RecordTypeBuilder.RecordProperty("b", JSType.INT));
    recordPropertiesC.put("c", new RecordTypeBuilder.RecordProperty("c", JSType.INT));
    RecordType recordTypeC = new RecordType(registry, recordPropertiesC);

    Map<String, RecordTypeBuilder.RecordProperty> recordPropertiesD = Maps.newHashMap();
    recordPropertiesD.put("a", new RecordTypeBuilder.RecordProperty("a", JSType.STRING));
    RecordType recordTypeD = new RecordType(registry, recordPropertiesD);

    assertEquals(recordTypeA, recordTypeA.getGreatestSubtype(recordTypeA));
    assertEquals(recordTypeA, recordTypeA.getGreatestSubtype(recordTypeB));
    assertEquals(recordTypeB, recordTypeB.getGreatestSubtype(recordTypeA));
    assertEquals(recordTypeB, recordTypeB.getGreatestSubtype(recordTypeB));
    assertEquals(recordTypeC, recordTypeC.getGreatestSubtype(recordTypeA));
    assertEquals(recordTypeC, recordTypeC.getGreatestSubtype(recordTypeB));

    assertNotNull(recordTypeA.getGreatestSubtype(recordTypeD));
    assertNotNull(recordTypeD.getGreatestSubtype(recordTypeA));
  }

  @Test
  public void testRecordTypeHasProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordTypeBuilder.RecordProperty> recordPropertiesA = Maps.newHashMap();
    recordPropertiesA.put("a", new RecordTypeBuilder.RecordProperty("a", JSType.INT));
    RecordType recordTypeA = new RecordType(registry, recordPropertiesA);

    Map<String, RecordTypeBuilder.RecordProperty> recordPropertiesB = Maps.newHashMap();
    recordPropertiesB.put("a", new RecordTypeBuilder.RecordProperty("a", JSType.STRING));
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB);

    assertTrue(recordTypeA.hasProperty("a"));
    assertFalse(recordTypeB.hasProperty("a"));
  }

  @Test
  public void testRecordTypeGetPropertyType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordTypeBuilder.RecordProperty> recordPropertiesA = Maps.newHashMap();
    recordPropertiesA.put("a", new RecordTypeBuilder.RecordProperty("a", JSType.INT));
    RecordType recordTypeA = new RecordType(registry, recordPropertiesA);

    Map<String, RecordTypeBuilder.RecordProperty> recordPropertiesB = Maps.newHashMap();
    recordPropertiesB.put("a", new RecordTypeBuilder.RecordProperty("a", JSType.STRING));
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB);

    assertEquals(JSType.INT, recordTypeA.getPropertyType("a"));
    assertEquals(JSType.STRING, recordTypeB.getPropertyType("a"));
  }

  @Test
  public void testRecordTypeGetPropertyNode() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordTypeBuilder.RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("a", new RecordTypeBuilder.RecordProperty("a", JSType.INT));
    RecordType recordType = new RecordType(registry, recordProperties);

    Node nodePropertyA = recordType.getPropertyNode("a");

    assertTrue(nodePropertyA instanceof JSTypeName);
    assertEquals("a", ((JSTypeName) nodePropertyA).getName());
  }

  @Test
  public void testRecordTypeGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordTypeBuilder.RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("a", new RecordTypeBuilder.RecordProperty("a", JSType.INT));
    RecordType recordType = new RecordType(registry, recordProperties);

    ObjectType implicitPrototype = recordType.getImplicitPrototype();

    assertEquals(
        "ObjectType{name=Object, isAbstract=false, isConstructor=false, isFunction=false, isNative=false, isRecord=false, isStatic=false, isUnion=false, fields=[], methods=[], staticFields=[], staticMethods=[], interfaces=[], parentType=null}",
        implicitPrototype.toString());
  }

  @Test
  public void testRecordTypeDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordTypeBuilder.RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("a", new RecordTypeBuilder.RecordProperty("a", JSType.INT));
    RecordType recordType = new RecordType(registry, recordProperties);

    recordType.defineProperty("b", JSType.INT, false, null);

    assertTrue(recordType.hasProperty("b"));
    assertEquals(JSType.INT, recordType.getPropertyType("b"));
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordTypeDefinePropertyFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordTypeBuilder.RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("a", new RecordTypeBuilder.RecordProperty("a", JSType.INT));
    RecordType recordType = new RecordType(registry, recordProperties);
    recordType.freeze();
    recordType.defineProperty("b", JSType.INT, false, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRecordTypeDefinePropertyNullPropertyNode() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordTypeBuilder.RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("a", new RecordTypeBuilder.RecordProperty("a", JSType.INT));
    RecordType recordType = new RecordType(registry, recordProperties);

    recordType.defineProperty("b", JSType.INT, false, null);
    recordType.defineProperty("b", JSType.INT, false, null); // Should throw
  }

  @Test
  public void testRecordTypeToString() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordTypeBuilder.RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("a", new RecordTypeBuilder.RecordProperty("a", JSType.INT));
    RecordType recordType = new RecordType(registry, recordProperties);

    assertEquals("RecordType{properties=[a: int], isFrozen=false}", recordType.toString());
  }
}