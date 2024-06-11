import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Collection;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.Scope;

public class MaybeReachingVariableUseTest {

  private MaybeReachingVariableUse varUse;

  @Before
  public void setUp() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    varUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);
  }

  @Test
  public void testCreateEntryLattice() {
    MaybeReachingVariableUse.ReachingUses lattice = varUse.createEntryLattice();
    assertNotNull(lattice);
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    MaybeReachingVariableUse.ReachingUses lattice = varUse.createInitialEstimateLattice();
    assertNotNull(lattice);
  }

  @Test
  public void testFlowThrough() {
    Node n = new Node(Token.FUNCTION, new Node(Token.NAME, "testFunction"));
    MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
    MaybeReachingVariableUse.ReachingUses output = varUse.flowThrough(n, input);
    assertNotNull(output);
  }

  @Test
  public void testGetUses() {
    Node defNode = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.NUMBER, 10));
    Collection<Node> uses = varUse.getUses("x", defNode);
    assertNotNull(uses);
  }

}