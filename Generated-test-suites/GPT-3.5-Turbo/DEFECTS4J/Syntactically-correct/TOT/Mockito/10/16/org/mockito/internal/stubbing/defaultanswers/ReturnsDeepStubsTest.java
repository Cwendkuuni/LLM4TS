import org.junit.*;
import org.mockito.*;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {
    
    private ReturnsDeepStubs returnsDeepStubs;
    
    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }
    
    @Test
    public void testAnswer_validInvocation_returnsMock() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Answer<Object> answer = returnsDeepStubs.answer(invocation);
        
        Assert.assertNotNull(answer);
        Assert.assertTrue(answer instanceof ReturnsDeepStubs.DeeplyStubbedAnswer);
    }
    
    @Test
    public void testAnswer_invalidInvocation_returnsReturnValue() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.getMock();
        Answer<Object> answer = returnsDeepStubs.answer(invocation);
        
        Assert.assertNotNull(answer);
        Assert.assertEquals(returnsDeepStubs.delegate(), answer.answer(invocation));
    }
    
    @Test
    public void testDeepStub_matchingInvocation_returnsAnswerFromStub() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        ReturnsDeepStubs returnValue = new ReturnsDeepStubs();
        InternalMockHandler<Object> handler = new MockUtil().getMockHandler(invocation.getMock());
        InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();
        StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);
        container.addAnswer(stubbedInvocationMatcher, false);
        
        Answer<Object> answer = returnsDeepStubs.answer(invocation);
        
        Assert.assertNotNull(answer);
        Assert.assertEquals(stubbedInvocationMatcher.answer(invocation), answer.answer(invocation));
    }
    
    @Test
    public void testDeepStub_noMatchingInvocation_returnsRecordedAnswer() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        ReturnsDeepStubs returnValue = new ReturnsDeepStubs();
        InternalMockHandler<Object> handler = new MockUtil().getMockHandler(invocation.getMock());
        InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();
        container.getInvocationForStubbing();
        
        Answer<Object> answer = returnsDeepStubs.answer(invocation);
        
        Assert.assertNotNull(answer);
        Assert.assertEquals(returnValue.recordDeepStubAnswer(returnValue.newDeepStubMock(returnValue.actualParameterizedType(invocation.getMock())), container), answer.answer(invocation));
    }
    
    @Test
    public void testNewDeepStubMock_noRawExtraInterfaces_returnsMock() {
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        Mockito.when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(false);
        MockitoCore mockitoCore = Mockito.mock(MockitoCore.class);
        Mockito.when(mockitoCore.mock(Matchers.any(Class.class), Matchers.any(MockSettings.class))).thenReturn("mock");
        Mockito.when(returnsDeepStubs.mockitoCore()).thenReturn(mockitoCore);
        
        Object mock = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);
        
        Assert.assertNotNull(mock);
        Assert.assertEquals("mock", mock);
    }
    
    @Test
    public void testNewDeepStubMock_hasRawExtraInterfaces_returnsMockWithExtraInterfaces() {
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        Mockito.when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);
        Mockito.when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class<?>[] {Serializable.class});
        MockitoCore mockitoCore = Mockito.mock(MockitoCore.class);
        Mockito.when(mockitoCore.mock(Matchers.any(Class.class), Matchers.any(MockSettings.class))).thenReturn("mock");
        Mockito.when(returnsDeepStubs.mockitoCore()).thenReturn(mockitoCore);
        
        Object mock = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);
        
        Assert.assertNotNull(mock);
        Assert.assertEquals("mock", mock);
    }
    
    @Test
    public void testWithSettingsUsing_hasRawExtraInterfaces_returnsSettingsWithExtraInterfaces() {
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        Mockito.when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);
        Mockito.when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class<?>[] {Serializable.class});
        
        MockSettings settings = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
        
        Assert.assertNotNull(settings);
        Assert.assertTrue(settings.extraInterfaces(Serializable.class).serializable().defaultAnswer(returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata)) instanceof MockSettings);
    }
    
    @Test
    public void testWithSettingsUsing_noRawExtraInterfaces_returnsSettings() {
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        Mockito.when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(false);
        
        MockSettings settings = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
        
        Assert.assertNotNull(settings);
        Assert.assertTrue(settings.serializable().defaultAnswer(returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata)) instanceof MockSettings);
    }
    
    @Test
    public void testReturnsDeepStubsAnswerUsing_validMetadata_returnsDeepStubsSerializationFallback() {
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        
        ReturnsDeepStubs answer = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
        
        Assert.assertNotNull(answer);
        Assert.assertTrue(answer instanceof ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback);
    }
    
    @Test
    public void testRecordDeepStubAnswer_validMockAndContainer_returnsMock() throws Throwable {
        Object mock = Mockito.mock(Object.class);
        ReturnsDeepStubs returnValue = new ReturnsDeepStubs();
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
        container.addAnswer(Mockito.any(ReturnsDeepStubs.DeeplyStubbedAnswer.class), Matchers.anyBoolean());
        
        Object result = returnValue.recordDeepStubAnswer(mock, container);
        
        Assert.assertNotNull(result);
        Assert.assertEquals(mock, result);
    }
    
    @Test
    public void testActualParameterizedType_validMock_returnsGenericMetadata() {
        Object mock = Mockito.mock(Object.class);
        CreationSettings mockSettings = Mockito.mock(CreationSettings.class);
        Mockito.when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        Mockito.when(mockSettings.getTypeToMock()).thenReturn(Serializable.class);
        GenericMetadataSupport genericMetadataSupport = Mockito.mock(GenericMetadataSupport.class);
        Mockito.when(returnsDeepStubs.actualParameterizedType(mock)).thenReturn(genericMetadataSupport);
        
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);
        
        Assert.assertNotNull(result);
        Assert.assertEquals(genericMetadataSupport, result);
    }
    
    @Test
    public void testReturnsDeepStubsSerializationFallback_writeReplace_returnsMockitoReturnsDeepStubs() throws IOException {
        ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback answer = returnsDeepStubs.new ReturnsDeepStubsSerializationFallback(null);
        
        Object result = answer.writeReplace();
        
        Assert.assertNotNull(result);
        Assert.assertEquals(Mockito.RETURNS_DEEP_STUBS, result);
    }
    
    @Test
    public void testDeeplyStubbedAnswer_validMock_returnsMock() {
        Object mock = Mockito.mock(Object.class);
        ReturnsDeepStubs returnValue = new ReturnsDeepStubs();
        ReturnsDeepStubs.DeeplyStubbedAnswer answer = new ReturnsDeepStubs.DeeplyStubbedAnswer(mock);
        
        Object result = answer.answer(Mockito.mock(InvocationOnMock.class));
        
        Assert.assertNotNull(result);
        Assert.assertEquals(mock, result);
    }
}