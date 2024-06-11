import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.AllInvocationsFinder;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.internal.verification.api.InOrderContext;
import org.mockito.internal.verification.api.VerificationDataInOrder;
import org.mockito.internal.verification.api.VerificationDataInOrderImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore = new MockitoCore();

    // Test mock()
    @Test
    public void testMock() {
        // Typical case
        List<Integer> mockList = mockitoCore.mock(List.class, Mockito.mock(MockSettings.class));
        assertNotNull(mockList);

        // Edge case: classToMock is an interface
        Comparable<String> mockComparable = mockitoCore.mock(Comparable.class, Mockito.mock(MockSettings.class));
        assertNotNull(mockComparable);
    }

    // Test stub()
    @Test
    public void testStub() {
        // Typical case: stubbing is in progress
        IOngoingStubbing ongoingStubbing = mockitoCore.stub();
        assertNull(ongoingStubbing);
        
        // Edge case: stubbing is not in progress
        mockitoCore.stub();
        verify(mockitoCore, times(2)).stub();
        assertNotNull(ongoingStubbing);
    }

    // Test stub(T methodCall)
    @Test
    public void testStubWithMethodCall() {
        // Typical case: stubbing is in progress
        List<Integer> methodCall = Arrays.asList(1, 2, 3);
        DeprecatedOngoingStubbing deprecatedOngoingStubbing = mockitoCore.stub(methodCall);
        assertNull(deprecatedOngoingStubbing);

        // Edge case: stubbing is not in progress
        mockitoCore.stub(methodCall);
        verify(mockitoCore, times(2)).stub(methodCall);
        assertNotNull(deprecatedOngoingStubbing);
    }

    // Test when(T methodCall)
    @Test
    public void testWhen() {
        // Typical case: stubbing is in progress
        List<Integer> methodCall = Arrays.asList(1, 2, 3);
        OngoingStubbing ongoingStubbing = mockitoCore.when(methodCall);
        assertNull(ongoingStubbing);

        // Edge case: stubbing is not in progress
        mockitoCore.when(methodCall);
        verify(mockitoCore, times(2)).when(methodCall);
        assertNotNull(ongoingStubbing);
    }

    // Test verify(T mock, VerificationMode mode)
    @Test
    public void testVerify() {
        // Typical case: mock is not null and is a mock
        List<Integer> mockList = mock(List.class);
        List<Integer> verifiedMock = mockitoCore.verify(mockList, Mockito.times(1));
        assertNotNull(verifiedMock);

        // Error scenario: mock is null
        try {
            mockitoCore.verify(null, Mockito.times(1));
            fail("Expected nullPassedToVerify() exception to be thrown");
        } catch (Reporter.NullPassedToVerifyException e) {
            // Test passed
        }

        // Error scenario: mock is not a mock
        try {
            mockitoCore.verify(new Object(), Mockito.times(1));
            fail("Expected notAMockPassedToVerify() exception to be thrown");
        } catch (Reporter.NotAMockPassedToVerifyException e) {
            // Test passed
        }
    }

    // Test reset(T ... mocks)
    @Test
    public void testReset() {
        // Typical case: reset multiple mocks
        List<Integer> mockList1 = mock(List.class);
        List<Integer> mockList2 = mock(List.class);
        mockitoCore.reset(mockList1, mockList2);

        verify(mockitoCore, times(1)).reset(mockList1, mockList2);
        verify(mockitoCore, times(1)).resetOngoingStubbing();
        verify(mockitoCore, times(1)).mockUtil.resetMock(mockList1);
        verify(mockitoCore, times(1)).mockUtil.resetMock(mockList2);
    }

    // Test verifyNoMoreInteractions(Object... mocks)
    @Test
    public void testVerifyNoMoreInteractions() {
        // Typical case: verify no more interactions on mock
        List<Integer> mockList = mock(List.class);
        mockList.add(1);
        mockitoCore.verifyNoMoreInteractions(mockList);

        verify(mockitoCore, times(1)).verifyNoMoreInteractions(mockList);
        verify(mockitoCore, times(1)).assertMocksNotEmpty(mockList);

        // Error scenario: mock is null
        try {
            mockitoCore.verifyNoMoreInteractions(null);
            fail("Expected nullPassedToVerifyNoMoreInteractions() exception to be thrown");
        } catch (Reporter.NullPassedToVerifyNoMoreInteractionsException e) {
            // Test passed
        }

        // Error scenario: mock is not a mock
        try {
            mockitoCore.verifyNoMoreInteractions(new Object());
            fail("Expected notAMockPassedToVerifyNoMoreInteractions() exception to be thrown");
        } catch (Reporter.NotAMockPassedToVerifyNoMoreInteractionsException e) {
            // Test passed
        }
    }

    // Test verifyNoMoreInteractionsInOrder(List<Object> mocks, InOrderContext inOrderContext)
    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        // Typical case: verify no more interactions in order
        List<Object> mocks = Arrays.asList(mock(List.class), mock(List.class));
        InOrderContext inOrderContext = Mockito.mock(InOrderContext.class);
        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);

        verify(mockitoCore, times(1)).verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
        verify(mockitoCore, times(1)).mockingProgress.validateState();

        // Error scenario: mocks is empty
        try {
            mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(), inOrderContext);
            fail("Expected mocksHaveToBePassedToVerifyNoMoreInteractions() exception to be thrown");
        } catch (Reporter.MocksHaveToBePassedToVerifyNoMoreInteractionsException e) {
            // Test passed
        }
    }

    // Test inOrder(Object... mocks)
    @Test
    public void testInOrder() {
        // Typical case: create InOrder with multiple mocks
        List<Integer> mockList1 = mock(List.class);
        List<Integer> mockList2 = mock(List.class);
        InOrder inOrder = mockitoCore.inOrder(mockList1, mockList2);

        assertNotNull(inOrder);

        // Error scenario: mocks is empty
        try {
            mockitoCore.inOrder();
            fail("Expected mocksHaveToBePassedWhenCreatingInOrder() exception to be thrown");
        } catch (Reporter.MocksHaveToBePassedWhenCreatingInOrderException e) {
            // Test passed
        }

        // Error scenario: mock is null
        try {
            mockitoCore.inOrder(mockList1, null, mockList2);
            fail("Expected nullPassedWhenCreatingInOrder() exception to be thrown");
        } catch (Reporter.NullPassedWhenCreatingInOrderException e) {
            // Test passed
        }

        // Error scenario: mock is not a mock
        try {
            mockitoCore.inOrder(mockList1, new Object(), mockList2);
            fail("Expected notAMockPassedWhenCreatingInOrder() exception to be thrown");
        } catch (Reporter.NotAMockPassedWhenCreatingInOrderException e) {
            // Test passed
        }
    }

    // Test doAnswer(Answer answer)
    @Test
    public void testDoAnswer() {
        // Typical case: do answer while stubbing is in progress
        Answer answer = Mockito.mock(Answer.class);
        Stubber stubber = mockitoCore.doAnswer(answer);

        assertNotNull(stubber);
        verify(mockitoCore, times(1)).doAnswer(answer);
        verify(mockitoCore, times(1)).resetOngoingStubbing();

        // Error scenario: stubbing is not in progress
        try {
            mockitoCore.doAnswer(answer);
            fail("Expected stubbingStarted() exception to be thrown");
        } catch (MockingProgress.StubbingStartedException e) {
            // Test passed
        }
    }

    // Test stubVoid(T mock)
    @Test
    public void testStubVoid() {
        // Typical case: stub void method while stubbing is in progress
        List<Integer> mockList = mock(List.class);
        VoidMethodStubbable<Void> voidMethodStubbable = mockitoCore.stubVoid(mockList);

        assertNotNull(voidMethodStubbable);
        verify(mockitoCore, times(1)).stubVoid(mockList);
        verify(mockitoCore, times(1)).mockingProgress.stubbingStarted();

        // Error scenario: stubbing is not in progress
        try {
            mockitoCore.stubVoid(mockList);
            fail("Expected stubbingStarted() exception to be thrown");
        } catch (MockingProgress.StubbingStartedException e) {
            // Test passed
        }
    }

    // Test validateMockitoUsage()
    @Test
    public void testValidateMockitoUsage() {
        // Typical case: validate Mockito usage
        mockitoCore.validateMockitoUsage();

        verify(mockitoCore, times(1)).validateMockitoUsage();
        verify(mockitoCore, times(1)).mockingProgress.validateState();
    }

    // Test getLastInvocation()
    @Test
    public void testGetLastInvocation() {
        // Typical case: get last invocation
        OngoingStubbingImpl ongoingStubbing = Mockito.mock(OngoingStubbingImpl.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Invocation> allInvocations = Arrays.asList(invocation);
        when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);
        when(mockitoCore.mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);

        Invocation lastInvocation = mockitoCore.getLastInvocation();

        assertEquals(invocation, lastInvocation);
        verify(mockitoCore, times(1)).getLastInvocation();
        verify(ongoingStubbing, times(1)).getRegisteredInvocations();
    }

}