import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.UnionTypeBuilder;
import org.junit.Test;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("a", new RecordProperty(JSType.getIntType(), null));
    recordProperties.put("b", new RecordProperty(JSType.getStringType(), null));

    RecordType recordType = new RecordType(registry, recordProperties, true);
    assertEquals("RecordType", recordType.getName());
    assertEquals(2, recordType.getProperties().size());

    RecordProperty recordPropertyA = recordType.getProperty("a");
    RecordProperty recordPropertyB = recordType.getProperty("b");

    assertEquals(JSType.getIntType(), recordPropertyA.getType());
    assertEquals(JSType.getStringType(), recordPropertyB.getType());
  }

  @Test
  public void testRecordTypeGetImplicitPrototype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();

    RecordType recordType = new RecordType(registry, recordProperties, true);
    ObjectType implicitPrototype = recordType.getImplicitPrototype();

    assertEquals(JSTypeNative.OBJECT_TYPE, implicitPrototype);
  }

  @Test
  public void testRecordTypeDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("a", new RecordProperty(JSType.getIntType(), null));
    recordProperties.put("b", new RecordProperty(JSType.getStringType(), null));

    RecordType recordType = new RecordType(registry, recordProperties, true);

    recordType.defineProperty("c", JSType.getBooleanType(), false, null);

    RecordProperty recordPropertyC = recordType.getProperty("c");

    assertEquals(JSType.getBooleanType(), recordPropertyC.getType());
    assertFalse(recordPropertyC.isPropertyNodePresent());
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordTypeDefinePropertyWithNullPropertyNode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();
    recordProperties.put("a", new RecordProperty(JSType.getIntType(), null));
    recordProperties.put("b", new RecordProperty(JSType.getStringType(), null));

    RecordType recordType = new RecordType(registry, recordProperties, true);

    recordType.defineProperty("c", JSType.getBooleanType(), false, null);
    recordType.defineProperty("c", JSType.getBooleanType(), false, null);
  }

  @Test
  public void testRecordTypeGetGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();

    recordPropertiesA.put("a", new RecordProperty(JSType.getIntType(), null));
    recordPropertiesA.put("b", new RecordProperty(JSType.getStringType(), null));
    RecordType recordTypeA = new RecordType(registry, recordPropertiesA, true);

    recordPropertiesB.put("a", new RecordProperty(JSType.getIntType(), null));
    recordPropertiesB.put("b", new RecordProperty(JSType.getStringType(), null));
    recordPropertiesB.put("c", new RecordProperty(JSType.getBooleanType(), null));
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB, true);

    JSType greatestSubtype = recordTypeA.getGreatestSubtype(recordTypeB);

    assertEquals(recordTypeB, greatestSubtype);
  }

  @Test
  public void testRecordTypeGetGreatestSubtypeWithEmptyObjectType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();

    RecordType recordType = new RecordType(registry, recordProperties, true);
    JSType emptyRecordType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    JSType greatestSubtype = recordType.getGreatestSubtype(emptyRecordType);

    assertEquals(emptyRecordType, greatestSubtype);
  }

  @Test
  public void testRecordTypeGetGreatestSubtypeWithNonRecordType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordProperties = Maps.newHashMap();

    RecordType recordType = new RecordType(registry, recordProperties, true);

    JSType nonRecordType = registry.getNativeType(JSTypeNative.NO_INTEGER_TYPE);

    JSType greatestSubtype = recordType.getGreatestSubtype(nonRecordType);

    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE), greatestSubtype);
  }

  @Test
  public void testRecordTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();
    Map<String, RecordProperty> recordPropertiesC = Maps.newHashMap();

    recordPropertiesA.put("a", new RecordProperty(JSType.getIntType(), null));
    recordPropertiesA.put("b", new RecordProperty(JSType.getStringType(), null));
    RecordType recordTypeA = new RecordType(registry, recordPropertiesA, true);

    recordPropertiesB.put("a", new RecordProperty(JSType.getIntType(), null));
    recordPropertiesB.put("b", new RecordProperty(JSType.getStringType(), null));
    recordPropertiesB.put("c", new RecordProperty(JSType.getBooleanType(), null));
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB, true);

    recordPropertiesC.put("a", new RecordProperty(JSType.getIntType(), null));
    recordPropertiesC.put("b", new RecordProperty(JSType.getStringType(), null));
    RecordType recordTypeC = new RecordType(registry, recordPropertiesC, true);

    assertTrue(recordTypeA.isSubtype(recordTypeB));
    assertTrue(recordTypeB.isSubtype(recordTypeA));
    assertTrue(recordTypeA.isSubtype(recordTypeC));
    assertTrue(recordTypeC.isSubtype(recordTypeA));

    JSType otherType = registry.getNativeType(JSTypeNative.OBJECT_TYPE);
    assertFalse(recordTypeA.isSubtype(otherType));
    assertFalse(otherType.isSubtype(recordTypeA));
  }

  @Test
  public void testRecordTypeCheckRecordEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();

    recordPropertiesA.put("a", new RecordProperty(JSType.getIntType(), null));
    recordPropertiesA.put("b", new RecordProperty(JSType.getStringType(), null));
    RecordType recordTypeA = new RecordType(registry, recordPropertiesA, true);

    recordPropertiesB.put("a", new RecordProperty(JSType.getIntType(), null));
    recordPropertiesB.put("b", new RecordProperty(JSType.getStringType(), null));
    recordPropertiesB.put("c", new RecordProperty(JSType.getBooleanType(), null));
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB, true);

    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, false));

    recordPropertiesB.put("d", new RecordProperty(JSType.getBooleanType(), null));

    assertFalse(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, false));
  }

  @Test
  public void testRecordTypeCheckRecordEquivalenceHelperWithTolerateUnknowns() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> recordPropertiesA = Maps.newHashMap();
    Map<String, RecordProperty> recordPropertiesB = Maps.newHashMap();

    recordPropertiesA.put("a", new RecordProperty(JSType.getIntType(), null));
    recordPropertiesA.put("b", new RecordProperty(JSType.getStringType(), null));
    RecordType recordTypeA = new RecordType(registry, recordPropertiesA, true);

    recordPropertiesB.put("a", new RecordProperty(JSType.getIntType(), null));
    recordPropertiesB.put("b", new RecordProperty(JSType.getStringType(), null));
    recordPropertiesB.put("c", new RecordProperty(JSType.getBooleanType(), null));
    RecordType recordTypeB = new RecordType(registry, recordPropertiesB, true);

    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, true));

    recordPropertiesB.put("d", new RecordProperty(JSType.getBooleanType(), null));

    assertTrue(recordTypeA.checkRecordEquivalenceHelper(recordTypeB, true));
  }
}