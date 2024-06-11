import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Type;
import com.google.javascript.rhino.jstype.Visitor;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private static final JSTypeNative ARRAY_TYPE = new JSTypeNative("Array");
  private static final JSTypeNative NO_OBJECT_TYPE = new JSTypeNative("Object");
  private static final JSTypeNative NULL_TYPE = new JSTypeNative("Null");
  private static final JSTypeNative OBJECT_TYPE = new JSTypeNative("Object");

  private ClosureReverseAbstractInterpreter interpreter;
  private Function<Type, JSType> getNativeType;
  private Function<JSTypeRegistry, JSTypeRegistry> getTypeRegistry;
  private JSTypeRegistry typeRegistry;

  @Before
  public void setUp() {
    getNativeType = mock(Function.class);
    when(getNativeType.apply(any())).thenReturn(NULL_TYPE);
    when(getNativeType.apply(eq(ARRAY_TYPE.getName()))).thenReturn(ARRAY_TYPE);
    when(getNativeType.apply(eq(OBJECT_TYPE.getName()))).thenReturn(OBJECT_TYPE);

    getTypeRegistry = mock(Function.class);
    when(getTypeRegistry.apply(any())).thenReturn(typeRegistry);

    typeRegistry = mock(JSTypeRegistry.class);

    interpreter = new ClosureReverseAbstractInterpreter(
        new CodingConvention(), getTypeRegistry.apply(typeRegistry));
  }

  @Test
  public void testRestrictToArrayVisitor() {
    JSType type = NO_OBJECT_TYPE;
    JSType result = interpreter.restrictToArrayVisitor.visit(type);
    assertNull(result);

    type = ARRAY_TYPE;
    result = interpreter.restrictToArrayVisitor.visit(type);
    assertSame(type, result);
  }

  @Test
  public void testRestrictToNotArrayVisitor() {
    JSType type = ARRAY_TYPE;
    JSType result = interpreter.restrictToNotArrayVisitor.visit(type);
    assertNull(result);

    type = NO_OBJECT_TYPE;
    result = interpreter.restrictToNotArrayVisitor.visit(type);
    assertSame(type, result);
  }

  @Test
  public void testRestrictToObjectVisitor() {
    JSType type = ARRAY_TYPE;
    JSType result = interpreter.restrictToObjectVisitor.visit(type);
    assertNull(result);

    type = NO_OBJECT_TYPE;
    result = interpreter.restrictToObjectVisitor.visit(type);
    assertSame(type, result);

    type = OBJECT_TYPE;
    result = interpreter.restrictToObjectVisitor.visit(type);
    assertSame(type, result);

    type = new FunctionType(ObjectType.instance(), NO_OBJECT_TYPE);
    result = interpreter.restrictToObjectVisitor.visit(type);
    assertSame(type, result);
  }

  @Test
  public void testRestrictToNotObjectVisitor() {
    JSType type = OBJECT_TYPE;
    JSType result = interpreter.restrictToNotObjectVisitor.visit(type);
    assertNull(result);

    type = NO_OBJECT_TYPE;
    result = interpreter.restrictToNotObjectVisitor.visit(type);
    assertSame(type, result);

    type = ARRAY_TYPE;
    result = interpreter.restrictToNotObjectVisitor.visit(type);
    assertNull(result);

    type = new FunctionType(ObjectType.instance(), NO_OBJECT_TYPE);
    result = interpreter.restrictToNotObjectVisitor.visit(type);
    assertNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    Node condition = mock(Node.class);
    FlowScope blindScope = mock(FlowScope.class);
    when(condition.getType()).thenReturn(CALL);
    when(condition.getChildCount()).thenReturn(2);

    Node callee = mock(Node.class);
    Node param = mock(Node.class);
    when(callee.getFirstChild()).thenReturn(getNameNode("goog"));
    when(callee.getLastChild()).thenReturn(param);
    when(param.isQualifiedName()).thenReturn(true);
    when(param.getType()).thenReturn(NULL_TYPE);

    when(blindScope.createChildFlowScope()).thenReturn(mock(FlowScope.class));

    when(interpreter.getTypeIfRefinable(param, blindScope)).thenReturn(NULL_TYPE);

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true);
    assertNull(result);

    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, false);
    assertNull(result);

    when(param.getType()).thenReturn(OBJECT_TYPE);

    when(interpreter.restricters.get("isNull")).thenReturn(
        new Function<TypeRestriction, JSType>() {
          @Override
          public JSType apply(TypeRestriction p) {
            return p.outcome ? getNativeType(NULL_TYPE) : null;
          }
        });

    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true);
    assertSame(typeRegistry.getRootScope(), result);

    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, false);
    assertNull(result);
  }

  @Test
  public void testRestrictParameter() {
    Node parameter = mock(Node.class);
    JSType type = OBJECT_TYPE;
    FlowScope blindScope = mock(FlowScope.class);
    Function<TypeRestriction, JSType> restriction = mock(Function.class);
    boolean outcome = true;

    when(interpreter.restrictParameter(parameter, type, blindScope, restriction,
        outcome)).thenCallRealMethod();

    type = getNativeType(ARRAY_TYPE);
    outcome = true;
    interpreter.restrictToArrayVisitor.visit(type);
    interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
    assertSame(type, typeRegistry.getRootScope().getVariableType(parameter));

    type = getNativeType(OBJECT_TYPE);
    outcome = false;
    interpreter.restrictToObjectVisitor.visit(type);
    interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
    assertNull(typeRegistry.getRootScope().getVariableType(parameter));

    type = typeRegistry.getRootScope().getVariableType(parameter);
    when(restriction.apply(any())).thenReturn(typeRegistry.getRootScope().getVariableType(parameter));
    outcome = true;
    interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
    assertSame(type, typeRegistry.getRootScope().getVariableType(parameter));

    type = typeRegistry.getRootScope().getVariableType(parameter);
    when(restriction.apply(any()))
        .thenReturn(new ObjectType("test", ImmutableMap.of("x", type)));
    outcome = false;
    interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
    assertNull(typeRegistry.getRootScope().getVariableType(parameter));
  }

  private Node getNameNode(String name) {
    return new Node(new ObjectType("name", ImmutableMap.of("name", new StringType(name))),
        NAME, name);
  }
}