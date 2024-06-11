import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.stubbing.Answer;
import java.util.*;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = new InvocationOnMock() {
            @Override
            public Object getMock() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public MockName getMockName() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void markVerified() {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void markStubbed(Answer<?> answer) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public Object getArgument(int index) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Object[] getArguments() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Object callRealMethod() throws Throwable {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public boolean isVerified() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean isIgnoredForVerification() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean isVoid() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public Object getMockSettings() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public boolean isAnswered() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public ObjectAnswer<Void> getVoidAnswer() {
                // TODO Auto-generated method stub
                return null;
            }
        };
        
        Object result = returnsEmptyValues.answer(invocation);
        assertNotNull(result);
        assertTrue(result instanceof String);
        
        assertEquals("Mock for Object, hashCode: 0", result);
    }
    
    @Test
    public void testReturnValueFor() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        
        Object result = returnsEmptyValues.returnValueFor(int.class);
        assertNotNull(result);
        assertTrue(result instanceof Integer);
        assertEquals(0, result);
        
        result = returnsEmptyValues.returnValueFor(Integer.class);
        assertNotNull(result);
        assertTrue(result instanceof Integer);
        assertEquals(0, result);
        
        result = returnsEmptyValues.returnValueFor(Collection.class);
        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
        assertEquals(0, ((LinkedList<?>) result).size());
        
        result = returnsEmptyValues.returnValueFor(Set.class);
        assertNotNull(result);
        assertTrue(result instanceof HashSet);
        assertEquals(0, ((HashSet<?>) result).size());
        
        result = returnsEmptyValues.returnValueFor(HashSet.class);
        assertNotNull(result);
        assertTrue(result instanceof HashSet);
        assertEquals(0, ((HashSet<?>) result).size());
        
        result = returnsEmptyValues.returnValueFor(SortedSet.class);
        assertNotNull(result);
        assertTrue(result instanceof TreeSet);
        assertEquals(0, ((TreeSet<?>) result).size());
        
        result = returnsEmptyValues.returnValueFor(TreeSet.class);
        assertNotNull(result);
        assertTrue(result instanceof TreeSet);
        assertEquals(0, ((TreeSet<?>) result).size());
        
        result = returnsEmptyValues.returnValueFor(LinkedHashSet.class);
        assertNotNull(result);
        assertTrue(result instanceof LinkedHashSet);
        assertEquals(0, ((LinkedHashSet<?>) result).size());
        
        result = returnsEmptyValues.returnValueFor(List.class);
        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
        assertEquals(0, ((LinkedList<?>) result).size());
        
        result = returnsEmptyValues.returnValueFor(LinkedList.class);
        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
        assertEquals(0, ((LinkedList<?>) result).size());
        
        result = returnsEmptyValues.returnValueFor(ArrayList.class);
        assertNotNull(result);
        assertTrue(result instanceof ArrayList);
        assertEquals(0, ((ArrayList<?>) result).size());
        
        result = returnsEmptyValues.returnValueFor(Map.class);
        assertNotNull(result);
        assertTrue(result instanceof HashMap);
        assertEquals(0, ((HashMap<?, ?>) result).size());
        
        result = returnsEmptyValues.returnValueFor(HashMap.class);
        assertNotNull(result);
        assertTrue(result instanceof HashMap);
        assertEquals(0, ((HashMap<?, ?>) result).size());
        
        result = returnsEmptyValues.returnValueFor(SortedMap.class);
        assertNotNull(result);
        assertTrue(result instanceof TreeMap);
        assertEquals(0, ((TreeMap<?, ?>) result).size());
        
        result = returnsEmptyValues.returnValueFor(TreeMap.class);
        assertNotNull(result);
        assertTrue(result instanceof TreeMap);
        assertEquals(0, ((TreeMap<?, ?>) result).size());
        
        result = returnsEmptyValues.returnValueFor(LinkedHashMap.class);
        assertNotNull(result);
        assertTrue(result instanceof LinkedHashMap);
        assertEquals(0, ((LinkedHashMap<?, ?>) result).size());
        
        result = returnsEmptyValues.returnValueFor(Object.class);
        assertNull(result);
    }

}