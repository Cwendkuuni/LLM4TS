import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvaluationException;
import org.junit.Test;
import org.junit.Assert.*;

import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.ri.compiler.Constant;
import org.apache.commons.jxpath.ri.compiler.NumberConstant;
import org.apache.commons.jxpath.ri.compiler.StringConstant;
import org.apache.commons.jxpath.ri.compiler.Variable;
import org.apache.commons.jxpath.ri.compiler.VariableRef;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Test for {@link CoreOperationCompare} class.
 */
public class CoreOperationCompareTest {

    @Test
    public void testEqualBoolean() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Constant(true),
            new Constant(true)
        );

        JXPathContext context = new JXPathContext();
        EvaluationException e = assertThrows(EvaluationException.class, () -> compare.evaluate(context, null));
        assertEquals("result type mismatch: java.lang.Boolean and java.lang.Boolean", e.getMessage());
    }

    @Test
    public void testEqualBooleanTrue() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Constant(true),
            new Constant(true)
        );

        JXPathContext context = new JXPathContext();
        Object result = compare.evaluate(context, null);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testEqualBooleanFalse() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Constant(true),
            new Constant(false)
        );

        JXPathContext context = new JXPathContext();
        Object result = compare.evaluate(context, null);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testEqualNumber() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new NumberConstant(1.0),
            new NumberConstant(1.0)
        );

        JXPathContext context = new JXPathContext();
        Object result = compare.evaluate(context, null);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testEqualNumberDifferent() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new NumberConstant(1.0),
            new NumberConstant(2.0)
        );

        JXPathContext context = new JXPathContext();
        Object result = compare.evaluate(context, null);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testEqualString() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new StringConstant("foo"),
            new StringConstant("foo")
        );

        JXPathContext context = new JXPathContext();
        Object result = compare.evaluate(context, null);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testEqualStringDifferent() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new StringConstant("foo"),
            new StringConstant("bar")
        );

        JXPathContext context = new JXPathContext();
        Object result = compare.evaluate(context, null);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testEqualCollectionEmpty() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Variable("a"),
            new Variable("b")
        );

        JXPathContext context = new JXPathContext();
        context.setVariables(Collections.singletonMap("a", new Constant(Collections.emptyList())));
        context.setVariables(Collections.singletonMap("b", new Constant(Collections.emptyList())));

        Object result = compare.evaluate(context, null);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testEqualCollectionSameElements() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Variable("a"),
            new Variable("b")
        );

        JXPathContext context = new JXPathContext();
        context.setVariables(Collections.singletonMap("a", new Variable(0, new List[]{new StringConstant("foo")})));
        context.setVariables(Collections.singletonMap("b", new Variable(0, new List[]{new StringConstant("foo")})));

        Object result = compare.evaluate(context, null);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testEqualCollectionDifferentSize() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Variable("a"),
            new Variable("b")
        );

        JXPathContext context = new JXPathContext();
        context.setVariables(
            Collections.singletonMap("a", new Variable(0, new List[]{new StringConstant("foo")})));
        context.setVariables(
            Collections.singletonMap("b", new Variable(0, new List[]{new StringConstant("foo"), new StringConstant("bar")})));

        Object result = compare.evaluate(context, null);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testEqualCollectionSameElementsOrdered() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Variable("a"),
            new Variable("b")
        );

        JXPathContext context = new JXPathContext();
        context.setVariables(
            Collections.singletonMap("a", new Variable(0, new List[]{new StringConstant("foo"), new StringConstant("bar")})));
        context.setVariables(
            Collections.singletonMap("b", new Variable(0, new List[]{new StringConstant("bar"), new StringConstant("foo")})));

        Object result = compare.evaluate(context, null);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testEqualCollectionDifferentElements() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Variable("a"),
            new Variable("b")
        );

        JXPathContext context = new JXPathContext();
        context.setVariables(
            Collections.singletonMap("a", new Variable(0, new List[]{new StringConstant("foo")})));
        context.setVariables(
            Collections.singletonMap("b", new Variable(0, new List[]{new StringConstant("bar")})));

        Object result = compare.evaluate(context, null);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testContainsIteratorBoolean() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Variable("a"),
            new Constant(true)
        );

        JXPathContext context = new JXPathContext();
        context.setVariables(
            Collections.singletonMap("a", new Variable(0, new List[]{
                new VariableRef("b"),
                new Constant(true)
            })));

        Iterator<?> it = context.getVariables().get("a").getValue().iterator();
        Object result = compare.evaluate(context, new Object[]{it});
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testContainsIteratorBooleanTrue() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Variable("a"),
            new Constant(true)
        );

        JXPathContext context = new JXPathContext();
        context.setVariables(Collections.singletonMap("a", new Variable(0, new List[]{
            new VariableRef("b"),
            new Constant(true)
        })));

        Iterator<?> it = Arrays.asList(new VariableRef("b"), new Constant(true)).iterator();
        Object result = compare.evaluate(context, new Object[]{it});
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testContainsIteratorBooleanFalse() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Variable("a"),
            new Constant(true)
        );

        JXPathContext context = new JXPathContext();
        context.setVariables(Collections.singletonMap("a", new Variable(0, new List[]{
            new VariableRef("b"),
            new Constant(false)
        })));

        Iterator<?> it = Arrays.asList(new VariableRef("b"), new Constant(true)).iterator();
        Object result = compare.evaluate(context, new Object[]{it});
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testFindMatchIteratorIterator() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Variable("a"),
            new Variable("b")
        );

        JXPathContext context = new JXPathContext();
        context.setVariables(
            Collections.singletonMap("a", new Variable(0, new List[]{
                new VariableRef("c"),
                new VariableRef("d")
            })));
        context.setVariables(
            Collections.singletonMap("b", new Variable(0, new List[]{
                new VariableRef("e"),
                new VariableRef("f")
            })));

        Iterator<?> lit = Arrays.asList(new VariableRef("c"), new VariableRef("d")).iterator();
        Iterator<?> rit = Arrays.asList(new VariableRef("e"), new VariableRef("f")).iterator();

        Object result = compare.evaluate(context, new Object[]{lit, rit});
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testFindMatchIteratorIteratorDifferentCollections() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Variable("a"),
            new Variable("b")
        );

        JXPathContext context = new JXPathContext();
        context.setVariables(
            Collections.singletonMap("a", new Variable(0, new List[]{
                new VariableRef("c"),
                new VariableRef("d")
            })));
        context.setVariables(
            Collections.singletonMap("b", new Variable(0, new List[]{
                new VariableRef("e"),
                new VariableRef("f")
            })));

        Iterator<?> lit = Arrays.asList(new VariableRef("c"), new VariableRef("d")).iterator();
        Iterator<?> rit = Arrays.asList(new VariableRef("g"), new VariableRef("h")).iterator();

        Object result = compare.evaluate(context, new Object[]{lit, rit});
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testFindMatchIteratorIteratorSameElements() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Variable("a"),
            new Variable("b")
        );

        JXPathContext context = new JXPathContext();
        context.setVariables(
            Collections.singletonMap("a", new Variable(0, new List[]{
                new VariableRef("c"),
                new VariableRef("d")
            })));
        context.setVariables(
            Collections.singletonMap("b", new Variable(0, new List[]{
                new VariableRef("c"),
                new VariableRef("d")
            })));

        Iterator<?> lit = Arrays.asList(new VariableRef("c"), new VariableRef("d")).iterator();
        Iterator<?> rit = Arrays.asList(new VariableRef("c"), new VariableRef("d")).iterator();

        Object result = compare.evaluate(context, new Object[]{lit, rit});
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testFindMatchIteratorIteratorDifferentElements() {
        CoreOperationCompare compare = new CoreOperationCompare(
            new Variable("a"),
            new Variable("b")
        );

        JXPathContext context = new JXPathContext();
        context.setVariables(
            Collections.singletonMap("a", new Variable(0, new List[]{
                new VariableRef("c"),
                new VariableRef("d")
            })));
        context.setVariables(
            Collections.singletonMap("b", new Variable(0, new List[]{
                new VariableRef("e"),
                new VariableRef("f")
            })));

        Iterator<?> lit = Arrays.asList(new VariableRef("c"), new VariableRef("d")).iterator();
        Iterator<?> rit = Arrays.asList(new VariableRef("e"), new VariableRef("f")).iterator();

        Object result = compare.evaluate(context, new Object[]{lit, rit});
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }
}