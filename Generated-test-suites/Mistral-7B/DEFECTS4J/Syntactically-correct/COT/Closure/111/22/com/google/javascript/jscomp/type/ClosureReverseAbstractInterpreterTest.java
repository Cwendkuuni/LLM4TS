package com.google.javascript.jscomp.type.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.type.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.type.FunctionType;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.ObjectType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistryImpl;

public class ClosureReverseAbstractInterpreterTest {

  private static JSTypeRegistry registry = new JSTypeRegistryImpl();
  private static CodingConvention convention = new CodingConvention();

  @Test
  public void testIsDef() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, registry);
    JSType type = registry.createFunctionType(new FunctionType(null, null));
    TypeRestriction restriction = new TypeRestriction(type, true);
    JSType result = interpreter.restricters.get("isDef").apply(restriction);
    assertNotNull(result);

    restriction = new TypeRestriction(type, false);
    result = interpreter.restricters.get("isDef").apply(restriction);
    assertNull(result);
  }

  // Add test cases for other methods in ClosureReverseAbstractInterpreter
  // such as isNull, isDefAndNotNull, isString, isBoolean, isNumber, isFunction,
  // isArray, and isObject.

  @Test
  public void testRestrictToArray() {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, registry);
    JSType arrayType = registry.createArrayType(registry.createStringType());
    JSType objectType = registry.createObjectType();

    // Test when goog.isArray returns true
    JSType result = interpreter.visit(restrictToArrayVisitor, objectType);
    assertNotNull(result);
    assertEquals(arrayType, result);

    result = interpreter.visit(restrictToArrayVisitor, arrayType);
    assertNotNull(result);
    assertEquals(arrayType, result);

    // Test when goog.isArray returns false
    result = interpreter.visit(restrictToNotArrayVisitor, arrayType);
    assertNull(result);

    result = interpreter.visit(restrictToNotArrayVisitor, objectType);
    assertNull(result);
  }

  // Add test cases for other methods in ClosureReverseAbstractInterpreter
  // such as restrictToNotObject and restrictToObject.

  private static Visitor<JSType> restrictToArrayVisitor =
      new Visitor<JSType>() {
        @Override
        public JSType visit(ObjectType type, VisitorContext context) {
          return interpreter.restrictToArrayVisitor.visit(type);
        }

        @Override
        public JSType visit(FunctionType type, VisitorContext context) {
          return interpreter.restrictToArrayVisitor.visit(type);
        }

        @Override
        public JSType visit(JSTypeNative nativeType, VisitorContext context) {
          return nativeType == JSTypeNative.ARRAY_TYPE ?
              JSTypeNative.ARRAY_TYPE : null;
        }

        @Override
        public JSType visit(JSType type, VisitorContext context) {
          return null;
        }
      };

  private static Visitor<JSType> restrictToNotArrayVisitor =
      new Visitor<JSType>() {
        @Override
        public JSType visit(ObjectType type, VisitorContext context) {
          return interpreter.restrictToNotArrayVisitor.visit(type);
        }

        @Override
        public JSType visit(FunctionType type, VisitorContext context) {
          return interpreter.restrictToNotArrayVisitor.visit(type);
        }

        @Override
        public JSType visit(JSTypeNative nativeType, VisitorContext context) {
          return nativeType != JSTypeNative.ARRAY_TYPE ?
              type : null;
        }

        @Override
        public JSType visit(JSType type, VisitorContext context) {
          return null;
        }
      };

  private static ClosureReverseAbstractInterpreter interpreter;

  private static JSType getRestrictedWithoutUndefined(JSType type) {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, registry);
    JSTypeNative undefinedType = JSTypeNative.UNDEFINED;
    Visitor<JSType> visitor = new Visitor<JSType>() {
      @Override
      public JSType visit(ObjectType type, VisitorContext context) {
        return interpreter.getRestrictedWithoutUndefined(type);
      }

      @Override
      public JSType visit(FunctionType type, VisitorContext context) {
        return interpreter.getRestrictedWithoutUndefined(type);
      }

      @Override
      public JSType visit(JSTypeNative nativeType, VisitorContext context) {
        return nativeType == undefinedType ? null : type;
      }

      @Override
      public JSType visit(JSType type, VisitorContext context) {
        return null;
      }
    };
    return type.visit(visitor);
  }

  private static JSType getRestrictedWithoutNull(JSType type) {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, registry);
    Visitor<JSType> visitor = new Visitor<JSType>() {
      @Override
      public JSType visit(ObjectType type, VisitorContext context) {
        return interpreter.getRestrictedWithoutNull(type);
      }

      @Override
      public JSType visit(FunctionType type, VisitorContext context) {
        return interpreter.getRestrictedWithoutNull(type);
      }

      @Override
      public JSType visit(JSTypeNative nativeType, VisitorContext context) {
        return nativeType != JSTypeNative.NULL_TYPE ? type : null;
      }

      @Override
      public JSType visit(JSType type, VisitorContext context) {
        return null;
      }
    };
    return type.visit(visitor);
  }

  private static JSType getRestrictedByTypeOfResult(JSType type,
      String expectedType, boolean outcome) {
    ClosureReverseAbstractInterpreter interpreter =
        new ClosureReverseAbstractInterpreter(convention, registry);
    Visitor<JSType> visitor = new Visitor<JSType>() {
      @Override
      public JSType visit(ObjectType type, VisitorContext context) {
        if (type.isSubtype(interpreter.getTypeRegistry().createStringType())) {
          return outcome ? interpreter.getTypeRegistry().createStringType() : null;
        } else if (type.isSubtype(interpreter.getTypeRegistry().createBooleanType())) {
          return outcome ? interpreter.getTypeRegistry().createBooleanType() : null;
        } else if (type.isSubtype(interpreter.getTypeRegistry().createNumberType())) {
          return outcome ? interpreter.getTypeRegistry().createNumberType() : null;
        } else {
          return null;
        }
      }

      @Override
      public JSType visit(FunctionType type, VisitorContext context) {
        return null;
      }

      @Override
      public JSType visit(JSTypeNative nativeType, VisitorContext context) {
        if (nativeType.getName().equals(expectedType)) {
          return outcome ? nativeType : null;
        } else {
          return null;
        }
      }

      @Override
      public JSType visit(JSType type, VisitorContext context) {
        return null;
      }
    };
    return type.visit(visitor);
  }
}