import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class MaybeReachingVariableUseTest {

  private MaybeReachingVariableUse maybeReachingVariableUse;

  @Before
  public void setUp() {
    ControlFlowGraph<Node> cfg = null; // TODO: initialize control flow graph
    Scope jsScope = null; // TODO: initialize JavaScript scope
    AbstractCompiler compiler = null; // TODO: initialize compiler
    maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);
  }

  @Test
  public void testGetUses() {
    Node defNode = null; // TODO: initialize defNode
    String name = null; // TODO: initialize name
    Collection<Node> expectedUses = null; // TODO: initialize expected uses
    Collection<Node> actualUses = maybeReachingVariableUse.getUses(name, defNode);
    assertEquals(expectedUses, actualUses);
  }
}