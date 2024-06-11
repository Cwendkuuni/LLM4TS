package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath. Pointer;
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
    public void testEqual_Boolean_Boolean() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreConstantExpression(true),
            new CoreConstantExpression(true)
        );

        EvalContext context = new EvalContext(new MockAxisContext());
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqual_Boolean_Boolean_False() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreConstantExpression(true),
            new CoreConstantExpression(false)
        );

        EvalContext context = new EvalContext(new MockAxisContext());
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqual_Number_Number() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreConstantExpression(1.0),
            new CoreConstantExpression(1.0)
        );

        EvalContext context = new EvalContext(new MockAxisContext());
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqual_Number_Number_Different() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreConstantExpression(1.0),
            new CoreConstantExpression(2.0)
        );

        EvalContext context = new EvalContext(new MockAxisContext());
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqual_String_String() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreConstantExpression("foo"),
            new CoreConstantExpression("foo")
        );

        EvalContext context = new EvalContext(new MockAxisContext());
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqual_String_String_Different() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreConstantExpression("foo"),
            new CoreConstantExpression("bar")
        );

        EvalContext context = new EvalContext(new MockAxisContext());
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqual_Pointer_Pointer() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreConstantExpression(new Pointer("node1")),
            new CoreConstantExpression(new Pointer("node1"))
        );

        EvalContext context = new EvalContext(new MockAxisContext());
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqual_Pointer_Pointer_Different() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreConstantExpression(new Pointer("node1")),
            new CoreConstantExpression(new Pointer("node2"))
        );

        EvalContext context = new EvalContext(new MockAxisContext());
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqual_Collection_Collection() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreConstantExpression(new ArrayList<Object>() {{ add(1); add(2); }}),
            new CoreConstantExpression(new ArrayList<Object>() {{ add(1); add(2); }})
        );

        EvalContext context = new EvalContext(new MockAxisContext());
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqual_Collection_Collection_DifferentSize() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreConstantExpression(new ArrayList<Object>() {{ add(1); add(2); }}),
            new CoreConstantExpression(new ArrayList<Object>() {{ add(1); }})
        );

        EvalContext context = new EvalContext(new MockAxisContext());
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqual_Collection_Collection_DifferentElement() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreConstantExpression(new ArrayList<Object>() {{ add(1); add(2); }}),
            new CoreConstantExpression(new ArrayList<Object>() {{ add(1); add("foo"); }})
        );

        EvalContext context = new EvalContext(new MockAxisContext());
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqual_Iterator_Object() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreConstantExpression(new Iterator<Object>() {
                @Override
                public boolean hasNext() {
                    return true;
                }

                @Override
                public Object next() {
                    return 1;
                }
            }),
            new CoreConstantExpression(1)
        );

        EvalContext context = new EvalContext(new MockAxisContext());
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqual_Object_Iterator() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreConstantExpression(1),
            new CoreConstantExpression(new Iterator<Object>() {
                @Override
                public boolean hasNext() {
                    return true;
                }

                @Override
                public Object next() {
                    return 1;
                }
            })
        );

        EvalContext context = new EvalContext(new MockAxisContext());
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqual_Iterator_Iterator() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreConstantExpression(new Iterator<Object>() {
                @Override
                public boolean hasNext() {
                    return true;
                }

                @Override
                public Object next() {
                    return 1;
                }
            }),
            new CoreConstantExpression(new Iterator<Object>() {
                @Override
                public boolean hasNext() {
                    return true;
                }

                @Override
                public Object next() {
                    return 1;
                }
            })
        );

        EvalContext context = new EvalContext(new MockAxisContext());
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqual_Iterator_Iterator_Different() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreConstantExpression(new Iterator<Object>() {
                @Override
                public boolean hasNext() {
                    return true;
                }

                @Override
                public Object next() {
                    return 1;
                }
            }),
            new CoreConstantExpression(new Iterator<Object>() {
                @Override
                public boolean hasNext() {
                    return true;
                }

                @Override
                public Object next() {
                    return 2;
                }
            })
        );

        EvalContext context = new EvalContext(new MockAxisContext());
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testContains_Iterator_Object() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreConstantExpression(new Iterator<Object>() {
                @Override
                public boolean hasNext() {
                    return true;
                }

                @Override
                public Object next() {
                    return 1;
                }
            }),
            new CoreConstantExpression(1)
        );

        EvalContext context = new EvalContext(new MockAxisContext());
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testContains_Iterator_Object_NotContained() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreConstantExpression(new Iterator<Object>() {
                @Override
                public boolean hasNext() {
                    return true;
                }

                @Override
                public Object next() {
                    return 1;
                }
            }),
            new CoreConstantExpression(2)
        );

        EvalContext context = new EvalContext(new MockAxisContext());
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testFindMatch_Iterator_Iterator() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreConstantExpression(new Iterator<Object>() {
                @Override
                public boolean hasNext() {
                    return true;
                }

                @Override
                public Object next() {
                    return 1;
                }
            }),
            new CoreConstantExpression(new Iterator<Object>() {
                @Override
                public boolean hasNext() {
                    return true;
                }

                @Override
                public Object next() {
                    return 1;
                }
            })
        );

        EvalContext context = new EvalContext(new MockAxisContext());
        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testFindMatch_Iterator_Iterator_NotMatch() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new CoreConstantExpression(new Iterator<Object>() {
                @Override
                public boolean hasNext() {
                    return true;
                }

                @Override
                public Object next() {
                    return 1;
                }
            }),
            new CoreConstantExpression(new Iterator<Object>() {
                @Override
                public boolean hasNext() {
                    return true;
                }

                @Override
                public Object next() {
                    return 2;
                }
            })
        );

        EvalContext context = new EvalContext(new MockAxisContext());
        assertFalse(coreOperationCompare.evaluate(context));
    }
}

class MockAxisContext implements EvalContext {
    @Override
    public Pointer getSingleNodePointer() {
        return null;
    }

    @Override
    public Collection getNodes() {
        return null;
    }
}