import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class VarCheckTest {

    private VarCheck varCheck;

    @Before
    public void setUp() {
        varCheck = new VarCheck();
    }

    @Test
    public void testProcess() {
        Node externs = new Node();
        Node root = new Node();
        varCheck.process(externs, root);
        // Add assertions here
    }

    @Test
    public void testVisit() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        varCheck.visit(t, n, parent);
        // Add assertions here
    }

    @Test
    public void testNameRefInExternsCheck() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        varCheck.new NameRefInExternsCheck().visit(t, n, parent);
        // Add assertions here
    }

    @Test
    public void testCreateSynthesizedExternVar() {
        String varName = "testVar";
        varCheck.createSynthesizedExternVar(varName);
        // Add assertions here
    }

    @Test
    public void testGetSynthesizedExternsInput() {
        CompilerInput synthesizedExternsInput = varCheck.getSynthesizedExternsInput();
        // Add assertions here
    }

    @Test
    public void testGetSynthesizedExternsRoot() {
        Node synthesizedExternsRoot = varCheck.getSynthesizedExternsRoot();
        // Add assertions here
    }
}