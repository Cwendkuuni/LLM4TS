import static org.junit.Assert.assertThat;
import static com.google.javascript.jscomp.JSType.JSTYPE;
import static com.google.javascript.jscomp.NodeType.CALL;
import static com.google.javascript.jscomp.NodeType.GETPROP;
import static com.google.javascript.jscomp.NodeType.NAME;
import static com.google.javascript.jscomp.NodeType.STRING;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.TypeRestriction;

import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  @Test
  public void testArrayIsDef() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);
    Node condition = Node.newCall(
        Node.newGetProp(Node.newName("goog"), "isDef"),
        Node.newArray(Node.newString("array"))
    );
    JSType expectedType = interpreter.getTypeIfRefinable(condition, null);
    JSType actualType = JSTYPE.ARRAY_TYPE;
    assertThat(expectedType).isSameInstanceAs(actualType);
  }

  @Test
  public void testArrayIsNotNull() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);
    Node condition = Node.newCall(
        Node.newGetProp(Node.newName("goog"), "isDef"),
        Node.newArray(Node.newNumber(1))
    );
    JSType expectedType = null;
    JSType actualType = interpreter.getTypeIfRefinable(condition, null);
    assertThat(actualType).isNull();
  }

  @Test
  public void testArrayIsDefAndNotNull() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);
    Node condition = Node.newCall(
        Node.newGetProp(Node.newName("goog"), "isDef"),
        Node.newArray(Node.newNumber(1))
    );
    JSType expectedType = JSTYPE.ARRAY_TYPE;
    JSType actualType = interpreter.getTypeIfRefinable(condition, true);
    assertThat(expectedType).isSameInstanceAs(actualType);
  }

  @Test(expected = AssertionError.class)
  public void testNonArrayIsDef() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);
    Node condition = Node.newCall(
        Node.newGetProp(Node.newName("goog"), "isDef"),
        Node.newName("string")
    );
    interpreter.getTypeIfRefinable(condition, true);
  }

  @Test(expected = AssertionError.class)
  public void testNullIsDef() {
    ClosureReverseAbstractInterpreter interpreter = new ClosureReverseAbstractInterpreter(
        null, null);
    Node condition = Node.newCall(
        Node.newGetProp(Node.newName("goog"), "isDef"),
        Node.newNull()
    );
    interpreter.getTypeIfRefinable(condition, true);
  }

  // Add test cases for other methods here...
}