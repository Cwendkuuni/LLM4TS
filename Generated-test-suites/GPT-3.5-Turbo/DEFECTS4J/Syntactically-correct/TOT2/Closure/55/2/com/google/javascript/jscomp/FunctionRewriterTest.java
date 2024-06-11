import static org.junit.Assert.*;
import org.junit.Test;

public class FunctionRewriterTest {

    @Test
    public void testParseHelperCode() {
        FunctionRewriter rewriter = new FunctionRewriter(null);
        assertNotNull(rewriter.parseHelperCode(new FunctionRewriter.EmptyFunctionReducer()));
    }

    @Test
    public void testReduceEmptyFunctionReducer() {
        // Test reducing empty function expression
        Node node = new Node(Token.FUNCTION);
        Node result = new FunctionRewriter.EmptyFunctionReducer().reduce(node);
        assertNotNull(result);
    }

    @Test
    public void testReduceIdentityReducer() {
        // Test reducing identity function
        Node node = new Node(Token.FUNCTION);
        Node param = new Node(Token.NAME, "value");
        Node body = new Node(Token.RETURN, new Node(Token.NAME, "value"));
        node.addChildToBack(param);
        node.addChildToBack(body);
        Node result = new FunctionRewriter.IdentityReducer().reduce(node);
        assertNotNull(result);
    }

    @Test
    public void testReduceReturnConstantReducer() {
        // Test reducing function with return of immutable value
        Node node = new Node(Token.FUNCTION);
        Node body = new Node(Token.RETURN, new Node(Token.NUMBER, 10.0));
        node.addChildToBack(body);
        Node result = new FunctionRewriter.ReturnConstantReducer().reduce(node);
        assertNotNull(result);
    }

    @Test
    public void testReduceGetterReducer() {
        // Test reducing function with property get
        Node node = new Node(Token.FUNCTION);
        Node body = new Node(Token.RETURN, new Node(Token.GETELEM, new Node(Token.THIS), new Node(Token.STRING, "b_")));
        node.addChildToBack(body);
        Node result = new FunctionRewriter.GetterReducer().reduce(node);
        assertNotNull(result);
    }

    @Test
    public void testReduceSetterReducer() {
        // Test reducing function with property set
        Node node = new Node(Token.FUNCTION);
        Node arg = new Node(Token.NAME, "value");
        Node body = new Node(Token.EXPR_RESULT, new Node(Token.ASSIGN, new Node(Token.GETELEM, new Node(Token.THIS), new Node(Token.STRING, "b_")), new Node(Token.NAME, "value")));
        node.addChildToBack(arg);
        node.addChildToBack(body);
        Node result = new FunctionRewriter.SetterReducer().reduce(node);
        assertNotNull(result);
    }
}