import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.JSTypeRegistry;
import com.google.javascript.jscomp.type.ObjectType;
import com.google.javascript.jscomp.type.FunctionType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private CodingConvention convention;
  private JSTypeRegistry typeRegistry;
  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setUp() {
    convention = mock(CodingConvention.class);
    typeRegistry = mock(JSTypeRegistry.class);
    interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
  }

  @Test
  public void testIsDefTrue() {
    JSType type = new ObjectType("MyType");
    Node condition = Node.newCall(
        Node.newName("goog"),
        Node.newQualifiedName("isDef"),
        Node.newThis(),
        Node.newString("myVar"));
    FlowScope blindScope = mock(FlowScope.class);

    JSType expected = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true).getScopeType();
    JSType actual = interpreter.restrictParameter(
        Node.newName("myVar"), type, blindScope, interpreter.restricters.get("isDef"),
        true).getScopeType();

    assertEquals(expected, actual);
    assertEquals(type.getGreatestSubtype(typeRegistry.createUnionType(
        typeRegistry.createNamedType("MyType"), typeRegistry.createNullType())), actual);
  }

  @Test
  public void testIsDefFalse() {
    JSType type = new ObjectType("MyType");
    Node condition = Node.newCall(
        Node.newName("goog"),
        Node.newQualifiedName("isDef"),
        Node.newThis(),
        Node.newString("myVar"));
    FlowScope blindScope = mock(FlowScope.class);

    JSType expected = interpreter.nextPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false).getScopeType();
    JSType actual = interpreter.restrictParameter(
        Node.newName("myVar"), type, blindScope, interpreter.restricters.get("isDef"),
        false).getScopeType();

    assertEquals(expected, actual);
    assertEquals(null, actual);
  }

  @Test
  public void testIsDefNull() {
    Node condition = Node.newCall(
        Node.newName("goog"),
        Node.newQualifiedName("isDef"),
        Node.newThis(),
        Node.newNull());
    FlowScope blindScope = mock(FlowScope.class);

    JSType expected = interpreter.nextPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false).getScopeType();
    JSType actual = interpreter.restrictParameter(
        Node.newName("myVar"), null, blindScope, interpreter.restricters.get("isDef"),
        false).getScopeType();

    assertEquals(expected, actual);
    assertEquals(JSTypeNative.VOID_TYPE, actual);
  }

  @Test
  public void testIsDefAndNotNullTrue() {
    JSType type = new ObjectType("MyType");
    Node condition = Node.newCall(
        Node.newName("goog"),
        Node.newQualifiedName("isDefAndNotNull"),
        Node.newThis(),
        Node.newString("myVar"));
    FlowScope blindScope = mock(FlowScope.class);

    JSType expected = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true).getScopeType();
    JSType actual = interpreter.restrictParameter(
        Node.newName("myVar"), type, blindScope, interpreter.restricters.get("isDefAndNotNull"),
        true).getScopeType();

    assertEquals(expected, actual);
    assertEquals(type.getGreatestSubtype(typeRegistry.createUnionType(
        typeRegistry.createNamedType("MyType"), typeRegistry.createNullType(),
        typeRegistry.createVoidType())), actual);
  }

  @Test
  public void testIsDefAndNotNullFalse() {
    JSType type = new ObjectType("MyType");
    Node condition = Node.newCall(
        Node.newName("goog"),
        Node.newQualifiedName("isDefAndNotNull"),
        Node.newThis(),
        Node.newString("myVar"));
    FlowScope blindScope = mock(FlowScope.class);

    JSType expected = interpreter.nextPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false).getScopeType();
    JSType actual = interpreter.restrictParameter(
        Node.newName("myVar"), type, blindScope, interpreter.restricters.get("isDefAndNotNull"),
        false).getScopeType();

    assertEquals(expected, actual);
    assertEquals(JSTypeNative.NULL_VOID, actual);
  }

  @Test
  public void testIsDefAndNotNullNull() {
    Node condition = Node.newCall(
        Node.newName("goog"),
        Node.newQualifiedName("isDefAndNotNull"),
        Node.newThis(),
        Node.newNull());
    FlowScope blindScope = mock(FlowScope.class);

    JSType expected = interpreter.nextPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false).getScopeType();
    JSType actual = interpreter.restrictParameter(
        Node.newName("myVar"), null, blindScope, interpreter.restricters.get("isDefAndNotNull"),
        false).getScopeType();

    assertEquals(expected, actual);
    assertEquals(JSTypeNative.NULL_VOID, actual);
  }

  @Test
  public void testIsStringTrue() {
    JSType type = new ObjectType("MyType");
    Node condition = Node.newCall(
        Node.newName("goog"),
        Node.newQualifiedName("isString"),
        Node.newThis(),
        Node.newString("myVar"));
    FlowScope blindScope = mock(FlowScope.class);

    JSType expected = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true).getScopeType();
    JSType actual = interpreter.restrictParameter(
        Node.newName("myVar"), type, blindScope, interpreter.restricters.get("isString"),
        true).getScopeType();

    assertEquals(expected, actual);
    assertEquals(typeRegistry.createNamedType("String"), actual);
  }

  @Test
  public void testIsStringFalse() {
    JSType type = new ObjectType("MyType");
    Node condition = Node.newCall(
        Node.newName("goog"),
        Node.newQualifiedName("isString"),
        Node.newThis(),
        Node.newString("myVar"));
    FlowScope blindScope = mock(FlowScope.class);

    JSType expected = interpreter.nextPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false).getScopeType();
    JSType actual = interpreter.restrictParameter(
        Node.newName("myVar"), type, blindScope, interpreter.restricters.get("isString"),
        false).getScopeType();

    assertEquals(expected, actual);
    assertEquals(type, actual);
  }

  @Test
  public void testIsFunctionTrue() {
    FunctionType functionType = new FunctionType(new ObjectType("MyType"), VOID_TYPE);
    JSType type = functionType;
    Node condition = Node.newCall(
        Node.newName("goog"),
        Node.newQualifiedName("isFunction"),
        Node.newThis(),
        Node.newString("myVar"));
    FlowScope blindScope = mock(FlowScope.class);

    JSType expected = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true).getScopeType();
    JSType actual = interpreter.restrictParameter(
        Node.newName("myVar"), type, blindScope, interpreter.restricters.get("isFunction"),
        true).getScopeType();

    assertEquals(expected, actual);
    assertEquals(functionType, actual);
  }

  @Test
  public void testIsFunctionFalse() {
    FunctionType functionType = new FunctionType(new ObjectType("MyType"), VOID_TYPE);
    JSType type = functionType;
    Node condition = Node.newCall(
        Node.newName("goog"),
        Node.newQualifiedName("isFunction"),
        Node.newThis(),
        Node.newString("myVar"));
    FlowScope blindScope = mock(FlowScope.class);

    JSType expected = interpreter.nextPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false).getScopeType();
    JSType actual = interpreter.restrictParameter(
        Node.newName("myVar"), type, blindScope, interpreter.restricters.get("isFunction"),
        false).getScopeType();

    assertEquals(expected, actual);
    assertEquals(null, actual);
  }

  @Test
  public void testIsFunctionNull() {
    Node condition = Node.newCall(
        Node.newName("goog"),
        Node.newQualifiedName("isFunction"),
        Node.newThis(),
        Node.newNull());
    FlowScope blindScope = mock(FlowScope.class);

    JSType expected = interpreter.nextPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false).getScopeType();
    JSType actual = interpreter.restrictParameter(
        Node.newName("myVar"), null, blindScope, interpreter.restricters.get("isFunction"),
        false).getScopeType();

    assertEquals(expected, actual);
    assertEquals(JSTypeNative.NULL_VOID, actual);
  }

  @Test
  public void testIsArrayTrue() {
    JSType arrayType = typeRegistry.createNamedType("Array<number>");
    Node condition = Node.newCall(
        Node.newName("goog"),
        Node.newQualifiedName("isArray"),
        Node.newThis(),
        Node.newString("myVar"));
    FlowScope blindScope = mock(FlowScope.class);

    JSType expected = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true).getScopeType();
    JSType actual = interpreter.restrictParameter(
        Node.newName("myVar"), null, blindScope, interpreter.restricters.get("isArray"),
        true).getScopeType();

    assertEquals(expected, actual);
    assertEquals(arrayType, actual);
  }

  @Test
  public void testIsArrayFalse() {
    JSType arrayType = typeRegistry.createNamedType("Array<number>");
    Node condition = Node.newCall(
        Node.newName("goog"),
        Node.newQualifiedName("isArray"),
        Node.newThis(),
        Node.newString("myVar"));
    FlowScope blindScope = mock(FlowScope.class);

    JSType expected = interpreter.nextPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false).getScopeType();
    JSType actual = interpreter.restrictParameter(
        Node.newName("myVar"), null, blindScope, interpreter.restricters.get("isArray"),
        false).getScopeType();

    assertEquals(expected, actual);
    assertEquals(null, actual);
  }

  @Test
  public void testIsObjectTrue() {
    ObjectType objectType = new ObjectType("MyType");
    Node condition = Node.newCall(
        Node.newName("goog"),
        Node.newQualifiedName("isObject"),
        Node.newThis(),
        Node.newString("myVar"));
    FlowScope blindScope = mock(FlowScope.class);

    JSType expected = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true).getScopeType();
    JSType actual = interpreter.restrictParameter(
        Node.newName("myVar"), null, blindScope, interpreter.restricters.get("isObject"),
        true).getScopeType();

    assertEquals(expected, actual);
    assertEquals(objectType, actual);
  }

  @Test
  public void testIsObjectFalse() {
    ObjectType objectType = new ObjectType("MyType");
    Node condition = Node.newCall(
        Node.newName("goog"),
        Node.newQualifiedName("isObject"),
        Node.newThis(),
        Node.newString("myVar"));
    FlowScope blindScope = mock(FlowScope.class);

    JSType expected = interpreter.nextPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false).getScopeType();
    JSType actual = interpreter.restrictParameter(
        Node.newName("myVar"), null, blindScope, interpreter.restricters.get("isObject"),
        false).getScopeType();

    assertEquals(expected, actual);
    assertEquals(null, actual);
  }

  @Test
  public void testIsObjectNull() {
    Node condition = Node.newCall(
        Node.newName("goog"),
        Node.newQualifiedName("isObject"),
        Node.newThis(),
        Node.newNull());
    FlowScope blindScope = mock(FlowScope.class);

    JSType expected = interpreter.nextPreciserScopeKnowingConditionOutcome(
        condition, blindScope, false).getScopeType();
    JSType actual = interpreter.restrictParameter(
        Node.newName("myVar"), null, blindScope, interpreter.restricters.get("isObject"),
        false).getScopeType();

    assertEquals(expected, actual);
    assertEquals(JSTypeNative.NULL_OBJECT_TYPE, actual);
  }

  private static final JSType VOID_TYPE = JSTypeNative.VOID_TYPE;
}