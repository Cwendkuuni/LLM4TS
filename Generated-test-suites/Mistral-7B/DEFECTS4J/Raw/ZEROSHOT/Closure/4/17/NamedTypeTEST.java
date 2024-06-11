import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;

import com.google.common.base.Predicates;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.testing.BaseTest;
import com.google.javascript.rhino.testing.PropertyTester;

import java.util.List;

public class NamedTypeTest extends BaseTest {

  public void testNamedTypeResolutionViaRegistry() {
    ErrorReporter t = new ErrorReporter();
    JSTypeRegistry registry = new JSTypeRegistry();

    String reference = "String";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;

    // Add String type to the registry.
    registry.putType(reference, String.class.getName());

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    JSType resolvedType = namedType.resolve(t, null);
    assertEquals("String", resolvedType.getClass().getName());
  }

  public void testNamedTypeResolutionViaProperties() {
    ErrorReporter t = new ErrorReporter();
    JSTypeRegistry registry = new JSTypeRegistry();

    String reference = "Object.prototype.toString";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;

    // Create an object with a toString property.
    Object obj = new Object() {
      @Override
      public String toString() {
        return "test";
      }
    };

    StaticScope<JSType> enclosing = createStaticScope(null);

    Node node = new Node(Node.IDENT, reference);
    JSType propertyType = registry.getNoType();
    enclosing.putSlot(reference.split("\\.")[0], propertyType);
    PropertyTester.defineProperty(obj, node, String.class, null, null, null, null);

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    JSType resolvedType = namedType.resolve(t, enclosing);
    assertEquals("java.lang.String", resolvedType.getClass().getName());
  }

  public void testNamedTypeResolutionViaPropertiesWithFunctionType() {
    ErrorReporter t = new ErrorReporter();
    JSTypeRegistry registry = new JSTypeRegistry();

    String reference = "Array.prototype.toString";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;

    StaticScope<JSType> enclosing = createStaticScope(null);

    Node node = new Node(Node.IDENT, reference);
    JSType propertyType = registry.getNativeFunctionType(NO_OBJECT_TYPE);
    enclosing.putSlot(reference.split("\\.")[0], propertyType);

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    JSType resolvedType = namedType.resolve(t, enclosing);
    assertEquals("Function", resolvedType.getClass().getSimpleName());
  }

  public void testNamedTypeResolutionViaPropertiesWithEnumType() {
    ErrorReporter t = new ErrorReporter();
    JSTypeRegistry registry = new JSTypeRegistry();

    String reference = "Date.prototype.getMonth";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;

    StaticScope<JSType> enclosing = createStaticScope(null);

    Node node = new Node(Node.IDENT, reference);
    JSType propertyType = registry.getNativeObjectType(UNKNOWN_TYPE);
    EnumType enumType = new EnumType(registry, "Date", null, null);
    enumType.defineProperty(" prototype ", propertyType, null, null);

    enclosing.putSlot(reference.split("\\.")[0], enumType);

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    JSType resolvedType = namedType.resolve(t, enclosing);
    assertEquals("DateMonths", resolvedType.getClass().getSimpleName());
  }

  public void testNamedTypeResolutionWithCycle() {
    ErrorReporter t = new ErrorReporter();
    JSTypeRegistry registry = new JSTypeRegistry();

    String reference = "CycleType";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;

    StaticScope<JSType> enclosing = createStaticScope(null);

    JSType cycleType = new NamedType(registry, reference, sourceName, lineno, charno);
    cycleType.setReferencedType(cycleType);

    try {
      cycleType.resolve(t, enclosing);
      fail("Expected a cycle error.");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  public void testNamedTypeResolutionWithEnumElementCycle() {
    ErrorReporter t = new ErrorReporter();
    JSTypeRegistry registry = new JSTypeRegistry();

    String reference = "EnumElementCycleType.element";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;

    EnumType enumType = new EnumType(registry, "EnumElementCycleType", null, null);
    enumType.defineElement("element", registry.getNativeObjectType(UNKNOWN_TYPE));
    enumType.setReferencedType(enumType);

    StaticScope<JSType> enclosing = createStaticScope(null);

    JSType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    namedType.setReferencedType(enumType);

    try {
      namedType.resolve(t, enclosing);
      fail("Expected a cycle error.");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  public void testNamedTypeWithValidator() {
    ErrorReporter t = new ErrorReporter();
    JSTypeRegistry registry = new JSTypeRegistry();

    String reference = "String";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;

    // Add String type to the registry.
    registry.putType(reference, String.class.getName());

    Predicate<JSType> validator = Predicates.isInstanceOf(String.class);

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    namedType.setValidator(validator);

    JSType resolvedType = namedType.resolve(t, null);
    assertEquals("String", resolvedType.getClass().getName());
    assertTrue(validator.apply(resolvedType));
  }

  public void testNamedTypeWithInvalidValidator() {
    ErrorReporter t = new ErrorReporter();
    JSTypeRegistry registry = new JSTypeRegistry();

    String reference = "String";
    String sourceName = "NamedTypeTest";
    int lineno = 1;
    int charno = 1;

    // Add String type to the registry.
    registry.putType(reference, String.class.getName());

    Predicate<JSType> validator = Predicates.isInstanceOf(Integer.class);

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    namedType.setValidator(validator);

    try {
      namedType.resolve(t, null);
      fail("Expected a type mismatch error.");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  // Helper method to create a static scope with an empty slots list.
  private StaticScope<JSType> createStaticScope(JSType parentType) {
    return new StaticScope<JSType>(parentType, null, null, null, null, null);
  }
}