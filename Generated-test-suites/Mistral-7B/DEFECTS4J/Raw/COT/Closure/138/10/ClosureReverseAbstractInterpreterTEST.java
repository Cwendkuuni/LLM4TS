import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.FlowScope;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Visitor;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private JSTypeRegistry typeRegistry;
  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setup() {
    typeRegistry = mock(JSTypeRegistry.class);
    CodingConvention convention = mock(CodingConvention.class);
    interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef_true() {
    Node condition = createNode("goog.isDef(x)");
    JSType expectedType = getNativeType(ObjectType.class);
    when(typeRegistry.getType(NodeType.OBJECT)).thenReturn(expectedType);

    FlowScope blindScope = createMockFlowScope();
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    assertNotNull(result);
    assertNotNull(result.getVariable("x"));
    assertEquals(expectedType, result.getVariable("x").getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDef_false() {
    Node condition = createNode("goog.isDef(x)");
    when(typeRegistry.getType(NodeType.OBJECT)).thenReturn(getNativeType(ObjectType.class));

    FlowScope blindScope = createMockFlowScope();

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);

    assertNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArray_true() {
    Node condition = createNode("goog.isArray(x)");
    when(typeRegistry.getType(NodeType.OBJECT)).thenReturn(getNativeType(ObjectType.class));

    FlowScope blindScope = createMockFlowScope();

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    assertNotNull(result);
    assertNotNull(result.getVariable("x"));
    assertEquals(getNativeType(ArrayType.class), result.getVariable("x").getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArray_false() {
    Node condition = createNode("goog.isArray(x)");
    when(typeRegistry.getType(NodeType.OBJECT)).thenReturn(getNativeType(ObjectType.class));

    FlowScope blindScope = createMockFlowScope();

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);

    assertNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isObject_true() {
    Node condition = createNode("goog.isObject(x)");
    when(typeRegistry.getType(NodeType.OBJECT)).thenReturn(getNativeType(ObjectType.class));

    FlowScope blindScope = createMockFlowScope();

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    assertNotNull(result);
    assertNotNull(result.getVariable("x"));
    assertEquals(getNativeType(ObjectType.class), result.getVariable("x").getType());
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isObject_false() {
    Node condition = createNode("goog.isObject(x)");
    when(typeRegistry.getType(NodeType.OBJECT)).thenReturn(getNativeType(ArrayType.class));

    FlowScope blindScope = createMockFlowScope();

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);

    assertNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_invalidCondition() {
    Node condition = createNode("invalidCondition");
    FlowScope blindScope = createMockFlowScope();

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    assertNull(result);
  }

  private Node createNode(String nodeString) {
    Node node = mock(Node.class);
    when(node.getString()).thenReturn(nodeString);
    return node;
  }

  private FlowScope createMockFlowScope() {
    FlowScope flowScope = mock(FlowScope.class);
    when(flowScope.createChildFlowScope()).thenReturn(flowScope);
    return flowScope;
  }

  private JSType getNativeType(JSTypeType type) {
    return typeRegistry.getType(type);
  }

  private JSType getNativeType(int typeCode) {
    return getNativeType(JSType.getType(typeCode));
  }
}