import static org.mockito.Mockito.*; // for mockito static imports
import static org.junit.Assert.*; // for JUnit assertion methods
import org.mockito.Answers;
import org.mockito.internal.stubbing.DefaultAnswer;
import org.mockito.internal.stubbing.ReturnsDeepStubs;
import org.mockito.internal.stubbing.answer.ReturnsEmptyValues;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.internal.util.reflection.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.MockitoCore;
import org.mockito.stubbing.Answer;
import org.mockito.MockSettings;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;

import java.io.Serializable;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class ReturnsDeepStubsTest {

    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testAnswer() throws Exception {
        // Given
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class, with(Answers.RETURNS_DEEP_STUBS));

        // When
        Object answer = returnsDeepStubs.answer(mockMethod(Object.class, "test", new Class[0]));

        // Then
        assertSame(mock, answer);
    }

    @org.junit.Test
    public void testAnswer_notMockableType() throws Exception {
        // Given
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(String.class);

        // When
        Object answer = returnsDeepStubs.answer(mockMethod(Object.class, "test", new Class[0]));

        // Then
        assertSame(new ReturnsEmptyValues().returnValueFor(String.class), answer);
    }

    @org.junit.Test
    public void testGetMock_matchedStubbedInvocation() throws Exception {
        // Given
        Object mock = mock(Object.class, withSettings(stubbing(on(mock).method("test"))
                .thenReturn(1)));
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(Integer.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // When
        Object answer = returnsDeepStubs.getMock(mock(InvocationOnMock.class), returnTypeGenericMetadata);

        // Then
        assertEquals(1, answer);
    }

    @org.junit.Test
    public void testGetMock_notMatchedStubbedInvocation() throws Exception {
        // Given
        Object mock = mock(Object.class, withSettings(stubbing(on(mock).method("test"))
                .thenReturn(1)));
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(String.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // When
        Object answer = returnsDeepStubs.getMock(mock(InvocationOnMock.class), returnTypeGenericMetadata);

        // Then
        assertNull(answer);
    }

    @org.junit.Test(expected = Throwable.class)
    public void testGetMock_throwsException() throws Exception {
        // Given
        Object mock = mock(Object.class);
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(Integer.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // When & Then
        returnsDeepStubs.getMock(mock(InvocationOnMock.class), returnTypeGenericMetadata);
    }

    @org.junit.Test
    public void testCreateNewDeepStubMock() throws Exception {
        // Given
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(List.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // When
        Object mock = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);

        // Then
        assertNotNull(mock);
        assertEquals(List.class, mock.getClass());
    }

    @org.junit.Test
    public void testWithSettingsUsing() throws Exception {
        // Given
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(Map.class);
        MockSettings mockSettings = mock(MockSettings.class);

        when(mockSettings.getTypeToMock()).thenReturn(Map.class);
        when(mockSettings.getDefaultAnswer()).thenReturn(Answers.RETURNS_DEEP_STUBS);
        when(mockSettings.rawExtraInterfaces()).thenReturn(new Class[] {Set.class});

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // When
        MockSettings result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        // Then
        assertEquals(mockSettings, result);
    }

    @org.junit.Test
    public void testReturnsDeepStubsAnswerUsing() throws Exception {
        // Given
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(Map.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // When
        ReturnsDeepStubs answer = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

        // Then
        assertEquals(returnsDeepStubs, answer);
    }

    @org.junit.Test
    public void testRecordDeepStubMock() throws Exception {
        // Given
        Object mock = mock(Object.class, withSettings(stubbing(on(mock).method("test"))
                .thenReturn(1)));
        InvocationContainerImpl container = new InvocationContainerImpl();
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // When
        Object answer = returnsDeepStubs.recordDeepStubMock(mock, container);

        // Then
        assertSame(mock, answer);
    }

    private InvocationOnMock mockMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        try {
            Method method = clazz.getMethod(methodName, parameterTypes);
            return mockMethod(method);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private InvocationOnMock mockMethod(Method method) {
        return mock(InvocationOnMock.class, with(on(any()))
                .doAnswer(invocation -> method.invoke(any(), invocation.getArguments())));
    }
}