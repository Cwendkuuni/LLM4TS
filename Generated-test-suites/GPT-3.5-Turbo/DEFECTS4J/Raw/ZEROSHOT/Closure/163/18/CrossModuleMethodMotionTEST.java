import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.CrossModuleMethodMotion;

public class CrossModuleMethodMotionTest {
   
   private CrossModuleMethodMotion crossModuleMethodMotion;
  
   @Before
   public void setUp() {
      crossModuleMethodMotion = new CrossModuleMethodMotion(null, null, false);
   }

   @After
   public void tearDown() {
      crossModuleMethodMotion = null;
   }

   @Test
   public void testProcess() {
      crossModuleMethodMotion.process(null, null);
   }

   @Test
   public void testMoveMethods() {
      crossModuleMethodMotion.moveMethods(null);
   }

   @Test
   public void testIdGenerator_hasGeneratedAnyIds() {
      CrossModuleMethodMotion.IdGenerator idGenerator = crossModuleMethodMotion.new IdGenerator();
      boolean result = idGenerator.hasGeneratedAnyIds();
      assertFalse(result);
   }
   
   @Test
   public void testIdGenerator_newId() {
      CrossModuleMethodMotion.IdGenerator idGenerator = crossModuleMethodMotion.new IdGenerator();
      int result = idGenerator.newId();
      assertEquals(0, result);
   }

}