import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CrossModuleMethodMotionTest {

  @Test
  public void testProcess() {
    AbstractCompiler compiler = new AbstractCompiler();
    CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
    boolean canModifyExterns = false;
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
    
    Node externRoot = new Node();
    Node root = new Node();
    pass.process(externRoot, root);
    
    //TODO: Add assertions for the process method
  }

  @Test
  public void testMoveMethods() {
    AbstractCompiler compiler = new AbstractCompiler();
    CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
    boolean canModifyExterns = false;
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
    
    AnalyzePrototypeProperties.NameInfo nameInfo = new AnalyzePrototypeProperties.NameInfo();
    //TODO: Add necessary setup for nameInfo
    
    pass.moveMethods(nameInfo);
    
    //TODO: Add assertions for the moveMethods method
  }

  @Test
  public void testNewId() {
    CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
    int id = idGenerator.newId();
    
    //TODO: Add assertions for the newId method
  }

}