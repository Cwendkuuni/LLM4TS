import static org.junit.Assert.*;
import static com.google.common.truth.Truth.*;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.error.ErrorReporter;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.PropertyContinuation;
import com.google.javascript.rhino.jstype.StaticSlot;
import com.google.javascript.rhino.jstype.TypeFactory;
import com.google.javascript.rhino.jstype.TypeRegistry;

import java.util.List;

public class NamedTypeTest {

  private static final ErrorReporter ERROR_REPORTER = new ErrorReporter();

  @org.junit.Test
  public void testNamedType_basic() {
    TypeRegistry registry = new TypeRegistry();
    String reference = "MyType";
    NamedType namedType = new NamedType(registry, reference, "Test", 1, 1);
    JSType resolvedType = namedType.getReferencedType();
    // Basic functionality test
    assertNull(resolvedType);
    assertEquals(NamedType.class, namedType.getClass());
    assertEquals(reference, namedType.getReferenceName());
  }

  @org.junit.Test
  public void testNamedType_resolveViaRegistry() {
    TypeRegistry registry = new TypeRegistry();
    String reference = "MyType";
    JSType myType = TypeFactory.createType(
        registry, "MyType", null, null, JSTypeNative.OBJECT_TYPE);
    registry.registerType(reference, myType);

    NamedType namedType = new NamedType(registry, reference, "Test", 1, 1);
    JSType resolvedType = namedType.getReferencedType();
    // Resolve via registry test
    assertEquals(myType, resolvedType);
  }

  @org.junit.Test
  public void testNamedType_resolveViaProperties() {
    TypeRegistry registry = new TypeRegistry();
    String reference = "MyType.InnerType";

    ObjectType outerType = new ObjectType(registry, "MyType", null, null);
    outerType.setPropertyType("InnerType", new ObjectType(registry, "InnerType", null, null));
    registry.registerType("MyType", outerType);

    NamedType namedType = new NamedType(registry, reference, "Test", 1, 1);
    JSType resolvedType = namedType.getReferencedType();
    // Resolve via properties test
    assertEquals(registry.getType("MyType").getPropertyType("InnerType"), resolvedType);
  }

  @org.junit.Test
  public void testNamedType_isResolved() {
    TypeRegistry registry = new TypeRegistry();
    String reference = "MyType";
    NamedType namedType = new NamedType(registry, reference, "Test", 1, 1);

    assertFalse(namedType.isResolved());

    registry.registerType(reference, new ObjectType(registry, "MyType", null, null));

    namedType.resolveInternal(ERROR_REPORTER, new StaticScope<JSType>(null));

    assertTrue(namedType.isResolved());
  }

  @org.junit.Test
  public void testNamedType_isResolved_cycle_warning() {
    TypeRegistry registry = new TypeRegistry();
    String reference = "A";
    NamedType a = new NamedType(registry, reference, "Test", 1, 1);
    registry.registerType(reference, a);

    String referenceB = "B";
    NamedType b = new NamedType(registry, referenceB, "Test", 1, 1);
    b.setReferencedType(a);

    registry.registerType(referenceB, b);

    // Resolve test with cycle warning
    NamedType resolvedType = (NamedType) a.resolveInternal(ERROR_REPORTER, new StaticScope<JSType>(null));
    assertEquals(a, resolvedType);

    // Check warning
    List<String> warnings = ERROR_REPORTER.getAllWarnings();
    String warningMessage = warnings.get(0);
    assertTrue(warningMessage.contains("Cycle detected in inheritance chain of type " + reference));
  }

  @org.junit.Test
  public void testNamedType_setValidator() {
    TypeRegistry registry = new TypeRegistry();
    String reference = "MyType";
    NamedType namedType = new NamedType(registry, reference, "Test", 1, 1);

    // Validator test
    Predicate<JSType> validator = (type) -> true;
    namedType.setValidator(validator);
    JSType validatedType = namedType.getReferencedType();
    assertTrue(validator.apply(validatedType));
  }

  @org.junit.Test
  public void testNamedType_setValidator_unknownType() {
    TypeRegistry registry = new TypeRegistry();
    String reference = "MyType";
    NamedType namedType = new NamedType(registry, reference, "Test", 1, 1);

    // Unknown type test
    Predicate<JSType> validator = (type) -> type != null;
    namedType.setValidator(validator);
    JSType validatedType = namedType.getReferencedType();
    assertNull(validatedType);
  }

  @org.junit.Test
  public void testNamedType_setValidator_noType() {
    TypeRegistry registry = new TypeRegistry();
    String reference = "MyType";
    NamedType namedType = new NamedType(registry, reference, "Test", 1, 1);

    // No type test
    Predicate<JSType> validator = (type) -> type == null;
    namedType.setValidator(validator);
    JSType validatedType = namedType.getReferencedType();
    assertNull(validatedType);
  }

  @org.junit.Test
  public void testNamedType_defineProperty() {
    TypeRegistry registry = new TypeRegistry();
    String reference = "MyType";
    NamedType namedType = new NamedType(registry, reference, "Test", 1, 1);

    // Define property test
    String propertyName = "p";
    JSType propertyType = new ObjectType(registry, "Object", null, null);
    namedType.defineProperty(propertyName, propertyType, true, null);
    JSType property = namedType.getPropertyType(propertyName);
    assertEquals(propertyType, property);
  }

  @org.junit.Test
  public void testNamedType_propertyContinuation() {
    TypeRegistry registry = new TypeRegistry();
    String reference = "MyType";
    NamedType namedType = new NamedType(registry, reference, "Test", 1, 1);

    // Property continuation test
    String propertyName = "p";
    JSType propertyType = new ObjectType(registry, "Object", null, null);
    PropertyContinuation continuation = new PropertyContinuation(
        propertyName, propertyType, true, null);
    namedType.defineProperty(propertyName, null, true, null);
    namedType.finishPropertyContinuations();
    JSType property = namedType.getPropertyType(propertyName);
    assertEquals(propertyType, property);
  }

  @org.junit.Test
  public void testNamedType_getReferencedObjTypeInternal() {
    TypeRegistry registry = new TypeRegistry();
    String reference = "MyType";
    NamedType namedType = new NamedType(registry, reference, "Test", 1, 1);

    // Get referenced object type test
    JSType referencedObjType = namedType.getReferencedObjTypeInternal();
    assertNull(referencedObjType);
  }

  @org.junit.Test
  public void testNamedType_getReferencedObjTypeInternal_resolved() {
    TypeRegistry registry = new TypeRegistry();
    String reference = "MyType";
    JSType myType = new ObjectType(registry, "MyType", null, null);
    registry.registerType(reference, myType);

    NamedType namedType = new NamedType(registry, reference, "Test", 1, 1);

    // Get referenced object type test - resolved
    JSType referencedObjType = namedType.getReferencedObjTypeInternal();
    assertEquals(myType, referencedObjType);
  }
}