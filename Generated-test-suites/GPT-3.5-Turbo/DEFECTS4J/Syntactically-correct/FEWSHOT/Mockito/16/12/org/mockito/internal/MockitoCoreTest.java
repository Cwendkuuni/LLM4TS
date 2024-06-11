
//import statements

public class MockitoCoreTest {
  
    @Test
    public void testMock() {
        //Initialize
        MockitoCore mockitoCore = new MockitoCore();
        Class<T> classToMock = T.class;
        MockSettings mockSettings = new MockSettings();
        boolean shouldResetOngoingStubbing = true;
        
        //Act
        T result = mockitoCore.mock(classToMock, mockSettings, shouldResetOngoingStubbing);
        
        //Assert
        assertNotNull(result);
        assertEquals(T.class, result.getClass());
    }
    
    @Test
    public void testMockWithSettings() {
        //Initialize
        MockitoCore mockitoCore = new MockitoCore();
        Class<T> classToMock = T.class;
        MockSettings mockSettings = new MockSettings();
        
        //Act
        T result = mockitoCore.mock(classToMock, mockSettings);
        
        //Assert
        assertNotNull(result);
        assertEquals(T.class, result.getClass());
    }
    
    @Test
    public void testStub() {
        //Initialize
        MockitoCore mockitoCore = new MockitoCore();
        
        //Act
        IOngoingStubbing result = mockitoCore.stub();
        
        //Assert
        assertNull(result);
        verify(mockitoCore.mockingProgress, times(1)).pullOngoingStubbing();
        verify(mockitoCore.mockingProgress, times(1)).resetOngoingStubbing();
        verify(mockitoCore.reporter, times(1)).missingMethodInvocation();
    }
    
    @Test
    public void testStubDeprecated() {
        //Initialize
        MockitoCore mockitoCore = new MockitoCore();
        T methodCall = new T();
        
        //Act
        DeprecatedOngoingStubbing<T> result = mockitoCore.stub(methodCall);
        
        //Assert
        assertNotNull(result);
        assertEquals(DeprecatedOngoingStubbing.class, result.getClass());
        verify(mockitoCore.mockingProgress, times(1)).stubbingStarted();
        verify(mockitoCore.mockingProgress, times(1)).pullOngoingStubbing();
    }
    
    @Test
    public void testWhen() {
        //Initialize
        MockitoCore mockitoCore = new MockitoCore();
        T methodCall = new T();
        
        //Act
        OngoingStubbing<T> result = mockitoCore.when(methodCall);
        
        //Assert
        assertNotNull(result);
        assertEquals(OngoingStubbing.class, result.getClass());
        verify(mockitoCore.mockingProgress, times(1)).stubbingStarted();
        verify(mockitoCore.mockingProgress, times(1)).pullOngoingStubbing();
    }
    
    @Test
    public void testVerify() {
        //Initialize
        MockitoCore mockitoCore = new MockitoCore();
        T mock = new T();
        VerificationMode mode = new VerificationMode();
        
        //Act
        T result = mockitoCore.verify(mock, mode);
        
        //Assert
        assertEquals(mock, result);
        verify(mockitoCore.mockingProgress, times(1)).validateState();
        verify(mockitoCore.reporter, times(1)).notAMockPassedToVerify();
        verify(mockitoCore.reporter, times(1)).nullPassedToVerify();
        verify(mockitoCore.mockingProgress, times(1)).verificationStarted(mode);
    }
    
    @Test
    public void testReset() {
        //Initialize
        MockitoCore mockitoCore = new MockitoCore();
        T mock = new T();
        
        //Act
        mockitoCore.reset(mock);
        
        //Assert
        verify(mockitoCore.mockingProgress, times(1)).validateState();
        verify(mockitoCore.mockingProgress, times(1)).reset();
        verify(mockitoCore.mockingProgress, times(1)).resetOngoingStubbing();
        verify(mockitoCore.mockUtil, times(1)).resetMock(mock);
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        //Initialize
        MockitoCore mockitoCore = new MockitoCore();
        T mock = new T();
        
        //Act
        mockitoCore.verifyNoMoreInteractions(mock);
        
        //Assert
        verify(mockitoCore.mockingProgress, times(1)).validateState();
        verify(mockitoCore.mockitoUtil, times(1)).getMockHandler(mock);
        verify(mockitoCore.reporter, times(1)).notAMockPassedToVerifyNoMoreInteractions();
        verify(mockitoCore.reporter, times(1)).nullPassedToVerifyNoMoreInteractions();
    }
    
    @Test
    public void testInOrder() {
        //Initialize
        MockitoCore mockitoCore = new MockitoCore();
        T mock = new T();
        
        //Act
        InOrder result = mockitoCore.inOrder(mock);
        
        //Assert
        assertNotNull(result);
        assertEquals(InOrderImpl.class, result.getClass());
        verify(mockitoCore.reporter, times(1)).mocksHaveToBePassedWhenCreatingInOrder();
        verify(mockitoCore.reporter, times(1)).nullPassedWhenCreatingInOrder();
        verify(mockitoCore.reporter, times(1)).notAMockPassedWhenCreatingInOrder();
    }
    
    @Test
    public void testDoAnswer() {
        //Initialize
        MockitoCore mockitoCore = new MockitoCore();
        Answer answer = new Answer();
        
        //Act
        Stubber result = mockitoCore.doAnswer(answer);
        
        //Assert
        assertNotNull(result);
        assertEquals(StubberImpl.class, result.getClass());
        verify(mockitoCore.mockingProgress, times(1)).stubbingStarted();
        verify(mockitoCore.mockingProgress, times(1)).resetOngoingStubbing();
    }
    
    @Test
    public void testStubVoid() {
        //Initialize
        MockitoCore mockitoCore = new MockitoCore();
        T mock = new T();
        
        //Act
        VoidMethodStubbable<T> result = mockitoCore.stubVoid(mock);
        
        //Assert
        assertNotNull(result);
        assertEquals(MockHandlerInterface.class, result.getClass());
        verify(mockitoCore.mockingProgress, times(1)).stubbingStarted();
        verify(mockitoCore.mockitoUtil, times(1)).getMockHandler(mock);
    }
    
    @Test
    public void testValidateMockitoUsage() {
        //Initialize
        MockitoCore mockitoCore = new MockitoCore();
        
        //Act
        mockitoCore.validateMockitoUsage();
        
        //Assert
        verify(mockitoCore.mockingProgress, times(1)).validateState();
    }
    
    @Test
    public void testGetLastInvocation() {
        //Initialize
        MockitoCore mockitoCore = new MockitoCore();
        
        //Act
        Invocation result = mockitoCore.getLastInvocation();
        
        //Assert
        assertNotNull(result);
        verify(mockitoCore.mockingProgress, times(1)).pullOngoingStubbing();
    }
}
