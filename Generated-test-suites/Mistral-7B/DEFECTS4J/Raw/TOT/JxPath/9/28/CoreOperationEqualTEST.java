import org.apache.commons.jxpath.ri.CompilerContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.apache.commons.jxpath.ri.expression.Variable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.AnnotatedClassLoader;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
public class CoreOperationEqualTest {

    private Expression arg1;
    private Expression arg2;
    private CoreOperationEqual coreOperationEqual;

    @Before
    public void setUp() {
        arg1 = new Variable("arg1");
        arg2 = new Variable("arg2");

        Class<?> contextClass = CoreOperationEqual.class;
        CompilerContext compilerContext = PowerMockito.mock(CompilerContext.class);
        EvalContext context = PowerMockito.mock(EvalContext.class);

        AnnotatedClassLoader classLoader = new AnnotatedClassLoader();
        PowerMockito.spy(CoreOperationEqual.class);
        PowerMockito.spy(contextClass);

        PowerMockito.whenNew(contextClass).withArguments(arg1, arg2).thenReturn(new CoreOperationEqual(arg1, arg2));
        PowerMockito.when(context.getCompilerContext()).thenReturn(compilerContext);
        PowerMockito.when(context.getValue(arg1)).thenReturn(1);
        PowerMockito.when(context.getValue(arg2)).thenReturn(1);

        coreOperationEqual = (CoreOperationEqual) PowerMockito.newInstance(CoreOperationEqual.class, arg1, arg2);
    }

    @Test
    public void testComputeValue_whenBothArgumentsHaveTheSameValue_thenReturnsTrue() {
        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_whenBothArgumentsHaveDifferentValues_thenReturnsFalse() {
        PowerMockito.when(context.getValue(arg1)).thenReturn(1);
        PowerMockito.when(context.getValue(arg2)).thenReturn(2);

        Object result = coreOperationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse(result);
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValue_whenAnyArgumentIsNull_thenThrowsNullPointerException() {
        arg2 = null;

        try {
            coreOperationEqual.computeValue(context);
        } catch (NullPointerException e) {
            assertSame(CoreOperationEqual.class, e.getCause().getCause().getCause().getCause().getCause().getCause().getClass());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_whenOneArgumentIsNotAnExpression_thenThrowsIllegalArgumentException() {
        arg1 = new Object();

        try {
            coreOperationEqual.computeValue(context);
        } catch (IllegalArgumentException e) {
            assertEquals("The left-hand side of the comparison operator is not an Expression", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_whenRightHandSideIsNotAnExpression_thenThrowsIllegalArgumentException() {
        arg2 = new Object();

        try {
            coreOperationEqual.computeValue(context);
        } catch (IllegalArgumentException e) {
            assertEquals("The right-hand side of the comparison operator is not an Expression", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testGetSymbol_whenInvoked_thenReturnsEqualSign() {
        String symbol = coreOperationEqual.getSymbol();
        assertEquals("=", symbol);
    }
}