package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvaluationContext;
import org.apache.commons.jxpath.ri.ITERATOR_TYPE;
import org.apache.commons.jxpath.ri.IteratingNodes;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Test class for CoreOperationRelationalExpression.
 */
public class CoreOperationRelationalExpressionTest {

    private EvaluationContext context;
    private Document document;

    @Before
    public void setUp() {
        context = new EvaluationContext();
        document = XPathTestHelper.newDocument();
    }

    /**
     * Test for >= operation.
     */
    @Test
    public void testGreaterOrEqual() {
        // given
        Node left = document.createNumberNode("5.0");
        Node right = document.createNumberNode("5.0");
        CoreOperationRelationalExpression expression = new GreaterOrEqualExpression(new VariableExpression("x"), new VariableExpression("y"));

        // when
        Object result = expression.computeValue(context);

        // then
        assertThat(result, is(Boolean.TRUE));
    }

    /**
     * Test for > operation.
     */
    @Test
    public void testGreater() {
        // given
        Node left = document.createNumberNode("5.0");
        Node right = document.createNumberNode("6.0");
        CoreOperationRelationalExpression expression = new GreaterExpression(new VariableExpression("x"), new VariableExpression("y"));

        // when
        Object result = expression.computeValue(context);

        // then
        assertThat(result, is(Boolean.TRUE));
    }

    /**
     * Test for <= operation.
     */
    @Test
    public void testLessOrEqual() {
        // given
        Node left = document.createNumberNode("5.0");
        Node right = document.createNumberNode("5.0");
        CoreOperationRelationalExpression expression = new LessOrEqualExpression(new VariableExpression("x"), new VariableExpression("y"));

        // when
        Object result = expression.computeValue(context);

        // then
        assertThat(result, is(Boolean.TRUE));
    }

    /**
     * Test for < operation.
     */
    @Test
    public void testLess() {
        // given
        Node left = document.createNumberNode("5.0");
        Node right = document.createNumberNode("4.0");
        CoreOperationRelationalExpression expression = new LessExpression(new VariableExpression("x"), new VariableExpression("y"));

        // when
        Object result = expression.computeValue(context);

        // then
        assertThat(result, is(Boolean.TRUE));
    }

    /**
     * Test for invalid number comparison.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidComparison() {
        // given
        Node left = document.createNode("invalid");
        Node right = document.createNumberNode("5.0");
        CoreOperationRelationalExpression expression = new GreaterExpression(new VariableExpression("x"), new VariableExpression("y"));

        // when
        expression.computeValue(context);

        // then
        // exception should be thrown
    }

    /**
     * Test for comparison with non-numeric value.
     */
    @Test
    public void testComparisonWithNonNumericValue() {
        // given
        Node left = document.createTextNode("5");
        Node right = document.createNumberNode("5.0");
        CoreOperationRelationalExpression expression = new GreaterExpression(new VariableExpression("x"), new VariableExpression("y"));

        // when
        Object result = expression.computeValue(context);

        // then
        assertThat(result, is(Boolean.TRUE));
    }

    /**
     * Test for comparison of empty nodes.
     */
    @Test
    public void testComparisonOfEmptyNodes() {
        // given
        Node left = document.createNode("empty");
        Node right = document.createNode("empty");
        CoreOperationRelationalExpression expression = new GreaterExpression(new VariableExpression("x"), new VariableExpression("y"));

        // when
        Object result = expression.computeValue(context);

        // then
        assertThat(result, is(Boolean.FALSE));
    }

    /**
     * Test for comparison with null value.
     */
    @Test
    public void testComparisonWithNullValue() {
        // given
        Node left = null;
        Node right = document.createNumberNode("5.0");
        CoreOperationRelationalExpression expression = new GreaterExpression(new VariableExpression("x"), new VariableExpression("y"));

        // when
        Object result = expression.computeValue(context);

        // then
        assertThat(result, is(Boolean.FALSE));
    }

    /**
     * Test for comparison of two empty Iterator.
     */
    @Test
    public void testComparisonOfEmptyIterators() {
        // given
        Iterator left = IteratingNodes.emptyIterator(ITERATOR_TYPE.NODE);
        Iterator right = IteratingNodes.emptyIterator(ITERATOR_TYPE.NODE);
        CoreOperationRelationalExpression expression = new GreaterExpression(new VariableExpression("x"), new VariableExpression("y"));

        // when
        Object result = expression.computeValue(context);

        // then
        assertThat(result, is(Boolean.FALSE));
    }

    /**
     * Test for comparison of one empty and one non-empty Iterator.
     */
    @Test
    public void testComparisonOfEmptyAndNonEmptyIterators() {
        // given
        Iterator left = IteratingNodes.emptyIterator(ITERATOR_TYPE.NODE);
        Iterator right = new NodeListIterator(document.getElementsByTagName("p"));
        CoreOperationRelationalExpression expression = new GreaterExpression(new VariableExpression("x"), new VariableExpression("y"));

        // when
        Object result = expression.computeValue(context);

        // then
        assertThat(result, is(Boolean.FALSE));
    }

    /**
     * Test for comparison of two non-empty Iterators.
     */
    @Test
    public void testComparisonOfNonEmptyIterators() {
        // given
        Iterator left = new NodeListIterator(document.getElementsByTagName("p"));
        Iterator right = new NodeListIterator(document.getElementsByTagName("p"));
        CoreOperationRelationalExpression expression = new GreaterExpression(new VariableExpression("x"), new VariableExpression("y"));

        // when
        Object result = expression.computeValue(context);

        // then
        assertThat(result, is(Boolean.FALSE));
    }

    /**
     * Test for comparison of two non-empty Iterators with different elements.
     */
    @Test
    public void testComparisonOfNonEmptyIteratorsWithDifferentElements() {
        // given
        Iterator left = new NodeListIterator(document.getElementsByTagName("p"));
        Iterator right = new NodeListIterator(document.getElementsByTagName("a"));
        CoreOperationRelationalExpression expression = new GreaterExpression(new VariableExpression("x"), new VariableExpression("y"));

        // when
        Object result = expression.computeValue(context);

        // then
        assertThat(result, is(Boolean.FALSE));
    }

    /**
     * Test for comparison of two non-empty Iterators with same elements.
     */
    @Test
    public void testComparisonOfNonEmptyIteratorsWithSameElements() {
        // given
        Iterator left = new NodeListIterator(document.getElementsByTagName("p"));
        Iterator right = new NodeListIterator(document.getElementsByTagName("p"));
        CoreOperationRelationalExpression expression = new GreaterExpression(new VariableExpression("x"), new VariableExpression("y"));

        // when
        Object result = expression.computeValue(context);

        // then
        assertThat(result, is(Boolean.FALSE));

        // when using findMatch
        result = expression.computeValue(context);

        // then
        assertThat(result, is(Boolean.FALSE));
    }

    /**
     * Test for comparison of two non-empty Iterators with same elements in different order.
     */
    @Test
    public void testComparisonOfNonEmptyIteratorsWithSameElementsInDifferentOrder() {
        // given
        Iterator left = new NodeListIterator(document.getElementsByTagName("p"));
        Iterator right = new NodeListIterator(document.getElementsByTagName("p"));
        CoreOperationRelationalExpression expression = new GreaterExpression(new VariableExpression("x"), new VariableExpression("y"));

        // when using findMatch
        Object result = expression.computeValue(context);

        // then
        assertThat(result, is(Boolean.FALSE));

        // when shuffling left iterator
        left = new ShufflingIterator(left);
        result = expression.computeValue(context);

        // then
        assertThat(result, is(Boolean.FALSE));
    }

    /**
     * Test for comparison of two non-empty Iterators with same elements but different data types.
     */
    @Test
    public void testComparisonOfNonEmptyIteratorsWithSameElementsButDifferentDataTypes() {
        // given
        Iterator left = new NodeListIterator(document.getElementsByTagName("p"));
        Iterator right = new NodeListIterator(document.getElementsByTagName("textArea"));
        CoreOperationRelationalExpression expression = new GreaterExpression(new VariableExpression("x"), new VariableExpression("y"));

        // when
        Object result = expression.computeValue(context);

        // then
        assertThat(result, is(Boolean.FALSE));
    }

    /**
     * Test for comparison of two non-empty Iterators with same elements but different data types and using "==" operator.
     */
    @Test
    public void testComparisonOfNonEmptyIteratorsWithSameElementsButDifferentDataTypesAndUsingEqualOperator() {
        // given
        Iterator left = new NodeListIterator(document.getElementsByTagName("p"));
        Iterator right = new NodeListIterator(document.getElementsByTagName("textArea"));
        CoreOperationRelationalExpression expression = new EqualsExpression(new VariableExpression("x"), new VariableExpression("y"));

        // when
        Object result = expression.computeValue(context);

        // then
        assertThat(result, is(Boolean.FALSE));
    }

    /**
     * Test for comparison of two non-empty Iterators with same elements but different data types and using "!=" operator.
     */
    @Test
    public void testComparisonOfNonEmptyIteratorsWithSameElementsButDifferentDataTypesAndUsingNotEqualOperator() {
        // given
        Iterator left = new NodeListIterator(document.getElementsByTagName("p"));
        Iterator right = new NodeListIterator(document.getElementsByTagName("textArea"));
        CoreOperationRelationalExpression expression = new NotEqualsExpression(new VariableExpression("x"), new VariableExpression("y"));

        // when
        Object result = expression.computeValue(context);

        // then
        assertThat(result, is(Boolean.TRUE));
    }
}