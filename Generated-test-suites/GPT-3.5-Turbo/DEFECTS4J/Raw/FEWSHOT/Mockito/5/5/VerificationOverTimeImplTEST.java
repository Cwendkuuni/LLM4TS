package org.mockito.internal.verification;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.internal.util.Timer;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.verification.VerificationMode;

public class VerificationOverTimeImplTest {
    
    @Test
    public void testVerify_ReturnOnSuccess_True() {
        // Setup
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate.toString()).thenReturn("Delegate Verification Mode");
        VerificationData data = mock(VerificationData.class);
        
        VerificationOverTimeImpl v = new VerificationOverTimeImpl(100, 1000, delegate, true);
        
        // Verify
        try {
            v.verify(data);
        } catch (MockitoAssertionError e) {
            // Verify Error Message
            assertEquals("Delegate Verification Mode does not succeed before the timeout", e.getMessage());
            
            // Verify Delegate Invocation
            verify(delegate, atLeastOnce()).verify(data);
        }
    }
    
    @Test
    public void testVerify_ReturnOnSuccess_False() {
        // Setup
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate.toString()).thenReturn("Delegate Verification Mode");
        VerificationData data = mock(VerificationData.class);
        
        VerificationOverTimeImpl v = new VerificationOverTimeImpl(100, 1000, delegate, false);
        
        // Verify
        try {
            v.verify(data);
        } catch (MockitoAssertionError e) {
            // Verify Error Message
            assertEquals("Delegate Verification Mode does not succeed before the timeout", e.getMessage());
            
            // Verify Delegate Invocation
            verify(delegate, atLeastOnce()).verify(data);
        }
    }
    
    @Test
    public void testVerify_DelegateSucceeds() {
        // Setup
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate.toString()).thenReturn("Delegate Verification Mode");
        VerificationData data = mock(VerificationData.class);
        
        VerificationOverTimeImpl v = new VerificationOverTimeImpl(100, 1000, delegate, true);
        
        // Action
        v.verify(data);
        
        // Verify Delegate Invocation
        verify(delegate, atLeastOnce()).verify(data);
    }
    
    @Test
    public void testVerify_DelegateFails_ReturnOnSuccess_True() {
        // Setup
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate.toString()).thenReturn("Delegate Verification Mode");
        doThrow(new MockitoAssertionError("Delegate Verification Mode fail")).when(delegate).verify(any());
        VerificationData data = mock(VerificationData.class);
        
        VerificationOverTimeImpl v = new VerificationOverTimeImpl(100, 1000, delegate, true);
        
        // Verify
        try {
            v.verify(data);
        } catch (MockitoAssertionError e) {
            // Verify Error Message
            assertEquals("Delegate Verification Mode fail", e.getMessage());
            
            // Verify Delegate Invocation
            verify(delegate, atLeastOnce()).verify(data);
        }
    }
    
    @Test
    public void testVerify_DelegateFails_ReturnOnSuccess_False() {
        // Setup
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate.toString()).thenReturn("Delegate Verification Mode");
        doThrow(new MockitoAssertionError("Delegate Verification Mode fail")).when(delegate).verify(any());
        VerificationData data = mock(VerificationData.class);
        
        VerificationOverTimeImpl v = new VerificationOverTimeImpl(100, 1000, delegate, false);
        
        // Verify
        try {
            v.verify(data);
        } catch (MockitoAssertionError e) {
            // Verify Error Message
            assertEquals("Delegate Verification Mode fail", e.getMessage());
            
            // Verify Delegate Invocation
            verify(delegate, atLeastOnce()).verify(data);
        }
    }
    
    @Test
    public void testVerify_DelegateNeverRecovers() {
        // Setup
        VerificationMode delegate = mock(VerificationMode.class);
        when(delegate.toString()).thenReturn("AtMost Verification Mode");
        doThrow(new MockitoAssertionError("AtMost Verification Mode fail")).when(delegate).verify(any());
        VerificationData data = mock(VerificationData.class);
        
        VerificationOverTimeImpl v = new VerificationOverTimeImpl(100, 1000, delegate, false);
        
        // Verify
        try {
            v.verify(data);
        } catch (MockitoAssertionError e) {
            // Verify Error Message
            assertEquals("AtMost Verification Mode fail", e.getMessage());
            
            // Verify Delegate Invocation
            verify(delegate, atLeastOnce()).verify(data);
        }
    }
    
    @Test
    public void testGetPollingPeriod() {
        // Setup
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        
        VerificationOverTimeImpl v = new VerificationOverTimeImpl(100, 1000, delegate, true);
        
        // Action
        long pollingPeriod = v.getPollingPeriod();
        
        // Verify
        assertEquals(100, pollingPeriod);
    }
    
    @Test
    public void testGetDuration() {
        // Setup
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        
        VerificationOverTimeImpl v = new VerificationOverTimeImpl(100, 1000, delegate, true);
        
        // Action
        long duration = v.getDuration();
        
        // Verify
        assertEquals(1000, duration);
    }
    
    @Test
    public void testGetDelegate() {
        // Setup
        VerificationMode delegate = mock(VerificationMode.class);
        VerificationData data = mock(VerificationData.class);
        
        VerificationOverTimeImpl v = new VerificationOverTimeImpl(100, 1000, delegate, true);
        
        // Action
        VerificationMode delegateResult = v.getDelegate();
        
        // Verify
        assertEquals(delegate, delegateResult);
    }
}