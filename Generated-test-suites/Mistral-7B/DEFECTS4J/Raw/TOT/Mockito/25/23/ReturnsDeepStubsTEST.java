package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.InternalMockHandler;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

 private InternalMockHandler<Object> mockHandler;
 private Map<String, Number> map;
 private Number number;

 @Before
 public void setup() {
 mockHandler = new MockUtil().getMockHandler(this);
 map = Mockito.mock(Map.class, Answers.RETURNS_DEEP_STUBS);
 number = map.entrySet().iterator().next().getValue().iterator().next();
 }

 @Test
 public void testReturnsDeepStubsForMapEntryValue() throws Exception {
 // Given
 when(map.entrySet().iterator().next().getValue()).thenReturn(number);
 when(number.iterator().next()).thenReturn(1);

 // When
 Number returnedNumber = map.entrySet().iterator().next().getValue().iterator().next();

 // Then
 assertEquals(1, returnedNumber.intValue());
 assertNotNull(map.entrySet().iterator().next().getValue());
 assertNotNull(map.entrySet().iterator().next().getValue().iterator().next());
 }

 @Test
 public void testReturnsDeepStubsForNestedMapValue() throws Exception {
 // Given
 when(map.entrySet().iterator().next().getValue()).thenReturn(new HashMap<>());
 when(map.entrySet().iterator().next().getValue().iterator().next().getValue()).thenReturn(number);
 when(number.iterator().next()).thenReturn(1);

 // When
 Map<String, Number> deepMap = map.entrySet().iterator().next().getValue();
 Number deepNumber = deepMap.iterator().next().getValue();

 // Then
 assertEquals(1, deepNumber.intValue());
 assertNotNull(deepMap.iterator().next().getValue());
 assertNotNull(deepMap.iterator().next().getValue().iterator().next());
 }

 @Test
 public void testReturnsEmptyValuesForNonMockableType() throws Exception {
 // Given
 when(map.entrySet().iterator().next().getValue()).thenReturn(Collections.emptyList());

 // When
 Object result = map.entrySet().iterator().next().getValue();

 // Then
 assertNotNull(result);
 assertEquals(Collections.emptyList().getClass(), result.getClass());
 }

 @Test(expected = IllegalStateException.class)
 public void testReturnsDeepStubsForInvocationWithNoStubbing() throws Exception {
 // Given
 when(map.entrySet().iterator().next().getValue().iterator().next()).thenReturn(1);

 // When, Then will fail with an IllegalStateException
 map.entrySet().iterator().next().getValue().iterator().next();
 }

 @Test
 public void testGetsMockFromInvocationContainer() throws Exception {
 // Given
 Class<?> clazz = Number[].class;
 Object deepMock = mock(clazz, Answers.RETURNS_DEEP_STUBS);
 InvocationContainerImpl container = new InvocationContainerImpl(mockHandler, new Object() {});
 when(container.getInvocationForStubbing()).thenReturn(new InvocationOnMock(deepMock, null, null, null, new Object[0], new Object[0], null, null, null, 0));

 // When
 Object returnedDeepMock = container.getAnswer(Answer.class, null).answer(new InvocationOnMock(deepMock, null, null, null, new Object[0], new Object[0], null, null, null, 0));

 // Then
 assertEquals(deepMock, returnedDeepMock);
 }
}