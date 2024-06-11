package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.TypeError;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.PropertyContinuation;
import com.google.javascript.rhino.jstype.StaticSlot;

public class NamedTypeTest {

  @Test
  public void testResolveViaRegistry() {
    ErrorReporter errorReporter = new ErrorReporter() {};
    StaticScope<JSType> enclosing = new StaticScope<JSType>() {};

    JSTypeRegistry registry = new JSTypeRegistry(errorReporter);
    registry.registerType("String", String.class);
    registry.registerType("Number", Number.class);

    String reference = "String";
    NamedType namedType = new NamedType(registry, reference, null, 0, 0);

    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosing);

    assertEquals(String.class, resolvedType.getTypeClass());
  }

  @Test
  public void testResolveViaProperties() {
    ErrorReporter errorReporter = new ErrorReporter() {};
    StaticScope<JSType> enclosing = new StaticScope<JSType>() {};

    JSTypeRegistry registry = new JSTypeRegistry(errorReporter);
    registry.registerType("Object", Object.class);
    registry.registerType("String", String.class);
    registry.registerType("Number", Number.class);

    StaticSlot<JSType> slotObject = new StaticSlot<JSType>() {
      @Override
      public JSType getType() {
        return ObjectType.create(registry, null);
      }
    };
    enclosing.putSlot("Object", slotObject);

    String reference = "String";
    NamedType namedType = new NamedType(registry, reference, null, 0, 0);

    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosing);

    assertEquals(String.class, resolvedType.getTypeClass());
  }

  @Test
  public void testHandleUnresolvedType() {
    ErrorReporter errorReporter = new ErrorReporter() {};
    StaticScope<JSType> enclosing = new StaticScope<JSType>() {};

    JSTypeRegistry registry = new JSTypeRegistry(errorReporter);

    String reference = "UnknownType";
    NamedType namedType = new NamedType(registry, reference, null, 0, 0);

    namedType.handleUnresolvedType(errorReporter, false);

    assertEquals(JSTypeNative.NO_RESOLVED_TYPE, namedType.getReferencedType());
  }

  @Test(expected = TypeError.class)
  public void testHandleUnresolvedTypeWithForwardDeclaredType() {
    ErrorReporter errorReporter = new ErrorReporter() {};
    StaticScope<JSType> enclosing = new StaticScope<JSType>() {};

    JSTypeRegistry registry = new JSTypeRegistry(errorReporter);
    registry.registerForwardDeclaration("ForwardDeclaredType", null, null);

    String reference = "ForwardDeclaredType";
    NamedType namedType = new NamedType(registry, reference, null, 0, 0);

    namedType.handleUnresolvedType(errorReporter, false);

    fail("TypeError expected but not thrown");
  }

  @Test
  public void testResolveWithPropertyContinuations() {
    ErrorReporter errorReporter = new ErrorReporter() {};
    StaticScope<JSType> enclosing = new StaticScope<JSType>() {};

    JSTypeRegistry registry = new JSTypeRegistry(errorReporter);

    ObjectType objectType = ObjectType.create(registry, null);
    objectType.defineProperty("name", StringType.getInstance(), true, null);

    String reference = "Object.name";
    NamedType namedType = new NamedType(registry, reference, null, 0, 0);

    namedType.finishPropertyContinuations();

    assertEquals(StringType.getInstance(), namedType.getReferencedType());
  }

  @Test
  public void testSetValidator() {
    ErrorReporter errorReporter = new ErrorReporter() {};
    StaticScope<JSType> enclosing = new StaticScope<JSType>() {};

    JSTypeRegistry registry = new JSTypeRegistry(errorReporter);

    String reference = "UnknownType";
    NamedType namedType = new NamedType(registry, reference, null, 0, 0);

    Predicate<JSType> validator = jstype -> jstype.isNumberType();

    namedType.setValidator(validator);

    JSType resolvedType = namedType.resolveInternal(errorReporter, enclosing);

    assertFalse(validator.apply(resolvedType));
  }

  @Test
  public void testResolveWithPropertyContinuationsAndValidator() {
    ErrorReporter errorReporter = new ErrorReporter() {};
    StaticScope<JSType> enclosing = new StaticScope<JSType>() {};

    JSTypeRegistry registry = new JSTypeRegistry(errorReporter);

    ObjectType objectType = ObjectType.create(registry, null);
    objectType.defineProperty("name", StringType.getInstance(), true, null);

    String reference = "Object.name";
    NamedType namedType = new NamedType(registry, reference, null, 0, 0);

    namedType.setValidator(jstype -> jstype.isStringType());

    namedType.finishPropertyContinuations();

    assertEquals(StringType.getInstance(), namedType.getReferencedType());
  }
}