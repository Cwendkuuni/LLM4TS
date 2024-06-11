import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

  private RecordType recordType;

  @Before
  public void setUp() {
    recordType = new RecordType(new JSTypeRegistry(), new HashMap<String, RecordProperty>());
  }

  @Test
  public void testDefineProperty() {
    String propertyName = "name";
    JSType type = new ObjectType(new JSTypeRegistry(), "TestObjectType");
    boolean inferred = false;
    Node propertyNode = new Node();

    boolean result = recordType.defineProperty(propertyName, type, inferred, propertyNode);

    assertTrue(result);
    assertTrue(recordType.hasProperty(propertyName));
    assertEquals(type, recordType.getPropertyType(propertyName));
    assertEquals(inferred, recordType.isPropertyTypeDeclared(propertyName));
    assertEquals(propertyNode, recordType.getPropertyNode(propertyName));
  }

  @Test
  public void testDefineProperty_WhenFrozen() {
    recordType.setFrozen(true);

    String propertyName = "name";
    JSType type = new ObjectType(new JSTypeRegistry(), "TestObjectType");
    boolean inferred = false;
    Node propertyNode = new Node();

    boolean result = recordType.defineProperty(propertyName, type, inferred, propertyNode);

    assertFalse(result);
    assertFalse(recordType.hasProperty(propertyName));
    assertNull(recordType.getPropertyType(propertyName));
    assertFalse(recordType.isPropertyTypeDeclared(propertyName));
    assertNull(recordType.getPropertyNode(propertyName));
  }

  @Test
  public void testIsSubtype_WhenTypesAreEqual() {
    RecordType other = new RecordType(new JSTypeRegistry(), new HashMap<String, RecordProperty>());

    boolean result = recordType.isSubtype(other);

    assertTrue(result);
  }

  @Test
  public void testIsSubtype_WhenOtherIsObject() {
    ObjectType other = new ObjectType(new JSTypeRegistry(), "TestObjectType");

    boolean result = recordType.isSubtype(other);

    assertTrue(result);
  }

  @Test
  public void testIsSubtype_WhenOtherIsNotRecordType() {
    PrimitiveType other = new PrimitiveType(new JSTypeRegistry(), "string");

    boolean result = recordType.isSubtype(other);

    assertFalse(result);
  }

  @Test
  public void testIsSubtype_WhenSubtypeHasAllProperties() {
    RecordType other = new RecordType(new JSTypeRegistry(), new HashMap<String, RecordProperty>());
    other.defineProperty("name", new PrimitiveType(new JSTypeRegistry(), "string"), false, new Node());

    boolean result = recordType.isSubtype(other);

    assertTrue(result);
  }

  @Test
  public void testIsSubtype_WhenSubtypeDoesNotHaveAllProperties() {
    RecordType other = new RecordType(new JSTypeRegistry(), new HashMap<String, RecordProperty>());
    other.defineProperty("age", new PrimitiveType(new JSTypeRegistry(), "number"), false, new Node());

    boolean result = recordType.isSubtype(other);

    assertFalse(result);
  }

  @Test
  public void testIsSubtype_WhenPropertiesAreEquivalent() {
    RecordType other = new RecordType(new JSTypeRegistry(), new HashMap<String, RecordProperty>());
    recordType.defineProperty("name", new PrimitiveType(new JSTypeRegistry(), "string"), false, new Node());
    other.defineProperty("name", new PrimitiveType(new JSTypeRegistry(), "string"), false, new Node());

    boolean result = recordType.isSubtype(other);

    assertTrue(result);
  }

  @Test
  public void testIsSubtype_WhenPropertiesAreNotEquivalent() {
    RecordType other = new RecordType(new JSTypeRegistry(), new HashMap<String, RecordProperty>());
    recordType.defineProperty("name", new PrimitiveType(new JSTypeRegistry(), "string"), false, new Node());
    other.defineProperty("name", new PrimitiveType(new JSTypeRegistry(), "number"), false, new Node());

    boolean result = recordType.isSubtype(other);

    assertFalse(result);
  }
}