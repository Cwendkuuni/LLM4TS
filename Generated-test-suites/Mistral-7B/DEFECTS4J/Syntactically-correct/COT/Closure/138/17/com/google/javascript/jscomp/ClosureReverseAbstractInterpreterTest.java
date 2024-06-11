import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Function;
import com.google.javascript.jscomp.ChainableReverseAbstractInterpreter;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Visitor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class ClosureReverseAbstractInterpreterTest {
  private JSTypeRegistry typeRegistry;
  private CodingConvention codingConvention;
  private ChainableReverseAbstractInterpreter interpreter;

  @Before
  public void setup() {
    typeRegistry = mock(JSTypeRegistry.class);
    codingConvention = mock(CodingConvention.class);
    interpreter = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_true() {
    Node condition = createMockNode("goog.isDef(x)");
    JSType expectedType = createMockType(JSType.OBJECT_TYPE);
    when(typeRegistry.getNativeType(JSTypeNative.OBJECT_TYPE)).thenReturn(expectedType);

    FlowScope blindScope = Mockito.mock(FlowScope.class);

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertNotNull(result);
    ArgumentCaptor<Node> parameterCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<JSType> typeCaptor = ArgumentCaptor.forClass(JSType.class);
    ArgumentCaptor<String> restrictionNameCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Boolean> restrictionOutcomeCaptor = ArgumentCaptor.forClass(Boolean.class);
    Mockito.verify(blindScope).declareNameInScope(Mockito.eq(result), parameterCaptor.capture(), typeCaptor.capture(), Mockito.eq(restrictionByName("isDef")), Mockito.eq(true));
    JSType actualType = typeCaptor.getValue();
    assertEquals(expectedType, actualType);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_false() {
    Node condition = createMockNode("goog.isDef(x)");

    FlowScope blindScope = Mockito.mock(FlowScope.class);

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(result);
  }

  @Test
  public void testRestrictParameter_true() {
    Node parameter = createMockNode("x");
    JSType initialType = createMockType(JSType.OBJECT_TYPE);
    JSType expectedType = createMockType(JSType.ARRAY_TYPE);

    FlowScope blindScope = Mockito.mock(FlowScope.class);

    Function<TypeRestriction, JSType> restriction = mock(Function.class);
    when(restriction.apply(Mockito.any(TypeRestriction.class))).thenReturn(expectedType);

    FlowScope result = interpreter.restrictParameter(parameter, initialType, blindScope, restriction, true);

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<JSType> typeCaptor = ArgumentCaptor.forClass(JSType.class);
    ArgumentCaptor<FlowScope> scopeCaptor = ArgumentCaptor.forClass(FlowScope.class);
    ArgumentCaptor<Function<TypeRestriction, JSType>> restricterCaptor = ArgumentCaptor.forClass(Function.class);
    ArgumentCaptor<Boolean> outcomeCaptor = ArgumentCaptor.forClass(Boolean.class);
    Mockito.verify(blindScope).declareNameInScope(Mockito.eq(result), nodeCaptor.capture(), typeCaptor.capture(), restricterCaptor.capture(), outcomeCaptor.capture());

    Node actualParameter = nodeCaptor.getValue();
    JSType actualType = typeCaptor.getValue();
    Function<TypeRestriction, JSType> actualRestriction = restricterCaptor.getValue();
    Boolean actualOutcome = outcomeCaptor.getValue();

    assertEquals(parameter, actualParameter);
    assertEquals(expectedType, actualType);
    assertEquals(restriction, actualRestriction);
    assertEquals(true, actualOutcome);
  }

  @Test
  public void testRestrictParameter_false() {
    Node parameter = createMockNode("x");
    JSType initialType = createMockType(JSType.OBJECT_TYPE);

    FlowScope blindScope = Mockito.mock(FlowScope.class);

    Function<TypeRestriction, JSType> restriction = mock(Function.class);

    FlowScope result = interpreter.restrictParameter(parameter, initialType, blindScope, restriction, false);

    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    ArgumentCaptor<JSType> typeCaptor = ArgumentCaptor.forClass(JSType.class);
    ArgumentCaptor<FlowScope> scopeCaptor = ArgumentCaptor.forClass(FlowScope.class);
    ArgumentCaptor<Function<TypeRestriction, JSType>> restricterCaptor = ArgumentCaptor.forClass(Function.class);
    ArgumentCaptor<Boolean> outcomeCaptor = ArgumentCaptor.forClass(Boolean.class);
    Mockito.verifyNoMoreInteractions(blindScope);

    Node actualParameter = nodeCaptor.getValue();
    JSType actualType = typeCaptor.getValue();
    Function<TypeRestriction, JSType> actualRestriction = restricterCaptor.getValue();
    Boolean actualOutcome = outcomeCaptor.getValue();

    assertEquals(parameter, actualParameter);
    assertNull(actualType);
    assertEquals(restriction, actualRestriction);
    assertEquals(false, actualOutcome);
  }

  private Node createMockNode(String expression) {
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(Node.CALL_TYPE);
    when(node.getLastChild()).thenReturn(createMockStringNode(expression));
    when(node.getFirstChild()).thenReturn(createMockNameNode("goog"));
    return node;
  }

  private Node createMockNameNode(String name) {
    Node node = mock(Node.class);
    when(node.getString()).thenReturn(name);
    return node;
  }

  private Node createMockStringNode(String value) {
    Node node = mock(Node.class);
    when(node.getType()).thenReturn(STRING);
    when(node.getString()).thenReturn(value);
    return node;
  }

  private JSType createMockType(Class<?> clazz) {
    JSType type = mock(JSType.class);
    when(typeRegistry.getNativeType(clazz)).thenReturn(type);
    return type;
  }

  private Function<TypeRestriction, JSType> restrictionByName(String name) {
    return new Function<TypeRestriction, JSType>() {
      @Override
      public JSType apply(TypeRestriction p) {
        if ("isDef".equals(name)) {
          return new TypeRestriction(p.type, p.outcome) {}.apply(interpreter.restricters.get(name));
        }
        throw new UnsupportedOperationException("Unrecognized restriction name: " + name);
      }
    };
  }
}