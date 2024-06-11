import static org.junit.Assert.*;
import org.junit.Test;

public class CrossModuleMethodMotionTest {
  
  @Test
  public void testProcess() {
    AbstractCompiler compiler = new AbstractCompiler();
    CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
    boolean canModifyExterns = true;
    CrossModuleMethodMotion motion = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
    Node externRoot = new Node();
    Node root = new Node();
    motion.process(externRoot, root);
    // test that motion.process() does not throw any exceptions
  }
  
  @Test
  public void testMoveMethods() {
    AbstractCompiler compiler = new AbstractCompiler();
    CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
    boolean canModifyExterns = true;
    CrossModuleMethodMotion motion = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
    Collection<AnalyzePrototypeProperties.NameInfo> allNameInfo = new ArrayList<>();
    motion.moveMethods(allNameInfo);
    // test that moveMethods() does not throw any exceptions
  }
  
  @Test
  public void testIdGeneratorHasGeneratedAnyIds() {
    CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
    assertFalse(idGenerator.hasGeneratedAnyIds());
    idGenerator.currentId = 1;
    assertTrue(idGenerator.hasGeneratedAnyIds());
  }
  
  @Test
  public void testIdGeneratorNewId() {
    CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
    int id1 = idGenerator.newId();
    assertEquals(0, id1);
    int id2 = idGenerator.newId();
    assertEquals(1, id2);
  }
  
}