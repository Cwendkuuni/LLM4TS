import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.FlowScope;
import com.google.javascript.rhino.NodeOperations;

public class ClosureReverseAbstractInterpreterTest {

  @org.junit.Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);

    Node conditionTrue = NodeOperations.createCall(NAME, "goog", "isDef", null,
        NodeOperations.createString("someVariable"));
    Node conditionFalse = NodeOperations.createCall(NAME, "goog", "isDef", null,
        NodeOperations.createNull());
    Node conditionNull = NodeOperations.createCall(NAME, "goog", "isDef", null,
        NodeOperations.createNull());
    Node conditionInvalidFunction = NodeOperations.createCall(NAME, "goog", "invalidFunction", null,
        NodeOperations.createString("someVariable"));
    Node conditionNotQualifiedName = NodeOperations.createCall(NAME, "goog", "isDef", null,
        NodeOperations.createCall(NAME, "invalid", "someVariable"));

    FlowScope blindScope = mock(FlowScope.class);

    when(blindScope.createChildFlowScope()).thenReturn(mock(FlowScope.class));

    FlowScope resultTrue = interpreter.getPreciserScopeKnowingConditionOutcome(conditionTrue,
        blindScope, true);
    assertNotNull(resultTrue);

    FlowScope resultFalse = interpreter.getPreciserScopeKnowingConditionOutcome(conditionFalse,
        blindScope, true);
    assertNull(resultFalse);

    FlowScope resultNull = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNull,
        blindScope, true);
    assertNull(resultNull);

    FlowScope resultInvalidFunction = interpreter.getPreciserScopeKnowingConditionOutcome(conditionInvalidFunction,
        blindScope, true);
    assertNull(resultInvalidFunction);

    FlowScope resultNotQualifiedName = interpreter.getPreciserScopeKnowingConditionOutcome(conditionNotQualifiedName,
        blindScope, true);
    assertNull(resultNotQualifiedName);
  }

  @org.junit.Test
  public void testGetPreciserScopeKnowingConditionOutcomeWithTwoArguments() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);

    Node condition = NodeOperations.createCall(NAME, "goog", "isDef", null,
        NodeOperations.createString("someVariable"), NodeOperations.createString("arg2"));

    FlowScope blindScope = mock(FlowScope.class);

    when(blindScope.createChildFlowScope()).thenReturn(mock(FlowScope.class));

    try {
      interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }

  @org.junit.Test
  public void testGetPreciserScopeKnowingConditionOutcomeWithInvalidCallee() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);

    Node condition = NodeOperations.createCall(NAME, "goog", "invalidCallee", null,
        NodeOperations.createString("someVariable"));

    FlowScope blindScope = mock(FlowScope.class);

    when(blindScope.createChildFlowScope()).thenReturn(mock(FlowScope.class));

    try {
      interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected
    }
  }
}