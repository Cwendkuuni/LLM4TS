package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CoreOperationCompareTest {

    @Test
    void testEqualWhenBothNull() {
        CoreOperationCompare comparison = new CoreOperationCompare(
            new CoreConstantExpression(null),
            new CoreConstantExpression(null)
        );

        assertTrue(comparison.evaluate(new EvalContext(new Pointer())).getValue());
    }

    @Test
    void testEqualWhenOneNull() {
        CoreOperationCompare comparison = new CoreOperationCompare(
            new CoreConstantExpression(null),
            new CoreConstantExpression("true")
        );

        assertFalse(comparison.evaluate(new EvalContext(new Pointer())).getValue());
    }

    @Test
    void testEqualWhenBothBooleanTrue() {
        CoreOperationCompare comparison = new CoreOperationCompare(
            new CoreConstantExpression(true),
            new CoreConstantExpression(true)
        );

        assertTrue(comparison.evaluate(new EvalContext(new Pointer())).getValue());
    }

    @Test
    void testEqualWhenBothBooleanFalse() {
        CoreOperationCompare comparison = new CoreOperationCompare(
            new CoreConstantExpression(false),
            new CoreConstantExpression(false)
        );

        assertTrue(comparison.evaluate(new EvalContext(new Pointer())).getValue());
    }

    @Test
    void testEqualWhenBothBooleanDifferent() {
        CoreOperationCompare comparison = new CoreOperationCompare(
            new CoreConstantExpression(true),
            new CoreConstantExpression(false)
        );

        assertFalse(comparison.evaluate(new EvalContext(new Pointer())).getValue());
    }

    @Test
    void testEqualWhenBothNumbersEqual() {
        CoreOperationCompare comparison = new CoreOperationCompare(
            new CoreConstantExpression(1.0),
            new CoreConstantExpression(1.0)
        );

        assertTrue(comparison.evaluate(new EvalContext(new Pointer())).getValue());
    }

    @Test
    void testEqualWhenBothNumbersNotEqual() {
        CoreOperationCompare comparison = new CoreOperationCompare(
            new CoreConstantExpression(1.0),
            new CoreConstantExpression(2.0)
        );

        assertFalse(comparison.evaluate(new EvalContext(new Pointer())).getValue());
    }

    @Test
    void testEqualWhenBothStringsEqual() {
        CoreOperationCompare comparison = new CoreOperationCompare(
            new CoreConstantExpression("abc"),
            new CoreConstantExpression("abc")
        );

        assertTrue(comparison.evaluate(new EvalContext(new Pointer())).getValue());
    }

    @Test
    void testEqualWhenBothStringsDifferent() {
        CoreOperationCompare comparison = new CoreOperationCompare(
            new CoreConstantExpression("abc"),
            new CoreConstantExpression("def")
        );

        assertFalse(comparison.evaluate(new EvalContext(new Pointer())).getValue());
    }

    @Test
    void testEqualWhenOneIsPointerAndOtherIsBooleanTrue() {
        CoreOperationCompare comparison = new CoreOperationCompare(
            new CoreConstantExpression(new Pointer() {
                @Override
                public Object getValue() {
                    return true;
                }
            }),
            new CoreConstantExpression(true)
        );

        assertFalse(comparison.evaluate(new EvalContext(new Pointer())).getValue());
    }

    @Test
    void testEqualWhenOneIsPointerAndOtherIsBooleanFalse() {
        CoreOperationCompare comparison = new CoreOperationCompare(
            new CoreConstantExpression(new Pointer() {
                @Override
                public Object getValue() {
                    return false;
                }
            }),
            new CoreConstantExpression(false)
        );

        assertTrue(comparison.evaluate(new EvalContext(new Pointer())).getValue());
    }

    @Test
    void testEqualWhenOneIsPointerAndOtherIsPointerToSameObject() {
        CoreOperationCompare comparison = new CoreOperationCompare(
            new CoreConstantExpression(new Pointer() {
                @Override
                public Object getValue() {
                    return new Object();
                }
            }),
            new CoreConstantExpression(new Pointer() {
                @Override
                public Object getValue() {
                    return new Object();
                }
            })
        );

        assertTrue(comparison.evaluate(new EvalContext(new Pointer())).getValue());
    }

    @Test
    void testEqualWhenOneIsPointerAndOtherIsPointerToDifferentObjects() {
        CoreOperationCompare comparison = new CoreOperationCompare(
            new CoreConstantExpression(new Pointer() {
                @Override
                public Object getValue() {
                    return new Object();
                }
            }),
            new CoreConstantExpression(new Pointer() {
                @Override
                public Object getValue() {
                    return new Object();
                }
            })
        );

        assertFalse(comparison.evaluate(new EvalContext(new Pointer())).getValue());
    }

    @Test
    void testEqualWhenOneIsPointerAndOtherIsCollectionWithOneElement() {
        CoreOperationCompare comparison = new CoreOperationCompare(
            new CoreConstantExpression(new Pointer() {
                @Override
                public Object getValue() {
                    return new Object();
                }
            }),
            new CoreCollectionExpression(List.of(new CoreConstantExpression(new Object())))
        );

        assertFalse(comparison.evaluate(new EvalContext(new Pointer())).getValue());
    }

    @Test
    void testEqualWhenOneIsCollectionWithOneElementAndOtherIsPointer() {
        CoreOperationCompare comparison = new CoreOperationCompare(
            new CoreCollectionExpression(List.of(new CoreConstantExpression(new Pointer() {
                @Override
                public Object getValue() {
                    return new Object();
                }
            }))),
            new CoreConstantExpression(new Pointer() {
                @Override
                public Object getValue() {
                    return new Object();
                }
            })
        );

        assertFalse(comparison.evaluate(new EvalContext(new Pointer())).getValue());
    }

    @Test
    void testEqualWhenOneIsCollectionWithOneElementAndOtherIsCollectionWithOneElement() {
        CoreOperationCompare comparison = new CoreOperationCompare(
            new CoreCollectionExpression(List.of(new CoreConstantExpression(new Pointer() {
                @Override
                public Object getValue() {
                    return new Object();
                }
            }))),
            new CoreCollectionExpression(List.of(new CoreConstantExpression(new Pointer() {
                @Override
                public Object getValue() {
                    return new Object();
                }
            })))
        );

        assertTrue(comparison.evaluate(new EvalContext(new Pointer())).getValue());
    }

    @Test
    void testEqualWhenOneIsCollectionWithMultipleElementsAndOtherIsPointer() {
        CoreOperationCompare comparison = new CoreOperationCompare(
            new CoreCollectionExpression(List.of(
                new CoreConstantExpression(new Pointer() {
                    @Override
                    public Object getValue() {
                        return new Object();
                    }
                }),
                new CoreConstantExpression(new Pointer() {
                    @Override
                    public Object getValue() {
                        return new Object();
                    }
                })
            )),
            new CoreConstantExpression(new Pointer() {
                @Override
                public Object getValue() {
                    return new Object();
                }
            })
        );

        assertFalse(comparison.evaluate(new EvalContext(new Pointer())).getValue());
    }

    @Test
    void testEqualWhenOneIsPointerAndOtherIsCollectionWithMultipleElements() {
        CoreOperationCompare comparison = new CoreOperationCompare(
            new CoreConstantExpression(new Pointer() {
                @Override
                public Object getValue() {
                    return new Object();
                }
            }),
            new CoreCollectionExpression(List.of(
                new CoreConstantExpression(new Pointer() {
                    @Override
                    public Object getValue() {
                        return new Object();
                    }
                }),
                new CoreConstantExpression(new Pointer() {
                    @Override
                    public Object getValue() {
                        return new Object();
                    }
                })
            ))
        );

        assertFalse(comparison.evaluate(new EvalContext(new Pointer())).getValue());
    }

    @Test
    void testEqualWhenBothAreCollectionsWithSameElements() {
        CoreOperationCompare comparison = new CoreOperationCompare(
            new CoreCollectionExpression(List.of(
                new CoreConstantExpression(new Pointer() {
                    @Override
                    public Object getValue() {
                        return new Object();
                    }
                }),
                new CoreConstantExpression(new Pointer() {
                    @Override
                    public Object getValue() {
                        return new Object();
                    }
                })
            )),
            new CoreCollectionExpression(List.of(
                new CoreConstantExpression(new Pointer() {
                    @Override
                    public Object getValue() {
                        return new Object();
                    }
                }),
                new CoreConstantExpression(new Pointer() {
                    @Override
                    public Object getValue() {
                        return new Object();
                    }
                })
            ))
        );

        assertTrue(comparison.evaluate(new EvalContext(new Pointer())).getValue());
    }

    @Test
    void testEqualWhenBothAreCollectionsWithDifferentElements() {
        CoreOperationCompare comparison = new CoreOperationCompare(
            new CoreCollectionExpression(List.of(
                new CoreConstantExpression(new Pointer() {
                    @Override
                    public Object getValue() {
                        return new Object();
                    }
                }),
                new CoreConstantExpression(new Pointer() {
                    @Override
                    public Object getValue() {
                        return new Object();
                    }
                })
            )),
            new CoreCollectionExpression(List.of(
                new CoreConstantExpression(new Pointer() {
                    @Override
                    public Object getValue() {
                        return new Object();
                    }
                }),
                new CoreConstantExpression(new Pointer() {
                    @Override
                    public Object getValue() {
                        return new Object();
                    }
                }),
                new CoreConstantExpression(new Pointer() {
                    @Override
                    public Object getValue() {
                        return new Object();
                    }
                })
            ))
        );

        assertFalse(comparison.evaluate(new EvalContext(new Pointer())).getValue());
    }

    @ParameterizedTest
    @MethodSource("provideTestCasesForUnequalComparisons")
    void testUnequal(Expression left, Expression right) {
        CoreOperationCompare comparison = new CoreOperationCompare(left, right);

        assertFalse(comparison.evaluate(new EvalContext(new Pointer())).getValue());
    }

    static Stream<Object[]> provideTestCasesForUnequalComparisons() {
        return Stream.of(
            new Object[] {
                new CoreConstantExpression(1.0),
                new CoreConstantExpression(2.0)
            },
            new Object[] {
                new CoreConstantExpression("abc"),
                new CoreConstantExpression("def")
            },
            new Object[] {
                new CoreConstantExpression(true),
                new CoreConstantExpression(false)
            },
            new Object[] {
                new CoreConstantExpression(new Pointer() {
                    @Override
                    public Object getValue() {
                        return new Object();
                    }
                }),
                new CoreConstantExpression(new Pointer() {
                    @Override
                    public Object getValue() {
                        return new Object();
                    }
                })
            },
            new Object[] {
                new CoreConstantExpression(new Pointer() {
                    @Override
                    public Object getValue() {
                        return new Object();
                    }
                }),
                new CoreConstantExpression(new CoreConstantExpression(new Pointer() {
                    @Override
                    public Object getValue() {
                        return new Object();
                    }
                }))
            },
            new Object[] {
                new CoreConstantExpression(new Pointer() {
                    @Override
                    public Object getValue() {
                        return new Object();
                    }
                }),
                new CoreCollectionExpression(List.of(new CoreConstantExpression(new Object())))
            },
            new Object[] {
                new CoreCollectionExpression(List.of(new CoreConstantExpression(new Pointer() {
                    @Override
                    public Object getValue() {
                        return new Object();
                    }
                }))),
                new CoreConstantExpression(new Pointer() {
                    @Override
                    public Object getValue() {
                        return new Object();
                    }
                })
            },
            new Object[] {
                new CoreCollectionExpression(List.of(new CoreConstantExpression(new Pointer() {
                    @Override
                    public Object getValue() {
                        return new Object();
                    }
                }))),
                new CoreCollectionExpression(List.of(new CoreConstantExpression(new Pointer() {
                    @Override
                    public Object getValue() {
                        return new Object();
                    }
                }), new CoreConstantExpression(new Pointer() {
                    @Override
                    public Object getValue() {
                        return new Object();
                    }
                })))
            }
        );
    }
}