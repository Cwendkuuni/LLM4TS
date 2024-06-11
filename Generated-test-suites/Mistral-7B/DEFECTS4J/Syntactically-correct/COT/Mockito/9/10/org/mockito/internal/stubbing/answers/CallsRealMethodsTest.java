package org.mockito.internal.stubbing.answers.test;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CallsRealMethodsTest {

    @Test
    public void testCallsRealMethod() {
        Object realObject = mock(RealClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        RealInterface realInterface = mock(RealInterface.class);
        realInterface.setRealObject(realObject);

        ArgumentCaptor<RealMethodArgument> argumentCaptor = ArgumentCaptor.forClass(RealMethodArgument.class);
        realInterface.callRealMethod(argumentCaptor.capture());

        Object actualResult = answer.answer(Mockito.argThat(i -> i.getTarget().equals(realInterface) &&
                i.getMethod().getName().equals("callRealMethod")));

        RealMethodArgument expectedArgument = argumentCaptor.getValue();
        Object expectedResult = expectedArgument.getArgument();

        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalAccessException.class)
    public void testCallsRealMethodWithPrivateMethod() {
        Object realObject = mock(PrivateRealClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        PrivateRealInterface realInterface = mock(PrivateRealInterface.class);
        realInterface.setRealObject(realObject);

        realInterface.callPrivateRealMethod();

        // This test case should throw an IllegalAccessException as the private method is not mocked
    }

    @Test
    public void testCallsRealMethodWithNullArgument() {
        Object realObject = mock(RealClassWithNullArgument.class);
        CallsRealMethods answer = new CallsRealMethods();

        RealInterfaceWithNullArgument realInterface = mock(RealInterfaceWithNullArgument.class);
        realInterface.setRealObject(realObject);

        realInterface.callRealMethodWithNullArgument(null);

        // No exception should be thrown as the answer delegates to the real method
    }

    @Test(expected = NullPointerException.class)
    public void testCallsRealMethodWithNullRealObject() {
        CallsRealMethods answer = new CallsRealMethods();

        RealInterface realInterface = mock(RealInterface.class);
        realInterface.setRealObject(null);

        realInterface.callRealMethod(new Object());

        // This test case should throw a NullPointerException as the real object is null
    }
}