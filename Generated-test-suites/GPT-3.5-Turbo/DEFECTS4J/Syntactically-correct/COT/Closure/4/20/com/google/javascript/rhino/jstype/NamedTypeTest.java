import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.rhino.ErrorReporter;

public class NamedTypeTest {

  private NamedType namedType;

  @Before
  public void setUp() {
    namedType = new NamedType(new JSTypeRegistry(), "com.google.javascript.rhino.jstype.NamedType", "source", 0, 0);
  }

  @Test
  public void testGetReferencedType() {
    JSType referencedType = namedType.getReferencedType();
    assertNotNull(referencedType);
    assertTrue(referencedType.isObjectType());
  }

  @Test
  public void testToStringHelper() {
    String toString = namedType.toStringHelper(true);
    assertNotNull(toString);
  }

  @Test
  public void testHasReferenceName() {
    boolean hasReferenceName = namedType.hasReferenceName();
    assertTrue(hasReferenceName);
  }

  @Test
  public void testIsNamedType() {
    boolean isNamedType = namedType.isNamedType();
    assertTrue(isNamedType);
  }

  @Test
  public void testIsNominalType() {
    boolean isNominalType = namedType.isNominalType();
    assertTrue(isNominalType);
  }

  @Test
  public void testResolveInternal() {
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<JSType>();

    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosing);
    assertNotNull(resolvedType);
    assertTrue(resolvedType.isObjectType());
  }

  @Test
  public void testSetValidator() {
    Predicate<JSType> validator = type -> !type.isUnknownType();
    boolean isSet = namedType.setValidator(validator);
    assertTrue(isSet);
  }
}