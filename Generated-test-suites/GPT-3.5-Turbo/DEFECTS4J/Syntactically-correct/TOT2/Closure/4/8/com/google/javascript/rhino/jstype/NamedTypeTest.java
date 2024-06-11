import static org.junit.Assert.*;
import org.junit.Test;

public class NamedTypeTest {

  @Test
  public void testNamedTypeCreation() {
    NamedType namedType = new NamedType(new JSTypeRegistry(), "Test", "TestSource", 1, 1);
    
    assertNotNull(namedType);
  }

  @Test
  public void testDefineProperty() {
    NamedType namedType = new NamedType(new JSTypeRegistry(), "Test", "TestSource", 1, 1);
    boolean result = namedType.defineProperty("property", new JSType(), false, new Node());
    
    assertTrue(result);
  }

  @Test
  public void testGetReferencedType() {
    NamedType namedType = new NamedType(new JSTypeRegistry(), "Test", "TestSource", 1, 1);
    
    assertEquals(namedType.getReferencedType(), namedType.getReferencedTypeInternal());
  }

  @Test
  public void testResolveInternal() {
    NamedType namedType = new NamedType(new JSTypeRegistry(), "Test", "TestSource", 1, 1);
    namedType.resolveInternal(new ErrorReporter(), new StaticScope<JSType>());
    
    assertTrue(namedType.isResolved());
  }

  @Test
  public void testSetValidator() {
    NamedType namedType = new NamedType(new JSTypeRegistry(), "Test", "TestSource", 1, 1);
    Predicate<JSType> validator = type -> true;
    boolean result = namedType.setValidator(validator);
    
    assertTrue(result);
  }
}