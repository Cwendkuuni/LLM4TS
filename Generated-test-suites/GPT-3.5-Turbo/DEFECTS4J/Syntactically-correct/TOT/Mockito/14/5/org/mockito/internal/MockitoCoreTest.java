import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class MockitoCoreTest {

    private MockitoCore mockitoCore;
    
    @Before
    public void setUp() {
        mockitoCore = new MockitoCore();
    }
    
    @Test
    public void testMock() {
        // Typical use case
        List<Object> mockList = mockitoCore.mock(List.class, MockSettingsImpl.defaultSettings());
        
        // Verify that the list is a mock
        Assert.assertTrue(Mockito.mockUtil.isMock(mockList));
        
        // Verify that the mockingProgress was called
        Mockito.verify(mockitoCore.mockingProgress, times(1)).mockingStarted(eq(mockList), eq(List.class), any(MockSettingsImpl.class));
    }
    
    @Test
    public void testStub() {
        // Typical use case
        mockitoCore.when(5).thenReturn("five");
        
        // Stubbing should return "five"
        Assert.assertEquals("five", mockitoCore.stub().thenApply(5));
        
        // Verify that the mockingProgress was called
        Mockito.verify(mockitoCore.mockingProgress, times(1)).stubbingStarted();
        Mockito.verify(mockitoCore.mockingProgress, times(1)).pullOngoingStubbing();
    }
    
    @Test
    public void testVerify() {
        // Typical use case
        List<Object> mockList = mockitoCore.mock(List.class, MockSettingsImpl.defaultSettings());
        
        // Verify that the mockList is verified with times(1) verification mode
        mockitoCore.verify(mockList, times(1));
        
        // Verify that the mockingProgress was called
        Mockito.verify(mockitoCore.mockingProgress, times(1)).verificationStarted(eq(times(1)));
    }
    
    @Test
    public void testReset() {
        // Typical use case
        List<Object> mockList = mockitoCore.mock(List.class, MockSettingsImpl.defaultSettings());
        
        // Reset the mockList
        mockitoCore.reset(mockList);
        
        // Verify that the mockingProgress was called
        Mockito.verify(mockitoCore.mockingProgress, times(1)).reset();
        Mockito.verify(mockitoCore.mockingProgress, times(1)).resetOngoingStubbing();
        
        // Verify that the mockUtil resetMock was called
        Mockito.verify(mockitoCore.mockUtil, times(1)).resetMock(eq(mockList));
    }
    
    @Test
    public void testVerifyNoMoreInteractions() {
        // Edge case: No mocks passed
        mockitoCore.verifyNoMoreInteractions(); // Should not throw any exception
        
        // Typical use case
        List<Object> mockList = mockitoCore.mock(List.class, MockSettingsImpl.defaultSettings());
        
        // Add some invocations to the mockList
        mockList.add("item1");
        mockList.add("item2");
        
        // Verify that there are no more interactions for the mockList
        mockitoCore.verifyNoMoreInteractions(mockList); // Should not throw any exception
        
        // Verify that the reporter methods were called
        Mockito.verify(mockitoCore.reporter, never()).nullPassedToVerifyNoMoreInteractions();
        Mockito.verify(mockitoCore.reporter, never()).notAMockPassedToVerifyNoMoreInteractions();
        
        // Edge case: null passed as mock
        mockitoCore.verifyNoMoreInteractions((List<Object>) null); // Should throw NullAMockException
        
        // Verify that the reporter methods were called
        Mockito.verify(mockitoCore.reporter, times(1)).nullPassedToVerifyNoMoreInteractions();
        
        // Edge case: object which is not a mock passed
        Object object = new Object();
        mockitoCore.verifyNoMoreInteractions(object); // Should throw NotAMockException
        
        // Verify that the reporter methods were called
        Mockito.verify(mockitoCore.reporter, times(1)).notAMockPassedToVerifyNoMoreInteractions();
    }
    
    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        // Edge case: Empty mocks list
        List<Object> emptyMocks = new ArrayList<>();
        InOrderContext inOrderContext = mock(InOrderContext.class);
        mockitoCore.verifyNoMoreInteractionsInOrder(emptyMocks, inOrderContext); // Should not throw any exception
        
        // Typical use case
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock(List.class, MockSettingsImpl.defaultSettings()));
        
        // Add some invocations to the mocks
        ((List) mocks.get(0)).add("item1");
        ((List) mocks.get(0)).add("item2");
        
        // Verify that there are no more interactions in order for the mocks
        mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext); // Should not throw any exception
        
        // Verify that the mockingProgress was called
        Mockito.verify(mockitoCore.mockingProgress, times(1)).validateState();
        
        // Verify that the AllInvocationsFinder was called
        Mockito.verify(mockitoCore.finder, times(anyInt())).find(mocks);
        
        // Verify that the VerificationModeFactory was called
        Mockito.verify(mockitoCore.verificationModeFactory, times(1)).noMoreInteractions();
    }
    
    @Test
    public void testInOrder() {
        // Edge case: Empty mocks list
        mockitoCore.inOrder(); // Should throw NullAMockException
        
        // Verify that the reporter methods were called
        Mockito.verify(mockitoCore.reporter, times(1)).mocksHaveToBePassedWhenCreatingInOrder();
        
        // Typical use case
        List<Object> mocks = new ArrayList<>();
        mocks.add(mock(List.class, MockSettingsImpl.defaultSettings()));
        
        // Create InOrder instance
        mockitoCore.inOrder(mocks.get(0));
        
        // Verify that the reporter methods were not called
        Mockito.verify(mockitoCore.reporter, times(0)).mocksHaveToBePassedWhenCreatingInOrder();
        Mockito.verify(mockitoCore.reporter, times(0)).nullPassedWhenCreatingInOrder();
        Mockito.verify(mockitoCore.reporter, times(0)).notAMockPassedWhenCreatingInOrder();
    }
    
    @Test
    public void testDoAnswer() {
        // Edge case: No ongoing stubbing
        mockitoCore.doAnswer(null); // Should not throw any exception
        
        // Typical use case
        mockitoCore.when(5).thenReturn("five");
        
        // Reset the ongoing stubbing
        mockitoCore.resetOngoingStubbing();
        
        // Stubbing using doAnswer
        Answer<String> answer = (invocation) -> "mocked answer";
        mockitoCore.doAnswer(answer);
        
        // Stubbing should return "mocked answer"
        Assert.assertEquals("mocked answer", mockitoCore.stub().thenAnswer(null));
        
        // Verify that the mockingProgress was called
        Mockito.verify(mockitoCore.mockingProgress, times(1)).stubbingStarted();
        Mockito.verify(mockitoCore.mockingProgress, times(1)).resetOngoingStubbing();
        
        // Verify that the StubberImpl was called
        Mockito.verify(mockitoCore.stubberImpl, times(1)).doAnswer(eq(answer));
    }
    
    @Test
    public void testStubVoid() {
        // Typical use case
        List<Object> mockList = mock(List.class, MockSettingsImpl.defaultSettings());
        
        // Stub the void method
        mockitoCore.stubVoid(mockList);
        
        // Verify that the mockingProgress was called
        Mockito.verify(mockitoCore.mockingProgress, times(1)).stubbingStarted();
        
        // Verify that the mockUtil getMockHandler was called
        Mockito.verify(mockitoCore.mockUtil, times(1)).getMockHandler(eq(mockList));
        
        // Verify that the voidMethodStubbable was called
        Mockito.verify(mockitoCore.handler, times(1)).voidMethodStubbable(eq(mockList));
    }
    
    @Test
    public void testValidateMockitoUsage() {
        // Typical use case
        mockitoCore.validateMockitoUsage();
        
        // Verify that the mockingProgress was called
        Mockito.verify(mockitoCore.mockingProgress, times(1)).validateState();
    }
    
    @Test
    public void testGetLastInvocation() {
        // Typical use case
        List<Object> mockList = mock(List.class, MockSettingsImpl.defaultSettings());
        mockitoCore.when(mockList.add("item")).thenReturn(true);
        mockList.add("item");
        
        // Get the last invocation
        Invocation lastInvocation = mockitoCore.getLastInvocation();
        
        // Verify the last invocation details
        Assert.assertEquals(mockList, lastInvocation.getMock());
        Assert.assertEquals(List.class.getMethod("add", Object.class), lastInvocation.getMethod());
        Assert.assertEquals(new Object[]{"item"}, lastInvocation.getArguments());
        
        // Verify that the mockingProgress was called
        Mockito.verify(mockitoCore.mockingProgress, times(1)).pullOngoingStubbing();
        
        // Verify that the OngoingStubbingImpl was called
        Mockito.verify(mockitoCore.ongoingStubbingImpl, times(1)).getRegisteredInvocations();
    }

}