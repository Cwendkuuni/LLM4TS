import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
public class CrossModuleMethodMotionTest {
    private CrossModuleMethodMotion crossModuleMethodMotion;
    
    @Before
    public void setUp() {
        crossModuleMethodMotion = new CrossModuleMethodMotion();
    }
    
    @Test
    public void testProcess() {
        // TODO: Implement test case for process() method
    }
    
    @Test
    public void testMoveMethods() {
        // TODO: Implement test case for moveMethods() method
    }

    @Test
    public void testHasGeneratedAnyIds() {
        IdGenerator idGenerator = crossModuleMethodMotion.new IdGenerator();
        assertEquals(false, idGenerator.hasGeneratedAnyIds());
    }
    
    @Test
    public void testNewId() {
        IdGenerator idGenerator = crossModuleMethodMotion.new IdGenerator();
        int id = idGenerator.newId();
        assertEquals(0, id);
    }
}