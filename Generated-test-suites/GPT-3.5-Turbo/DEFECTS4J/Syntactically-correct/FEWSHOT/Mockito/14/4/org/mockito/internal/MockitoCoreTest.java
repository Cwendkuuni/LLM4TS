
package org.mockito.internal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.Stubber;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public class MockitoCoreTest {

    private MockitoCore mockitoCore = new MockitoCore();
    private Reporter reporter = new Reporter();

    @Test
    public void testMockWithValidClass() {
        MockSettings mockSettings = new MockSettingsImpl();
        Object result = mockitoCore.mock(String.class, mockSettings);
        assertNotNull(result);
    }

    @Test
    public void testMockWithInvalidClass() {
        MockSettings mockSettings = new MockSettingsImpl();
        try {
            mockitoCore.mock(null, mockSettings);
            fail("Expected exception NotAMockException was not thrown.");
        } catch (NotAMockException e) {
            // Expected exception
        }
    }

    @Test
    public void testStubWithValidMethodCall() {
        mockitoCore.when("methodCall").thenReturn("returnValue");
        IOngoingStubbing stubbing = mockitoCore.stub();
        assertNotNull(stubbing);
    }

    @Test
    public void testStubWithInvalidMethodCall() {
        try {
            mockitoCore.when(null).thenReturn("returnValue");
            fail("Expected exception MissingMethodInvocationException was not thrown.");
        } catch (MissingMethodInvocationException e) {
            // Expected exception
        }
    }

    @Test
    public void testWhenWithValidMethodCall() {
        String methodCall = "methodCall";
        mockitoCore.when(methodCall).thenReturn("returnValue");
        OngoingStubbing stubbing = mockitoCore.stub();
        assertNotNull(stubbing);
    }

    @Test
    public void testWhenWithInvalidMethodCall() {
        try {
            mockitoCore.when(null).thenReturn("returnValue");
            fail("Expected exception MissingMethodInvocationException was not thrown.");
        } catch (MissingMethodInvocationException e) {
            // Expected exception
        }
    }

    @Test
    public void testVerifyWithValidMock() {
        String mock = mock(String.class);
        VerificationMode mode = times(1);
        String result = mockitoCore.verify(mock, mode);
        assertNotNull(result);
    }

    @Test
    public void testVerifyWithNullMock() {
        VerificationMode mode = times(1);
        try {
            mockitoCore.verify(null, mode);
            fail("Expected exception NullPointerException was not thrown.");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testVerifyWithNonMockObject() {
        String object = "";
        VerificationMode mode = times(1);
        try {
            mockitoCore.verify(object, mode);
            fail("Expected exception NotAMockException was not thrown.");
        } catch (NotAMockException e) {
            // Expected exception
        }
    }

    @Test
    public void testResetWithValidMocks() {
        List<String> mocks = new ArrayList<>();
        mocks.add(mock(String.class));
        mocks.add(mock(Integer.class));
        mockitoCore.reset(mocks.toArray());
    }

    @Test
    public void testResetWithNoMocks() {
        try {
            mockitoCore.reset();
            fail("Expected exception IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testVerifyNoMoreInteractionsWithValidMocks() {
        List<String> mocks = new ArrayList<>();
        mocks.add(mock(String.class));
        mocks.add(mock(Integer.class));
        mockitoCore.verifyNoMoreInteractions(mocks.toArray());
    }

    @Test
    public void testVerifyNoMoreInteractionsWithNullMock() {
        try {
            mockitoCore.verifyNoMoreInteractions(null);
            fail("Expected exception NullPointerException was not thrown.");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testVerifyNoMoreInteractionsWithInvalidMocks() {
        List<String> mocks = new ArrayList<>();
        mocks.add(mock(String.class));
        mocks.add("invalid");
        mockitoCore.verifyNoMoreInteractions(mocks.toArray());
    }

    @Test
    public void testInOrderWithValidMocks() {
        List<String> mocks = new ArrayList<>();
        mocks.add(mock(String.class));
        mocks.add(mock(Integer.class));
        InOrder result = mockitoCore.inOrder(mocks.toArray());
        assertNotNull(result);
    }

    @Test
    public void testInOrderWithNullMock() {
        try {
            mockitoCore.inOrder(null);
            fail("Expected exception NullPointerException was not thrown.");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testInOrderWithNonMockObject() {
        String object = "";
        try {
            mockitoCore.inOrder(object);
            fail("Expected exception NotAMockException was not thrown.");
        } catch (NotAMockException e) {
            // Expected exception
        }
    }

    @Test
    public void testDoAnswer() {
        Answer answer = mock(Answer.class);
        Stubber stubber = mockitoCore.doAnswer(answer);
        assertNotNull(stubber);
    }

    @Test
    public void testStubVoid() {
        String mock = mock(String.class);
        VoidMethodStubbable stubbable = mockitoCore.stubVoid(mock);
        assertNotNull(stubbable);
    }

    @Test
    public void testValidateMockitoUsage() {
        mockitoCore.validateMockitoUsage();
        // No exception thrown indicates validation passed
    }

    @Test
    public void testGetLastInvocation() {
        Invocation result = mockitoCore.getLastInvocation();
        assertNull(result);
    }
}
