package org.mockito.internal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockSettings;
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
import org.mockito.stubbing.*;

import java.util.Arrays;
import java.util.List;

public class MockitoCoreTest {
    
    private MockitoCore mockitoCore;
    private Reporter reporter;
    private MockUtil mockUtil;
    private MockingProgress mockingProgress;
    
    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
    }
    
    @Test
    public void testMock() {
        Class<?> classToMock = TestClass.class;
        MockSettings mockSettings = new MockSettingsImpl();
        boolean shouldResetOngoingStubbing = true;
        
        TestClass mockObject = mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing);
        
        // Verify that the mock object is created correctly
        assertEquals(classToMock, mockObject.getClass());
        verify(mockUtil).createMock(classToMock, (MockSettingsImpl) mockSettings);
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockingProgress).validateState();
    }
    
    @Test
    public void testMockWithoutShouldResetOngoingStubbing() {
        Class<?> classToMock = TestClass.class;
        MockSettings mockSettings = new MockSettingsImpl();
        boolean shouldResetOngoingStubbing = false;
        
        TestClass mockObject = mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing);
        
        // Verify that the mock object is created correctly
        assertEquals(classToMock, mockObject.getClass());
        verify(mockUtil).createMock(classToMock, (MockSettingsImpl) mockSettings);
        verify(mockingProgress, never()).resetOngoingStubbing();
        verify(mockingProgress).validateState();
    }
    
    @Test
    public void testMockWithNullSettings() {
        Class<?> classToMock = TestClass.class;
        MockSettings mockSettings = null;
        boolean shouldResetOngoingStubbing = true;
        
        TestClass mockObject = mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing);
        
        // Verify that the mock object is created correctly
        assertEquals(classToMock, mockObject.getClass());
        verify(mockUtil).createMock(classToMock, (MockSettingsImpl) mockSettings);
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockingProgress).validateState();
    }
    
    @Test
    public void testMockWithoutResetOngoingStubbingAndNullSettings() {
        Class<?> classToMock = TestClass.class;
        MockSettings mockSettings = null;
        boolean shouldResetOngoingStubbing = false;
        
        TestClass mockObject = mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing);
        
        // Verify that the mock object is created correctly
        assertEquals(classToMock, mockObject.getClass());
        verify(mockUtil).createMock(classToMock, (MockSettingsImpl) mockSettings);
        verify(mockingProgress, never()).resetOngoingStubbing();
        verify(mockingProgress).validateState();
    }
    
    @Test
    public void testMockWithMockSettings() {
        Class<?> classToMock = TestClass.class;
        MockSettings mockSettings = mock(MockSettings.class);
        boolean shouldResetOngoingStubbing = true;
        
        TestClass mockObject = mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing);
        
        // Verify that the mock object is created correctly
        assertEquals(classToMock, mockObject.getClass());
        verify(mockUtil).createMock(classToMock, (MockSettingsImpl) mockSettings);
        verify(mockingProgress).resetOngoingStubbing();
        verify(mockingProgress).validateState();
    }
    
    @Test
    public void testMockWithoutResetOngoingStubbingAndMockSettings() {
        Class<?> classToMock = TestClass.class;
        MockSettings mockSettings = mock(MockSettings.class);
        boolean shouldResetOngoingStubbing = false;
        
        TestClass mockObject = mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing);
        
        // Verify that the mock object is created correctly
        assertEquals(classToMock, mockObject.getClass());
        verify(mockUtil).createMock(classToMock, (MockSettingsImpl) mockSettings);
        verify(mockingProgress, never()).resetOngoingStubbing();
        verify(mockingProgress).validateState();
    }
    
    @Test
    public void testStub() {
        IOngoingStubbing stubbing = mock(IOngoingStubbing.class);
        when(mockingProgress.pullOngoingStubbing()).thenReturn(stubbing);
        
        IOngoingStubbing result = mockitoCore.stub();
        
        // Verify that the correct stubbing is returned
        assertEquals(stubbing, result);
        verify(mockingProgress).pullOngoingStubbing();
        verify(mockingProgress, never()).reset();
        verify(reporter, never()).missingMethodInvocation();
    }
    
    @Test
    public void testStubWithNullStubbing() {
        IOngoingStubbing stubbing = null;
        when(mockingProgress.pullOngoingStubbing()).thenReturn(stubbing);
        
        IOngoingStubbing result = mockitoCore.stub();
        
        // Verify that null is returned when there is no ongoing stubbing
        assertNull(result);
        verify(mockingProgress).pullOngoingStubbing();
        verify(mockingProgress).reset();
        verify(reporter).missingMethodInvocation();
    }
    
    private static class TestClass {}
}