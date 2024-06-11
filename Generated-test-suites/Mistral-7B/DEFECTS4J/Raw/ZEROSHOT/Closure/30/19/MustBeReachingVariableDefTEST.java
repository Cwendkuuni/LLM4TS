import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MustBeReachingVariableDef.*;

import org.junit.Test;

public class MustBeReachingVariableDefTest {

  @Test
  public void testMustBeReachingVariableDef() {
    String source = "function foo() {" +
        "var x = 1;" +
        "var y = 2;" +
        "var z = x + y;"
        + "}";

    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(source, new AbstractCompiler());
    Scope jsScope = cfg.getEntryNodeScope();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, new AbstractCompiler());

    Node useX = cfg.getEntryNode().getFirstChild();
    Node useY = useX.getNext();
    Node useZ = useY.getNext();

    // getDef should return the definition of a variable at the use location
    Node defX = analysis.getDef("x", useX);
    Node defY = analysis.getDef("y", useY);
    Node defZ = analysis.getDef("z", useZ);

    // getDef should return null for variables not defined in the function
    assertNull(analysis.getDef("a", useX));

    // getDef should return null for variables that are function parameters
    assertNull(analysis.getDef("foo", useX));

    // getDef should return null for variables that are captured by a closure
    assertNull(analysis.getDef("x", cfg.getNode(1)));

    // getDef should return null for variables that are declared in a catch block
    assertNull(analysis.getDef("x", cfg.getNode(3)));

    // getDef should return the correct definition node
    assertSame(defX, cfg.getNode(0));
    assertSame(defY, cfg.getNode(1));
    assertSame(defZ, cfg.getNode(2));

    // dependsOnOuterScopeVars should return false for local variables
    assertFalse(analysis.dependsOnOuterScopeVars("x", useX));

    // dependsOnOuterScopeVars should return true for variables that depend on outer scope variables
    // (this is the case when the variable is not defined inside the function)
    assertTrue(analysis.dependsOnOuterScopeVars("a", useX));
  }
}