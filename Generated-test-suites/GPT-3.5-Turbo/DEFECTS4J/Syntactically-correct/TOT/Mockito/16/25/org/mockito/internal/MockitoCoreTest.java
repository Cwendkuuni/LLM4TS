import org.junit.Test;
import org.mockito.*;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.stubbing.*;
import org.mockito.verification.VerificationMode;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MockitoCoreTest {

    @Test
    public void testMock_withClassToMockAndMockSettings_mockShouldReturnMockObject() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        ClassToMock classToMock = ClassToMock.class;
        MockSettings mockSettings = new MockSettingsImpl();
        
        // Act
        ClassToMock result = mockitoCore.mock(classToMock, mockSettings);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof ClassToMock);
    }

    @Test
    public void stub_stubbingNotStarted_returnNull() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        
        // Act
        IOngoingStubbing result = mockitoCore.stub();
        
        // Assert
        assertNull(result);
    }

    @Test
    public void stub_withMethodCall_stubbingStarted_returnOngoingStubbing() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        MethodCall methodCall = Mockito.mock(MethodCall.class);
        mockitoCore.stub(methodCall);
        
        // Act
        IOngoingStubbing result = mockitoCore.stub();
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof OngoingStubbingImpl);
    }
    
    @Test
    public void when_withMethodCall_stubbingStarted_returnOngoingStubbing() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        MethodCall methodCall = new MethodCall();
        
        // Act
        OngoingStubbing result = mockitoCore.when(methodCall);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof OngoingStubbingImpl);
    }
    
    @Test
    public void verify_withMockAndMode_mockIsNull_throwNullPointerException() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = null;
        VerificationMode mode = Mockito.mock(VerificationMode.class);
        
        try {
            // Act
            mockitoCore.verify(mock, mode);
            
            // Assert
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Pass
        }
    }
    
    @Test
    public void verify_withNonMockObject_throwNotAMockException() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Object nonMockObject = new Object();
        VerificationMode mode = Mockito.mock(VerificationMode.class);
        
        try {
            // Act
            mockitoCore.verify(nonMockObject, mode);
            
            // Assert
            fail("Expected NotAMockException to be thrown");
        } catch (NotAMockException e) {
            // Pass
        }
    }
    
    @Test
    public void reset_withMocks_mockHandlerResetCalledForEachMock() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        ClassToMock mock1 = Mockito.mock(ClassToMock.class);
        ClassToMock mock2 = Mockito.mock(ClassToMock.class);
        ClassToMock mock3 = Mockito.mock(ClassToMock.class);
        
        // Act
        mockitoCore.reset(mock1, mock2, mock3);
        
        // Assert
        // Verify that mockHandler.resetMock() is called for each mock object
        Mockito.verify(mockitoCore.getMockHandler(mock1), Mockito.times(1)).resetMock(mock1);
        Mockito.verify(mockitoCore.getMockHandler(mock2), Mockito.times(1)).resetMock(mock2);
        Mockito.verify(mockitoCore.getMockHandler(mock3), Mockito.times(1)).resetMock(mock3);
    }
    
    @Test
    public void verifyNoMoreInteractions_withNullMock_throwNullPointerException() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Object nullMock = null;
        
        try {
            // Act
            mockitoCore.verifyNoMoreInteractions(nullMock);
            
            // Assert
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Pass
        }
    }
    
    @Test
    public void verifyNoMoreInteractions_withNonMockObject_throwNotAMockException() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Object nonMockObject = new Object();
        
        try {
            // Act
            mockitoCore.verifyNoMoreInteractions(nonMockObject);
            
            // Assert
            fail("Expected NotAMockException to be thrown");
        } catch (NotAMockException e) {
            // Pass
        }
    }
    
    @Test
    public void inOrder_withNullMocks_throwNullPointerException() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Object[] nullMocks = null;
        
        try {
            // Act
            mockitoCore.inOrder(nullMocks);
            
            // Assert
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Pass
        }
    }
    
    @Test
    public void inOrder_withNonMockObject_throwNotAMockException() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Object[] nonMockObjects = {new Object()};
        
        try {
            // Act
            mockitoCore.inOrder(nonMockObjects);
            
            // Assert
            fail("Expected NotAMockException to be thrown");
        } catch (NotAMockException e) {
            // Pass
        }
    }
    
    @Test
    public void doAnswer_withAnswer_stubbingStarted_returnStubber() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        Answer answer = Mockito.mock(Answer.class);
        
        // Act
        Stubber result = mockitoCore.doAnswer(answer);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof StubberImpl);
    }
    
    @Test
    public void stubVoid_withMock_stubbingStarted_returnVoidMethodStubbable() {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        ClassToMock mock = Mockito.mock(ClassToMock.class);
        
        // Act
        VoidMethodStubbable result = mockitoCore.stubVoid(mock);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof VoidMethodStubbable);
    }
    
    @Test
    public void validateMockitoUsage_mockingProgressInValidState_doNothing() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        MockingProgress mockingProgress = Mockito.mock(MockingProgress.class);
        MockitoField mockingProgressField = new MockitoField(mockitoCore.getClass().getDeclaredField("mockingProgress"), mockitoCore);
        mockingProgressField.setMock(mockingProgress);
        
        // Act
        mockitoCore.validateMockitoUsage();
        
        // Assert
        // Verify that mockingProgress.validateState() is called
        Mockito.verify(mockingProgress, Mockito.times(1)).validateState();
    }
    
    @Test
    public void getLastInvocation_ongoingStubbingIsNotNull_returnLastInvocation() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        OngoingStubbing ongoingStubbing = Mockito.mock(OngoingStubbing.class);
        List<Invocation> registeredInvocations = new ArrayList<>();
        Invocation lastInvocation = Mockito.mock(Invocation.class);
        registeredInvocations.add(Mockito.mock(Invocation.class));
        registeredInvocations.add(lastInvocation);
        MockitoField ongoingStubbingField = new MockitoField(mockitoCore.getClass().getDeclaredField("ongoingStubbing"), mockitoCore);
        ongoingStubbingField.setMock(ongoingStubbing);
        Mockito.when(ongoingStubbing.getRegisteredInvocations()).thenReturn(registeredInvocations);
        
        // Act
        Invocation result = mockitoCore.getLastInvocation();
        
        // Assert
        assertEquals(lastInvocation, result);
    }
    
    @Test
    public void getLastInvocation_ongoingStubbingIsNull_returnNull() throws NoSuchFieldException, IllegalAccessException {
        // Arrange
        MockitoCore mockitoCore = new MockitoCore();
        MockitoField ongoingStubbingField = new MockitoField(mockitoCore.getClass().getDeclaredField("ongoingStubbing"), mockitoCore);
        ongoingStubbingField.setMock(null);
        
        // Act
        Invocation result = mockitoCore.getLastInvocation();
        
        // Assert
        assertNull(result);
    }

    private static class ClassToMock {}

    private static class MethodCall {}
    
    private static class MockitoField {
        private final Field field;
        private final Object target;

        public MockitoField(Field field, Object target) {
            this.field = field;
            this.target = target;
        }

        public void setMock(Object value) throws IllegalAccessException {
            field.setAccessible(true);
            field.set(target, value);
        }
    }
}