import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Test
    public void testAnswer_returnsMockForUnmockableRawType() throws Throwable {
        returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn("String");

        Object result = returnsDeepStubs.answer(invocation);

        assertNull(result);
    }

    @Test
    public void testAnswer_returnsMockForMockableRawType() throws Throwable {
        returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(1);

        Object result = returnsDeepStubs.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof Integer);
    }

    @Test
    public void testGetMock_returnsMatchingStubbedInvocationResult() throws Throwable {
        returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn("String");
        GenericMetadataSupport genericMetadata = Mockito.mock(GenericMetadataSupport.class);
        InvocationContainerImpl containerMock = Mockito.mock(InvocationContainerImpl.class);
        InternalMockHandler mockHandler = Mockito.mock(InternalMockHandler.class);

        when(invocation.getMock()).thenReturn("String");
        when(genericMetadata.rawType()).thenReturn(String.class);
        when(mockHandler.getInvocationContainer()).thenReturn(containerMock);
        when(containerMock.getInvocationForStubbing().matches(any())).thenReturn(true);
        
        Object result = returnsDeepStubs.getMock(invocation, genericMetadata);
        
        assertNull(result);
    }

    @Test
    public void testGetMock_returnsNewDeepStubMockForNonMatchingStubbedInvocation() throws Throwable {
        returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn("String");
        GenericMetadataSupport genericMetadata = Mockito.mock(GenericMetadataSupport.class);
        InvocationContainerImpl containerMock = Mockito.mock(InvocationContainerImpl.class);
        InternalMockHandler mockHandler = Mockito.mock(InternalMockHandler.class);

        when(invocation.getMock()).thenReturn("String");
        when(genericMetadata.rawType()).thenReturn(String.class);
        when(mockHandler.getInvocationContainer()).thenReturn(containerMock);
        when(containerMock.getInvocationForStubbing().matches(any())).thenReturn(false);
        when(returnsDeepStubs.recordDeepStubMock(any(), any())).thenReturn("DeepStubMock");
        
        Object result = returnsDeepStubs.getMock(invocation, genericMetadata);
        
        assertEquals("DeepStubMock", result);
    }

    @Test
    public void testCreateNewDeepStubMock_returnsMockWithGivenRawTypeAndGenericMetadata() {
        returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport genericMetadata = Mockito.mock(GenericMetadataSupport.class);
        MockitoCore mockitoCoreMock = Mockito.mock(MockitoCore.class);

        when(genericMetadata.rawType()).thenReturn(String.class);
        when(returnsDeepStubs.getMockitoCore()).thenReturn(mockitoCoreMock);
        when(mockitoCoreMock.mock(eq(String.class), any())).thenReturn("Mock");

        Object result = returnsDeepStubs.createNewDeepStubMock(genericMetadata);

        assertEquals("Mock", result);
    }

    @Test
    public void testWithSettingsUsing_returnsMockSettingsWithExtraInterfacesIfExist() {
        returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport genericMetadata = Mockito.mock(GenericMetadataSupport.class);
        InvocationContainerImpl containerMock = Mockito.mock(InvocationContainerImpl.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        when(genericMetadata.rawExtraInterfaces()).thenReturn(new Class[]{Cloneable.class});
        when(invocation.getMock()).thenReturn("Mock");
        when(returnsDeepStubs.withSettings()).thenReturn(Mockito.withSettings());
        when(returnsDeepStubs.returnsDeepStubsAnswerUsing(any())).thenCallRealMethod();

        MockSettings result = returnsDeepStubs.withSettingsUsing(genericMetadata);

        assertNotNull(result);
        assertArrayEquals(new Class[]{Cloneable.class}, result.createMock().getClass().getInterfaces());
        assertTrue(result.getDefaultAnswer() instanceof ReturnsDeepStubs);
    }

    @Test
    public void testWithSettingsUsing_returnsMockSettingsWithDefaultSettingsIfNoExtraInterfaces() {
        returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport genericMetadata = Mockito.mock(GenericMetadataSupport.class);
        InvocationContainerImpl containerMock = Mockito.mock(InvocationContainerImpl.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        when(genericMetadata.rawExtraInterfaces()).thenReturn(new Class[]{});
        when(invocation.getMock()).thenReturn("Mock");
        when(returnsDeepStubs.withSettings()).thenReturn(Mockito.withSettings());
        when(returnsDeepStubs.returnsDeepStubsAnswerUsing(any())).thenCallRealMethod();

        MockSettings result = returnsDeepStubs.withSettingsUsing(genericMetadata);

        assertNotNull(result);
        assertEquals("Mock", result.createMock());
        assertTrue(result.getDefaultAnswer() instanceof ReturnsDeepStubs);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing_createInstanceWithExpectedGenericMetadata() {
        returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport genericMetadata = Mockito.mock(GenericMetadataSupport.class);
        MockitoCore mockitoCore = Mockito.mock(MockitoCore.class);

        when(mockitoCore.mock(eq(String.class), any())).thenReturn("Mock");
        when(genericMetadata.rawType()).thenReturn(String.class);
        when(returnsDeepStubs.getMockitoCore()).thenReturn(mockitoCore);

        ReturnsDeepStubs result = returnsDeepStubs.returnsDeepStubsAnswerUsing(genericMetadata);

        assertNotNull(result);
        assertEquals(genericMetadata, result.actualParameterizedType("Mock"));
    }

    @Test
    public void testRecordDeepStubMock_addsAnswerToInvocationContainerAndReturnsMock() throws Throwable {
        returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        InvocationContainerImpl containerMock = Mockito.mock(InvocationContainerImpl.class);
        when(invocation.getMock()).thenReturn("Mock");
        when(returnsDeepStubs.recordDeepStubMock(any(), any())).thenCallRealMethod();

        Object result = returnsDeepStubs.recordDeepStubMock("Mock", containerMock);

        assertEquals("Mock", result);
        verify(containerMock).addAnswer(any(Answer.class), eq(false));
    }

    @Test
    public void testActualParameterizedType_returnsGenericMetadataFromMockSettings() {
        returnsDeepStubs = new ReturnsDeepStubs();
        MockUtil mockUtil = Mockito.mock(MockUtil.class);
        CreationSettings mockSettings = Mockito.mock(CreationSettings.class);
        GenericMetadataSupport expectedMetadata = Mockito.mock(GenericMetadataSupport.class);

        when(mockSettings.getTypeToMock()).thenReturn(String.class);
        when(mockUtil.getMockHandler(any())).thenReturn(mockHandler);
        when(mockHandler.getMockSettings()).thenReturn(mockSettings);
        when(returnsDeepStubs.getMockUtil()).thenReturn(mockUtil);
        when(returnsDeepStubs.inferFrom(String.class)).thenReturn(expectedMetadata);

        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType("Mock");

        assertEquals(expectedMetadata, result);
        verify(mockHandler).getMockSettings();
    }

    private final InternalMockHandler mockHandler = new InternalMockHandler<Object>() {
        public Object handle(Invocation invocation) throws Throwable {
            return null;
        }

        public MockCreationSettings getMockSettings() {
            return null;
        }

        public InvocationContainer getInvocationContainer() {
            return null;
        }
    };

    private final ReturnsDeepStubs.MockUtil mockUtil = new ReturnsDeepStubs.MockUtil();

    private class MockUtil extends org.mockito.internal.util.MockUtil {

        @Override
        public InternalMockHandler getMockHandler(Object mock) {
            return mockHandler;
        }
    }

    private class MockitoCore extends org.mockito.internal.MockitoCore {

    }

    private ReturnsDeepStubs.MockUtil getMockUtil() {
        return mockUtil;
    }

    private ReturnsDeepStubs.MockitoCore getMockitoCore() {
        return new MockitoCore();
    }
}