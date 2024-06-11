package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito. stubbing.Answer;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.internal.util.reflection.GenericMetadataSupport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mock1;
 private Object mock2;
 private Object mock3;

 @Before
 public void setUp() {
 returnsDeepStubs = new ReturnsDeepStubs();
 mock1 = Mockito.mock(Object.class, returnsDeepStubs);
 mock2 = Mockito.mock(Object.class, returnsDeepStubs);
 mock3 = Mockito.mock(List.class, returnsDeepStubs);
 }

 @Test
 public void testAnswer_ClassLevelMock() throws Exception {
 Object result = ((Answer<Object>) returnsDeepStubs).answer(Mockito.argThat(invocation -> "getClass".equals(invocation.getMethod().getName())));
 assertEquals(mock1.getClass(), result);
 }

 @Test
 public void testAnswer_MethodLevelMock() throws Exception {
 Object result = returnsDeepStubs.answer(Mockito.argThat(invocation -> "getClass".equals(invocation.getMethod().getName())), mock1);
 assertEquals(mock1.getClass(), result);
 }

 @Test
 public void testRecordDeepStubMock_SameClass() throws Exception {
 Object result1 = ((Answer<Object>) returnsDeepStubs).answer(Mockito.argThat(invocation -> "hashCode".equals(invocation.getMethod().getName())));
 Object result2 = ((Answer<Object>) returnsDeepStubs).answer(Mockito.argThat(invocation -> "hashCode".equals(invocation.getMethod().getName())), mock1);
 assertEquals(result1, result2);
 }

 @Test
 public void testRecordDeepStubMock_DifferentClass() throws Exception {
 Object result1 = returnsDeepStubs.answer(Mockito.argThat(invocation -> "getClass".equals(invocation.getMethod().getName())), mock1);
 Object result2 = returnsDeepStubs.answer(Mockito.argThat(invocation -> "getClass".equals(invocation.getMethod().getName())), mock2);
 assertNotEquals(result1, result2);
 }

 @Test
 public void testRecordDeepStubMock_GenericClass() throws Exception {
 Object mockList = Mockito.mock(List.class, returnsDeepStubs);
 List<Integer> list = (List<Integer>) mockList;
 Object result1 = returnsDeepStubs.answer(ArgumentMatchers.argThat(invocation -> "size".equals(invocation.getMethod().getName()) && List.class.isAssignableFrom(invocation.getMock().getClass())), mockList);
 Object result2 = returnsDeepStubs.answer(ArgumentMatchers.argThat(invocation -> "size".equals(invocation.getMethod().getName()) && List.class.isAssignableFrom(invocation.getMock().getClass())), list);
 assertEquals(result1, result2);
 }

 @Test
 public void testRecordDeepStubMock_NestedGenericClass() throws Exception {
 Object mockMap = Mockito.mock(Map.class, returnsDeepStubs);
 Map<Integer, List<String>> map = (Map<Integer, List<String>>) mockMap;
 Object result1 = returnsDeepStubs.answer(ArgumentMatchers.argThat(invocation -> "entrySet".equals(invocation.getMethod().getName()) && Map.class.isAssignableFrom(invocation.getMock().getClass())), mockMap);
 Object result2 = returnsDeepStubs.answer(ArgumentMatchers.argThat(invocation -> "entrySet".equals(invocation.getMethod().getName()) && Map.class.isAssignableFrom(invocation.getMock().getClass())), map);
 assertEquals(result1, result2);
 List<String> list = (List<String>) ((Map.Entry<Integer, List<String>>) result1.entrySet().iterator().next().getValue()).iterator().next();
 Object result3 = returnsDeepStubs.answer(ArgumentMatchers.argThat(invocation -> "iterator".equals(invocation.getMethod().getName()) && List.class.isAssignableFrom(invocation.getMock().getType())), list);
 Object result4 = returnsDeepStubs.answer(ArgumentMatchers.argThat(invocation -> "iterator".equals(invocation.getMethod().getName()) && List.class.isAssignableFrom(invocation.getMock().getType())), list);
 assertEquals(result3, result4);
 }

 @Test
 public void testRecordDeepStubMock_NestedGenericClassWithWildcard() throws Exception {
 Object mockMap = Mockito.mock(Map.class, returnsDeepStubs);
 Map<?, List<String>> map = (Map<?, List<String>>) mockMap;
 Object result1 = returnsDeepStubs.answer(ArgumentMatchers.argThat(invocation -> "entrySet".equals(invocation.getMethod().getName()) && Map.class.isAssignableFrom(invocation.getMock().getClass())), mockMap);
 Object result2 = returnsDeepStubs.answer(ArgumentMatchers.argThat(invocation -> "entrySet".equals(invocation.getMethod().getName()) && Map.class.isAssignableFrom(invocation.getMock().getClass())), map);
 assertEquals(result1, result2);
 List<String> list = (List<String>) ((Map.Entry<?, List<String>>) result1.entrySet().iterator().next()).getValue();
 Object result3 = returnsDeepStubs.answer(ArgumentMatchers.argThat(invocation -> "iterator".equals(invocation.getMethod().getName()) && List.class.isAssignableFrom(invocation.getMock().getType())), list);
 Object result4 = returnsDeepStubs.answer(ArgumentMatchers.argThat(invocation -> "iterator".equals(invocation.getMethod().getName()) && List.class.isAssignableFrom(invocation.getMock().getType())), list);
 assertEquals(result3, result4);
 }
}