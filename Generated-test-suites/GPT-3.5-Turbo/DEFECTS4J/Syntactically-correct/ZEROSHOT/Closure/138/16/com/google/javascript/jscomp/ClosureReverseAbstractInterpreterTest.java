import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private ClosureReverseAbstractInterpreter interpreter;

  @Before
  public void setUp() {
    interpreter = new ClosureReverseAbstractInterpreter();
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withIsDef() {
    // Arrange
    Node condition = new Node();
    condition.setType(Token.CALL);
    condition.setChildCount(2);
    Node callee = new Node();
    callee.setType(Token.GETPROP);
    Node param = new Node();
    param.setQualifiedName(true);
    callee.addChildToBack(param);
    Node left = new Node();
    left.setType(Token.NAME);
    left.setString("goog");
    Node right = new Node();
    right.setType(Token.STRING);
    right.setString("isDef");
    callee.addChildToFront(left);
    callee.addChildToBack(right);
    condition.addChildToFront(callee);
    condition.addChildToBack(param);
    FlowScope blindScope = new FlowScope();

    // Act
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    // Assert
    assertNotNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withIsNull() {
    // Arrange
    Node condition = new Node();
    condition.setType(Token.CALL);
    condition.setChildCount(2);
    Node callee = new Node();
    callee.setType(Token.GETPROP);
    Node param = new Node();
    param.setQualifiedName(true);
    callee.addChildToBack(param);
    Node left = new Node();
    left.setType(Token.NAME);
    left.setString("goog");
    Node right = new Node();
    right.setType(Token.STRING);
    right.setString("isNull");
    callee.addChildToFront(left);
    callee.addChildToBack(right);
    condition.addChildToFront(callee);
    condition.addChildToBack(param);
    FlowScope blindScope = new FlowScope();

    // Act
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    // Assert
    assertNotNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withIsDefAndNotNull() {
    // Arrange
    Node condition = new Node();
    condition.setType(Token.CALL);
    condition.setChildCount(2);
    Node callee = new Node();
    callee.setType(Token.GETPROP);
    Node param = new Node();
    param.setQualifiedName(true);
    callee.addChildToBack(param);
    Node left = new Node();
    left.setType(Token.NAME);
    left.setString("goog");
    Node right = new Node();
    right.setType(Token.STRING);
    right.setString("isDefAndNotNull");
    callee.addChildToFront(left);
    callee.addChildToBack(right);
    condition.addChildToFront(callee);
    condition.addChildToBack(param);
    FlowScope blindScope = new FlowScope();

    // Act
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    // Assert
    assertNotNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withIsString() {
    // Arrange
    Node condition = new Node();
    condition.setType(Token.CALL);
    condition.setChildCount(2);
    Node callee = new Node();
    callee.setType(Token.GETPROP);
    Node param = new Node();
    param.setQualifiedName(true);
    callee.addChildToBack(param);
    Node left = new Node();
    left.setType(Token.NAME);
    left.setString("goog");
    Node right = new Node();
    right.setType(Token.STRING);
    right.setString("isString");
    callee.addChildToFront(left);
    callee.addChildToBack(right);
    condition.addChildToFront(callee);
    condition.addChildToBack(param);
    FlowScope blindScope = new FlowScope();

    // Act
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    // Assert
    assertNotNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withIsBoolean() {
    // Arrange
    Node condition = new Node();
    condition.setType(Token.CALL);
    condition.setChildCount(2);
    Node callee = new Node();
    callee.setType(Token.GETPROP);
    Node param = new Node();
    param.setQualifiedName(true);
    callee.addChildToBack(param);
    Node left = new Node();
    left.setType(Token.NAME);
    left.setString("goog");
    Node right = new Node();
    right.setType(Token.STRING);
    right.setString("isBoolean");
    callee.addChildToFront(left);
    callee.addChildToBack(right);
    condition.addChildToFront(callee);
    condition.addChildToBack(param);
    FlowScope blindScope = new FlowScope();

    // Act
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    // Assert
    assertNotNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withIsNumber() {
    // Arrange
    Node condition = new Node();
    condition.setType(Token.CALL);
    condition.setChildCount(2);
    Node callee = new Node();
    callee.setType(Token.GETPROP);
    Node param = new Node();
    param.setQualifiedName(true);
    callee.addChildToBack(param);
    Node left = new Node();
    left.setType(Token.NAME);
    left.setString("goog");
    Node right = new Node();
    right.setType(Token.STRING);
    right.setString("isNumber");
    callee.addChildToFront(left);
    callee.addChildToBack(right);
    condition.addChildToFront(callee);
    condition.addChildToBack(param);
    FlowScope blindScope = new FlowScope();

    // Act
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    // Assert
    assertNotNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withIsFunction() {
    // Arrange
    Node condition = new Node();
    condition.setType(Token.CALL);
    condition.setChildCount(2);
    Node callee = new Node();
    callee.setType(Token.GETPROP);
    Node param = new Node();
    param.setQualifiedName(true);
    callee.addChildToBack(param);
    Node left = new Node();
    left.setType(Token.NAME);
    left.setString("goog");
    Node right = new Node();
    right.setType(Token.STRING);
    right.setString("isFunction");
    callee.addChildToFront(left);
    callee.addChildToBack(right);
    condition.addChildToFront(callee);
    condition.addChildToBack(param);
    FlowScope blindScope = new FlowScope();

    // Act
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    // Assert
    assertNotNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withIsArray() {
    // Arrange
    Node condition = new Node();
    condition.setType(Token.CALL);
    condition.setChildCount(2);
    Node callee = new Node();
    callee.setType(Token.GETPROP);
    Node param = new Node();
    param.setQualifiedName(true);
    callee.addChildToBack(param);
    Node left = new Node();
    left.setType(Token.NAME);
    left.setString("goog");
    Node right = new Node();
    right.setType(Token.STRING);
    right.setString("isArray");
    callee.addChildToFront(left);
    callee.addChildToBack(right);
    condition.addChildToFront(callee);
    condition.addChildToBack(param);
    FlowScope blindScope = new FlowScope();

    // Act
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    // Assert
    assertNotNull(result);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withIsObject() {
    // Arrange
    Node condition = new Node();
    condition.setType(Token.CALL);
    condition.setChildCount(2);
    Node callee = new Node();
    callee.setType(Token.GETPROP);
    Node param = new Node();
    param.setQualifiedName(true);
    callee.addChildToBack(param);
    Node left = new Node();
    left.setType(Token.NAME);
    left.setString("goog");
    Node right = new Node();
    right.setType(Token.STRING);
    right.setString("isObject");
    callee.addChildToFront(left);
    callee.addChildToBack(right);
    condition.addChildToFront(callee);
    condition.addChildToBack(param);
    FlowScope blindScope = new FlowScope();

    // Act
    FlowScope result = interpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, true);

    // Assert
    assertNotNull(result);
  }
}