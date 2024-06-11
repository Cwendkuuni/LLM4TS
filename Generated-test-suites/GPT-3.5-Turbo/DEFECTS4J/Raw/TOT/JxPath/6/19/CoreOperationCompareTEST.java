import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.base.JXPathBasicBeanInfo;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationCompareTest {

    private CoreOperationCompare coreOperationCompare;

    @Before
    public void setUp() {
        coreOperationCompare = new CoreOperationCompare();
    }
    
    @Test
    public void testEqualNullValues() {
        EvalContext context = new EvalContext();
        Expression left = new Expression(null);
        Expression right = new Expression(null);
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertTrue(result);
    }
    
    @Test
    public void testEqualDifferentValues() {
        EvalContext context = new EvalContext();
        Expression left = new Expression("value1");
        Expression right = new Expression("value2");
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertFalse(result);
    }
    
    @Test
    public void testEqualEqualValues() {
        EvalContext context = new EvalContext();
        Expression left = new Expression("value");
        Expression right = new Expression("value");
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertTrue(result);
    }
    
    @Test
    public void testEqualSameObject() {
        EvalContext context = new EvalContext();
        Object obj = new Object();
        Expression left = new Expression(obj);
        Expression right = new Expression(obj);
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertTrue(result);
    }
    
    @Test
    public void testEqualDifferentObjects() {
        EvalContext context = new EvalContext();
        Expression left = new Expression(new Object());
        Expression right = new Expression(new Object());
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertFalse(result);
    }
    
    @Test
    public void testEqualBooleanValues() {
        EvalContext context = new EvalContext();
        Expression left = new Expression(true);
        Expression right = new Expression(false);
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertFalse(result);
    }
    
    @Test
    public void testEqualNumberValues() {
        EvalContext context = new EvalContext();
        Expression left = new Expression(1);
        Expression right = new Expression(2);
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertFalse(result);
    }
    
    @Test
    public void testEqualStringValues() {
        EvalContext context = new EvalContext();
        Expression left = new Expression("value");
        Expression right = new Expression("value");
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertTrue(result);
    }
    
    @Test
    public void testEqualDifferentTypes() {
        EvalContext context = new EvalContext();
        Expression left = new Expression("1");
        Expression right = new Expression(1);
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertFalse(result);
    }
    
    @Test
    public void testEqualCollectionWithSingleValue() {
        EvalContext context = new EvalContext();
        Collection<String> collection = new ArrayList<String>(Arrays.asList("value"));
        Expression left = new Expression(collection);
        Expression right = new Expression("value");
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertTrue(result);
    }
    
    @Test
    public void testEqualCollectionWithoutValue() {
        EvalContext context = new EvalContext();
        Collection<String> collection = new ArrayList<String>(Arrays.asList("value1", "value2", "value3"));
        Expression left = new Expression(collection);
        Expression right = new Expression("value");
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertFalse(result);
    }
    
    @Test
    public void testEqualCollectionWithMultipleValues() {
        EvalContext context = new EvalContext();
        Collection<String> collection = new ArrayList<String>(Arrays.asList("value1", "value2", "value3"));
        Expression left = new Expression(collection);
        Expression right = new Expression("value2");
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertTrue(result);
    }
    
    @Test
    public void testEqualCollectionWithDifferentTypes() {
        EvalContext context = new EvalContext();
        Collection<Integer> collection = new ArrayList<Integer>(Arrays.asList(1, 2, 3));
        Expression left = new Expression(collection);
        Expression right = new Expression("1");
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertFalse(result);
    }
    
    @Test
    public void testEqualCollectionOfPointers() {
        EvalContext context = new EvalContext();
        Pointer pointer1 = new Pointer("value1");
        Pointer pointer2 = new Pointer("value2");
        Collection<Pointer> collection = new ArrayList<Pointer>(Arrays.asList(pointer1, pointer2));
        Expression left = new Expression(collection);
        Expression right = new Expression(new ArrayList<>(Arrays.asList("value1", "value2")));
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertTrue(result);
    }
    
    @Test
    public void testEqualCollectionOfPointersWithoutValues() {
        EvalContext context = new EvalContext();
        Pointer pointer1 = new Pointer("value1");
        Pointer pointer2 = new Pointer("value2");
        Collection<Pointer> collection = new ArrayList<Pointer>(Arrays.asList(pointer1, pointer2));
        Expression left = new Expression(collection);
        Expression right = new Expression(new ArrayList<>(Arrays.asList("value3", "value4")));
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertFalse(result);
    }
    
    @Test
    public void testEqualCollectionOfPointersWithDifferentTypes() {
        EvalContext context = new EvalContext();
        Pointer pointer1 = new Pointer("value1");
        Pointer pointer2 = new Pointer("value2");
        Collection<Pointer> collection = new ArrayList<Pointer>(Arrays.asList(pointer1, pointer2));
        Expression left = new Expression(collection);
        Expression right = new Expression(new ArrayList<>(Arrays.asList(1, 2)));
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertFalse(result);
    }
    
    @Test
    public void testContainsWithIteratorContainingValue() {
        Iterator<String> iterator = new ArrayList<String>(Arrays.asList("value1", "value2")).iterator();
        Expression left = new Expression(iterator);
        Expression right = new Expression("value1");
        
        boolean result = coreOperationCompare.contains(left, right);
        
        assertTrue(result);
    }
    
    @Test
    public void testContainsWithIteratorWithoutValue() {
        Iterator<String> iterator = new ArrayList<String>(Arrays.asList("value1", "value2")).iterator();
        Expression left = new Expression(iterator);
        Expression right = new Expression("value");
        
        boolean result = coreOperationCompare.contains(left, right);
        
        assertFalse(result);
    }
    
    @Test
    public void testFindMatchWithMatchingIterators() {
        Iterator<String> iterator1 = new ArrayList<String>(Arrays.asList("value1", "value2")).iterator();
        Iterator<String> iterator2 = new ArrayList<String>(Arrays.asList("value2", "value3")).iterator();
        Expression left = new Expression(iterator1);
        Expression right = new Expression(iterator2);
        
        boolean result = coreOperationCompare.findMatch(left, right);
        
        assertTrue(result);
    }
    
    @Test
    public void testFindMatchWithoutMatchingIterators() {
        Iterator<String> iterator1 = new ArrayList<String>(Arrays.asList("value1", "value2")).iterator();
        Iterator<String> iterator2 = new ArrayList<String>(Arrays.asList("value3", "value4")).iterator();
        Expression left = new Expression(iterator1);
        Expression right = new Expression(iterator2);
        
        boolean result = coreOperationCompare.findMatch(left, right);
        
        assertFalse(result);
    }
    
    @Test
    public void testFindMatchWithDifferentTypes() {
        Iterator<String> iterator1 = new ArrayList<String>(Arrays.asList("1", "2")).iterator();
        Iterator<Integer> iterator2 = new ArrayList<Integer>(Arrays.asList(2, 3)).iterator();
        Expression left = new Expression(iterator1);
        Expression right = new Expression(iterator2);
        
        boolean result = coreOperationCompare.findMatch(left, right);
        
        assertFalse(result);
    }
    
    @Test
    public void testEqualBetweenPointers() {
        EvalContext context = new EvalContext();
        Pointer pointer1 = new Pointer("value");
        Pointer pointer2 = new Pointer("value");
        Expression left = new Expression(pointer1);
        Expression right = new Expression(pointer2);
        
        boolean result = coreOperationCompare.equal(context, left, right);
        
        assertTrue(result);
    }

}