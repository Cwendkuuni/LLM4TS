import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.MockitoCore;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyOrder;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mock1;
 private Object mock2;
 private Object mock3;
 private Stubber stubber;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 mockitoCore = new MockitoCore();
 mock1 = mock(Object.class);
 mock2 = mock(Object.class);
 mock3 = mock(Object.class);
 }

 @Test
 public void testMockCreation() {
 Object mock = mockitoCore.mock(Object.class, null);
 assertNotNull(mock);
 }

 @Test
 public void testStubbing() {
 stubber = mockitoCore.stub();
 when(mock1).thenReturn("mockReturnValue");
 assertEquals("mockReturnValue", mock1.someMethod());
 }

 @Test
 public void testStubbingWithAnswer() {
 when(mock1).thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 42;
 }
 });
 assertEquals(42, mock1.someMethod());
 }

 @Test
 public void testVerify() {
 verify(mock1).someMethod();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyNullMock() {
 verify(null);
 }

 @Test(expected = IllegalStateException.class)
 public void testVerifyNotAMock() {
 verify(new Object());
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 mockitoCore.verifyNoMoreInteractions(mock1);
 }

 @Test(expected = IllegalStateException.class)
 public void testVerifyNoMoreInteractionsNullMock() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 @Test(expected = IllegalStateException.class)
 public void testVerifyNoMoreInteractionsNotAMock() {
 mockitoCore.verifyNoMoreInteractions(new Object());
 }

 @Test
 public void testVerifyOrder() {
 Object[] mocks = {mock1, mock2};
 InOrder inOrder = mockitoCore.inOrder(mocks);
 when(mock1.someMethod()).thenReturn(1);
 when(mock2.someMethod()).thenReturn(2);
 inOrder.verify(mock1).someMethod();
 inOrder.verify(mock2).someMethod();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyOrderNullMocks() {
 mockitoCore.verifyOrder(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testVerifyOrderEmptyMocks() {
 mockitoCore.verifyOrder(new Object[0]);
 }

 @Test(expected = IllegalStateException.class)
 public void testVerifyOrderNotAMock() {
 Object notAMock = new Object();
 InOrder inOrder = mockitoCore.inOrder(new Object[]{notAMock});
 }

 @Test
 public void testReset() {
 mockitoCore.reset(mock1, mock2);
 verifyNoMoreInteractions(mock1, mock2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testResetNullMocks() {
 mockitoCore.reset(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testResetEmptyMocks() {
 mockitoCore.reset(new Object[0]);
 }

 @Test
 public void testGetLastInvocation() {
 mockitoCore.stubbingStarted();
 when(mock1.someMethod()).thenReturn(1);
 mockitoCore.stubbingStarted();
 when(mock1.someMethod()).thenReturn(2);
 Invocation lastInvocation = mockitoCore.getLastInvocation();
 assertEquals(2, lastInvocation.getArgument(0));
 }

 @Test(expected = IllegalStateException.class)
 public void testGetLastInvocationNoStubbingStarted() {
 mockitoCore.getLastInvocation();
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }

 @Test(expected = IllegalStateException.class)
 public void testStubbingWithDeprecatedMethod() {
 mockitoCore.stub(mock1);
 }

 @Test(expected = IllegalStateException.class)
 public void testStubbingWithVoidMethodStubbable() {
 mockitoCore.stubVoid(mock1);
 }

 @Test(expected = IllegalStateException.class)
 public void testStubbingWithOngoingStubbing() {
 mockitoCore.stubbingStarted();
 mockitoCore.when(mock1).thenReturn("mockReturnValue");
 }
}