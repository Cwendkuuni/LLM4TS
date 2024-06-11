import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<MockitoTestMock> mockHandler;
 private MockitoTestMock mock;

 @Before
 public void setUp() {
 mock = Mockito.mock(MockitoTestMock.class);
 mockHandler = new MockHandler<>();
 }

 @Test
 public void testHandle_StubbingVoidMethod() {
 Invocation invocation = new Invocation(MockitoTestMock.class, "voidMethod");
 InvocationMatcher invocationMatcher = ArgumentMatchers.any();
 mockHandler.setAnswersForStubbing(new ArrayList<Answer>() {{
 add(invocation -> System.out.println("Stubbed void method called"));
}});

 when(mockHandler.handle(Mockito.eq(mock), ArgumentMatchers.eq(invocation))).thenReturn(null);

 mockHandler.voidMethodStubbable(mock).stubVoid("voidMethod");

 mockHandler.handle(new InvocationOnMock(mock, "voidMethod"));

 verify(mock).voidMethod();
 System.out.println("Stubbed void method called");
 }

 @Test
 public void testHandle_Verification() {
 Invocation invocation = new Invocation(MockitoTestMock.class, "methodWithArguments");
 InvocationMatcher invocationMatcher = ArgumentMatchers.eq(1);

 when(mock.methodWithArguments(1)).thenReturn(1);

 mockHandler.handle(new InvocationOnMock(mock, invocation));

 mockHandler.handle(new InvocationOnMock(mock, invocation));

 verify(mock).methodWithArguments(ArgumentMatchers.eq(1));
 }

 @Test(expected = IllegalStateException.class)
 public void testHandle_VerificationWithNoMatchingInvocation() {
 Invocation invocation = new Invocation(MockitoTestMock.class, "nonExistentMethod");
 InvocationMatcher invocationMatcher = ArgumentMatchers.any();

 mockHandler.handle(new InvocationOnMock(mock, invocation));

 verify(mock).nonExistentMethod();
 }
}

class MockitoTestMock {
 void methodWithArguments(int arg) {};
 void voidMethod() {};
}