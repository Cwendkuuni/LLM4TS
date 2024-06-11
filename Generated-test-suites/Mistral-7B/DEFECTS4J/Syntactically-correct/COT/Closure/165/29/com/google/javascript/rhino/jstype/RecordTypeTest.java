import com.google.javascript.rhino.*;
import static com.google.javascript.rhino.JSType.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty(intType(), null));
    properties.put("b", new RecordProperty(booleanType(), null));

    RecordType recordType = new RecordType(registry, properties);
    assertEquals(intType(), recordType.getPropertyType("a"));
    assertEquals(booleanType(), recordType.getPropertyType("b"));
  }

  @Test
  public void testRecordTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty(intType(), null));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty(intType(), null));
    propertiesB.put("b", new RecordProperty(booleanType(), null));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertTrue(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testRecordTypeIsNotSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty(intType(), null));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("b", new RecordProperty(booleanType(), null));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertFalse(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testRecordTypeIsEquivalent() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty(intType(), null));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty(intType(), null));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertTrue(recordTypeA.isEquivalentTo(recordTypeB));
  }

  @Test
  public void testRecordTypeWithIncompatibleProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty(intType(), null));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty(stringType(), null));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertFalse(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testRecordTypeWithMissingProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty(intType(), null));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("b", new RecordProperty(booleanType(), null));

    RecordType recordTypeA = new RecordType(registry, propertiesA);
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    assertFalse(recordTypeA.isSubtype(recordTypeB));
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordTypeWithNullProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty(intType(), null));

    try {
      new RecordType(registry, properties);
    } catch (IllegalStateException e) {
      assertEquals("RecordProperty associated with a property should not be null!", e.getMessage());
      throw e;
    }
  }

  @Test
  public void testRecordTypeFreeze() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty(intType(), null));

    RecordType recordType = new RecordType(registry, properties);
    recordType.defineProperty("a", intType(), false, null);

    assertFalse(recordType.isFrozen());

    recordType.defineProperty("b", booleanType(), false, null);

    assertTrue(recordType.isFrozen());
  }

  @Test
  public void testRecordTypeGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty(intType(), null));
    RecordType recordTypeA = new RecordType(registry, propertiesA);

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty(stringType(), null));
    RecordType recordTypeB = new RecordType(registry, propertiesB);

    Map<String, RecordProperty> propertiesC = Maps.newHashMap();
    propertiesC.put("a", new RecordProperty(numberType(), null));
    RecordType recordTypeC = new RecordType(registry, propertiesC);

    Map<String, RecordProperty> propertiesD = Maps.newHashMap();
    propertiesD.put("a", new RecordProperty(intType(), null));
    RecordType recordTypeD = new RecordType(registry, propertiesD);

    Map<String, RecordProperty> propertiesE = Maps.newHashMap();
    propertiesE.put("a", new RecordProperty(intType(), null));
    propertiesE.put("b", new RecordProperty(booleanType(), null));
    RecordType recordTypeE = new RecordType(registry, propertiesE);

    Map<String, RecordProperty> propertiesF = Maps.newHashMap();
    propertiesF.put("a", new RecordProperty(intType(), null));
    propertiesF.put("b", new RecordProperty(booleanType(), null));
    propertiesF.put("c", new RecordProperty(booleanType(), null));
    RecordType recordTypeF = new RecordType(registry, propertiesF);

    Map<String, RecordProperty> propertiesG = Maps.newHashMap();
    propertiesG.put("a", new RecordProperty(nullType(), null));
    RecordType recordTypeG = new RecordType(registry, propertiesG);

    Map<String, RecordProperty> propertiesH = Maps.newHashMap();
    propertiesH.put("a", new RecordProperty(objectType(), null));
    RecordType recordTypeH = new RecordType(registry, propertiesH);

    Map<String, RecordProperty> propertiesI = Maps.newHashMap();
    propertiesI.put("a", new RecordProperty(nullType(), null));
    propertiesI.put("b", new RecordProperty(objectType(), null));
    RecordType recordTypeI = new RecordType(registry, propertiesI);

    Map<String, RecordProperty> propertiesJ = Maps.newHashMap();
    propertiesJ.put("a", new RecordProperty(objectType(), null));
    propertiesJ.put("b", new RecordProperty(nullType(), null));
    RecordType recordTypeJ = new RecordType(registry, propertiesJ);

    UnionType greatestSubtype = recordTypeA.getGreatestSubtypeHelper(recordTypeB);
    assertEquals(recordTypeD, greatestSubtype);

    greatestSubtype = recordTypeD.getGreatestSubtypeHelper(recordTypeC);
    assertEquals(recordTypeE, greatestSubtype);

    greatestSubtype = recordTypeE.getGreatestSubtypeHelper(recordTypeF);
    assertEquals(recordTypeF, greatestSubtype);

    greatestSubtype = recordTypeF.getGreatestSubtypeHelper(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    assertEquals(registry.getNativeType(JSTypeNative.OBJECT_TYPE), greatestSubtype);

    greatestSubtype = recordTypeG.getGreatestSubtypeHelper(recordTypeH);
    assertEquals(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE), greatestSubtype);

    greatestSubtype = recordTypeH.getGreatestSubtypeHelper(recordTypeI);
    assertEquals(recordTypeI, greatestSubtype);

    greatestSubtype = recordTypeI.getGreatestSubtypeHelper(recordTypeJ);
    assertEquals(recordTypeJ, greatestSubtype);

    greatestSubtype = recordTypeJ.getGreatestSubtypeHelper(recordTypeF);
    assertEquals(recordTypeF, greatestSubtype);
  }
}