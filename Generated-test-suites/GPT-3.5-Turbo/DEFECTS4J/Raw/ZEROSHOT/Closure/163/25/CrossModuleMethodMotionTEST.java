import org.junit.*;
import static org.junit.Assert.*;

public class CrossModuleMethodMotionTest {

    private CrossModuleMethodMotion.IdGenerator idGenerator;
    private CrossModuleMethodMotion crossModuleMethodMotion;

    @Before
    public void setUp() throws Exception {
        idGenerator = new CrossModuleMethodMotion.IdGenerator();
        crossModuleMethodMotion = new CrossModuleMethodMotion(null, idGenerator, false);
    }

    @Test
    public void testMoveMethods() {
        CrossModuleMethodMotion.NameInfo nameInfo = crossModuleMethodMotion.new NameInfo();
        nameInfo.setReferenced(true);
        nameInfo.setDeepestCommonModuleRef(new JSModule());
        crossModuleMethodMotion.moveMethods(nameInfo);
        assertEquals(true, idGenerator.hasGeneratedAnyIds());

        nameInfo.setReferenced(false);
        crossModuleMethodMotion.moveMethods(nameInfo);
        assertEquals(false, idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testProcess() {
        Node externRoot = new Node();
        Node root = new Node();
        crossModuleMethodMotion.process(externRoot, root);
    }
}