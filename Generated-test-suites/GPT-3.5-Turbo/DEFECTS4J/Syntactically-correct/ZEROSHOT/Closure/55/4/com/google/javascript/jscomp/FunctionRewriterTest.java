import org.junit.Test;

import static org.junit.Assert.*;

public class FunctionRewriterTest {

    @Test
    public void testEmptyFunctionReducer() {
        FunctionRewriter.EmptyFunctionReducer reducer = new FunctionRewriter.EmptyFunctionReducer();
        Node functionNode = NodeUtil.emptyFunction();
        Node result = reducer.reduce(functionNode);
        assertNotNull(result);
    }

    @Test
    public void testIdentityReducer() {
        FunctionRewriter.IdentityReducer reducer = new FunctionRewriter.IdentityReducer();
        Node functionNode = NodeUtil.function("return a;");
        Node result = reducer.reduce(functionNode);
        assertNotNull(result);
    }

    @Test
    public void testReturnConstantReducer() {
        FunctionRewriter.ReturnConstantReducer reducer = new FunctionRewriter.ReturnConstantReducer();
        Node functionNode = NodeUtil.function("return 10;");
        Node result = reducer.reduce(functionNode);
        assertNotNull(result);
    }

    @Test
    public void testGetterReducer() {
        FunctionRewriter.GetterReducer reducer = new FunctionRewriter.GetterReducer();
        Node functionNode = NodeUtil.function("return this.a_");
        Node result = reducer.reduce(functionNode);
        assertNotNull(result);
    }

    @Test
    public void testSetterReducer() {
        FunctionRewriter.SetterReducer reducer = new FunctionRewriter.SetterReducer();
        Node functionNode = NodeUtil.function("this.a_ = newValue;");
        Node result = reducer.reduce(functionNode);
        assertNotNull(result);
    }
}