import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RecordTypeTest {

  private RecordType recordType;

  @Before
  public void setUp() {
    Map<String, RecordProperty> properties = new HashMap<>();

    properties.put("name", new RecordProperty("name", StringType.getInstance()));
    properties.put("age", new RecordProperty("age", NumberType.getInstance()));
    properties.put("email", new RecordProperty("email", StringType.getInstance()));

    recordType = new RecordType(properties);
  }

  @Test
  public void testDefineDeclaredProperty() {
    Assert.assertTrue(recordType.defineProperty("address", StringType.getInstance(), true, null));
  }

  @Test
  public void testDefineSynthesizedProperty() {
    Assert.assertTrue(recordType.defineProperty("phone", StringType.getInstance(), false, null));
  }

  @Test
  public void testIsSynthetic() {
    Assert.assertFalse(recordType.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", StringType.getInstance()));
    properties.put("age", new RecordProperty("age", NumberType.getInstance()));
    properties.put("email", new RecordProperty("email", StringType.getInstance()));

    RecordType otherRecordType = new RecordType(properties);

    Assert.assertTrue(recordType.checkRecordEquivalenceHelper(otherRecordType, EquivalenceMethod.EQUALS));
  }

  @Test
  public void testGetImplicitPrototype() {
    ObjectType implicitPrototype = recordType.getImplicitPrototype();

    Assert.assertNotNull(implicitPrototype);
    Assert.assertEquals(implicitPrototype, JSTypeRegistry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
  }

  @Test
  public void testIsSubtype() {
    Assert.assertTrue(recordType.isSubtype(recordType));
  }

  @Test
  public void testIsSubtype_RecordType() {
    Map<String, RecordProperty> properties = new HashMap<>();
    properties.put("name", new RecordProperty("name", StringType.getInstance()));
    properties.put("age", new RecordProperty("age", NumberType.getInstance()));

    RecordType otherRecordType = new RecordType(properties);

    Assert.assertTrue(recordType.isSubtype(otherRecordType));
  }

  @Test
  public void testIsSubtype_ObjectType() {
    Assert.assertTrue(recordType.isSubtype(JSTypeRegistry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(recordType);

    Assert.assertEquals(greatestSubtype, JSTypeRegistry.getNativeType(JSTypeNative.NO_OBJECT_TYPE));
  }

  @Test
  public void testToMaybeRecordType() {
    RecordType maybeRecordType = recordType.toMaybeRecordType();
    
    Assert.assertNotNull(maybeRecordType);
    Assert.assertEquals(maybeRecordType, recordType);
  }

  @Test
  public void testIsInvariant() {
    JSType typeA = JSTypeRegistry.getNativeType(JSTypeNative.NUMBER_TYPE);
    JSType typeB = JSTypeRegistry.getNativeType(JSTypeNative.STRING_TYPE);
    
    boolean isInvariant = typeA.isInvariant(typeB);
    
    Assert.assertFalse(isInvariant);
  }

  @Test
  public void testHasProperty() {
    boolean hasProperty = recordType.hasProperty("name");

    Assert.assertTrue(hasProperty);
  }

  @Test
  public void testGetPropertyType() {
    JSType propertyType = recordType.getPropertyType("name");

    Assert.assertEquals(propertyType, StringType.getInstance());
  }

  @Test
  public void testIsPropertyTypeDeclared() {
    boolean isPropertyTypeDeclared = recordType.isPropertyTypeDeclared("name");

    Assert.assertTrue(isPropertyTypeDeclared);
  }

  @Test
  public void testIsSubtype_static() {
    RecordType typeA = new RecordType();
    RecordType typeB = new RecordType();
    
    boolean isSubtype = RecordType.isSubtype(typeA, typeB);

    Assert.assertTrue(isSubtype);
  }

  @Test
  public void testIsSubtype_static_NotSubtype() {
    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("name", new RecordProperty("name", StringType.getInstance()));
    propertiesA.put("age", new RecordProperty("age", NumberType.getInstance()));

    RecordType typeA = new RecordType(propertiesA);

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("name", new RecordProperty("name", StringType.getInstance()));
    propertiesB.put("age", new RecordProperty("age", NumberType.getInstance()));
    propertiesB.put("email", new RecordProperty("email", StringType.getInstance()));

    RecordType typeB = new RecordType(propertiesB);

    boolean isSubtype = RecordType.isSubtype(typeA, typeB);

    Assert.assertFalse(isSubtype);
  }

  @Test
  public void testIsSubtype_static_MissingProperty() {
    Map<String, RecordProperty> propertiesA = new HashMap<>();
    propertiesA.put("name", new RecordProperty("name", StringType.getInstance()));
    propertiesA.put("age", new RecordProperty("age", NumberType.getInstance()));
    propertiesA.put("email", new RecordProperty("email", StringType.getInstance()));

    RecordType typeA = new RecordType(propertiesA);

    Map<String, RecordProperty> propertiesB = new HashMap<>();
    propertiesB.put("name", new RecordProperty("name", StringType.getInstance()));
    propertiesB.put("age", new RecordProperty("age", NumberType.getInstance()));

    RecordType typeB = new RecordType(propertiesB);

    boolean isSubtype = RecordType.isSubtype(typeA, typeB);

    Assert.assertFalse(isSubtype);
  }
}