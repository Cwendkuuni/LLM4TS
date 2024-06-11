import org.junit.*;
import static org.junit.Assert.*;

public class NamedTypeTest {

  @Test
  public void testCreateNamedType() {
    NamedType namedType = new NamedType("com.google.javascript.rhino.jstype.JSTypeRegistry", "source", 1, 2);
    assertNotNull(namedType);
    assertEquals("com.google.javascript.rhino.jstype.JSTypeRegistry", namedType.getReferenceName());
    assertEquals("source", namedType.getSourceName());
    assertEquals(1, namedType.getLineno());
    assertEquals(2, namedType.getCharno());
  }

  @Test
  public void testDefineProperty() {
    NamedType namedType = new NamedType("com.google.javascript.rhino.jstype.JSTypeRegistry", "source", 1, 2);
    namedType.defineProperty("propertyName", JSType.STRING, true, null);
    assertTrue(namedType.hasProperty("propertyName"));
    assertEquals(JSType.STRING, namedType.getPropertyType("propertyName"));
  }

  @Test
  public void testResolveInternal() {
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope staticScope = new StaticScope();
    NamedType namedType = new NamedType("com.google.javascript.rhino.jstype.JSTypeRegistry", "source", 1, 2);
    namedType.resolveInternal(errorReporter, staticScope);
    assertEquals(JSType.UNKNOWN_TYPE, namedType.getReferencedType());
    assertEquals(JSType.UNKNOWN_TYPE, namedType.getResolvedTypeInternal());
  }

  @Test
  public void testSetValidator() {
    NamedType namedType = new NamedType("com.google.javascript.rhino.jstype.JSTypeRegistry", "source", 1, 2);
    Predicate<JSType> validator = JSType::isValidType;
    namedType.setValidator(validator);
    assertEquals(validator, namedType.getValidator());
  }

}