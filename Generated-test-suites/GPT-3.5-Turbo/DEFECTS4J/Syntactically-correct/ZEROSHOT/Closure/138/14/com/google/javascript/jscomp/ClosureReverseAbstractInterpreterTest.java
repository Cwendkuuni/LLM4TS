import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;
import static com.google.javascript.rhino.Token.CALL;
import static com.google.javascript.rhino.Token.GETPROP;
import static com.google.javascript.rhino.Token.NAME;
import static com.google.javascript.rhino.Token.STRING;
import static com.google.javascript.rhino.jstype.JSTypeNative.ARRAY_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.NO_OBJECT_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.NULL_TYPE;
import static com.google.javascript.rhino.jstype.JSTypeNative.OBJECT_TYPE;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ClosureReverseAbstractInterpreterTest {

  private ClosureReverseAbstractInterpreter closureReverseAbstractInterpreter;
  private CodingConvention codingConvention;
  private JSTypeRegistry typeRegistry;

  @Before
  public void setUp() {
    codingConvention = new CodingConvention();
    typeRegistry = new JSTypeRegistry();
    closureReverseAbstractInterpreter = new ClosureReverseAbstractInterpreter(codingConvention, typeRegistry);
  }

  @Test
  public void testGetPreciserScopeKnowingConditionOutcome() {
    Node condition = new Node(GETPROP,
        new Node(NAME, new Node(STRING, "goog")),
        new Node(STRING, "isDef"));
    FlowScope blindScope = new FlowScope();
    boolean outcome = true;

    FlowScope scope = closureReverseAbstractInterpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);

    // Assert statements
  }

  @Test
  public void testVisitor_restrictToArrayVisitor() {
    Visitor<JSType> visitor = closureReverseAbstractInterpreter.restrictToArrayVisitor;

    // Assert statements
  }

  @Test
  public void testVisitor_restrictToNotArrayVisitor() {
    Visitor<JSType> visitor = closureReverseAbstractInterpreter.restrictToNotArrayVisitor;

    // Assert statements
  }

  @Test
  public void testVisitor_restrictToObjectVisitor() {
    Visitor<JSType> visitor = closureReverseAbstractInterpreter.restrictToObjectVisitor;

    // Assert statements
  }

  @Test
  public void testVisitor_restrictToNotObjectVisitor() {
    Visitor<JSType> visitor = closureReverseAbstractInterpreter.restrictToNotObjectVisitor;

    // Assert statements
  }
}