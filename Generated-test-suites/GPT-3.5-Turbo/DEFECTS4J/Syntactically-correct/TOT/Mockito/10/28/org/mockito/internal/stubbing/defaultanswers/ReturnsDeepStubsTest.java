import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Mock
    private GenericsNest mock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAnswerForGenericsNest() {
        // Arrange
        when(mock.entrySet().iterator().next().getValue().iterator().next()).thenReturn(10);

        // Act
        Number result = mock.entrySet().iterator().next().getValue().iterator().next();

        // Assert
        assertNotNull(result);
        assertEquals(10, result.intValue());
    }

    @Test
    public void testAnswerForNonMockableType() {
        // Arrange
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.isTypeMockable(any(Class.class))).thenReturn(false);
        when(mockUtil.getMockHandler(mock)).thenReturn(new InternalMockHandler<Object>());
        when(mockUtil.getMockHandler(mock).getInvocationContainer()).thenReturn(new InvocationContainerImpl());
        when(mockUtil.getMockHandler(mock).getInvocationContainer().getStubbedInvocations()).thenReturn(null);
        when(mockUtil.getMockHandler(mock).getInvocationContainer().getInvocationForStubbing()).thenReturn(null);
        when(mockUtil.getMockHandler(mock).getInvocationContainer().getInvocationForStubbing().matches(any(InvocationOnMock.class))).thenReturn(false);
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
        when(delegate.returnValueFor(any(Class.class))).thenReturn(null);
        LazyHolder.DELEGATE = delegate;

        // Act
        Object result = null;
        try {
            result = new ReturnsDeepStubs().answer(null);
        } catch (Throwable throwable) {
            fail("Unexpected exception occurred");
        }

        // Assert
        assertNull(result);
        verify(mockUtil, times(1)).getMockHandler(mock);
        verify(mockUtil, times(1)).isTypeMockable(any(Class.class));
        verify(delegate, times(1)).returnValueFor(any(Class.class));
        verify(mockUtil.getMockHandler(mock), times(1)).getInvocationContainer();
        assertNull(mockUtil.getMockHandler(mock).getInvocationContainer().getStubbedInvocations());
        assertNull(mockUtil.getMockHandler(mock).getInvocationContainer().getInvocationForStubbing());
    }

    @Test
    public void testAnswerForMatchingInvocation() {
        // Arrange
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.isTypeMockable(any(Class.class))).thenReturn(true);
        when(mockUtil.getMockHandler(mock)).thenReturn(new InternalMockHandler<Object>());
        when(mockUtil.getMockHandler(mock).getInvocationContainer()).thenReturn(new InvocationContainerImpl());
        when(mockUtil.getMockHandler(mock).getInvocationContainer().getStubbedInvocations()).thenReturn(null);
        when(mockUtil.getMockHandler(mock).getInvocationContainer().getInvocationForStubbing()).thenReturn(null);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocationMatcher.getInvocation()).thenReturn(null);
        when(mockUtil.getMockHandler(mock).getInvocationContainer().getStubbedInvocations().contains(stubbedInvocationMatcher)).thenReturn(true);

        // Act
        Object result = null;
        try {
            result = new ReturnsDeepStubs().answer(null);
        } catch (Throwable throwable) {
            fail("Unexpected exception occurred");
        }

        // Assert
        assertNull(result);
        verify(mockUtil, times(1)).getMockHandler(mock);
        verify(mockUtil, times(1)).isTypeMockable(any(Class.class));
        verify(mockUtil.getMockHandler(mock), times(1)).getInvocationContainer();
        assertNull(mockUtil.getMockHandler(mock).getInvocationContainer().getInvocationForStubbing());
        verify(stubbedInvocationMatcher, times(1)).getInvocation();
        verify(mockUtil.getMockHandler(mock).getInvocationContainer().getStubbedInvocations(), times(1)).contains(stubbedInvocationMatcher);
    }

    @Test
    public void testAnswerForNonMatchingInvocation() {
        // Arrange
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.isTypeMockable(any(Class.class))).thenReturn(true);
        when(mockUtil.getMockHandler(mock)).thenReturn(new InternalMockHandler<Object>());
        when(mockUtil.getMockHandler(mock).getInvocationContainer()).thenReturn(new InvocationContainerImpl());
        when(mockUtil.getMockHandler(mock).getInvocationContainer().getStubbedInvocations()).thenReturn(null);
        when(mockUtil.getMockHandler(mock).getInvocationContainer().getInvocationForStubbing()).thenReturn(null);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocationMatcher.getInvocation()).thenReturn(null);
        when(mockUtil.getMockHandler(mock).getInvocationContainer().getStubbedInvocations().contains(stubbedInvocationMatcher)).thenReturn(false);

        // Act
        Object result = null;
        try {
            result = new ReturnsDeepStubs().answer(null);
        } catch (Throwable throwable) {
            fail("Unexpected exception occurred");
        }

        // Assert
        assertNull(result);
        verify(mockUtil, times(1)).getMockHandler(mock);
        verify(mockUtil, times(1)).isTypeMockable(any(Class.class));
        verify(mockUtil.getMockHandler(mock), times(1)).getInvocationContainer();
        assertNull(mockUtil.getMockHandler(mock).getInvocationContainer().getInvocationForStubbing());
        verify(stubbedInvocationMatcher, times(1)).getInvocation();
        verify(mockUtil.getMockHandler(mock).getInvocationContainer().getStubbedInvocations(), times(1)).contains(stubbedInvocationMatcher);
    }

    @Test
    public void testNewDeepStubMockWithRawExtraInterfaces() {
        // Arrange
        MockSettings mockSettings = mock(MockSettings.class);
        when(mockSettings.extraInterfaces(any(Class[].class))).thenReturn(mockSettings);
        when(mockSettings.serializable()).thenReturn(mockSettings);
        when(mockSettings.defaultAnswer(any(ReturnsDeepStubs.class))).thenReturn(mockSettings);
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.isTypeMockable(any(Class.class))).thenReturn(true);
        when(mockUtil.getMockHandler(mock)).thenReturn(new InternalMockHandler<Object>());
        when(mockUtil.getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(genericMetadataSupport.hasRawExtraInterfaces()).thenReturn(true);
        when(genericMetadataSupport.rawExtraInterfaces()).thenReturn(new Class[]{Map.class, Set.class});
        when(mockUtil.inferFrom(any(Class.class))).thenReturn(genericMetadataSupport);

        // Act
        Object result = new ReturnsDeepStubs().newDeepStubMock(genericMetadataSupport);

        // Assert
        assertNotNull(result);
        verify(mockUtil, times(1)).getMockHandler(mock);
        verify(mockUtil, times(1)).isTypeMockable(any(Class.class));
        verify(genericMetadataSupport, times(1)).hasRawExtraInterfaces();
        verify(genericMetadataSupport, times(1)).rawExtraInterfaces();
        verify(mockSettings, times(1)).extraInterfaces(new Class[]{Map.class, Set.class});
        verify(mockSettings, times(1)).serializable();
        verify(mockSettings, times(1)).defaultAnswer(any(ReturnsDeepStubs.class));
    }

    @Test
    public void testNewDeepStubMockWithoutRawExtraInterfaces() {
        // Arrange
        MockSettings mockSettings = mock(MockSettings.class);
        when(mockSettings.serializable()).thenReturn(mockSettings);
        when(mockSettings.defaultAnswer(any(ReturnsDeepStubs.class))).thenReturn(mockSettings);
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.isTypeMockable(any(Class.class))).thenReturn(true);
        when(mockUtil.getMockHandler(mock)).thenReturn(new InternalMockHandler<Object>());
        when(mockUtil.getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(genericMetadataSupport.hasRawExtraInterfaces()).thenReturn(false);
        when(mockUtil.inferFrom(any(Class.class))).thenReturn(genericMetadataSupport);

        // Act
        Object result = new ReturnsDeepStubs().newDeepStubMock(genericMetadataSupport);

        // Assert
        assertNotNull(result);
        verify(mockUtil, times(1)).getMockHandler(mock);
        verify(mockUtil, times(1)).isTypeMockable(any(Class.class));
        verify(genericMetadataSupport, times(1)).hasRawExtraInterfaces();
        verify(mockSettings, times(1)).serializable();
        verify(mockSettings, times(1)).defaultAnswer(any(ReturnsDeepStubs.class));
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        // Arrange
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Map.class);
        ReturnsDeepStubsSerializationFallback serializationFallback = new ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata);

        // Act
        ReturnsDeepStubs result = serializationFallback.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

        // Assert
        assertNotNull(result);
        assertSame(returnTypeGenericMetadata, result.actualParameterizedType(null));
    }

    @Test
    public void testRecordDeepStubAnswer() {
        // Arrange
        InvocationContainerImpl container = new InvocationContainerImpl();
        Object mock = mock(Object.class);
        DeeplyStubbedAnswer deeplyStubbedAnswer = new DeeplyStubbedAnswer(mock);

        // Act
        Object result = null;
        try {
            result = new ReturnsDeepStubs().recordDeepStubAnswer(mock, container);
        } catch (Throwable throwable) {
            fail("Unexpected exception occurred");
        }

        // Assert
        assertNotNull(result);
        assertSame(mock, result);
        assertEquals(1, container.getAnswerList().size());
        assertTrue(container.getAnswerList().get(0) instanceof DeeplyStubbedAnswer);
    }

    @Test
    public void testActualParameterizedType() {
        // Arrange
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(new MockUtil().inferFrom(any(Class.class))).thenReturn(genericMetadataSupport);

        // Act
        GenericMetadataSupport result = new ReturnsDeepStubs().actualParameterizedType(mock);

        // Assert
        assertNotNull(result);
        assertSame(genericMetadataSupport, result);
        verify(new MockUtil(), times(1)).getMockHandler(mock);
        verify(new MockUtil().getMockHandler(mock), times(1)).getMockSettings();
        verify(mockSettings, times(1)).getTypeToMock();
        verify(new MockUtil(), times(1)).inferFrom(any(Class.class));
    }

    @Test
    public void testReturnsDeepStubsSerializationFallback() {
        // Arrange
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        ReturnsDeepStubsSerializationFallback serializationFallback = new ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata);

        // Act
        Object result = serializationFallback.writeReplace();

        // Assert
        assertNotNull(result);
        assertSame(Mockito.RETURNS_DEEP_STUBS, result);
    }

    @Test
    public void testDeeplyStubbedAnswer() {
        // Arrange
        Object mock = mock(Object.class);
        DeeplyStubbedAnswer deeplyStubbedAnswer = new DeeplyStubbedAnswer(mock);

        // Act
        Object result = null;
        try {
            result = deeplyStubbedAnswer.answer(null);
        } catch (Throwable throwable) {
            fail("Unexpected exception occurred");
        }

        // Assert
        assertNotNull(result);
        assertSame(mock, result);
    }
}