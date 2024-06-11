import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PrepareAstTest {
    
    private PrepareAst prepareAst;
    
    @Before
    public void setUp() {
        AbstractCompiler compiler = new AbstractCompiler();
        prepareAst = new PrepareAst(compiler);
    }
    
    @Test
    public void testProcess_checkOnly() {
        Node externs = new Node(Token.EXTERN);
        Node root = new Node(Token.ROOT);
        
        prepareAst.process(externs, root);
        
        // Add assertions
        // ...
    }
    
    @Test
    public void testProcess_notCheckOnly() {
        Node externs = new Node(Token.EXTERN);
        Node root = new Node(Token.ROOT);
        
        prepareAst = new PrepareAst(compiler, true);
        prepareAst.process(externs, root);
        
        // Add assertions
        // ...
    }
    
    @Test
    public void testReportChange() {
        Assert.assertThrows(IllegalStateException.class, () -> prepareAst.reportChange());
    }
    
    @Test
    public void testNormalizeNodeTypes() {
        Node n = new Node(Token.CALL);
        
        prepareAst.normalizeNodeTypes(n);
        
        // Add assertions
        // ...
    }
    
    @Test
    public void testNormalizeBlocks() {
        Node n = new Node(Token.WHILE);
        
        prepareAst.normalizeBlocks(n);
        
        // Add assertions
        // ...
    }
    
    @Test
    public void testPrepareAnnotations_shouldTraverse() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.OBJECTLIT);
        Node parent = new Node(Token.ROOT);
        
        PrepareAst.PrepareAnnotations annotations = new PrepareAst.PrepareAnnotations();
        boolean result = annotations.shouldTraverse(t, n, parent);
        
        // Add assertions
        // ...
    }
    
    @Test
    public void testPrepareAnnotations_visit() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.CALL);
        Node parent = new Node(Token.ROOT);
        
        PrepareAst.PrepareAnnotations annotations = new PrepareAst.PrepareAnnotations();
        annotations.visit(t, n, parent);
        
        // Add assertions
        // ...
    }
    
    @Test
    public void testNormalizeObjectLiteralAnnotations() {
        Node objlit = new Node(Token.OBJECTLIT);
        
        prepareAst.normalizeObjectLiteralAnnotations(objlit);
        
        // Add assertions
        // ...
    }
    
    @Test
    public void testAnnotateCalls() {
        Node n = new Node(Token.CALL);
        
        prepareAst.annotateCalls(n);
        
        // Add assertions
        // ...
    }
    
    @Test
    public void testAnnotateDispatchers() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.ASSIGN);
        
        prepareAst.annotateDispatchers(n, parent);
        
        // Add assertions
        // ...
    }
    
    @Test
    public void testNormalizeObjectLiteralKeyAnnotations() {
        Node objlit = new Node(Token.OBJECTLIT);
        Node key = new Node(Token.STRING);
        Node value = new Node(Token.STRING);
        
        prepareAst.normalizeObjectLiteralKeyAnnotations(objlit, key, value);
        
        // Add assertions
        // ...
    }
    
}