import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StylizedError;
import com.google.javascript.rhino.testing.PolymerTestRunner;
import com.google.javascript.rhino.testing.TopLevelTest;
import com.google.javascript.rhino.testing.type.JSTypeRegistry;

/**
 * Tests for {@code NamedType} class.
 */
public class NamedTypeTest extends TopLevelTest {

  /** Test NamedType construction. */
  @Test
  public void testNamedTypeConstruction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "String", "Test", 1, 1);
    assertEquals("String", namedType.getReferenceName());
    assertEquals(JSTypeNative.STRING_TYPE, namedType.getReferencedType());
  }

  /** Test a NamedType with a non-existent referenced type. */
  @Test
  public void testNamedTypeNoReferencedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "UnknownType", "Test", 1, 1);
    assertNull(namedType.getReferencedType());
  }

  /** Test defineProperty continuation. */
  @Test
  public void testNamedTypeDefinePropertyContinuation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "TestType", "Test", 1, 1);

    // Define a property continuation.
    PropertyContinuation continuation = new PropertyContinuation(
        "name", JSTypeNative.STRING_TYPE, false, null);
    namedType.defineProperty("name", null, true, null);
    namedType.finishPropertyContinuations();

    // Verify the property was defined.
    assertEquals(JSTypeNative.STRING_TYPE, namedType.getPropertyType("name"));
  }

  /** Test handling of unresolved types. */
  @Test
  public void testNamedTypeUnresolvedTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "UnknownType", "Test", 1, 1);

    // Check that an unresolved type returns an unknown type.
    assertSame(JSTypeNative.NO_RESOLVED_TYPE, namedType.getReferencedType());

    // Check that an unresolved type can be validated.
    Predicate<JSType> validator = type -> assertTrue(type.isUnknownType());
    namedType.setValidator(validator);
    assertTrue(validator.apply(namedType.getReferencedType()));
  }

  /** Test type resolution via registry. */
  @Test
  public void testNamedTypeResolutionViaRegistry() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.register("String", JSTypeNative.STRING_TYPE);
    registry.register("Number", JSTypeNative.NUMBER_TYPE);
    NamedType namedType = new NamedType(registry, "String", "Test", 1, 1);

    // Check that the type is resolved correctly via registry.
    assertEquals(JSTypeNative.STRING_TYPE, namedType.getReferencedType());
  }

  /** Test type resolution via properties. */
  @Test
  public void testNamedTypeResolutionViaProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.register("Object", JSTypeNative.OBJECT_TYPE);

    StaticScope<JSType> enclosing = new StaticScope<>();
    enclosing.define("MyObject", registry.getType("Object"));

    NamedType namedType = new NamedType(registry, "MyObject.Property",
        "Test", 1, 1);

    // Check that the type is resolved correctly via properties.
    assertEquals(registry.getNativeObjectType("Object").getPropertyType("Property"),
        namedType.getReferencedType());
  }

  /** Test handling of unresolved types via properties. */
  @Test
  public void testNamedTypeUnresolvedTypesViaProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.register("Object", JSTypeNative.OBJECT_TYPE);

    StaticScope<JSType> enclosing = new StaticScope<>();
    enclosing.define("MyObject", JSTypeNative.NO_RESOLVED_TYPE);

    NamedType namedType = new NamedType(registry, "MyObject.Property",
        "Test", 1, 1);

    // Check that an unresolved type returns an unknown type.
    assertSame(JSTypeNative.NO_RESOLVED_TYPE, namedType.getReferencedType());

    // Check that an unresolved type can be validated.
    Predicate<JSType> validator = type -> assertTrue(type.isUnknownType());
    namedType.setValidator(validator);
    assertTrue(validator.apply(namedType.getReferencedType()));
  }

  /** Test handling of unresolved types in a cycle. */
  @Test(expected = StylizedError.class)
  public void testNamedTypeUnresolvedTypeCycle() {
    JSTypeRegistry registry = new JSTypeRegistry();

    registry.register("Object", new ObjectType(registry, null, new String[]{"name"},
        new Node[]{null},
        null, null, 1, 0, null));

    NamedType namedType = new NamedType(registry, "Object.name", "Test", 1, 1);

    // Set a type validator to check for the cycle.
    Predicate<JSType> validator = type -> {
      if (type instanceof NamedType) {
        NamedType namedType = (NamedType) type;
        if (namedType.getReferenceName().equals("Object.name")) {
          fail("Detected a cycle in inheritance chain of type Object.name");
        }
      }
      return true;
    };
    namedType.setValidator(validator);

    // Trigger the cycle by setting the referenced type to the same NamedType.
    namedType.setReferencedType(namedType);
  }

  /** Test handling of enum element cycles. */
  @Test(expected = StylizedError.class)
  public void testNamedTypeEnumElementCycle() {
    JSTypeRegistry registry = new JSTypeRegistry();

    registry.register("MyEnum", new EnumType(
        registry, new String[]{"RED", "GREEN", "BLUE"}, null, 1, 1, null));

    NamedType namedType = new NamedType(registry, "MyEnum.RED", "Test", 1, 1);

    // Set a type validator to check for the cycle.
    Predicate<JSType> validator = type -> {
      if (type instanceof EnumElementType &&
          ((EnumElementType) type).getPrimitiveType() == namedType) {
        fail("Detected a cycle in inheritance chain of type " +
            namedType.getReferenceName());
      }
      return true;
    };
    namedType.setValidator(validator);

    // Trigger the cycle by setting the referenced type to the same NamedType.
    namedType.setReferencedType(namedType);
  }
}