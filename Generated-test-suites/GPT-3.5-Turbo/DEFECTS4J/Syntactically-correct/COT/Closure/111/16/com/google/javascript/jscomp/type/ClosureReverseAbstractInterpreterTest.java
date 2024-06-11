
import static org.junit.Assert.*;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {
  
  private ClosureReverseAbstractInterpreter interpreter;
  
  @Before
  public void setUp() {
    interpreter = new ClosureReverseAbstractInterpreter(null, new JSTypeRegistry());
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withIsDefReturnTrue() {
    Node condition = new Node(
      new Node(
        Node.newString(Token.NAME, "goog"),
        Node.newString(Token.STRING, "isDef")
      ),
      Node.newName("param")
    );
    assertTrue(interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true));
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withIsDefReturnFalse() {
    Node condition = new Node(
      new Node(
        Node.newString(Token.NAME, "goog"),
        Node.newString(Token.STRING, "isDef")
      ),
      Node.newName("param")
    );
    assertFalse(interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false));
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withIsNullReturnTrue() {
    Node condition = new Node(
      new Node(
        Node.newString(Token.NAME, "goog"),
        Node.newString(Token.STRING, "isNull")
      ),
      Node.newName("param")
    );
    assertTrue(interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true));
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withIsNullReturnFalse() {
    Node condition = new Node(
      new Node(
        Node.newString(Token.NAME, "goog"),
        Node.newString(Token.STRING, "isNull")
      ),
      Node.newName("param")
    );
    assertFalse(interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false));
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withIsDefAndNotNullReturnTrue() {
    Node condition = new Node(
      new Node(
        Node.newString(Token.NAME, "goog"),
        Node.newString(Token.STRING, "isDefAndNotNull")
      ),
      Node.newName("param")
    );
    assertTrue(interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true));
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withIsDefAndNotNullReturnFalse() {
    Node condition = new Node(
      new Node(
        Node.newString(Token.NAME, "goog"),
        Node.newString(Token.STRING, "isDefAndNotNull")
      ),
      Node.newName("param")
    );
    assertFalse(interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false));
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withIsStringReturnTrue() {
    Node condition = new Node(
      new Node(
        Node.newString(Token.NAME, "goog"),
        Node.newString(Token.STRING, "isString")
      ),
      Node.newName("param")
    );
    assertTrue(interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true));
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withIsStringReturnFalse() {
    Node condition = new Node(
      new Node(
        Node.newString(Token.NAME, "goog"),
        Node.newString(Token.STRING, "isString")
      ),
      Node.newName("param")
    );
    assertFalse(interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false));
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withIsBooleanReturnTrue() {
    Node condition = new Node(
      new Node(
        Node.newString(Token.NAME, "goog"),
        Node.newString(Token.STRING, "isBoolean")
      ),
      Node.newName("param")
    );
    assertTrue(interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true));
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withIsBooleanReturnFalse() {
    Node condition = new Node(
      new Node(
        Node.newString(Token.NAME, "goog"),
        Node.newString(Token.STRING, "isBoolean")
      ),
      Node.newName("param")
    );
    assertFalse(interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false));
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withIsNumberReturnTrue() {
    Node condition = new Node(
      new Node(
        Node.newString(Token.NAME, "goog"),
        Node.newString(Token.STRING, "isNumber")
      ),
      Node.newName("param")
    );
    assertTrue(interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true));
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withIsNumberReturnFalse() {
    Node condition = new Node(
      new Node(
        Node.newString(Token.NAME, "goog"),
        Node.newString(Token.STRING, "isNumber")
      ),
      Node.newName("param")
    );
    assertFalse(interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false));
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withIsFunctionReturnTrue() {
    Node condition = new Node(
      new Node(
        Node.newString(Token.NAME, "goog"),
        Node.newString(Token.STRING, "isFunction")
      ),
      Node.newName("param")
    );
    assertTrue(interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true));
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withIsFunctionReturnFalse() {
    Node condition = new Node(
      new Node(
        Node.newString(Token.NAME, "goog"),
        Node.newString(Token.STRING, "isFunction")
      ),
      Node.newName("param")
    );
    assertFalse(interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false));
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withIsArrayReturnTrue() {
    Node condition = new Node(
      new Node(
        Node.newString(Token.NAME, "goog"),
        Node.newString(Token.STRING, "isArray")
      ),
      Node.newName("param")
    );
    assertTrue(interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true));
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withIsArrayReturnFalse() {
    Node condition = new Node(
      new Node(
        Node.newString(Token.NAME, "goog"),
        Node.newString(Token.STRING, "isArray")
      ),
      Node.newName("param")
    );
    assertFalse(interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false));
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withIsObjectReturnTrue() {
    Node condition = new Node(
      new Node(
        Node.newString(Token.NAME, "goog"),
        Node.newString(Token.STRING, "isObject")
      ),
      Node.newName("param")
    );
    assertTrue(interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, true));
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_withIsObjectReturnFalse() {
    Node condition = new Node(
      new Node(
        Node.newString(Token.NAME, "goog"),
        Node.newString(Token.STRING, "isObject")
      ),
      Node.newName("param")
    );
    assertFalse(interpreter.getPreciserScopeKnowingConditionOutcome(condition, null, false));
  }
}
