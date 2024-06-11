import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.util.MockUtil;
import org.mockito.MockHandlerInterface;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.answer;
import static org.mockito.Mockito.argThat;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 private MockHandlerInterface<Object> mockHandler;
 private Object mock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 mockitoCore = new MockitoCore();
 mockHandler = mockUtil(MockHandlerInterface.class);
 mock = mock(Object.class);
 }

 @Test
 public void testMockCreation() {
 T mock = mockitoCore.mock(Object.class);
 assert mock != null;
 }

 @Test
 public void testStubbing() {
 Object arg = new Object();
 when(mock).thenReturn(arg);
 verify(mock).someMethod();
 assert arg.equals(mockitoCore.getLastInvocation().getArgument(0));
 }

 @Test
 public void testReset() {
 Object arg = "reset";
 when(mock).thenReturn(arg);
 reset(mockitoCore.mockingProgress, mockitoCore.mockUtil, mockitoCore.reporter);
 verify(mock).someMethod();
 assert null == mockitoCore.getLastInvocation();
 }

 @Test
 public void testVerify() {
 verify(mockitoCore.mockUtil).isMock(mock);
 }

 @Test(expected = NullPointerException.class)
 public void testVerifyNull() {
 MockitoCore testMockitoCore = mockitoCore; // assign mockitoCore to a new variable
 verify(testMockitoCore);
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object arg = new Object();
 when(mock).thenReturn(arg);
 verifyNoMoreInteractions(mock);
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsWithNull() {
 Object nullMock = null;
 verifyNoMoreInteractions(nullMock);
 }

 @Test
 public void testInOrder() {
 Object arg1 = new Object();
 Object arg2 = new Object();
 Object arg3 = new Object();
 InOrder inOrder = mockitoCore.inOrder(arg1, arg2, arg3);
 arg1 = new Object(); // change arg1 value
 inOrder.verify(arg1).someMethod();
 inOrder.verify(arg2).someOtherMethod();
 inOrder.verify(arg3).thirdMethod();
 }

 @Test
 public void testAnswer() {
 Object arg = new Object();
 Answer<Object> answer = answer(arg);
 when(mock).thenAnswer(answer);
 verify(mock).someMethod();
 assert arg.equals(mockitoCore.getLastInvocation().getAnswer());
 }

 @Test
 public void testDoAnswer() {
 Object arg = new Object();
 Answer<Object> answer = answer(arg);
 doAnswer(answer).when(mock).someMethod();
 verify(mock).someMethod();
 assert arg.equals(mockitoCore.getLastInvocation().getArgument(0));
 }

 private MockUtil mockUtil(Class<?> clazz) {
 return mockitoCore.mockUtil;
 }
}