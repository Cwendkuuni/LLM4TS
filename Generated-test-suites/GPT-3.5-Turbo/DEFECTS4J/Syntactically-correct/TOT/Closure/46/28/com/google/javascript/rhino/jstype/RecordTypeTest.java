import org.junit.Assert;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class RecordTypeTest {

  @Test
  public void testRecordTypeConstructor() {
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordTypeBuilder.RecordProperty(JSTypeNative.UNKNOWN_TYPE, null));
    properties.put("b", new RecordTypeBuilder.RecordProperty(JSTypeNative.STRING_TYPE, null));

    RecordType recordType = new RecordType(new JSTypeRegistry(), properties);

    Assert.assertNotNull(recordType);
  }

  @Test
  public void testRecordTypeIsEquivalentTo() {
    Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
    properties1.put("a", new RecordTypeBuilder.RecordProperty(JSTypeNative.UNKNOWN_TYPE, null));
    properties1.put("b", new RecordTypeBuilder.RecordProperty(JSTypeNative.STRING_TYPE, null));
    RecordType recordType1 = new RecordType(new JSTypeRegistry(), properties1);

    Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
    properties2.put("a", new RecordTypeBuilder.RecordProperty(JSTypeNative.UNKNOWN_TYPE, null));
    properties2.put("b", new RecordTypeBuilder.RecordProperty(JSTypeNative.STRING_TYPE, null));
    RecordType recordType2 = new RecordType(new JSTypeRegistry(), properties2);

    Map<String, RecordTypeBuilder.RecordProperty> properties3 = new HashMap<>();
    properties3.put("c", new RecordTypeBuilder.RecordProperty(JSTypeNative.UNKNOWN_TYPE, null));
    properties3.put("d", new RecordTypeBuilder.RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    RecordType recordType3 = new RecordType(new JSTypeRegistry(), properties3);

    Assert.assertTrue(recordType1.isEquivalentTo(recordType2));
    Assert.assertFalse(recordType1.isEquivalentTo(recordType3));
  }

  @Test
  public void testRecordTypeIsSubtype() {
    Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
    properties1.put("a", new RecordTypeBuilder.RecordProperty(JSTypeNative.UNKNOWN_TYPE, null));
    properties1.put("b", new RecordTypeBuilder.RecordProperty(JSTypeNative.STRING_TYPE, null));
    RecordType recordType1 = new RecordType(new JSTypeRegistry(), properties1);

    Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
    properties2.put("a", new RecordTypeBuilder.RecordProperty(JSTypeNative.UNKNOWN_TYPE, null));
    properties2.put("b", new RecordTypeBuilder.RecordProperty(JSTypeNative.STRING_TYPE, null));
    properties2.put("c", new RecordTypeBuilder.RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    RecordType recordType2 = new RecordType(new JSTypeRegistry(), properties2);

    Map<String, RecordTypeBuilder.RecordProperty> properties3 = new HashMap<>();
    properties3.put("c", new RecordTypeBuilder.RecordProperty(JSTypeNative.UNKNOWN_TYPE, null));
    properties3.put("d", new RecordTypeBuilder.RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    RecordType recordType3 = new RecordType(new JSTypeRegistry(), properties3);

    Assert.assertTrue(recordType1.isSubtype(recordType2));
    Assert.assertFalse(recordType1.isSubtype(recordType3));
  }

  @Test
  public void testRecordTypeDefineProperty() {
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordTypeBuilder.RecordProperty(JSTypeNative.UNKNOWN_TYPE, null));
    RecordType recordType = new RecordType(new JSTypeRegistry(), properties);

    boolean result1 = recordType.defineProperty("b", JSTypeNative.STRING_TYPE, false, null);

    Assert.assertTrue(result1);
    Assert.assertEquals(JSTypeNative.STRING_TYPE, recordType.getPropertyType("b"));

    boolean result2 = recordType.defineProperty("a", JSTypeNative.NUMBER_TYPE, false, null);

    Assert.assertFalse(result2);
    Assert.assertNotEquals(JSTypeNative.NUMBER_TYPE, recordType.getPropertyType("a"));
  }

  @Test
  public void testRecordTypeGetLeastSupertype() {
    Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
    properties1.put("a", new RecordTypeBuilder.RecordProperty(JSTypeNative.UNKNOWN_TYPE, null));
    properties1.put("b", new RecordTypeBuilder.RecordProperty(JSTypeNative.STRING_TYPE, null));
    RecordType recordType1 = new RecordType(new JSTypeRegistry(), properties1);

    Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
    properties2.put("a", new RecordTypeBuilder.RecordProperty(JSTypeNative.UNKNOWN_TYPE, null));
    properties2.put("b", new RecordTypeBuilder.RecordProperty(JSTypeNative.STRING_TYPE, null));
    properties2.put("c", new RecordTypeBuilder.RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    RecordType recordType2 = new RecordType(new JSTypeRegistry(), properties2);

    JSType leastSupertype1 = recordType1.getLeastSupertype(recordType2);
    JSType leastSupertype2 = recordType2.getLeastSupertype(recordType1);

    Assert.assertTrue(leastSupertype1.isRecordType());
    Assert.assertTrue(leastSupertype2.isRecordType());
    Assert.assertEquals(3, leastSupertype1.toMaybeRecordType().getPropertyCount());
    Assert.assertEquals(3, leastSupertype2.toMaybeRecordType().getPropertyCount());
  }

  @Test
  public void testRecordTypeGetGreatestSubtypeHelper() {
    Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
    properties1.put("a", new RecordTypeBuilder.RecordProperty(JSTypeNative.UNKNOWN_TYPE, null));
    properties1.put("b", new RecordTypeBuilder.RecordProperty(JSTypeNative.STRING_TYPE, null));
    RecordType recordType1 = new RecordType(new JSTypeRegistry(), properties1);

    Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
    properties2.put("a", new RecordTypeBuilder.RecordProperty(JSTypeNative.UNKNOWN_TYPE, null));
    properties2.put("b", new RecordTypeBuilder.RecordProperty(JSTypeNative.STRING_TYPE, null));
    properties2.put("c", new RecordTypeBuilder.RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    RecordType recordType2 = new RecordType(new JSTypeRegistry(), properties2);

    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

    Assert.assertTrue(greatestSubtype.isRecordType());
    Assert.assertEquals(3, greatestSubtype.toMaybeRecordType().getPropertyCount());
  }

  @Test
  public void testRecordTypeResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordTypeBuilder.RecordProperty(JSTypeNative.UNKNOWN_TYPE, null));
    RecordType recordType = new RecordType(registry, properties);

    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>();
    JSType resolvedType = recordType.resolveInternal(errorReporter, scope);

    Assert.assertEquals(recordType, resolvedType);
  }
}