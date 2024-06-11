import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

  private RecordType recordType;

  @Before
  public void setUp() {
    recordType = new RecordType();
  }

  @Test
  public void testIsEquivalentTo() {
    RecordType other = new RecordType();
    assertTrue(recordType.isEquivalentTo(other));
  }

  @Test
  public void testGetImplicitPrototype() {
    ObjectType prototype = recordType.getImplicitPrototype();
    assertNotNull(prototype);
  }

  @Test
  public void testDefineProperty() {
    String propertyName = "property";
    JSType type = new JSType();
    Node propertyNode = new Node();
    assertTrue(recordType.defineProperty(propertyName, type, false, propertyNode));
  }

  @Test
  public void testGetLeastSupertype() {
    JSType other = new JSType();
    JSType leastSupertype = recordType.getLeastSupertype(other);
    assertNotNull(leastSupertype);
    assertEquals(JSTypeNative.NO_OBJECT_TYPE, leastSupertype.toString());
  }

  @Test
  public void testGetGreatestSubtypeHelper() {
    JSType other = new JSType();
    JSType greatestSubtype = recordType.getGreatestSubtypeHelper(other);
    assertNotNull(greatestSubtype);
    assertEquals(JSTypeNative.NO_OBJECT_TYPE, greatestSubtype.toString());
  }

  @Test
  public void testIsSubtype() {
    JSType other = new JSType();
    assertFalse(recordType.isSubtype(other));
  }

  @Test
  public void testResolveInternal() {
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<JSType>();
    JSType resolvedType = recordType.resolveInternal(t, scope);
    assertNotNull(resolvedType);
  }

}