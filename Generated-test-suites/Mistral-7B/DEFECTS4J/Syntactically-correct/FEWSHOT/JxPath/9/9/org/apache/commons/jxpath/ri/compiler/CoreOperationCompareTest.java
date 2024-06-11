package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testCoreOperationCompare() {
        assertNotNull(new CoreOperationCompare(null, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationCompareNullArg() {
        new CoreOperationCompare(null, new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        });
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationCompare op = new CoreOperationCompare(new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        }, new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        });
        assertEquals(2, op.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationCompare op = new CoreOperationCompare(new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        }, new Expression() {
            @Override
            public Object compute(EvalContext context) {
                return null;
            }
        });
        assertTrue(op.isSymmetric());
    }

    @Test
    public void testEqualNullContexts() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return null;
                }
            }, new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return null;
                }
            });
        assertFalse(op.equal(context, null, null));
    }

    @Test
    public void testEqualNullLeft() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return null;
                }
            }, new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return "foo";
                }
            });
        assertFalse(op.equal(context, null, "foo"));
    }

    @Test
    public void testEqualNullRight() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return "foo";
                }
            }, new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return null;
                }
            });
        assertFalse(op.equal(context, "foo", null));
    }

    @Test
    public void testEqualBooleanValues() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return InfoSetUtil.booleanValue(true);
                }
            }, new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return InfoSetUtil.booleanValue(true);
                }
            });
        assertTrue(op.equal(context, null, null));
    }

    @Test
    public void testEqualBooleanValuesDifferent() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return InfoSetUtil.booleanValue(true);
                }
            }, new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return InfoSetUtil.booleanValue(false);
                }
            });
        assertFalse(op.equal(context, null, null));
    }

    @Test
    public void testEqualNumberValues() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return InfoSetUtil.doubleValue(1.0);
                }
            }, new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return InfoSetUtil.doubleValue(1.0);
                }
            });
        assertTrue(op.equal(context, null, null));
    }

    @Test
    public void testEqualNumberValuesDifferent() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return InfoSetUtil.doubleValue(1.0);
                }
            }, new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return InfoSetUtil.doubleValue(2.0);
                }
            });
        assertFalse(op.equal(context, null, null));
    }

    @Test
    public void testEqualStringValues() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return InfoSetUtil.stringValue("foo");
                }
            }, new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return InfoSetUtil.stringValue("foo");
                }
            });
        assertTrue(op.equal(context, null, null));
    }

    @Test
    public void testEqualStringValuesDifferent() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return InfoSetUtil.stringValue("foo");
                }
            }, new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return InfoSetUtil.stringValue("bar");
                }
            });
        assertFalse(op.equal(context, null, null));
    }

    @Test
    public void testEqualPointerValues() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return new Pointer(1, null, null, null);
                }
            }, new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return new Pointer(1, null, null, null);
                }
            });
        assertTrue(op.equal(context, null, null));
    }

    @Test
    public void testEqualPointerValuesDifferent() {
        EvalContext context = new EvalContext();
        CoreOperationCompare op = new CoreOperationCompare(
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return new Pointer(1, null, null, null);
                }
            }, new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return new Pointer(2, null, null, null);
                }
            });
        assertFalse(op.equal(context, null, null));
    }

    @Test
    public void testContainsIteratorsEmptyLeft() {
        Iterator left = Collections.emptyIterator();
        Object right = "foo";
        CoreOperationCompare op = new CoreOperationCompare(
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return left;
                }
            }, new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return right;
                }
            });
        assertFalse(op.contains(left, right));
    }

    @Test
    public void testContainsIteratorsEmptyRight() {
        Iterator left = "foo".iterator();
        Object right = Collections.emptyIterator();
        CoreOperationCompare op = new CoreOperationCompare(
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return left;
                }
            }, new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return right;
                }
            });
        assertFalse(op.contains(left, right));
    }

    @Test
    public void testContainsIteratorsNoMatch() {
        List list = new ArrayList();
        list.add("foo");
        Iterator lit = list.iterator();
        Iterator rit = "bar".iterator();
        CoreOperationCompare op = new CoreOperationCompare(
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return lit;
                }
            }, new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return rit;
                }
            });
        assertFalse(op.contains(lit, rit));
    }

    @Test
    public void testContainsIteratorsMatch() {
        List list = new ArrayList();
        list.add("foo");
        Iterator lit = list.iterator();
        Iterator rit = "foo".iterator();
        CoreOperationCompare op = new CoreOperationCompare(
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return lit;
                }
            }, new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return rit;
                }
            });
        assertTrue(op.contains(lit, rit));
    }

    @Test
    public void testFindMatchIteratorsEmptyLeft() {
        Iterator left = Collections.emptyIterator();
        Iterator right = "foo".iterator();
        CoreOperationCompare op = new CoreOperationCompare(
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return left;
                }
            }, new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return right;
                }
            });
        assertFalse(op.findMatch(left, right));
    }

    @Test
    public void testFindMatchIteratorsEmptyRight() {
        Iterator left = "foo".iterator();
        Iterator right = Collections.emptyIterator();
        CoreOperationCompare op = new CoreOperationCompare(
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return left;
                }
            }, new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return right;
                }
            });
        assertFalse(op.findMatch(left, right));
    }

    @Test
    public void testFindMatchIteratorsNoMatch() {
        List list = new ArrayList();
        list.add("foo");
        Iterator lit = list.iterator();
        Iterator rit = "bar".iterator();
        CoreOperationCompare op = new CoreOperationCompare(
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return lit;
                }
            }, new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return rit;
                }
            });
        assertFalse(op.findMatch(lit, rit));
    }

    @Test
    public void testFindMatchIteratorsMatch() {
        List list = new ArrayList();
        list.add("foo");
        Iterator lit = list.iterator();
        Iterator rit = "foo".iterator();
        CoreOperationCompare op = new CoreOperationCompare(
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return lit;
                }
            }, new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return rit;
                }
            });
        assertTrue(op.findMatch(lit, rit));
    }

    @Test
    public void testFindMatchIteratorsMatchDifferentOrder() {
        List list = new ArrayList();
        list.add("foo");
        list.add("bar");
        Iterator lit = list.iterator();
        Iterator rit = "foo bar".iterator();
        CoreOperationCompare op = new CoreOperationCompare(
            new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return lit;
                }
            }, new Expression() {
                @Override
                public Object compute(EvalContext context) {
                    return rit;
                }
            });
        assertTrue(op.findMatch(lit, rit));
    }

}