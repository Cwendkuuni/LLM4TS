package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.InvocationOnMock;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.Invocation;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Arrays;

import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class ReturnsDeepStubsTest {

    private InternalMockHandler<Object> handler;
    private InvocationContainerImpl container;
    private Object mock;
    private Object deepStub;
    private Object nestedMock;

    @BeforeEach
    public void setup() {
        handler = new MockUtil().getMockHandler(mock(Object.class));
        container = (InvocationContainerImpl) handler.getInvocationContainer();
    }

    @Test
    public void testAnswerMethodWithNonMockableType() {
        // Given
        Type nonMockableType = Integer.class;
        when(handler.getMockSettings()).thenReturn(new CreationSettings(nonMockableType));

        // When
        Object returnedObject = new ReturnsDeepStubs().answer(ArgumentMatchers.any(InvocationOnMock.class));

        // Then
        Assertions.assertSame(new ReturnsEmptyValues().delegate.returnValueFor(nonMockableType), returnedObject);
    }

    @Test
    public void testAnswerMethodWithMockableType() {
        // Given
        Type mockableType = Map.class;
        when(handler.getMockSettings()).thenReturn(new CreationSettings(mockableType));
        mock = mock(Map.class, new ReturnsDeepStubs());

        // When
        Object returnedObject = new ReturnsDeepStubs().answer(ArgumentMatchers.any(InvocationOnMock.class));

        // Then
        Assertions.assertSame(mock, returnedObject);
    }

    @Test
    public void testGetMockMethodWithNoMatchingStubbedInvocation() {
        // Given
        Invocation invocation = Mockito.mock(Invocation.class);

        // When
        Object returnedObject = new ReturnsDeepStubs().getMock(ArgumentMatchers.any(InvocationOnMock.class));

        // Then
        Assertions.assertNull(returnedObject);
    }

    @Test
    public void testGetMockMethodWithMatchingStubbedInvocation() {
        // Given
        Invocation invocation = Mockito.mock(Invocation.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = new StubbedInvocationMatcher(ArgumentMatchers.any());
        container.addAnswer(stubbedInvocationMatcher, true);

        // When
        Object returnedObject = new ReturnsDeepStubs().getMock(ArgumentMatchers.any(InvocationOnMock.class));

        // Then
        Assertions.assertSame(stubbedInvocationMatcher.answer(ArgumentMatchers.any(InvocationOnMock.class)), returnedObject);
    }

    @Test
    public void testRecordDeepStubMockMethod() {
        // Given
        Invocation invocation = Mockito.mock(Invocation.class);
        Class<?> clz = Arrays.asList(Integer.class, Map.class).get(0);

        // When
        deepStub = new ReturnsDeepStubs().recordDeepStubMock(ArgumentMatchers.any(InvocationOnMock.class), container);

        // Then
        Mockito.verify(container).addAnswer(ArgumentMatchers.isInstanceOf(Answer.class), false);
        Assertions.assertSame(mock(clz, new ReturnsDeepStubs()), deepStub);
    }

    @Test
    public void testRecordDeepStubMockMethodWithNestedGenericType() {
        // Given
        Invocation invocation = Mockito.mock(Invocation.class);
        Class<?> clz = Arrays.asList(Map.class, GenericsNest.class).get(1);

        // When
        nestedMock = new ReturnsDeepStubs().recordDeepStubMock(ArgumentMatchers.any(InvocationOnMock.class), container);

        // Then
        Mockito.verify(container).addAnswer(ArgumentMatchers.isInstanceOf(Answer.class), false);
        Assertions.assertSame(mock(clz), nestedMock);
    }

    @Test
    public void testGetMockMethodWithMatchingDeepStub() {
        // Given
        Invocation invocation = Mockito.mock(Invocation.class);
        deepStub = new ReturnsDeepStubs().recordDeepStubMock(invocation, container);

        // When
        Object returnedObject = new ReturnsDeepStubs().getMock(ArgumentMatchers.any(InvocationOnMock.class));

        // Then
        Assertions.assertSame(deepStub, returnedObject);
    }

    @Test
    public void testGetMockMethodWithMatchingDeepStubAndVerification() {
        // Given
        Invocation invocation = Mockito.mock(Invocation.class);
        deepStub = new ReturnsDeepStubs().recordDeepStubMock(invocation, container);

        // When
        Object returnedObject1 = new ReturnsDeepStubs().getMock(ArgumentMatchers.any(InvocationOnMock.class));
        Object returnedObject2 = new ReturnsDeepStubs().getMock(ArgumentMatchers.any(InvocationOnMock.class));

        // Then
        Assertions.assertSame(deepStub, returnedObject1);
        Assertions.assertSame(deepStub, returnedObject2);
        verify(deepStub).toString();
        verifyZeroInteractions(new ReturnsEmptyValues().delegate);
    }
}