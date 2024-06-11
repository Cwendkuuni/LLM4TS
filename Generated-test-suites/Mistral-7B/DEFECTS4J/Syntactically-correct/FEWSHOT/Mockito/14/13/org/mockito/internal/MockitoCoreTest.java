package org.mockito.internal;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.invocation.AllInvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.DeprecatedOngoingStubbing;
import org.mockito.internal.stubbing.OngoingStubbing;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.verification.InOrderContext;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.mock.MockHandlerInterface;
import org.mockito.stubbing.Answer2;
import org.mockito.stubbing.Answer3;
import org.mockito.stubbing.Answer4;
import org.mockito.stubbing.VoidMethodStubbable;

public class MockitoCoreTest {

    private Reporter reporter;
    private MockUtil mockUtil;
    private MockitoCore mockitoCore;
    private MockHandlerInterface<Object> mockHandler;
    private InOrder inOrder;

    @Before
    public void setUp() {
        reporter = new Reporter();
        mockUtil = mock(MockUtil.class);
        mockitoCore = new MockitoCore() {
            @Override
            public Reporter getReporter() {
                return reporter;
            }

            @Override
            public MockUtil getMockUtil() {
                return mockUtil;
            }
        };
        mockHandler = mock(MockHandlerInterface.class);
        inOrder = inOrder(mockHandler);
    }

    @Test
    public void mock_shouldReturnMockedObject() {
        Class<String> classToMock = String.class;
        MockSettings mockSettings = new MockSettingsImpl();
        String mockedObject = mockitoCore.mock(classToMock, mockSettings);
        assertSame(mockedObject, mockitoCore.mock(classToMock, mockSettings));
    }

    @Test
    public void mock_shouldCallMockUtilCreateMock() {
        Class<String> classToMock = String.class;
        MockSettings mockSettings = new MockSettingsImpl();
        String mockedObject = mockitoCore.mock(classToMock, mockSettings);
        verify(mockUtil).createMock(classToMock, (MockSettingsImpl) mockSettings);
    }

    @Test
    public void stub_shouldReturnOngoingStubbing() {
        IOngoingStubbing stubbing = mockitoCore.stub();
        assertSame(stubbing, mockitoCore.stub());
    }

    @Test
    public void stub_shouldResetMockingProgressIfOngoingStubbingIsNull() {
        mockitoCore.stubbingStarted();
        mockitoCore.reset();
        mockingProgress.assertReset();
    }

    @Test(expected = RuntimeException.class)
    public void stub_shouldThrowExceptionIfOngoingStubbingIsNotResettable() {
        mockitoCore.stubbingStarted();
        mockitoCore.pullOngoingStubbing();
        mockitoCore.reset();
    }

    @Test
    public void stub_shouldResetOngoingStubbing() {
        mockitoCore.stubbingStarted();
        IOngoingStubbing stubbing = mockitoCore.stub();
        mockitoCore.reset();
        assertNull(mockitoCore.pullOngoingStubbing());
    }

    @Test
    public void stub_whenCalledWithNull_shouldThrowNullPointerException() {
        try {
            mockitoCore.stub(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void stub_whenCalledWithT_shouldReturnDeprecatedOngoingStubbing() {
        DeprecatedOngoingStubbing<Object> deprecatedStubbing = mockitoCore.stub((Object) null);
        assertSame(deprecatedStubbing, mockitoCore.stub((Object) null));
    }

    @Test
    public void when_shouldReturnOngoingStubbing() {
        OngoingStubbing<Object> ongoingStubbing = mockitoCore.when((Object) null);
        assertSame(ongoingStubbing, mockitoCore.when((Object) null));
    }

    @Test
    public void when_shouldReturnNullWhenNoOngoingStubbing() {
        mockitoCore.stubbingStarted();
        assertNull(mockitoCore.when((Object) null));
    }

    @Test
    public void verify_shouldThrowNullPointerExceptionWhenCalledWithNull() {
        try {
            mockitoCore.verify(null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void verify_shouldThrowNotAMockExceptionWhenCalledWithNonMockObject() {
        Object nonMockObject = new Object();
        try {
            mockitoCore.verify(nonMockObject, null);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Expected
        }
    }

    @Test
    public void verify_shouldStartVerification() {
        Object mockObject = mock(Object.class);
        mockitoCore.verify(mockObject, VerificationModeFactory.withDefaultVerifyOptions());
        mockingProgress.assertVerificationStarted();
    }

    @Test
    public void reset_shouldResetMockingProgress() {
        mockitoCore.mockingStarted(mockHandler, Object.class, new MockSettingsImpl());
        mockitoCore.reset();
        mockingProgress.assertReset();
    }

    @Test
    public void reset_shouldResetOngoingStubbing() {
        mockitoCore.stubbingStarted();
        IOngoingStubbing stubbing = mockitoCore.stub();
        mockitoCore.reset();
        assertNull(mockitoCore.pullOngoingStubbing());
    }

    @Test
    public void verifyNoMoreInteractions_shouldThrowNullPointerExceptionWhenCalledWithNull() {
        try {
            mockitoCore.verifyNoMoreInteractions(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void verifyNoMoreInteractions_shouldThrowNotAMockExceptionWhenCalledWithNonMockObject() {
        Object nonMockObject = new Object();
        try {
            mockitoCore.verifyNoMoreInteractions(nonMockObject);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // Expected
        }
    }

    @Test
    public void verifyNoMoreInteractions_shouldVerifyNoMoreInteractions() {
        Object mockObject = mock(Object.class);
        List<Invocation> invocations = Arrays.asList(mock(Invocation.class));
        when(mockHandler.getInvocationContainer()).thenReturn(new InvocationContainer() {
            @Override
            public List<Invocation> getInvocations() {
                return invocations;
            }
        });
        mockitoCore.verifyNoMoreInteractions(mockObject);
        verifyNoMoreInteractions(mockObject);
    }

    @Test
    public void verifyNoMoreInteractionsInOrder_shouldThrowNullPointerExceptionWhenCalledWithNull() {
        try {
            mockitoCore.verifyNoMoreInteractionsInOrder(null, inOrder());
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void verifyNoMoreInteractionsInOrder_shouldThrowIllegalArgumentExceptionWhenCalledWithEmptyList() {
        try {
            mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(), inOrder());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void verifyNoMoreInteractionsInOrder_shouldVerifyNoMoreInteractionsInOrder() {
        Object mockObject1 = mock(Object.class);
        Object mockObject2 = mock(Object.class);
        List<Object> mocks = Arrays.asList(mockObject1, mockObject2);
        List<Invocation> invocations = Arrays.asList(mock(Invocation.class), mock(Invocation.class));
        when(mockHandler.getInvocationContainer()).thenReturn(new InvocationContainer() {
            @Override
            public List<Invocation> getInvocations() {
                return invocations;
            }
        });
        InOrderContext context = inOrder(mockObject1, mockObject2);
        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, context);
        verifyNoMoreInteractionsInOrder(mocks, context);
    }

    @Test
    public void inOrder_shouldReturnInOrderInstance() {
        Object mockObject = mock(Object.class);
        InOrder inOrder = mockitoCore.inOrder(mockObject);
        assertSame(inOrder, mockitoCore.inOrder(mockObject));
    }

    @Test
    public void inOrder_shouldThrowIllegalArgumentExceptionWhenCalledWithNull() {
        try {
            mockitoCore.inOrder(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void inOrder_shouldVerifyOrderOfInvocations() {
        Object mockObject1 = mock(Object.class);
        Object mockObject2 = mock(Object.class);
        Object mockObject3 = mock(Object.class);
        List<Object> mocks = Arrays.asList(mockObject1, mockObject2, mockObject3);
        List<Invocation> invocations = Arrays.asList(mock(Invocation.class), mock(Invocation.class), mock(Invocation.class));
        when(mockHandler.getInvocationContainer()).thenReturn(new InvocationContainer() {
            @Override
            public List<Invocation> getInvocations() {
                return invocations;
            }
        });
        InOrder inOrder = mockitoCore.inOrder(mocks.toArray(new Object[0]));
        inOrder.verify(mockObject1).someMethod();
        inOrder.verify(mockObject2).someMethod();
        inOrder.verify(mockObject3).someMethod();
        verifyOrderOfInvocations(invocations);
    }

    @Test
    public void doAnswer_shouldReturnStubberImpl() {
        Answer<Void> answer = mock(Answer.class);
        Stubber stubber = mockitoCore.doAnswer(answer);
        assertSame(stubber, mockitoCore.doAnswer(answer));
    }

    @Test
    public void stubVoid_shouldReturnVoidMethodStubbable() {
        VoidMethodStubbable<Object> voidMethodStubbable = mockitoCore.stubVoid(mockHandler);
        assertSame(voidMethodStubbable, mockitoCore.stubVoid(mockHandler));
    }

    @Test
    public void validateMockitoUsage_shouldNotThrowExceptionWhenMockitoIsUsedCorrectly() {
        mockitoCore.validateMockitoUsage();
    }

    @Test(expected = RuntimeException.class)
    public void validateMockitoUsage_shouldThrowExceptionWhenMockitoIsUsedIncorrectly() {
        mockitoCore = spy(mockitoCore);
        doThrow(new RuntimeException()).when(mockitoCore).pullOngoingStubbing();
        mockitoCore.validateMockitoUsage();
    }

    private void verifyOrderOfInvocations(List<Invocation> invocations) {
        for (int i = 0; i < invocations.size() - 1; i++) {
            Invocation invocation1 = invocations.get(i);
            Invocation invocation2 = invocations.get(i + 1);
            verify(invocation1.getMock()).someMethod();
            verify(invocation2.getMock()).someMethod();
            assertTrue(invocation1.getMock() == invocation2.getMock() || invocation1.getMock().getClass().isAssignableFrom(invocation2.getMock().getClass()));
            assertTrue(invocation1.getProxiedMethod().getParameterTypes()[0].isAssignableFrom(invocation2.getProxiedMethod().getParameterTypes()[0]));
            assertTrue(Arrays.deepEquals(invocation1.getArguments(), invocation2.getArguments()));
        }
    }
}