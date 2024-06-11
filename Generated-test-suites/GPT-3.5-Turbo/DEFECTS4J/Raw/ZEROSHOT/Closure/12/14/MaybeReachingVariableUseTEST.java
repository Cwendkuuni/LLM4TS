import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MaybeReachingVariableUseTest {

  private MaybeReachingVariableUse testClass;
  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    cfg = new ControlFlowGraph<Node>();
    jsScope = new Scope();
    compiler = new AbstractCompiler();

    // TODO: Add necessary initialization code for `cfg`, `jsScope`, and `compiler`

    testClass = new MaybeReachingVariableUse(cfg, jsScope, compiler);
  }

  @Test
  public void testFlowThrough() {
    // TODO: Add test cases for the `flowThrough` method
  }

  @Test
  public void testGetUses() {
    // TODO: Add test cases for the `getUses` method
  }

}