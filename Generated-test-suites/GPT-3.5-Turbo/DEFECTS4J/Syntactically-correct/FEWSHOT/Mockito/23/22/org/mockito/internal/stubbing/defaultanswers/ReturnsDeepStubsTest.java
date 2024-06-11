
package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        
        // Create mocks for the invocation container
        InvocationOnMock invocation1 = mock(InvocationOnMock.class);
        InvocationOnMock invocation2 = mock(InvocationOnMock.class);
        
        // Create generic metadata support
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        
        // Test when mockitoCore isTypeMockable method returns false
        when(mockitoCore.isTypeMockable(Object.class)).thenReturn(false);
        when(delegate.returnValueFor(Object.class)).thenReturn("MockObject");
        Object result1 = deepStubs.answer(invocation1);
        assertEquals("MockObject", result1);
        
        // Test when actualParameterizedType method returns genericMetadataSupport
        when(genericMetadataSupport.rawType()).thenReturn(Object.class);
        when(genericMetadataSupport.rawExtraInterfaces()).thenReturn(new Class<?>[]{});
        Object result2 = deepStubs.createNewDeepStubMock(genericMetadataSupport);
        assertNotNull(result2);
        
        // Test when actualParameterizedType method returns null
        when(mockitoCore.isTypeMockable(null)).thenReturn(true);
        Object result3 = deepStubs.recordDeepStubMock(result2, invocation2);
        assertNull(result3);
    }
}
