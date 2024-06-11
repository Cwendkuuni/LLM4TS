
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

        NodeTraversal t = new NodeTraversal();

        Node n1 = new Node(Token.FUNCTION);
        Node n2 = new Node(Token.NAME);
        Node parent1 = new Node(Token.NAME);

        assertFalse(checker.shouldTraverse(t, n1, parent1));

        JSDocInfo jsDoc1 = new JSDocInfo();
        jsDoc1.setConstructor(true);
        n1.setJSDocInfo(jsDoc1);
        assertFalse(checker.shouldTraverse(t, n1, parent1));

        JSDocInfo jsDoc2 = new JSDocInfo();
        jsDoc2.recordThisType();
        n1.setJSDocInfo(jsDoc2);
        assertFalse(checker.shouldTraverse(t, n1, parent1));

        Node parent2 = new Node(Token.NAME);
        parent2.setFirstChild(new Node(Token.NAME));
        parent2.getFirstChild().setNext(new Node(Token.LP));
        assertFalse(checker.shouldTraverse(t, n1, parent2));

        Node n3 = new Node(Token.ASSIGN);
        Node n4 = new Node(Token.GETPROP);
        Node n5 = new Node(Token.NAME);
        n4.addChildToBack(n5);
        n3.addChildToFront(n4);
        parent2.setFirstChild(n3);
        n5.setString("prototype");
        assertFalse(checker.shouldTraverse(t, n1, parent2));

        Node n6 = new Node(Token.GETPROP);
        n6.addChildToBack(n5);
        n5.setString("prototype");
        assertTrue(checker.shouldTraverse(t, n1, n6));

        Node n7 = new Node(Token.NAME);
        n7.setString("a.prototype.b");
        assertTrue(checker.shouldTraverse(t, n1, n7));
    }

    @Test
    public void testVisit() {
        AbstractCompiler compiler = new AbstractCompiler();
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

        NodeTraversal t = new NodeTraversal();

        Node n1 = new Node(Token.THIS);
        Node n2 = new Node(Token.THIS);
        Node n3 = new Node(Token.THIS);
        Node n4 = new Node(Token.THIS);
        Node parent1 = new Node(Token.THIS);
        Node parent2 = null;

        assertFalse(checker.shouldReportThis(n1, parent1));
        assertFalse(checker.shouldReportThis(n2, parent2));
        assertFalse(checker.shouldReportThis(n3, parent1));

        assertTrue(checker.shouldReportThis(n4, parent2));
    }

    @Test
    public void testGetFunctionJsDocInfo() {
        CheckGlobalThis checker = new CheckGlobalThis();

        Node n1 = new Node(Token.FUNCTION);
        Node parent1 = null;
        assertNull(checker.getFunctionJsDocInfo(n1));

        Node n2 = new Node(Token.FUNCTION);
        JSDocInfo jsDoc1 = new JSDocInfo();
        n2.setJSDocInfo(jsDoc1);
        assertEquals(jsDoc1, checker.getFunctionJsDocInfo(n2));

        Node n3 = new Node(Token.VAR);
        Node n4 = new Node(Token.NAME);
        n3.addChildToFront(n4);
        Node parent2 = new Node(Token.NAME);
        parent2.addChildToFront(n3);
        JSDocInfo jsDoc2 = new JSDocInfo();
        n4.setJSDocInfo(jsDoc2);
        assertEquals(jsDoc2, checker.getFunctionJsDocInfo(n4));

        Node n5 = new Node(Token.NAME);
        Node parent3 = new Node(Token.ASSIGN);
        parent3.addChildToFront(n5);
        JSDocInfo jsDoc3 = new JSDocInfo();
        parent3.setJSDocInfo(jsDoc3);
        assertEquals(jsDoc3, checker.getFunctionJsDocInfo(n5));
    }

}
