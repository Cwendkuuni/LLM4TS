
/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

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

@SuppressWarnings("unchecked")
public class MockitoCoreTest {

    private final Reporter reporter = new Reporter();
    private final MockUtil mockUtil = new MockUtil();
    private final MockingProgress mockingProgress = new ThreadSafeMockingProgress();
    
    @Test
    public void testMock() {
        MockitoCore mockitoCore = new MockitoCore();
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        
        // Null check
        assertNull(mockitoCore.mock(null, mockSettings));
        
        // Successful mock creation
        List<String> mock = mockitoCore.mock(List.class, mockSettings);
        assertTrue(mockUtil.isMock(mock));
    }
    
    @Test
    public void testStub() {
        MockitoCore mockitoCore = new MockitoCore();
        
        // Null check
        assertNull(mockitoCore.stub());
        
        // Missing method invocation
        mockitoCore.reset();
        reporter.missingMethodInvocation();
        assertNull(mockitoCore.stub());
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void testStub_Deprecated() {
        MockitoCore mockitoCore = new MockitoCore();
        
        // Successful stubbing
        mockitoCore.stub("methodCall");
        
        // Deprecated method call stubbing
        ((DeprecatedOngoingStubbing<String>) mockitoCore.stub("methodCall")).toReturn("result");
    }
    
    @Test
    public void testWhen() {
        MockitoCore mockitoCore = new MockitoCore();
        
        // Successful stubbing
        mockitoCore.when("methodCall");
        
        // Deprecated method call stubbing
        ((OngoingStubbing<String>) mockitoCore.stub("methodCall")).thenReturn("result");
    }
    
    @Test
    public void testVerify() {
        MockitoCore mockitoCore = new MockitoCore();
        
        // Null passed to verify
        reporter.nullPassedToVerify();
        assertNull(mockitoCore.verify(null, Mockito.times(1)));
        
        // Not a mock passed to verify
        reporter.notAMockPassedToVerify();
        assertNull(mockitoCore.verify("notAMock", Mockito.times(1)));
        
        // Successful verification
        List<String> mock = mockitoCore.mock(List.class, new MockSettingsImpl());
        assertEquals(mock, mockitoCore.verify(mock, Mockito.times(1)));
    }
    
    @Test
    public void testReset() {
        MockitoCore mockitoCore = new MockitoCore();
        
        // Successful reset
        List<String> mock1 = mockitoCore.mock(List.class, new MockSettingsImpl());
        List<String> mock2 = mockitoCore.mock(List.class, new MockSettingsImpl());
        mockitoCore.reset(mock1, mock2);
        
        // Null parameter
        mockitoCore.reset((List<String>[]) null);
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoCore mockitoCore = new MockitoCore();
        
        // Null passed to verifyNoMoreInteractions
        reporter.nullPassedToVerifyNoMoreInteractions();
        mockitoCore.verifyNoMoreInteractions(null);
        
        // Not a mock passed to verifyNoMoreInteractions
        reporter.notAMockPassedToVerifyNoMoreInteractions();
        mockitoCore.verifyNoMoreInteractions("notAMock");
        
        // Successful verification
        List<String> mock = mockitoCore.mock(List.class, new MockSettingsImpl());
        mockitoCore.verifyNoMoreInteractions(mock);
    }
    
    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        MockitoCore mockitoCore = new MockitoCore();
        
        // Successful verification
        List<Object> mocks = Arrays.asList(mockitoCore.mock(List.class, new MockSettingsImpl()));
        InOrderContext inOrderContext = Mockito.mock(InOrderContext.class);
        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
    }
    
    @Test
    public void testInOrder() {
        MockitoCore mockitoCore = new MockitoCore();
        
        // Null mocks passed when creating in order
        reporter.mocksHaveToBePassedWhenCreatingInOrder();
        mockitoCore.inOrder((Object[]) null);
        
        // Null passed when creating in order
        reporter.nullPassedWhenCreatingInOrder();
        mockitoCore.inOrder(mockitoCore.mock(List.class, new MockSettingsImpl()), null);
        
        // Not a mock passed when creating in order
        reporter.notAMockPassedWhenCreatingInOrder();
        mockitoCore.inOrder(mockitoCore.mock(List.class, new MockSettingsImpl()), "notAMock");
        
        // Successful in order creation
        List<String> mock1 = mockitoCore.mock(List.class, new MockSettingsImpl());
        List<String> mock2 = mockitoCore.mock(List.class, new MockSettingsImpl());
        InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
        assertTrue(inOrder instanceof InOrderImpl);
    }
    
    @Test
    public void testDoAnswer() {
        MockitoCore mockitoCore = new MockitoCore();
        
        // Successful stubbing
        mockitoCore.stub();
        
        // Successful doAnswer
        Answer<Object> answer = (invocation) -> null;
        Stubber stubber = mockitoCore.doAnswer(answer);
        assertTrue(stubber instanceof StubberImpl);
    }
    
    @Test
    public void testStubVoid() {
        MockitoCore mockitoCore = new MockitoCore();
        
        // Successful stubbing
        List<String> mock = mockitoCore.mock(List.class, new MockSettingsImpl());
        mockitoCore.stubVoid(mock);
    }
    
    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockitoCore = new MockitoCore();
        
        // Successful validation
        mockitoCore.validateMockitoUsage();
    }
    
    @Test
    public void testGetLastInvocation() {
        MockitoCore mockitoCore = new MockitoCore();
        
        // Null last invocation
        assertNull(mockitoCore.getLastInvocation());
        
        // Successful last invocation retrieval
        mockitoCore.stub();
        Invocation invocation = mockitoCore.getLastInvocation();
        assertTrue(invocation instanceof Invocation);
    }
}
