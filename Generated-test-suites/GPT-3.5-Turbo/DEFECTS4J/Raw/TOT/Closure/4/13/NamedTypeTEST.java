import static org.junit.Assert.*;
import org.junit.Test;
import com.google.common.collect.Iterables;

public class NamedTypeTest {

  @Test
  public void testResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter();

    NamedType namedType1 = new NamedType(registry, "com.google.javascript.rhino.jstype.NamedType", "source1", 1, 1);
    assertFalse(namedType1.isResolved());
    namedType1.resolveInternal(errorReporter, null);
    assertTrue(namedType1.isResolved());
    
    NamedType namedType2 = new NamedType(registry, "com.google.javascript.rhino.jstype.NamedType", "source2", 2, 2);
    assertFalse(namedType2.isResolved());
    namedType2.resolveInternal(errorReporter, null);
    assertTrue(namedType2.isResolved());
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter();

    NamedType namedType = new NamedType(registry, "com.google.javascript.rhino.jstype.NamedType", "source", 1, 1);
    assertFalse(namedType.defineProperty("propertyName", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null));
    assertFalse(namedType.isResolved());

    namedType.resolveInternal(errorReporter, null);
    assertTrue(namedType.defineProperty("propertyName", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null));
    assertTrue(namedType.isResolved());
  }

  @Test
  public void testGetReferencedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter();

    NamedType namedType = new NamedType(registry, "com.google.javascript.rhino.jstype.NamedType", "source", 1, 1);
    namedType.resolveInternal(errorReporter, null);
    assertEquals(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), namedType.getReferencedType());
  }

  @Test
  public void testGetReferenceName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter();

    NamedType namedType = new NamedType(registry, "com.google.javascript.rhino.jstype.NamedType", "source", 1, 1);
    assertEquals("com.google.javascript.rhino.jstype.NamedType", namedType.getReferenceName());
  }

  @Test
  public void testIsNominalType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter();

    NamedType namedType = new NamedType(registry, "com.google.javascript.rhino.jstype.NamedType", "source", 1, 1);
    assertTrue(namedType.isNominalType());
  }

  @Test
  public void testHasReferenceName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter();

    NamedType namedType = new NamedType(registry, "com.google.javascript.rhino.jstype.NamedType", "source", 1, 1);
    assertTrue(namedType.hasReferenceName());
  }

  @Test
  public void testIsNamedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter();

    NamedType namedType = new NamedType(registry, "com.google.javascript.rhino.jstype.NamedType", "source", 1, 1);
    assertTrue(namedType.isNamedType());
  }

}