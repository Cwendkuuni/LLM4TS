import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

  private RecordType recordType;

  @Before
  public void setUp() {
    // Create a sample record type with properties
    RecordTypeBuilder.RecordProperty prop1 = new RecordTypeBuilder.RecordProperty();
    prop1.setType(new JSType());
    prop1.setPropertyNode(new Node());

    RecordTypeBuilder.RecordProperty prop2 = new RecordTypeBuilder.RecordProperty();
    prop2.setType(new JSType());
    prop2.setPropertyNode(new Node());

    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("property1", prop1);
    properties.put("property2", prop2);

    recordType = new RecordType(new JSTypeRegistry(), properties);
  }

  @Test
  public void testIsSynthetic() {
    assertFalse(recordType.isSynthetic());
  }

  @Test
  public void testCheckRecordEquivalenceHelper() {
    RecordType otherRecord = createOtherRecord();
    EquivalenceMethod eqMethod = new EquivalenceMethod();

    boolean result = recordType.checkRecordEquivalenceHelper(otherRecord, eqMethod);

    assertTrue(result);
  }

  @Test
  public void testDefineProperty() {
    String propertyName = "newProperty";
    JSType type = new JSType();
    boolean inferred = false;
    Node propertyNode = new Node();

    boolean result = recordType.defineProperty(propertyName, type, inferred, propertyNode);

    assertTrue(result);
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSType that = createThatType();

    JSType result = recordType.getGreatestSubtypeHelper(that);

    assertNotNull(result);
  }

  @Test
  public void testIsSubtype() {
    JSType that = createThatType();

    boolean result = recordType.isSubtype(that);

    assertTrue(result);
  }

  // Helper methods to create test data
  private RecordType createOtherRecord() {
    RecordTypeBuilder.RecordProperty prop1 = new RecordTypeBuilder.RecordProperty();
    prop1.setType(new JSType());
    prop1.setPropertyNode(new Node());

    RecordTypeBuilder.RecordProperty prop2 = new RecordTypeBuilder.RecordProperty();
    prop2.setType(new JSType());
    prop2.setPropertyNode(new Node());

    Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
    properties.put("property1", prop1);
    properties.put("property2", prop2);

    return new RecordType(new JSTypeRegistry(), properties);
  }

  private JSType createThatType() {
    return new JSType();
  }
}