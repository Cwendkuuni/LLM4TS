import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SyntacticScopeCreatorTest {
    
    private SyntacticScopeCreator scopeCreator;
    private AbstractCompiler compiler;
    
    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        scopeCreator = new SyntacticScopeCreator(compiler);
    }
    
    @Test
    public void testCreateScope() {
        Node n = new Node();
        Scope parent = new Scope(new Node(), compiler);
        
        Scope scope = scopeCreator.createScope(n, parent);
        
        assertNotNull(scope);
        assertEquals(parent, scope.getParent());
        assertNull(scope.getParentNode());
    }
    
    @Test
    public void testScanVars_Var() {
        Node n = new Node(Token.VAR);
        Node parent = new Node();
        
        scopeCreator.scanVars(n, parent);
        
        // TODO: Add assertions
    }
    
    @Test
    public void testScanVars_Function() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node();
        
        scopeCreator.scanVars(n, parent);
        
        // TODO: Add assertions
    }
    
    @Test
    public void testScanVars_Catch() {
        Node n = new Node(Token.CATCH);
        Node parent = new Node();
        
        scopeCreator.scanVars(n, parent);
        
        // TODO: Add assertions
    }
    
    @Test
    public void testScanVars_Script() {
        Node n = new Node(Token.SCRIPT);
        Node parent = new Node();
        
        scopeCreator.scanVars(n, parent);
        
        // TODO: Add assertions
    }
    
    @Test
    public void testDeclareVar() {
        String name = "test";
        Node n = new Node();
        Node parent = new Node();
        Node gramps = new Node();
        JSType declaredType = new JSType();
        Node nodeWithLineNumber = new Node();
        
        scopeCreator.declareVar(name, n, parent, gramps, declaredType, nodeWithLineNumber);
        
        // TODO: Add assertions
    }

}