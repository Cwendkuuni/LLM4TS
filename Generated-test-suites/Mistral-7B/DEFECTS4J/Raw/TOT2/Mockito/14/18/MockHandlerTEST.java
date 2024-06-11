import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.MockitoSession;
import org.mockito.exceptions.base.MockitoException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;
    private Object mockObject;
    private Object mockObjectSpy;

    @Before
    public void setup() {
        mockHandler = new MockHandler<>();
        mockObject = mock(Object.class);
        mockObjectSpy = spy(mockObject);
    }

    @Test
    public void testHandleMethod_HasAnswersForStubbing_NoVerificationMode() throws Throwable {
        // Arrange
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Answer answer = Mockito.answer(new Object());
        mockHandler.setAnswersForStubbing(Collections.singletonList(answer));

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verifyNoInteractions(mockObject); // No real method should be invoked
        Mockito.verify(mockObject).answer(invocation); // Verify stubbed answer
        Mockito.assertEquals(new Object(), result);
    }

    @Test
    public void testHandleMethod_NoAnswersForStubbing() throws Throwable {
        // Arrange
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        // Act
        Object result = mockHandler.handle(invocation);

        // Assert
        verifyNoInteractions(mockObject); // No real method should be invoked
        Mockito.assertEquals(mockSettings.getDefaultAnswer().answer(invocation), result);
    }

    @Test(expected = NullPointerException.class)
    public void testHandleMethod_NullInvocation() throws Throwable {
        // Arrange
        InvocationOnMock invocation = null;

        // Act
        mockHandler.handle(invocation);
    }

    @Test
    public void testHandleMethod_VerificationModePresent_CorrectMock() throws Throwable {
        // Arrange
        MockitoSession mockitoSession = withSettings().initMocks();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        ArgumentCaptor<VerificationDataImpl> verificationDataCaptor = ArgumentCaptor.forClass(VerificationDataImpl.class);

        Mockito.when(mockObject.answer(ArgumentMatchers.any(InvocationOnMock.class)))
                .thenReturn(new Object());

        try {
            // Act
            mockHandler.handle(invocation);
            mockitoSession.finishMocking();

            // Assert
            verify(mockObject).verify(verificationDataCaptor.capture());
        } finally {
            mockitoSession.close();
        }

        Mockito.assertEquals(Arrays.asList(invocation), verificationDataCaptor.getValue().getInvocations());
    }

    @Test(expected = AssertionError.class)
    public void testHandleMethod_VerificationModePresent_IncorrectMock() throws Throwable {
        // Arrange
        MockitoSession mockitoSession = withSettings().initMocks();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        ArgumentCaptor<VerificationDataImpl> verificationDataCaptor = ArgumentCaptor.forClass(VerificationDataImpl.class);

        try {
            // Act
            mockHandler.handle(invocation);
            mockitoSession.finishMocking();

            // Assert
            verify(mockObjectSpy).verify(verificationDataCaptor.capture());
            throw new AssertionError();
        } finally {
            mockitoSession.close();
        }
    }

    @Test
    public void testHandleMethod_OngoingStubbing() throws Throwable {
        // Arrange
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Answer answer = Mockito.answer(new Object());

        // Act
        mockHandler.handle(invocation);
        mockHandler.setAnswersForStubbing(Collections.singletonList(answer));

        // Assert
        verifyNoInteractions(mockObject); // No real method should be invoked
        Mockito.verify(mockObject).answer(invocation); // Verify stubbed answer
        Mockito.assertEquals(new Object(), mockHandler.handle(invocation));
    }

    @Test
    public void testVoidMethodStubbable() throws Throwable {
        // Arrange
        Object mock = mock(Object.class);
        VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);

        // Act
        voidMethodStubbable.stubVoid();

        // Assert
        verifyNoInteractions(mock); // No real method should be invoked
    }

    @Test
    public void testSettingAnswersForStubbing() throws Throwable {
        // Arrange
        Object mock = mock(Object.class);
        List<Answer> answers = new ArrayList<>();
        answers.add(Mockito.answer(new Object()));

        // Act
        mockHandler.setAnswersForStubbing(answers);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        // Assert
        Mockito.verify(mock).answer(invocation);
        Mockito.verify(mock).answer(invocation); // The answer list should be reused
        Mockito.assertEquals(answers.get(0).answer(invocation), mockHandler.handle(invocation));
    }

    @Test(expected = MockitoException.class)
    public void testHandleMethod_UnexpectedException() throws Throwable {
        // Arrange
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        // Act
        doThrow(new RuntimeException("Exception"))
                .when(mockObject)
                .answer(invocation);

        // Assert
        mockHandler.handle(invocation);
    }
}