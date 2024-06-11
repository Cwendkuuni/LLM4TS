import static org.junit.Assert.*;
import static com.google.javascript.jscomp.type.JSTypeNative.*;
import static com.google.javascript.jscomp.type.JSType.createUnionType;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.types.AnnotationType;
import com.google.javascript.rhino.types.ArrayType;
import com.google.javascript.rhino.types.BooleanType;
import com.google.javascript.rhino.types.FunctionType;
import com.google.javascript.rhino.types.ObjectType;
import com.google.javascript.rhino.types.Type;
import com.google.javascript.rhino.types.TypeFactory;
import com.google.javascript.rhino.types.UnionType;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private static final TypeRegistry typeRegistry = new TypeRegistry();
  private static final CodingConvention convention = new CodingConvention();
  private ClosureReverseAbstractInterpreter interpreter;
  private TypeFactory typeFactory;

  @Before
  public void setup() {
    interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
    typeFactory = interpreter.getTypeRegistry();
  }

  @Test
  public void testIsDefTrue() {
    Node condition = Node.makeCall(Node.makeName("goog"), "isDef",
        Node.makeName("myVar"));
    Node blindScope = Node.makeBlock(Node.makeEmptyStatementList());
    Type expectedType = NO_OBJECT_TYPE;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    Type actualType = result.getDeclaredType(Node.makeName("myVar"));
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsDefFalse() {
    Node condition = Node.makeCall(Node.makeName("goog"), "isDef",
        Node.makeNull());
    Node blindScope = Node.makeBlock(Node.makeEmptyStatementList());
    Type expectedType = createUnionType(NULL_VOID, VOID_TYPE);
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    Type actualType = result.getDeclaredType(Node.makeName("myVar"));
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsDefAndNotNullTrue() {
    Node condition = Node.makeCall(Node.makeName("goog"), "isDefAndNotNull",
        Node.makeName("myVar"));
    Node blindScope = Node.makeBlock(Node.makeEmptyStatementList());
    Type expectedType = NO_OBJECT_TYPE;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    Type actualType = result.getDeclaredType(Node.makeName("myVar"));
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsDefAndNotNullFalse() {
    Node condition = Node.makeCall(Node.makeName("goog"), "isDefAndNotNull",
        Node.makeNull());
    Node blindScope = Node.makeBlock(Node.makeEmptyStatementList());
    Type expectedType = NULL_VOID;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    Type actualType = result.getDeclaredType(Node.makeName("myVar"));
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsStringTrue() {
    Node condition = Node.makeCall(Node.makeName("goog"), "isString",
        Node.makeInt(1));
    Node blindScope = Node.makeBlock(Node.makeEmptyStatementList());
    Type expectedType = STRING_TYPE;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    Type actualType = result.getDeclaredType(Node.makeName("myVar"));
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsStringFalse() {
    Node condition = Node.makeCall(Node.makeName("goog"), "isString",
        Node.makeInt(1));
    Node blindScope = Node.makeBlock(Node.makeEmptyStatementList());
    Type expectedType = createUnionType(NUMBER_TYPE, NULL_VOID);
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    Type actualType = result.getDeclaredType(Node.makeName("myVar"));
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsBooleanTrue() {
    Node condition = Node.makeCall(Node.makeName("goog"), "isBoolean",
        Node.makeInt(1));
    Node blindScope = Node.makeBlock(Node.makeEmptyStatementList());
    Type expectedType = BOOLEAN_TYPE;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    Type actualType = result.getDeclaredType(Node.makeName("myVar"));
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsBooleanFalse() {
    Node condition = Node.makeCall(Node.makeName("goog"), "isBoolean",
        Node.makeInt(1));
    Node blindScope = Node.makeBlock(Node.makeEmptyStatementList());
    Type expectedType = createUnionType(NUMBER_TYPE, NULL_VOID);
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    Type actualType = result.getDeclaredType(Node.makeName("myVar"));
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsNumberTrue() {
    Node condition = Node.makeCall(Node.makeName("goog"), "isNumber",
        Node.makeInt(1));
    Node blindScope = Node.makeBlock(Node.makeEmptyStatementList());
    Type expectedType = NUMBER_TYPE;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    Type actualType = result.getDeclaredType(Node.makeName("myVar"));
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsNumberFalse() {
    Node condition = Node.makeCall(Node.makeName("goog"), "isNumber",
        Node.makeInt(1));
    Node blindScope = Node.makeBlock(Node.makeEmptyStatementList());
    Type expectedType = createUnionType(NUMBER_TYPE, NULL_VOID);
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    Type actualType = result.getDeclaredType(Node.makeName("myVar"));
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsFunctionTrue() {
    Node condition = Node.makeCall(Node.makeName("goog"), "isFunction",
        Node.makeName("myFunction"));
    Node blindScope = Node.makeBlock(Node.makeEmptyStatementList());
    Type expectedType = FunctionType.create(new ArrayType(new Type[0]), VOID_TYPE);
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    Type actualType = result.getDeclaredType(Node.makeName("myFunction"));
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsFunctionFalse() {
    Node condition = Node.makeCall(Node.makeName("goog"), "isFunction",
        Node.makeName("myFunction"));
    Node blindScope = Node.makeBlock(Node.makeEmptyStatementList());
    Type expectedType = null;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    Type actualType = result.getDeclaredType(Node.makeName("myFunction"));
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsArrayTrue() {
    Node condition = Node.makeCall(Node.makeName("goog"), "isArray",
        Node.makeName("myArray"));
    Node blindScope = Node.makeBlock(Node.makeEmptyStatementList());
    Type expectedType = ARRAY_TYPE;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    Type actualType = result.getDeclaredType(Node.makeName("myArray"));
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsArrayFalse() {
    Node condition = Node.makeCall(Node.makeName("goog"), "isArray",
        Node.makeName("myArray"));
    Node blindScope = Node.makeBlock(Node.makeEmptyStatementList());
    Type expectedType = null;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    Type actualType = result.getDeclaredType(Node.makeName("myArray"));
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsObjectTrue() {
    Node condition = Node.makeCall(Node.makeName("goog"), "isObject",
        Node.makeNull());
    Node blindScope = Node.makeBlock(Node.makeEmptyStatementList());
    Type expectedType = NO_OBJECT_TYPE;
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    Type actualType = result.getDeclaredType(Node.makeName("myVar"));
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testIsObjectFalse() {
    Node condition = Node.makeCall(Node.makeName("goog"), "isObject",
        Node.makeNull());
    Node blindScope = Node.makeBlock(Node.makeEmptyStatementList());
    Type expectedType = createUnionType(NULL_TYPE, NUMBER_STRING_BOOLEAN);
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    Type actualType = result.getDeclaredType(Node.makeName("myVar"));
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testRestrictToArray() {
    Type type = ANNOTATION_TYPE.create(ARRAY_TYPE, new AnnotationType[0]);
    Type result = interpreter.getRestrictedByTypeOfResult(type, "array", true);
    assertEquals(ARRAY_TYPE, result);
  }

  @Test
  public void testRestrictToNotArray() {
    Type type = ANNOTATION_TYPE.create(ARRAY_TYPE, new AnnotationType[0]);
    Type result = interpreter.getRestrictedByTypeOfResult(type, "array", false);
    assertNull(result);
  }

  @Test
  public void testRestrictToObject() {
    Type type = NO_OBJECT_TYPE;
    Type result = interpreter.getRestrictedByTypeOfResult(type, "object", true);
    assertEquals(NO_OBJECT_TYPE, result);
  }

  @Test
  public void testRestrictToNotObject() {
    Type type = NO_OBJECT_TYPE;
    Type result = interpreter.getRestrictedByTypeOfResult(type, "object", false);
    assertNull(result);
  }

  @Test
  public void testRestrictToNull() {
    Type type = NULL_TYPE;
    Type result = interpreter.getRestrictedWithoutNull(type);
    assertNull(result);
  }

  @Test
  public void testRestrictToNullWithoutUndefined() {
    Type type = NULL_TYPE;
    Type result = interpreter.getRestrictedWithoutUndefined(type);
    assertNull(result);
  }

  @Test
  public void testRestrictToUndefined() {
    Type type = NO_OBJECT_TYPE;
    Type result = interpreter.getRestrictedWithoutUndefined(type);
    assertEquals(NO_OBJECT_TYPE, result);
  }
}