import static org.junit.Assert.*;
import org.junit.Test;

public class CrossModuleMethodMotionTest {
    
    @Test
    public void testProcess() {
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
        CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(null, idGenerator, true);
        crossModuleMethodMotion.process(null, null);
    }
    
    @Test
    public void testMoveMethods() {
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
        CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(null, idGenerator, true);
        crossModuleMethodMotion.moveMethods(null);
    }
    
    @Test
    public void testNewId() {
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
        int id = idGenerator.newId();
        assertEquals(0, id);
    }
    
    @Test
    public void testHasGeneratedAnyIds() {
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
        boolean hasGenerated = idGenerator.hasGeneratedAnyIds();
        assertFalse(hasGenerated);
        idGenerator.newId();
        hasGenerated = idGenerator.hasGeneratedAnyIds();
        assertTrue(hasGenerated);
    }
}