package org.mockito.internal.stubbing.answers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class CallsRealMethodsTest {

    @Test
    public void testCallsRealMethods() {
        Object mockObject = mock(TestClass.class);
        CallsRealMethods answer = new CallsRealMethods();

        TestClass testClass = new TestClass();

        // Call real method
        Object result1 = mockObject.realMethod();
        assertEquals(result1, answer.answer(mockitoMock(invocation("realMethod", new Object[0])))[0]);

        // Call real method with arguments
        Object result2 = testClass.realMethodWithArgs(1, "arg1", new Object[0]);
        Object[] invocationArgs = invocationArgs("realMethodWithArgs", new Object[]{1, "arg1", new Object[0]});
        Object[] result = answer.answer(mockitoMock(invocation("realMethodWithArgs", invocationArgs)));
        assertEquals(result2, result[0]);
        verify(testClass).realMethodWithArgs(1, "arg1", new Object[0]);
    }

    private InvocationOnMock mockitoMock(InvocationOnMock invocation) {
        return mock(InvocationOnMock.class);
    }

    private Object[] invocationArgs(String methodName, Object[] args) {
        return new Object[]{invocation(methodName, args)};
    }

    private InvocationOnMock invocation(String methodName, Object[] args) {
        return mockitoMock(new InvocationOnMock() {
            @Override
            public Object getMock() {
                return null;
            }

            @Override
            public Method getMethod() {
                return TestClass.class.getMethod(methodName, args.getClass());
            }

            @Override
            public Object[] getArgs() {
                return args;
            }

            @Override
            public Object getTarget() {
                return null;
            }
        });
    }

    static class TestClass {
        public Object realMethod() {
            return "real method result";
        }

        public Object realMethodWithArgs(int arg1, String arg2, Object[] args) {
            return "real method with args result: " + arg1 + ", " + arg2;
        }
    }
}