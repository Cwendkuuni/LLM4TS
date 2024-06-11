import org.junit.Test;
	import org.mockito.Mockito;

	public class ReturnsDeepStubsTest {
	
	    @Test
	    public void testAnswer_InvocationOnMockNull_ReturnNull() throws Throwable {
	        // Arrange
	        InvocationOnMock invocation = null;
	        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
	
	        // Act
	        Object result = returnsDeepStubs.answer(invocation);
	
	        // Assert
	        assertNull(result);
	    }
	
	
	    @Test
	    public void testAnswer_RawTypeNotMockable_ReturnDelegateReturnValue() throws Throwable {
	        // Arrange
	        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
	        GenericMetadataSupport genericMetadataSupport = Mockito.mock(GenericMetadataSupport.class);
	        Mockito.when(genericMetadataSupport.rawType()).thenReturn(String.class);
	        MockitoCore mockitoCore = Mockito.mock(MockitoCore.class);
	        Mockito.when(mockitoCore.isTypeMockable(String.class)).thenReturn(false);
	        ReturnsEmptyValues delegate = Mockito.mock(ReturnsEmptyValues.class);
	        Mockito.when(delegate.returnValueFor(String.class)).thenReturn("mockValue");
	
	        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
	        Whitebox.setInternalState(returnsDeepStubs, "mockitoCore", mockitoCore);
	        Whitebox.setInternalState(returnsDeepStubs, "delegate", delegate);
	
	        // Act
	        Object result = returnsDeepStubs.answer(invocation);
	
	        // Assert
	        assertEquals("mockValue", result);
	    }
	
	
	    @Test
	    public void testAnswer_DeepStubAnswerReturned_ReturnStubbedInvocationMatcherAnswer() throws Throwable {
	        // Arrange
	        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
	        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
	        StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);
	
	        Mockito.when(container.getStubbedInvocations()).thenReturn(Arrays.asList(stubbedInvocationMatcher));
	        Mockito.when(container.getInvocationForStubbing()).thenReturn(invocation);
	        Mockito.when(container.getInvocationForStubbing().matches(Mockito.any())).thenReturn(true);
	        Mockito.when(stubbedInvocationMatcher.getInvocation()).thenReturn(Mockito.mock(InvocationOnMock.class));
	        Mockito.when(stubbedInvocationMatcher.answer(invocation)).thenReturn("stubbedAnswer");
	
	
	        InternalMockHandler<Object> mockHandler = Mockito.mock(InternalMockHandler.class);
	        Mockito.when(mockHandler.getInvocationContainer()).thenReturn(container);
	        MockUtil mockUtil = Mockito.mock(MockUtil.class);
	        Mockito.when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(mockHandler);
	
	        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
	        Whitebox.setInternalState(returnsDeepStubs, "mockitoUtil", mockUtil);
	
	        // Act
	        Object result = returnsDeepStubs.answer(invocation);
	
	        // Assert
	        assertEquals("stubbedAnswer", result);
	    }
	
	
	
	    @Test
	    public void testAnswer_DeepStubAnswerNotReturned_ReturnNewDeepStubMock() throws Throwable {
	        // Arrange
	        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
	        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
	        Mockito.when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
	
	        MockSettings mockSettings = Mockito.mock(MockSettings.class);
	        InternalMockHandler<Object> mockHandler = Mockito.mock(InternalMockHandler.class);
	        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
	
	        Mockito.when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(false);
	        Mockito.when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[]{});
	        Mockito.when(mockHandler.getInvocationContainer()).thenReturn(container);
	        Mockito.when(container.addAnswer(Mockito.any(), Mockito.anyBoolean())).thenReturn(null);
	        Mockito.when(mockSettings.serializable()).thenReturn(mockSettings);
	        Mockito.when(mockSettings.defaultAnswer(Mockito.any(ReturnsDeepStubs.class))).thenReturn(mockSettings);
	        Mockito.when(Mockito.withSettings()).thenReturn(mockSettings);
	
	
	        MockitoCore mockitoCore = Mockito.mock(MockitoCore.class);
	        Mockito.when(mockitoCore.mock(returnTypeGenericMetadata.rawType(), mockSettings)).thenReturn("newDeepStubMock");
	
	        MockUtil mockUtil = Mockito.mock(MockUtil.class);
	        Mockito.when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(mockHandler);
	
	        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
	        Whitebox.setInternalState(returnsDeepStubs, "mockitoCore", mockitoCore);
	        Whitebox.setInternalState(returnsDeepStubs, "mockUtil", mockUtil);
	
	        // Act
	        Object result = returnsDeepStubs.answer(invocation);
	
	        // Assert
	        assertEquals("newDeepStubMock", result);
	    }
	}