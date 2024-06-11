import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockUtil;
import org.mockito.internal.verification.api.InOrder;
import org.mockito.internal.verification.api.VerificationDataImpl;
import org.mockito.internal.verification.api.VerificationModeFactory;
import org.mockito.internal.verification.api.VerificationModeFactory.VerificationModes;
import org.mockito.internal.verification.api.VerificationOrder;
import org.mockito.internal.verification.api.VerificationOrderType;
import org.mockito.internal.verification.api.VerificationResult;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.stubbing.VoidMethodStubbable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockSettings mockSettings = new MockSettingsImpl();
        Object mock = new MockitoCore().mock(mockitoCoreClass, mockSettings);
        Assert.assertNotNull(mock);
        Mockito.verifyNoMoreInteractions(mock);
    }

    @Test
    public void testStub() throws Exception {
        MockitoCore mockitoCore = new MockitoCore();
        IOngoingStubbing stubbing = mockitoCore.stub();
        Assert.assertNotNull(stubbing);

        Callable<String> callable = () -> "Hello, World!";
        final String expectedResult = "Hello, World!";

        OngoingStubbing<String> stringOngoingStubbing = mockitoCore.when(ArgumentMatchers.any());
        final Stubber<String> stringStubber = stringOngoingStubbing.thenReturn(expectedResult);

        String result = stubbing.call(callable);
        Assert.assertEquals(expectedResult, result);
        Mockito.verify(callable).call();
        Mockito.verify(callable).call(); // Verify that the stubbing was only called once

        stringStubber.thenCallRealMethod();
        result = stubbing.call(callable);
        Assert.assertEquals(expectedResult, result);
        Mockito.verify(callable, VerificationOrder.inOrder()).call();
    }

    @Test(expected = NotAMockException.class)
    public void testStubWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        IOngoingStubbing stubbing = mockitoCore.stub();
        mockitoCore.stub(null);
    }

    @Test
    public void testStubDeprecated() {
        MockitoCore mockitoCore = new MockitoCore();
        IOngoingStubbing stubbing = mockitoCore.stub();

        DeprecatedOngoingStubbing<Object> deprecatedOngoingStubbing = mockitoCore.stub(Mockito.mock(Object.class));
        deprecatedOngoingStubbing.thenReturn("Deprecated Return Value");

        Object mock = deprecatedOngoingStubbing.getMock();
        String result = (String) stubbing.call(mock);
        Assert.assertEquals("Deprecated Return Value", result);
        Mockito.verify(mock).toString();
    }

    @Test
    public void testWhen() {
        MockitoCore mockitoCore = new MockitoCore();
        IOngoingStubbing stubbing = mockitoCore.stub();

        Object mock = Mockito.mock(Object.class);
        OngoingStubbing<Object> whenOngoingStubbing = mockitoCore.when(mock);

        whenOngoingStubbing.thenReturn("When Return Value");

        Object result = stubbing.call(mock);
        Assert.assertEquals("When Return Value", result);
        Mockito.verify(mock).toString();
    }

    @Test(expected = NotAMockException.class)
    public void testWhenWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        IOngoingStubbing stubbing = mockitoCore.stub();
        mockitoCore.when(null);
    }

    @Test
    public void testVerify() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = Mockito.mock(Object.class);

        mockitoCore.verify(mock, VerificationModes.times(1));
        Mockito.verify(mock, VerificationModes.never());
        Mockito.verify(mock, VerificationModes.atLeast(1));
        Mockito.verify(mock, VerificationModes.atLeastOnce());
        Mockito.verify(mock, VerificationModes.atMost(1));
        Mockito.verify(mock, VerificationModes.atMostOnce());
        Mockito.verify(mock, VerificationModes.exactly(3));
        Mockito.verify(mock, VerificationModes.inOrder(VerificationOrder.inOrder(VerificationOrderType.chronological())));
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.verify(null);
    }

    @Test
    public void testReset() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = Mockito.mock(Object.class);
        mockitoCore.mockingProgress.mockingStarted(mock, Object.class, null);
        mockitoCore.mockingProgress.reset();
        Mockito.verifyNoMoreInteractions(mock);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = Mockito.mock(Object.class);
        mockitoCore.mockingProgress.mockingStarted(mock, Object.class, null);
        mockitoCore.verify(mock, VerificationModes.never());
        mockitoCore.verifyNoMoreInteractions(mock);
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock1 = Mockito.mock(Object1.class);
        Object mock2 = Mockito.mock(Object2.class);

        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
        inOrder.verify(mock1).method1();
        inOrder.verify(mock2).method2();
        mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractionsInOrderWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        InOrder inOrder = mockitoCore.inOrder();
        mockitoCore.verifyNoMoreInteractionsInOrder(null, inOrder);
    }

    @Test
    public void testInOrder() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock1 = Mockito.mock(Object1.class);
        Object mock2 = Mockito.mock(Object2.class);

        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
        inOrder.verify(mock1).method1();
        inOrder.verify(mock1).method2();
        inOrder.verify(mock2).method3();

        Mockito.verifyNoMoreInteractions(mock1);
        Mockito.verifyNoMoreInteractions(mock2);
    }

    @Test(expected = NotAMockException.class)
    public void testInOrderWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        InOrder inOrder = mockitoCore.inOrder();
        inOrder.verify(null).method();
    }

    @Test
    public void testDoAnswer() {
        MockitoCore mockitoCore = new MockitoCore();

        Answer<String> answer = new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocationOnMock) throws Throwable {
                return "Do Answer Return Value";
            }
        };

        Stubber<Object> stubber = mockitoCore.doAnswer(answer);
        Object mock = Mockito.mock(Object.class);
        Object result = stubber.call(mock);

        Assert.assertEquals("Do Answer Return Value", result);
        Mockito.verify(mock).toString();
    }

    @Test
    public void testStubVoid() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = Mockito.mock(Object.class);
        MockHandlerInterface<Object> mockHandler = MockUtil.getMockHandler(mock);

        VoidMethodStubbable<Object> voidMethodStubbable = mockitoCore.stubVoid(mock);
        voidMethodStubbable. Answer("Void Method Stubbing Answer");

        voidMethodStubbable.call();
        Mockito.verify(mock).toString();

       mockHandler.answerVoidCallsWith(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                return null;
            }
        });

        voidMethodStubbable.call();
        Mockito.verifyNoMoreInteractions(mock);
    }

    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation() {
        MockitoCore mockitoCore = new MockitoCore();

        Object mock = Mockito.mock(Object.class);
        OngoingStubbing<Object> ongoingStubbing = mockitoCore.when(mock);
        ongoingStubbing.thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                return invocationOnMock.getArgument(0);
            }
        });
        Object argument = "Argument";
        Object result = mockitoCore.getLastInvocation().getArguments()[0];

        Assert.assertEquals(argument, result);
    }
}