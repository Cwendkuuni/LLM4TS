package com.google.javascript.jscomp.test;

import static com.google.javascript.rhino.Token.CALL;
import static com.google.javascript.rhino.Token.GETPROP;
import static com.google.javascript.rhino.Token.NAME;
import static com.google.javascript.rhino.Token.STRING;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.TypeChecker;
import com.google.javascript.rhino.types.Reference;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ClosureReverseAbstractInterpreterTest {

  private static final JSTypeNative ARRAY_TYPE = new JSTypeNative(ARRAY_TYPE);
  private static final JSTypeNative NO_OBJECT_TYPE = new JSTypeNative(NO_OBJECT_TYPE);
  private static final JSTypeNative NULL_TYPE = new JSTypeNative(NULL_TYPE);
  private static final JSTypeNative OBJECT_TYPE = new JSTypeNative(OBJECT_TYPE);

  private ClosureReverseAbstractInterpreter interpreter;
  private JSTypeRegistry typeRegistry;
  private TypeChecker typeChecker;
  private Function<TypeRestriction, JSType> isDefRestricter;
  private Function<TypeRestriction, JSType> isNullRestricter;
  private Function<TypeRestriction, JSType> isDefAndNotNullRestricter;

  @Before
  public void setUp() {
    typeRegistry = mock(JSTypeRegistry.class);
    typeChecker = mock(TypeChecker.class);
    interpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), typeRegistry);
    isDefRestricter = interpreter.restricters.get("isDef");
    isNullRestricter = interpreter.restricters.get("isNull");
    isDefAndNotNullRestricter = interpreter.restricters.get("isDefAndNotNull");
  }

  @Test
  public void testRestrictIsDef() {
    JSType inputType = mock(JSType.class);
    Reference reference = mock(Reference.class);
    JSType restrictedType = isDefRestricter.apply(new TypeRestriction(inputType, true));
    assertSame(inputType, restrictedType);
  }

  @Test
  public void testRestrictIsNull() {
    JSType inputType = mock(JSType.class);
    Reference reference = mock(Reference.class);
    JSType restrictedType = isNullRestricter.apply(new TypeRestriction(inputType, false));
    assertNull(restrictedType);
  }

  @Test
  public void testRestrictIsDefAndNotNull() {
    JSType inputType = mock(JSType.class);
    Reference reference = mock(Reference.class);
    JSType restrictedType = isDefAndNotNullRestricter.apply(new TypeRestriction(inputType, true));
    assertSame(inputType, restrictedType);
  }

  @Test
  public void testRestrictArrayIsDef() {
    JSType inputType = ARRAY_TYPE;
    JSType restrictedType = isDefRestricter.apply(new TypeRestriction(inputType, true));
    assertSame(ARRAY_TYPE, restrictedType);
  }

  @Test
  public void testRestrictArrayIsNotDef() {
    JSType inputType = ARRAY_TYPE;
    JSType restrictedType = isDefRestricter.apply(new TypeRestriction(inputType, false));
    assertNull(restrictedType);
  }

  @Test
  public void testRestrictObjectIsDef() {
    ObjectType inputType = (ObjectType) OBJECT_TYPE;
    JSType restrictedType = isDefRestricter.apply(new TypeRestriction(inputType, true));
    assertSame(inputType, restrictedType);
  }

  @Test
  public void testRestrictObjectIsNotDef() {
    ObjectType inputType = (ObjectType) OBJECT_TYPE;
    JSType restrictedType = isDefRestricter.apply(new TypeRestriction(inputType, false));
    assertNull(restrictedType);
  }

  @Test
  public void testRestrictFunctionIsDef() {
    FunctionType inputType = mock(FunctionType.class);
    JSType restrictedType = isDefRestricter.apply(new TypeRestriction(inputType, true));
    assertSame(inputType, restrictedType);
  }

  @Test
  public void testRestrictFunctionIsNotDef() {
    FunctionType inputType = mock(FunctionType.class);
    JSType restrictedType = isDefRestricter.apply(new TypeRestriction(inputType, false));
    assertNull(restrictedType);
  }

  @Test
  public void testRestrictIsString() {
    JSType inputType = mock(JSType.class);
    when(inputType.isString()).thenAnswer(new Answer<Boolean>() {
      @Override
      public Boolean answer(InvocationOnMock invocation) throws Throwable {
        return true;
      }
    });
    JSType restrictedType = interpreter.restrictByTypeOfResult(inputType, "string", true);
    assertSame(inputType, restrictedType);
  }

  @Test
  public void testRestrictIsStringFalse() {
    JSType inputType = mock(JSType.class);
    when(inputType.isString()).thenAnswer(new Answer<Boolean>() {
      @Override
      public Boolean answer(InvocationOnMock invocation) throws Throwable {
        return false;
      }
    });
    JSType restrictedType = interpreter.restrictByTypeOfResult(inputType, "string", false);
    assertNull(restrictedType);
  }

  @Test
  public void testRestrictIsBoolean() {
    JSType inputType = mock(JSType.class);
    when(inputType.isBoolean()).thenAnswer(new Answer<Boolean>() {
      @Override
      public Boolean answer(InvocationOnMock invocation) throws Throwable {
        return true;
      }
    });
    JSType restrictedType = interpreter.restrictByTypeOfResult(inputType, "boolean", true);
    assertSame(inputType, restrictedType);
  }

  @Test
  public void testRestrictIsBooleanFalse() {
    JSType inputType = mock(JSType.class);
    when(inputType.isBoolean()).thenAnswer(new Answer<Boolean>() {
      @Override
      public Boolean answer(InvocationOnMock invocation) throws Throwable {
        return false;
      }
    });
    JSType restrictedType = interpreter.restrictByTypeOfResult(inputType, "boolean", false);
    assertNull(restrictedType);
  }

  @Test
  public void testRestrictIsNumber() {
    JSType inputType = mock(JSType.class);
    when(inputType.isNumber()).thenAnswer(new Answer<Boolean>() {
      @Override
      public Boolean answer(InvocationOnMock invocation) throws Throwable {
        return true;
      }
    });
    JSType restrictedType = interpreter.restrictByTypeOfResult(inputType, "number", true);
    assertSame(inputType, restrictedType);
  }

  @Test
  public void testRestrictIsNumberFalse() {
    JSType inputType = mock(JSType.class);
    when(inputType.isNumber()).thenAnswer(new Answer<Boolean>() {
      @Override
      public Boolean answer(InvocationOnMock invocation) throws Throwable {
        return false;
      }
    });
    JSType restrictedType = interpreter.restrictByTypeOfResult(inputType, "number", false);
    assertNull(restrictedType);
  }

  @Test
  public void testRestrictIsFunction() {
    FunctionType inputType = mock(FunctionType.class);
    JSType restrictedType = interpreter.restrictByTypeOfResult(inputType, "function", true);
    assertSame(inputType, restrictedType);
  }

  @Test
  public void testRestrictIsFunctionFalse() {
    FunctionType inputType = mock(FunctionType.class);
    JSType restrictedType = interpreter.restrictByTypeOfResult(inputType, "function", false);
    assertNull(restrictedType);
  }
}