import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMockImpl;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsMatcher;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@RunWith(org.mockito.runners.JUnit4.class)
public class InvocationMatcherTest {

 // Test constructor with empty matchers list
 @Test
 public void testInvocationMatcherEmptyConstructor() {
 InvocationMatcher im = new InvocationMatcher(new InvocationOnMockImpl(new Method("testMethod", new Class[0]), new Object[0]));
 // Check that matchers list is empty
 assert (im.getMatchers().size() == 0);
 }

 // Test constructor with matchers list
 @Test
 public void testInvocationMatcherMatcherListConstructor() {
 InvocationOnMock mock = mock(String.class);
 Object[] args = new Object[]{ "test" };
 Invocation invocation = new InvocationOnMockImpl(new Method("test", String.class), args);
 List<Matcher> matchers = Arrays.asList(Mockito.eq("test"));
 InvocationMatcher im = new InvocationMatcher(invocation, matchers);
 // Check that matchers are set correctly
 for (int i = 0; i < matchers.size(); i++) {
 assert (im.getMatchers().get(i).matches(args[i]));
 }
 }

 // Test getMethod() method
 @Test
 public void testGetMethod() {
 InvocationOnMock mock = mock(String.class);
 Object[] args = new Object[]{ "test" };
 Invocation invocation = new InvocationOnMockImpl(new Method("test", String.class), args);
 InvocationMatcher im = new InvocationMatcher(invocation);
 Method method = im.getMethod();
 // Check that method is set correctly
 assert (method.getName().equals("test"));
 }

 // Test getInvocation() method
 @Test
 public void testGetInvocation() {
 InvocationOnMock mock = mock(String.class);
 Object[] args = new Object[]{ "test" };
 Invocation invocation = new InvocationOnMockImpl(new Method("test", String.class), args);
 InvocationMatcher im = new InvocationMatcher(invocation);
 Invocation invocationFromMatcher = im.getInvocation();
 // Check that invocation is the same as the one passed to the constructor
 assert (invocationFromMatcher.equals(invocation));
 }

 // Test getMatchers() method
 @Test
 public void testGetMatchers() {
 InvocationOnMock mock = mock(String.class);
 Object[] args = new Object[]{ "test" };
 Invocation invocation = new InvocationOnMockImpl(new Method("test", String.class), args);
 List<Matcher> matchers = Arrays.asList(Mockito.eq("test"));
 InvocationMatcher im = new InvocationMatcher(invocation, matchers);
 List<Matcher> matchersFromMatcher = im.getMatchers();
 // Check that matchers are the same as the ones passed to the constructor
 for (int i = 0; i < matchers.size(); i++) {
 assert (matchersFromMatcher.get(i).matches(args[i]));
 assert (matchers.get(i).equals(matchersFromMatcher.get(i)));
 }
 }

 // Test toString() method
 @Test
 public void testToString() {
 InvocationOnMock mock = mock(String.class);
 Object[] args = new Object[]{ "test" };
 Invocation invocation = new InvocationOnMockImpl(new Method("test", String.class), args);
 List<Matcher> matchers = Arrays.asList(Mockito.eq("test"));
 InvocationMatcher im = new InvocationMatcher(invocation, matchers);
 String s = im.toString();
 // Check that toString() returns a string representation of the matcher
 assert (s.contains("InvocationMatcher"));
 assert (s.contains("test"));
 }

 // Test matches() method with same invocation and matchers
 @Test
 public void testMatchesSameInvocationAndMatchers() {
 InvocationOnMock mock = mock(String.class);
 Object[] args = new Object[]{ "test" };
 Invocation invocation = new InvocationOnMockImpl(new Method("test", String.class), args);
 List<Matcher> matchers = Arrays.asList(Mockito.eq("test"));
 InvocationMatcher im = new InvocationMatcher(invocation, matchers);
 Invocation sameInvocation = new InvocationOnMockImpl(invocation);
 boolean result = im.matches(sameInvocation);
 // Check that matches() returns true
 assert (result == true);
 }

 // Test matches() method with different invocations and matchers
 @Test
 public void testMatchesDifferentInvocationsAndMatchers() {
 InvocationOnMock mock1 = mock(String.class);
 InvocationOnMock mock2 = mock(String.class);
 Object[] args1 = new Object[]{ "test1" };
 Object[] args2 = new Object[]{ "test2" };
 Invocation invocation1 = new InvocationOnMockImpl(new Method("test", String.class), args1);
 Invocation invocation2 = new InvocationOnMockImpl(new Method("test", String.class), args2);
 List<Matcher> matchers = Arrays.asList(Mockito.eq("test1"));
 InvocationMatcher im = new InvocationMatcher(invocation1, matchers);
 boolean result = im.matches(invocation2);
 // Check that matches() returns false
 assert (result == false);
 }

 // Test matches() method with same invocation but different matchers
 @Test
 public void testMatchesSameInvocationButDifferentMatchers() {
 InvocationOnMock mock = mock(String.class);
 Object[] args = new Object[]{ "test" };
 Invocation invocation = new InvocationOnMockImpl(new Method("test", String.class), args);
 List<Matcher> matchers1 = Arrays.asList(Mockito.eq("test"));
 List<Matcher> matchers2 = Arrays.asList(Mockito.eq("test2"));
 InvocationMatcher im1 = new InvocationMatcher(invocation, matchers1);
 InvocationMatcher im2 = new InvocationMatcher(invocation, matchers2);
 boolean result = im1.matches(im2);
 // Check that matches() returns false
 assert (result == false);
 }

 // Test hasSimilarMethod() method with same invocation
 @Test
 public void testHasSimilarMethodSameInvocation() {
 InvocationOnMock mock = mock(String.class);
 Object[] args = new Object[]{ "test" };
 Invocation invocation = new InvocationOnMockImpl(new Method("test", String.class), args);
 InvocationMatcher im1 = new InvocationMatcher(invocation);
 InvocationMatcher im2 = new InvocationMatcher(invocation);
 boolean result = im1.hasSimilarMethod(im2);
 // Check that hasSimilarMethod() returns true
 assert (result == true);
 }

 // Test hasSimilarMethod() method with different invocations but same method and arguments
 @Test
 public void testHasSimilarMethodSameMethodAndArgumentsDifferentInvocations() {
 InvocationOnMock mock1 = mock(String.class);
 InvocationOnMock mock2 = mock(String.class);
 Object[] args = new Object[]{ "test" };
 Invocation invocation1 = new InvocationOnMockImpl(new Method("test", String.class), args);
 Invocation invocation2 = new InvocationOnMockImpl(new Method("test", String.class), args);
 InvocationMatcher im1 = new InvocationMatcher(invocation1);
 InvocationMatcher im2 = new InvocationMatcher(invocation2);
 boolean result = im1.hasSimilarMethod(im2);
 // Check that hasSimilarMethod() returns true
 assert (result == true);
 }

 // Test hasSimilarMethod() method with different invocations and different methods
 @Test
 public void testHasSimilarMethodDifferentMethods() {
 InvocationOnMock mock1 = mock(String.class);
 InvocationOnMock mock2 = mock(Integer.class);
 Object[] args1 = new Object[]{ "test" };
 Object[] args2 = new Object[]{ new Integer(1) };
 Invocation invocation1 = new InvocationOnMockImpl(new Method("test", String.class), args1);
 Invocation invocation2 = new InvocationOnMockImpl(new Method("testInt", Integer.class), args2);
 InvocationMatcher im1 = new InvocationMatcher(invocation1);
 InvocationMatcher im2 = new InvocationMatcher(invocation2);
 boolean result = im1.hasSimilarMethod(im2);
 // Check that hasSimilarMethod() returns false
 assert (result == false);
 }

 // Test hasSameMethod() method with same invocations
 @Test
 public void testHasSameMethodSameInvocations() {
 InvocationOnMock mock = mock(String.class);
 Object[] args = new Object[]{ "test" };
 Invocation invocation1 = new InvocationOnMockImpl(new Method("test", String.class), args);
 Invocation invocation2 = new InvocationOnMockImpl(invocation1);
 InvocationMatcher im1 = new InvocationMatcher(invocation1);
 InvocationMatcher im2 = new InvocationMatcher(invocation2);
 boolean result = im1.hasSameMethod(im2);
 // Check that hasSameMethod() returns true
 assert (result == true);
 }

 // Test hasSameMethod() method with same invocations but different matchers
 @Test
 public void testHasSameMethodDifferentMatchers() {
 InvocationOnMock mock = mock(String.class);
 Object[] args = new Object[]{ "test" };
 Invocation invocation = new InvocationOnMockImpl(new Method("test", String.class), args);
 List<Matcher> matchers1 = Arrays.asList(Mockito.eq("test"));
 List<Matcher> matchers2 = Arrays.asList(Mockito.eq("test2"));
 InvocationMatcher im1 = new InvocationMatcher(invocation, matchers1);
 InvocationMatcher im2 = new InvocationMatcher(invocation, matchers2);
 boolean result = im1.hasSameMethod(im2);
 // Check that hasSameMethod() returns false
 assert (result == false);
 }

 // Test captureArgumentsFrom() method with simple arguments
 @Test
 public void testCaptureArgumentsFromSimpleArguments() {
 InvocationOnMock mock = mock(String.class);
 Object[] args = new Object[]{ "test" };
 Invocation invocation = new InvocationOnMockImpl(new Method("test", String.class), args);
 InvocationMatcher im = new InvocationMatcher(invocation);
 // Test capturing a simple argument
 im.captureArgumentsFrom(invocation);
 ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
 verify(mock).test(argumentCaptor.capture());
 String capturedArgument = argumentCaptor.getValue();
 // Check that captured argument is the same as the original argument
 assert (capturedArgument.equals("test"));
 }

 // Test captureArgumentsFrom() method with varargs
 @Test(expected = UnsupportedOperationException.class)
 public void testCaptureArgumentsFromVarargs() {
 InvocationOnMock mock = mock(String[].class);
 Object[] args = new Object[]{ new String[]{"test1", "test2"} };
 Invocation invocation = new InvocationOnMockImpl(new Method("test", String[].class), args);
 InvocationMatcher im = new InvocationMatcher(invocation);
 im.captureArgumentsFrom(invocation);
 }

}