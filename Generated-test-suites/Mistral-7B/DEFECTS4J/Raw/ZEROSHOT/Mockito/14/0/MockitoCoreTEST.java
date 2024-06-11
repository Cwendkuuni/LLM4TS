import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito. ArgumentMatcher;
import org.mockito.internal.MockitoCore;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 @Mock private Object mockObject;
 @Captor private ArgumentCaptor<String> stringArgumentCaptor;
 @Captor private ArgumentCaptor<Integer> integerArgumentCaptor;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 mockitoCore = new MockitoCore();
 }

 @Test
 public void testMockCreation() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 verify(mockitoCore).mockingStarted(mock, Object.class, new MockSettingsImpl());
 }

 @Test
 public void testOngoingStubbing() {
 IOngoingStubbing stubbing = mockitoCore.stub();
 when(mockObject).thenReturn("return value");
 verify(mockitoCore).mockingProgress().stubbingStarted();
 verify(mockObject).toString();
 String returnValue = stubbing.thenReturn("return value");
 verify(mockObject).thenReturn(returnValue);
 }

 @Test
 public void testDeprecatedOngoingStubbing() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 DeprecatedOngoingStubbing<Object> stubbing = mockitoCore.stub(mock);
 stubbing.thenReturn("return value");
 verify(mockitoCore).mockingProgress().stubbingStarted();
 verify(mock).toString();
 verify(mock).thenReturn("return value");
 }

 @Test
 public void testOngoingStubbingWithAnswer() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 Stubber stubber = mockitoCore.doAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 int argument = (Integer) invocation.getArgument(0);
 return argument * 2;
 }
 });
 when(mockObject).thenAnswer(stubber).returning(0);
 int result = (int) mockitoCore.getLastInvocation().getArguments()[0];
 verify(mockObject).thenAnswer(stubber).returning(result * 2);
 }

 @Test
 public void testVerification() {
 Mockito.verify(mockObject, Mockito.times(1)).toString();
 }

 @Test(expected = IllegalStateException.class)
 public void testVerifyNullMock() {
 mockitoCore.verify(null, Mockito.any());
 }

 @Test(expected = AssertionError.class)
 public void testVerifyNotAMock() {
 mockitoCore.verify(new Object(), Mockito.any());
 }

 @Test
 public void testVerifyNoMoreInteractions() {
 Object mock1 = mockitoCore.mock(Object.class, new MockSettingsImpl());
 Object mock2 = mockitoCore.mock(Object.class, new MockSettingsImpl());
 mockitoCore.verifyNoMoreInteractions(mock1, mock2);
 }

 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 Object mock1 = mockitoCore.mock(Object.class, new MockSettingsImpl());
 Object mock2 = mockitoCore.mock(Object.class, new MockSettingsImpl());
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 when(mock1.toString()).thenReturn("mock1");
 when(mock2.toString()).thenReturn("mock2");
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), inOrder);
 }

 @Test
 public void testReset() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 mockitoCore.reset(mock);
 verifyNoMoreInteractions(mock);
 }

 @Test
 public void testVerifyOrder() {
 Object mock1 = mockitoCore.mock(Object.class, new MockSettingsImpl());
 Object mock2 = mockitoCore.mock(Object.class, new MockSettingsImpl());
 InOrder inOrder = mockitoCore.inOrder(mock1, mock2);
 when(mock1.toString()).thenReturn("mock1");
 when(mock2.toString()).thenReturn("mock2");
 when(mock1.method1()).thenReturn("result1");
 when(mock2.method1()).thenReturn("result2");
 mockitoCore.verifyOrder(inOrder);
 }

 @Test
 public void testVerifyOrderWithArgument() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 ArgumentMatcher<String> argumentMatcher = arg -> arg.equals("expectedValue");
 InOrder inOrder = mockitoCore.inOrder(mock);
 when(mock.method1()).thenReturn("result1").thenReturn("result2");
 mockitoCore.verifyOrder(inOrder).verify(mock).method1().withArgument(stringArgumentCaptor.capture()).thenReturn("result3");
 verify(mock).method1().withArgument(stringArgumentCaptor.capture()).thenReturn("result3");
 verify(mock).method1().withArgument(stringArgumentCaptor.capture()).thenReturn("result3");
 String capturedValue = stringArgumentCaptor.getValue();
 assertEquals("expectedValue", capturedValue);
 }

 @Test
 public void testVerifyOrderWithGenericArgument() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 InOrder inOrder = mockitoCore.inOrder(mock);
 ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
 when(mock.method1()).thenReturn(1).thenReturn(2).thenReturn(3);
 mockitoCore.verifyOrder(inOrder).verify(mock).method1().withArgument(integerArgumentCaptor.capture()).thenReturn(4);
 verify(mock).method1().withArgument(integerArgumentCaptor.capture()).thenReturn(4);
 verify(mock).method1().withArgument(integerArgumentCaptor.capture()).thenReturn(4);
 int capturedValue = integerArgumentCaptor.getValue();
 assertEquals(1, capturedValue.intValue());
 }

 @Test
 public void testVerifyOrderWithMultipleArguments() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 InOrder inOrder = mockitoCore.inOrder(mock);
 ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
 ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
 when(mock.method1(String.class, Integer.class)).thenReturn("result1", 1, "result2", 2, "result3", 3);
 mockitoCore.verifyOrder(inOrder).verify(mock).method1(stringArgumentCaptor.capture(), integerArgumentCaptor.capture()).thenReturn("result4", 4);
 verify(mock).method1(stringArgumentCaptor.capture(), integerArgumentCaptor.capture()).thenReturn("result4", 4);
 verify(mock).method1(stringArgumentCaptor.capture(), integerArgumentCaptor.capture()).thenReturn("result4", 4);
 String capturedStringValue = stringArgumentCaptor.getValue();
 Integer capturedIntegerValue = integerArgumentCaptor.getValue();
 assertEquals("expectedStringValue", capturedStringValue);
 assertEquals((int) 1, capturedIntegerValue.intValue());
 }

 @Test
 public void testInOrderWithAnswer() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 InOrder inOrder = mockitoCore.inOrder(mock);
 ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
 when(mock.method1(String.class)).thenAnswer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 String argument = (String) invocation.getArgument(0);
 return argument.toUpperCase();
 }
 });
 mockitoCore.verifyOrder(inOrder).verify(mock).method1("expectedStringValue");
 }

 @Test
 public void testVerifyOrderWithAnswerAndArgument() {
 Object mock = mockitoCore.mock(Object.class, new MockSettingsImpl());
 InOrder inOrder = mockitoCore.inOrder(mock);
 ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
 when(mock.method1(String.class)).thenAnswer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 String argument = (String) invocation.getArgument(0);
 return argument.toUpperCase() + "_" + invocation.getMethod().getName();
 }
 });
 mockitoCore.verifyOrder(inOrder).verify(mock).method1("expectedStringValue1").thenReturn("expectedResult1");
 mockitoCore.verifyOrder(inOrder).verify(mock).method1("expectedStringValue2").thenReturn("expectedResult2");
 String result1 = mockitoCore.getLastInvocation().getReturnValue();
 String result2 = mockitoCore.getLastInvocation().getReturnValue();
 assertEquals("expectedStringValue1_method1", result1);
 assertEquals("expectedStringValue2_method1", result2);
 }
}