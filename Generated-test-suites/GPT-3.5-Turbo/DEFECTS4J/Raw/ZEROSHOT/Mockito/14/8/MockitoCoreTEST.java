import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockSettings;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        Class mockClass = ArrayList.class;
        MockSettings mockSettings = mock(MockSettingsImpl.class);

        when(mockitoCore.mock(mockClass, mockSettings)).thenReturn(mockClass);

        assertEquals(mockitoCore.mock(mockClass, mockSettings), mockClass);

        verify(mockitoCore, times(2)).mock(mockClass, mockSettings);
    }

    @Test
    public void testStub() {
        MockitoCore mockitoCore = mock(MockitoCore.class);

        when(mockitoCore.stub()).thenReturn(mock(IOngoingStubbing.class));

        assertNotNull(mockitoCore.stub());

        verify(mockitoCore).stub();
    }

    @Test
    public void testStubWithValue() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        Object methodCall = new Object();

        when(mockitoCore.stub(methodCall)).thenReturn(mock(DeprecatedOngoingStubbing.class));

        assertNotNull(mockitoCore.stub(methodCall));

        verify(mockitoCore).stub(methodCall);
    }

    @Test
    public void testWhen() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        Object methodCall = new Object();

        when(mockitoCore.when(methodCall)).thenReturn(mock(OngoingStubbing.class));

        assertNotNull(mockitoCore.when(methodCall));

        verify(mockitoCore).when(methodCall);
    }

    @Test
    public void testVerify() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        Object mock = new Object();
        VerificationMode mode = mock(VerificationMode.class);

        when(mockitoCore.verify(mock, mode)).thenReturn(mock);

        assertEquals(mockitoCore.verify(mock, mode), mock);

        verify(mockitoCore, times(2)).verify(mock, mode);
    }

    @Test
    public void testReset() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        Object[] mocks = {mock(Object.class)};

        doNothing().when(mockitoCore).reset(mocks);

        mockitoCore.reset(mocks);

        verify(mockitoCore).reset(mocks);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        Object[] mocks = {mock(Object.class)};

        doNothing().when(mockitoCore).verifyNoMoreInteractions(mocks);

        mockitoCore.verifyNoMoreInteractions(mocks);

        verify(mockitoCore).verifyNoMoreInteractions(mocks);
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        List<Object> mocks = new ArrayList<>();
        InOrderContext inOrderContext = mock(InOrderContext.class);

        doNothing().when(mockitoCore).verifyNoMoreInteractionsInOrder(mocks, inOrderContext);

        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);

        verify(mockitoCore).verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
    }

    @Test
    public void testInOrder() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        Object[] mocks = {mock(Object.class)};

        when(mockitoCore.inOrder(mocks)).thenReturn(mock(InOrder.class));

        assertNotNull(mockitoCore.inOrder(mocks));

        verify(mockitoCore).inOrder(mocks);
    }

    @Test
    public void testDoAnswer() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        Answer answer = mock(Answer.class);

        when(mockitoCore.doAnswer(answer)).thenReturn(mock(Stubber.class));

        assertNotNull(mockitoCore.doAnswer(answer));

        verify(mockitoCore).doAnswer(answer);
    }

    @Test
    public void testStubVoid() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        Object mock = mock(Object.class);

        when(mockitoCore.stubVoid(mock)).thenReturn(mock(VoidMethodStubbable.class));

        assertNotNull(mockitoCore.stubVoid(mock));

        verify(mockitoCore).stubVoid(mock);
    }

    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockitoCore = mock(MockitoCore.class);

        doNothing().when(mockitoCore).validateMockitoUsage();

        mockitoCore.validateMockitoUsage();

        verify(mockitoCore).validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        Invocation invocation = mock(Invocation.class);

        when(mockitoCore.getLastInvocation()).thenReturn(invocation);

        assertEquals(mockitoCore.getLastInvocation(), invocation);

        verify(mockitoCore).getLastInvocation();
    }
}