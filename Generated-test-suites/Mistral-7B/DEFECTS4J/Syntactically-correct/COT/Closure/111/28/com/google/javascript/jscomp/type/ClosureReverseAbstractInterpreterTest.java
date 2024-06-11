import static com.google.javascript.jscomp.type.JSType.ARRAY_TYPE;
import static com.google.javascript.jscomp.type.JSType.BOOLEAN_TYPE;
import static com.google.javascript.jscomp.type.JSType.FUNCTION_TYPE;
import static com.google.javascript.jscomp.type.JSType.NUMBER_TYPE;
import static com.google.javascript.jscomp.type.JSType.NULL_TYPE;
import static com.google.javascript.jscomp.type.JSType.OBJECT_TYPE;
import static com.google.javascript.jscomp.type.JSType.STRING_TYPE;
import static com.google.javascript.jscomp.type.JSType.VOID_TYPE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.type.ChainableReverseAbstractInterpreter;
import com.google.javascript.jscomp.type.FlowScope;
import com.google.javascript.jscomp.type.JSType;
import com.google.javascript.jscomp.type.ObjectType;
import com.google.javascript.jscomp.type.Visitor;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ClosureReverseAbstractInterpreterTest {
  private static final JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
  private static final CodingConvention codingConvention = mock(CodingConvention.class);
  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    interpreter = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
  }

  @Test
  public void testRestrictToArrayVisitor() {
    JSType type = JSTypeNative.INTEGER_TYPE;
    JSType expectedType = ARRAY_TYPE;

    JSType result = interpreter.restrictToArrayVisitor.visit(type);
    assertEquals(expectedType, result);
  }

  @Test
  public void testRestrictToNotArrayVisitor() {
    JSType type = JSTypeNative.OBJECT_TYPE;
    JSType expectedType = NUMBER_TYPE;

    JSType result = interpreter.restrictToNotArrayVisitor.visit(type);
    assertEquals(expectedType, result);
  }

  @Test
  public void testRestrictToObjectVisitor() {
    JSType type = FUNCTION_TYPE;
    JSType expectedType = FUNCTION_TYPE;

    JSType result = interpreter.restrictToObjectVisitor.visit(type);
    assertEquals(expectedType, result);
  }

  @Test
  public void testRestrictToNotObjectVisitor() {
    JSType type = JSTypeNative.VOID_TYPE;
    JSType expectedType = VOID_TYPE;

    JSType result = interpreter.restrictToNotObjectVisitor.visit(type);
    assertEquals(expectedType, result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeWithTrueCondition() {
    JSType type = STRING_TYPE;
    Node condition = Node.newCall(Node.newName("goog"), "isDef", Node.newString("test"));
    Node parameter = Node.newQualifiedName("test", "testParameter");
    FlowScope blindScope = new FlowScope(JSType.UNKNOWN_TYPE);

    JSType expectedType = STRING_TYPE;

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    declareNameInScope(result, parameter, expectedType);

    JSType actualType = result.getVariable(parameter.getIdentifierName()).getType();
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcomeWithFalseCondition() {
    JSType type = NUMBER_TYPE;
    Node condition = Node.newCall(Node.newName("goog"), "isDef", Node.newString("test"));
    Node parameter = Node.newQualifiedName("test", "testParameter");
    FlowScope blindScope = new FlowScope(JSType.UNKNOWN_TYPE);

    JSType expectedType = VOID_TYPE;

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);

    JSType actualType = result.getVariable(parameter.getIdentifierName()).getType();
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testRestrictParameterWithStringRestriction() {
    JSType originalType = STRING_TYPE;
    Node parameter = Node.newQualifiedName("test", "testParameter");
    JSType expectedType = BOOLEAN_TYPE;
    boolean outcome = true;

    FlowScope blindScope = new FlowScope(JSType.UNKNOWN_TYPE);
    Function<TypeRestriction, JSType> restriction = mock(Function.class);
    when(restriction.apply(any(TypeRestriction.class))).thenReturn(expectedType);

    FlowScope result = interpreter.restrictParameter(parameter, originalType, blindScope, restriction, outcome);

    JSType actualType = result.getVariable(parameter.getIdentifierName()).getType();
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testRestrictParameterWithNullType() {
    JSType originalType = null;
    Node parameter = Node.newQualifiedName("test", "testParameter");
    JSType expectedType = null;
    boolean outcome = true;

    FlowScope blindScope = new FlowScope(JSType.UNKNOWN_TYPE);

    FlowScope result = interpreter.restrictParameter(parameter, originalType, blindScope, null, outcome);

    JSType actualType = result.getVariable(parameter.getIdentifierName()).getType();
    assertEquals(expectedType, actualType);
  }

  private void declareNameInScope(FlowScope scope, Node node, JSType type) {
    ObjectType objectType = new ObjectType("test", type, new ObjectType.Builder());
    scope.declareName(node, objectType);
  }
}