import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.stubbing.Answer;
import org.junit.Test;

public class ReturnsEmptyValuesTest {
    
    private ReturnsEmptyValues returnsEmptyValues;
    
    public ReturnsEmptyValuesTest() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }
    
    @Test
    public void testAnswerForToStringMethod() {
        InvocationOnMock invocation = createInvocation("toString", new Object[0]);
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertEquals("Mock for Class, hashCode: 0", result);
    }
    
    @Test
    public void testAnswerForCompareToMethodWithDifferentReferences() {
        Object mock = new Object();
        Object[] arguments = { new Object() };
        InvocationOnMock invocation = createInvocation("compareTo", arguments, mock);
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertEquals(1, result);
    }
    
    @Test
    public void testAnswerForCompareToMethodWithSameReferences() {
        Object mock = new Object();
        Object[] arguments = { mock };
        InvocationOnMock invocation = createInvocation("compareTo", arguments, mock);
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testAnswerForReturnTypePrimitiveInt() {
        InvocationOnMock invocation = createInvocation("method", new Object[0]);
        invocation.getMethod().getReturnType();
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testAnswerForReturnTypePrimitiveWrapperInteger() {
        InvocationOnMock invocation = createInvocation("method", new Object[0]);
        invocation.getMethod().getReturnType();
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testAnswerForReturnTypeCollection() {
        InvocationOnMock invocation = createInvocation("method", new Object[0]);
        invocation.getMethod().getReturnType();
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertEquals(new LinkedList<Object>(), result);
    }
    
    @Test
    public void testAnswerForReturnTypeSet() {
        InvocationOnMock invocation = createInvocation("method", new Object[0]);
        invocation.getMethod().getReturnType();
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertEquals(new HashSet<Object>(), result);
    }
    
    @Test
    public void testAnswerForReturnTypeHashSet() {
        InvocationOnMock invocation = createInvocation("method", new Object[0]);
        invocation.getMethod().getReturnType();
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertEquals(new HashSet<Object>(), result);
    }
    
    @Test
    public void testAnswerForReturnTypeSortedSet() {
        InvocationOnMock invocation = createInvocation("method", new Object[0]);
        invocation.getMethod().getReturnType();
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertEquals(new TreeSet<Object>(), result);
    }
    
    @Test
    public void testAnswerForReturnTypeTreeSet() {
        InvocationOnMock invocation = createInvocation("method", new Object[0]);
        invocation.getMethod().getReturnType();
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertEquals(new TreeSet<Object>(), result);
    }
    
    @Test
    public void testAnswerForReturnTypeLinkedHashSet() {
        InvocationOnMock invocation = createInvocation("method", new Object[0]);
        invocation.getMethod().getReturnType();
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertEquals(new LinkedHashSet<Object>(), result);
    }
    
    @Test
    public void testAnswerForReturnTypeList() {
        InvocationOnMock invocation = createInvocation("method", new Object[0]);
        invocation.getMethod().getReturnType();
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertEquals(new LinkedList<Object>(), result);
    }
    
    @Test
    public void testAnswerForReturnTypeLinkedList() {
        InvocationOnMock invocation = createInvocation("method", new Object[0]);
        invocation.getMethod().getReturnType();
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertEquals(new LinkedList<Object>(), result);
    }
    
    @Test
    public void testAnswerForReturnTypeArrayList() {
        InvocationOnMock invocation = createInvocation("method", new Object[0]);
        invocation.getMethod().getReturnType();
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertEquals(new ArrayList<Object>(), result);
    }
    
    @Test
    public void testAnswerForReturnTypeMap() {
        InvocationOnMock invocation = createInvocation("method", new Object[0]);
        invocation.getMethod().getReturnType();
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertEquals(new HashMap<Object, Object>(), result);
    }
    
    @Test
    public void testAnswerForReturnTypeHashMap() {
        InvocationOnMock invocation = createInvocation("method", new Object[0]);
        invocation.getMethod().getReturnType();
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertEquals(new HashMap<Object, Object>(), result);
    }
    
    @Test
    public void testAnswerForReturnTypeSortedMap() {
        InvocationOnMock invocation = createInvocation("method", new Object[0]);
        invocation.getMethod().getReturnType();
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertEquals(new TreeMap<Object, Object>(), result);
    }
    
    @Test
    public void testAnswerForReturnTypeTreeMap() {
        InvocationOnMock invocation = createInvocation("method", new Object[0]);
        invocation.getMethod().getReturnType();
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertEquals(new TreeMap<Object, Object>(), result);
    }
    
    @Test
    public void testAnswerForReturnTypeLinkedHashMap() {
        InvocationOnMock invocation = createInvocation("method", new Object[0]);
        invocation.getMethod().getReturnType();
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertEquals(new LinkedHashMap<Object, Object>(), result);
    }
    
    private InvocationOnMock createInvocation(String methodName, Object[] arguments) {
        return createInvocation(methodName, arguments, null);
    }
    
    private InvocationOnMock createInvocation(String methodName, Object[] arguments, Object mock) {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(getMethodByName(methodName));
        Mockito.when(invocation.getArguments()).thenReturn(arguments);
        Mockito.when(invocation.getMock()).thenReturn(mock);
        return invocation;
    }
    
    private Method getMethodByName(String name) {
        try {
            return ReturnsEmptyValues.class.getDeclaredMethod(name, new Class<?>[0]);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}