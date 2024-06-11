import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VarCheckTest {
    
    private VarCheck varCheck;
    
    @Before
    public void setUp() {
        varCheck = new VarCheck();
    }
    
    @Test
    public void testProcess() {
        Node externs = new Node(Token.EXTERN, new Node(Token.VAR, Node.newString(Token.NAME, "var1")));
        Node root = new Node(Token.ROOT);
        
        varCheck.process(externs, root);
    }
    
    @Test
    public void testVisit() {
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.NAME, "var1");
        Node parent = new Node(Token.VAR);
        
        varCheck.visit(t, n, parent);
    }
}