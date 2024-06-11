import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CrossModuleMethodMotion;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.DiagnosticType;

import org.junit.Test;
import static org.junit.Assert.*;

public class CrossModuleMethodMotionTest {

  @Test
  public void testProcess() {
    AbstractCompiler compiler = null;
    IdGenerator idGenerator = null;
    boolean canModifyExterns = true;
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
    Node externRoot = new Node();
    Node root = new Node();
    pass.process(externRoot, root);
  }

  @Test
  public void testMoveMethods() {
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(null, null, true);
    Collection<AnalyzePrototypeProperties.NameInfo> allNameInfo = null;
    pass.moveMethods(allNameInfo);
  }

  @Test
  public void testHasGeneratedAnyIds() {
    CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
    boolean result = idGenerator.hasGeneratedAnyIds();
    assertFalse(result);
  }

  @Test
  public void testNewId() {
    CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
    int result = idGenerator.newId();
    assertEquals(0, result);
  }
}