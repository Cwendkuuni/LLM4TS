import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<DummyClass> mockHandler;
 private DummyClass dummy;

 @Before
 public void setUp() {
 dummy = mock(DummyClass.class);
 mockHandler = new MockHandler<>(dummy.getClass());
 }

 @Test
 public void testHandle_noStubbingOrVerification() throws Throwable {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Object result = mockHandler.handle(invocation);

 assertNull(result);
 }

 @Test
 public void testHandle_stubbing() throws Throwable {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(dummy.someMethod(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())).thenReturn("result");

 invocation.invokeMock(dummy, "arg1", 1);
 Object result = mockHandler.handle(invocation);

 assertEquals("result", result);
 }

 @Test
 public void testHandle_verification() throws Throwable {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.invokeMock(dummy, "arg1", 1);
 verify(dummy).someMethod(ArgumentMatchers.eq("arg1"), ArgumentMatchers.eq(1));

 invocation.invokeMock(dummy, "arg2", 2);
 verify(dummy, Mockito.never()).someMethod(ArgumentMatchers.eq("arg2"), ArgumentMatchers.eq(2));
 }

 @Test
 public void testHandle_stubbingAndVerification() throws Throwable {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(dummy.someMethod(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()))
 .thenReturn("result")
 .thenThrow(new RuntimeException("expected exception"));

 invocation.invokeMock(dummy, "arg1", 1);
 Object result = mockHandler.handle(invocation);

 assertEquals("result", result);

 invocation.invokeMock(dummy, "arg1", 1);
 verify(dummy).someMethod(ArgumentMatchers.eq("arg1"), ArgumentMatchers.eq(1));

 invocation.invokeMock(dummy, "arg2", 2);
 verify(dummy, Mockito.never()).someMethod(ArgumentMatchers.eq("arg2"), ArgumentMatchers.eq(2));
 }

 @Test
 public void testHandle_multipleVerifications() throws Throwable {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(dummy.someMethod(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt()))
 .thenReturn("result1")
 .thenReturn("result2");

 invocation.invokeMock(dummy, "arg1", 1);
 Object result1 = mockHandler.handle(invocation);

 assertEquals("result1", result1);

 invocation.invokeMock(dummy, "arg1", 1);
 Object result2 = mockHandler.handle(invocation);

 assertEquals("result2", result2);

 verify(dummy).someMethod(ArgumentMatchers.eq("arg1"), ArgumentMatchers.eq(1));
 verify(dummy, Mockito.times(2)).someMethod(ArgumentMatchers.eq("arg1"), ArgumentMatchers.eq(1));
 }

 @Test
 public void testHandle_voidStubbing() throws Throwable {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 DummyClass dummy2 = mock(DummyClass.class);
 VoidMethodStubbable<DummyClass> voidStub = mockHandler.voidMethodStubbable(dummy2);
 voidStub.doCallRealMethod();
 voidStub.doAnswer(invocation -> {});

 invocation.invokeMock(dummy, "arg1", 1);
 invocation.invokeMock(dummy2, "arg2", 2);
 mockHandler.handle(invocation);

 verify(dummy).someMethod(ArgumentMatchers.eq("arg1"), ArgumentMatchers.eq(1));
 verify(dummy2, Mockito.never()).someMethod(anyObject(), anyInt());
 }

 @Test
 public void testSetAnswersForStubbing() throws Throwable {
 List<Answer> answers = Arrays.asList((Answer) invocation -> invocation.getArgumentAt(0),
 (Answer) invocation -> invocation.getArgumentAt(1) * 2);
 mockHandler.setAnswersForStubbing(answers);

 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.invokeMock(dummy, "arg1", 1);
 Object result1 = mockHandler.handle(invocation);

 assertEquals("arg1", result1);

 invocation.invokeMock(dummy, "arg1", 2);
 Object result2 = mockHandler.handle(invocation);

 assertEquals(2, result2.intValue());
 }
}

class DummyClass {
 public String someMethod(String arg, int arg2) {
 return arg;
 }
}