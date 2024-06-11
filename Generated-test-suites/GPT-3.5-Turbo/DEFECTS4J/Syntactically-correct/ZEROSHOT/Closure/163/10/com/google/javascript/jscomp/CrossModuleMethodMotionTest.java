import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;

public class CrossModuleMethodMotionTest {

    private CrossModuleMethodMotion crossModuleMethodMotion;

    @Before
    public void setUp() {
        crossModuleMethodMotion = new CrossModuleMethodMotion();
    }

    @Test
    public void testProcess() {
        Node externRoot = IR.root();
        Node root = IR.root();
        crossModuleMethodMotion.process(externRoot, root);
    }

    @Test
    public void testMoveMethods() {
        Collection<AnalyzePrototypeProperties.NameInfo> allNameInfo = new ArrayList<>();
        crossModuleMethodMotion.moveMethods(allNameInfo);
    }

    @Test
    public void testNewId() {
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
        int id = idGenerator.newId();
        assertTrue(id >= 0);
    }
}