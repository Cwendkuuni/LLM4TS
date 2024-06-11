package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        EvalContext context = new EvalContext(new Object() {
            @Override
            public Iterator iterator() {
                return new ArrayList(List.of(1, 2, 3)).iterator();
            }
        }, new Object() {
            @Override
            public Iterator iterator() {
                return new ArrayList(List.of(1, 2, 3)).iterator();
            }
        });

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreConstant(1),
            new CoreConstant(1)
        );

        assertTrue(coreOperationCompare.evaluate(context));

        context = new EvalContext(new Object() {
            @Override
            public Iterator iterator() {
                return new ArrayList(List.of(1, 2, 3)).iterator();
            }
        }, new Object() {
            @Override
            public Iterator iterator() {
                return new ArrayList(List.of(2, 3, 4)).iterator();
            }
        });

        assertFalse(coreOperationCompare.evaluate(context));

        CoreOperationCompare coreOperationCompareString = new CoreOperationCompare(
            new CoreConstant("foo"),
            new CoreConstant("foo")
        );

        assertTrue(coreOperationCompareString.evaluate(context));

        CoreOperationCompare coreOperationCompareStringNotEqual = new CoreOperationCompare(
            new CoreConstant("foo"),
            new CoreConstant("bar")
        );

        assertFalse(coreOperationCompareStringNotEqual.evaluate(context));
    }

    @Test
    public void testEqualPointer() {
        Pointer left = new Pointer(new Object() {
            @Override
            public Object getValue() {
                return "foo";
            }
        });

        Pointer right = new Pointer(new Object() {
            @Override
            public Object getValue() {
                return "foo";
            }
        });

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(left, right);

        assertTrue(coreOperationCompare.evaluate(new EvalContext(new InitialContext(), new SelfContext())));

        left = new Pointer(new Object() {
            @Override
            public Object getValue() {
                return "foo";
            }
        });

        right = new Pointer(new Object() {
            @Override
            public Object getValue() {
                return "bar";
            }
        });

        assertFalse(coreOperationCompare.evaluate(new EvalContext(new InitialContext(), new SelfContext())));
    }

    @Test
    public void testEqualCollection() {
        EvalContext context = new EvalContext(new Object() {
            @Override
            public Iterator iterator() {
                return new ArrayList(List.of(1, 2, 3)).iterator();
            }
        }, new Object() {
            @Override
            public Iterator iterator() {
                return new ArrayList(List.of(1, 2, 3)).iterator();
            }
        });

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreFunctionCall("count(.)", new EvaluationStack(), new EvalContext(new InitialContext(), new SelfContext())),
            new CoreConstant(3)
        );

        assertTrue(coreOperationCompare.evaluate(context));

        context = new EvalContext(new Object() {
            @Override
            public Iterator iterator() {
                return new ArrayList(List.of(1, 2, 3)).iterator();
            }
        }, new Object() {
            @Override
            public Iterator iterator() {
                return new ArrayList(List.of(1, 2, 4)).iterator();
            }
        });

        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testContains() {
        EvalContext context = new EvalContext(new Object() {
            @Override
            public Iterator iterator() {
                return new ArrayList(List.of(1, 2, 3)).iterator();
            }
        }, new Object() {
            @Override
            public Iterator iterator() {
                return new ArrayList(List.of(1, 2, 3)).iterator();
            }
        });

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreFunctionCall("count(.)", new EvaluationStack(), new EvalContext(new InitialContext(), new SelfContext())),
            new CoreConstant(3)
        );

        assertTrue(coreOperationCompare.evaluate(context));

        context = new EvalContext(new Object() {
            @Override
            public Iterator iterator() {
                return new ArrayList(List.of(1, 2, 3)).iterator();
            }
        }, new Object() {
            @Override
            public Iterator iterator() {
                return new ArrayList(List.of(2, 3, 4)).iterator();
            }
        });

        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testFindMatch() {
        EvalContext context = new EvalContext(new Object() {
            @Override
            public Iterator iterator() {
                return new ArrayList(List.of(1, 2, 3)).iterator();
            }
        }, new Object() {
            @Override
            public Iterator iterator() {
                return new ArrayList(List.of(1, 3, 3, 2, 1)).iterator();
            }
        });

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreFunctionCall("count(.)", new EvaluationStack(), new EvalContext(new InitialContext(), new SelfContext())),
            new CoreFunctionCall("count(.))", new EvaluationStack(), new EvalContext(new InitialContext(), new SelfContext()))
        );

        assertTrue(coreOperationCompare.evaluate(context));

        context = new EvalContext(new Object() {
            @Override
            public Iterator iterator() {
                return new ArrayList(List.of(1, 2, 3)).iterator();
            }
        }, new Object() {
            @Override
            public Iterator iterator() {
                return new ArrayList(List.of(2, 3, 4)).iterator();
            }
        });

        assertFalse(coreOperationCompare.evaluate(context));
    }
}