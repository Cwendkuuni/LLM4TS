import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.testing.TypeEquivalenceTester;
import com.google.javascript.rhino.testing.TypeSubtypingTester;
import com.google.javascript.rhino.testing.Values;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RecordTypeTest {

  @Test
  public void testRecordTypeEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("int", NodeOperations.INT_LITERAL(1)));
    properties.put("b", new RecordProperty("string", NodeOperations.STRING_LITERAL("hello")));

    RecordType recordTypeA = new RecordType(registry, properties, true);
    RecordType recordTypeB = new RecordType(registry, properties, true);

    TypeEquivalenceTester.assertEquals(recordTypeA, recordTypeB);
  }

  @Test
  public void testRecordTypeSubtyping() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("int", NodeOperations.INT_LITERAL(1)));
    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("int", NodeOperations.INT_LITERAL(1)));
    propertiesB.put("b", new RecordProperty("string", NodeOperations.STRING_LITERAL("hello")));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    assertTrue(recordTypeA.isSubtype(recordTypeB));
  }

  @Test
  public void testRecordTypeIncompatiblePropertyTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("int", NodeOperations.INT_LITERAL(1)));
    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("boolean", NodeOperations.BOOLEAN_LITERAL(true)));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    TypeSubtypingTester.assertFalse(recordTypeA, recordTypeB);
  }

  @Test
  public void testRecordTypeMissingProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("int", NodeOperations.INT_LITERAL(1)));
    Map<String, RecordProperty> propertiesB = Maps.newHashMap();

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    RecordType recordTypeB = new RecordType(registry, Collections.emptyMap(), true);

    TypeSubtypingTester.assertFalse(recordTypeA, recordTypeB);
  }

  @Test
  public void testRecordTypeFrozen() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = Maps.newHashMap();
    properties.put("a", new RecordProperty("int", NodeOperations.INT_LITERAL(1)));

    RecordType recordType = new RecordType(registry, properties, true);
    recordType.freeze();

    try {
      recordType.defineProperty("a", JSType.INT, false, null);
      fail("Record type should not be modifiable after freezing.");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @Test
  public void testRecordTypeGreatestSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> propertiesA = Maps.newHashMap();
    propertiesA.put("a", new RecordProperty("int", NodeOperations.INT_LITERAL(1)));
    propertiesA.put("b", new RecordProperty("int", NodeOperations.INT_LITERAL(2)));

    Map<String, RecordProperty> propertiesB = Maps.newHashMap();
    propertiesB.put("a", new RecordProperty("int", NodeOperations.INT_LITERAL(1)));
    propertiesB.put("c", new RecordProperty("int", NodeOperations.INT_LITERAL(3)));

    RecordType recordTypeA = new RecordType(registry, propertiesA, true);
    RecordType recordTypeB = new RecordType(registry, propertiesB, true);

    RecordType greatestSubtype = recordTypeA.getGreatestSubtype(recordTypeB);
    assertEquals(recordTypeB, greatestSubtype);
  }
}