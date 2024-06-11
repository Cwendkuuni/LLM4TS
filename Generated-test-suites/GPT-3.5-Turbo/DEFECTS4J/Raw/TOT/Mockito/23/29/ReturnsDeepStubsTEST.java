import org.junit.Test;
import org.mockito.stubbing.InvocationOnMock;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.Invocation;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {
    
    private ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    
    @Test
    public void answer_should_return_delegated_value_for_non_mockable_type() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn("mocked");
        
        Object result = returnsDeepStubs.answer(invocation);
        
        assertSame("mocked", result);
    }
    
    @Test
    public void answer_should_return_mocked_value_for_mockable_type() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        
        Object result = returnsDeepStubs.answer(invocation);
        
        assertSame(invocation.getMock(), result);
    }
    
    @Test
    public void answer_should_return_stubbed_value_for_matching_invocation() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        
        Object mockedObject = invocation.getMock();
        when(mockedObject.toString()).thenReturn("mocked");
        when(invocation.getMethod()).thenReturn(mockedObject.getClass().getMethod("toString"));
        
        Object result = returnsDeepStubs.answer(invocation);
        
        assertSame("mocked", result);
    }
    
    @Test
    public void answer_should_return_mocked_value_for_non_matching_invocation() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        
        Object mockedObject = invocation.getMock();
        when(mockedObject.toString()).thenReturn("mocked");
        when(invocation.getMethod()).thenReturn(mockedObject.getClass().getMethod("equals", Object.class));
        
        Object result = returnsDeepStubs.answer(invocation);
        
        assertSame(mockedObject, result);
    }
    
    @Test
    public void getMock_should_return_mock_with_correct_generic_metadata() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        
        Object mockedObject = invocation.getMock();
        when(mockedObject.toString()).thenReturn("mocked");
        when(invocation.getMethod()).thenReturn(mockedObject.getClass().getMethod("toString"));
        when(invocation.getMethod().getName()).thenReturn("iterator");
        when(invocation.getMethod().getReturnType()).thenReturn(Iterator.class);
        
        Object result = returnsDeepStubs.getMock(invocation, new GenericMetadataSupport());
        
        assertSame(Iterator.class, result.getClass());
        assertNotNull(result);
    }
    
    @Test
    public void createNewDeepStubMock_should_return_mock_with_correct_generic_metadata() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = spy(new ReturnsDeepStubs());
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        
        Object mockedObject = invocation.getMock();
        when(mockedObject.toString()).thenReturn("mocked");
        when(invocation.getMethod()).thenReturn(mockedObject.getClass().getMethod("toString"));
        when(invocation.getMethod().getReturnType()).thenReturn(Iterator.class);
        
        GenericMetadataSupport genericMetadataSupport = new GenericMetadataSupport();
        genericMetadataSupport.setRawType(Map.class);
        when(invocation.getMethod().getGenericReturnType()).thenReturn(genericMetadataSupport);
        
        Object result = returnsDeepStubs.createNewDeepStubMock(genericMetadataSupport);
        
        assertSame(Map.class, result.getClass());
        assertNotNull(result);
    }
    
    @Test
    public void withSettingsUsing_should_account_for_extra_interfaces() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = spy(new ReturnsDeepStubs());
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        
        Object mockedObject = invocation.getMock();
        when(mockedObject.toString()).thenReturn("mocked");
        when(invocation.getMethod()).thenReturn(mockedObject.getClass().getMethod("toString"));
        when(invocation.getMethod().getReturnType()).thenReturn(Iterator.class);
        
        GenericMetadataSupport genericMetadataSupport = new GenericMetadataSupport();
        genericMetadataSupport.setRawType(Map.class);
        genericMetadataSupport.setRawExtraInterfaces(new Class[]{Serializable.class});
        when(invocation.getMethod().getGenericReturnType()).thenReturn(genericMetadataSupport);
        
        Object result = returnsDeepStubs.withSettingsUsing(genericMetadataSupport);
        
        assertSame(MockSettings.class, result.getClass());
        assertNotNull(result);
    }
    
    @Test
    public void returnsDeepStubsAnswerUsing_should_return_ReturnsDeepStubs() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = spy(new ReturnsDeepStubs());
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        
        Object mockedObject = invocation.getMock();
        when(mockedObject.toString()).thenReturn("mocked");
        when(invocation.getMethod()).thenReturn(mockedObject.getClass().getMethod("toString"));
        when(invocation.getMethod().getReturnType()).thenReturn(Iterator.class);
        
        GenericMetadataSupport genericMetadataSupport = new GenericMetadataSupport();
        genericMetadataSupport.setRawType(Map.class);
        when(invocation.getMethod().getGenericReturnType()).thenReturn(genericMetadataSupport);
        
        Object result = returnsDeepStubs.returnsDeepStubsAnswerUsing(genericMetadataSupport);
        
        assertSame(ReturnsDeepStubs.class, result.getClass());
        assertNotNull(result);
    }
    
    @Test
    public void recordDeepStubMock_should_return_mock_with_added_answer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = spy(new ReturnsDeepStubs());
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        
        Object mockedObject = invocation.getMock();
        when(mockedObject.toString()).thenReturn("mocked");
        when(invocation.getMethod()).thenReturn(mockedObject.getClass().getMethod("toString"));
        when(invocation.getMethod().getReturnType()).thenReturn(Iterator.class);
        
        GenericMetadataSupport genericMetadataSupport = new GenericMetadataSupport();
        genericMetadataSupport.setRawType(Map.class);
        when(invocation.getMethod().getGenericReturnType()).thenReturn(genericMetadataSupport);
        
        Object mock = mock(Object.class);
        when(mock.toString()).thenReturn("mocked");
        when(invocation.getMock()).thenReturn(mock);
        
        Object result = returnsDeepStubs.recordDeepStubMock(mock, new InvocationContainerImpl());
        
        assertSame(mock, result);
    }
    
    @Test
    public void actualParameterizedType_should_return_generic_metadata_from_mock_settings() {
        ReturnsDeepStubs returnsDeepStubs = spy(new ReturnsDeepStubs());
        Object mock = mock(Object.class);
        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(new CreationSettings());
        when(new CreationSettings().getTypeToMock()).thenReturn(Map.class);
        
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);
        
        assertSame(Map.class, result.getRawType());
    }
}