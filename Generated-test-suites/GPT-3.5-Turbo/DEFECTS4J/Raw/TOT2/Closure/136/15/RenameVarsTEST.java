import static org.junit.Assert.*;
import org.junit.Test;

public class RenameVarsTest {

  @Test
  public void testProcessVars_globalNameNodesNotEmpty() {
    RenameVars rv = new RenameVars(new AbstractCompiler(), "prefix", false, false, false, null, null, null);
    rv.process(new Node(), new Node());

    assertFalse(rv.globalNameNodes.isEmpty());
  }

  @Test
  public void testProcessVars_localNameNodesNotEmpty() {
    RenameVars rv = new RenameVars(new AbstractCompiler(), "prefix", false, false, false, null, null, null);
    rv.process(new Node(), new Node());

    assertFalse(rv.localNameNodes.isEmpty());
  }

  @Test
  public void testAssignNames_renameMapNotEmpty() {
    RenameVars rv = new RenameVars(new AbstractCompiler(), "prefix", false, false, false, null, null, null);
    rv.process(new Node(), new Node());
    rv.assignNames(rv.assignments.values());

    assertFalse(rv.renameMap.isEmpty());
  }

}