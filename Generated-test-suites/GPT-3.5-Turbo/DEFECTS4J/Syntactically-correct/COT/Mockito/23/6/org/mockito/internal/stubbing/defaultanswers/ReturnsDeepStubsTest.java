
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Test 1: Test answer() when returnTypeGenericMetadata is mockable
        GenericMetadataSupport mockReturnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        InvocationOnMock mockInvocation = mock(InvocationOnMock.class);
        when(mockInvocation.getMock()).thenReturn(new Object());
        when(mockReturnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        MockitoCore mockMockitoCore = mock(MockitoCore.class);
        when(returnsDeepStubs.getMock(mockInvocation, mockReturnTypeGenericMetadata)).thenAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            assertEquals(mockInvocation, args[0]);
            assertEquals(mockReturnTypeGenericMetadata, args[1]);
            return new Object();
        });
        returnsDeepStubs.mockitoCore = mockMockitoCore;
        Object result1 = returnsDeepStubs.answer(mockInvocation);
        assertNotNull(result1);

        // Test 2: Test answer() when returnTypeGenericMetadata is not mockable
        GenericMetadataSupport nonMockableMockReturnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(nonMockableMockReturnTypeGenericMetadata.rawType()).thenReturn(String.class);
        ReturnsEmptyValues mockDelegate = mock(ReturnsEmptyValues.class);
        ReturnsEmptyValues spyDelegate = spy(mockDelegate);
        returnsDeepStubs.delegate = spyDelegate;
        when(mockMockitoCore.isTypeMockable(String.class)).thenReturn(false);
        when(spyDelegate.returnValueFor(String.class)).thenReturn("Mocked String");
        Object result2 = returnsDeepStubs.answer(mockInvocation);
        assertEquals("Mocked String", result2);
    }

    @Test
    public void testGetMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Test 1: Test getMock() when invocation matches stubbedInvocationMatcher
        InvocationOnMock mockInvocation = mock(InvocationOnMock.class);
        when(mockInvocation.getMock()).thenReturn(new Object());
        MockUtil mockMockUtil = mock(MockUtil.class);
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        InvocationContainerImpl mockContainer = mock(InvocationContainerImpl.class);
        when(mockMockUtil.getMockHandler(mockInvocation.getMock())).thenReturn(mockHandler);
        when(mockHandler.getInvocationContainer()).thenReturn(mockContainer);
        StubbedInvocationMatcher mockStubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(mockContainer.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{ mockStubbedInvocationMatcher });
        when(mockContainer.getInvocationForStubbing()).thenReturn(mockInvocation);
        Answer<Object> mockAnswer = mock(Answer.class);
        when(mockStubbedInvocationMatcher.getInvocation()).thenReturn(mockInvocation);
        MockitoCore mockMockitoCore = mock(MockitoCore.class);
        when(returnsDeepStubs.recordDeepStubMock(any(), any())).thenAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            assertEquals(mockStubbedInvocationMatcher, args[0]);
            return new Object();
        });
        returnsDeepStubs.mockitoCore = mockMockitoCore;
        Object result1 = returnsDeepStubs.getMock(mockInvocation, mock(GenericMetadataSupport.class));
        assertNotNull(result1);

        // Test 2: Test getMock() when invocation does not match any stubbedInvocationMatcher
        when(mockContainer.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[0]);
        when(returnsDeepStubs.createNewDeepStubMock(any())).thenReturn(new Object());
        Object result2 = returnsDeepStubs.getMock(mockInvocation, mock(GenericMetadataSupport.class));
        assertNotNull(result2);
    }

    @Test
    public void testCreateNewDeepStubMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport mockGenericMetadata = mock(GenericMetadataSupport.class);
        MockitoCore mockMockitoCore = mock(MockitoCore.class);
        when(returnsDeepStubs.mockitoCore).thenReturn(mockMockitoCore);
        when(mockGenericMetadata.rawType()).thenReturn(Object.class);
        Object result = returnsDeepStubs.createNewDeepStubMock(mockGenericMetadata);
        assertNotNull(result);
        verify(mockMockitoCore).mock(Object.class, withSettingsUsing(mockGenericMetadata));
    }

    @Test
    public void testWithSettingsUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport mockGenericMetadata = mock(GenericMetadataSupport.class);
        MockSettings mockSettings = mock(MockSettings.class);
        when(returnsDeepStubs.withSettings()).thenReturn(mockSettings);
        when(mockGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[]{ Comparable.class });
        when(mockSettings.extraInterfaces(new Class[]{ Comparable.class })).thenReturn(mockSettings);
        when(returnsDeepStubs.returnsDeepStubsAnswerUsing(any())).thenReturn(new ReturnsDeepStubs());
        MockSettings result = returnsDeepStubs.withSettingsUsing(mockGenericMetadata);
        assertNotNull(result);
        verify(mockGenericMetadata, times(2)).rawExtraInterfaces();
        verify(mockSettings).extraInterfaces(new Class[]{ Comparable.class });
        verify(mockSettings).defaultAnswer(any(ReturnsDeepStubs.class));
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport mockGenericMetadata = mock(GenericMetadataSupport.class);
        ReturnsDeepStubs result = returnsDeepStubs.returnsDeepStubsAnswerUsing(mockGenericMetadata);
        assertNotNull(result);
        assertSame(mockGenericMetadata, result.actualParameterizedType(mock(Object.class)));
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationContainerImpl mockContainer = mock(InvocationContainerImpl.class);
        MockitoCore mockMockitoCore = mock(MockitoCore.class);
        when(returnsDeepStubs.mockitoCore).thenReturn(mockMockitoCore);
        when(mockMockitoCore.mock(any(Class.class), any(MockSettings.class))).thenReturn(new Object());
        Object result = returnsDeepStubs.recordDeepStubMock(new Object(), mockContainer);
        assertNotNull(result);
        verify(mockContainer).addAnswer(any(Answer.class), anyBoolean());
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        MockUtil mockMockUtil = mock(MockUtil.class);
        when(returnsDeepStubs.actualParameterizedType(any())).thenCallRealMethod();
        when(mockMockUtil.getMockHandler(any())).thenReturn(mock(InternalMockHandler.class));
        when(returnsDeepStubs.createNewDeepStubMock(any())).thenReturn(mock(Object.class));
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockSettings.getTypeToMock()).thenReturn(mock(Class.class));
        when(mockMockUtil.getMockSettings(mock)).thenReturn(mockSettings);
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);
        assertNotNull(result);
        verify(mockMockUtil).getMockHandler(mock);
        verify(mockSettings).getTypeToMock();
    }

}
