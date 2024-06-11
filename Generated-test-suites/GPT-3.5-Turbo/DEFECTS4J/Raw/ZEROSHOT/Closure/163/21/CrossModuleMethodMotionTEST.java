import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class CrossModuleMethodMotionTest {

    private CrossModuleMethodMotion crossModuleMethodMotion;

    @Before
    public void setUp() {
        crossModuleMethodMotion = new CrossModuleMethodMotion();
    }

    @Test
    public void testProcess() {
        Node externRoot = new Node();
        Node root = new Node();
        crossModuleMethodMotion.process(externRoot, root);
    }

    @Test
    public void testMoveMethods() {
        Collection<NameInfo> allNameInfo = null;
        crossModuleMethodMotion.moveMethods(allNameInfo);
    }

    @Test
    public void testHasGeneratedAnyIds() {
        CrossModuleMethodMotion.IdGenerator idGenerator = crossModuleMethodMotion.new IdGenerator();
        assertFalse(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testNewId() {
        CrossModuleMethodMotion.IdGenerator idGenerator = crossModuleMethodMotion.new IdGenerator();
        int id = idGenerator.newId();
        assertEquals(0, id);
    }
}