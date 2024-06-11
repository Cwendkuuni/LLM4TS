package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsDeepStubsTest {
    private MockitoCore mockitoCore;
    private ReturnsDeepStubs returnsDeepStubs;
    private Object mockObject;

    @Before
    public void setup() {
        mockitoCore = new MockitoCore();
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer() throws Exception {
        Class<?> clazz = Mockito.class;
        Object mock = mock(clazz, new ReturnsDeepStubs());

        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(mock.answer(ArgumentMatchers.eq(invocation))).thenReturn("return value");

        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        Class<?> rawType = returnTypeGenericMetadata.rawType();

        Object deepStub = returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);
        Object answer = returnsDeepStubs.answer(invocation);

        assertNotNull(answer);
        assertEquals(deepStub, answer);

        verify(mock).answer(invocation);
    }

    @Test
    public void testCreateNewDeepStubMock() throws Exception {
        Class<?> clazz = Map.class;
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        returnTypeGenericMetadata.addRawType(clazz);

        Object deepStub = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);

        assertNotNull(deepStub);
        assertEquals(clazz, deepStub.getClass());
    }

    @Test
    public void testWithSettingsUsing() throws Exception {
        Class<?> clazz = Map.class;
        Class<?> setClass = Number.class;

        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        returnTypeGenericMetadata.addRawType(clazz);
        returnTypeGenericMetadata.addRawExtraInterface(Set.class);

        MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        Class<?>[] extraInterfaces = mockSettings.extraInterfaces();
        assertArrayEquals(new Class<?>[]{Set.class}, extraInterfaces);
    }

    @Test
    public void testRecordDeepStubMock() throws Exception {
        Class<?> clazz = Map.class;

        Object mock = mock(clazz, new ReturnsDeepStubs());
        InvocationContainerImpl invocationContainer = Mockito.mock(InvocationContainerImpl.class);

        when(returnsDeepStubs.recordDeepStubMock(mock, ArgumentMatchers.eq(invocationContainer))).thenReturn(mock);

        Object deepStub = returnsDeepStubs.getMock(Mockito.mock(InvocationOnMock.class), Mockito.mock(GenericMetadataSupport.class));
        StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);

        invocationContainer.addAnswer(stubbedInvocationMatcher);
        stubbedInvocationMatcher.answer(Mockito.mock(InvocationOnMock.class));

        Object answer = returnsDeepStubs.answer(Mockito.mock(InvocationOnMock.class));

        assertNotNull(answer);
        assertEquals(deepStub, answer);

        verify(stubbedInvocationMatcher).answer(Mockito.mock(InvocationOnMock.class));
    }

    @Test
    public void testAnswerWithNestedGenerics() throws Exception {
        Class<?> clazz = Map.class;
        Class<?> keyClass = String.class;
        Class<?> valueClass = Number.class;

        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        returnTypeGenericMetadata.addRawType(clazz);
        returnTypeGenericMetadata.addTypeArgument(0, keyClass);
        returnTypeGenericMetadata.addTypeArgument(1, valueClass);

        Object deepStub = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);

        when(mockObject = mock(clazz, new ReturnsDeepStubs())).thenReturn(deepStub);

        Map<String, Number> map = (Map<String, Number>) mockObject;
        Set<Number> numbers = map.entrySet().iterator().next().getValue();
        Number number = numbers.iterator().next();

        assertEquals("expected number", number);
    }
}