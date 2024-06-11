package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.NativeObjectType;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.FunctionType;
import com.google.javascript.rhino.EnumType;
import com.google.javascript.rhino.EnumElementType;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.ProxyObjectType;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.ArrayList;

import org.junit.Test;

public class NamedTypeTest {
  @Test
  public void testNamedTypeConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "String", "SourceFile.js", 1, 1);
    assertEquals("String", namedType.getReferenceName());
    assertNotNull(namedType.getReferencedType());
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "String", "SourceFile.js", 1, 1);

    assertFalse(namedType.defineProperty("test", null, false, null));
    assertTrue(namedType.isResolved());

    List<PropertyContinuation> propertyContinuations = Lists.newArrayList();
    namedType = new NamedType(registry, "String", "SourceFile.js", 1, 1) {
      @Override
      List<PropertyContinuation> propertyContinuations() {
        return propertyContinuations;
      }
    };

    StaticScope<JSType> enclosing = new StaticScope<>();
    PropertyContinuation propertyContinuation = new PropertyContinuation(
        "test", new FunctionType(new NativeObjectType(JSTypeNative.NUMBER_TYPE), null), false, null);
    assertEquals(0, namedType.propertyContinuations().size());

    namedType.defineProperty("test", null, false, null);
    assertEquals(1, namedType.propertyContinuations().size());
    assertEquals(propertyContinuation, namedType.propertyContinuations().get(0));
  }

  @Test(expected = AssertionError.class)
  public void testUnresolvedPropertyDefinition() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "String", "SourceFile.js", 1, 1);

    StaticScope<JSType> enclosing = new StaticScope<>();
    PropertyContinuation propertyContinuation = new PropertyContinuation(
        "test", new FunctionType(new NativeObjectType(JSTypeNative.NUMBER_TYPE), null), false, null);

    namedType.defineProperty("test", null, false, null);
    assertEquals(0, namedType.propertyContinuations().size());
  }

  @Test
  public void testFinishPropertyContinuations() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "String", "SourceFile.js", 1, 1);

    List<PropertyContinuation> propertyContinuations = Lists.newArrayList();
    namedType = new NamedType(registry, "String", "SourceFile.js", 1, 1) {
      @Override
      List<PropertyContinuation> propertyContinuations() {
        return propertyContinuations;
      }
    };

    StaticScope<JSType> enclosing = new StaticScope<>();
    PropertyContinuation propertyContinuation1 = new PropertyContinuation(
        "test1", new FunctionType(new NativeObjectType(JSTypeNative.NUMBER_TYPE), null), false, null);
    PropertyContinuation propertyContinuation2 = new PropertyContinuation(
        "test2", new FunctionType(new NativeObjectType(JSTypeNative.STRING_TYPE), null), false, null);

    namedType.propertyContinuations().add(propertyContinuation1);
    namedType.propertyContinuations().add(propertyContinuation2);

    namedType.finishPropertyContinuations();

    ObjectType referencedObjType = namedType.getReferencedObjTypeInternal();
    if (referencedObjType != null && !referencedObjType.isUnknownType()) {
      ObjectType target = new ObjectType(new JSTypeRegistry(), referencedObjType.getReferencedType());
      target.defineProperty("test1", new FunctionType(new NativeObjectType(JSTypeNative.NUMBER_TYPE), null), false, null);
      target.defineProperty("test2", new FunctionType(new NativeObjectType(JSTypeNative.STRING_TYPE), null), false, null);

      assertEquals(target.getOwnPropertyNames().size(), 2);
      assertTrue(target.hasProperty("test1"));
      assertTrue(target.hasProperty("test2"));
    }
  }

  @Test(expected = AssertionError.class)
  public void testUnresolvedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "UndefType", "SourceFile.js", 1, 1);
    ErrorReporter errorReporter = new ErrorReporter() {};

    assertNull(namedType.getReferencedType());
    namedType.resolveInternal(errorReporter, null);
    assertNull(namedType.getReferencedType());
  }

  @Test
  public void testResolveViaRegistry() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.registerType(new StringType());
    registry.registerType(new NumberType());

    NamedType namedType = new NamedType(registry, "String", "SourceFile.js", 1, 1);
    ErrorReporter errorReporter = new ErrorReporter() {};
    StaticScope<JSType> enclosing = new StaticScope<>();

    namedType.resolveInternal(errorReporter, enclosing);

    JSType type = namedType.getReferencedType();
    assertTrue(type instanceof StringType);
  }

  @Test(expected = AssertionError.class)
  public void testResolveViaPropertiesWithNoSuchProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    StaticScope<JSType> enclosing = new StaticScope<>();

    StaticSlot<JSType> staticSlot = new StaticSlot<>(enclosing, "String");
    JSType staticSlotType = staticSlot.getType();

    ErrorReporter errorReporter = new ErrorReporter() {};
    NamedType namedType = new NamedType(registry, "String.length", "SourceFile.js", 1, 1);

    namedType.resolveInternal(errorReporter, enclosing);

    assertNull(namedType.getReferencedType());
  }

  @Test
  public void testResolveViaPropertiesWithUnresolvedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    StaticScope<JSType> enclosing = new StaticScope<>();

    StaticSlot<JSType> staticSlot = new StaticSlot<>(enclosing, "Function");
    JSType staticSlotType = staticSlot.getType();

    ErrorReporter errorReporter = new ErrorReporter() {};
    NamedType namedType = new NamedType(registry, "Function.prototype", "SourceFile.js", 1, 1);

    namedType.resolveInternal(errorReporter, enclosing);

    JSType type = namedType.getReferencedType();
    assertEquals(type, registry.getNativeObjectType(JSTypeNative.FUNCTION_PROTOTYPE));
  }

  @Test
  public void testResolveViaPropertiesWithFunctionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    StaticScope<JSType> enclosing = new StaticScope<>();

    JSType functionType = new FunctionType(new NativeObjectType(JSTypeNative.NUMBER_TYPE), null);
    StaticSlot<JSType> staticSlot = new StaticSlot<>(enclosing, "Function");
    staticSlot.setType(functionType);

    ErrorReporter errorReporter = new ErrorReporter() {};
    NamedType namedType = new NamedType(registry, "Function.prototype.constructor", "SourceFile.js", 1, 1);

    namedType.resolveInternal(errorReporter, enclosing);

    JSType instanceType = namedType.getReferencedType();
    assertTrue(instanceType instanceof FunctionType);
  }

  @Test
  public void testResolveViaPropertiesWithEnumType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    StaticScope<JSType> enclosing = new StaticScope<>();

    JSType elementsType = new EnumType("Color", new NativeObjectType(JSTypeNative.NUMBER_TYPE));
    StaticSlot<JSType> staticSlot = new StaticSlot<>(enclosing, "Color");
    staticSlot.setType(elementsType);

    ErrorReporter errorReporter = new ErrorReporter() {};
    NamedType namedType = new NamedType(registry, "Color.RED", "SourceFile.js", 1, 1);

    namedType.resolveInternal(errorReporter, enclosing);

    JSType enumType = namedType.getReferencedType();
    assertTrue(enumType instanceof EnumType);
    assertEquals("Color", enumType.getReferenceName());

    JSType elementsType2 = enumType.getElementsType();
    assertEquals(elementsType, elementsType2);
  }

  @Test
  public void testResolveViaPropertiesWithEnumElementType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    StaticScope<JSType> enclosing = new StaticScope<>();

    JSType elementsType = new EnumType("Color", new NativeObjectType(JSTypeNative.NUMBER_TYPE));
    StaticSlot<JSType> staticSlot = new StaticSlot<>(enclosing, "Color");
    staticSlot.setType(elementsType);

    ErrorReporter errorReporter = new ErrorReporter() {};
    NamedType namedType = new NamedType(registry, "Color.RED", "SourceFile.js", 1, 1);

    namedType.resolveInternal(errorReporter, enclosing);

    JSType enumElementType = namedType.getReferencedType();
    assertTrue(enumElementType instanceof EnumElementType);
    assertEquals("RED", enumElementType.getReferenceName());

    JSType primitiveType = enumElementType.getPrimitiveType();
    assertEquals(new NumberType(JSTypeNative.INT_32), primitiveType);
  }

  @Test(expected = AssertionError.class)
  public void testInvalidComponentName() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "", "SourceFile.js", 1, 1);
  }

  @Test(expected = AssertionError.class)
  public void testResolveViaPropertiesWithUnresolvableComponent() {
    JSTypeRegistry registry = new JSTypeRegistry();
    StaticScope<JSType> enclosing = new StaticScope<>();

    StaticSlot<JSType> staticSlot = new StaticSlot<>(enclosing, "UnresolvedType");
    JSType staticSlotType = staticSlot.getType();

    ErrorReporter errorReporter = new ErrorReporter() {};
    NamedType namedType = new NamedType(registry, "UnresolvedType.test", "SourceFile.js", 1, 1);

    namedType.resolveInternal(errorReporter, enclosing);

    assertNull(namedType.getReferencedType());
  }
}