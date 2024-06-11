package org.mockito.internal;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.invocation.Invocation;
import org.mockito.verification.VerificationMode;

public class MockitoCoreTest {

    // Test mock method
    @Test
    public void testMock() {
        // create a mock object
        MockitoCore mockito = new MockitoCore();
        MockSettings mockSettings = mock(MockSettings.class);

        assertNotNull(mockito.mock(MockitoCore.class, mockSettings));

        assertNull(mockito.mock(null, mockSettings));

        try {
            mockito.mock(String.class, mockSettings);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // expected
        }
    }

    // Test stub and stubbingStarted methods
    @Test
    public void testStub() {
        MockitoCore mockito = new MockitoCore();

        assertNotNull(mockito.stub());

        mockito.stub();

        MockSettings mockSettings = mock(MockSettings.class);
        assertNotNull(mockito.stub(mockSettings));

        assertEquals(mockito.stub(mockSettings).getClass(), DeprecatedOngoingStubbing.class);

        mockito.stub(null);

        mockito.stub();
    }

    // Test when and stubbingStarted methods
    @Test
    public void testWhen() {
        MockitoCore mockito = new MockitoCore();

        assertNotNull(mockito.when(new Object()));

        mockito.when(new Object());

        MockSettings mockSettings = mock(MockSettings.class);
        assertNotNull(mockito.when(mockSettings));

        assertEquals(mockito.when(mockSettings).getClass(), OngoingStubbing.class);

        mockito.when(null);

        mockito.when(new Object());
    }

    // Test verify and verificationStarted methods
    @Test
    public void testVerify() {
        MockitoCore mockito = new MockitoCore();

        assertNotNull(mockito.verify("", times(1)));

        assertEquals(mockito.verify("", times(1)).getClass(), String.class);

        mockito.verify(null, times(1));

        try {
            mockito.verify(new Object(), times(1));
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // expected
        }
    }

    // Test reset method
    @Test
    public void testReset() {
        MockitoCore mockito = new MockitoCore();

        mockito.reset(new Object(), new Object());

        mockito.reset(null, null);
    }

    // Test verifyNoMoreInteractions method
    @Test
    public void testVerifyNoMoreInteractions() {
        MockitoCore mockito = new MockitoCore();

        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        mockito.verifyNoMoreInteractions(mock1, mock2);

        mockito.verifyNoMoreInteractions(null, mock2);

        try {
            mockito.verifyNoMoreInteractions(mock2, new Object());
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            // expected
        }
    }

    // Test verifyNoMoreInteractionsInOrder method
    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        MockitoCore mockito = new MockitoCore();

        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        mockito.verifyNoMoreInteractionsInOrder(mock1, mock2, inOrder(mock1, mock2));

        mockito.verifyNoMoreInteractionsInOrder(null, mock2, inOrder(mock1, mock2));
    }

    // Test assertMocksNotEmpty method
    @Test
    public void testAssertMocksNotEmpty() {
        MockitoCore mockito = new MockitoCore();

        Object[] mocks = {mock(Object.class)};

        mockito.assertMocksNotEmpty(mocks);

        mocks = null;
        try {
            mockito.assertMocksNotEmpty(mocks);
            fail("Expected AssertionError");
        } catch (AssertionError e) {
            // expected
        }

        mocks = new Object[0];
        try {
            mockito.assertMocksNotEmpty(mocks);
            fail("Expected AssertionError");
        } catch (AssertionError e) {
            // expected
        }
    }

    // Test inOrder method
    @Test
    public void testInOrder() {
        MockitoCore mockito = new MockitoCore();

        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        assertNotNull(mockito.inOrder(mock1, mock2));

        mockito.inOrder(mock1);

        mockito.inOrder();

        try {
            mockito.inOrder(null, mock2);
            fail("Expected AssertionError");
        } catch (AssertionError e) {
            // expected
        }

        try {
            mockito.inOrder(mock1, null);
            fail("Expected AssertionError");
        } catch (AssertionError e) {
            // expected
        }

        try {
            mockito.inOrder(null, null);
            fail("Expected AssertionError");
        } catch (AssertionError e) {
            // expected
        }
    }

    // Test doAnswer method
    @Test
    public void testDoAnswer() {
        MockitoCore mockito = new MockitoCore();

        assertNotNull(mockito.doAnswer(null));

        mockito.doAnswer(null);
    }

    // Test stubVoid and stubbingStarted methods
    @Test
    public void testStubVoid() {
        MockitoCore mockito = new MockitoCore();

        assertNotNull(mockito.stubVoid(new Object()));

        mockito.stubVoid(new Object());

        mockito.stubVoid(null);
    }

    // Test validateMockitoUsage method
    @Test
    public void testValidateMockitoUsage() {
        MockitoCore mockito = new MockitoCore();

        mockito.validateMockitoUsage();
    }

    // Test getLastInvocation method
    @Test
    public void testGetLastInvocation() {
        MockitoCore mockito = new MockitoCore();

        assertNotNull(mockito.getLastInvocation());

        assertEquals(mockito.getLastInvocation().getClass(), Invocation.class);
    }
}