import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.Pointer;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new CoreOperation() {
                @Override
                public Object compute(EvalContext context) {
                    return 5;
                }
            },
            new CoreOperation() {
                @Override
                public Object compute(EvalContext context) {
                    return 5;
                }
            }
        );
        EvalContext context = new EvalContext();
        assertTrue(compare.equal(context, new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return 5;
            }
        }, new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return 5;
            }
        }));
    }

    @Test
    public void testInequal1() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new CoreOperation() {
                @Override
                public Object compute(EvalContext context) {
                    return 5;
                }
            },
            new CoreOperation() {
                @Override
                public Object compute(EvalContext context) {
                    return 6;
                }
            }
        );
        EvalContext context = new EvalContext();
        assertFalse(compare.equal(context, new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return 5;
            }
        }, new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return 6;
            }
        }));
    }

    @Test
    public void testInequal2() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new CoreOperation() {
                @Override
                public Object compute(EvalContext context) {
                    return new Pointer("5");
                }
            },
            new CoreOperation() {
                @Override
                public Object compute(EvalContext context) {
                    return new Pointer("6");
                }
            }
        );
        EvalContext context = new EvalContext();
        assertFalse(compare.equal(context, new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new Pointer("5");
            }
        }, new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new Pointer("6");
            }
        }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInequal3() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new CoreOperation() {
                @Override
                public Object compute(EvalContext context) {
                    return new Pointer("5");
                }
            }, null
        );
        EvalContext context = new EvalContext();
        compare.equal(context, new CoreOperation() {
            @Override
            public Object compute(EvalContext context) {
                return new Pointer("5");
            }
        }, null);
    }
}