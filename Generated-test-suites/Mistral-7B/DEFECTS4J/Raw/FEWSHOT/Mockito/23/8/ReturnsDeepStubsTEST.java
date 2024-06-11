package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Answers;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.ReturnsDeepStubs;
import org.mockito.internal.stubbing.DeepStubbingPerformer;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.AnswerWithContext;
import org.mockito.internal.reflection.GenericMetadataSupport;
import org.mockito.InvocationType;
import org.mockito.internal.reflection.MockType;
import org.mockito.internal.reflection.MockTypeFactory;
import org.mockito.internal.util.reflection.JavaReflectionHelper;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerWithContext() {
        MockSettings mockSettings = createMockSettings(Map.class, List.class);
        Object mock = mock(Map.class, withSettings(mockSettings));

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        ReturnsDeepStubs answer = returnsDeepStubs.returnsDeepStubsAnswerUsing(mockSettings);

        answer.answer(ArgumentMatchers.any(Map.class));
        assertNotNull(answer);
    }

    @Test
    public void testAnswerWithNoExtraInterfaces() {
        MockSettings mockSettings = createMockSettings(Map.class);
        Object mock = mock(Map.class, withSettings(mockSettings));

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object answer = returnsDeepStubs.answer(ArgumentMatchers.any(Map.class), ArgumentMatchers.any(InvocationOnMock.class));
        assertNotNull(answer);
        verify(mock).answer(ArgumentMatchers.any(Map.class), ArgumentMatchers.eq(DeepStubbingPerformer.DEEP_STUBBING_PERFORMER));
    }

    @Test
    public void testAnswerWithExtraInterfaces() {
        MockSettings mockSettings = createMockSettings(Map.class, List.class);
        Object mock = mock(Map.class, withSettings(mockSettings));

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object answer = returnsDeepStubs.answer(ArgumentMatchers.any(Map.class), ArgumentMatchers.any(InvocationOnMock.class));
        assertNotNull(answer);
        verify(mock).answer(ArgumentMatchers.any(Map.class), ArgumentMatchers.eq(DeepStubbingPerformer.DEEP_STUBBING_PERFORMER));
        verify(mock).answer(ArgumentMatchers.any(List.class), ArgumentMatchers.eq(DeepStubbingPerformer.DEEP_STUBBING_PERFORMER));
    }

    @Test
    public void testAnswerWithGenericReturnType() {
        MockSettings mockSettings = createMockSettings(Map.class, List.class);
        Object mapMock = mock(Map.class, withSettings(mockSettings));
        Object listMock = mock(List.class, withSettings(mockSettings));

        when(mapMock.entrySet()).thenReturn(listMock);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Map<?, Set<Number>> map = returnsDeepStubs.answer(ArgumentMatchers.any(Map.class), ArgumentMatchers.any(InvocationOnMock.class));
        assertNotNull(map);
        verify(mapMock).answer(ArgumentMatchers.any(Map.class), ArgumentMatchers.eq(DeepStubbingPerformer.DEEP_STUBBING_PERFORMER));
        verify(listMock).answer(ArgumentMatchers.any(List.class), ArgumentMatchers.eq(DeepStubbingPerformer.DEEP_STUBBING_PERFORMER));
    }

    @Test
    public void testAnswerWithGenericReturnTypeAndExtraInterfaces() {
        MockSettings mockSettings = createMockSettings(Map.class, List.class);
        Object mapMock = mock(Map.class, withSettings(mockSettings));
        Object listMock = mock(List.class, withSettings(mockSettings));

        when(mapMock.entrySet()).thenReturn(listMock);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Map<?, Set<Number>> map = returnsDeepStubs.answer(ArgumentMatchers.any(Map.class), ArgumentMatchers.any(InvocationOnMock.class));
        assertNotNull(map);
        verify(mapMock).answer(ArgumentMatchers.any(Map.class), ArgumentMatchers.eq(DeepStubbingPerformer.DEEP_STUBBING_PERFORMER));
        verify(listMock).answer(ArgumentMatchers.any(List.class), ArgumentMatchers.eq(DeepStubbingPerformer.DEEP_STUBBING_PERFORMER));
        verify(listMock).answer(ArgumentMatchers.any(Collection.class), ArgumentMatchers.eq(DeepStubbingPerformer.DEEP_STUBBING_PERFORMER));
    }

    @Test
    public void testGetMock() {
        MockSettings mockSettings = createMockSettings(Map.class, List.class);
        Object mapMock = mock(Map.class, withSettings(mockSettings));

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object result = returnsDeepStubs.getMock(ArgumentMatchers.any(InvocationOnMock.class), ArgumentMatchers.any(GenericMetadataSupport.class));
        assertNotNull(result);
        assertEquals(mapMock.getClass(), result.getClass());
    }

    @Test
    public void testRecordDeepStubMock() throws Exception {
        MockSettings mockSettings = createMockSettings(Map.class, List.class);
        Object mapMock = mock(Map.class, withSettings(mockSettings));
        Object listMock = mock(List.class, withSettings(mockSettings));

        InvocationOnMock invocationOnMock = mock(InvocationOnMock.class);
        InvocationContainerImpl invocationContainer = new InvocationContainerImpl(invocationOnMock, null);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object stub = returnsDeepStubs.recordDeepStubMock(mapMock, invocationContainer);
        assertNotNull(stub);
        assertEquals(mapMock.getClass(), stub.getClass());

        verify(mapMock).answer(invocationOnMock, ArgumentMatchers.eq(DeepStubbingPerformer.DEEP_STUBBING_PERFORMER));
    }

    private MockSettings createMockSettings(Class<?>... types) {
        String className = getClass().getName();
        Object[] extraInterfaces = types.length > 0 ? new Object[] { types } : null;
        return withSettings()
                .defaultAnswer(Answers.RETURNS_DEEP_STUBS)
                .extraInterfaces(extraInterfaces)
                .mockType(MockTypeFactory.createMockType(JavaReflectionHelper.getRawType(types[0]), className));
    }

    private MockSettings withSettings() {
        return new MockSettings();
    }
}