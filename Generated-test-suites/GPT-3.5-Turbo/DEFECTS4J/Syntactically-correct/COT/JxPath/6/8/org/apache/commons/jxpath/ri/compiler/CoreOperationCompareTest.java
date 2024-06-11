import org.junit.Test;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

public class CoreOperationCompareTest {
    
    @Test
    public void testEqual() {
        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        
        // Test when both arg1 and arg2 are null
        Object result = compare.equal(context, null, null);
        assert(result == true);
        
        // Test when arg1 is null and arg2 is not null
        result = compare.equal(context, null, 5);
        assert(result == false);
        
        // Test when arg2 is null and arg1 is not null
        result = compare.equal(context, 5, null);
        assert(result == false);
        
        // Test when arg1 and arg2 are equal numbers
        result = compare.equal(context, 5, 5);
        assert(result == true);
        
        // Test when arg1 and arg2 are different numbers
        result = compare.equal(context, 5, 10);
        assert(result == false);
        
        // Test when arg1 and arg2 are equal Strings
        result = compare.equal(context, "hello", "hello");
        assert(result == true);
        
        // Test when arg1 and arg2 are different Strings
        result = compare.equal(context, "hello", "world");
        assert(result == false);
        
        // Test when arg1 and arg2 are equal Pointers
        Pointer pointer1 = new Pointer("path1");
        Pointer pointer2 = new Pointer("path1");
        result = compare.equal(context, pointer1, pointer2);
        assert(result == true);
        
        // Test when arg1 and arg2 are different Pointers
        pointer2 = new Pointer("path2");
        result = compare.equal(context, pointer1, pointer2);
        assert(result == false);
        
        // Test when arg1 and arg2 are equal Booleans
        result = compare.equal(context, true, true);
        assert(result == true);
        
        // Test when arg1 and arg2 are different Booleans
        result = compare.equal(context, true, false);
        assert(result == false);
        
        // Test when arg1 and arg2 are equal Collections
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(1, 2, 3);
        result = compare.equal(context, list1, list2);
        assert(result == true);
        
        // Test when arg1 and arg2 are different Collections
        list2 = Arrays.asList(4, 5, 6);
        result = compare.equal(context, list1, list2);
        assert(result == false);
        
        // Test when arg1 and arg2 are equal Iterators
        Iterator<Integer> iterator1 = list1.iterator();
        Iterator<Integer> iterator2 = list2.iterator();
        result = compare.equal(context, iterator1, iterator2);
        assert(result == true);
        
        // Test when arg1 and arg2 are different Iterators
        iterator2 = Arrays.asList(4, 5, 6).iterator();
        result = compare.equal(context, iterator1, iterator2);
        assert(result == false);
        
        // Test when arg1 and arg2 are equal InitialContext
        InitialContext initialContext1 = new InitialContext();
        InitialContext initialContext2 = new InitialContext();
        result = compare.equal(context, initialContext1, initialContext2);
        assert(result == true);
        
        // Test when arg1 and arg2 are different InitialContext
        initialContext2 = new SelfContext();
        result = compare.equal(context, initialContext1, initialContext2);
        assert(result == false);
        
        // Test when arg1 and arg2 are equal SelfContext
        SelfContext selfContext1 = new SelfContext();
        SelfContext selfContext2 = new SelfContext();
        result = compare.equal(context, selfContext1, selfContext2);
        assert(result == true);
        
        // Test when arg1 and arg2 are different SelfContext
        selfContext2 = new InitialContext();
        result = compare.equal(context, selfContext1, selfContext2);
        assert(result == false);
    }
    
    @Test
    public void testContains() {
        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        Iterator<Integer> iterator = Arrays.asList(1, 2, 3).iterator();
        
        // Test when iterator contains the value
        Object result = compare.contains(iterator, 2);
        assert(result == true);
        
        // Test when iterator does not contains the value
        result = compare.contains(iterator, 5);
        assert(result == false);
    }
    
    @Test
    public void testFindMatch() {
        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        Iterator<Integer> iterator1 = Arrays.asList(1, 2, 3).iterator();
        Iterator<Integer> iterator2 = Arrays.asList(3, 4, 5).iterator();
        
        // Test when there is a match between the two iterators
        Object result = compare.findMatch(iterator1, iterator2);
        assert(result == true);
        
        // Test when there is no match between the two iterators
        iterator1 = Arrays.asList(1, 2, 3).iterator();
        iterator2 = Arrays.asList(4, 5, 6).iterator();
        result = compare.findMatch(iterator1, iterator2);
        assert(result == false);
    }
    
    @Test
    public void testEqualWithPointers() {
        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        
        // Test when arg1 is an InitialContext or SelfContext and arg2 is a Pointer
        InitialContext initialContext = new InitialContext();
        Pointer pointer = new Pointer("path");
        Object result = compare.equal(context, initialContext, pointer);
        assert(result == true);
        
        // Test when arg1 is a Pointer and arg2 is an InitialContext or SelfContext
        result = compare.equal(context, pointer, initialContext);
        assert(result == true);
        
        // Test when arg1 and arg2 are Pointers and they are equal
        result = compare.equal(context, pointer, pointer);
        assert(result == true);
        
        // Test when arg1 and arg2 are Pointers and they are different
        Pointer pointer2 = new Pointer("path2");
        result = compare.equal(context, pointer, pointer2);
        assert(result == false);
    }
    
    @Test
    public void testEqualWithBoolean() {
        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        
        // Test when arg1 and arg2 are Booleans and they are equal
        Object result = compare.equal(context, true, true);
        assert(result == true);
        
        // Test when arg1 and arg2 are Booleans and they are different
        result = compare.equal(context, true, false);
        assert(result == false);
    }
    
    @Test
    public void testEqualWithNumber() {
        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        
        // Test when arg1 and arg2 are Numbers and they are equal
        Object result = compare.equal(context, 5, 5);
        assert(result == true);
        
        // Test when arg1 and arg2 are Numbers and they are different
        result = compare.equal(context, 5, 10);
        assert(result == false);
    }
    
    @Test
    public void testEqualWithString() {
        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        
        // Test when arg1 and arg2 are Strings and they are equal
        Object result = compare.equal(context, "hello", "hello");
        assert(result == true);
        
        // Test when arg1 and arg2 are Strings and they are different
        result = compare.equal(context, "hello", "world");
        assert(result == false);
    }
    
    @Test
    public void testEqualWithCollection() {
        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        
        // Test when arg1 and arg2 are Collections and they are equal
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(1, 2, 3);
        Object result = compare.equal(context, list1, list2);
        assert(result == true);
        
        // Test when arg1 and arg2 are Collections and they are different
        list2 = Arrays.asList(4, 5, 6);
        result = compare.equal(context, list1, list2);
        assert(result == false);
    }
    
    @Test
    public void testEqualWithIterator() {
        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        
        // Test when arg1 and arg2 are Iterators and they are equal
        Iterator<Integer> iterator1 = Arrays.asList(1, 2, 3).iterator();
        Iterator<Integer> iterator2 = Arrays.asList(1, 2, 3).iterator();
        Object result = compare.equal(context, iterator1, iterator2);
        assert(result == true);
        
        // Test when arg1 and arg2 are Iterators and they are different
        iterator2 = Arrays.asList(4, 5, 6).iterator();
        result = compare.equal(context, iterator1, iterator2);
        assert(result == false);
    }
    
    @Test
    public void testEqualWithInitialContext() {
        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        
        // Test when arg1 and arg2 are InitialContext and they are equal
        InitialContext initialContext1 = new InitialContext();
        InitialContext initialContext2 = new InitialContext();
        Object result = compare.equal(context, initialContext1, initialContext2);
        assert(result == true);
        
        // Test when arg1 and arg2 are InitialContext and they are different
        initialContext2 = new SelfContext();
        result = compare.equal(context, initialContext1, initialContext2);
        assert(result == false);
    }
    
    @Test
    public void testEqualWithSelfContext() {
        CoreOperationCompare compare = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        
        // Test when arg1 and arg2 are SelfContext and they are equal
        SelfContext selfContext1 = new SelfContext();
        SelfContext selfContext2 = new SelfContext();
        Object result = compare.equal(context, selfContext1, selfContext2);
        assert(result == true);
        
        // Test when arg1 and arg2 are SelfContext and they are different
        selfContext2 = new InitialContext();
        result = compare.equal(context, selfContext1, selfContext2);
        assert(result == false);
    }
    
}