import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.testing.TypeEqualityTester;
import com.google.javascript.rhino.testing.TypeSubstitutionTester;
import com.google.javascript.rhino.testing.ValueAssert;
import com.google.javascript.rhino.testing.Values;

import static com.google.common.truth.Truth.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class NamedTypeTest {

  private JSTypeRegistry registry;
  private ErrorReporter errorReporter;

  @Before
  public void setUp() {
    registry = new JSTypeRegistry();
    errorReporter = new ErrorReporter() {};
  }

  @Test
  public void testResolvedNamedType() {
    String reference = "String";
    JSType namedType = new NamedType(registry, reference, "NamedTypeTest.java", 1, 1);
    JSType type = registry.getType(reference);
    namedType.setReferencedAndResolvedType(type, errorReporter, null);

    JSType result = namedType.getReferencedType();
    TypeEqualityTester.assertTypesEqual(type, result);
  }

  @Test
  public void testUnresolvedNamedType() {
    String reference = "UnknownType";
    JSType namedType = new NamedType(registry, reference, "NamedTypeTest.java", 1, 1);

    // Unresolved type should still define properties
    namedType.defineProperty("prop", Values.stringType, false, null);
    ValueAssert.isInstanceOf(namedType.getProperty("prop"), JSType.class);

    TypeSubstitutionTester.assertThrows(
        "Unresolved type should not resolve to any value",
        () -> namedType.getReferencedType());
  }

  @Test
  public void testResolveViaProperties() {
    String reference = "Object.prototype.toString";
    JSType namedType = new NamedType(registry, reference, "NamedTypeTest.java", 1, 1);
    JSType type = registry.getType("Object").getPropertyType("prototype");
    type = type.getPropertyType("toString");

    namedType.resolveInternal(errorReporter, null);

    JSType result = namedType.getReferencedType();
    TypeEqualityTester.assertTypesEqual(type, result);
  }

  @Test
  public void testResolveViaRegistry() {
    String reference = "Function";
    JSType namedType = new NamedType(registry, reference, "NamedTypeTest.java", 1, 1);
    JSType type = registry.getType(reference);

    namedType.setReferencedAndResolvedType(type, errorReporter, null);

    JSType result = namedType.getReferencedType();
    TypeEqualityTester.assertTypesEqual(type, result);
  }

  @Test
  public void testResolveViaRegistryNonExistent() {
    String reference = "NonExistentType";
    JSType namedType = new NamedType(registry, reference, "NamedTypeTest.java", 1, 1);

    namedType.resolveInternal(errorReporter, null);

    JSType result = namedType.getReferencedType();
    TypeEqualityTester.assertTypesEqual(
        registry.getNativeObjectType(JSTypeNative.NO_RESOLVED_TYPE), result);
  }

  @Test
  public void testResolveViaRegistryForwardDeclared() {
    registry.registerType("NamedType", registry.getUndefinedType());
    String reference = "NamedType";
    JSType namedType = new NamedType(registry, reference, "NamedTypeTest.java", 1, 1);

    namedType.resolveInternal(errorReporter, null);

    JSType result = namedType.getReferencedType();
    TypeEqualityTester.assertTypesEqual(registry.getType("NamedType"), result);
  }

  @Test
  public void testResolveViaRegistryCircularDependency() {
    // Create circular dependency types
    String type1 = "Type1";
    String type2 = "Type2";

    registry.registerType(type1, registry.getUndefinedType());
    registry.registerType(type2, registry.getUndefinedType());

    registry.addDependency(type1, type2);
    registry.addDependency(type2, type1);

    // Create NamedTypes
    JSType namedType1 = new NamedType(registry, type1, "NamedTypeTest.java", 1, 1);
    JSType namedType2 = new NamedType(registry, type2, "NamedTypeTest.java", 1, 1);

    // Resolve NamedTypes
    namedType1.resolveInternal(errorReporter, null);
    namedType2.resolveInternal(errorReporter, null);

    // Check that types are not resolved to each other
    JSType type1 = namedType1.getReferencedType();
    JSType type2 = namedType2.getReferencedType();

    List<JSType> types1 = JSType.getAllTypes(type1);
    List<JSType> types2 = JSType.getAllTypes(type2);

    // Neither type should contain the other
    assertThat(types1).doesNotContain(type2);
    assertThat(types2).doesNotContain(type1);
  }

  @Test
  public void testResolveViaPropertiesInvalidPropertyAccess() {
    String reference = "Object.prototype.toString.length";
    JSType namedType = new NamedType(registry, reference, "NamedTypeTest.java", 1, 1);

    namedType.resolveInternal(errorReporter, null);

    // Property access on a non-property type should throw an error
    TypeSubstitutionTester.assertThrows(
        "NamedType referencing a non-property type should throw an error",
        () -> namedType.getProperty("length"));
  }

  @Test
  public void testResolveViaPropertiesNonExistent() {
    String reference = "NonExistentProperty";
    JSType namedType = new NamedType(registry, reference, "NamedTypeTest.java", 1, 1);

    namedType.resolveInternal(errorReporter, null);

    JSType result = namedType.getReferencedType();
    TypeEqualityTester.assertTypesEqual(
        registry.getNativeObjectType(JSTypeNative.NO_RESOLVED_TYPE), result);
  }
}