import com.google.javascript.rhino.Node;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FunctionRewriterTest {

    @Test
    public void testEmptyFunctionReducer() {
        FunctionRewriter.EmptyFunctionReducer reducer = new FunctionRewriter.EmptyFunctionReducer();
        Node node = new Node(Token.FUNCTION, new Node("b"));
        Node reducedNode = reducer.reduce(node);
        assertEquals(new Node(Token.CALL, new Node("JSCompiler_emptyFn"), null), reducedNode);
    }

    @Test
    public void testIdentityReducer() {
        FunctionRewriter.IdentityReducer reducer = new FunctionRewriter.IdentityReducer();
        Node node = new Node(Token.FUNCTION, new Node("a"), new Node("a"));
        Node reducedNode = reducer.reduce(node);
        assertEquals(new Node(Token.CALL, new Node("JSCompiler_identityFn"), new Node("a")), reducedNode);
    }

    @Test
    public void testReturnConstantReducer() {
        FunctionRewriter.ReturnConstantReducer reducer = new FunctionRewriter.ReturnConstantReducer();
        Node node = new Node(Token.FUNCTION, new Node(Token.EMPTY));
        Node reducedNode = reducer.reduce(node);
        assertEquals(new Node(Token.CALL, new Node("JSCompiler_returnArg"), null), reducedNode);
    }

    @Test
    public void testGetterReducer() {
        FunctionRewriter.GetterReducer reducer = new FunctionRewriter.GetterReducer();
        Node node = new Node(Token.FUNCTION, new Node("this.b_"));
        Node reducedNode = reducer.reduce(node);
        assertEquals(new Node(Token.CALL, new Node("JSCompiler_get"), new Node("\"b_\"")), reducedNode);
    }

    @Test
    public void testSetterReducer() {
        FunctionRewriter.SetterReducer reducer = new FunctionRewriter.SetterReducer();
        Node node = new Node(Token.FUNCTION, new Node("a"), new Node("this.b_ = a"));
        Node reducedNode = reducer.reduce(node);
        assertEquals(new Node(Token.CALL, new Node("JSCompiler_set"), new Node("\"b_\"")), reducedNode);
    }

}