import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private Object mock;
 private Object mock2;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 mock = Mockito.mock(Object.class);
 mock2 = Mockito.mock(Object.class);
 }

 // Test for mock() method
 @Test
 public void testMock() {
 T mockedObject = mockitoCore.mock(Object.class, new MockSettingsImpl());
 assertNotNull(mockedObject);
 verify(mockedObject).toString();
 }

 // Test for stub() method
 @Test
 public void testStub() {
 IOngoingStubbing stubbing = mockitoCore.stub();
 stubbing.thenReturn("Hello World");
 String result = (String) mockitoCore.when(mock).toString();
 assertEquals("Hello World", result);
 }

 // Test for deprecatedStub() method
 @Test
 public void testDeprecatedStub() {
 DeprecatedOngoingStubbing<Object> stubbing = mockitoCore.stub(mock);
 stubbing.thenReturn("Hello World");
 String result = (String) mockitoCore.when(mock).toString();
 assertEquals("Hello World", result);
 }

 // Test for when() method
 @Test
 public void testWhen() {
 OngoingStubbing<Object> stubbing = mockitoCore.when(mock);
 stubbing.thenReturn("Hello World");
 String result = (String) mockitoCore.when(mock).toString();
 assertEquals("Hello World", result);
 }

 // Test for verify() method
 @Test
 public void testVerify() {
 mockitoCore.verify(mock, VerificationMode.times(1));
 }

 // Test for verifyNoMoreInteractions() method
 @Test
 public void testVerifyNoMoreInteractions() {
 mockitoCore.verifyNoMoreInteractions(mock);
 }

 // Test for verifyNoMoreInteractionsInOrder() method
 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 Object[] mocks = {mock, mock2};
 InOrder inOrder = mockitoCore.inOrder(mocks);
 inOrder.verify(mock).toString();
 inOrder.verify(mock2).toString();
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock, mock2), inOrder);
 }

 // Test for reset() method
 @Test
 public void testReset() {
 mockitoCore.reset(mock, mock2);
 verify(mock, VerificationMode.never()).toString();
 verify(mock2, VerificationMode.never()).toString();
 }

 // Test for verify() with null
 @Test(expected = NullPointerException.class)
 public void testVerifyNull() {
 mockitoCore.verify(null, VerificationMode.times(1));
 }

 // Test for verifyNoMoreInteractions() with null
 @Test(expected = NullPointerException.class)
 public void testVerifyNoMoreInteractionsNull() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 // Test for verifyNoMoreInteractionsInOrder() with null
 @Test(expected = NullPointerException.class)
 public void testVerifyNoMoreInteractionsInOrderNull() {
 Object[] mocks = {mock, null};
 InOrder inOrder = mockitoCore.inOrder(mocks);
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock, null), inOrder);
 }

 // Test for verify() with non-mock object
 @Test(expected = org.mockito.exceptions.misusing.NotAMockException.class)
 public void testVerifyNonMock() {
 mockitoCore.verify(new Object() {}, VerificationMode.times(1));
 }

 // Test for verifyNoMoreInteractions() with non-mock object
 @Test(expected = org.mockito.exceptions.misusing.NotAMockException.class)
 public void testVerifyNoMoreInteractionsNonMock() {
 mockitoCore.verifyNoMoreInteractions(new Object());
 }
}