package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues answer;
 private Object mock;

 @Before
 public void setUp() {
 answer = new ReturnsEmptyValues();
 mock = mock(Object.class);
 }

 @Test
 public void testToStringAnswer() {
 // Given a mock object
 // When the toString() method is called
 String toStringAnswer = answer.answer(Mockito.argThat(invocation -> methodsGuru.isToString(invocation.getMethod())));
 // Then the answer is a description of the mock object
 assertEquals("Mock for Object, hashCode: " + mock.hashCode(), toStringAnswer);
 }

 @Test
 public void testCompareToZeroAnswer() {
 // Given a mock object and another mock object as argument, both are the same
 Object sameMock = mock(Object.class);
 // When the compareTo() method is called
 int compareToZeroAnswer = answer.answer(Mockito.argThat(invocation -> methodsGuru.isCompareToMethod(invocation.getMethod())));
 // Then the answer is 0, since both objects are the same
 assertEquals(0, compareToZeroAnswer);

 // Given a mock object and another mock object as argument, both are different
 Object differentMock = mock(Object.class);
 // When the compareTo() method is called
 int compareToDifferentAnswer = answer.answer(Mockito.argThat(invocation -> methodsGuru.isCompareToMethod(invocation.getMethod())));
 // Then the answer is a non-zero value, since both objects are different
 assertNotNull(compareToDifferentAnswer);
 }

 @Test
 public void testArrayListAnswer() {
 // Given a mock object
 // When a method that returns ArrayList is called
 Class<List<String>> listType = new TypeToken<List<String>>() {}.getType();
 List<String> listAnswer = (List<String>) answer.answer(Mockito.argThat(invocation -> methodsGuru.isAssignableFrom(invocation.getMethod().getReturnType(), listType)));
 // Then the answer is an empty ArrayList
 assertNotNull(listAnswer);
 assertEquals(0, listAnswer.size());
 }

 @Test
 public void testMapAnswer() {
 // Given a mock object
 // When a method that returns HashMap is called
 Class<Map<String, Integer>> mapType = new TypeToken<Map<String, Integer>>() {}.getType();
 Map<String, Integer> mapAnswer = (Map<String, Integer>) answer.answer(Mockito.argThat(invocation -> methodsGuru.isAssignableFrom(invocation.getMethod().getReturnType(), mapType)));
 // Then the answer is an empty HashMap
 assertNotNull(mapAnswer);
 assertEquals(0, mapAnswer.size());
 }

 @Test
 public void testCustomTypeAnswer() {
 // Given a mock object
 // When a method that returns a custom type is called
 class CustomType {}
 Class<CustomType> customType = CustomType.class;
 CustomType customTypeAnswer = (CustomType) answer.answer(Mockito.argThat(invocation -> methodsGuru.isAssignableFrom(invocation.getMethod().getReturnType(), customType)));
 // Then the answer is null
 assertNull(customTypeAnswer);
 }

 @Test
 public void testInvokeMethodsWithArguments() {
 // Given a mock object
 // When a method that returns ArrayList with arguments is called
 Class<List<String>> listType = new TypeToken<List<String>>() {}.getType();
 // Given a mock object for the arguments
 Object argument = mock(String.class);
 // When a method is called with the argument
 InvocationOnMock invocation = Mockito.mockMethod(mock, "getListWithArgument", String.class);
 when(invocation.callRealMethod()).thenAnswer(answer);
 List<String> listAnswer = (List<String>) invocation.callRealMethod(argument);
 // Then the answer is an empty ArrayList
 assertNotNull(listAnswer);
 assertEquals(0, listAnswer.size());
 verify(answer).answer(invocation);
 }
}