import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {
    
    private ReturnsDeepStubs returnsDeepStubs;
    private MockUtil mockUtil;
    private InternalMockHandler<Object> mockHandler;
    private InvocationOnMock invocationMock;
    private InvocationContainerImpl container;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
        mockUtil = mock(MockUtil.class);
        mockHandler = mock(InternalMockHandler.class);
        invocationMock = mock(InvocationOnMock.class);
        container = mock(InvocationContainerImpl.class);
    }

    @Test
    public void testAnswer() throws Throwable {
        when(mockUtil.getMockHandler(invocationMock.getMock())).thenReturn(mockHandler);
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        when(invocationMock.getMock()).thenReturn("mockObject");
        when(invocationMock.getMethod()).thenReturn(ReturnsDeepStubs.class.getDeclaredMethod("answer", InvocationOnMock.class));

        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(mockUtil.getMockHandler(invocationMock.getMock())).thenReturn(mockHandler);
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        when(container.getInvocationForStubbing().matches(any())).thenReturn(true);
        when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
        when(mockUtilMock.getMockHandler(invocationMock.getMock())).thenReturn(mockHandler);
        
        Object result = returnsDeepStubs.answer(invocationMock);
        
        assertEquals("mockObject", result);
        verify(container, times(1)).addAnswer(any(), eq(false));
    }
    
    @Test
    public void testGetMock() throws Throwable {
        when(mockUtil.getMockHandler(invocationMock.getMock())).thenReturn(mockHandler);
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        when(container.getInvocationForStubbing().matches(any())).thenReturn(false);
        
        Object result = returnsDeepStubs.getMock(invocationMock);
        
        assertNotNull(result);
        verify(container, times(1)).addAnswer(any(), eq(false));
    }
    
    @Test
    public void testRecordDeepStubMock() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        when(mockUtil.getMockHandler(invocationMock.getMock())).thenReturn(mockHandler);
        
        Object result = returnsDeepStubs.recordDeepStubMock(invocation, container);
        
        assertNotNull(result);
        verify(container, times(1)).addAnswer(any(), eq(false));
    }
    
    @Test
    public void testActualParameterizedType() {
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);
        when(mockUtil.getMockHandler(invocationMock.getMock())).thenReturn(mockHandler);
        when(mockHandler.getMockSettings()).thenReturn(mockSettings);
        
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(invocationMock.getMock());
        
        assertNotNull(result);
    }
}