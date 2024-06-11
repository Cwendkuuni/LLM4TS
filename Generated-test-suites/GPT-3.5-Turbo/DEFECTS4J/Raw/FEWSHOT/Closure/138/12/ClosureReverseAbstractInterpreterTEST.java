import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class ClosureReverseAbstractInterpreterTest {

  private ClosureReverseAbstractInterpreter interpreter;
  private Visitor<JSType> restrictToArrayVisitor;
  private Visitor<JSType> restrictToNotArrayVisitor;
  private Visitor<JSType> restrictToObjectVisitor;
  private Visitor<JSType> restrictToNotObjectVisitor;

  @Before
  public void setUp() {
    JSTypeRegistry typeRegistry = mock(JSTypeRegistry.class);
    restrictToArrayVisitor = mock(Visitor.class);
    restrictToNotArrayVisitor = mock(Visitor.class);
    restrictToObjectVisitor = mock(Visitor.class);
    restrictToNotObjectVisitor = mock(Visitor.class);
    interpreter = new ClosureReverseAbstractInterpreter(null, typeRegistry);
    interpreter.setRestrictToArrayVisitor(restrictToArrayVisitor);
    interpreter.setRestrictToNotArrayVisitor(restrictToNotArrayVisitor);
    interpreter.setRestrictToObjectVisitor(restrictToObjectVisitor);
    interpreter.setRestrictToNotObjectVisitor(restrictToNotObjectVisitor);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    Node condition = mock(Node.class);
    FlowScope blindScope = mock(FlowScope.class);
    boolean outcome = true;

    // Test processing when callee type is GETPROP and param is qualified name
    Node callee = mock(Node.class);
    Node param = mock(Node.class);
    Node left = mock(Node.class);
    Node right = mock(Node.class);
    JSType paramType = mock(JSType.class);
    Function<TypeRestriction, JSType> restricter = mock(Function.class);

    condition.getType();
    condition.getChildCount();
    condition.getFirstChild();
    condition.getLastChild();
    callee.getType();
    callee.getFirstChild();
    callee.getLastChild();
    left.getType();
    left.getString();
    right.getType();
    param.isQualifiedName();

    interpreter.getPreciserScopeKnowingConditionOutcome(
        condition, blindScope, outcome);

    // Assert statements
    //...
  }
}