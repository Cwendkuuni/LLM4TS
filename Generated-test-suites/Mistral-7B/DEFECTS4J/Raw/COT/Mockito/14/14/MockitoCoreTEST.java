import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.JUnit4;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class MockitoCoreTest {

 private MockitoCore mockitoCore;
 @Mock private Object mockObject;
 @Mock private Object mockObject2;
 @Mock private List mockList;

 @Before
 public void setup() {
 mockitoCore = new MockitoCore();
 }

 // Testing constructor and mock method
 @Test
 public void testMockitoConstructorAndMockMethod() {
 MockSettings mockSettings = new MockSettingsImpl();
 when(mockitoCore.mock(Mockito.mock(Object.class), mockSettings)).thenReturn(mockObject);
 Object result = mockitoCore.mock(Object.class, mockSettings);
 verify(mockitoCore).mock(ArgumentMatchers.eq(Object.class), ArgumentMatchers.isInstance(mockSettings));
 verify(mockitoCore).mockingStarted(Mockito.eq(mockObject), ArgumentMatchers.eq(Object.class), ArgumentMatchers.isInstance(mockSettings));
 assert(result == mockObject);
 }

 // Testing stub method
 @Test
 public void testStubMethod() {
 IOngoingStubbing stubbing = mockitoCore.stub();
 stubbing.thenReturn("Some String");
 when(mockObject.someMethod()).thenReturn("Some String");
 String result = mockObject.someMethod();
 verify(mockitoCore).stubbingStarted();
 assert(result.equals("Some String"));
 }

 // Testing when method
 @Test
 public void testWhenMethod() {
 IOngoingStubbing stubbing = mockitoCore.stub();
 stubbing.thenReturn("Some String");
 when(mockObject.someMethod()).thenReturn("Some String");
 String result = mockObject.someMethod();
 verify(mockitoCore).stubbingStarted();
 assert(result.equals("Some String"));

 stubbing.thenThrow(new RuntimeException("Test Exception"));
 try {
 mockObject.someMethod();
 } catch (RuntimeException e) {
 assert(e.getMessage().equals("Test Exception"));
 }
 }

 // Testing verify method
 @Test
 public void testVerifyMethod() {
 verify(mockObject).someMethod();
 }

 // Testing verifyNoMoreInteractions method
 @Test
 public void testVerifyNoMoreInteractions() {
 mockitoCore.verify(mockObject, Mockito.never());
 }

 // Testing verifyNoMoreInteractionsInOrder method
 @Test
 public void testVerifyNoMoreInteractionsInOrder() {
 InOrder inOrder = mockitoCore.inOrder(mockObject, mockObject2, mockList);
 when(mockObject.someMethod()).thenReturn("Some String");
 when(mockObject2.someMethod()).thenReturn("Another String");
 when(mockList.add(ArgumentMatchers.any())).thenReturn(true);
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mockObject, mockObject2, mockList), inOrder);
 }

 // Testing reset method
 @Test
 public void testResetMethod() {
 mockitoCore.mock(Object.class, mockSettings).someMethod();
 mockitoCore.reset();
 verify(mockObject, Mockito.never()).someMethod();
 }

 // Testing verifyNoMoreInteractions with null mocks
 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsWithNullMocks() {
 mockitoCore.verifyNoMoreInteractions(null);
 }

 // Testing verify method with null mock
 @Test(expected = AssertionError.class)
 public void testVerifyWithNullMock() {
 mockitoCore.verify(null, Mockito.eq(VerificationMode.times(1)));
 }

 // Testing when method with null object
 @Test(expected = NotAMockException.class)
 public void testWhenMethodWithNullObject() {
 mockitoCore.when(null).someMethod();
 }

 // Testing stub method with null object
 @Test(expected = NotAMockException.class)
 public void testStubMethodWithNullObject() {
 mockitoCore.stub(null).someMethod();
 }

 // Testing verify method with non-mock object
 @Test(expected = NotAMockException.class)
 public void testVerifyMethodWithNonMockObject() {
 mockitoCore.verify(new Object());
 }

 // Testing stub method with non-mock object
 @Test(expected = NotAMockException.class)
 public void testStubMethodWithNonMockObject() {
 mockitoCore.stub(new Object()).someMethod();
 }

 // Testing verifyNoMoreInteractionsInOrder with null mocks
 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsInOrderWithNullMocks() {
 InOrder inOrder = mockitoCore.inOrder(new Object[]{null, mockObject, mockObject2, mockList});
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(null, mockObject, mockObject2, mockList), inOrder);
 }

 // Testing verifyNoMoreInteractionsInOrder with non-mocks
 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsInOrderWithNonMocks() {
 InOrder inOrder = mockitoCore.inOrder(new Object[]{new Object(), mockObject, mockObject2, mockList});
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(new Object(), mockObject, mockObject2, mockList), inOrder);
 }

 // Testing InOrder creation with null mocks
 @Test(expected = AssertionError.class)
 public void testInOrderCreationWithNullMocks() {
 mockitoCore.inOrder(new Object[]{null, mockObject, mockObject2, mockList});
 }

 // Testing InOrder creation with non-mocks
 @Test(expected = AssertionError.class)
 public void testInOrderCreationWithNonMocks() {
 mockitoCore.inOrder(new Object[]{new Object(), mockObject, mockObject2, mockList});
 }

 // Testing verifyNoMoreInteractions with empty mock list
 @Test
 public void testVerifyNoMoreInteractionsWithEmptyMockList() {
 mockitoCore.verifyNoMoreInteractions((Object[]) new Object[0]);
 }

 // Testing verifyNoMoreInteractionsInOrder with empty mock list and inOrder
 @Test(expected = AssertionError.class)
 public void testVerifyNoMoreInteractionsInOrderWithEmptyMockListAndInOrder() {
 InOrder inOrder = mockitoCore.inOrder(new Object[0]);
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(), inOrder);
 }

 // Testing verifyNoMoreInteractions and verifyNoMoreInteractionsInOrder with same mocks
 @Test
 public void testVerifyNoMoreInteractionsAndVerifyNoMoreInteractionsInOrderWithSameMocks() {
 Object mock = mockitoCore.mock(Object.class, mockSettings);
 mockitoCore.verifyNoMoreInteractions(mock);
 InOrder inOrder = mockitoCore.inOrder(mock);
 mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock), inOrder);
 }

 // Testing stubbing with Answer
 @Test
 public void testStubbingWithAnswer() {
 Stubber stubber = mockitoCore.doAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 42;
 }
 });
 when(mockObject.someMethod()).thenAnswer(stubber);
 assertEquals(42, mockObject.someMethod().intValue());
 }
}