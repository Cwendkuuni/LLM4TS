import org.junit.Test;
import java.util.HashMap;
import static org.junit.Assert.*;

public class RecordTypeTest {

  @Test
  public void defineProperty_shouldAddProperty_whenPassedValidParameters() {
    HashMap<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordTypeBuilder.RecordProperty(Type.OBJECT, null));
    properties.put("property2", new RecordTypeBuilder.RecordProperty(Type.STRING, null));
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, properties);
    
    assertTrue(recordType.defineProperty("property3", Type.BOOLEAN, true, null));
    assertTrue(recordType.defineProperty("property4", Type.NUMBER, false, null));
  }

  @Test
  public void defineProperty_shouldNotAddProperty_whenTypeIsUnknown() {
    HashMap<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordTypeBuilder.RecordProperty(Type.OBJECT, null));
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, properties);
    
    assertFalse(recordType.defineProperty("property2", Type.UNKNOWN, false, null));
  }

  @Test
  public void defineProperty_shouldNotAddProperty_whenRecordTypeIsFrozen() {
    HashMap<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordTypeBuilder.RecordProperty(Type.OBJECT, null));
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, properties);
    recordType.setFrozen(true);
    
    assertFalse(recordType.defineProperty("property2", Type.STRING, false, null));
  }

  @Test
  public void isEquivalentTo_shouldReturnTrue_whenComparingSameRecordType() {
    HashMap<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordTypeBuilder.RecordProperty(Type.OBJECT, null));
    properties.put("property2", new RecordTypeBuilder.RecordProperty(Type.STRING, null));
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType1 = new RecordType(registry, properties);
    RecordType recordType2 = new RecordType(registry, properties);
    
    assertTrue(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void isEquivalentTo_shouldReturnFalse_whenComparingDifferentRecordTypes() {
    HashMap<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
    properties1.put("property1", new RecordTypeBuilder.RecordProperty(Type.OBJECT, null));
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType1 = new RecordType(registry, properties1);

    HashMap<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
    properties2.put("property2", new RecordTypeBuilder.RecordProperty(Type.STRING, null));
    RecordType recordType2 = new RecordType(registry, properties2);
    
    assertFalse(recordType1.isEquivalentTo(recordType2));
  }

  @Test
  public void getLeastSupertype_shouldReturnExpectedResult_whenPassedRecordTypeWithMatchingProperties() {
    HashMap<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
    properties1.put("property1", new RecordTypeBuilder.RecordProperty(Type.OBJECT, null));
    
    HashMap<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
    properties2.put("property1", new RecordTypeBuilder.RecordProperty(Type.OBJECT, null));
    
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType1 = new RecordType(registry, properties1);
    RecordType recordType2 = new RecordType(registry, properties2);
    
    assertTrue(recordType1.getLeastSupertype(recordType2).isEquivalentTo(recordType1));
    assertTrue(recordType2.getLeastSupertype(recordType1).isEquivalentTo(recordType2));
  }

  @Test
  public void getLeastSupertype_shouldReturnEmptyObjectType_whenPassedNonRecordType() {
    HashMap<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordTypeBuilder.RecordProperty(Type.OBJECT, null));
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, properties);
    ObjectType objectType = new ObjectType(registry, null, null);
    
    assertTrue(recordType.getLeastSupertype(objectType).isEquivalentTo(objectType));
  }

  @Test
  public void isSubtype_shouldReturnTrue_whenCheckingIfRecordTypeIsASubtypeOfItself() {
    HashMap<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordTypeBuilder.RecordProperty(Type.OBJECT, null));
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, properties);
    
    assertTrue(recordType.isSubtype(recordType));
  }

  @Test
  public void isSubtype_shouldReturnFalse_whenCheckingSubtypeRelationBetweenRecordTypes() {
    HashMap<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
    properties1.put("property1", new RecordTypeBuilder.RecordProperty(Type.OBJECT, null));
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType1 = new RecordType(registry, properties1);

    HashMap<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
    properties2.put("property2", new RecordTypeBuilder.RecordProperty(Type.STRING, null));
    RecordType recordType2 = new RecordType(registry, properties2);
    
    assertFalse(recordType1.isSubtype(recordType2));
  }

  @Test
  public void isSubtype_shouldReturnTrue_whenCheckingSubtypeRelationWithObjectType() {
    HashMap<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordTypeBuilder.RecordProperty(Type.OBJECT, null));
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, properties);
    ObjectType objectType = new ObjectType(registry, null, null);
    
    assertTrue(recordType.isSubtype(objectType));
  }

  @Test
  public void resolveInternal_shouldReplacePropertiesWithResolvedTypes() {
    HashMap<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("property1", new RecordTypeBuilder.RecordProperty(Type.OBJECT, null));
    JSTypeRegistry registry = new JSTypeRegistry();
    RecordType recordType = new RecordType(registry, properties);
    
    StaticScope<JSType> scope = new StaticScope<>();
    scope.addVariable("property1", Type.STRING);
    JSType resolvedType = recordType.resolveInternal(null, scope);
    
    assertEquals(Type.STRING, recordType.getPropertyType("property1"));
    assertEquals(Type.STRING, resolvedType.getPropertyType("property1"));
  }
}