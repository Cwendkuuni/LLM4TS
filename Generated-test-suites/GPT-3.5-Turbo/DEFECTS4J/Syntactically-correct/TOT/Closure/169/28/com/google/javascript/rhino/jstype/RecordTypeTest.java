import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {

  private Map<String, RecordProperty> properties;

  @Before
  public void setUp() {
    properties = new HashMap<>();
  }

  @Test
  public void testRecordTypeConstructor() {
    properties.put("a", new RecordProperty(TYPE_1, propertyNode));
    properties.put("b", new RecordProperty(TYPE_2, propertyNode));

    RecordType record = new RecordType(registry, properties);

    assertNotNull(record);
    assertTrue(record.isSynthetic());
    assertTrue(record.isFrozen());
    assertEquals(2, record.getProperties().size());
    assertEquals(TYPE_1, record.getProperties().get("a"));
    assertEquals(TYPE_2, record.getProperties().get("b"));
  }

  @Test
  public void testRecordTypeConstructorWithDeclaredType() {
    properties.put("a", new RecordProperty(TYPE_1, propertyNode));

    RecordType record = new RecordType(registry, properties, true);

    assertNotNull(record);
    assertFalse(record.isSynthetic());
    assertTrue(record.isFrozen());
    assertEquals(1, record.getProperties().size());
    assertEquals(TYPE_1, record.getProperties().get("a"));
  }

  @Test
  public void testRecordTypeConstructorWithNullProperty() {
    properties.put("a", null);

    try {
      RecordType record = new RecordType(registry, properties);
      fail("IllegalArgumentException not thrown.");
    } catch (IllegalArgumentException e) {
      assertEquals("RecordProperty associated with a property should not be null!", e.getMessage());
    }
  }

  @Test
  public void testIsSynthetic() {
    RecordType synthRecord = new RecordType(registry, properties, true);
    RecordType nonSynthRecord = new RecordType(registry, properties, false);

    assertTrue(synthRecord.isSynthetic());
    assertFalse(nonSynthRecord.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    properties.put("a", new RecordProperty(TYPE_1, propertyNode));
    properties.put("b", new RecordProperty(TYPE_2, propertyNode));

    RecordType record1 = new RecordType(registry, properties);
    RecordType record2 = new RecordType(registry, properties);

    assertTrue(record1.checkRecordEquivalenceHelper(record2, true));
  }

  @Test
  public void testGetImplicitPrototype() {
    RecordType record = new RecordType(registry, properties);

    assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), record.getImplicitPrototype());
  }

  @Test
  public void testDefinePropertyOnNotFrozenRecord() {
    properties.put("a", new RecordProperty(TYPE_1, propertyNode));

    RecordType record = new RecordType(registry, properties);
    JSType newType = new JSType();

    assertTrue(record.defineProperty("b", newType, true, propertyNode));
    assertEquals(newType, record.getProperties().get("b"));
  }

  @Test
  public void testDefinePropertyOnFrozenRecord() {
    properties.put("a", new RecordProperty(TYPE_1, propertyNode));

    RecordType record = new RecordType(registry, properties);
    record.setFrozen(true);
    JSType newType = new JSType();

    assertFalse(record.defineProperty("b", newType, true, propertyNode));
    assertNull(record.getProperties().get("b"));
  }

  @Test
  public void testGetGreatestSubtypeHelperWithRecordType() {
    properties.put("a", new RecordProperty(TYPE_1, propertyNode));
    properties.put("b", new RecordProperty(TYPE_2, propertyNode));

    RecordType record1 = new RecordType(registry, properties);
    RecordType record2 = new RecordType(registry, properties);

    AbstractCompiler compiler = new AbstractCompiler();
    JSTypeRegistry jsTypeRegistry = new JSTypeRegistry(compiler);

    JSType greatestSubtype = record1.getGreatestSubtypeHelper(record2);

    assertEquals(jsTypeRegistry.getNativeObjectType(JSTypeNative.NO_TYPE), greatestSubtype);
  }

  @Test
  public void testGetGreatestSubtypeHelperWithObjectType() {
    properties.put("a", new RecordProperty(TYPE_1, propertyNode));
    properties.put("b", new RecordProperty(TYPE_2, propertyNode));
    ObjectType objectType = new ObjectType(registry, null, null);

    RecordType record = new RecordType(registry, properties);
    
    JSTypeRegistry jsTypeRegistry = new JSTypeRegistry(new AbstractCompiler());
    JSType greatestSubtype = record.getGreatestSubtypeHelper(objectType);
    JSType expectedSubtype = jsTypeRegistry.getNativeType(JSTypeNative.NO_OBJECT_TYPE);
    
    assertEquals(expectedSubtype, greatestSubtype);
  }

  @Test
  public void testIsSubtypeWithDifferentTypes() {
    properties.put("a", new RecordProperty(TYPE_1, propertyNode));
    RecordType record1 = new RecordType(registry, properties);
    RecordType record2 = new RecordType(registry, properties);

    assertTrue(record1.isSubtype(record2));
  }

  @Test
  public void testIsSubtypeWithDifferentPropertyTypes() {
    properties.put("a", new RecordProperty(TYPE_1, propertyNode));
    properties.put("b", new RecordProperty(TYPE_2, propertyNode));

    RecordType record1 = new RecordType(registry, properties);
    properties.replace("b", new RecordProperty(TYPE_3, propertyNode));
    RecordType record2 = new RecordType(registry, properties);

    assertFalse(record1.isSubtype(record2));
  }

  @Test
  public void testIsSubtypeWithSuperType() {
    properties.put("a", new RecordProperty(TYPE_1, propertyNode));
    RecordType record = new RecordType(registry, properties);
    ObjectType objectType = new ObjectType(registry, null, null);

    assertTrue(record.isSubtype(objectType));
  }

  @Test
  public void testResolveInternal() {
    properties.put("a", new RecordProperty(TYPE_1, propertyNode));
    RecordType record = new RecordType(registry, properties);

    ErrorReporter errorReporter = new ErrorReporterImpl();
    StaticScope<JSType> scope = new StaticScope<>();

    JSType resolvedType = record.resolveInternal(errorReporter, scope);

    assertEquals(properties.get("a").getType(), resolvedType.getProperties().get("a"));
    assertEquals(properties.get("a").getType(), record.getProperties().get("a"));
    assertSame(properties.get("a").getpropertyNode(), resolvedType.getProperties().getpropertyNode("a"));
    assertSame(properties.get("a").getpropertyNode(), record.getProperties().getpropertyNode("a"));
  }

}