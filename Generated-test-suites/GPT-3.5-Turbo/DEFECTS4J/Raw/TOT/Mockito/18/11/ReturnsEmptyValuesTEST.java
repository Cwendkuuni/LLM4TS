import org.junit.Assert;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

    @Test
    public void testAnswerToString() {
        // Mock object
        MyMockObject mock = new MyMockObject();
        
        // Set up invocation
        InvocationOnMock invocation = new MyInvocation(mock, "toString");
        
        // Perform the answer
        Object answer = returnsEmptyValues.answer(invocation);
        
        // Verify the result
        Assert.assertEquals("Mock for MyMockObject, hashCode: " + mock.hashCode(), answer);
    }
    
    @Test
    public void testAnswerCompareToSameObject() {
        // Mock object
        MyMockObject mock = new MyMockObject();
        
        // Set up invocation with same object as argument
        InvocationOnMock invocation = new MyInvocation(mock, "compareTo", new Object[]{mock});
        
        // Perform the answer
        Object answer = returnsEmptyValues.answer(invocation);
        
        // Verify the result
        Assert.assertEquals(0, answer);
    }
    
    @Test
    public void testAnswerCompareToDifferentObject() {
        // Mock objects
        MyMockObject mock1 = new MyMockObject();
        MyMockObject mock2 = new MyMockObject();
        
        // Set up invocation with different object as argument
        InvocationOnMock invocation = new MyInvocation(mock1, "compareTo", new Object[]{mock2});
        
        // Perform the answer
        Object answer = returnsEmptyValues.answer(invocation);
        
        // Verify the result
        Assert.assertEquals(1, answer);
    }
    
    @Test
    public void testAnswerPrimitiveReturnType() {
        // Mock object
        MyMockObject mock = new MyMockObject();
        
        // Set up invocation with a method returning a primitive type
        InvocationOnMock invocation = new MyInvocation(mock, "getIntValue");
        
        // Perform the answer
        Object answer = returnsEmptyValues.answer(invocation);
        
        // Verify the result
        Assert.assertEquals(0, answer);
    }
    
    @Test
    public void testAnswerWrapperClassReturnType() {
        // Mock object
        MyMockObject mock = new MyMockObject();
        
        // Set up invocation with a method returning a wrapper class type
        InvocationOnMock invocation = new MyInvocation(mock, "getIntegerValue");
        
        // Perform the answer
        Object answer = returnsEmptyValues.answer(invocation);
        
        // Verify the result
        Assert.assertEquals(0, answer);
    }
    
    @Test
    public void testAnswerCollectionReturnType() {
        // Mock object
        MyMockObject mock = new MyMockObject();
        
        // Set up invocation with a method returning a Collection type
        InvocationOnMock invocation = new MyInvocation(mock, "getCollectionValue");
        
        // Perform the answer
        Object answer = returnsEmptyValues.answer(invocation);
        
        // Verify the result
        Assert.assertTrue(answer instanceof java.util.Collection);
        Assert.assertTrue(((java.util.Collection<?>) answer).isEmpty());
    }
    
    @Test
    public void testAnswerSetReturnType() {
        // Mock object
        MyMockObject mock = new MyMockObject();
        
        // Set up invocation with a method returning a Set type
        InvocationOnMock invocation = new MyInvocation(mock, "getSetValue");
        
        // Perform the answer
        Object answer = returnsEmptyValues.answer(invocation);
        
        // Verify the result
        Assert.assertTrue(answer instanceof java.util.Set);
        Assert.assertTrue(((java.util.Set<?>) answer).isEmpty());
    }
    
    @Test
    public void testAnswerSortedSetReturnType() {
        // Mock object
        MyMockObject mock = new MyMockObject();
        
        // Set up invocation with a method returning a SortedSet type
        InvocationOnMock invocation = new MyInvocation(mock, "getSortedSetValue");
        
        // Perform the answer
        Object answer = returnsEmptyValues.answer(invocation);
        
        // Verify the result
        Assert.assertTrue(answer instanceof java.util.SortedSet);
        Assert.assertTrue(((java.util.SortedSet<?>) answer).isEmpty());
    }
    
    @Test
    public void testAnswerLinkedHashSetReturnType() {
        // Mock object
        MyMockObject mock = new MyMockObject();
        
        // Set up invocation with a method returning a LinkedHashSet type
        InvocationOnMock invocation = new MyInvocation(mock, "getLinkedHashSetValue");
        
        // Perform the answer
        Object answer = returnsEmptyValues.answer(invocation);
        
        // Verify the result
        Assert.assertTrue(answer instanceof java.util.LinkedHashSet);
        Assert.assertTrue(((java.util.LinkedHashSet<?>) answer).isEmpty());
    }
    
    @Test
    public void testAnswerListReturnType() {
        // Mock object
        MyMockObject mock = new MyMockObject();
        
        // Set up invocation with a method returning a List type
        InvocationOnMock invocation = new MyInvocation(mock, "getListValue");
        
        // Perform the answer
        Object answer = returnsEmptyValues.answer(invocation);
        
        // Verify the result
        Assert.assertTrue(answer instanceof java.util.List);
        Assert.assertTrue(((java.util.List<?>) answer).isEmpty());
    }
    
    @Test
    public void testAnswerLinkedListReturnType() {
        // Mock object
        MyMockObject mock = new MyMockObject();
        
        // Set up invocation with a method returning a LinkedList type
        InvocationOnMock invocation = new MyInvocation(mock, "getLinkedListValue");
        
        // Perform the answer
        Object answer = returnsEmptyValues.answer(invocation);
        
        // Verify the result
        Assert.assertTrue(answer instanceof java.util.LinkedList);
        Assert.assertTrue(((java.util.LinkedList<?>) answer).isEmpty());
    }
    
    @Test
    public void testAnswerArrayListReturnType() {
        // Mock object
        MyMockObject mock = new MyMockObject();
        
        // Set up invocation with a method returning an ArrayList type
        InvocationOnMock invocation = new MyInvocation(mock, "getArrayListValue");
        
        // Perform the answer
        Object answer = returnsEmptyValues.answer(invocation);
        
        // Verify the result
        Assert.assertTrue(answer instanceof java.util.ArrayList);
        Assert.assertTrue(((java.util.ArrayList<?>) answer).isEmpty());
    }
    
    @Test
    public void testAnswerMapReturnType() {
        // Mock object
        MyMockObject mock = new MyMockObject();
        
        // Set up invocation with a method returning a Map type
        InvocationOnMock invocation = new MyInvocation(mock, "getMapValue");
        
        // Perform the answer
        Object answer = returnsEmptyValues.answer(invocation);
        
        // Verify the result
        Assert.assertTrue(answer instanceof java.util.Map);
        Assert.assertTrue(((java.util.Map<?, ?>) answer).isEmpty());
    }
    
    @Test
    public void testAnswerHashMapReturnType() {
        // Mock object
        MyMockObject mock = new MyMockObject();
        
        // Set up invocation with a method returning a HashMap type
        InvocationOnMock invocation = new MyInvocation(mock, "getHashMapValue");
        
        // Perform the answer
        Object answer = returnsEmptyValues.answer(invocation);
        
        // Verify the result
        Assert.assertTrue(answer instanceof java.util.HashMap);
        Assert.assertTrue(((java.util.HashMap<?, ?>) answer).isEmpty());
    }
    
    @Test
    public void testAnswerSortedMapReturnType() {
        // Mock object
        MyMockObject mock = new MyMockObject();
        
        // Set up invocation with a method returning a SortedMap type
        InvocationOnMock invocation = new MyInvocation(mock, "getSortedMapValue");
        
        // Perform the answer
        Object answer = returnsEmptyValues.answer(invocation);
        
        // Verify the result
        Assert.assertTrue(answer instanceof java.util.SortedMap);
        Assert.assertTrue(((java.util.SortedMap<?, ?>) answer).isEmpty());
    }
    
    @Test
    public void testAnswerTreeMapReturnType() {
        // Mock object
        MyMockObject mock = new MyMockObject();
        
        // Set up invocation with a method returning a TreeMap type
        InvocationOnMock invocation = new MyInvocation(mock, "getTreeMapValue");
        
        // Perform the answer
        Object answer = returnsEmptyValues.answer(invocation);
        
        // Verify the result
        Assert.assertTrue(answer instanceof java.util.TreeMap);
        Assert.assertTrue(((java.util.TreeMap<?, ?>) answer).isEmpty());
    }
    
    @Test
    public void testAnswerLinkedHashMapReturnType() {
        // Mock object
        MyMockObject mock = new MyMockObject();
        
        // Set up invocation with a method returning a LinkedHashMap type
        InvocationOnMock invocation = new MyInvocation(mock, "getLinkedHashMapValue");
        
        // Perform the answer
        Object answer = returnsEmptyValues.answer(invocation);
        
        // Verify the result
        Assert.assertTrue(answer instanceof java.util.LinkedHashMap);
        Assert.assertTrue(((java.util.LinkedHashMap<?, ?>) answer).isEmpty());
    }
    
    private static class MyMockObject {
        public int getIntValue() {
            return 0;
        }
        
        public Integer getIntegerValue() {
            return 0;
        }
        
        public Collection<?> getCollectionValue() {
            return null;
        }
        
        public Set<?> getSetValue() {
            return null;
        }
        
        public SortedSet<?> getSortedSetValue() {
            return null;
        }
        
        public LinkedHashSet<?> getLinkedHashSetValue() {
            return null;
        }
        
        public List<?> getListValue() {
            return null;
        }
        
        public LinkedList<?> getLinkedListValue() {
            return null;
        }
        
        public ArrayList<?> getArrayListValue() {
            return null;
        }
        
        public Map<?, ?> getMapValue() {
            return null;
        }
        
        public HashMap<?, ?> getHashMapValue() {
            return null;
        }
        
        public SortedMap<?, ?> getSortedMapValue() {
            return null;
        }
        
        public TreeMap<?, ?> getTreeMapValue() {
            return null;
        }
        
        public LinkedHashMap<?, ?> getLinkedHashMapValue() {
            return null;
        }
    }
    
    private static class MyInvocation implements InvocationOnMock {
        private final Object mock;
        private final String methodName;
        private final Object[] arguments;
        
        public MyInvocation(Object mock, String methodName) {
            this(mock, methodName, new Object[0]);
        }
        
        public MyInvocation(Object mock, String methodName, Object[] arguments) {
            this.mock = mock;
            this.methodName = methodName;
            this.arguments = arguments;
        }
        
        @Override
        public Object getMock() {
            return mock;
        }
        
        @Override
        public Object[] getArguments() {
            return arguments;
        }
        
        @Override
        public boolean isVerified() {
            return false;
        }
        
        @Override
        public void markVerified() {
        
        }
        
        @Override
        public String toString() {
            return methodName;
        }
    }
}