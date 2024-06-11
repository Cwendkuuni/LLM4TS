import static org.junit.Assert.*;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

  @Test
  public void testMustBeReachingVariableDefConstructor() {
    // Test constructor with valid parameters
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef reachingVarDef = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    assertNotNull(reachingVarDef);
  }

  @Test
  public void testGetDef() {
    // Test getting definition for a variable in a node
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef reachingVarDef = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    String varName = "testVar";
    Node useNode = new Node(Token.FUNCTION);
    Node defNode = new Node(Token.VAR, new Node(Token.NAME, varName));
    MustBeReachingVariableDef.MustDef mustDef = reachingVarDef.new MustDef();
    Definition def = reachingVarDef.new Definition(defNode);
    mustDef.reachingDef.put(varName, def);

    Node result = reachingVarDef.getDef(varName, useNode);

    assertEquals(defNode, result);
  }

  @Test
  public void testDependsOnOuterScopeVars() {
    // Test checking if a variable depends on outer scope variables in a node
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef reachingVarDef = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    String varName = "testVar";
    Node useNode = new Node(Token.FUNCTION);
    Node defNode = new Node(Token.VAR, new Node(Token.NAME, varName));
    MustBeReachingVariableDef.MustDef mustDef = reachingVarDef.new MustDef();
    Definition def = reachingVarDef.new Definition(defNode);
    def.depends.add(new Var());
    mustDef.reachingDef.put(varName, def);

    boolean result = reachingVarDef.dependsOnOuterScopeVars(varName, useNode);

    assertTrue(result);
  }

  @Test
  public void testFlowThrough() {
    // Test flowing through a node with valid input
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    MustBeReachingVariableDef reachingVarDef = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node n = new Node(Token.VAR, new Node(Token.NAME, "testVar"));
    MustBeReachingVariableDef.MustDef input = reachingVarDef.new MustDef();
    MustBeReachingVariableDef.MustDef result = reachingVarDef.flowThrough(n, input);

    assertNotNull(result);
  }
}