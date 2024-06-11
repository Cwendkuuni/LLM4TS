/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

public class ReturnsDeepStubsTest {
    
    @Test
    public void testAnswer() {
        ReturnsDeepStubs stubs = new ReturnsDeepStubs();
        
        // Test mock
        assertEquals("mocked", stubs.answer(mock(InvocationOnMock.class)));
    }
    
    @Test
    public void testDeepStub() {
        ReturnsDeepStubs stubs = new ReturnsDeepStubs();
        
        // Test deepStub
        assertEquals("deepStub", stubs.deepStub(mock(InvocationOnMock.class)));
    }
    
    @Test
    public void testNewDeepStubMock() {
        ReturnsDeepStubs stubs = new ReturnsDeepStubs();
        
        // Test newDeepStubMock
        assertEquals("newDeepStubMock", stubs.newDeepStubMock(mock(GenericMetadataSupport.class)));
    }
    
    @Test
    public void testWithSettingsUsing() {
        ReturnsDeepStubs stubs = new ReturnsDeepStubs();
        
        // Test withSettingsUsing
        assertEquals("withSettingsUsing", stubs.withSettingsUsing(mock(GenericMetadataSupport.class)));
    }
    
    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs stubs = new ReturnsDeepStubs();
        
        // Test returnsDeepStubsAnswerUsing
        assertEquals("returnsDeepStubsAnswerUsing", stubs.returnsDeepStubsAnswerUsing(mock(GenericMetadataSupport.class)));
    }
    
    @Test
    public void testRecordDeepStubAnswer() {
        ReturnsDeepStubs stubs = new ReturnsDeepStubs();
        
        // Test recordDeepStubAnswer
        assertEquals("recordDeepStubAnswer", stubs.recordDeepStubAnswer(mock(Object.class), mock(InvocationContainerImpl.class)));
    }
    
    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs stubs = new ReturnsDeepStubs();
        
        // Test actualParameterizedType
        assertEquals("actualParameterizedType", stubs.actualParameterizedType(mock(Object.class)));
    }
    
    @Test
    public void testWritesReplace() {
        ReturnsDeepStubs stubs = new ReturnsDeepStubs();
        
        // Test writeReplace
        assertEquals("writeReplace", stubs.writeReplace());
    }
    
    @Test
    public void testDeeplyStubbedAnswer() {
        ReturnsDeepStubs stubs = new ReturnsDeepStubs();
        
        // Test DeeplyStubbedAnswer
        assertEquals("deeplyStubbedAnswer", stubs.deeplyStubbedAnswer(mock(Object.class)));
    }
    
    @Test
    public void testMockitoCore() {
        MockitoCore core = mock(MockitoCore.class);
        MockitoCore originalCore = ReturnsDeepStubs.getMockitoCore();
        
        try {
            ReturnsDeepStubs.setMockitoCore(core);
            assertEquals(core, ReturnsDeepStubs.getMockitoCore());
        } finally {
            ReturnsDeepStubs.setMockitoCore(originalCore);
        }
    }
    
    @Test
    public void testDelegate() {
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
        ReturnsEmptyValues originalDelegate = ReturnsDeepStubs.getDelegate();
        
        try {
            ReturnsDeepStubs.setDelegate(delegate);
            assertEquals(delegate, ReturnsDeepStubs.getDelegate());
        } finally {
            ReturnsDeepStubs.setDelegate(originalDelegate);
        }
    }
}