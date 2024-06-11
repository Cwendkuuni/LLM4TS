import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Visitor;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor.TypeRestriction;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private JSTypeRegistry typeRegistry;
  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setup() {
    JSTypeNative arrayType = new JSTypeNative(JSTypeNative.ARRAY_TYPE);
    JSTypeNative noObjectType = new JSTypeNative(JSTypeNative.NO_OBJECT_TYPE);
    JSTypeNative nullType = new JSTypeNative(JSTypeNative.NULL_TYPE);
    JSTypeNative objectType = new JSTypeNative(JSTypeNative.OBJECT_TYPE);

    typeRegistry = new JSTypeRegistry();
    typeRegistry.addNativeType(ARRAY_TYPE, arrayType);
    typeRegistry.addNativeType(NO_OBJECT_TYPE, noObjectType);
    typeRegistry.addNativeType(NULL_TYPE, nullType);
    typeRegistry.addNativeType(OBJECT_TYPE, objectType);

    interpreter = new ClosureReverseAbstractInterpreter(null, typeRegistry);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDefReturnsType() {
    Node condition = createNode("goog.isDef(x)");
    JSType expectedType = new ObjectType("x");
    FlowScope blindScope = mock(FlowScope.class);
    when(blindScope.createChildFlowScope()).thenReturn(mock(FlowScope.class));

    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true).asType();
    assertSame(expectedType, actualType);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isDefReturnsNull() {
    Node condition = createNode("goog.isDef(x)");
    FlowScope blindScope = mock(FlowScope.class);
    when(blindScope.createChildFlowScope()).thenReturn(mock(FlowScope.class));

    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false).asType();
    assertNull(actualType);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArray_true() {
    Node condition = createNode("goog.isArray([])");
    JSType expectedType = new ObjectType("[]");
    FlowScope blindScope = mock(FlowScope.class);

    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true).asType();
    assertSame(expectedType, actualType);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isArray_false() {
    Node condition = createNode("goog.isArray([])");
    FlowScope blindScope = mock(FlowScope.class);

    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false).asType();
    assertNull(actualType);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isString_true() {
    Node condition = createNode("goog.isString(\"hello\")");
    JSType expectedType = new ObjectType("\"hello\"");
    FlowScope blindScope = mock(FlowScope.class);

    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true).asType();
    assertSame(expectedType, actualType);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_isString_false() {
    Node condition = createNode("goog.isString(\"hello\")");
    FlowScope blindScope = mock(FlowScope.class);

    JSType actualType = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false).asType();
    assertNull(actualType);
  }

  private Node createNode(String nodeRepresentation) {
    Node node = new Node();
    node.setType(STRING);
    node.setString(nodeRepresentation);
    return node;
  }
}