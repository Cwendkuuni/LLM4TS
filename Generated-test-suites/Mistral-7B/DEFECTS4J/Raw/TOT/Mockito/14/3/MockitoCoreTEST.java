import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private InOrder inOrder;
 private Object mockObject;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 mockObject = mock(Object.class);
 inOrder = mockitoCore.inOrder(mockObject);
 }

 @Test
 public void testMockCreation() {
 Object mockedObject = mockitoCore.mock(Object.class, new MockSettingsImpl());
 verify(mockObject).mock(ArgumentMatchers.eq(Object.class), ArgumentMatchers.any(MockSettings.class));
 verify(mockitoCore).mockingStarted(ArgumentMatchers.eq(mockedObject), ArgumentMatchers.eq(Object.class), ArgumentMatchers.any(MockSettings.class));
 }

 @Test
 public void testOngoingStubbing() {
 mockitoCore.stub().thenReturn(42);
 verify(mockObject).equals(any(Object.class));
 verify(mockitoCore.stubbing()).thenReturn(42);
 }

 @Test(expected = AssertionError.class)
 public void testStubbingFailure() {
 mockitoCore.stub().thenReturn(mockitoCore.stub());
 }

 @Test
 public void testVerificationMode() {
 mockitoCore.verify(mockObject, Mockito.verifyNoMoreInteractions());
 }

 @Test
 public void testVerificationModeInOrder() {
 Object mockObject1 = mock(Object.class);
 Object mockObject2 = mock(Object.class);
 inOrder.verify(mockObject1).equals(mockObject2);
 inOrder.verify(mockObject2).equals(mockObject1);
 }

 @Test
 public void testAnswer() {
 Answer<String> answer = new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "answer";
 }
 };
 mockitoCore.doAnswer(answer).when(mockObject).toString();
 verify(mockObject).toString();
 }

 @Test
 public void testVoidMethodStubbing() {
 Mockito.doNothing().when(mockObject).doSomething();
 mockObject.doSomething();
 verify(mockObject, never()).doSomething();
 }

 @Test
 public void testValidateMockitoUsage() {
 mockitoCore.validateMockitoUsage();
 }
}