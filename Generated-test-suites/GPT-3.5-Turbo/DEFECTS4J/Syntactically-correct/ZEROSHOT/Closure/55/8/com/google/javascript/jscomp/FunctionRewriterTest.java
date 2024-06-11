import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionRewriterTest {

    private FunctionRewriter functionRewriter;

    @Before
    public void setUp() {
        functionRewriter = new FunctionRewriter(new AbstractCompiler());
    }

    @Test
    public void testEmptyFunctionReducer() {
        FunctionRewriter.EmptyFunctionReducer reducer = new FunctionRewriter.EmptyFunctionReducer();
        assertNull(reducer.reduce(null));
        assertEquals(FunctionRewriter.EmptyFunctionReducer.FACTORY_METHOD_NAME, reducer.getHelperSource());
    }

    @Test
    public void testIdentityReducer() {
        FunctionRewriter.IdentityReducer reducer = new FunctionRewriter.IdentityReducer();
        assertNull(reducer.reduce(null));
        assertEquals(FunctionRewriter.IdentityReducer.FACTORY_METHOD_NAME, reducer.getHelperSource());
    }

    @Test
    public void testReturnConstantReducer() {
        FunctionRewriter.ReturnConstantReducer reducer = new FunctionRewriter.ReturnConstantReducer();
        assertNull(reducer.reduce(null));
        assertEquals(FunctionRewriter.ReturnConstantReducer.FACTORY_METHOD_NAME, reducer.getHelperSource());
    }

    @Test
    public void testGetterReducer() {
        FunctionRewriter.GetterReducer reducer = new FunctionRewriter.GetterReducer();
        assertNull(reducer.reduce(null));
        assertEquals(FunctionRewriter.GetterReducer.FACTORY_METHOD_NAME, reducer.getHelperSource());
    }

    @Test
    public void testSetterReducer() {
        FunctionRewriter.SetterReducer reducer = new FunctionRewriter.SetterReducer();
        assertNull(reducer.reduce(null));
        assertEquals(FunctionRewriter.SetterReducer.FACTORY_METHOD_NAME, reducer.getHelperSource());
    }
}