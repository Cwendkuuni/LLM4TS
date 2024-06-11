package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.verification.VerificationMode;

public class MockitoCoreTest {
    
    private MockitoCore mockitoCore = new MockitoCore();
    private Reporter reporter = new Reporter();
    
    @Test
    public void testMock() {
        Class<T> classToMock = T.class;
        MockSettings mockSettings = Mockito.mock(MockSettings.class);
        assertEquals(T.class, mockitoCore.mock(classToMock, mockSettings));
    }
    
    @Test
    public void testStub() {
        IOngoingStubbing stubbing = Mockito.mock(IOngoingStubbing.class);
        assertEquals(stubbing, mockitoCore.stub());
    }
    
    @Test
    public void testStub_method() {
        T methodCall = Mockito.mock(T.class);
        Mockito.when(mockitoCore.stub(methodCall)).thenReturn(Mockito.mock(DeprecatedOngoingStubbing.class));
    }
    
    @Test
    public void testWhen() {
        T methodCall = Mockito.mock(T.class);
        Mockito.when(mockitoCore.when(methodCall)).thenReturn(Mockito.mock(OngoingStubbing.class));
    }
    
    @Test
    public void testVerify() {
        T mock = Mockito.mock(T.class);
        VerificationMode mode = Mockito.mock(VerificationMode.class);
        assertEquals(mock, mockitoCore.verify(mock, mode));
    }
    
    @Test
    public void testReset() {
        T[] mocks = {Mockito.mock(T.class)};
        mockitoCore.reset(mocks);
        assertNull(mockitoCore.getLastInvocation());
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        Object[] mocks = {Mockito.mock(T.class)};
        mockitoCore.verifyNoMoreInteractions(mocks);
    }
    
    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        List<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(T.class));
        InOrderContext inOrderContext = Mockito.mock(InOrderContext.class);
        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
    }
    
    @Test
    public void testInOrder() {
        Object[] mocks = {Mockito.mock(T.class)};
        assertEquals(Mockito.inOrder(mocks), mockitoCore.inOrder(mocks));
    }
    
    @Test
    public void testDoAnswer() {
        Answer answer = Mockito.mock(Answer.class);
        Stubber stubber = Mockito.mock(Stubber.class);
        Mockito.when(stubber.doAnswer(answer)).thenReturn(Mockito.mock(Stubber.class));
    }
    
    @Test
    public void testStubVoid() {
        T mock = Mockito.mock(T.class);
        Mockito.when(mockitoCore.stubVoid(mock)).thenReturn(Mockito.mock(VoidMethodStubbable.class));
    }
    
    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
    }
    
    @Test
    public void testGetLastInvocation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        OngoingStubbingImpl ongoingStubbing = Mockito.mock(OngoingStubbingImpl.class);
        List<Invocation> allInvocations = new ArrayList<>();
        allInvocations.add(invocation);
        Mockito.when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);
        Mockito.when(mockitoCore.getLastInvocation()).thenReturn(invocation);
    }
}