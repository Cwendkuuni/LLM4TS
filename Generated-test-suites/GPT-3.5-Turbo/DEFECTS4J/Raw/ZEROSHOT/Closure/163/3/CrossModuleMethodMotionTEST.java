import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.CrossModuleMethodMotion;
import com.google.javascript.jscomp.AbstractCompiler;

public class CrossModuleMethodMotionTest {

    private CrossModuleMethodMotion crossModuleMethodMotion;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, new CrossModuleMethodMotion.IdGenerator(), true);
    }

    @Test
    public void testProcess() {
        // TODO: Add test case for process() method
    }

    @Test
    public void testMoveMethods() {
        // TODO: Add test case for moveMethods() method
    }

    @Test
    public void testHasGeneratedAnyIds() {
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
        assertFalse(idGenerator.hasGeneratedAnyIds());

        idGenerator.newId();
        assertTrue(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testNewId() {
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
        assertEquals(0, idGenerator.newId());
        assertEquals(1, idGenerator.newId());
        assertEquals(2, idGenerator.newId());
    }
}