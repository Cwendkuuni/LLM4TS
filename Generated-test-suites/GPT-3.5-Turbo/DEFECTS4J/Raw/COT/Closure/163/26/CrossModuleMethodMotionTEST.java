
import static org.junit.Assert.*;
import org.junit.Test;

public class CrossModuleMethodMotionTest {

  @Test
  public void testConstructor() {
    CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
    CrossModuleMethodMotion methodMotion = new CrossModuleMethodMotion(new AbstractCompiler(), idGenerator, true);
    assertNotNull(methodMotion);
  }
  
  @Test
  public void testProcess() {
    CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
    CrossModuleMethodMotion methodMotion = new CrossModuleMethodMotion(new AbstractCompiler(), idGenerator, true);
    methodMotion.process(new Node(), new Node());
    //TODO: Add assertions here
  }
  
  @Test
  public void testMoveMethods() {
    CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
    CrossModuleMethodMotion methodMotion = new CrossModuleMethodMotion(new AbstractCompiler(), idGenerator, true);
    methodMotion.moveMethods(new ArrayList<AnalyzePrototypeProperties.NameInfo>());
    //TODO: Add assertions here
  }
  
  @Test
  public void testHasGeneratedAnyIds() {
    CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
    boolean result = idGenerator.hasGeneratedAnyIds();
    assertEquals(false, result);
  }
  
  @Test
  public void testNewId() {
    CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
    int newId = idGenerator.newId();
    assertEquals(0, newId);
  }
}
