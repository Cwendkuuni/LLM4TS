
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.NodeTraversal;

import javax.annotation.Nullable;

import org.junit.Before;
import org.junit.Test;

public class RenamePrototypesTest {

  private RenamePrototypes renamePrototypes;

  @Before
  public void setUp() {
    AbstractCompiler compiler = new AbstractCompiler();
    boolean aggressiveRenaming = true;
    char[] reservedCharacters = null;
    VariableMap prevUsedRenameMap = null;
    renamePrototypes = new RenamePrototypes(
        compiler, aggressiveRenaming, reservedCharacters, prevUsedRenameMap);
  }

  @Test
  public void testProcess() {
    //TODO: Implement this test
  }

  @Test
  public void testProcessPropertyMap() {
    //TODO: Implement this test
  }
}
