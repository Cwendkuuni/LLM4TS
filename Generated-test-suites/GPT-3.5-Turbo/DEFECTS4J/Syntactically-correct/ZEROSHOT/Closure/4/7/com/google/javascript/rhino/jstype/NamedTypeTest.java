import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamedTypeTest {

  NamedType namedType;

  @Before
  public void setUp() {
    namedType = new NamedType(new JSTypeRegistry(), "reference", "sourceName", 1, 1);
  }

  @Test
  public void testGetReferencedType() {
    JSType result = namedType.getReferencedType();
    assertNull(result);
  }

  @Test
  public void testGetReferenceName() {
    String result = namedType.getReferenceName();
    assertEquals("reference", result);
  }

  @Test
  public void testToStringHelper() {
    String result = namedType.toStringHelper(false);
    assertEquals("reference", result);
  }

  @Test
  public void testHasReferenceName() {
    boolean result = namedType.hasReferenceName();
    assertTrue(result);
  }

  @Test
  public void testIsNamedType() {
    boolean result = namedType.isNamedType();
    assertTrue(result);
  }

  @Test
  public void testIsNominalType() {
    boolean result = namedType.isNominalType();
    assertTrue(result);
  }

  @Test
  public void testHashCode() {
    int result = namedType.hashCode();
    assertNotEquals(0, result);
  }

  @Test
  public void testResolveInternal() {
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<JSType>();
    JSType result = namedType.resolveInternal(t, enclosing);
    assertEquals(namedType, result);
  }

  @Test
  public void testSetValidator() {
    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType t) {
        return true;
      }
    };
    boolean result = namedType.setValidator(validator);
    assertTrue(result);
  }

}