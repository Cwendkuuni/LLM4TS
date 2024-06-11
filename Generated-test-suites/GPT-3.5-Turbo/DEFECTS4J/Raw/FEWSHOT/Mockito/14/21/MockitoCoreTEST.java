package org.mockito.internal;

import org.junit.Assert;
import org.junit.Test;

import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.InOrder;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.MockitoCore;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationMode;
import org.mockito.stubbing.DeprecatedOngoingStubbing;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.invocation.Invocation;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class MockitoCoreTest {

    @Test
    public void testMock() {
        Mockito mockito = new Mockito();
        MockSettings mockSettings = Mockito.mock(MockSettings.class);
        Assert.assertNull(mockito.mock(null, mockSettings));
    }

    @Test
    public void testStub() {
        Mockito mockito = new Mockito();
        Assert.assertNull(mockito.stub());
    }

    @Test
    public void testStubWithValue() {
        Mockito mockito = new Mockito();
        Integer methodCall = 10;
        Assert.assertNull(mockito.stub(methodCall));
    }

    @Test
    public void testWhen() {
        Mockito mockito = new Mockito();
        Integer methodCall = 10;
        Assert.assertNull(mockito.when(methodCall));
    }

    @Test
    public void testVerify() {
        Mockito mockito = new Mockito();
        VerificationMode mode = Mockito.mock(VerificationMode.class);
        Object mock = Mockito.mock(Object.class);
        Assert.assertNull(mockito.verify(mock, mode));
    }

    @Test
    public void testReset() {
        Mockito mockito = new Mockito();
        Object[] mocks = {Mockito.mock(Object.class)};
        mockito.reset(mocks);
    }

    @Test
    public void testResetWithNull() {
        Mockito mockito = new Mockito();
        Object[] mocks = {null};
        mockito.reset(mocks);
    }

    @Test
    public void testVerifyNoMoreInteractions() {
        Mockito mockito = new Mockito();
        Object[] mocks = {Mockito.mock(Object.class)};
        mockito.verifyNoMoreInteractions(mocks);
    }

    @Test
    public void testVerifyNoMoreInteractionsWithNull() {
        Mockito mockito = new Mockito();
        Object[] mocks = {null};
        mockito.verifyNoMoreInteractions(mocks);
    }

    @Test
    public void testVerifyNoMoreInteractionsInOrder() {
        Mockito mockito = new Mockito();
        List<Object> mocks = new ArrayList<>();
        mocks.add(Mockito.mock(Object.class));
        InOrderContext inOrderContext = Mockito.mock(InOrderContext.class);
        mockito.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
    }

    @Test
    public void testInOrder() {
        Mockito mockito = new Mockito();
        Object[] mocks = {Mockito.mock(Object.class)};
        Assert.assertNotNull(mockito.inOrder(mocks));
    }

    @Test
    public void testInOrderWithNull() {
        Mockito mockito = new Mockito();
        Object[] mocks = {null};
        mockito.inOrder(mocks);
    }

    @Test
    public void testDoAnswer() {
        Mockito mockito = new Mockito();
        Answer answer = Mockito.mock(Answer.class);
        Assert.assertNotNull(mockito.doAnswer(answer));
    }

    @Test
    public void testStubVoid() {
        Mockito mockito = new Mockito();
        Object mock = Mockito.mock(Object.class);
        Assert.assertNotNull(mockito.stubVoid(mock));
    }

    @Test
    public void testValidateMockitoUsage() {
        Mockito mockito = new Mockito();
        mockito.validateMockitoUsage();
    }

    @Test
    public void testGetLastInvocation() {
        Mockito mockito = new Mockito();
        Assert.assertNull(mockito.getLastInvocation());
    }

}