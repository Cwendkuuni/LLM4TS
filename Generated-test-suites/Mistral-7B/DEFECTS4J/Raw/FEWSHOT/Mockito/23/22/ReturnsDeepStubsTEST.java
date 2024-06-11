package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerWithDeepStubs() {
        Class<?> mockType = Mockito.mock(String.class, new ReturnsDeepStubs()).getClass();

        Object deepStub = mock(String.class).answer(new ReturnsDeepStubs());

        assertEquals(deepStub, mockType.answer(new InvocationOnMock(mockType, new Object[]{}, new Object[0], new Class[0], new Method[]{})));
    }

    @Test
    public void testAnswerWithEmptyValues() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Answer<Object> emptyValues = new ReturnsEmptyValues();

        Class<?> mockType = Mockito.mock(String.class, withSettings().defaultAnswer(emptyValues));

        assertEquals(emptyValues, mockType.answer(new ReturnsDeepStubs()));
    }

    @Test
    public void testGetMockWithSameInvocation() {
        Class<?> mockType = Mockito.mock(String.class, new ReturnsDeepStubs());
        InvocationContainerImpl container = (InvocationContainerImpl) Mockito.mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);

        when(container.getInvocationForStubbing()).thenReturn(stubbedInvocationMatcher.getInvocation());
        when(stubbedInvocationMatcher.answer(any(InvocationOnMock.class))).thenReturn("result");

        assertEquals("result", mockType.answer(new InvocationOnMock(mockType, new Object[]{}, new Object[0], new Class[0], new Method[]{})));
    }

    @Test
    public void testGetMockWithDifferentInvocations() {
        Class<?> mockType = Mockito.mock(String.class, new ReturnsDeepStubs());
        InvocationContainerImpl container1 = (InvocationContainerImpl) Mockito.mock(InvocationContainerImpl.class);
        InvocationContainerImpl container2 = (InvocationContainerImpl) Mockito.mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher1 = mock(StubbedInvocationMatcher.class);
        StubbedInvocationMatcher stubbedInvocationMatcher2 = mock(StubbedInvocationMatcher.class);

        when(container1.getInvocationForStubbing()).thenReturn(stubbedInvocationMatcher1.getInvocation());
        when(container2.getInvocationForStubbing()).thenReturn(stubbedInvocationMatcher2.getInvocation());
        when(stubbedInvocationMatcher1.answer(any(InvocationOnMock.class))).thenReturn("result1");
        when(stubbedInvocationMatcher2.answer(any(InvocationOnMock.class))).thenReturn("result2");

        assertNotSame("result1", mockType.answer(new InvocationOnMock(mockType, new Object[]{}, new Object[0], new Class[0], new Method[]{})));
        assertNotSame("result2", mockType.answer(new InvocationOnMock(mockType, new Object[]{}, new Object[0], new Class[0], new Method[]{})));
    }

    @Test
    public void testCreateNewDeepStubMock() {
        Class<?> mockType = Mockito.mock(String.class, new ReturnsDeepStubs());
        InternalMockHandler mockHandler = new MockUtil().getMockHandler(mockType);
        InvocationContainerImpl container = (InvocationContainerImpl) mockHandler.getInvocationContainer();

        MockSettings mockSettings = container.getMockSettings();
        Type returnType = mockSettings.getTypeToMock().getGenericSuperclass();
        Type[] genericInterfaces = ((ParameterizedType) returnType).getActualTypeArguments();

        Class<?> genericReturnType = (Class<?>) genericInterfaces[0];
        Object deepStub = mock(genericReturnType, withSettingsUsing(mockSettings));

        assertEquals(deepStub, mockType.answer(new InvocationOnMock(mockType, new Object[]{}, new Object[0], new Class[0], new Method[]{})));
    }

    @Test
    public void testWithSettingsUsing() {
        Class<?> mockType = Mockito.mock(String.class, new ReturnsDeepStubs());
        InternalMockHandler mockHandler = new MockUtil().getMockHandler(mockType);
        InvocationContainerImpl container = (InvocationContainerImpl) mockHandler.getInvocationContainer();

        MockSettings mockSettings = container.getMockSettings();
        Type returnType = mockSettings.getTypeToMock().getGenericSuperclass();
        Type[] genericInterfaces = ((ParameterizedType) returnType).getActualTypeArguments();

        Class<?> genericReturnType = (Class<?>) genericInterfaces[0];
        Object deepStub = mock(genericReturnType, withSettings().extraInterfaces(new Class[]{Collection.class}));

        assertNotSame(deepStub, mockType.answer(new InvocationOnMock(mockType, new Object[]{}, new Object[0], new Class[0], new Method[]{})));
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        Class<?> mockType = Mockito.mock(String.class, new ReturnsDeepStubs());
        InvocationContainerImpl container = (InvocationContainerImpl) Mockito.mock(InvocationContainerImpl.class);

        when(container.addAnswer(any(Answer.class), anyBoolean())).thenReturn(mockType);

        assertEquals(mockType, mockType.answer(new InvocationOnMock(mockType, new Object[]{}, new Object[0], new Class[0], new Method[]{})));
    }
}