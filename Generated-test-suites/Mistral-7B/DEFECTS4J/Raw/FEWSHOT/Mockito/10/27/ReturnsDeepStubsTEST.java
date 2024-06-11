package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Answers;
import org.mockito.InternalMockHandler;
import org.mockito.MockSettings;
import org.mockito.MockitoCore;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerWithSameInstance() {
        MockSettings mockSettings = withSettings().serializable();
        Object mock = mock(Map.class, mockSettings);
        when(mock.entrySet().iterator().next()).thenReturn(Collections.singletonMap("key", 1));

        MockSettings mockSettings2 = withSettings().serializable();
        Object mock2 = mock(Map.class, mockSettings2);
        assertSame(mock, mock2);
    }

    @Test
    public void testAnswerWithNewInstance() {
        MockSettings mockSettings = withSettings().serializable();
        Object mock = mock(Map.class, mockSettings);
        when(mock.entrySet().iterator().next()).thenReturn(new HashMap<>());

        MockSettings mockSettings2 = withSettings().serializable();
        Object mock2 = mock(Map.class, mockSettings2);
        assertSame(mock.getClass(), mock2.getClass());
        assertNotSame(mock, mock2);
    }

    @Test
    public void testDeepStubbing() {
        MockSettings mockSettings = withSettings().serializable();
        Object mock = mock(Map.class, mockSettings);
        when(mock.entrySet().iterator().next()).thenReturn(Collections.singletonMap("key", new ReturnsDeepStubs()));

        Object deepStub = mock.entrySet().iterator().next().getValue();
        assertSame(mock.getClass(), deepStub.getClass());
        MockSettings deepStubSettings = new MockSettings().serializable();
        Object deepStubMock = mock(deepStub.getClass(), deepStubSettings);
        verify(deepStubMock).answer(any(InvocationOnMock.class));
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        MockSettings mockSettings = withSettings().serializable();
        Object mock = mock(Map.class, mockSettings);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
        InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();

        Object deepStub = new ReturnsDeepStubs().answer(invocation);
        container.addAnswer(new DeeplyStubbedAnswer(deepStub), false);

        InvocationOnMock stubbedInvocation = mock(InvocationOnMock.class);
        when(mock.entrySet().iterator().next().getValue()).thenAnswer(new ReturnsDeepStubs());
        Object stubbedAnswer = mock.entrySet().iterator().next().getValue();

        verify(deepStub).answer(stubbedInvocation);
        verify(stubbedAnswer).answer(invocation);
    }

    private static class DeeplyStubbedAnswer implements Answer<Object> {
        private final Object mock;

        DeeplyStubbedAnswer(Object mock) {
            this.mock = mock;
        }

        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
            return mock;
        }
    }
}