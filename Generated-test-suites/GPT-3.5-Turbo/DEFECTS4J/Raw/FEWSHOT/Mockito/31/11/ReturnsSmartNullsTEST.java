package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {
   
    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        
        // Test case when delegate answer returns null and method return type is not mockable
        InvocationOnMock invocation1 = null; //create invocation object
        assertNull(returnsSmartNulls.answer(invocation1));
        
        // Test case when delegate answer returns non-null value
        InvocationOnMock invocation2 = null; //create invocation object
        Object defaultReturnValue2 = new Object();
        assertNotNull(returnsSmartNulls.answer(invocation2, defaultReturnValue2));
        
        // Test case when delegate answer returns null and method return type is mockable
        InvocationOnMock invocation3 = null; //create invocation object
        Object defaultReturnValue3 = null;
        Class<?> type3 = null; //set the mockable return type
        assertNotNull(returnsSmartNulls.answer(invocation3, defaultReturnValue3, type3));
    }

    @Test
    public void testThrowingInterceptor() throws Throwable {
        ReturnsSmartNulls rs = new ReturnsSmartNulls();
        InvocationOnMock invocation = null; //create invocation object
        ThrowingInterceptor ti = rs.new ThrowingInterceptor(invocation);

        // Test case for toString method
        Method method1 = null; //create method object with toString method
        String expectedResult1 = "SmartNull returned by unstubbed methodCall method on mock";
        assertEquals(expectedResult1, ti.intercept(null, method1, null, null));
  
        // Test case for any other method
        Method method2 = null; //create method object with any other method
        Location location = null; //create location object
        String expectedResult2 = null; //expected null value 
        assertEquals(expectedResult2, ti.intercept(null, method2, null, null)); 
        //assert smartNullPointerException is thrown and caught
    }

}