package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsDeepStubsTest {

    @Test
    public void testGetMockWithGenericReturnType() {
        interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
        }

        GenericsNest<Object> mock = mock(GenericsNest.class, new ReturnsDeepStubs(), Answers.RETURNS_DEEP_STUBS);

        Type[] genericInterfaces = new Type[] {Collection.class, Map.class, GenericsNest.class};
        CreationSettings creationSettings = new CreationSettings(Mockito.class.getClassLoader(), null, genericInterfaces, null);

        GenericMetadataSupport genericMetadata = new GenericMetadataSupport(GenericsNest.class, creationSettings);
        Class<?> rawType = genericMetadata.rawType();
        Assert.assertNotNull(rawType);
        Assert.assertEquals(GenericsNest.class, rawType);

        InvocationOnMock invocation = mockMethodCall(mock, "entrySet");
        Object deepStub = getMock(invocation, genericMetadata);
        Assert.assertNotNull(deepStub);
        Assert.assertSame(mock, deepStub);
    }

    @Test
    public void testGetMockWithNonGenericReturnType() {
        interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
            Number getNumber();
        }

        GenericsNest<Object> mock = mock(GenericsNest.class, new ReturnsDeepStubs(), Answers.RETURNS_DEEP_STUBS);

        Type numberType = Number.class;
        Type[] genericInterfaces = new Type[] {Collection.class, Map.class, GenericsNest.class};
        CreationSettings creationSettings = new CreationSettings(Mockito.class.getClassLoader(), null, genericInterfaces, numberType);

        GenericMetadataSupport genericMetadata = new GenericMetadataSupport(GenericsNest.class, creationSettings);
        Class<?> rawType = genericMetadata.rawType();
        Assert.assertNotNull(rawType);
        Assert.assertEquals(Number.class, rawType);

        InvocationOnMock invocation = mockMethodCall(mock, "getNumber");
        Object deepStub = getMock(invocation, genericMetadata);
        Assert.assertNotNull(deepStub);
        Assert.assertNotNull(mockitoCore().getMockHandler(deepStub));
    }

    @Test
    public void testRecordDeepStubMock() {
        interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
            Number getNumber();
        }

        GenericsNest<Object> mock = mock(GenericsNest.class, new ReturnsDeepStubs(), Answers.RETURNS_DEEP_STUBS);

        Type numberType = Number.class;
        Type[] genericInterfaces = new Type[] {Collection.class, Map.class, GenericsNest.class};
        CreationSettings creationSettings = new CreationSettings(Mockito.class.getClassLoader(), null, genericInterfaces, numberType);

        GenericMetadataSupport genericMetadata = new GenericMetadataSupport(GenericsNest.class, creationSettings);
        Class<?> rawType = genericMetadata.rawType();
        Assert.assertNotNull(rawType);
        Assert.assertEquals(Number.class, rawType);

        InvocationContainerImpl container = mockitoCore().getMockHandler(mock).getMockHandler();

        Object deepStub = createNewDeepStubMock(container, genericMetadata, genericMetadata.rawType());
        container.addAnswer(answersDeepStub(), false);

        InvocationOnMock invocation = mockMethodCall(mock, "getNumber");
        Object result = getMock(invocation, genericMetadata).getNumber();

        Assert.assertSame(deepStub, result);
    }

    @Test
    public void testGetMockWithMatchingStubbedInvocation() {
        interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
            Number getNumber();
        }

        GenericsNest<Object> mock = mock(GenericsNest.class, new ReturnsDeepStubs(), Answers.RETURNS_DEEP_STUBS);

        Type numberType = Number.class;
        Type[] genericInterfaces = new Type[] {Collection.class, Map.class, GenericsNest.class};
        CreationSettings creationSettings = new CreationSettings(Mockito.class.getClassLoader(), null, genericInterfaces, numberType);

        GenericMetadataSupport genericMetadata = new GenericMetadataSupport(GenericsNest.class, creationSettings);
        Class<?> rawType = genericMetadata.rawType();
        Assert.assertNotNull(rawType);
        Assert.assertEquals(Number.class, rawType);

        InvocationContainerImpl container = mockitoCore().getMockHandler(mock).getMockHandler();

        Object deepStub = createNewDeepStubMock(container, genericMetadata, genericMetadata.rawType());
        container.addAnswer(answersDeepStub(), false);

        StubbedInvocationMatcher stubbedInvocationMatcher = createStubbedInvocationMatcher(container.getInvocationForStubbing(), ArgumentMatchers.anyInt());

        InvocationOnMock invocation = mockMethodCall(mock, "getNumber", 1);
        Object result = getMock(invocation, genericMetadata);

        Assert.assertSame(deepStub, result);

        verify(mock).getNumber(ArgumentMatchers.eq(1));
    }

    @Test
    public void testGetMockWithNonMatchingStubbedInvocation() {
        interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
            Number getNumber();
        }

        GenericsNest<Object> mock = mock(GenericsNest.class, new ReturnsDeepStubs(), Answers.RETURNS_DEEP_STUBS);

        Type numberType = Number.class;
        Type[] genericInterfaces = new Type[] {Collection.class, Map.class, GenericsNest.class};
        CreationSettings creationSettings = new CreationSettings(Mockito.class.getClassLoader(), null, genericInterfaces, numberType);

        GenericMetadataSupport genericMetadata = new GenericMetadataSupport(GenericsNest.class, creationSettings);
        Class<?> rawType = genericMetadata.rawType();
        Assert.assertNotNull(rawType);
        Assert.assertEquals(Number.class, rawType);

        InvocationContainerImpl container = mockitoCore().getMockHandler(mock).getMockHandler();

        Object deepStub = createNewDeepStubMock(container, genericMetadata, genericMetadata.rawType());
        container.addAnswer(answersDeepStub(), false);

        StubbedInvocationMatcher stubbedInvocationMatcher = createStubbedInvocationMatcher(container.getInvocationForStubbing(), ArgumentMatchers.anyInt());

        InvocationOnMock invocation = mockMethodCall(mock, "getNumber", 2);
        Object result = getMock(invocation, genericMetadata);

        Assert.assertNotNull(result);
        Assert.assertNotSame(deepStub, result);

        verifyNoMoreInteractions(mock);
    }

    private InvocationOnMock mockMethodCall(Object mock, Method method, Object... arguments) {
        try {
            return Mockito.methodCall(method, mock, arguments);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object getMock(InvocationOnMock invocation, GenericMetadataSupport genericMetadata) throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InternalMockHandler<Object> handler = new MockUtil().getMockHandler(invocation.getMock());
        InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();

        Object result = returnsDeepStubs.getMock(invocation, genericMetadata);

        if (result == null) {
            result = delegateAnswer().returnValueFor(genericMetadata.rawType());
        }

        return result;
    }

    private Object createNewDeepStubMock(InvocationContainerImpl container, GenericMetadataSupport genericMetadata, Class<?> type) throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs() {
            @Override
            protected GenericMetadataSupport actualParameterizedType(Object mock) {
                return genericMetadata;
            }
        };

        Object mock = mockitoCore().mock(type, withSettingsUsing(genericMetadata));
        container.addAnswer(returnsDeepStubs, false);

        return mock;
    }

    private MockSettings withSettingsUsing(GenericMetadataSupport genericMetadata) {
        MockSettings mockSettings = genericMetadata.rawExtraInterfaces().length > 0
                ? withSettings().extraInterfaces(genericMetadata.rawExtraInterfaces())
                : withSettings();

        return mockSettings.defaultAnswer(returnsDeepStubsAnswerUsing(genericMetadata));
    }

    private ReturnsDeepStubs returnsDeepStubsAnswerUsing(GenericMetadataSupport genericMetadata) {
        return new ReturnsDeepStubs() {
            @Override
            protected GenericMetadataSupport actualParameterizedType(Object mock) {
                return genericMetadata;
            }
        };
    }

    private Object answersDeepStub() {
        return new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return getMock(invocation, actualParameterizedType(invocation.getMock()));
            }
        };
    }

    private StubbedInvocationMatcher createStubbedInvocationMatcher(InvocationOnMock invocation, Object argument) {
        return new StubbedInvocationMatcher(invocation, new InvocationMatcher() {
            @Override
            public boolean matches(Object invocation) {
                InvocationOnMock invocationToMatch = (InvocationOnMock) invocation;
                return invocationToMatch.getMethod().equals(invocation.getMethod())
                        && Arrays.equals(invocationToMatch.getArguments(), invocation.getArguments())
                        && (argument == null ? invocationToMatch.getArguments()[0] == null : argument.equals(invocationToMatch.getArguments()[0]));
            }
        });
    }

    private Object delegateAnswer() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        return returnsEmptyValues;
    }

    private Object actualParameterizedType(Object mock) {
        CreationSettings mockSettings = (CreationSettings) new MockUtil().getMockHandler(mock).getMockSettings();
        return GenericMetadataSupport.inferFrom(mockSettings.getTypeToMock());
    }

    private MockitoCore mockitoCore() {
        return new MockitoCore();
    }
}