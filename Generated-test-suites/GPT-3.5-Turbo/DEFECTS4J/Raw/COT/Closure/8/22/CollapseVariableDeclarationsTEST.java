import com.google.javascript.rhino.Node;
import org.junit.Assert;
import org.junit.Test;

public class CollapseVariableDeclarationsTest {

    @Test
    public void testCollapseVariableDeclarations() {
        CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations();
        
        // Test case 1
        Node varNode1 = new Node(Token.VAR);
        Node varNode2 = new Node(Token.VAR);
        Node varNode3 = new Node(Token.VAR);
        
        Node assign1 = new Node(Token.ASSIGN, new Node(Token.NAME, "a"), new Node(Token.TRUE));
        Node assign2 = new Node(Token.ASSIGN, new Node(Token.NAME, "b"), new Node(Token.TRUE));
        Node assign3 = new Node(Token.ASSIGN, new Node(Token.NAME, "c"), new Node(Token.TRUE));
        
        varNode1.addChildToBack(assign1);
        varNode2.addChildToBack(assign2);
        varNode3.addChildToBack(assign3);
        
        Node root = new Node(Token.ROOT);
        root.addChildToBack(varNode1);
        root.addChildToBack(varNode2);
        root.addChildToBack(varNode3);
        
        Node externs = new Node(Token.ROOT);
        
        collapseVariableDeclarations.process(externs, root);
        
        Assert.assertEquals("var c = b = a = true;", root.toString());
        
        // Test case 2
        Node varNode4 = new Node(Token.VAR);
        
        Node assign4 = new Node(Token.ASSIGN, new Node(Token.NAME, "x"), new Node(Token.TRUE));
        
        varNode4.addChildToBack(assign4);
        
        Node root1 = new Node(Token.ROOT);
        root1.addChildToBack(varNode4);
        
        Node externs1 = new Node(Token.ROOT);
        
        collapseVariableDeclarations.process(externs1, root1);
        
        Assert.assertEquals("var x = true;", root1.toString());
    }
}