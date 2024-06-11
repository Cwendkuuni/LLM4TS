package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.IterateIterable;
import org.apache.commons.jxpath.ri.Node;
import org.apache.commons.jxpath.ri.nodes.NodeIterator;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan("true", new LiteralExpression(1.5d), new LiteralExpression(1.5d));
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public Iterator getIterator() {
                return new IterateIterable().asIterator(new NodeIterator() {
                    private int index = 0;

                    @Override
                    public boolean hasNext() {
                        return index++ < 5;
                    }

                    @Override
                    public Node nextNode() {
                        return new Node() {
                            @Override
                            public Object getValue() {
                                return index;
                            }
                        };
                    }
                });
            }
        });

        assertEquals(Boolean.TRUE, expr.computeValue(context));

        expr = new CoreOperationGreaterThan("true", new LiteralExpression(1.5d), new LiteralExpression(2.0d));
        assertFalse(expr.computeValue(context).booleanValue());

        expr = new CoreOperationGreaterThan("true", new LiteralExpression(2.0d), new LiteralExpression(1.5d));
        assertTrue(expr.computeValue(context).booleanValue());

        expr = new CoreOperationGreaterThanOrEqual("true", new LiteralExpression(1.5d), new LiteralExpression(1.5d));
        assertTrue(expr.computeValue(context).booleanValue());

        expr = new CoreOperationGreaterThanOrEqual("true", new LiteralExpression(1.5d), new LiteralExpression(2.0d));
        assertFalse(expr.computeValue(context).booleanValue());

        expr = new CoreOperationGreaterThanOrEqual("true", new LiteralExpression(2.0d), new LiteralExpression(1.5d));
        assertTrue(expr.computeValue(context).booleanValue());

        expr = new CoreOperationLessThan("true", new LiteralExpression(1.5d), new LiteralExpression(1.5d));
        assertFalse(expr.computeValue(context).booleanValue());

        expr = new CoreOperationLessThan("true", new LiteralExpression(1.5d), new LiteralExpression(1.0d));
        assertTrue(expr.computeValue(context).booleanValue());

        expr = new CoreOperationLessThanOrEqual("true", new LiteralExpression(1.5d), new LiteralExpression(1.5d));
        assertTrue(expr.computeValue(context).booleanValue());

        expr = new CoreOperationLessThanOrEqual("true", new LiteralExpression(1.5d), new LiteralExpression(1.0d));
        assertTrue(expr.computeValue(context).booleanValue());

        expr = new CoreOperationLessThanOrEqual("true", new LiteralExpression(1.0d), new LiteralExpression(1.5d));
        assertFalse(expr.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueWithIterators() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan("true", new IteratorExpression(new NodeIterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Node nextNode() {
                return new Node() {
                    @Override
                    public Object getValue() {
                        return 1;
                    }
                };
            }
        }), new IteratorExpression(new NodeIterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Node nextNode() {
                return new Node() {
                    @Override
                    public Object getValue() {
                        return 2;
                    }
                };
            }
        }));

        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public Iterator getIterator() {
                return new IterateIterable().asIterator(new NodeIterator() {
                    private int index = 0;

                    @Override
                    public boolean hasNext() {
                        return index++ < 5;
                    }

                    @Override
                    public Node nextNode() {
                        return new Node() {
                            @Override
                            public Object getValue() {
                                return index;
                            }
                        };
                    }
                });
            }
        });

        assertFalse(expr.computeValue(context).booleanValue());

        expr = new CoreOperationGreaterThan("true", new IteratorExpression(new NodeIterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Node nextNode() {
                return new Node() {
                    @Override
                    public Object getValue() {
                        return 2;
                    }
                };
            }
        }), new IteratorExpression(new NodeIterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Node nextNode() {
                return new Node() {
                    @Override
                    public Object getValue() {
                        return 1;
                    }
                };
            }
        }));

        assertTrue(expr.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueWithCollections() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan("true", new IteratorExpression(new NodeIterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Node nextNode() {
                return new Node() {
                    @Override
                    public Object getValue() {
                        return 1;
                    }
                };
            }
        }), new LiteralExpression(new HashSet() {{
            add(2);
        }}));

        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public Iterator getIterator() {
                return new IterateIterable().asIterator(new NodeIterator() {
                    private int index = 0;

                    @Override
                    public boolean hasNext() {
                        return index++ < 5;
                    }

                    @Override
                    public Node nextNode() {
                        return new Node() {
                            @Override
                            public Object getValue() {
                                return index;
                            }
                        };
                    }
                });
            }
        });

        assertFalse(expr.computeValue(context).booleanValue());

        expr = new CoreOperationGreaterThan("true", new LiteralExpression(new HashSet() {{
            add(2);
        }}), new IteratorExpression(new NodeIterator() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Node nextNode() {
                return new Node() {
                    @Override
                    public Object getValue() {
                        return 1;
                    }
                };
            }
        }));

        assertTrue(expr.computeValue(context).booleanValue());
    }

    @Test
    public void testComputeValueWithNull() {
        CoreOperationRelationalExpression expr = new CoreOperationGreaterThan("true", null, new LiteralExpression(1.5d));
        assertNull(expr.computeValue(new EvalContext(new InfoSet())));

        expr = new CoreOperationGreaterThan("true", new LiteralExpression(1.5d), null);
        assertNull(expr.computeValue(new EvalContext(new InfoSet())));
    }
}