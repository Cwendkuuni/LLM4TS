package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class NamedTypeTest {

  @Test
  public void testResolveInternal() {
    // Test resolving a named type via registry
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "String";
    NamedType namedType = new NamedType(registry, reference, "Test", 1, 1);
    JSType resolvedType = namedType.resolveInternal(null, null);
    assertNotNull(resolvedType);
    assertEquals(JSType.STRING, resolvedType);

    // Test resolving a non-existent named type via registry
    reference = "NonExistentType";
    namedType = new NamedType(registry, reference, "Test", 1, 1);
    assertNull(namedType.getReferencedType());
    assertNull(namedType.resolveInternal(null, null));

    // Test resolving a named type via properties
    registry = new JSTypeRegistry();
    ObjectType objectType = new ObjectType(registry, "MyObject");
    Property property = new Property("prop1", JSType.NUMBER, null, null, null);
    objectType.defineProperty("prop1", JSType.NUMBER, false, null);

    reference = "MyObject.prop1";
    namedType = new NamedType(registry, reference, "Test", 1, 1);
    resolvedType = namedType.resolveInternal(null, null);
    assertNotNull(resolvedType);
    assertEquals(JSType.NUMBER, resolvedType);

    // Test resolving a non-existent named type via properties
    reference = "NonExistentObject.nonExistentProperty";
    namedType = new NamedType(registry, reference, "Test", 1, 1);
    assertNull(namedType.getReferencedType());
    assertNull(namedType.resolveInternal(null, null));

    // Test handling unresolved types
    registry = new JSTypeRegistry();
    reference = "UnknownType";
    namedType = new NamedType(registry, reference, "Test", 1, 1);
    assertNull(namedType.getReferencedType());
    assertNull(namedType.resolveInternal(null, null));

    registry.setLastGeneration(false);
    namedType = new NamedType(registry, reference, "Test", 1, 1);
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> enclosing = null;
    namedType.resolveInternal(t, enclosing);
    assertNotNull(namedType.getReferencedType());
    assertEquals(JSType.UNKNOWN_TYPE, namedType.getReferencedType());
  }

  @Test
  public void testSetValidator() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "NamedType";
    NamedType namedType = new NamedType(registry, reference, "Test", 1, 1);

    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return JSType.STRING.equals(input);
      }
    };

    namedType.setValidator(validator);

    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> enclosing = null;
    namedType.resolveInternal(t, enclosing);

    JSType referencedType = namedType.getReferencedType();
    if (validator != null) {
      validator.apply(referencedType);
    }

    assertEquals(JSType.STRING, referencedType);
  }
}