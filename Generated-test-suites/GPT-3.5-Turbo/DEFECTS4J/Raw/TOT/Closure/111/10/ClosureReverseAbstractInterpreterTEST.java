import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    // Typical Use Case - Call to goog.isDef with outcome true
    Node condition = mock(Node.class);
    when(condition.isCall()).thenReturn(true);
    when(condition.getChildCount()).thenReturn(2);
    Node callee = mock(Node.class);
    Node param = mock(Node.class);
    when(condition.getFirstChild()).thenReturn(callee);
    when(condition.getLastChild()).thenReturn(param);
    when(callee.isGetProp()).thenReturn(true);
    when(param.isQualifiedName()).thenReturn(true);
    Node left = mock(Node.class);
    Node string = mock(Node.class);
    when(callee.getFirstChild()).thenReturn(left);
    when(callee.getLastChild()).thenReturn(string);
    when(left.isName()).thenReturn(true);
    when(left.getString()).thenReturn("goog");
    when(string.isString()).thenReturn(true);
    when(string.getString()).thenReturn("isDef");

    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    // Verify that the restrictParameter method is called with the correct parameters
    verify(interpreter, times(1)).restrictParameter(eq(param), any(JSType.class), eq(blindScope),
      any(Function.class), eq(true));
  }

  @Test
  public void testRestrictParameter() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();

    // Edge Case - parameter type is null, outcome is true
    Node parameter = mock(Node.class);
    JSType type = null;
    FlowScope blindScope = mock(FlowScope.class);
    Function<TypeRestriction, JSType> restriction = mock(Function.class);
    boolean outcome = true;

    FlowScope result = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);

    // Verify that FlowScope is not changed when parameter type is null
    Assert.assertEquals(blindScope, result);

    // Typical Use Case - parameter type is not null, outcome is true
    parameter = mock(Node.class);
    type = mock(JSType.class);
    restriction = mock(Function.class);

    result = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);

    // Verify that the declareNameInScope method is called with the correct parameters
    verify(interpreter, times(1)).declareNameInScope(any(FlowScope.class), eq(parameter), eq(type));
  }
}