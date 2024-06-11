import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Type;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.ObjectType;
import com.google.javascript.jscomp.type.FunctionType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;

import java.util.Map;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testRestrictToArrayVisitor() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    JSType arrayType = JSTypeNative.getNativeType(ARRAY_TYPE);
    ObjectType objectType1 = mock(ObjectType.class);
    ObjectType objectType2 = mock(ObjectType.class);
    when(objectType1.isSubtype(objectType2)).thenReturn(false);
    when(objectType2.isSubtype(objectType1)).thenReturn(false);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), typeRegistry);
    Visitor<JSType> visitor = interpreter.restrictToArrayVisitor;

    assertEquals(arrayType, visitor.caseObjectType(objectType1));
    assertEquals(null, visitor.caseObjectType(objectType2));
  }

  @Test
  public void testRestrictToNotArrayVisitor() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    ObjectType objectType1 = mock(ObjectType.class);
    ObjectType objectType2 = mock(ObjectType.class);
    FunctionType functionType = mock(FunctionType.class);
    when(objectType1.isSubtype(objectType2)).thenReturn(false);
    when(objectType2.isSubtype(objectType1)).thenReturn(false);
    when(objectType1.isSubtype(JSTypeNative.getNativeType(ARRAY_TYPE)))
        .thenReturn(false);
    when(functionType.isSubtype(JSTypeNative.getNativeType(ARRAY_TYPE))).thenReturn(false);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), typeRegistry);
    Visitor<JSType> visitor = interpreter.restrictToNotArrayVisitor;

    assertEquals(null, visitor.caseObjectType(objectType1));
    assertEquals(objectType1, visitor.caseObjectType(objectType2));
    assertEquals(null, visitor.caseFunctionType(functionType));
  }

  @Test
  public void testRestrictToObjectVisitor() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    ObjectType objectType1 = mock(ObjectType.class);
    ObjectType objectType2 = mock(ObjectType.class);
    FunctionType functionType = mock(FunctionType.class);

    when(objectType1.isSubtype(objectType2)).thenReturn(false);
    when(objectType2.isSubtype(objectType1)).thenReturn(false);
    when(objectType1.isSubtype(JSTypeNative.getNativeType(ARRAY_TYPE)))
        .thenReturn(false);
    when(functionType.isSubtype(JSTypeNative.getNativeType(ARRAY_TYPE))).thenReturn(false);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), typeRegistry);
    Visitor<JSType> visitor = interpreter.restrictToObjectVisitor;

    ArgumentCaptor<JSType> captureTopType = ArgumentCaptor.forClass(JSType.class);
    ArgumentCaptor<ObjectType> captureObjectType = ArgumentCaptor.forClass(ObjectType.class);
    ArgumentCaptor<FunctionType> captureFunctionType = ArgumentCaptor.forClass(FunctionType.class);

    assertEquals(JSTypeNative.getNativeType(NO_OBJECT_TYPE),
        visitor.caseTopType(captureTopType.capture(JSTypeNative.getNativeType(OBJECT_TYPE))));
    assertEquals(objectType1, visitor.caseObjectType(captureObjectType.capture(objectType1)));
    assertEquals(functionType, visitor.caseFunctionType(captureFunctionType.capture(functionType)));
  }

  @Test
  public void testRestrictToNotObjectVisitor() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    ObjectType objectType1 = mock(ObjectType.class);
    ObjectType objectType2 = mock(ObjectType.class);
    FunctionType functionType = mock(FunctionType.class);

    when(objectType1.isSubtype(objectType2)).thenReturn(false);
    when(objectType2.isSubtype(objectType1)).thenReturn(false);
    when(objectType1.isSubtype(JSTypeNative.getNativeType(ARRAY_TYPE)))
        .thenReturn(false);
    when(functionType.isSubtype(JSTypeNative.getNativeType(ARRAY_TYPE))).thenReturn(false);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), typeRegistry);
    Visitor<JSType> visitor = interpreter.restrictToNotObjectVisitor;

    ArgumentCaptor<JSType> captureAllType = ArgumentCaptor.forClass(JSType.class);

    assertEquals(JSTypeNative.createUnionType(
        JSTypeNative.getNativeType(NUMBER_STRING_BOOLEAN),
        JSTypeNative.getNativeType(NULL_VOID)),
        visitor.caseAllType());
  }

  @Test
  public void testRestrictByTypeOfResultVisitor() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    ObjectType objectTypeString = mock(ObjectType.class);
    ObjectType objectTypeBoolean = mock(ObjectType.class);
    ObjectType objectTypeNumber = mock(ObjectType.class);
    JSType typeString = JSTypeNative.getNativeType("string");
    JSType typeBoolean = JSTypeNative.getNativeType("boolean");
    JSType typeNumber = JSTypeNative.getNativeType("number");

    when(objectTypeString.isSubtype(objectTypeBoolean)).thenReturn(false);
    when(objectTypeString.isSubtype(objectTypeNumber)).thenReturn(false);
    when(objectTypeBoolean.isSubtype(objectTypeString)).thenReturn(false);
    when(objectTypeBoolean.isSubtype(objectTypeNumber)).thenReturn(false);
    when(objectTypeNumber.isSubtype(objectTypeString)).thenReturn(false);
    when(objectTypeNumber.isSubtype(objectTypeBoolean)).thenReturn(false);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), typeRegistry);

    assertEquals(typeString, interpreter.restrictByTypeOfResult(
        objectTypeString, "string", true));
    assertEquals(typeBoolean, interpreter.restrictByTypeOfResult(
        objectTypeBoolean, "boolean", true));
    assertEquals(typeNumber, interpreter.restrictByTypeOfResult(
        objectTypeNumber, "number", true));
    assertEquals(null, interpreter.restrictByTypeOfResult(
        objectTypeString, "number", true));
    assertEquals(null, interpreter.restrictByTypeOfResult(
        objectTypeString, "boolean", false));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    ObjectType objectType1 = mock(ObjectType.class);
    ObjectType objectType2 = mock(ObjectType.class);
    FunctionType functionType = mock(FunctionType.class);
    Node conditionCall = Node.newCall(Node.newName("goog"), "isArray", null,
        Node.newQualifiedName("isDef"), Node.newName("param"), null);

    when(objectType1.isSubtype(objectType2)).thenReturn(false);
    when(objectType2.isSubtype(objectType1)).thenReturn(false);
    when(objectType1.isSubtype(JSTypeNative.getNativeType(ARRAY_TYPE)))
        .thenReturn(true);
    when(objectType2.isSubtype(JSTypeNative.getNativeType(ARRAY_TYPE)))
        .thenReturn(false);
    when(functionType.isSubtype(JSTypeNative.getNativeType(ARRAY_TYPE)))
        .thenReturn(false);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), typeRegistry);

    Node param = Node.newName("param");
    JSType paramType = JSTypeNative.getNativeType("number");
    FlowScope blindScope = Node.newRootScope();

    FlowScope scope = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionCall, blindScope, true);

    ArgumentCaptor<Node> captureParam = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<JSType> captureType = ArgumentCaptor.forClass(JSType.class);
    assertEquals(JSTypeNative.getNativeType(ARRAY_TYPE), captureType.capture(
        interpreter.restrictParameter(param, paramType, blindScope,
            interpreter.restricters.get("isArray"), true).getType()));
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeNegative() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    ObjectType objectType1 = mock(ObjectType.class);
    ObjectType objectType2 = mock(ObjectType.class);
    FunctionType functionType = mock(FunctionType.class);
    Node conditionCall = Node.newCall(Node.newName("goog"), "isArray", null,
        Node.newQualifiedName("isDef"), Node.newName("param"), null);

    when(objectType1.isSubtype(objectType2)).thenReturn(false);
    when(objectType2.isSubtype(objectType1)).thenReturn(false);
    when(objectType1.isSubtype(JSTypeNative.getNativeType(ARRAY_TYPE)))
        .thenReturn(false);
    when(objectType2.isSubtype(JSTypeNative.getNativeType(ARRAY_TYPE)))
        .thenReturn(true);
    when(functionType.isSubtype(JSTypeNative.getNativeType(ARRAY_TYPE)))
        .thenReturn(false);

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), typeRegistry);

    Node param = Node.newName("param");
    JSType paramType = JSTypeNative.getNativeType("number");
    FlowScope blindScope = Node.newRootScope();

    FlowScope scope = interpreter.getPreciserScopeKnowingConditionOutcome(
        conditionCall, blindScope, false);

    assertEquals(blindScope, scope);
  }
}