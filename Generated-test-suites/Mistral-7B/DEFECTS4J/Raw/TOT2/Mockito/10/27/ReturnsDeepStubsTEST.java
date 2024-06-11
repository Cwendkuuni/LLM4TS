import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerMethod() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = new ReturnsDeepStubs().answer(invocation);
        verify(mock).answer(invocation);
        assertNotNull(result);
    }

    @Test
    public void testNonMockableType() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Mockito.when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
        Mockito.when(mockitoCore().isTypeMockable(String.class)).thenReturn(false);
        Object result = new ReturnsDeepStubs().answer(invocation);
        Mockito.verifyNoMoreInteractions(mock);
        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testThrowableException() throws IOException {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Mockito.when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
        Mockito.when(mockitoCore().isTypeMockable(String.class)).thenReturn(true);
        Mockito.when(mockitoCore().mock(String.class, withSettings())).thenThrow(new IOException());
        Object result = new ReturnsDeepStubs().answer(invocation);
        // The test will fail with an IOException exception as expected
    }

    @Test
    public void testDeepStubMatch() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        Mockito.when(container.getInvocationForStubbing()).thenReturn(invocation);
        Mockito.when(stubbedInvocationMatcher.answer(invocation)).thenReturn(new Object());
        Mockito.when(container.getStubbedInvocations()).thenReturn(Arrays.asList(stubbedInvocationMatcher));
        Object result = new ReturnsDeepStubs().answer(invocation);
        assertEquals(new Object(), result);
    }

    @Test
    public void testNewDeepStubMock() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Object newMock = new ReturnsDeepStubs().newDeepStubMock(returnTypeGenericMetadata);
        assertNotNull(newMock);
        assertEquals(returnTypeGenericMetadata.rawType(), newMock.getClass());
    }

    @Test
    public void testWithSettingsUsing() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Mockito.when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);
        Mockito.when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[0]);
        Mockito.when(mockitoCore().mock(returnTypeGenericMetadata.rawType(), withSettings())).thenReturn(mock);
        Object newMock = new ReturnsDeepStubs().withSettingsUsing(returnTypeGenericMetadata);
        assertSame(mock, newMock);
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Object newMock = new ReturnsDeepStubs().newDeepStubMock(Mockito.mock(GenericMetadataSupport.class));
        Mockito.when(container.addAnswer(any(Answer.class), anyBoolean())).thenReturn(newMock);
        Object result = new ReturnsDeepStubs().recordDeepStubAnswer(mock, container);
        assertEquals(newMock, result);
    }

    @Test
    public void testActualParameterizedType() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        Object actualType = new ReturnsDeepStubs().actualParameterizedType(mock);
        assertNotNull(actualType);
        assertEquals(mock.getClass(), actualType.getClass());
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Object newDeepStubs = new ReturnsDeepStubs().returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
        assertSame(new ReturnsDeepStubs(), newDeepStubs);
    }
}