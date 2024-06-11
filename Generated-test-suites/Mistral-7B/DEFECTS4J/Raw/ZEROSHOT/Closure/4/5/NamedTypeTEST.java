import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;

import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.PropertyContinuation;
import com.google.javascript.rhino.jstype.PropertyType;
import com.google.javascript.rhino.jstype.StaticSlot;

/**
 * Tests for {@code NamedType} class.
 */
public class NamedTypeTest {

  private static ErrorReporter errorReporter = new ErrorReporter();
  private static JSTypeRegistry typeRegistry = new JSTypeRegistry();
  private static StaticScope<JSType> scope = new StaticScope<JSType>(typeRegistry);

  /**
   * Verifies that a named type is correctly resolved by looking it up in the
   * registry.
   */
  @Test
  public void testNamedTypeResolvedByRegistry() {
    // Set up registry.
    JSType type1 = new ObjectType(typeRegistry, "Type1");
    JSType type2 = new ObjectType(typeRegistry, "Type2");
    JSType type3 = new ObjectType(typeRegistry, "Type3");
    typeRegistry.registerType("Type1", type1);
    typeRegistry.registerType("Type2", type2);
    typeRegistry.registerType("Type3", type3);

    // Create named type.
    NamedType namedType = new NamedType(typeRegistry, "Type1", "", 1, 1);

    // Resolve named type.
    JSType resolvedType = namedType.resolve(errorReporter, scope);

    // Verify resolved type is correct.
    assertEquals(type1, resolvedType);
  }

  /**
   * Verifies that a named type is correctly resolved by looking up its first
   * component in the scope and subsequent components as properties.
   */
  @Test
  public void testNamedTypeResolvedByProperties() {
    // Set up registry and scope.
    JSType type1 = new ObjectType(typeRegistry, "Type1");
    JSType type2 = new ObjectType(typeRegistry, "Type2");
    JSType type3 = new ObjectType(typeRegistry, "Type3");
    typeRegistry.registerType("Type1", type1);
    StaticSlot<JSType> slot1 = new StaticSlot<JSType>(type1, "Type2");
    typeRegistry.registerSlot(slot1, type2);
    StaticSlot<JSType> slot2 = new StaticSlot<JSType>(type2, "Type3");
    typeRegistry.registerSlot(slot2, type3);

    // Create named type.
    NamedType namedType = new NamedType(typeRegistry, "Type1.Type2.Type3", "", 1, 1);

    // Resolve named type.
    JSType resolvedType = namedType.resolve(errorReporter, scope);

    // Verify resolved type is correct.
    assertEquals(type3, resolvedType);
  }

  /**
   * Verifies that a named type with a cycle in its inheritance chain is
   * correctly detected and handled.
   */
  @Test
  public void testNamedTypeWithCycle() {
    // Set up registry and scope.
    JSType cycleType = new ObjectType(typeRegistry, "CycleType");
    typeRegistry.registerType("CycleType", cycleType);

    // Create named type with cycle.
    NamedType namedType = new NamedType(typeRegistry, "CycleType.CycleType", "", 1, 1);

    // Resolve named type.
    namedType.resolve(errorReporter, scope);

    // Verify no warning was issued.
    assertEquals(0, errorReporter.getErrorCount());

    // Verify named type is correctly marked as unknown.
    assertTrue(namedType.isResolved());
    assertEquals(typeRegistry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE),
        namedType.getReferencedType());
    assertEquals(typeRegistry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE),
        namedType.getResolvedType());
  }

  /**
   * Verifies that a named type with an unresolved reference is correctly
   * handled.
   */
  @Test
  public void testNamedTypeWithUnresolvedReference() {
    // Set up registry and scope.
    JSType unresolvedType = new ObjectType(typeRegistry, "UnresolvedType");
    typeRegistry.registerType("UnresolvedType", unresolvedType);

    // Create named type with unresolved reference.
    NamedType namedType = new NamedType(typeRegistry, "UnresolvedType", "", 1, 1);

    // Resolve named type.
    namedType.resolve(errorReporter, scope);

    // Verify warning was issued.
    assertEquals(1, errorReporter.getErrorCount());
    String expectedMessage = "Unknown type UnresolvedType";
    String actualMessage = errorReporter.getErrors().get(0).getMessage();
    assertTrue(actualMessage.contains(expectedMessage));

    // Verify named type is correctly marked as unresolved.
    assertFalse(namedType.isResolved());
    assertNull(namedType.getReferencedType());
    assertNull(namedType.getResolvedType());
  }

  /**
   * Verifies that a named type with a forward-declared reference is correctly
   * handled.
   */
  @Test
  public void testNamedTypeWithForwardDeclaredReference() {
    // Set up registry and scope.
    JSType forwardDeclaredType = new ObjectType(typeRegistry, "ForwardDeclaredType");
    typeRegistry.registerType("ForwardDeclaredType", forwardDeclaredType);
    typeRegistry.registerForwardDeclaredType("ForwardDeclaredType");

    // Create named type with forward-declared reference.
    NamedType namedType = new NamedType(typeRegistry, "ForwardDeclaredType", "", 1, 1);

    // Resolve named type.
    namedType.resolve(errorReporter, scope);

    // Verify no warning was issued.
    assertEquals(0, errorReporter.getErrorCount());

    // Verify named type is correctly marked as resolved.
    assertTrue(namedType.isResolved());
    assertEquals(forwardDeclaredType, namedType.getReferencedType());
    assertEquals(forwardDeclaredType, namedType.getResolvedType());
  }

  /**
   * Verifies that a named type can be correctly used as a property type.
   */
  @Test
  public void testNamedTypeAsPropertyType() {
    // Set up registry and scope.
    JSType namedType = new ObjectType(typeRegistry, "NamedType");
    typeRegistry.registerType("NamedType", namedType);

    // Create named type as property type.
    PropertyType propertyType = new PropertyType(typeRegistry, "property", namedType);

    // Verify property type is correctly marked as resolved.
    assertTrue(propertyType.isResolved());
    assertEquals(namedType, propertyType.getReferencedType());
    assertEquals(namedType, propertyType.getResolvedType());
  }

  /**
   * Verifies that a named type can be correctly used as a property continuation.
   */
  @Test
  public void testNamedTypeAsPropertyContinuation() {
    // Set up registry and scope.
    JSType namedType = new ObjectType(typeRegistry, "NamedType");
    typeRegistry.registerType("NamedType", namedType);

    // Create named type as property continuation.
    NamedType namedTypeContinuation = new NamedType(typeRegistry, "NamedType", "", 1, 1);
    ObjectType parentType = new ObjectType(typeRegistry, "ParentType");
    parentType.defineProperty("property", namedTypeContinuation, false, null);

    // Verify property continuation is correctly marked as resolved.
    assertTrue(namedTypeContinuation.isResolved());
    assertEquals(namedType, namedTypeContinuation.getReferencedType());
    assertEquals(namedType, namedTypeContinuation.getResolvedType());
  }

  /**
   * Verifies that a named type with a resolved referenced type can be used as a
   * property type.
   */
  @Test
  public void testNamedTypeWithResolvedReferencedTypeAsPropertyType() {
    // Set up registry and scope.
    JSType referencedType = new ObjectType(typeRegistry, "ReferencedType");
    typeRegistry.registerType("ReferencedType", referencedType);

    JSType namedType = new NamedType(typeRegistry, "NamedType", "", 1, 1);
    namedType.setValidator(new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input.equals(referencedType);
      }
    });

    // Create property type with named type as referenced type.
    PropertyType propertyType = new PropertyType(typeRegistry, "property", namedType);

    // Verify property type is correctly marked as resolved.
    assertTrue(propertyType.isResolved());
    assertEquals(referencedType, propertyType.getReferencedType());
    assertEquals(referencedType, propertyType.getResolvedType());
  }

  /**
   * Verifies that a named type with an unresolved referenced type cannot be used
   * as a property type.
   */
  @Test
  public void testNamedTypeWithUnresolvedReferencedTypeAsPropertyType() {
    // Set up registry and scope.
    JSType unresolvedType = new ObjectType(typeRegistry, "UnresolvedType");
    typeRegistry.registerType("UnresolvedType", unresolvedType);

    JSType namedType = new NamedType(typeRegistry, "NamedType", "", 1, 1);
    namedType.setValidator(new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input.equals(unresolvedType);
      }
    });

    // Create property type with named type as referenced type.
    try {
      new PropertyType(typeRegistry, "property", namedType);
      fail();
    } catch (AssertionError e) {
      // Expected.
    }
  }
}