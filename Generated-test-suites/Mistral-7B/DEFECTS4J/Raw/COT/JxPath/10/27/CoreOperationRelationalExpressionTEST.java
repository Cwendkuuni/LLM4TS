import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.CoreOperation;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for CoreOperationRelationalExpression class
 */
public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue_Boolean_Boolean() {
        Expression leftExpression = new CoreOperationBinaryExpression(new CoreStringExpression("true"), new CoreStringExpression("true"));
        Expression rightExpression = new CoreStringExpression("true");
        CoreOperationRelationalExpression relationalExpression = new CoreOperationGtExpression(new CoreOperation[] { leftExpression, rightExpression });

        assertTrue(relationalExpression.computeValue(new EvalContext("")).equals(Boolean.TRUE));
    }

    @Test
    public void testComputeValue_Boolean_Boolean_false() {
        Expression leftExpression = new CoreStringExpression("true");
        Expression rightExpression = new CoreStringExpression("false");
        CoreOperationRelationalExpression relationalExpression = new CoreOperationGtExpression(new CoreOperation[] { leftExpression, rightExpression });

        assertFalse(relationalExpression.computeValue(new EvalContext("")).equals(Boolean.TRUE));
    }

    @Test
    public void testComputeValue_Number_Number() {
        Expression leftExpression = new CoreNumberExpression(1.0);
        Expression rightExpression = new CoreNumberExpression(2.0);
        CoreOperationRelationalExpression relationalExpression = new CoreOperationGtExpression(new CoreOperation[] { leftExpression, rightExpression });

        assertFalse(relationalExpression.computeValue(new EvalContext("")).equals(Boolean.TRUE));
    }

    @Test
    public void testComputeValue_Number_Number_true() {
        Expression leftExpression = new CoreNumberExpression(2.0);
        Expression rightExpression = new CoreNumberExpression(1.0);
        CoreOperationRelationalExpression relationalExpression = new CoreOperationGtExpression(new CoreOperation[] { leftExpression, rightExpression });

        assertTrue(relationalExpression.computeValue(new EvalContext("")).equals(Boolean.TRUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_InvalidArguments() {
        Expression leftExpression = new CoreStringExpression("true");
        Expression rightExpression = null;
        CoreOperationRelationalExpression relationalExpression = new CoreOperationGtExpression(new CoreOperation[] { leftExpression, rightExpression });

        relationalExpression.computeValue(new EvalContext(""));
    }

    @Test
    public void testComputeValue_Iterator_Iterator() {
        Iterator<Object> leftIterator = new ArrayList<>(List.of(1, 2, 3)).iterator();
        Iterator<Object> rightIterator = new ArrayList<>(List.of(1, 3, 4)).iterator();
        CoreOperationRelationalExpression relationalExpression = new CoreOperationGtExpression(new CoreOperation[] { new CoreIteratorExpression(leftIterator), new CoreIteratorExpression(rightIterator) });

        assertFalse(relationalExpression.computeValue(new EvalContext("")).equals(Boolean.TRUE));
    }

    @Test
    public void testComputeValue_Iterator_Iterator_true() {
        Iterator<Object> leftIterator = new ArrayList<>(List.of(1.0, 2.0, 3.0)).iterator();
        Iterator<Object> rightIterator = new ArrayList<>(List.of(1.0, 2.0)).iterator();
        CoreOperationRelationalExpression relationalExpression = new CoreOperationGtExpression(new CoreOperation[] { new CoreIteratorExpression(leftIterator), new CoreIteratorExpression(rightIterator) });

        assertTrue(relationalExpression.computeValue(new EvalContext("")).equals(Boolean.TRUE));
    }

    @Test
    public void testComputeValue_Collection_Collection() {
        Collection<Object> leftCollection = new ArrayList<>(List.of(1, 2, 3));
        Collection<Object> rightCollection = new ArrayList<>(List.of(1, 3, 4));
        CoreOperationRelationalExpression relationalExpression = new CoreOperationGtExpression(new CoreOperation[] { new CoreCollectionExpression(leftCollection), new CoreCollectionExpression(rightCollection) });

        assertFalse(relationalExpression.computeValue(new EvalContext("")).equals(Boolean.TRUE));
    }

    @Test
    public void testComputeValue_Collection_Collection_true() {
        Collection<Object> leftCollection = new ArrayList<>(List.of(1.0, 2.0, 3.0));
        Collection<Object> rightCollection = new ArrayList<>(List.of(1.0, 2.0));
        CoreOperationRelationalExpression relationalExpression = new CoreOperationGtExpression(new CoreOperation[] { new CoreCollectionExpression(leftCollection), new CoreCollectionExpression(rightCollection) });

        assertTrue(relationalExpression.computeValue(new EvalContext("")).equals(Boolean.TRUE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_InvalidArguments_Collection() {
        Collection<Object> leftCollection = new ArrayList<>(List.of(1, 2, 3));
        Iterator<Object> rightIterator = new ArrayList<>(List.of(1, 3, 4)).iterator();
        CoreOperationRelationalExpression relationalExpression = new CoreOperationGtExpression(new CoreOperation[] { new CoreCollectionExpression(leftCollection), new CoreIteratorExpression(rightIterator) });

        relationalExpression.computeValue(new EvalContext(""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeValue_InvalidArguments_Iterator() {
        Iterator<Object> leftIterator = new ArrayList<>(List.of(1, 2, 3)).iterator();
        Collection<Object> rightCollection = new ArrayList<>(List.of(1, 3, 4));
        CoreOperationRelationalExpression relationalExpression = new CoreOperationGtExpression(new CoreOperation[] { new CoreIteratorExpression(leftIterator), new CoreCollectionExpression(rightCollection) });

        relationalExpression.computeValue(new EvalContext(""));
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression relationalExpression = new CoreOperationGtExpression(new CoreOperation[0]);

        int precedence = relationalExpression.getPrecedence();

        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric_Gt() {
        CoreOperationRelationalExpression relationalExpression = new CoreOperationGtExpression(new CoreOperation[0]);

        assertFalse(relationalExpression.isSymmetric());
    }

    @Test
    public void testIsSymmetric_Lt() {
        CoreOperationRelationalExpression relationalExpression = new CoreOperationLtExpression(new CoreOperation[0]);

        assertFalse(relationalExpression.isSymmetric());
    }

    @Test
    public void testIsSymmetric_Ge() {
        CoreOperationRelationalExpression relationalExpression = new CoreOperationGeExpression(new CoreOperation[0]);

        assertFalse(relationalExpression.isSymmetric());
    }

    @Test
    public void testIsSymmetric_Le() {
        CoreOperationRelationalExpression relationalExpression = new CoreOperationLeExpression(new CoreOperation[0]);

        assertFalse(relationalExpression.isSymmetric());
    }

}