import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.RecordTypeBuilder;
import com.google.javascript.rhino.jstype.TypeRegistry;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class RecordTypeTest {

  private TypeRegistry registry;
  private RecordType recordType;

  @Before
  public void setup() {
    registry = new TypeRegistry();
    Map<String, RecordProperty> recordProperties = new HashMap<>();
    recordProperties.put("name", new RecordProperty("name", String.class));
    recordProperties.put("age", new RecordProperty("age", Integer.class));
    recordType = new RecordType(registry, recordProperties, true);
  }

  @Test
  public void testRecordTypeBasicFunctionality() {
    JSType recordTypeInstance = recordType.getInstanceType(registry);
    Assert.assertEquals(recordType, recordTypeInstance.toMaybeRecordType());
    Assert.assertTrue(recordTypeInstance.isRecordType());
  }

  @Test
  public void testRecordTypeEquivalence() {
    Map<String, RecordProperty> recordProperties2 = new HashMap<>();
    recordProperties2.putAll(recordProperties);
    RecordType recordType2 = new RecordType(registry, recordProperties2, true);

    Assert.assertTrue(recordType.checkRecordEquivalenceHelper(recordType2, true));
  }

  @Test(expected = IllegalStateException.class)
  public void testRecordTypeFrozen() {
    recordType.defineProperty("newProperty", Integer.class, false, null);
  }

  @Test
  public void testRecordTypeSubtyping() {
    Map<String, RecordProperty> recordPropertiesSub = new HashMap<>();
    recordPropertiesSub.putAll(recordProperties);
    recordPropertiesSub.remove("age");
    RecordType recordTypeSub = new RecordType(registry, recordPropertiesSub, true);

    JSType greaterSubtype = recordType.getGreatestSubtypeHelper(recordTypeSub.getInstanceType(registry));
    Assert.assertEquals(recordType, greaterSubtype);

    Map<String, RecordProperty> recordPropertiesSuper = new HashMap<>();
    recordPropertiesSuper.putAll(recordProperties);
    recordPropertiesSuper.put("newProperty", String.class);
    RecordType recordTypeSuper = new RecordType(registry, recordPropertiesSuper, true);

    greaterSubtype = recordTypeSub.getGreatestSubtypeHelper(recordTypeSuper.getInstanceType(registry));
    Assert.assertEquals(recordTypeSuper, greaterSubtype);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRecordTypeIncompatiblePropertyTypes() {
    Map<String, RecordProperty> recordPropertiesIncompatible = new HashMap<>();
    recordPropertiesIncompatible.putAll(recordProperties);
    recordPropertiesIncompatible.put("age", new RecordProperty("age", Double.class));
    RecordType recordTypeIncompatible = new RecordType(registry, recordPropertiesIncompatible, true);

    RecordType incompatibleRecordType = new RecordType(registry, recordProperties, true);
    Assert.assertFalse(incompatibleRecordType.checkRecordEquivalenceHelper(recordTypeIncompatible, false));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRecordTypeMissingProperty() {
    Map<String, RecordProperty> recordPropertiesMissing = new HashMap<>();
    recordPropertiesMissing.put("name", new RecordProperty("name", String.class));
    RecordType recordTypeMissing = new RecordType(registry, recordPropertiesMissing, true);

    JSType recordTypeInstanceMissing = recordTypeMissing.getInstanceType(registry);
    Assert.assertFalse(recordTypeInstanceMissing.hasProperty("age"));
    recordTypeInstanceMissing.defineProperty("age", Integer.class, false, null);
    Assert.assertTrue(recordTypeInstanceMissing.hasProperty("age"));

    Assert.assertFalse(recordType.checkRecordEquivalenceHelper(recordTypeMissing, false));
  }

  @Test
  public void testRecordTypeToString() {
    SortedMap<String, JSType> sortedProperties = new TreeMap<>();
    sortedProperties.putAll(recordType.properties);
    String expectedToString = "{name: String, age: Integer}";
    String actualToString = sortedProperties.toString();
    Assert.assertEquals(expectedToString, actualToString);
  }

  @Test
  public void testRecordTypeToPrototypeObjectType() {
    Assert.assertEquals(PrototypeObjectType.class, recordType.getClass().getSuperclass());
  }

  @Test
  public void testRecordTypeGetImplicitPrototype() {
    Assert.assertEquals(Object.class, recordType.getImplicitPrototype().getClass());
  }

  @Test
  public void testRecordTypeDefineProperty() {
    recordType.defineProperty("newProperty", String.class, false, null);
    Assert.assertTrue(recordType.hasProperty("newProperty"));
  }

  @Test
  public void testRecordTypeGetGreatestSubtypeHelperWithEmptyRecord() {
    RecordType emptyRecordType = new RecordType(registry, new HashMap<>(), true);
    JSType emptyRecordTypeInstance = emptyRecordType.getInstanceType(registry);

    Assert.assertTrue(recordType.getGreatestSubtypeHelper(emptyRecordTypeInstance).isEmptyType());
  }

  @Test
  public void testRecordTypeGetGreatestSubtypeHelperWithObjectType() {
    JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

    Assert.assertEquals(objectType, recordType.getGreatestSubtypeHelper(objectType));
  }
}