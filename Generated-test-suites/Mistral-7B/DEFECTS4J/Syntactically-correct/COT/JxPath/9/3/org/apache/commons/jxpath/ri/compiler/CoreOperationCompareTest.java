import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(JUnit4.class)
public class CoreOperationCompareTest {

    private CoreOperationCompare coreOperationCompare;
    private Expression arg1;
    private Expression arg2;
    private EvalContext context;

    @Before
    public void setUp() {
        arg1 = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return new Integer(1);
            }
        };

        arg2 = new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return new Integer(1);
            }
        };

        context = new EvalContext();
    }

    @Test
    public void testEqual() {
        coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_differentObjects() {
        coreOperationCompare = new CoreOperationCompare(arg1, new Integer(2));
        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_null() {
        coreOperationCompare = new CoreOperationCompare(arg1, null);
        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_boolean() {
        coreOperationCompare = new CoreOperationCompare(
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return Boolean.TRUE;
                }
            },
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return Boolean.TRUE;
                }
            }
        );
        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_number() {
        coreOperationCompare = new CoreOperationCompare(
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return new Double(1.0);
                }
            },
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return new Double(1.0);
                }
            }
        );
        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_string() {
        coreOperationCompare = new CoreOperationCompare(
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return "one";
                }
            },
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return "one";
                }
            }
        );
        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_pointers() {
        Pointer pointer1 = new Pointer(new Object(), new String[]{"/test/1"});
        Pointer pointer2 = new Pointer(new Object(), new String[]{"/test/1"});
        coreOperationCompare = new CoreOperationCompare(pointer1, pointer2);
        assertTrue(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testEqual_differentPointers() {
        Pointer pointer1 = new Pointer(new Object(), new String[]{"/test/1"});
        Pointer pointer2 = new Pointer(new Object(), new String[]{"/test/2"});
        coreOperationCompare = new CoreOperationCompare(pointer1, pointer2);
        assertFalse(coreOperationCompare.equal(context, arg1, arg2));
    }

    @Test
    public void testContains() {
        List list = new ArrayList();
        list.add(new Integer(1));
        list.add(new Integer(2));
        coreOperationCompare = new CoreOperationCompare(list.iterator(), arg2);
        assertTrue(coreOperationCompare.contains(context, arg1, arg2));
    }

    @Test
    public void testContains_emptyList() {
        coreOperationCompare = new CoreOperationCompare(new ArrayList().iterator(), arg2);
        assertFalse(coreOperationCompare.contains(context, arg1, arg2));
    }

    @Test
    public void testContains_null() {
        coreOperationCompare = new CoreOperationCompare(new ArrayList().iterator(), null);
        assertFalse(coreOperationCompare.contains(context, arg1, arg2));
    }

    @Test
    public void testFindMatch() {
        Set set1 = new HashSet();
        set1.add(new Integer(1));
        set1.add(new Integer(2));
        Iterator iterator1 = set1.iterator();

        Set set2 = new HashSet();
        set2.add(new Integer(1));
        Iterator iterator2 = set2.iterator();

        coreOperationCompare = new CoreOperationCompare(iterator1, iterator2);
        assertTrue(coreOperationCompare.findMatch(context, iterator1, iterator2));
    }

    @Test
    public void testFindMatch_emptySets() {
        Iterator iterator1 = new ArrayList().iterator();
        Iterator iterator2 = new ArrayList().iterator();
        coreOperationCompare = new CoreOperationCompare(iterator1, iterator2);
        assertFalse(coreOperationCompare.findMatch(context, iterator1, iterator2));
    }
}