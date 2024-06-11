import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CrossModuleMethodMotionTest {
    private CrossModuleMethodMotion.IdGenerator idGenerator;
    private CrossModuleMethodMotion crossModuleMethodMotion;

    @Before
    public void setUp() {
        AbstractCompiler compiler = new AbstractCompiler();
        idGenerator = new CrossModuleMethodMotion.IdGenerator();
        crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, true);
    }

    @Test
    public void testConstructor() {
        AbstractCompiler compiler = new AbstractCompiler();
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
        CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, true);

        assertEquals(compiler, crossModuleMethodMotion.compiler);
        assertEquals(idGenerator, crossModuleMethodMotion.idGenerator);
        assertTrue(crossModuleMethodMotion.moduleGraph.isEmpty());
        assertEquals(compiler, crossModuleMethodMotion.analyzer.compiler);
        assertEquals(idGenerator, crossModuleMethodMotion.analyzer.idGenerator);
        assertNull(crossModuleMethodMotion.analyzer.externsRoot);
        assertNull(crossModuleMethodMotion.analyzer.jsRoot);
        assertNull(CrossModuleMethodMotion.idGenerator);
        assertFalse(CrossModuleMethodMotion.hasStubDeclaration);
    }

    @Test
    public void testHasGeneratedAnyIds_noIdsGenerated() {
        assertFalse(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testHasGeneratedAnyIds_idsGenerated() {
        idGenerator.newId();
        idGenerator.newId();

        assertTrue(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testNewId() {
        assertEquals(0, idGenerator.currentId);

        int id1 = idGenerator.newId();
        assertEquals(0, id1);

        int id2 = idGenerator.newId();
        assertEquals(1, id2);
    }
}