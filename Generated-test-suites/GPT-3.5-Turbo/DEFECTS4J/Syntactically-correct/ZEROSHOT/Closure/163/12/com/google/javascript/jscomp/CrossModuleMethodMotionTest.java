import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CrossModuleMethodMotionTest {
    
    private CrossModuleMethodMotion methodMotion;
    
    @Before
    public void setUp() {
        methodMotion = new CrossModuleMethodMotion();
    }
    
    @Test
    public void testMoveMethods() {
        // TODO: Add test cases for moveMethods method
    }
    
    @Test
    public void testProcess() {
        // TODO: Add test cases for process method
    }
    
    @Test
    public void testIdGenerator_hasGeneratedAnyIds() {
        CrossModuleMethodMotion.IdGenerator idGenerator = methodMotion.new IdGenerator();
        assertFalse(idGenerator.hasGeneratedAnyIds());
        
        idGenerator.newId();
        assertTrue(idGenerator.hasGeneratedAnyIds());
    }
    
    @Test
    public void testIdGenerator_newId() {
        CrossModuleMethodMotion.IdGenerator idGenerator = methodMotion.new IdGenerator();
        assertEquals(0, idGenerator.newId());
        assertEquals(1, idGenerator.newId());
        assertEquals(2, idGenerator.newId());
    }
}