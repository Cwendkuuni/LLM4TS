
import org.junit.Assert;
import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.FUNCTION);
        Node parent = null;
        
        boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
        
        Assert.assertTrue(result);
    }
    
    @Test
    public void testVisit() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
        
        NodeTraversal t = new NodeTraversal();
        Node n = new Node(Token.THIS);
        Node parent = new Node(Token.GETPROP);
        
        checkGlobalThis.visit(t, n, parent);
        
        // Check if the compiler reported an error
        List<JSError> errors = testCompiler.getErrors();
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals(CheckGlobalThis.GLOBAL_THIS, errors.get(0).getType());
    }
}
