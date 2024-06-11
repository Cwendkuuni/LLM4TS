import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CrossModuleMethodMotionTest {
  
  @Test
  public void testProcess() {
    AbstractCompiler compiler = new AbstractCompiler();
    CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
    boolean canModifyExterns = true;
    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
    Node externRoot = new Node();
    Node root = new Node();
    crossModuleMethodMotion.process(externRoot, root);
    // Assert statements here
  }
  
  @Test
  public void testMoveMethods() {
    AbstractCompiler compiler = new AbstractCompiler();
    CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
    boolean canModifyExterns = true;
    CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
    Collection<AnalyzePrototypeProperties.NameInfo> allNameInfo = new ArrayList<>();
    /* Populate allNameInfo with test data */
    crossModuleMethodMotion.moveMethods(allNameInfo);
    // Assert statements here
  }
  
  @Test
  public void testNewId() {
    CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
    int id = idGenerator.newId();
    assertEquals(1, id);
  }
  
  @Test
  public void testHasGeneratedAnyIds() {
    CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
    boolean hasGeneratedAnyIds = idGenerator.hasGeneratedAnyIds();
    assertEquals(false, hasGeneratedAnyIds);
  }
}