import static org.junit.Assert.*;
import static com.google.javascript.rhino.Token.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Visitor;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
  

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = new Node(CALL, 2);
    condition.setFirstChild(new Node(GETPROP, 1));
    condition.getFirstChild().setFirstChild(new Node(NAME, "goog"));
    condition.getFirstChild().setLastChild(new Node(STRING, "isDef"));
    condition.setLastChild(new Node(STRING, "true"));

    FlowScope blindScope = new FlowScope();

    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, true);

    JSType expectedType = interpreter.getNativeType(JSTypeNative.Object_TYPE);
    expectedType = interpreter.restrictToObjectVisitor.visit(expectedType);

    assertEquals(expectedType, result.getVariable("true").getType());

    condition.setLastChild(new Node(STRING, "false"));

    result = interpreter.getPreciserScopeKnowingConditionOutcome(condition,
        blindScope, false);

    assertNull(result.getVariable("true"));
  }

  @Test
  public void testRestrictParameter() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node parameter = new Node(GETPROP, 1);
    parameter.setFirstChild(new Node(NAME, "type"));

    JSType initialType = getNativeType(Object_TYPE);
    JSType restrictedType = interpreter.getNativeType(NO_OBJECT_TYPE);

    TypeRestriction typeRestriction = new TypeRestriction(initialType, true);

    FlowScope blindScope = new FlowScope();
    FlowScope result = interpreter.restrictParameter(parameter, initialType,
        blindScope, interpreter.restrictToNotObjectVisitor, true);

    assertNull(result.getVariable("type"));

    initialType = interpreter.getNativeType(ARRAY_TYPE);
    typeRestriction = new TypeRestriction(initialType, false);

    result = interpreter.restrictParameter(parameter, initialType,
        blindScope, interpreter.restrictToArrayVisitor, false);

    assertEquals(initialType, result.getVariable("type").getType());
  }

  @Test
  public void testIsDef() {
    CodingConvention convention = new CodingConvention();
    JSTypeRegistry typeRegistry = new JSTypeRegistry();
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        convention, typeRegistry);

    Node condition = new Node(CALL, 2);
    condition.setFirstChild(new Node(GETPROP, 1));
    condition.getFirstChild().setFirstChild(new Node(NAME, "goog"));
    condition.getFirstChild().setLastChild(new Node(STRING, "isDef"));
    condition.setLastChild(new Node(STRING, "true"));

    JSType expectedType = interpreter.getNativeType(Object_TYPE);
    expectedType = interpreter.restrictToObjectVisitor.visit(expectedType);

    FlowScope blindScope = new FlowScope();
    interpreter.restrictParameter(new Node(GETPROP, 0), getNativeType(Object_TYPE),
        blindScope, interpreter.restrictToDefVisitor(), true);

    TypeRestriction typeRestriction = new TypeRestriction(expectedType, true);

    Node testedType = interpreter.restrictParameter(new Node(GETPROP, 0),
        getNativeType(Object_TYPE), blindScope, interpreter.restrictToDefVisitor(),
        false).getVariable("true").getType();

    assertNull(testedType);

    testedType = interpreter.restrictParameter(new Node(GETPROP, 0), expectedType,
        blindScope, interpreter.restrictToDefVisitor(), false).getVariable("true")
        .getType();

    assertEquals(expectedType, testedType);
  }

  
}