package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answer.ReturnsDeepStubs;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mock1;
 private Object mock2;
 private Object mock3;
 private Object mock4;
 private Object mock5;

 @Before
 public void setup() {
 this.returnsDeepStubs = new ReturnsDeepStubs();
 this.mock1 = mock(Number.class);
 this.mock2 = mock(Map.class, Answers.RETURNS_DEEP_STUBS);
 this.mock3 = mock(Set.class, Answers.RETURNS_DEEP_STUBS);
 this.mock4 = mock(Number.class);
 this.mock5 = mock(Map.class, Answers.RETURNS_DEEP_STUBS);
 }

 @Test
 public void testReturnsDeepStubForSimpleType() {
 when(mock2.entrySet()).thenReturn(Collections.singletonMap(mock1, mock4).entrySet());
 when(mock2.entrySet().iterator().next()).thenReturn(new HashMap.SimpleEntry<>(mock1, mock4));
 when(mock2.entrySet().iterator().next().getValue()).thenReturn(mock4);
 when(mock2.entrySet().iterator().next().getValue().iterator()).thenReturn(Arrays.asList(mock4).iterator());

 verify(mock2.entrySet().iterator().next().getValue().iterator().next()).isInstanceOf(Number.class);
 }

 @Test
 public void testReturnsDeepStubForComplexType() {
 when(mock5.entrySet()).thenReturn(new HashMap<Number, Set<Number>>() {
 @Override
 public Set<Number> get(Number key) {
 return new ArrayList<Number>() {{
 add(mock1);
 add(mock2);
 add(mock3);
 }};
 }}.entrySet());

 verify(mock5.entrySet().iterator().next()).isInstanceOf(Map.Entry.class);
 verify(mock5.entrySet().iterator().next().getValue()).isInstanceOf(Set.class);
 verify(mock5.entrySet().iterator().next().getValue().iterator()).hasNext();
 verify(mock5.entrySet().iterator().next().getValue().iterator().next()).isInstanceOf(Number.class);
 }

 @Test
 public void testReturnsEmptyValuesForNonMockableType() {
 when(mock4.getClass()).thenReturn(String.class);

 verify(returnsDeepStubs).delegate.returnValueFor(String.class);
 }
}