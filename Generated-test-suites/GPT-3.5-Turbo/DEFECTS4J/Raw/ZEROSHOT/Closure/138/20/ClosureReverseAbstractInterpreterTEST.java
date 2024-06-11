import org.junit.Test;
import java.util.Map;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;
import com.google.javascript.rhino.Node;

import static org.junit.Assert.*;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testRestrictToArrayVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    Node node = new Node();
    JSType jsType = interpreter.restrictToArrayVisitor.visit(node);
    assertNull(jsType);
  }

  @Test
  public void testRestrictToNotArrayVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    Node node = new Node();
    JSType jsType = interpreter.restrictToNotArrayVisitor.visit(node);
    assertNull(jsType);
  }

  @Test
  public void testRestrictToObjectVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    Node node = new Node();
    JSType jsType = interpreter.restrictToObjectVisitor.visit(node);
    assertNull(jsType);
  }

  @Test
  public void testRestrictToNotObjectVisitor() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    Node node = new Node();
    JSType jsType = interpreter.restrictToNotObjectVisitor.visit(node);
    assertNull(jsType);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    Node condition = new Node();
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;
    FlowScope flowScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    assertNotNull(flowScope);
  }

  @Test
  public void testRestrictParameter() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter();
    Node parameter = new Node();
    JSType type = new JSType();
    FlowScope blindScope = new FlowScope();
    Function<TypeRestriction, JSType> restriction = new Function<TypeRestriction, JSType>() {
      @Override
      public JSType apply(TypeRestriction typeRestriction) {
        return null;
      }
    };
    boolean outcome = true;
    FlowScope flowScope = interpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
    assertNotNull(flowScope);
  }

  @Test
  public void testTypeRestrictionConstructor() {
    ClosureReverseAbstractInterpreter.TypeRestriction typeRestriction = new ClosureReverseAbstractInterpreter.TypeRestriction(new JSType(), true);
    assertNotNull(typeRestriction);
  }
}