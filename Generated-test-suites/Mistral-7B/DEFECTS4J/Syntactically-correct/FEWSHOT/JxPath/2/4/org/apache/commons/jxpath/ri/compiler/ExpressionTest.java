package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.cli.Utils;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.model.NodePointer;
import org.apache.commons.jxpath.QName;
import org.apache.commons.jxpath.util.ValueUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

public class ExpressionTest {
    private EvalContext context;

    @Before
    public void setUp() {
        context = new EvalContext(null, null, null, null, null);
    }

    @Test
    public void testContextDependencyDefault() {
        AbstractExpression exp = new AbstractExpression() {
            @Override
            public boolean computeContextDependent() {
                return false;
            }
        };
        Assert.assertFalse(exp.isContextDependent());
    }

    @Test
    public void testContextDependencyCompute() {
        AbstractExpression exp = new AbstractExpression() {
            @Override
            public boolean computeContextDependent() {
                return true;
            }
        };
        Assert.assertTrue(exp.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        NumberLiteralExpression exp = new NumberLiteralExpression("1");
        Object result = exp.computeValue(context);
        Assert.assertEquals(1.0, result.doubleValue());
    }

    @Test
    public void testCompute() {
        NumberLiteralExpression exp = new NumberLiteralExpression("1");
        Object result = exp.compute(context);
        Assert.assertEquals(1.0, result.doubleValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterateNullContext() {
        AbstractExpression exp = new AbstractExpression() {
            @Override
            public Iterator iterate(EvalContext context) {
                throw new UnsupportedOperationException();
            }
        };
        exp.iterate(null);
    }

    @Test
    public void testIterateEmpty() {
        NumberLiteralExpression exp = new NumberLiteralExpression("1");
        Iterator it = exp.iterate(context);
        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void testIterateSingleValue() {
        NumberLiteralExpression exp = new NumberLiteralExpression("1");
        Iterator it = exp.iterate(context);
        Object value = it.next();
        Assert.assertEquals(1.0, value.doubleValue());
        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void testIterateSinglePointer() {
        PointerExpression exp = new PointerExpression(new NodePointer("root", 0));
        Iterator it = exp.iterate(context);
        Object value = it.next();
        Pointer pointer = (Pointer) value;
        Assert.assertEquals("root", pointer.getCollectionName());
        Assert.assertEquals(0, pointer.getNodeIndex());
        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void testIterateMultipleValues() {
        NumberLiteralExpression exp1 = new NumberLiteralExpression("1");
        NumberLiteralExpression exp2 = new NumberLiteralExpression("2");
        Iterator it = new CompositeExpression(new Expression[]{exp1, exp2}).iterate(context);
        Object value1 = it.next();
        Object value2 = it.next();
        Assert.assertEquals(1.0, value1.doubleValue());
        Assert.assertEquals(2.0, value2.doubleValue());
        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void testIterateMultiplePointers() {
        PointerExpression exp1 = new PointerExpression(new NodePointer("root1", 0));
        PointerExpression exp2 = new PointerExpression(new NodePointer("root2", 0));
        Iterator it = new CompositeExpression(new Expression[]{exp1, exp2}).iterate(context);
        Object value1 = it.next();
        Object value2 = it.next();
        Pointer pointer1 = (Pointer) value1;
        Pointer pointer2 = (Pointer) value2;
        Assert.assertEquals("root1", pointer1.getCollectionName());
        Assert.assertEquals(0, pointer1.getNodeIndex());
        Assert.assertEquals("root2", pointer2.getCollectionName());
        Assert.assertEquals(0, pointer2.getNodeIndex());
        Assert.assertFalse(it.hasNext());
    }

    @Test
    public void testIteratePointersEmpty() {
        AbstractExpression exp = new AbstractExpression() {
            @Override
            public Iterator iteratePointers(EvalContext context) {
                return Collections.EMPTY_LIST.iterator();
            }
        };
        Iterator it = exp.iteratePointers(context);
        Assert.assertFalse(it.hasNext());
    }
}