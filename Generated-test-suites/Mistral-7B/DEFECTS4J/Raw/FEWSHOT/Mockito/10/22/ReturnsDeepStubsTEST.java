package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.withSettings;
import static org.mockito.internal.stubbing.answers.ReturnsDeepStubs.deepStub;
import static org.mockito.internal.stubbing.answers.ReturnsDeepStubs.newDeepStubMock;
import static org.mockito.internal.stubbing.answers.ReturnsDeepStubs.recordDeepStubAnswer;
import static org.mockito.internal.stubbing.answers.ReturnsDeepStubs.withSettingsUsing;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Answer;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReturnsDeepStubsTest {

    @Test
    public void testDeepStubs() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        when(mockitoCore.isTypeMockable(String.class)).thenReturn(true);

        Object mock = mock(String.class, withSettings().serializable().defaultAnswer(ReturnsDeepStubs.returnsDeepStubsAnswerUsing(GenericMetadataSupport.forClass(String.class))));

        InvocationContainerImpl container = spy(mockitoCore).getMockHandler(mock);

        String value = (String) deepStub(new InvocationOnMock(mock, new Invocation("toString()")));
        assertSame(mock, value);

        StubbedInvocationMatcher stubbedInvocationMatcher = new StubbedInvocationMatcher(new Invocation("toString()"), value);
        container.addAnswer(stubbedInvocationMatcher, false);

        String valueFromStub = (String) deepStub(new InvocationOnMock(mock, new Invocation("toString()")));
        assertSame(value, valueFromStub);

        Type type = new DeeplyStubbedAnswer(mock).getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        GenericDeclaration genericDeclaration = actualTypeArguments[0].getDeclaringClass();

        assertEquals(String.class, genericDeclaration);
    }

    @Test(expected = IllegalStateException.class)
    public void testDeepStubsWithNonMockableType() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        when(mockitoCore.isTypeMockable(Integer.class)).thenReturn(false);

        Object mock = mock(Integer.class, withSettings().serializable().defaultAnswer(ReturnsDeepStubs.returnsDeepStubsAnswerUsing(GenericMetadataSupport.forClass(Integer.class))));

        InvocationOnMock invocationOnMock = new InvocationOnMock(mock, new Invocation("toString()"));
        deepStub(invocationOnMock);
    }

    @Test
    public void testDeepStubsNestedGenerics() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        when(mockitoCore.isTypeMockable(Map.class)).thenReturn(true);
        when(mockitoCore.isTypeMockable(Number.class)).thenReturn(true);

        Object mockMap = mock(Map.class, withSettings().serializable().defaultAnswer(ReturnsDeepStubs.returnsDeepStubsAnswerUsing(GenericMetadataSupport.forClass(Map.class))));

        InvocationContainerImpl containerMap = spy(mockitoCore).getMockHandler(mockMap);

        Object mockSet = newDeepStubMock(GenericMetadataSupport.inferFrom(new CreationSettings().setTypeToMock(Set.class)));
        InvocationContainerImpl containerSet = spy(mockitoCore).getMockHandler(mockSet);

        Object mockNumber = newDeepStubMock(GenericMetadataSupport.inferFrom(new CreationSettings().setTypeToMock(Number.class)));

        InvocationOnMock invocationOnMap = new InvocationOnMock(mockMap, new Invocation("entrySet().iterator().next()"));
        Object deepStubMapEntry = deepStub(invocationOnMap);

        StubbedInvocationMatcher stubbedInvocationMatcherMapEntry = new StubbedInvocationMatcher(invocationOnMap, deepStubMapEntry);
        containerMap.addAnswer(stubbedInvocationMatcherMapEntry, false);

        InvocationOnMock invocationOnSet = new InvocationOnMock(deepStubMapEntry, new Invocation("iterator().next()"));
        Object deepStubNumber = deepStub(invocationOnSet);

        StubbedInvocationMatcher stubbedInvocationMatcherNumber = new StubbedInvocationMatcher(invocationOnSet, deepStubNumber);
        containerSet.addAnswer(stubbedInvocationMatcherNumber, false);

        Type type = new DeeplyStubbedAnswer(deepStubMapEntry).getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        GenericDeclaration genericDeclaration = actualTypeArguments[0].getDeclaringClass();

        assertEquals(Set.class, genericDeclaration);

        type = new DeeplyStubbedAnswer(deepStubNumber).getClass().getGenericSuperclass();
        parameterizedType = (ParameterizedType) type;
        genericDeclaration = actualTypeArguments[0].getDeclaringClass();

        assertEquals(Number.class, genericDeclaration);
    }
}