package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.StaticSlot;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class NamedTypeTest {

  static final String SOURCE_NAME = "NamedTypeTest";
  static final int LINE_NUMBER = 1;
  static final int CHAR_NUMBER = 1;

  private ErrorReporter errorReporter;
  private StaticScope<JSType> enclosingScope;
  private NamedType namedType;

  @Before
  public void setUp() {
    errorReporter = new ErrorReporter() {};
    enclosingScope = new StaticScope<JSType>() {};
  }

  @Test
  public void testNewInstance() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "String";
    namedType = new NamedType(registry, reference, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    JSType expectedType = registry.getType(reference);
    assertTrue(namedType.isResolved());
    assertSame(expectedType, namedType.getReferencedType());
  }

  @Test
  public void testUnresolvedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "UnknownType";
    namedType = new NamedType(registry, reference, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    assertFalse(namedType.isResolved());
    assertNull(namedType.getReferencedType());
  }

  @Test
  public void testResolveViaRegistry() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "Object";
    namedType = new NamedType(registry, reference, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    JSType expectedType = registry.getType(reference);
    registry.setType(reference, new ObjectType(registry, "Object"));
    namedType.resolveInternal(errorReporter, enclosingScope);
    assertTrue(namedType.isResolved());
    assertSame(expectedType, namedType.getReferencedType());
  }

  @Test
  public void testResolveViaProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "Object.prototype.toString";
    JSType baseType = new ObjectType(registry, "Object");
    JSType prototype = new ObjectType(registry, "Object");
    prototype.setPrototypeOf(baseType);
    baseType.setPrototypeOf(prototype);
    namedType = new NamedType(registry, reference, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    namedType.resolveInternal(errorReporter, enclosingScope);
    assertTrue(namedType.isResolved());
    JSType expectedType = prototype.getPropertyType("toString");
    assertSame(expectedType, namedType.getReferencedType());
  }

  @Test
  public void testSetValidator() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "String";
    namedType = new NamedType(registry, reference, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input instanceof StringType;
      }
    };
    namedType.setValidator(validator);
    JSType expectedType = registry.getType(reference);
    assertTrue(expectedType instanceof StringType);
    assertTrue(validator.apply(expectedType));
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "String";
    namedType = new NamedType(registry, reference, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    List<PropertyContinuation> propertyContinuations = Lists.newArrayList();
    namedType.propertyContinuations = propertyContinuations;
    namedType.defineProperty("test", registry.getNativeBooleanType(), false, null);
    assertFalse(namedType.isResolved());
    assertNull(namedType.getReferencedType());
    assertSame(registry.getNativeBooleanType(), propertyContinuations.get(0).type);
  }

  @Test
  public void testFinishPropertyContinuations() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "String";
    namedType = new NamedType(registry, reference, SOURCE_NAME, LINE_NUMBER, CHAR_NUMBER);
    List<PropertyContinuation> propertyContinuations = Lists.newArrayList();
    namedType.propertyContinuations = propertyContinuations;
    PropertyContinuation propertyContinuation = new PropertyContinuation("test", registry.getNativeBooleanType(), false, null);
    propertyContinuations.add(propertyContinuation);
    ObjectType referencedObjType = new ObjectType(registry, "String");
    namedType.finishPropertyContinuations();
    assertTrue(namedType.isResolved());
    assertSame(referencedObjType, namedType.getReferencedType());
    propertyContinuation.commit(namedType);
    assertSame(referencedObjType.getPropertyType("test"), namedType.getReferencedType());
  }
}