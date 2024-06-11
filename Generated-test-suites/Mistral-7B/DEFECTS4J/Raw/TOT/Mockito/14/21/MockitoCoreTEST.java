import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {

 private Reporter reporter;
 private MockitoCore mockitoCore;
 private Object mock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 mockitoCore = new MockitoCore();
 }

 @Test
 public void testMockCreation() {
 mock = mockitoCore.mock(String.class, new MockSettingsImpl());
 assertNotNull(mock);
 }

 @Test(expected = NotAMockException.class)
 public void testMockCreationWithNullClass() {
 mockitoCore.mock(null, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testMockCreationWithNullSettings() {
 mockitoCore.mock(String.class, null);
 }

 @Test
 public void testStubbing() {
 mockitoCore.stubbingStarted();
 Stubber stubber = mockitoCore.stub(any(String.class));
 stubber.thenReturn("mockito");
 String result = (String) mockitoCore.verify(mock).toString();
 assertEquals("mockito", result);
 }

 @Test
 public void testStubbingWithAnswer() {
 ArgumentCaptor<String> argCaptor = ArgumentCaptor.forClass(String.class);
 mockitoCore.stubbingStarted();
 Stubber stubber = mockitoCore.stub(mock);
 stubber.thenAnswer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 String input = invocation.getArgument(0);
 return input.toUpperCase();
 }
 });
 verify(mock).printName(eq("John Doe"));
 String result = (String) mockitoCore.verify(mock).getName();
 assertEquals("JOHN DOE", result);
 }

 @Test
 public void testVerification() {
 mockitoCore.verificationStarted(VerificationMode.times(1));
 verify(mock).toString();
 }

 @Test(expected = NotAMockException.class)
 public void testVerificationWithNullMock() {
 mockitoCore.verificationStarted(VerificationMode.times(1));
 verify(null);
 }

 @Test(expected = NotAMockException.class)
 public void testVerificationWithNonMockObject() {
 Object nonMockObject = new Object();
 mockitoCore.verificationStarted(VerificationMode.times(1));
 verify(nonMockObject);
 }

 @Test
 public void testReset() {
 mockitoCore.reset(mock);
 verifyZeroInteractions(mock);
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 String mockString = mockitoCore.mock(String.class, new MockSettingsImpl());
 verifyNoMoreInteractions(mockString);
 }

 @Test(expected = NotAMockException.class)
 public void testVerifyNoMoreInteractionsWithNullMock() {
 verifyNoMoreInteractions(null);
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 String mockString1 = mockitoCore.mock(String.class, new MockSettingsImpl());
 String mockString2 = mockitoCore.mock(String.class, new MockSettingsImpl());
 InOrder inOrder = inOrder(mockString1, mockString2);
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mockString1, mockString2), inOrder);
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrderWithNullMocks() {
 InOrder inOrder = mockitoCore.inOrder(new Object[0]);
 mockitoCore.verifyNoMoreInteractionsInOrder(null, inOrder);
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrderWithNullContext() {
 InOrderContext inOrderContext = null;
 List<Object> mocks = Arrays.asList(mockitoCore.mock(String.class, new MockSettingsImpl()), mockitoCore.mock(String.class, new MockSettingsImpl()));
 mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrderWithNullMocksAndContext() {
 List<Object> mocks = null;
 InOrderContext inOrderContext = mockitoCore.inOrder(new Object[0]);
 mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);
 }

 @Test
 public void testInOrder() {
 String mockString1 = mockitoCore.mock(String.class, new MockSettingsImpl());
 String mockString2 = mockitoCore.mock(String.class, new MockSettingsImpl());
 InOrder inOrder = mockitoCore.inOrder(mockString1, mockString2);
 inOrder.verify(mockString1).printName(eq("John Doe"));
 inOrder.verify(mockString2).printName(eq("Jane Doe"));
 }

 @Test
 public void testInOrderWithNullMock() {
 InOrder inOrder = mockitoCore.inOrder(new Object[0]);
 inOrder.verify(null).printName(eq("John Doe"));
 }

 @Test
 public void testInOrderWithNullMocks() {
 InOrder inOrder = mockitoCore.inOrder(null);
 inOrder.verify(mockitoCore.mock(String.class, new MockSettingsImpl())).printName(eq("John Doe"));
 }

 @Test
 public void testVerifyMode() {
 String mockString = mockitoCore.mock(String.class, new MockSettingsImpl());
 verify(mockString, times(3)).toString();
 }
}