
/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.stubbing.defaultanswers;

import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;


/**
 * Returning deep stub implementation.
 *
 * Will return previously created mock if the invocation matches.
 *
 * <p>Supports nested generic information, with this answer you can write code like this :
 *
 * <pre class="code"><code class="java">
 *     interface GenericsNest&lt;K extends Comparable&lt;K&gt; & Cloneable&gt; extends Map&lt;K, Set&lt;Number&gt;&gt; {}
 *
 *     GenericsNest&lt;?&gt; mock = mock(GenericsNest.class, new ReturnsGenericDeepStubs());
 *     Number number = mock.entrySet().iterator().next().getValue().iterator().next();
 * </code></pre>
 * </p>
 *
 * @see org.mockito.Mockito#RETURNS_DEEP_STUBS
 * @see org.mockito.Answers#RETURNS_DEEP_STUBS
 */
public class ReturnsDeepStubsTest {
    
    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        answerMockCreation(returnsDeepStubs);
        answerMockCreationCastingToReturnType(returnsDeepStubs);
        answerMockInvocationOnRealObject(returnsDeepStubs);
        answerMockDeepStub(returnsDeepStubs);
    }
    
    private void answerMockCreation(ReturnsDeepStubs returnsDeepStubs) throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMock()).thenReturn(invocation.getMock());
        
        Object result = returnsDeepStubs.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof InvocationOnMock);
    }
    
    private void answerMockCreationCastingToReturnType(ReturnsDeepStubs returnsDeepStubs) throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMock()).thenReturn(invocation.getMock());
        Method method = InvocationOnMock.class.getMethod("getMock");
        
        InvocationOnMock result = (InvocationOnMock) returnsDeepStubs.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof InvocationOnMock);
        assertEquals(invocation.getMock(), result.getMock());
    }
    
    private void answerMockInvocationOnRealObject(ReturnsDeepStubs returnsDeepStubs) throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMock()).thenReturn(new Object());
        
        Object result = returnsDeepStubs.answer(invocation);
        
        assertNull(result);
    }
    
    private void answerMockDeepStub(ReturnsDeepStubs returnsDeepStubs) throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMock()).thenReturn(new Object());
        Method method = InvocationOnMock.class.getMethod("getMock");
        
        Object result = returnsDeepStubs.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof Object);
    }
    
}
