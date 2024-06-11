import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.*;

public class NamedTypeTest {

  private NamedType namedType;
  private JSTypeRegistry registry;

  @Before
  public void setUp() {
    registry = Mockito.mock(JSTypeRegistry.class);
    namedType = new NamedType(registry, "Reference", "Source", 1, 0);
  }

  @Test
  public void testDefineProperty() {
    String propertyName = "propertyName";
    JSType type = Mockito.mock(JSType.class);
    boolean inferred = true;
    Node propertyNode = Mockito.mock(Node.class);

    assertTrue(namedType.defineProperty(propertyName, type, inferred, propertyNode));
  }

  @Test
  public void testGetReferencedType() {
    JSType expectedResult = Mockito.mock(JSType.class);
    Mockito.when(registry.getType("Reference")).thenReturn(expectedResult);
    assertEquals(expectedResult, namedType.getReferencedType());
  }

  @Test
  public void testToStringHelper() {
    assertFalse(namedType.toStringHelper(true).isEmpty());
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
    assertFalse(namedType.hashCode() == 0);
  }

  @Test
  public void testResolveInternal() {
    ErrorReporter t = Mockito.mock(ErrorReporter.class);
    StaticScope<JSType> enclosing = Mockito.mock(StaticScope.class);
    assertTrue(namedType.resolveInternal(t, enclosing) instanceof NamedType);
  }

  @Test
  public void testSetValidator() {
    Predicate<JSType> validator = Mockito.mock(Predicate.class);
    assertTrue(namedType.setValidator(validator));
  }

  @Test
  public void testGetReferenceName() {
    assertEquals("Reference", namedType.getReferenceName());
  }

  @Test
  public void testIsResolved() {
    assertTrue(namedType.isResolved());
  }

  @Test
  public void testGetPropertyType() {
    assertNull(namedType.getPropertyType("Property"));
  }
}