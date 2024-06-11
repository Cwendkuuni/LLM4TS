import org.junit.Before;
import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Mock
    InvocationOnMock invocation;
    
    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer() throws Throwable {
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(genericMetadataSupport.rawType()).thenReturn(Class.class);
        
        Object result = returnsDeepStubs.answer(invocation);
        
        verify(genericMetadataSupport).resolveGenericReturnType(invocation.getMethod());
        verify(invocation, times(1)).getMock();
        verify(invocation, times(1)).getMethod();
        
        // TODO: Validate the result
    }

    @Test
    public void testGetMock() throws Throwable {
        ReturnsDeepStubs spyReturnsDeepStubs = spy(new ReturnsDeepStubs());
        Object result = spyReturnsDeepStubs.getMock(invocation, null);
        
        verify(invocation, times(1)).getMock();
        
        // TODO: Validate the result
    }

    @Test
    public void testCreateNewDeepStubMock() throws Throwable {
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(genericMetadataSupport.rawType()).thenReturn(Class.class);
        
        Object result = returnsDeepStubs.createNewDeepStubMock(genericMetadataSupport);
        
        verify(genericMetadataSupport).rawType();
        
        // TODO: Validate the result
    }

    @Test
    public void testWithSettingsUsing() throws Throwable {
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(genericMetadataSupport.rawType()).thenReturn(Class.class);
        MockSettings mockSettings = mock(MockSettings.class);
        when(mockSettings.defaultAnswer(any())).thenReturn(mockSettings);
        
        Object result = returnsDeepStubs.withSettingsUsing(genericMetadataSupport);
        
        verify(mockSettings).extraInterfaces(any(Class[].class));
        verify(mockSettings).defaultAnswer(any(ReturnsDeepStubs.class));
        
        // TODO: Validate the result
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() throws Throwable {
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(genericMetadataSupport.rawType()).thenReturn(Class.class);
        
        ReturnsDeepStubs result = returnsDeepStubs.returnsDeepStubsAnswerUsing(genericMetadataSupport);
        
        // TODO: Validate the result
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        
        Object result = returnsDeepStubs.recordDeepStubMock(mock, container);
        
        verify(container).addAnswer(any(Answer.class), eq(false));
        
        // TODO: Validate the result
    }

    @Test
    public void testActualParameterizedType() throws Throwable {
        Object mock = mock(Object.class);
        MockUtil mockUtil = mock(MockUtil.class);
        ReturnsDeepStubs returnsDeepStubs = spy(new ReturnsDeepStubs());
        when(mockUtil.getMockHandler(mock)).thenReturn(null);
        
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);
        
        verify(mockUtil).getMockHandler(mock);
        
        // TODO: Validate the result
    }
}