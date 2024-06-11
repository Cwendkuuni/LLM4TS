import org.junit.Test;
import static org.junit.Assert.*;

public class NamedTypeTest {
  
  @Test
  public void testGetReferencedType() {
    NamedType namedType = new NamedType(registry, "typeName", "sourceName", 1, 1);
    JSType referencedType = namedType.getReferencedType();
    assertEquals(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), referencedType);
  }
  
  @Test
  public void testGetReferenceName() {
    NamedType namedType = new NamedType(registry, "typeName", "sourceName", 1, 1);
    String referenceName = namedType.getReferenceName();
    assertEquals("typeName", referenceName);
  }
  
  @Test
  public void testHasReferenceName() {
    NamedType namedType = new NamedType(registry, "typeName", "sourceName", 1, 1);
    boolean hasReferenceName = namedType.hasReferenceName();
    assertTrue(hasReferenceName);
  }
  
  @Test
  public void testIsNamedType() {
    NamedType namedType = new NamedType(registry, "typeName", "sourceName", 1, 1);
    boolean isNamedType = namedType.isNamedType();
    assertTrue(isNamedType);
  }
  
  @Test
  public void testIsNominalType() {
    NamedType namedType = new NamedType(registry, "typeName", "sourceName", 1, 1);
    boolean isNominalType = namedType.isNominalType();
    assertTrue(isNominalType);
  }
  
  @Test
  public void testResolveInternal() {
    NamedType namedType = new NamedType(registry, "typeName", "sourceName", 1, 1);
    JSType resolvedType = namedType.resolveInternal(t, enclosing);
    assertEquals(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), resolvedType);
  }
  
  @Test
  public void testToStringHelper() {
    NamedType namedType = new NamedType(registry, "typeName", "sourceName", 1, 1);
    String toString = namedType.toStringHelper(true);
    assertEquals("typeName", toString);
  }
  
  @Test
  public void testHashCode() {
    NamedType namedType = new NamedType(registry, "typeName", "sourceName", 1, 1);
    int hashCode = namedType.hashCode();
    assertEquals("typeName".hashCode(), hashCode);
  }
  
  @Test
  public void testDefineProperty() {
    NamedType namedType = new NamedType(registry, "typeName", "sourceName", 1, 1);
    boolean defineProperty = namedType.defineProperty("propertyName", type, inferred, propertyNode);
    assertTrue(defineProperty);
  }
  
  @Test
  public void testSetValidator() {
    NamedType namedType = new NamedType(registry, "typeName", "sourceName", 1, 1);
    boolean setValidator = namedType.setValidator(validator);
    assertTrue(setValidator);
  }
  
}