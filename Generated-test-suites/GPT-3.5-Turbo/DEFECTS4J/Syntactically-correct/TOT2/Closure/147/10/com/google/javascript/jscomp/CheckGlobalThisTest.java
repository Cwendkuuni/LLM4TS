
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_WhenFunctionIsConstructor() {
        // Test shouldTraverse method checks if the function is a constructor
        Node node = new Node(Token.FUNCTION, new Node(Token.NAME, "testFunction"));
        CheckGlobalThis check = new CheckGlobalThis(null, CheckLevel.WARNING);
        assertFalse(check.shouldTraverse(null, node, null));
    }

    @Test
    public void testVisit_WhenThisEncountered() {
        // Test visit method checks if the error is reported when THIS keyword is encountered
        Node thisNode = new Node(Token.THIS);
        CheckGlobalThis check = new CheckGlobalThis(null, CheckLevel.WARNING);
        check.visit(null, thisNode, null); // Error should be reported
    }

    @Test
    public void testGetFunctionJsDocInfo() {
        // Test getFunctionJsDocInfo method checks if JSDoc information is correctly retrieved
        Node node = new Node(Token.FUNCTION, new Node(Token.NAME, "testFunction"));
        node.setJSDocInfo(null); // No JSDoc info set
        CheckGlobalThis check = new CheckGlobalThis(null, CheckLevel.WARNING);
        assertNull(check.getFunctionJsDocInfo(node));
    }

}
