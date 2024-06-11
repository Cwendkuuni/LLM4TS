package com.google.javascript.rhino.jstype;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StylusObject;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.ProxyObjectType;
import com.google.javascript.rhino.jstype.StaticSlot;
import com.google.javascript.rhino.jstype.TypeFactory;
import com.google.javascript.rhino.jstype.TypeUtils;

import java.util.Arrays;
import java.util.List;

public class NamedTypeTest {

  private static ErrorReporter t = new ErrorReporter();
  private static StaticScope<JSType> enclosing = new StylusObject() {};

  @Test
  public void testNewInstance() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "String";
    NamedType namedType = new NamedType(registry, reference, "test", 1, 1);
    assertThat(namedType.getReferenceName()).isEqualTo(reference);
  }

  @Test
  public void testIsNamedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "String";
    NamedType namedType = new NamedType(registry, reference, "test", 1, 1);
    assertThat(namedType.isNamedType()).isTrue();
  }

  @Test
  public void testIsNominalType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "String";
    NamedType namedType = new NamedType(registry, reference, "test", 1, 1);
    assertThat(namedType.isNominalType()).isTrue();
  }

  @Test
  public void testHashCode() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "String";
    NamedType namedType1 = new NamedType(registry, reference, "test1", 1, 1);
    NamedType namedType2 = new NamedType(registry, reference, "test2", 1, 1);
    assertThat(namedType1.hashCode()).isEqualTo(namedType2.hashCode());
  }

  @Test
  public void testToStringHelper() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "String";
    NamedType namedType = new NamedType(registry, reference, "test", 1, 1);
    assertThat(namedType.toStringHelper(false)).isEqualTo(reference);
  }

  @Test
  public void testDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "String";
    NamedType namedType = new NamedType(registry, reference, "test", 1, 1);

    namedType.defineProperty("foo", TypeFactory.createBooleanType(), false, null);

    ObjectType objType = (ObjectType) namedType.getReferencedType();
    StaticSlot<JSType> slot = objType.getSlot("foo");

    assertThat(slot.getType()).isInstanceOf(TypeFactory.createBooleanType());
  }

  @Test
  public void testResolveViaRegistry() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "String";
    registry.registerType(reference, TypeFactory.createStringType());

    NamedType namedType = new NamedType(registry, reference, "test", 1, 1);
    JSType resolvedType = namedType.resolveInternal(t, enclosing);

    assertThat(resolvedType).isInstanceOf(TypeFactory.createStringType());
  }

  @Test
  public void testResolveViaRegistry_UnknownType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "UnknownType";
    NamedType namedType = new NamedType(registry, reference, "test", 1, 1);

    JSType resolvedType = namedType.resolveInternal(t, enclosing);
    assertThat(resolvedType).isInstanceOf(TypeFactory.getNativeObjectType());
  }

  @Test
  public void testResolveViaProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "String.prototype.toLowerCase";

    registry.registerType("String", TypeFactory.createStringType());
    registry.registerType("String.prototype", new ProxyObjectType(registry, TypeFactory.createStringType()));
    registry.registerType("String.prototype.toLowerCase", TypeFactory.createFunctionType(null, TypeFactory.createStringType(), true));

    NamedType namedType = new NamedType(registry, reference, "test", 1, 1);
    JSType resolvedType = namedType.resolveInternal(t, enclosing);

    assertThat(resolvedType).isInstanceOf(TypeFactory.createFunctionType(null, TypeFactory.createStringType(), true));
  }

  @Test
  public void testResolveViaProperties_UnknownType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "UnknownType.prototype.unknownMethod";

    registry.registerType("UnknownType", new ProxyObjectType(registry, TypeFactory.createNoObjectType()));
    registry.registerType("UnknownType.prototype", new ProxyObjectType(registry, TypeFactory.createNoObjectType()));

    NamedType namedType = new NamedType(registry, reference, "test", 1, 1);
    JSType resolvedType = namedType.resolveInternal(t, enclosing);

    assertNull(resolvedType);
  }

  @Test
  public void testResolveViaProperties_FunctionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "Function";

    registry.registerType("Function", new FunctionType(null, null, null, null, null, false, 0, false, null, null, false, false, false, false, false));

    NamedType namedType = new NamedType(registry, reference, "test", 1, 1);
    JSType resolvedType = namedType.resolveInternal(t, enclosing);

    assertThat(resolvedType).isInstanceOf(FunctionType.class);
  }

  @Test
  public void testResolveViaProperties_EnumType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    registry.registerType("MyEnum", new EnumType("MyEnum", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
    registry.registerType("MyEnum.RED", new EnumElementType("MyEnum", "RED", null, null, null, null, null, null, null, null));

    NamedType namedType = new NamedType(registry, "MyEnum.RED", "test", 1, 1);
    JSType resolvedType = namedType.resolveInternal(t, enclosing);

    assertThat(resolvedType).isInstanceOf(EnumElementType.class);
  }

  @Test
  public void testSetValidator() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "String";
    ObjectType objectType = new ObjectType(registry, "ObjectType");
    NamedType namedType = new NamedType(registry, reference, "test", 1, 1);

    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input.isStringType();
      }
    };

    namedType.setValidator(validator);
    JSType referencedType = namedType.getReferencedType();
    boolean valid = validator.apply(referencedType);

    assertThat(valid).isTrue();
  }

  @Test
  public void testSetValidator_UnknownType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "UnknownType";
    NamedType namedType = new NamedType(registry, reference, "test", 1, 1);

    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input.isNativeObjectType();
      }
    };

    namedType.setValidator(validator);
    JSType referencedType = namedType.getReferencedType();
    boolean valid = validator.apply(referencedType);

    assertThat(valid).isFalse();
  }
}