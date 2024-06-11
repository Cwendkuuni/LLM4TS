import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Mock
    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAnswer() throws Throwable {
        // Test case for answer method
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = returnsDeepStubs.answer(invocation);
        
        // Verify that the actualParameterizedType method is called and returns the expected value
        verify(returnsDeepStubs).actualParameterizedType(invocation.getMock());
        
        // Verify that the createNewDeepStubMock and recordDeepStubMock methods are called and return the expected value
        verify(returnsDeepStubs).createNewDeepStubMock(any());
        verify(returnsDeepStubs).recordDeepStubMock(any(), any());
        
        // Verify that the returnValueFor method is called with the expected argument and returns the expected value
        verify(returnsDeepStubs).delegate.returnValueFor(any(Class.class));
        
        // Assert the result of the answer method
        assertNotNull(result);
    }

    @Test
    public void testGetMock() throws Throwable {
        // Test case for getMock method
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Object result = returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);
        
        // Verify that the getMockHandler method is called and returns the expected value
        verify(new MockUtil()).getMockHandler(invocation.getMock());
        
        // Verify that the getStubbedInvocations method is called and returns the expected value
        verify(result).getStubbedInvocations();
        
        // Assert the result of the getMock method
        assertNotNull(result);
    }

    @Test
    public void testCreateNewDeepStubMock() {
        // Test case for createNewDeepStubMock method
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Object result = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);
        
        // Verify that the mock method is called with the expected arguments and returns the expected value
        verify(new MockitoCore()).mock(returnTypeGenericMetadata.rawType(), withSettingsUsing(returnTypeGenericMetadata));
        
        // Assert the result of the createNewDeepStubMock method
        assertNotNull(result);
    }

    @Test
    public void testWithSettingsUsing() {
        // Test case for withSettingsUsing method
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
        
        // Verify that the extraInterfaces method is called with the expected argument and returns the expected value
        verify(mockSettings).extraInterfaces(returnTypeGenericMetadata.rawExtraInterfaces());
        
        // Verify that the defaultAnswer method is called with the expected argument and returns the expected value
        verify(mockSettings).defaultAnswer(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
        
        // Assert the result of the withSettingsUsing method
        assertNotNull(mockSettings);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        // Test case for returnsDeepStubsAnswerUsing method
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        ReturnsDeepStubs result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
        
        // Verify that the actualParameterizedType method is called with the expected argument and returns the expected value
        verify(result).actualParameterizedType(any(Object.class));
        
        // Assert the result of the returnsDeepStubsAnswerUsing method
        assertNotNull(result);
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        // Test case for recordDeepStubMock method
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Object result = returnsDeepStubs.recordDeepStubMock(mock, container);
        
        // Verify that the addAnswer method is called with the expected arguments
        verify(container).addAnswer(any(Answer.class), eq(false));
        
        // Assert the result of the recordDeepStubMock method
        assertNotNull(result);
    }

    @Test
    public void testActualParameterizedType() {
        // Test case for actualParameterizedType method
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);
        
        // Verify that the getMockSettings method is called and returns the expected value
        verify(new MockUtil()).getMockHandler(mock).getMockSettings();
        
        // Assert the result of the actualParameterizedType method
        assertNotNull(result);
    }

}