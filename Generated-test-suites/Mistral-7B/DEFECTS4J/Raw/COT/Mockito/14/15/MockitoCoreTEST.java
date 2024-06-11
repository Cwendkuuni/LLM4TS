import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentsMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.api.InOrder;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;
    private Object mock;
    private Object mockWithMultipleMethods;

    @Before
    public void setup() {
        mockitoCore = new MockitoCore();
        MockitoAnnotations.initMocks(this);
        mock = Mockito.mock(String.class);
        mockWithMultipleMethods = new MockWithMultipleMethods();
    }

    @Test
    public void testMockCreation() {
        Object mockedObject = mockitoCore.mock(String.class, new MockSettingsImpl());
        Assert.assertNotNull(mockedObject);
    }

    @Test(expected = NullPointerException.class)
    public void testMockCreationWithNullClass() {
        mockitoCore.mock(null, new MockSettingsImpl());
    }

    @Test
    public void testStubbing() {
        IOngoingStubbing stubbing = mockitoCore.stub();
        stubbing.thenReturn("Return value");
        when(mock.toString()).thenReturn("Mocked String");
        assertEquals("Mocked String", mockitoCore.verify(mock).toString());
        assertEquals("Return value", mockitoCore.verify(mock).someMethod());
    }

    @Test(expected = IllegalStateException.class)
    public void testMissingStubbing() {
        mockitoCore.verify(mock).someMethod();
    }

    @Test
    public void testVerification() {
        mockitoCore.verify(mock).toString();
    }

    @Test(expected = NullPointerException.class)
    public void testNullPassedToVerify() {
        mockitoCore.verify(null, VerificationMode.times(1));
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        mockitoCore.verifyNoMoreInteractions(mock);
    }

    @Test(expected = NullPointerException.class)
    public void testNullPassedToVerifyNoMoreInteractions() {
        mockitoCore.verifyNoMoreInteractions(null);
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        InOrder inOrder = mockitoCore.inOrder(mock, mockWithMultipleMethods);
        mockitoCore.verify(mock).someMethod();
        mockitoCore.verify(mockWithMultipleMethods).otherMethod();
        inOrder.verify();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyNoMoreInteractionsInOrderWithNullList() {
        mockitoCore.verifyNoMoreInteractionsInOrder(null, Mockito.inOrder(mock));
    }

    @Test
    public void testReset() {
        when(mock.toString()).thenReturn("Mocked String");
        assertEquals("Mocked String", mockitoCore.verify(mock).toString());
        mockitoCore.reset(mock);
        assertNull(mockitoCore.verify(mock).toString());
    }

    @Test
    public void testStubbingWithAnswer() {
        Answer<Integer> answer = a -> a.hashCode();
        Stubber stubber = mockitoCore.doAnswer(answer);
        when(mock.toString()).thenAnswer(answer);
        assertEquals(mock.toString().hashCode(), mockitoCore.verify(mock).toString().hashCode());
    }

    @Test
    public void testVerifyWithNullVerificationMode() {
        mockitoCore.verify(mock, null);
    }

    @Test
    public void testStubbingVoidMethod() {
        VoidMethodStubbable<Object> voidMethodStubbable = mockitoCore.stubVoid(mock);
        voidMethodStubbable.thenReturnVoid();
        mockitoCore.verify(mock).voidMethod();
    }

    @Test(expected = IllegalStateException.class)
    public void testStubbingVoidMethodTwice() {
        VoidMethodStubbable<Object> voidMethodStubbable = mockitoCore.stubVoid(mock);
        mockitoCore.stubVoid(mock);
        mockitoCore.verify(mock).voidMethod();
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
    }

    @Test(expected = IllegalStateException.class)
    public void testVerifyWithNotAMockObject() {
        mockitoCore.verify(new Object());
    }

    @Test(expected = IllegalStateException.class)
    public void testVerifyWithMultipleMocks() {
        Object mockedObject1 = mockitoCore.mock(String.class, new MockSettingsImpl());
        Object mockedObject2 = mockitoCore.mock(String.class, new MockSettingsImpl());
        mockitoCore.verify(mockedObject1, VerificationMode.times(1)).toString();
        mockitoCore.verify(mockedObject2, VerificationMode.times(1)).toString();
    }

    @Test
    public void testToString() {
        assertEquals("org.mockito.internal.MockitoCore@" +
                "62f14643", mockitoCore.toString());
    }

    @Test
    public void testGetLastInvocation() {
        when(mock.toString()).thenReturn("Mocked String");
        Invocation lastInvocation = mockitoCore.getLastInvocation();
        assertEquals("toString", lastInvocation.getMethod().getName());
        assertEquals("Mocked String", lastInvocation.getArgument(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testGetLastInvocationWithNoStubbing() {
        mockitoCore.getLastInvocation();
    }
}

class MockWithMultipleMethods {
    public void someMethod() {
    }

    public void otherMethod() {
    }
}