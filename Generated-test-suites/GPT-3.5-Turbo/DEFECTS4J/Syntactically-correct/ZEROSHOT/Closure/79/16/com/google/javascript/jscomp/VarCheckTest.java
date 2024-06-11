import static org.junit.Assert.*;
import org.junit.Test;

public class VarCheckTest {
    
    @Test
    public void testProcess() {
        VarCheck varCheck = new VarCheck(null);
        // test case 1
        NodeTraversal traversal1 = new NodeTraversal(null, null);
        Node externs1 = new Node(Token.EXTERNANDABSTRACT, new Node(Token.NUMBER));
        Node root1 = new Node(Token.ROOT, new Node(Token.NUMBER), new Node(Token.NAME));
        varCheck.process(externs1, root1);
        
        // test case 2
        NodeTraversal traversal2 = new NodeTraversal(null, null);
        Node externs2 = new Node(Token.ROOT, new Node(Token.NAME), new Node(Token.NULL));
        Node root2 = new Node(Token.NULL, new Node(Token.NAME));
        varCheck.process(externs2, root2);
        
        // test case 3
        NodeTraversal traversal3 = new NodeTraversal(null, null);
        Node externs3 = new Node(Token.ROOT);
        Node root3 = new Node(Token.ROOT);
        varCheck.process(externs3, root3);
    }

    @Test
    public void testVisit() {
        VarCheck varCheck = new VarCheck(null);
        NodeTraversal traversal = new NodeTraversal(null, null);
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.NUMBER);
        varCheck.visit(traversal, n, parent);
    }

    @Test
    public void testCreateSynthesizedExternVar() {
        VarCheck varCheck = new VarCheck(null);
        String varName1 = "var1";
        varCheck.createSynthesizedExternVar(varName1);
        String varName2 = "var2";
        varCheck.createSynthesizedExternVar(varName2);
    }

    @Test
    public void testNameRefInExternsCheck() {
        VarCheck varCheck = new VarCheck(null);
        NodeTraversal traversal = new NodeTraversal(null, null);
        Node n = new Node(Token.NAME);
        Node parent = new Node(Token.NUMBER);
        varCheck.new NameRefInExternsCheck().visit(traversal, n, parent);
    }

    @Test
    public void testGetSynthesizedExternsInput() {
        VarCheck varCheck = new VarCheck(null);
        varCheck.getSynthesizedExternsInput();
    }

    @Test
    public void testGetSynthesizedExternsRoot() {
        VarCheck varCheck = new VarCheck(null);
        varCheck.getSynthesizedExternsRoot();
    }

}