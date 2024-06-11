import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToString() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        String result = (String) returnsEmptyValues.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result.contains("Mock for"));
    }
    
    @Test
    public void testAnswerCompareTo() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getArguments()).thenReturn(new Object[] {mock(Object.class)});
        
        int result = (int) returnsEmptyValues.answer(invocation);
        
        assertEquals(1, result);
    }
    
    @Test
    public void testReturnValueForPrimitiveTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        
        Object resultBoolean = returnsEmptyValues.returnValueFor(boolean.class);
        Object resultInt = returnsEmptyValues.returnValueFor(int.class);
        Object resultLong = returnsEmptyValues.returnValueFor(long.class);
        
        assertEquals(false, resultBoolean);
        assertEquals(0, resultInt);
        assertEquals(0L, resultLong);
    }
    
    @Test
    public void testReturnValueForCollections() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        
        Object resultCollection = returnsEmptyValues.returnValueFor(Collection.class);
        Object resultSet = returnsEmptyValues.returnValueFor(Set.class);
        Object resultList = returnsEmptyValues.returnValueFor(List.class);
        Object resultMap = returnsEmptyValues.returnValueFor(Map.class);
        
        assertTrue(resultCollection instanceof LinkedList);
        assertTrue(resultSet instanceof HashSet);
        assertTrue(resultList instanceof LinkedList);
        assertTrue(resultMap instanceof HashMap);
    }
}