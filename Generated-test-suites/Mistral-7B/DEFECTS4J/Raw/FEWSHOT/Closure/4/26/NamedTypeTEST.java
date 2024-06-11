package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.google.common.collect.Lists;
import org.junit.Test;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.PropertyContinuation;
import com.google.javascript.rhino.PropertyType;
import com.google.javascript.rhino.FunctionType;
import com.google.javascript.rhino.EnumType;
import com.google.javascript.rhino.EnumElementType;
import com.google.javascript.rhino.JSTypeNative;

public class NamedTypeTest {

  @Test
  public void testResolveViaRegistry() {
    JSTypeRegistry registry = new JSTypeRegistry() {
      @Override
      public JSType getType(String name) {
        if (name.equals("String")) {
          return registry.getStringType();
        }
        if (name.equals("Object")) {
          return registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        }
        if (name.equals("NamedType")) {
          return new NamedType(registry, "String", "N/A", 1, 1) {
            @Override
            public JSType getReferencedType() {
              return registry.getStringType();
            }
          };
        }
        return null;
      }
    };

    StaticScope<JSType> enclosing = new StaticScope<>();
    NamedType namedType = new NamedType(registry, "String", "N/A", 1, 1);
    ErrorReporter t = new ErrorReporter() {};
    JSType type = namedType.resolveInternal(t, enclosing);
    assertEquals("String", type.getClass().getSimpleName());
  }

  @Test
  public void testResolveViaProperties() {
    JSTypeRegistry registry = new JSTypeRegistry() {
      private static final JSType TYPE_A = new ObjectType(registry, "TypeA");
      private static final PropertyType PROP_B = new PropertyType("b", registry.getNumberType());

      @Override
      public JSType getType(String name) {
        if (name.equals("TypeA")) {
          return TYPE_A;
        }
        return null;
      }

      @Override
      public JSType getNativeObjectType(int type) {
        switch (type) {
          case JSTypeNative.OBJECT_TYPE:
            return registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
          case JSTypeNative.NUMBER_TYPE:
            return registry.getNumberType();
          default:
            return super.getNativeObjectType(type);
        }
      }
    };

    StaticScope<JSType> enclosing = new StaticScope<>();
    ObjectType typeA = new ObjectType(registry, "TypeA");
    StaticSlot<JSType> slot = new StaticSlot<>(typeA, "b", registry.getNumberType());
    enclosing.setSlot(slot);

    NamedType namedType = new NamedType(registry, "TypeA.b", "N/A", 1, 1);
    ErrorReporter t = new ErrorReporter() {};
    JSType type = namedType.resolveInternal(t, enclosing);

    assertEquals("Number", type.getClass().getSimpleName());
  }

  @Test
  public void testResolveViaProperties_invalid() {
    JSTypeRegistry registry = new JSTypeRegistry() {
      private static final ObjectType TYPE_A = new ObjectType(registry, "TypeA");

      @Override
      public JSType getType(String name) {
        if (name.equals("TypeA")) {
          return TYPE_A;
        }
        return null;
      }

      @Override
      public JSType getNativeObjectType(int type) {
        switch (type) {
          case JSTypeNative.OBJECT_TYPE:
            return registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
          default:
            return super.getNativeObjectType(type);
        }
      }
    };

    StaticScope<JSType> enclosing = new StaticScope<>();
    ObjectType typeA = new ObjectType(registry, "TypeA");
    StaticSlot<JSType> slot = new StaticSlot<>(typeA, "b", registry.getNumberType());
    enclosing.setSlot(slot);

    NamedType namedType = new NamedType(registry, "TypeX.b", "N/A", 1, 1);
    ErrorReporter t = new ErrorReporter() {};
    JSType type = namedType.resolveInternal(t, enclosing);
    assertNull(type);
  }

  @Test
  public void testResolveViaRegistry_unresolved() {
    JSTypeRegistry registry = new JSTypeRegistry() {
      @Override
      public JSType getType(String name) {
        if (name.equals("String")) {
          return registry.getStringType();
        }
        if (name.equals("NamedType")) {
          return new NamedType(registry, "String", "N/A", 1, 1);
        }
        return null;
      }
    };

    StaticScope<JSType> enclosing = new StaticScope<>();
    NamedType namedType = new NamedType(registry, "NamedType", "N/A", 1, 1);
    ErrorReporter t = new ErrorReporter() {};
    JSType type = namedType.resolveInternal(t, enclosing);
    assertNull(type);
  }

  @Test
  public void testResolveViaProperties_unresolved() {
    JSTypeRegistry registry = new JSTypeRegistry() {
      private static final ObjectType TYPE_A = new ObjectType(registry, "TypeA");

      @Override
      public JSType getType(String name) {
        if (name.equals("TypeA")) {
          return TYPE_A;
        }
        return null;
      }

      @Override
      public JSType getNativeObjectType(int type) {
        switch (type) {
          case JSTypeNative.OBJECT_TYPE:
            return registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
          default:
            return super.getNativeObjectType(type);
        }
      }
    };

    StaticScope<JSType> enclosing = new StaticScope<>();
    ObjectType typeA = new ObjectType(registry, "TypeA");
    StaticSlot<JSType> slot = new StaticSlot<>(typeA, "b", registry.getNumberType());
    enclosing.setSlot(slot);

    NamedType namedType = new NamedType(registry, "TypeX.b", "N/A", 1, 1);
    ErrorReporter t = new ErrorReporter() {};
    JSType type = namedType.resolveInternal(t, enclosing);
    assertNull(type);
  }

  @Test
  public void testSetValidator() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "String", "N/A", 1, 1);
    Predicate<JSType> validator = t -> t instanceof StringType;
    namedType.setValidator(validator);

    JSType type = namedType.resolveInternal(new ErrorReporter() {}, new StaticScope<>());
    assertNotNull(type);
    assertEquals("String", type.getClass().getSimpleName());
  }

  @Test(expected = AssertionError.class)
  public void testSetValidator_alreadyResolved() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = new NamedType(registry, "String", "N/A", 1, 1) {
      @Override
      public JSType getReferencedType() {
        return registry.getStringType();
      }
    };

    Predicate<JSType> validator = t -> t instanceof StringType;
    namedType.setValidator(validator);
  }
}