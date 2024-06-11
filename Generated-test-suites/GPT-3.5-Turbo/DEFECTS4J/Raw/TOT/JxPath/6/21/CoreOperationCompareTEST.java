import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CoreOperationCompareTest {

    private CoreOperationCompare coreOperationCompare;
    private Expression left;
    private Expression right;
    private EvalContext evalContext;
    private List<Object> leftList;
    private List<Object> rightList;

    @Before
    public void setUp() {
        left = new Expression();
        right = new Expression();
        coreOperationCompare = new CoreOperationCompare(left, right);
        evalContext = new EvalContext();

        // Create sample data
        leftList = new ArrayList<>();
        leftList.add("apple");
        leftList.add("banana");
        leftList.add("cherry");

        rightList = new ArrayList<>();
        rightList.add("apple");
        rightList.add("grape");
        rightList.add("cherry");
    }

    @Test
    public void testEqualWithNullValues() {
        Object result = coreOperationCompare.equal(evalContext, left, right);

        Assert.assertFalse((Boolean)result);
    }

    @Test
    public void testEqualWithInitialContext() {
        EvalContext initialContext = new InitialContext();
        
        Object result = coreOperationCompare.equal(initialContext, left, right);

        Assert.assertFalse((Boolean)result);
    }

    @Test
    public void testEqualWithSelfContext() {
        EvalContext selfContext = new SelfContext();
        
        Object result = coreOperationCompare.equal(selfContext, left, right);

        Assert.assertFalse((Boolean)result);
    }

    @Test
    public void testEqualWithCollection() {
        Collection leftCollection = leftList;
        Collection rightCollection = rightList;

        Object result = coreOperationCompare.equal(leftCollection, rightCollection);

        Assert.assertFalse((Boolean)result);
    }

    @Test
    public void testEqualWithIterator() {
        Iterator<Object> leftIterator = leftList.iterator();
        Iterator<Object> rightIterator = rightList.iterator();

        Object result = coreOperationCompare.equal(leftIterator, rightIterator);

        Assert.assertFalse((Boolean)result);
    }

    @Test
    public void testEqualWithDifferentObjects() {
        Object leftObject = "apple";
        Object rightObject = "banana";

        Object result = coreOperationCompare.equal(leftObject, rightObject);

        Assert.assertFalse((Boolean)result);
    }

    @Test
    public void testEqualWithSameObjects() {
        Object leftObject = "apple";
        Object rightObject = "apple";

        Object result = coreOperationCompare.equal(leftObject, rightObject);

        Assert.assertTrue((Boolean)result);
    }

    @Test
    public void testEqualWithBooleanValues() {
        Object leftObject = true;
        Object rightObject = false;

        Object result = coreOperationCompare.equal(leftObject, rightObject);

        Assert.assertFalse((Boolean)result);
    }

    @Test
    public void testEqualWithNumberValues() {
        Object leftObject = 5;
        Object rightObject = 10;

        Object result = coreOperationCompare.equal(leftObject, rightObject);

        Assert.assertFalse((Boolean)result);
    }

    @Test
    public void testEqualWithStringValues() {
        Object leftObject = "hello";
        Object rightObject = "world";

        Object result = coreOperationCompare.equal(leftObject, rightObject);

        Assert.assertFalse((Boolean)result);
    }

    @Test
    public void testEqualWithEqualPointers() {
        Pointer leftPointer = new Pointer();
        Pointer rightPointer = leftPointer;

        Object result = coreOperationCompare.equal(leftPointer, rightPointer);

        Assert.assertTrue((Boolean)result);
    }

    @Test
    public void testEqualWithDifferentPointers() {
        Object leftValue = "apple";
        Object rightValue = "banana";
        Pointer leftPointer = new Pointer(leftValue);
        Pointer rightPointer = new Pointer(rightValue);

        Object result = coreOperationCompare.equal(leftPointer, rightPointer);

        Assert.assertFalse((Boolean)result);
    }

    @Test
    public void testContainsWithMatchingElement() {
        Iterator<Object> iterator = leftList.iterator();
        Object value = "banana";

        Object result = coreOperationCompare.contains(iterator, value);

        Assert.assertTrue((Boolean)result);
    }

    @Test
    public void testContainsWithNoMatchingElement() {
        Iterator<Object> iterator = leftList.iterator();
        Object value = "grape";

        Object result = coreOperationCompare.contains(iterator, value);

        Assert.assertFalse((Boolean)result);
    }

    @Test
    public void testFindMatchWithMatchingElements() {
        Iterator<Object> leftIterator = leftList.iterator();
        Iterator<Object> rightIterator = rightList.iterator();

        Object result = coreOperationCompare.findMatch(leftIterator, rightIterator);

        Assert.assertTrue((Boolean)result);
    }

    @Test
    public void testFindMatchWithNoMatchingElements() {
        List<Object> additionalList = new ArrayList<>();
        additionalList.add("mango");
        additionalList.add("kiwi");

        rightList.addAll(additionalList);

        Iterator<Object> leftIterator = leftList.iterator();
        Iterator<Object> rightIterator = rightList.iterator();

        Object result = coreOperationCompare.findMatch(leftIterator, rightIterator);

        Assert.assertFalse((Boolean)result);
    }

    @Test
    public void testEqualWithMatchingElementInCollection() {
        Collection leftCollection = leftList;
        Object rightObject = "banana";

        Object result = coreOperationCompare.equal(leftCollection, rightObject);

        Assert.assertTrue((Boolean)result);
    }

    @Test
    public void testEqualWithNoMatchingElementInCollection() {
        Collection leftCollection = leftList;
        Object rightObject = "grape";

        Object result = coreOperationCompare.equal(leftCollection, rightObject);

        Assert.assertFalse((Boolean)result);
    }

    @Test
    public void testEqualWithMatchingElementInIterator() {
        Iterator<Object> iterator = leftList.iterator();
        Object value = "banana";

        Object result = coreOperationCompare.equal(iterator, value);

        Assert.assertTrue((Boolean)result);
    }

    @Test
    public void testEqualWithNoMatchingElementInIterator() {
        Iterator<Object> iterator = leftList.iterator();
        Object value = "grape";

        Object result = coreOperationCompare.equal(iterator, value);

        Assert.assertFalse((Boolean)result);
    }

    @Test
    public void testEqualWithMatchingElementsInIterator() {
        Iterator<Object> iterator = leftList.iterator();
        Object value = "apple";

        Object result = coreOperationCompare.equal(iterator, value);

        Assert.assertTrue((Boolean)result);
    }

    @Test
    public void testEqualWithNullElementsInIterator() {
        List<Object> nullList = new ArrayList<>();
        nullList.add(null);

        Iterator<Object> iterator = nullList.iterator();
        Object value = null;

        Object result = coreOperationCompare.equal(iterator, value);

        Assert.assertTrue((Boolean)result);
    }
}