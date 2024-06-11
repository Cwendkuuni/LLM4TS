import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  @Test
  public void testRecordTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSType.INT));
    properties.put("b", new RecordProperty("b", JSType.STRING));

    RecordType recordType = new RecordType(registry, properties, true);

    assertEquals("{ a: int, b: string }", recordType.toString());
    assertTrue(recordType.isSubtype(new RecordType(registry, properties, false)));
    assertTrue(recordType.isSubtype(new RecordType(registry, properties, true)));
    assertFalse(recordType.isSubtype(new RecordType(registry, null, true)));

    JSType recordTypeWithoutProperty = new RecordType(registry, properties, true);
    JSType recordTypeWithMissingProperty = new RecordType(registry, null, true);

    assertFalse(recordTypeWithoutProperty.isSubtype(recordTypeWithMissingProperty));
    assertFalse(recordTypeWithMissingProperty.isSubtype(recordTypeWithoutProperty));

    assertEquals(recordType.getImplicitPrototype(),
        registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
  }

  @Test
  public void testRecordPropertyTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSType.INT));
    properties.put("b", new RecordProperty("b", JSType.BOOLEAN));

    RecordType recordType = new RecordType(registry, properties, true);

    JSType intType = JSType.INT;
    JSType booleanType = JSType.BOOLEAN;

    assertEquals(intType, recordType.getPropertyType("a"));
    assertEquals(booleanType, recordType.getPropertyType("b"));

    RecordType recordTypeWithExtraProperty = new RecordType(registry,
        Maps.<String, RecordProperty>newHashMap(), true);
    RecordType recordTypeWithMissingProperty = new RecordType(registry,
        null, true);

    assertNull(recordTypeWithExtraProperty.getPropertyType("c"));
    assertNull(recordTypeWithMissingProperty.getPropertyType("a"));
  }

  @Test
  public void testRecordEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties1 = Maps.newHashMap();
    properties1.put("a", new RecordProperty("a", JSType.INT));
    properties1.put("b", new RecordProperty("b", JSType.STRING));

    Map<String, RecordProperty> properties2 = Maps.newHashMap();
    properties2.put("a", new RecordProperty("a", JSType.INT));
    properties2.put("b", new RecordProperty("b", JSType.NUMBER));

    RecordType recordType1 = new RecordType(registry, properties1, true);
    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, false));
    assertFalse(recordType2.checkRecordEquivalenceHelper(recordType1, false));

    RecordType recordType3 = new RecordType(registry, properties1, true);
    RecordType recordType4 = new RecordType(registry, properties2, true);

    assertTrue(recordType3.checkRecordEquivalenceHelper(recordType4, true));
    assertTrue(recordType4.checkRecordEquivalenceHelper(recordType3, true));
  }

  @Test
  public void testRecordSubtyping() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("a", JSType.INT));
    propertiesA.put("b", new RecordProperty("b", JSType.STRING));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("a", JSType.INT));
    propertiesB.put("b", new RecordProperty("b", JSType.STRING));
    propertiesB.put("c", new RecordProperty("c", JSType.BOOLEAN));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    RecordType recordTypeC = new RecordType(registry, propertiesB, true);
    recordTypeC.removeProperty("c");

    assertEquals(recordTypeB, recordTypeC.getGreatestSubtypeHelper(recordTypeA));
    assertEquals(recordTypeA, recordTypeC.getGreatestSubtypeHelper(recordTypeB));

    assertEquals(recordTypeA, recordTypeA.getGreatestSubtypeHelper(recordTypeB));
    assertEquals(recordTypeB, recordTypeB.getGreatestSubtypeHelper(recordTypeA));
  }

  @Test
  public void testRecordEmptySubtyping() {
    JSTypeRegistry registry = new JSTypeRegistry();

    RecordType recordTypeA = new RecordType(registry, null, true);
    RecordType recordTypeB = new RecordType(registry, null, true);

    assertEquals(recordTypeB, recordTypeA.getGreatestSubtypeHelper(recordTypeB));
    assertEquals(recordTypeA, recordTypeA.getGreatestSubtypeHelper(recordTypeB));
    assertEquals(recordTypeB, registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).getGreatestSubtype(recordTypeB));
    assertEquals(recordTypeA, registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE).getGreatestSubtype(recordTypeA));
  }

  @Test
  public void testRecordToString() {
    JSTypeRegistry registry = new JSTypeRegistry();

    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("a", JSType.INT));
    properties.put("b", new RecordProperty("b", JSType.STRING));

    RecordType recordType = new RecordType(registry, properties, true);

    String recordTypeString = recordType.toString();
    String expectedRecordTypeString = "{ a: int, b: string }";

    assertEquals(expectedRecordTypeString, recordTypeString);
  }
}