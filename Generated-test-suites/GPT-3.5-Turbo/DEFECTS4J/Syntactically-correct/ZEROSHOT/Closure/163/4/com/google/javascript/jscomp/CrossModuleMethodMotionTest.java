import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class CrossModuleMethodMotionTest {

    private CrossModuleMethodMotion crossModuleMethodMotion;

    @Before
    public void setUp() {
        Compiler compiler = new Compiler();
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
        boolean canModifyExterns = true;
        crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, canModifyExterns);
    }

    @Test
    public void testProcess() {
        Node externRoot = new Node();
        Node root = new Node();

        crossModuleMethodMotion.process(externRoot, root);
    }

    @Test
    public void testMoveMethods() {
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
        Collection<AnalyzePrototypeProperties.NameInfo> allNameInfo = new ArrayList<>();

        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo("example.name", true, new JSModule());
        AnalyzePrototypeProperties.Symbol symbol = new AnalyzePrototypeProperties.Property(new Node());
        symbol.setValue(new Node(IR.function()));
        symbol.setPrototype(new Node());
        nameInfo.addDeclaration(symbol);
        allNameInfo.add(nameInfo);

        crossModuleMethodMotion.moveMethods(allNameInfo);
    }

    @Test
    public void testIdGeneratorHasGeneratedAnyIds() {
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();

        boolean result = idGenerator.hasGeneratedAnyIds();

        assertEquals(false, result);
    }

    @Test
    public void testIdGeneratorNewId() {
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();

        int result = idGenerator.newId();

        assertEquals(0, result);
    }
}