package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.Answers;
import org.mockito.InvocationOnMock;
import org.mockito.MockitoCore;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() {
        MockitoCore mockitoCore = MockitoCore.INSTANCE;
        Class<?> rawType = String.class;

        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(rawType);
        Object deepStub = new ReturnsDeepStubs().deepStub(mock(Object.class), returnTypeGenericMetadata);
        assertEquals(deepStub, deepStub); // should be equal to itself
        verify(deepStub).answer(null); // should call answer method

        assertEquals(deepStub, deepStub); // should be equal to itself after calling answer method

        // Test with nested generic types
        Class<?> listType = List.class;
        Class<?> stringType = String.class;
        GenericMetadataSupport listMetadata = new GenericMetadataSupport(listType).withTypeArgument(stringType);
        Object listStub = new ReturnsDeepStubs().deepStub(mock(Object.class), listMetadata);
        List<String> list = (List<String>) listStub;
        list.add("foo");
        assertEquals(list, list); // should be equal to itself
        verify(list).add("foo"); // should call add method

        assertEquals(list, list); // should be equal to itself after calling add method
    }

    @Test
    public void testAnswerWithMultipleInvocations() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation1 = mockitoCore().createMockInvocation(mock, "method1");
        Object answer1 = new ReturnsDeepStubs().answer(invocation1);
        InvocationOnMock invocation2 = mockitoCore().createMockInvocation(mock, "method1");
        Object answer2 = new ReturnsDeepStubs().answer(invocation2);

        assertEquals(answer1, answer2); // should be equal since both invocations match the same deep stub
    }

    @Test
    public void testAnswerWithDifferentInvocations() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation1 = mockitoCore().createMockInvocation(mock, "method1");
        Object answer1 = new ReturnsDeepStubs().answer(invocation1);
        InvocationOnMock invocation2 = mockitoCore().createMockInvocation(mock, "method2");
        Object answer2 = new ReturnsDeepStubs().answer(invocation2);

        assertNotSame(answer1, answer2); // should not be equal since both invocations match different mocks
    }

    @Test
    public void testNewDeepStubMock() {
        Class<?> stringListType = List.class.getTypeParameters()[0].getType();
        Class<?> stringType = String.class;
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(List.class).withTypeArgument(stringType);
        Object deepStubMock = new ReturnsDeepStubs().newDeepStubMock(returnTypeGenericMetadata);

        assertEquals(deepStubMock.getClass(), List.class);
        assertEquals(deepStubMock.getClass().getTypeParameters()[0].getType(), String.class);
    }

    @Test
    public void testRecordDeepStubAnswer() throws Exception {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = mockitoCore().createMockInvocation(mock, "method1");
        Answer<Object> answer = new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return "answer";
            }
        };

        InvocationContainerImpl container = new InvocationContainerImpl();
        container.addAnswer(answer, false);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object stubbedAnswer = returnsDeepStubs.recordDeepStubAnswer(mock, container);

        assertEquals("answer", stubbedAnswer);
        verify(mock).answer(invocation);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        Class<?> stringListType = List.class.getTypeParameters()[0].getType();
        Class<?> stringType = String.class;
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(List.class).withTypeArgument(stringType);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Answer<Object> answer = mock(Answer.class);

        Object deepStubAnswerUsing = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
        assertEquals(answer.getClass(), deepStubAnswerUsing.getClass());
    }

    @Test
    public void testWithSettingsUsing() {
        Class<?> stringListType = List.class.getTypeParameters()[0].getType();
        Class<?> stringType = String.class;
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(List.class).withTypeArgument(stringType);

        MockSettings mockSettings = new ReturnsDeepStubs().withSettingsUsing(returnTypeGenericMetadata);
        assertEquals(mockSettings.getDefaultAnswer(), ReturnsDeepStubs.class);
        assertEquals(mockSettings.getExtraInterfaces(), Collections.emptyList());
        assertEquals(mockSettings.isSerializable(), true);
    }
}