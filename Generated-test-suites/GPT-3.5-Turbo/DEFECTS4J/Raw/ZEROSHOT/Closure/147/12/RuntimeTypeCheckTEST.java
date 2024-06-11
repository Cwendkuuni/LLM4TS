
import org.junit.Before;
import org.junit.Test;

public class RuntimeTypeCheckTest {

    private RuntimeTypeCheck runtimeTypeCheck;

    @Before
    public void setup() {
        runtimeTypeCheck = new RuntimeTypeCheck(new AbstractCompiler(), null);
    }

    @Test
    public void testProcess() {
        Node externs = new Node(Token.EXTERN, new Node(Token.SCRIPT));
        Node root = new Node(Token.ROOT);
        runtimeTypeCheck.process(externs, root);
    }

    @Test
    public void testAddMarkers() {
        NodeTraversal t = new NodeTraversal(new AbstractPostOrderCallback());
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.SCRIPT);
        runtimeTypeCheck.new AddMarkers(new AbstractCompiler()).visit(t, n, parent);
    }

    @Test
    public void testAddChecks() {
        NodeTraversal t = new NodeTraversal(new AbstractPostOrderCallback());
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.SCRIPT);
        runtimeTypeCheck.new AddChecks(new AbstractCompiler()).visit(t, n, parent);
    }

    @Test
    public void testCreateCheckTypeCallNode() {
        JSType type = new ObjectType();
        Node expr = new Node(Token.NAME);
        runtimeTypeCheck.new AddChecks(new AbstractCompiler()).createCheckTypeCallNode(type, expr);
    }

    @Test
    public void testCreateCheckerNode() {
        JSType type = new ObjectType();
        runtimeTypeCheck.new AddChecks(new AbstractCompiler()).createCheckerNode(type);
    }

    @Test
    public void testAddBoilerplateCode() {
        runtimeTypeCheck.addBoilerplateCode();
    }

    @Test
    public void testGetBoilerplateCode() {
        AbstractCompiler compiler = new AbstractCompiler();
        String logFunction = "logFunction";
        RuntimeTypeCheck.getBoilerplateCode(compiler, logFunction);
    }
}
