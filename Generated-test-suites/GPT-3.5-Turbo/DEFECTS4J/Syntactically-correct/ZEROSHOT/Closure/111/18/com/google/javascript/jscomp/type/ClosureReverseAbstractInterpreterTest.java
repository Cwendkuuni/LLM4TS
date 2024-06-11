import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;
import org.junit.Before;
import org.junit.Test;
import static com.google.javascript.rhino.jstype.JSTypeNative.ARRAY_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.NO_OBJECT_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.NULL_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.NULL_VOID;
import static com.google.javascript.rhino.jstype.JSTypeNative.NUMBER_STRING_BOOLEAN;
import static com.google.javascript.rhino.jstype.JSTypeNative.OBJECT_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.VOID_TYPE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ClosureReverseAbstractInterpreterTest {

  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setUp() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    Node condition = new Node();
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;

    FlowScope resultScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

    assertEquals(blindScope, resultScope);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome2() {
    Node condition = new Node();
    FlowScope blindScope = new FlowScope();
    boolean outcome = false;

    FlowScope resultScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

    assertEquals(blindScope, resultScope);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome3() {
    Node condition = new Node();
    condition.addChildToBack(new Node(Token.NAME, "goog"));
    condition.getFirstChild().addChildToBack(new Node(Token.STRING, "isDef"));
    Node param = new Node(Token.NAME, "param");
    condition.addChildToBack(param);
    FlowScope blindScope = new FlowScope();
    blindScope.createChildFlowScope();
    boolean outcome = true;

    FlowScope resultScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

    // Asserts go here
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome4() {
    Node condition = new Node();
    condition.addChildToBack(new Node(Token.NAME, "goog"));
    condition.getFirstChild().addChildToBack(new Node(Token.STRING, "isDef"));
    Node param = new Node(Token.NAME, "param");
    condition.addChildToBack(param);
    FlowScope blindScope = new FlowScope();
    blindScope.createChildFlowScope();
    boolean outcome = false;

    FlowScope resultScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

    // Asserts go here
  }

  @Test
  public void testRestrictParameter() {
    Node parameter = new Node();
    JSType type = new JSType();
    FlowScope blindScope = new FlowScope();
    Function<TypeRestriction, JSType> restriction = new Function<TypeRestriction, JSType>() {
      @Override
      public JSType apply(TypeRestriction p) {
        return null;
      }
    };
    boolean outcome = true;

    FlowScope resultScope = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);

    assertEquals(blindScope, resultScope);
  }

  @Test
  public void testRestrictParameter2() {
    Node parameter = new Node();
    JSType type = new JSType();
    FlowScope blindScope = new FlowScope();
    Function<TypeRestriction, JSType> restriction = new Function<TypeRestriction, JSType>() {
      @Override
      public JSType apply(TypeRestriction p) {
        return new JSType();
      }
    };
    boolean outcome = true;

    FlowScope resultScope = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);

    // Asserts go here
  }

  @Test
  public void testRestrictParameter3() {
    Node parameter = new Node();
    JSType type = null;
    FlowScope blindScope = new FlowScope();
    Function<TypeRestriction, JSType> restriction = new Function<TypeRestriction, JSType>() {
      @Override
      public JSType apply(TypeRestriction p) {
        return null;
      }
    };
    boolean outcome = true;

    FlowScope resultScope = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);

    assertEquals(blindScope, resultScope);
  }
}