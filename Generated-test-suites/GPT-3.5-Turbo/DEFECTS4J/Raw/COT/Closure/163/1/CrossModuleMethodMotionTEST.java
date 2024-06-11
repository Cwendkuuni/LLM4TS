
import static org.junit.Assert.*;
import org.junit.Test;

public class CrossModuleMethodMotionTest {
  
  @Test
  public void testProcess() {
    AbstractCompiler compiler = new AbstractCompiler();
    CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
    boolean canModifyExterns = false;
    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
    Node externRoot = new Node();
    Node root = new Node();
    crossModuleMethodMotion.process(externRoot, root);
    // Assert something here
  }
  
  @Test
  public void testMoveMethods() {
    AbstractCompiler compiler = new AbstractCompiler();
    CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
    boolean canModifyExterns = false;
    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
    Collection<NameInfo> allNameInfo = new ArrayList<NameInfo>();
    crossModuleMethodMotion.moveMethods(allNameInfo);
    // Assert something here
  }
  
  @Test
  public void testIdGeneratorHasGeneratedAnyIds() {
    CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
    boolean result = idGenerator.hasGeneratedAnyIds();
    assertFalse(result);
    // Assert something here
  }
  
  @Test
  public void testIdGeneratorNewId() {
    CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
    int result = idGenerator.newId();
    assertEquals(0, result);
    // Assert something here
  }
  
}
