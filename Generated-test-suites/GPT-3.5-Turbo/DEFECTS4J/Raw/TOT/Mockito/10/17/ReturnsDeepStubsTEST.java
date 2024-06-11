import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsDeepStubsTest {

    @Mock
    private ReturnsDeepStubs returnsDeepStubs;

    @Test
    public void testAnswer_ReturnsDeepStubs_InvokesReturnValueMatchingInvocation() {
        List<String> list1 = mock(List.class, ReturnsDeepStubs.class);
        List<String> list2 = list1.get(0);

        assertTrue(list2 instanceof List);
        assertTrue(list2 instanceof RandomAccess);
        assertEquals("test", list2.get(0));
    }

    @Test
    public void testAnswer_ReturnsDeepStubs_InvokesReturnValueNotMatchingInvocation() {
        List<String> list1 = mock(List.class, ReturnsDeepStubs.class);
        List<String> list2 = list1.get(1);

        assertNull(list2);
    }

    @Test
    public void testAnswer_ReturnsDeepStubs_MockTypeNotMockable() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn("test");

        Object result = null;
        try {
            result = returnsDeepStubs.answer(invocation);
        } catch (Throwable throwable) {
            fail("Exception should not be thrown");
        }

        assertEquals("test", result);
    }

    @Test
    public void testDeepStub_InvocationMatchesVerification() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(invocation.getMock()).thenReturn(handler);

        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocationMatcher.getInvocation()).thenReturn(invocation);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        Answer<Object> answer = returnsDeepStubs::deepStub;

        Object result = null;
        try {
            result = answer.answer(invocation);
        } catch (Throwable throwable) {
            fail("Exception should not be thrown");
        }

        assertEquals(invocation.getMock(), result);
    }

    @Test
    public void testDeepStub_InvocationDoesNotMatchVerification() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(invocation.getMock()).thenReturn(handler);

        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocationMatcher.getInvocation()).thenReturn(mock(InvocationOnMock.class));
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        Answer<Object> answer = returnsDeepStubs::deepStub;

        Object result = answer.answer(invocation);

        assertNull(result);
    }

    @Test
    public void testNewDeepStubMock_WithRawExtraInterfaces() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(List.class);
        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[]{Set.class});
        Answer<Object> answer = returnsDeepStubs::newDeepStubMock;

        Object result = answer.answer(returnTypeGenericMetadata);

        assertNotNull(result);
        assertTrue(result instanceof List);
        assertTrue(result instanceof Set);
    }

    @Test
    public void testNewDeepStubMock_WithoutRawExtraInterfaces() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(List.class);
        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(false);
        Answer<Object> answer = returnsDeepStubs::newDeepStubMock;

        Object result = answer.answer(returnTypeGenericMetadata);

        assertNotNull(result);
        assertTrue(result instanceof List);
        assertFalse(result instanceof Set);
    }

    @Test
    public void testWithSettingsUsing_WithRawExtraInterfaces() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[]{Set.class});
        Answer<Object> answer = returnsDeepStubs::withSettingsUsing;

        Object result = null;
        try {
            result = answer.answer(returnTypeGenericMetadata);
        } catch (Throwable throwable) {
            fail("Exception should not be thrown");
        }

        assertNotNull(result);
        assertTrue(result instanceof MockSettings);
        assertTrue(((MockSettings) result).getExtraInterfaces()[0] == Set.class);
        assertTrue(((MockSettings) result).getDefaultAnswer() instanceof ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback);
    }

    @Test
    public void testWithSettingsUsing_WithoutRawExtraInterfaces() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(false);
        Answer<Object> answer = returnsDeepStubs::withSettingsUsing;

        Object result = null;
        try {
            result = answer.answer(returnTypeGenericMetadata);
        } catch (Throwable throwable) {
            fail("Exception should not be thrown");
        }

        assertNotNull(result);
        assertTrue(result instanceof MockSettings);
        assertNull(((MockSettings) result).getExtraInterfaces());
        assertTrue(((MockSettings) result).getDefaultAnswer() instanceof ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback fallback = mock(ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback.class);
        when(fallback.returnTypeGenericMetadata).thenReturn(returnTypeGenericMetadata);
        Answer<Object> answer = returnsDeepStubs::returnsDeepStubsAnswerUsing;

        Object result = null;
        try {
            result = answer.answer(fallback);
        } catch (Throwable throwable) {
            fail("Exception should not be thrown");
        }

        assertNotNull(result);
        assertTrue(result instanceof ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback);
        assertEquals(returnTypeGenericMetadata, ((ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback) result).returnTypeGenericMetadata);
    }

    @Test
    public void testRecordDeepStubAnswer() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Answer<Object> answer = returnsDeepStubs::recordDeepStubAnswer;

        Object result = null;
        try {
            result = answer.answer(mock, container);
        } catch (Throwable throwable) {
            fail("Exception should not be thrown");
        }

        assertEquals(mock, result);
        verify(container, times(1)).addAnswer(any(), eq(false));
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(List.class);
        Answer<Object> answer = returnsDeepStubs::actualParameterizedType;

        Object result = null;
        try {
            result = answer.answer(mock);
        } catch (Throwable throwable) {
            fail("Exception should not be thrown");
        }

        assertNotNull(result);
        assertTrue(result instanceof GenericMetadataSupport);
    }

    // Additional test cases for other methods and private inner classes...

}