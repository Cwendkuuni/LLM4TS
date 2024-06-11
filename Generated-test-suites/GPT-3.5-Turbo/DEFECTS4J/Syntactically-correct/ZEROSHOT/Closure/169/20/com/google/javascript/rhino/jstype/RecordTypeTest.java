import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

  @Test
  public void testConstructorDeclared() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<String, RecordProperty>();
    properties.put("a", new RecordProperty(TYPE_1, propertyNode));
    RecordType recordType = new RecordType(registry, properties, true);
    assertNotNull(recordType);
  }

  @Test
  public void testConstructorSynthesized() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<String, RecordProperty>();
    properties.put("a", new RecordProperty(TYPE_1, propertyNode));
    RecordType recordType = new RecordType(registry, properties, false);
    assertNotNull(recordType);
  }

  @Test(expected = IllegalStateException.class)
  public void testConstructorNullProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<String, RecordProperty>();
    properties.put("a", null);
    RecordType recordType = new RecordType(registry, properties, true);
  }

  @Test
  public void testGetPropertyType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<String, RecordProperty>();
    properties.put("a", new RecordProperty(TYPE_1, propertyNode));
    RecordType recordType = new RecordType(registry, properties, true);
    assertEquals(TYPE_1, recordType.getPropertyType("a"));
  }

  @Test
  public void testHasProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<String, RecordProperty>();
    properties.put("a", new RecordProperty(TYPE_1, propertyNode));
    RecordType recordType = new RecordType(registry, properties, true);
    assertTrue(recordType.hasProperty("a"));
    assertFalse(recordType.hasProperty("b"));
  }

  @Test
  public void testIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new HashMap<String, RecordProperty>();
    properties1.put("a", new RecordProperty(TYPE_1, propertyNode));
    RecordType recordType1 = new RecordType(registry, properties1, true);

    Map<String, RecordProperty> properties2 = new HashMap<String, RecordProperty>();
    properties2.put("a", new RecordProperty(TYPE_1, propertyNode));
    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertTrue(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testIsNotSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties1 = new HashMap<String, RecordProperty>();
    properties1.put("a", new RecordProperty(TYPE_1, propertyNode));
    RecordType recordType1 = new RecordType(registry, properties1, true);

    Map<String, RecordProperty> properties2 = new HashMap<String, RecordProperty>();
    properties2.put("b", new RecordProperty(TYPE_1, propertyNode));
    RecordType recordType2 = new RecordType(registry, properties2, true);

    assertFalse(recordType1.isSubtype(recordType2));
  }

  @Test
  public void testResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<String, RecordProperty>();
    properties.put("a", new RecordProperty(TYPE_1, propertyNode));
    RecordType recordType = new RecordType(registry, properties, true);
    recordType.resolveInternal(t, scope);
  }

  @Test
  public void testIsSynthetic() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<String, RecordProperty>();
    properties.put("a", new RecordProperty(TYPE_1, propertyNode));
    RecordType recordType = new RecordType(registry, properties, false);
    assertTrue(recordType.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<String, RecordProperty>();
    properties.put("a", new RecordProperty(TYPE_1, propertyNode));
    RecordType recordType1 = new RecordType(registry, properties, true);
    
    properties.put("b", new RecordProperty(TYPE_2, propertyNode));
    RecordType recordType2 = new RecordType(registry, properties, true);

    assertFalse(recordType1.checkRecordEquivalenceHelper(recordType2, false));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Map<String, RecordProperty> properties = new HashMap<String, RecordProperty>();
    properties.put("a", new RecordProperty(TYPE_1, propertyNode));
    properties.put("b", new RecordProperty(TYPE_2, propertyNode));
    properties.put("c", new RecordProperty(TYPE_1, propertyNode));
    RecordType recordType1 = new RecordType(registry, properties, true);
    
    properties.put("d", new RecordProperty(TYPE_2, propertyNode));
    properties.put("e", new RecordProperty(TYPE_1, propertyNode));
    properties.put("f", new RecordProperty(TYPE_2, propertyNode));
    RecordType recordType2 = new RecordType(registry, properties, true);
    
    JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
    assertNotNull(greatestSubtype);
  }

}