import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NamedTypeTest {

  private NamedType namedType;

  @Before
  public void setUp() {
    namedType = new NamedType(null, null, null, 0, 0);
  }

  @After
  public void tearDown() {
    namedType = null;
  }

  @Test
  public void testDefineProperty() {
    String propertyName = "testProperty";
    JSType type = new JSType(null);
    boolean inferred = false;
    Node propertyNode = new Node();

    boolean result = namedType.defineProperty(propertyName, type, inferred, propertyNode);

    assertTrue(result);
  }

  @Test
  public void testGetReferencedType() {
    JSType referencedType = new JSType(null);
    namedType.setReferencedType(referencedType);

    JSType result = namedType.getReferencedType();

    assertEquals(referencedType, result);
  }

  @Test
  public void testGetReferenceName() {
    String referenceName = "testReferenceName";
    namedType.setReferenceName(referenceName);

    String result = namedType.getReferenceName();

    assertEquals(referenceName, result);
  }

  @Test
  public void testToStringHelper() {
    String expectedResult = "testToStringHelper";

    namedType.setStringProperty(expectedResult);

    String result = namedType.toStringHelper(true);

    assertEquals(expectedResult, result);
  }

  @Test
  public void testHasReferenceName() {
    assertTrue(namedType.hasReferenceName());
  }

  @Test
  public void testIsNamedType() {
    assertTrue(namedType.isNamedType());
  }

  @Test
  public void testIsNominalType() {
    assertTrue(namedType.isNominalType());
  }

  @Test
  public void testHashCode() {
    int expectedResult = 12345;
    namedType.setHashCode(expectedResult);

    int result = namedType.hashCode();

    assertEquals(expectedResult, result);
  }

  @Test
  public void testResolveInternal() {
    ErrorReporter t = new ErrorReporter(null, null);
    StaticScope<JSType> enclosing = null;

    JSType result = namedType.resolveInternal(t, enclosing);

    assertNotNull(result);
  }

  @Test
  public void testSetValidator() {
    Predicate<JSType> validator = new Predicate<JSType>() {
      public boolean apply(JSType t) {
        return true;
      }
    };

    boolean result = namedType.setValidator(validator);

    assertTrue(result);
  }
}