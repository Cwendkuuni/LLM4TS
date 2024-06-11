import org.junit.Test;
import static org.mockito.Mockito.*;
import org.mockito.MockitoCore;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.MockUtil;
import org.mockito.internal.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.VerificationDataImpl;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.api.VerificationDataInOrderImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.stubbing.VoidMethodStubbable;
import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {

    @Test
    public void testMockCreation() {
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockSettings mockSettings = new MockSettingsImpl();
        MockitoCore mockitoCore = new MockitoCore().mock(mockitoCoreClass, mockSettings);
        assertNotNull(mockitoCore);
    }

    @Test
    public void testStubbing() {
        MockitoCore mockitoCore = new MockitoCore();
        IOngoingStubbing stubbing = mockitoCore.stub();
        assertNotNull(stubbing);
    }

    @Test
    public void testStubbingWithWhen() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = new Object();
        OngoingStubbing<Object> stubbing = mockitoCore.when(mock);
        assertNotNull(stubbing);
    }

    @Test
    public void testVerification() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = new Object();
        mockitoCore.verify(mock, VerificationMode.times(1));
    }

    @Test
    public void testReset() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = new Object();
        mockitoCore.reset(mock);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = new Object();
        mockitoCore.verifyNoMoreInteractions(mock);
    }

    @Test
    public void testInOrder() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock1 = new Object();
        Object mock2 = new Object();
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
    }

    @Test
    public void testDoAnswer() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = new Object();
        Answer answer = new Answer() {
            @Override
            public Object answer(Invocation invocation) throws Throwable {
                return "answer";
            }
        };
        Stubber stubber = mockitoCore.doAnswer(answer);
        stubber.when(mock).someMethod();
    }

    @Test
    public void testStubVoid() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = new Object();
        VoidMethodStubbable<Object> voidMethodStubbable = mockitoCore.stubVoid(mock);
        voidMethodStubbable.thenAnswer(new Answer() {
            @Override
            public Object answer(Invocation invocation) throws Throwable {
                return null;
            }
        });
    }

    @Test(expected = NullPointerException.class)
    public void testMockCreationWithNullClass() {
        MockitoCore mockitoCore = new MockitoCore();
        Class<?> nullClass = null;
        MockSettings mockSettings = new MockSettingsImpl();
        mockitoCore.mock(nullClass, mockSettings);
    }

    @Test(expected = NullPointerException.class)
    public void testMockCreationWithNullMockSettings() {
        MockitoCore mockitoCore = new MockitoCore();
        Class<MockitoCore> mockitoCoreClass = MockitoCore.class;
        MockSettings nullMockSettings = null;
        mockitoCore.mock(mockitoCoreClass, nullMockSettings);
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        Object nullMock = null;
        mockitoCore.verify(nullMock, VerificationMode.times(1));
    }

    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractionsWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        Object nullMock = null;
        mockitoCore.verifyNoMoreInteractions(nullMock);
    }

    @Test(expected = NotAMockException.class)
    public void testInOrderWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        Object nullMock = null;
        InOrder inOrder = mockitoCore.inOrder(nullMock);
    }

    @Test(expected = NotAMockException.class)
    public void testDoAnswerWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        Object nullMock = null;
        Answer answer = new Answer() {
            @Override
            public Object answer(Invocation invocation) throws Throwable {
                return null;
            }
        };
        Stubber stubber = mockitoCore.doAnswer(answer);
        stubber.when(nullMock).someMethod();
    }

    @Test(expected = NotAMockException.class)
    public void testStubVoidWithNullMock() {
        MockitoCore mockitoCore = new MockitoCore();
        Object nullMock = null;
        VoidMethodStubbable<Object> voidMethodStubbable = mockitoCore.stubVoid(nullMock);
        voidMethodStubbable.thenAnswer(new Answer() {
            @Override
            public Object answer(Invocation invocation) throws Throwable {
                return null;
            }
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyWithInvalidVerificationMode() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = new Object();
        mockitoCore.verify(mock, new VerificationMode() {
            @Override
            public VerificationMode times(long times) {
                throw new UnsupportedOperationException();
            }

            @Override
            public VerificationMode atLeastOnce() {
                throw new UnsupportedOperationException();
            }

            @Override
            public VerificationMode atLeast(long times) {
                throw new UnsupportedOperationException();
            }

            @Override
            public VerificationMode atMostOnce() {
                throw new UnsupportedOperationException();
            }

            @Override
            public VerificationMode atMost(long times) {
                throw new UnsupportedOperationException();
            }

            @Override
            public VerificationMode never() {
                throw new UnsupportedOperationException();
            }

            @Override
            public VerificationMode inOrder() {
                throw new UnsupportedOperationException();
            }
        });
    }

    @Test
    public void testVerifyNoMoreInteractionsWithEmptyMocks() {
        MockitoCore mockitoCore = new MockitoCore();
        Object[] emptyMocks = {};
        mockitoCore.verifyNoMoreInteractions(emptyMocks);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVerifyNoMoreInteractionsWithNullInOrderContext() {
        MockitoCore mockitoCore = new MockitoCore();
        Object[] mocks = {};
        mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mocks), null);
    }

    @Test(expected = AssertionError.class)
    public void testVerifyNoMoreInteractionsWithWrongMockOrder() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock1 = new Object();
        Object mock2 = new Object();
        mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock2, mock1), new VerificationDataInOrderImpl(null, null, null));
    }

    @Test
    public void testGetLastInvocationWhenNoInvocationsRegistered() {
        MockitoCore mockitoCore = new MockitoCore();
        assertNull(mockitoCore.getLastInvocation());
    }

    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockitoCore = new MockitoCore();
        mockitoCore.validateMockitoUsage();
    }
}