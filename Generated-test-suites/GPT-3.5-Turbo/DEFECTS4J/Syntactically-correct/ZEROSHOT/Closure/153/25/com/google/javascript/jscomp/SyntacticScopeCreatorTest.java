import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class SyntacticScopeCreatorTest {
    
    private SyntacticScopeCreator scopeCreator;
    
    @Before
    public void setUp() {
        scopeCreator = new SyntacticScopeCreator(new AbstractCompiler());
    }
    
    @Test
    public void testCreateScope() {
        Node n = new Node(Token.VAR);
        Scope parent = new Scope(null, null);
        Scope result = scopeCreator.createScope(n, parent);
        assertNotNull(result);
    }
    
    @Test
    public void testCreateScope_Function() {
        Node n = new Node(Token.FUNCTION);
        Scope parent = new Scope(null, null);
        Scope result = scopeCreator.createScope(n, parent);
        assertNotNull(result);
    }
    
    @Test
    public void testCreateScope_Catch() {
        Node n = new Node(Token.CATCH);
        Scope parent = new Scope(null, null);
        Scope result = scopeCreator.createScope(n, parent);
        assertNotNull(result);
    }
    
    @Test
    public void testCreateScope_Script() {
        Node n = new Node(Token.SCRIPT);
        Scope parent = new Scope(null, null);
        Scope result = scopeCreator.createScope(n, parent);
        assertNotNull(result);
    }
    
    @Test
    public void testScanVars_Var() {
        Node n = new Node(Token.VAR);
        Node parent = new Node(Token.SCRIPT);
        scopeCreator.scanVars(n, parent);
    }
    
    @Test
    public void testScanVars_Function() {
        Node n = new Node(Token.FUNCTION);
        Node parent = new Node(Token.SCRIPT);
        scopeCreator.scanVars(n, parent);
    }
    
    @Test
    public void testScanVars_Catch() {
        Node n = new Node(Token.CATCH);
        Node parent = new Node(Token.SCRIPT);
        scopeCreator.scanVars(n, parent);
    }
    
    @Test
    public void testScanVars_Script() {
        Node n = new Node(Token.SCRIPT);
        Node parent = new Node(Token.SCRIPT);
        scopeCreator.scanVars(n, parent);
    }
    
    @Test
    public void testDeclareVar() {
        String name = "test";
        Node n = new Node(Token.NAME, name);
        Node parent = new Node(Token.SCRIPT);
        Node gramps = new Node(Token.SCRIPT);
        JSType declaredType = null;
        Node nodeWithLineNumber = new Node(Token.SCRIPT);
        scopeCreator.declareVar(name, n, parent, gramps, declaredType, nodeWithLineNumber);
    }
}