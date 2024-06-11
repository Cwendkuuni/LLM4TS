
package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.stubbing.VoidMethodStubbable;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public class MockitoCoreTest {
    private MockitoCore mockitoCore;
    private Reporter reporter;
    private MockUtil mockUtil;
    private MockingProgress mockingProgress;
    
    @Before
    public void setup() {
        mockitoCore = new MockitoCore();
        reporter = new Reporter();
        mockUtil = new MockUtil();
        mockingProgress = new ThreadSafeMockingProgress();
    }
    
    @Test
    public void testMock() {
        Class<?> classToMock = String.class;
        MockSettings mockSettings = Mockito.mock(MockSettings.class);
        boolean shouldResetOngoingStubbing = true;
        
        Object result = mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing);
        
        assertNotNull(result);
        assertTrue(result instanceof String);
    }
    
    @Test
    public void testMockWithSettings() {
        Class<?> classToMock = Integer.class;
        MockSettings mockSettings = Mockito.mock(MockSettings.class);
        
        Object result = mockitoCore.mock(classToMock, mockSettings);
        
        assertNotNull(result);
        assertTrue(result instanceof Integer);
    }
    
    @Test
    public void testStub() {
        IOngoingStubbing expectedStubbing = Mockito.mock(IOngoingStubbing.class);
        mockingProgress.stubbingStarted();
        
        IOngoingStubbing result = mockitoCore.stub();
        
        assertSame(expectedStubbing, result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testStubNoOngoingStubbing() {
        mockitoCore.stub();
    }
    
    @Test
    public void testDeprecatedStub() {
        Object methodCall = "mockMethod";
        mockingProgress.stubbingStarted();
        
        DeprecatedOngoingStubbing<?> result = mockitoCore.stub(methodCall);
        
        assertNotNull(result);
    }
    
    @Test
    public void testWhen() {
        Object methodCall = "mockMethod";
        mockingProgress.stubbingStarted();
        
        OngoingStubbing<?> result = mockitoCore.when(methodCall);
        
        assertNotNull(result);
    }
    
    @Test
    public void testVerify() {
        Object mock = Mockito.mock(Object.class);
        VerificationMode mode = Mockito.mock(VerificationMode.class);
        
        Object result = mockitoCore.verify(mock, mode);
        
        assertSame(mock, result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testVerifyNullMock() {
        mockitoCore.verify(null, Mockito.mock(VerificationMode.class));
    }
    
    @Test(expected = NotAMockException.class)
    public void testVerifyNotAMock() {
        Object object = new Object();
        
        mockitoCore.verify(object, Mockito.mock(VerificationMode.class));
    }
    
    @Test
    public void testReset() {
        Object[] mocks = {Mockito.mock(Object.class)};
        
        mockitoCore.reset(mocks);
        
        assertTrue(true);
    }
    
    @Test
    public void testResetEmptyMocks() {
        Object[] mocks = {};
        
        mockitoCore.reset(mocks);
        
        assertTrue(true);
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        Object[] mocks = {Mockito.mock(Object.class)};
        
        mockitoCore.verifyNoMoreInteractions(mocks);
        
        assertTrue(true);
    }
    
    @Test(expected = NotAMockException.class)
    public void testVerifyNoMoreInteractionsNotAMock() {
        Object[] mocks = { new Object(), Mockito.mock(Object.class) };
        
        mockitoCore.verifyNoMoreInteractions(mocks);
    }
    
    @Test(expected = NullPointerException.class)
    public void testVerifyNoMoreInteractionsNullMock() {
        Object[] mocks = { Mockito.mock(Object.class), null };
        
        mockitoCore.verifyNoMoreInteractions(mocks);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testVerifyNoMoreInteractionsEmptyMocks() {
        Object[] mocks = {};
        
        mockitoCore.verifyNoMoreInteractions(mocks);
    }
    
    @Test
    public void testInOrder() {
        Object[] mocks = {Mockito.mock(Object.class)};
        
        InOrder result = mockitoCore.inOrder(mocks);
        
        assertNotNull(result);
        assertTrue(result instanceof InOrder);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInOrderEmptyMocks() {
        Object[] mocks = {};
        
        mockitoCore.inOrder(mocks);
    }
    
    @Test(expected = NullPointerException.class)
    public void testInOrderNullMock() {
        Object[] mocks = {Mockito.mock(Object.class), null};
        
        mockitoCore.inOrder(mocks);
    }
    
    @Test
    public void testDoAnswer() {
        Answer answer = Mockito.mock(Answer.class);
        mockingProgress.stubbingStarted();
        mockingProgress.resetOngoingStubbing();
        
        Stubber result = mockitoCore.doAnswer(answer);
        
        assertNotNull(result);
        assertTrue(result instanceof StubberImpl);
    }
    
    @Test
    public void testStubVoid() {
        Object mock = Mockito.mock(Object.class);
        mockingProgress.stubbingStarted();
        
        VoidMethodStubbable<?> result = mockitoCore.stubVoid(mock);
        
        assertNotNull(result);
        assertTrue(result instanceof VoidMethodStubbable);
    }
    
    @Test
    public void testValidateMockitoUsage() {
        mockingProgress.validateState();
        
        assertTrue(true);
    }
    
    @Test
    public void testGetLastInvocation() {
        OngoingStubbingImpl ongoingStubbing = Mockito.mock(OngoingStubbingImpl.class);
        List<Invocation> allInvocations = Mockito.mock(List.class);
        
        Mockito.when(mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
        Mockito.when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);
        Mockito.when(allInvocations.size()).thenReturn(1);
        
        Invocation result = mockitoCore.getLastInvocation();
        
        assertNotNull(result);
    }
}
