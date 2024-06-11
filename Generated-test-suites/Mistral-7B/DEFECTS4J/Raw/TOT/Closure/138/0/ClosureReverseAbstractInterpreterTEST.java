import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.ClosureReverseAbstractInterpreter;
import com.google.javascript.jscomp.CodingConvention;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;

import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private ClosureReverseAbstractInterpreter interpreter;
  private JSTypeRegistry typeRegistry;
  private CodingConvention convention;

  @Before
  public void setUp() {
    typeRegistry = mock(JSTypeRegistry.class);
    convention = new CodingConvention();
    interpreter = new ClosureReverseAbstractInterpreter(convention, typeRegistry);
  }

  @Test
  public void testRestrictToArray() {
    Node condition = Node.newCall(Node.newGetProp(Node.newName("goog"),
        "isDef"), Node.newUndefined(), Node.newString("array"));

    Node blindScope = Node.newBlock(Node.newPass());

    // Test when goog.isDef("array") returns true
    when(interpreter.getTypeIfRefinable(condition.getLastChild(), blindScope))
        .thenReturn(JSType.ARRAY_TYPE);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType restrictedType = informedScope.getDeclaredVariable("array").getType();
    assertNotNull(restrictedType);
    assertNotNull(restrictedType.isArray());

    // Test when goog.isDef("array") returns false
    when(interpreter.getTypeIfRefinable(condition.getLastChild(), blindScope))
        .thenReturn(null);
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    assertNull(informedScope.getDeclaredVariable("array"));
  }

  @Test
  public void testRestrictToNotArray() {
    Node condition = Node.newCall(Node.newGetProp(Node.newName("goog"),
        "isDef"), Node.newUndefined(), Node.newString("notArray"));

    Node blindScope = Node.newBlock(Node.newPass());

    // Test when goog.isDef("notArray") returns true
    when(interpreter.getTypeIfRefinable(condition.getLastChild(), blindScope))
        .thenReturn(JSType.OBJECT_TYPE);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType restrictedType = informedScope.getDeclaredVariable("notArray").getType();
    assertNotNull(restrictedType);
    assertFalse(restrictedType.isArray());

    // Test when goog.isDef("notArray") returns false
    when(interpreter.getTypeIfRefinable(condition.getLastChild(), blindScope))
        .thenReturn(JSType.ARRAY_TYPE);
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(informedScope.getDeclaredVariable("notArray"));
  }

  @Test
  public void testRestrictToObject() {
    Node condition = Node.newCall(Node.newGetProp(Node.newName("goog"),
        "isObject"), Node.newUndefined(), Node.newString("objectFunction"));

    Node blindScope = Node.newBlock(Node.newPass());

    // Test when goog.isObject("objectFunction") returns true
    when(interpreter.getTypeIfRefinable(condition.getLastChild(), blindScope))
        .thenReturn(JSType.FUNCTION_TYPE);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType restrictedType = informedScope.getDeclaredVariable("objectFunction").getType();
    assertNotNull(restrictedType);
    assertTrue(restrictedType instanceof FunctionType);

    // Test when goog.isObject("objectFunction") returns false
    when(interpreter.getTypeIfRefinable(condition.getLastChild(), blindScope))
        .thenReturn(JSType.OBJECT_TYPE);
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(informedScope.getDeclaredVariable("objectFunction"));
  }

  @Test
  public void testRestrictToNotObject() {
    Node condition = Node.newCall(Node.newGetProp(Node.newName("goog"),
        "isObject"), Node.newUndefined(), Node.newString("notObjectFunction"));

    Node blindScope = Node.newBlock(Node.newPass());

    // Test when goog.isObject("notObjectFunction") returns true
    when(interpreter.getTypeIfRefinable(condition.getLastChild(), blindScope))
        .thenReturn(JSType.OBJECT_TYPE);
    FlowScope informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);
    JSType restrictedType = informedScope.getDeclaredVariable("notObjectFunction").getType();
    assertNotNull(restrictedType);
    assertTrue(restrictedType instanceof ObjectType);

    // Test when goog.isObject("notObjectFunction") returns false
    when(interpreter.getTypeIfRefinable(condition.getLastChild(), blindScope))
        .thenReturn(JSType.FUNCTION_TYPE);
    informedScope = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, false);
    assertNull(informedScope.getDeclaredVariable("notObjectFunction"));
  }
}