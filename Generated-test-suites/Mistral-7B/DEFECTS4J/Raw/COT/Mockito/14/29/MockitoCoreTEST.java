import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockUtil;
import org.mockito.internal.verification.VerificationMode;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyOrder;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;

    @Captor
    private ArgumentCaptor<VerificationMode> verificationModeCaptor;

    @Before
    public void setup() {
        mockitoCore = new MockitoCore();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMockCreation() {
        Class<String> stringClass = String.class;
        MockSettings mockSettings = new MockSettingsImpl();
        String mock = mockitoCore.mock(stringClass, mockSettings);
        // Perform assertions on the mock object here
    }

    @Test
    public void testStubbing() {
        Object mock = mock(Object.class);
        Stubber stubber = mockitoCore.stub();

        stubber.thenReturn("Hello");
        String result = (String) mockitoCore.when(mock).toString();
        assertEquals("Hello", result);
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNullMock() {
        mockitoCore.verify(null, VerificationModeFactory.instance().eq(1));
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNonMockObject() {
        Object nonMockObject = new Object();
        mockitoCore.verify(nonMockObject, VerificationModeFactory.instance().eq(1));
    }

    @Test
    public void testVerificationMode() {
        Object mock = mock(Object.class);
        mockitoCore.verify(mock, verificationModeCaptor.capture());
        assertEquals(VerificationMode.times(1), verificationModeCaptor.getValue());
    }

    @Test
    public void testVerificationModeTimes() {
        Object mock = mock(Object.class);
        mockitoCore.verify(mock, VerificationModeFactory.instance().times(3));
    }

    @Test
    public void testVerificationModeAtLeast() {
        Object mock = mock(Object.class);
        mockitoCore.verify(mock, VerificationModeFactory.instance().atLeast(3));
    }

    @Test
    public void testVerificationModeAtMost() {
        Object mock = mock(Object.class);
        mockitoCore.verify(mock, VerificationModeFactory.instance().atMost(3));
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractionsNullMocksArray() {
        mockitoCore.verifyNoMoreInteractions();
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractionsEmptyMocksArray() {
        mockitoCore.verifyNoMoreInteractions(new Object[0]);
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractionsNullMock() {
        Object[] mocks = new Object[1];
        mocks[0] = null;
        mockitoCore.verifyNoMoreInteractions(mocks);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        mockitoCore.verifyNoMoreInteractions(mock1, mock2, mock3);
    }

    @Test
    public void testVerifyOrder() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);

        when(mock1.doSomething()).thenReturn(1);
        when(mock2.doSomething()).thenReturn(2);

        inOrder.verify(mock1).doSomething();
        inOrder.verify(mock2).doSomething();

        verifyOrder(inOrder);
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyOrderNullMocks() {
        InOrder inOrder = mockitoCore.inOrder();
        inOrder.verify(null);
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyOrderEmptyMocks() {
        InOrder inOrder = mockitoCore.inOrder();
        inOrder.verify(new Object[0]);
    }

    @Test
    public void testVerifyOrderInOrder() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);

        when(mock1.doSomething()).thenReturn(1);
        when(mock2.doSomething()).thenReturn(2);

        inOrder.verify(mock1).doSomething();
        inOrder.verify(mock2).doSomething();

        verifyOrder(inOrder);
    }

    @Test
    public void testReset() {
        Object mock = mock(Object.class);
        mockitoCore.reset(mock);
        verifyZeroInteractions(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testResetNullMocks() {
        mockitoCore.reset(null);
    }

    @Test(expected = NotAMockException.class)
    public void testResetEmptyMocks() {
        mockitoCore.reset();
    }

    @Test
    public void testAnswer() {
        Object mock = mock(Object.class);
        Stubber stubber = mockitoCore.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArgumentValues();
                return args[0];
            }
        });

        when(mock.doSomething(1)).thenAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgument(0) * 2;
            }
        });

        Object result = (Object) mockitoCore.when(mock).doSomething(1);
        assertEquals(2, result.hashCode());
    }

    @Test
    public void testStubVoid() {
        Object mock = mock(Object.class);
        mockitoCore.stubVoid(mock).doSomething();
        verify(mock).doSomething();
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
    }

    @Test(expected = NotAMockException.class)
    public void testGetLastInvocationBeforeStubbing() {
        Object mock = mock(Object.class);
        mockitoCore.getLastInvocation();
    }

    @Test
    public void testGetLastInvocationAfterStubbing() {
        Object mock = mock(Object.class);
        mockitoCore.stub(mock).doSomething().thenReturn(1);
        Invocation lastInvocation = mockitoCore.getLastInvocation();
        assertEquals(1, lastInvocation.getMethod().getName());
    }
}