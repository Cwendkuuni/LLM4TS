import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RecordTypeTest {

  private RecordType recordType;

  @Before
  public void setUp() {
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordTypeBuilder.RecordProperty(JSTypeNative.STRING_TYPE, null));
    properties.put("b", new RecordTypeBuilder.RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    properties.put("c", new RecordTypeBuilder.RecordProperty(JSTypeNative.BOOLEAN_TYPE, null));
    properties.put("d", new RecordTypeBuilder.RecordProperty(JSTypeNative.OBJECT_TYPE, null));

    recordType = new RecordType(properties);
  }

  @Test
  public void testGetPropertyType_existingProperty_returnsType() {
    JSType expectedType = JSTypeNative.STRING_TYPE;
    JSType actualType = recordType.getPropertyType("a");
    Assert.assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetPropertyType_nonExistingProperty_returnsNull() {
    JSType actualType = recordType.getPropertyType("x");
    Assert.assertNull(actualType);
  }

  @Test
  public void testDefineDeclaredProperty_newProperty_successfullyDefinesProperty() {
    String propertyName = "e";
    JSType propertyType = JSTypeNative.STRING_TYPE;

    boolean success = recordType.defineProperty(propertyName, propertyType, false, null);

    Assert.assertTrue(success);
    Assert.assertEquals(propertyType, recordType.getPropertyType(propertyName));
  }

  @Test
  public void testDefineDeclaredProperty_existingProperty_failsAndDoesNotChangePropertyType() {
    String propertyName = "a";
    JSType propertyType = JSTypeNative.NUMBER_TYPE;

    boolean success = recordType.defineProperty(propertyName, propertyType, false, null);

    Assert.assertFalse(success);
    Assert.assertEquals(JSTypeNative.STRING_TYPE, recordType.getPropertyType(propertyName));
  }

  @Test
  public void testIsSubtype_subtype_returnsTrue() {
    RecordType subtype = createSubtype();

    boolean isSubtype = subtype.isSubtype(recordType);

    Assert.assertTrue(isSubtype);
  }

  @Test
  public void testIsSubtype_notSubtype_returnsFalse() {
    RecordType other = createOtherRecordType();

    boolean isSubtype = other.isSubtype(recordType);

    Assert.assertFalse(isSubtype);
  }

  @Test
  public void testIsSubtype_sameType_returnsTrue() {
    boolean isSubtype = recordType.isSubtype(recordType);

    Assert.assertTrue(isSubtype);
  }

  private RecordType createSubtype() {
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordTypeBuilder.RecordProperty(JSTypeNative.STRING_TYPE, null));
    properties.put("b", new RecordTypeBuilder.RecordProperty(JSTypeNative.NUMBER_TYPE, null));

    return new RecordType(properties);
  }

  private RecordType createOtherRecordType() {
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("e", new RecordTypeBuilder.RecordProperty(JSTypeNative.STRING_TYPE, null));
    properties.put("f", new RecordTypeBuilder.RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    properties.put("g", new RecordTypeBuilder.RecordProperty(JSTypeNative.BOOLEAN_TYPE, null));

    return new RecordType(properties);
  }

  @Test
  public void testIsEquivalentTo_equivalentType_returnsTrue() {
    RecordType equivalentType = createEquivalentType();

    boolean isEquivalent = equivalentType.isEquivalentTo(recordType);

    Assert.assertTrue(isEquivalent);
  }

  @Test
  public void testIsEquivalentTo_notEquivalentType_returnsFalse() {
    RecordType other = createOtherRecordType();

    boolean isEquivalent = other.isEquivalentTo(recordType);

    Assert.assertFalse(isEquivalent);
  }

  @Test
  public void testIsEquivalentTo_sameType_returnsTrue() {
    boolean isEquivalent = recordType.isEquivalentTo(recordType);

    Assert.assertTrue(isEquivalent);
  }

  private RecordType createEquivalentType() {
    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("a", new RecordTypeBuilder.RecordProperty(JSTypeNative.STRING_TYPE, null));
    properties.put("b", new RecordTypeBuilder.RecordProperty(JSTypeNative.NUMBER_TYPE, null));
    properties.put("c", new RecordTypeBuilder.RecordProperty(JSTypeNative.BOOLEAN_TYPE, null));
    properties.put("d", new RecordTypeBuilder.RecordProperty(JSTypeNative.OBJECT_TYPE, null));

    return new RecordType(properties);
  }

}