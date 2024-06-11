import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

public class CoreOperationCompareTest {

    private CoreOperationCompare coreOperationCompare;
    private EvalContext context;

    @Before
    public void setUp() {
        coreOperationCompare = new CoreOperationCompare(null, null);
        context = new EvalContext(null, null, null);
    }

    @Test
    public void testEqual_withEqualValues_shouldReturnTrue() {
        Object l = "test1";
        Object r = "test1";
        
        boolean result = coreOperationCompare.equal(context, l, r);

        assertTrue(result);
    }

    @Test
    public void testEqual_withDifferentValues_shouldReturnFalse() {
        Object l = "test1";
        Object r = "test2";
        
        boolean result = coreOperationCompare.equal(context, l, r);

        assertFalse(result);
    }

    @Test
    public void testEqual_withPointersToEqualValues_shouldReturnTrue() {
        Object l = new Pointer("test1");
        Object r = new Pointer("test1");
        
        boolean result = coreOperationCompare.equal(context, l, r);

        assertTrue(result);
    }

    @Test
    public void testEqual_withPointersToDifferentValues_shouldReturnFalse() {
        Object l = new Pointer("test1");
        Object r = new Pointer("test2");
        
        boolean result = coreOperationCompare.equal(context, l, r);

        assertFalse(result);
    }

    @Test
    public void testEqual_withBooleanValues_shouldReturnTrue() {
        Object l = true;
        Object r = true;
        
        boolean result = coreOperationCompare.equal(context, l, r);

        assertTrue(result);
    }

    @Test
    public void testEqual_withDifferentBooleanValues_shouldReturnFalse() {
        Object l = true;
        Object r = false;
        
        boolean result = coreOperationCompare.equal(context, l, r);

        assertFalse(result);
    }

    @Test
    public void testEqual_withNumericValues_shouldReturnTrue() {
        Object l = 1;
        Object r = 1;
        
        boolean result = coreOperationCompare.equal(context, l, r);

        assertTrue(result);
    }

    @Test
    public void testEqual_withDifferentNumericValues_shouldReturnFalse() {
        Object l = 1;
        Object r = 2;
        
        boolean result = coreOperationCompare.equal(context, l, r);

        assertFalse(result);
    }

    @Test
    public void testEqual_withStringValues_shouldReturnTrue() {
        Object l = "test";
        Object r = "test";
        
        boolean result = coreOperationCompare.equal(context, l, r);

        assertTrue(result);
    }

    @Test
    public void testEqual_withDifferentStringValues_shouldReturnFalse() {
        Object l = "test1";
        Object r = "test2";
        
        boolean result = coreOperationCompare.equal(context, l, r);

        assertFalse(result);
    }

    @Test
    public void testEqual_withNullValues_shouldReturnTrue() {
        Object l = null;
        Object r = null;
        
        boolean result = coreOperationCompare.equal(context, l, r);

        assertTrue(result);
    }

    @Test
    public void testEqual_withNonNullAndNullValues_shouldReturnFalse() {
        Object l = "test";
        Object r = null;
        
        boolean result = coreOperationCompare.equal(context, l, r);

        assertFalse(result);
    }

    @Test
    public void testEqual_withNonNullAndNullPointers_shouldReturnFalse() {
        Object l = new Pointer("test");
        Object r = null;
        
        boolean result = coreOperationCompare.equal(context, l, r);

        assertFalse(result);
    }
}