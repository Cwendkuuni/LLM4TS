package com.yourpackage; // replace with your package name

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsSmartNullsTest {

    @Rule
    public TestRule mockitoRule = MockitoInitRules.initMocks(this);

    private ReturnsSmartNulls returnsSmartNulls;

    @Before
    public void setup() {
        returnsSmartNulls = new ReturnsSmartNulls();
    }

    @Test
    public void testAnswerNullMethodReturnsSmartNull() throws Exception {
        Object mock = mock(TestClass.class, returnsSmartNulls);
        Method method = TestClass.class.getMethod("methodThatReturnsNull");

        Object result = method.invoke(mock, new Object[0]);

        assertEquals("SmartNull returned by unstubbed methodThatReturnsNull method on mock", result);
    }

    @Test
    public void testAnswerNotNullMethodReturnsSameInstance() throws Exception {
        Object mock = mock(TestClass.class, returnsSmartNulls);
        Method method = TestClass.class.getMethod("methodThatReturnsNotNull");
        Object input = new Object();

        Object result = method.invoke(mock, input);

        assertEquals(input, result);
        verify(mock).methodThatReturnsNotNull(input);
    }

    @Test(expected = NullPointerException.class)
    public void testAnswerNullMethodThrowsNPEWhenInvoked() throws Exception {
        Object mock = mock(TestClass.class, returnsSmartNulls);
        Method method = TestClass.class.getMethod("methodThatThrowsNPE");

        method.invoke(mock, new Object[0]);
    }

    @Test
    public void testAnswerFinalClassMethodReturnsNull() throws Exception {
        Object mock = mock(FinalClass.class, returnsSmartNulls);
        Method method = FinalClass.class.getMethod("methodThatReturnsFinalClass");

        Object result = method.invoke(mock, new Object[0]);

        assertNull(result);
    }

    @Test
    public void testAnswerNonMockableClassMethodReturnsNull() throws Exception {
        Object mock = mock(TestClass.class, returnsSmartNulls);
        ClassNonMockable classNonMockable = new ClassNonMockable();
        Method method = classNonMockable.getClass().getMethod("methodThatReturnsClassNonMockable");

        Object result = method.invoke(mock, new Object[0]);

        assertNull(result);
    }

    @Test
    public void testAnswerMethodWithArrayReturnsSmartNullArray() throws Exception {
        Object mock = mock(TestClassWithArray.class, returnsSmartNulls);
        Method method = TestClassWithArray.class.getMethod("methodThatReturnsArray");

        Object[] result = (Object[]) method.invoke(mock, new Object[0]);

        assertEquals("SmartNull[]", result.getClass().getName());
        for (Object smartNull : result) {
            assertEquals("SmartNull", smartNull.getClass().getName());
        }
    }

    private static class TestClass {
        String methodThatReturnsNull() {
            return null;
        }

        String methodThatReturnsNotNull(Object obj) {
            return obj.toString();
        }

        void methodThatThrowsNPE() {
            throw new NullPointerException();
        }
    }

    private static class TestClassWithArray {
        SmartNull[] methodThatReturnsArray() {
            return new SmartNull[0];
        }

        static class SmartNull {}
    }

    private static class FinalClass {
        final Object methodThatReturnsFinalClass() {
            return new Object();
        }
    }

    private static class ClassNonMockable {}
}