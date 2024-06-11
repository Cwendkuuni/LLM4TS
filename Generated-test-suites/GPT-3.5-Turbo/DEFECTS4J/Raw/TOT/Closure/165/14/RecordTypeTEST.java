import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.jstype.RecordType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class RecordTypeTest {

  private RecordType recordType;
  private JSType propertyType;
  private Node propertyNode;

  @Before
  public void setUp() {
    SortedMap<String, RecordTypeBuilder.RecordProperty> properties = new TreeMap<>();
    properties.put("property", new RecordTypeBuilder.RecordProperty(propertyType, propertyNode));
    recordType = new RecordType(null, properties);
  }

  @Test
  public void testDefineProperty() {
    // Typical case
    String propertyName = "testProperty";
    JSType type = new JSType();
    Node propertyNode = new Node();
    boolean inferred = false;
    boolean success = recordType.defineProperty(propertyName, type, inferred, propertyNode);
    assertTrue(success);
    assertEquals(type, recordType.getPropertyType(propertyName));
    assertEquals(propertyNode, recordType.getPropertyNode(propertyName));

    // Error case
    String duplicatedPropertyName = "property";
    success = recordType.defineProperty(duplicatedPropertyName, type, inferred, propertyNode);
    assertFalse(success);
  }

  @Test
  public void testIsEquivalentTo() {
    // Typical case
    RecordType otherRecordType = new RecordType(null, null);
    assertFalse(recordType.isEquivalentTo(otherRecordType));

    // Boundary case
    assertTrue(recordType.isEquivalentTo(recordType));
  }

  @Test
  public void testGetImplicitPrototype() {
    assertNotNull(recordType.getImplicitPrototype());    
  }

  @Test
  public void testIsSubtype() {
    // Typical case
    RecordType subtypeRecordType = new RecordType(null, null);
    assertTrue(recordType.isSubtype(subtypeRecordType));

    // Boundary case
    assertTrue(recordType.isSubtype(recordType));
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    // Typical case with supertype record
    RecordType supertypeRecordType = new RecordType(null, null);
    JSType subtype = recordType.getGreatestSubtypeHelper(supertypeRecordType);
    assertEquals(JSType.NO_TYPE, subtype);

    // Typical case with supertype object type
    JSType supertypeObjectType = new JSType();
    subtype = recordType.getGreatestSubtypeHelper(supertypeObjectType);
    assertNotNull(subtype);

    // Boundary case
    subtype = recordType.getGreatestSubtypeHelper(recordType);
    assertEquals(recordType, subtype.toMaybeRecordType());
  }

  @Test
  public void testResolveInternal() {
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<>();
    JSType resolvedType = recordType.resolveInternal(errorReporter, scope);
    assertNotEquals(recordType, resolvedType);
    assertEquals(recordType.getPropertyType("property"), resolvedType.getPropertyType("property"));
    assertEquals(recordType.getPropertyNode("property"), resolvedType.getPropertyNode("property"));
  }

}