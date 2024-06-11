import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class CoreOperationCompareTest {
    private CoreOperationCompare coreOperationCompare;

    @Before
    public void setUp() {
        coreOperationCompare = new CoreOperationCompare(null, null);
    }

    @Test
    public void testEqual() {
        // Test when the left and right expressions are null
        EvalContext context1 = new EvalContext();
        Expression left1 = null;
        Expression right1 = null;
        boolean result1 = coreOperationCompare.equal(context1, left1, right1);
        assertTrue(result1);

        // Test when the left expression is null and the right expression is not null
        Expression left2 = null;
        Expression right2 = new Expression();
        boolean result2 = coreOperationCompare.equal(context1, left2, right2);
        assertFalse(result2);

        // Test when the left expression is not null and the right expression is null
        Expression left3 = new Expression();
        Expression right3 = null;
        boolean result3 = coreOperationCompare.equal(context1, left3, right3);
        assertFalse(result3);

        // Test when the left and right expressions are not null and they are equal
        Expression left4 = new Expression();
        Expression right4 = new Expression();
        boolean result4 = coreOperationCompare.equal(context1, left4, right4);
        assertTrue(result4);

        // Test when the left and right expressions are not null and they are not equal
        Expression left5 = new Expression();
        Expression right5 = new Expression();
        right5.setCompute("Test compute");
        boolean result5 = coreOperationCompare.equal(context1, left5, right5);
        assertFalse(result5);
    }

    @Test
    public void testContains() {
        // Test when the iterator is empty and the value is null
        Iterator it1 = Collections.emptyIterator();
        Object value1 = null;
        boolean result1 = coreOperationCompare.contains(it1, value1);
        assertFalse(result1);
        
        // Test when the iterator is not empty and the value is null
        List<String> list = new ArrayList<>();
        list.add("Test 1");
        list.add("Test 2");
        Iterator it2 = list.iterator();
        boolean result2 = coreOperationCompare.contains(it2, value1);
        assertFalse(result2);

        // Test when the iterator is empty and the value is not null
        String value3 = "Test 3";
        Iterator it3 = Collections.emptyIterator();
        boolean result3 = coreOperationCompare.contains(it3, value3);
        assertFalse(result3);

        // Test when the iterator is not empty and the value is not null and exists in the iterator
        List<String> list2 = new ArrayList<>();
        list2.add("Test 4");
        list2.add("Test 5");
        String value4 = "Test 4";
        Iterator it4 = list2.iterator();
        boolean result4 = coreOperationCompare.contains(it4, value4);
        assertTrue(result4);

        // Test when the iterator is not empty and the value is not null and does not exist in the iterator
        List<String> list3 = new ArrayList<>();
        list3.add("Test 6");
        list3.add("Test 7");
        String value5 = "Test 8";
        Iterator it5 = list3.iterator();
        boolean result5 = coreOperationCompare.contains(it5, value5);
        assertFalse(result5);
    }

    @Test
    public void testFindMatch() {
        // Test when both iterators are empty
        Iterator lit1 = Collections.emptyIterator();
        Iterator rit1 = Collections.emptyIterator();
        boolean result1 = coreOperationCompare.findMatch(lit1, rit1);
        assertFalse(result1);

        // Test when the left iterator is not empty and the right iterator is empty
        List<String> list1 = new ArrayList<>();
        list1.add("Test 1");
        list1.add("Test 2");
        Iterator lit2 = list1.iterator();
        Iterator rit2 = Collections.emptyIterator();
        boolean result2 = coreOperationCompare.findMatch(lit2, rit2);
        assertFalse(result2);

        // Test when the left iterator is empty and the right iterator is not empty
        List<String> list2 = new ArrayList<>();
        list2.add("Test 3");
        list2.add("Test 4");
        Iterator lit3 = Collections.emptyIterator();
        Iterator rit3 = list2.iterator();
        boolean result3 = coreOperationCompare.findMatch(lit3, rit3);
        assertFalse(result3);

        // Test when both iterators have common elements
        List<String> list3 = new ArrayList<>();
        list3.add("Test 5");
        list3.add("Test 6");
        List<String> list4 = new ArrayList<>();
        list4.add("Test 6");
        list4.add("Test 7");
        Iterator lit4 = list3.iterator();
        Iterator rit4 = list4.iterator();
        boolean result4 = coreOperationCompare.findMatch(lit4, rit4);
        assertTrue(result4);

        // Test when both iterators don't have any common elements
        List<String> list5 = new ArrayList<>();
        list5.add("Test 8");
        list5.add("Test 9");
        List<String> list6 = new ArrayList<>();
        list6.add("Test 10");
        list6.add("Test 11");
        Iterator lit5 = list5.iterator();
        Iterator rit5 = list6.iterator();
        boolean result5 = coreOperationCompare.findMatch(lit5, rit5);
        assertFalse(result5);
    }


    @Test
    public void testEqualWithPointers() {
        // Test when the left and right values are both Pointers and they are equal
        EvalContext context1 = new EvalContext();
        Pointer pointer1 = new Pointer();
        pointer1.setValue("Test 1");
        Pointer pointer2 = new Pointer();
        pointer2.setValue("Test 1");
        boolean result1 = coreOperationCompare.equal(pointer1, pointer2);
        assertTrue(result1);

        // Test when the left value is a Pointer and the right value is not
        Pointer pointer3 = new Pointer();
        pointer3.setValue("Test 2");
        String value1 = "Test 2";
        boolean result2 = coreOperationCompare.equal(pointer3, value1);
        assertTrue(result2);

        // Test when the left value is not a Pointer and the right value is a Pointer
        String value2 = "Test 3";
        Pointer pointer4 = new Pointer();
        pointer4.setValue("Test 3");
        boolean result3 = coreOperationCompare.equal(value2, pointer4);
        assertTrue(result3);

        // Test when the left and right values are both Pointers and they are not equal
        Pointer pointer5 = new Pointer();
        pointer5.setValue("Test 4");
        Pointer pointer6 = new Pointer();
        pointer6.setValue("Test 5");
        boolean result4 = coreOperationCompare.equal(pointer5, pointer6);
        assertFalse(result4);

        // Test when the left value is a Pointer and the right value is not equal to the Pointer's value
        Pointer pointer7 = new Pointer();
        pointer7.setValue("Test 6");
        String value3 = "Test 7";
        boolean result5 = coreOperationCompare.equal(pointer7, value3);
        assertFalse(result5);

        // Test when the left value is not equal to the Pointer's value and the right value is a Pointer
        String value4 = "Test 8";
        Pointer pointer8 = new Pointer();
        pointer8.setValue("Test 9");
        boolean result6 = coreOperationCompare.equal(value4, pointer8);
        assertFalse(result6);
    }

    @Test
    public void testEqualWithBoolean() {
        // Test when the left and right values are both Booleans and they are equal
        boolean l1 = true;
        boolean r1 = true;
        boolean result1 = coreOperationCompare.equal(l1, r1);
        assertTrue(result1);

        // Test when the left value is Boolean and the right value is not
        boolean l2 = true;
        Object r2 = null;
        boolean result2 = coreOperationCompare.equal(l2, r2);
        assertFalse(result2);

        // Test when the left value is not Boolean and the right value is Boolean
        Object l3 = null;
        boolean r3 = true;
        boolean result3 = coreOperationCompare.equal(l3, r3);
        assertFalse(result3);

        // Test when the left and right values are both Booleans and they are not equal
        boolean l4 = true;
        boolean r4 = false;
        boolean result4 = coreOperationCompare.equal(l4, r4);
        assertFalse(result4);
    }

    @Test
    public void testEqualWithNumber() {
        // Test when the left and right values are both Numbers and they are equal
        double l1 = 1.23;
        double r1 = 1.23;
        boolean result1 = coreOperationCompare.equal(l1, r1);
        assertTrue(result1);

        // Test when the left value is Number and the right value is not
        double l2 = 1.23;
        String r2 = "Test";
        boolean result2 = coreOperationCompare.equal(l2, r2);
        assertFalse(result2);

        // Test when the left value is not Number and the right value is Number
        String l3 = "Test";
        double r3 = 1.23;
        boolean result3 = coreOperationCompare.equal(l3, r3);
        assertFalse(result3);

        // Test when the left and right values are both Numbers and they are not equal
        double l4 = 1.23;
        double r4 = 4.56;
        boolean result4 = coreOperationCompare.equal(l4, r4);
        assertFalse(result4);
    }

    @Test
    public void testEqualWithString() {
        // Test when the left and right values are both Strings and they are equal
        String l1 = "Test 1";
        String r1 = "Test 1";
        boolean result1 = coreOperationCompare.equal(l1, r1);
        assertTrue(result1);

        // Test when the left value is String and the right value is not
        String l2 = "Test 2";
        Object r2 = null;
        boolean result2 = coreOperationCompare.equal(l2, r2);
        assertFalse(result2);

        // Test when the left value is not String and the right value is String
        Object l3 = null;
        String r3 = "Test 3";
        boolean result3 = coreOperationCompare.equal(l3, r3);
        assertFalse(result3);

        // Test when the left and right values are both Strings and they are not equal
        String l4 = "Test 4";
        String r4 = "Test 5";
        boolean result4 = coreOperationCompare.equal(l4, r4);
        assertFalse(result4);
    }

    @Test
    public void testEqualWithValue() {
        // Test when the left value is null and the right value is null
        Object l1 = null;
        Object r1 = null;
        boolean result1 = coreOperationCompare.equal(l1, r1);
        assertTrue(result1);

        // Test when the left value is null and the right value is not null
        Object l2 = null;
        Object r2 = "Test";
        boolean result2 = coreOperationCompare.equal(l2, r2);
        assertFalse(result2);

        // Test when the left value is not null and the right value is null
        Object l3 = "Test";
        Object r3 = null;
        boolean result3 = coreOperationCompare.equal(l3, r3);
        assertFalse(result3);

        // Test when the left value is not null and the right value is not null and they are equal
        Object l4 = "Test";
        Object r4 = "Test";
        boolean result4 = coreOperationCompare.equal(l4, r4);
        assertTrue(result4);

        // Test when the left value is not null and the right value is not null and they are not equal
        Object l5 = "Test 1";
        Object r5 = "Test 2";
        boolean result5 = coreOperationCompare.equal(l5, r5);
        assertFalse(result5);
    }
}