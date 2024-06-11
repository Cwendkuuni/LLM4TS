import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

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
        
        //assertions
    }
    
    @Test
    public void testVisit() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        
        varCheck.visit(t, n, parent);
        
        //assertions
    }
    
    @Test
    public void testCreateSynthesizedExternVar() {
        String varName = "varName";
        
        varCheck.createSynthesizedExternVar(varName);
        
        //assertions
    }
    
    @Test
    public void testNameRefInExternsCheck() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node();
        Node parent = new Node();
        
        varCheck.new NameRefInExternsCheck().visit(t, n, parent);
        
        //assertions
    }
    
    @Test
    public void testGetSynthesizedExternsInput() {
        CompilerInput synthesizedExternsInput = varCheck.getSynthesizedExternsInput();
        
        //assertions
    }
    
    @Test
    public void testGetSynthesizedExternsRoot() {
        Node synthesizedExternsRoot = varCheck.getSynthesizedExternsRoot();
        
        //assertions
    }
}