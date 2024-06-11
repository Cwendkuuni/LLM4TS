import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.internal.stubbing.answers.ReturnsDeepStubs;
import org.mockito.internal.stubbing.answers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.internal.verification.VerificationModeFailedException;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;
    private ReturnsEmptyValues returnsEmptyValues;
    private InternalMockHandler<Object> mockHandler;
    private InvocationContainerImpl invocationContainer;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
        returnsEmptyValues = new ReturnsEmptyValues();
        mockHandler = mock(InternalMockHandler.class);
        invocationContainer = mock(InvocationContainerImpl.class);
    }

    @Test
    public void testAnswer_NonMockableType() {
        // Given
        Class<Integer> integerClass = Integer.class;
        doReturn(returnsEmptyValues).when(mockHandler).getInvocationContainer();
        when(mockHandler.getMockHandler(any(Object.class))).thenReturn(mockHandler);

        // When
        Object result = returnsDeepStubs.answer(mock(InvocationOnMock.class).withCallRealMethod().withArguments(any()));

        // Then
        assertEquals(returnsEmptyValues, result);
    }

    @Test
    public void testAnswer_MockableType() {
        // Given
        Class<Map<String, Number>> mapClass = Map.class;
        given(mockHandler.getInvocationContainer()).willReturn(invocationContainer);
        when(invocationContainer.getInvocationForStubbing()).thenReturn(mock(InvocationOnMock.class));
        when(invocationContainer.getStubbedInvocations()).thenReturn(Collections.emptyList());

        Object mock = mock(mapClass, returnsDeepStubs);
        InvocationOnMock invocation = mock(InvocationOnMock.class).withCallRealMethod().withArguments(mock);

        // When
        Object result = returnsDeepStubs.answer(invocation);

        // Then
        verify(invocationContainer).addAnswer(argThat(answers -> answers instanceof Answer), isA(Answer.class));
        assertEquals(mock, result);
    }

    @Test(expected = VerificationException.class)
    public void testAnswer_DoubleInvocation_VerificationFails() {
        // Given
        Class<Map<String, Number>> mapClass = Map.class;
        given(mockHandler.getInvocationContainer()).willReturn(invocationContainer);
        when(invocationContainer.getInvocationForStubbing()).thenReturn(mock(InvocationOnMock.class));
        when(invocationContainer.getStubbedInvocations()).thenReturn(Collections.singletonList(any()));

        Object mock = mock(mapClass, returnsDeepStubs);
        InvocationOnMock invocation1 = mock(InvocationOnMock.class).withCallRealMethod().withArguments(mock);
        InvocationOnMock invocation2 = mock(InvocationOnMock.class).withCallRealMethod().withArguments(mock);

        // When
        Object result1 = returnsDeepStubs.answer(invocation1);
        Object result2 = returnsDeepStubs.answer(invocation2);

        // Then
        verify(invocationContainer).addAnswer(argThat(answers -> answers instanceof Answer), isA(Answer.class));
        verify(invocationContainer).addAnswer(argThat(answers -> answers instanceof Answer), isA(Answer.class));
        verify(mock).answer(invocation1);
        verify(mock).answer(invocation2);
        throw new VerificationException("Expected exactly once: mock.answer");
    }

    @Test(expected = Throwable.class)
    public void testAnswer_Throwable() {
        // Given
        Class<Number> numberClass = Number.class;
        doThrow(new RuntimeException("Test exception")).when(mockHandler).getInvocationContainer();
        when(mockHandler.getMockHandler(any(Object.class))).thenReturn(mockHandler);

        // When
        try {
            Object result = returnsDeepStubs.answer(mock(InvocationOnMock.class).withCallRealMethod().withArguments(any()));
        } catch (Throwable e) {
            // Then
            assertEquals("Test exception", e.getMessage());
            throw e;
        }
    }
}