import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AnalyzePrototypeProperties;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class CrossModuleMethodMotionTest {

  @Test
  public void testProcess() {
    AbstractCompiler compiler = new AbstractCompiler();
    CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
    boolean canModifyExterns = true;
    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
    
    Node externRoot = null;
    Node root = null;
    
    crossModuleMethodMotion.process(externRoot, root);
    
    // assert your test cases
  }

  @Test
  public void testMoveMethods() {
    AbstractCompiler compiler = new AbstractCompiler();
    CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
    boolean canModifyExterns = true;
    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
    
    // set up your test data
    
    // call the moveMethods method
    
    // assert your test cases
  }

  @Test
  public void testIdGenerator_hasGeneratedAnyIds() {
    CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
    
    boolean result = idGenerator.hasGeneratedAnyIds();
    
    // assert your test cases
  }

  @Test
  public void testIdGenerator_newId() {
    CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
    
    int result = idGenerator.newId();
    
    // assert your test cases
  }

}