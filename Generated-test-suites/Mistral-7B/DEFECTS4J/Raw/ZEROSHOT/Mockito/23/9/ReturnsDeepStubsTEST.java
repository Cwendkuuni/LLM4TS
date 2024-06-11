package org.mockito.internal.stubbing.defaultanswers.test;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.internal.reflection.MockUtil;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.GenericMetadataSupport;
import org.mockito.internal.util.reflection.GenericInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.AnswerWithContext;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private InternalMockHandler<Object> mockHandler;
 private InvocationContainerImpl container;

 @Before
 public void setup() {
 mockHandler = new MockUtil().getMockHandler(mock(Object.class));
 container = (InvocationContainerImpl) mockHandler.getInvocationContainer();
 returnsDeepStubs = new ReturnsDeepStubs();
 }

 @After
 public void tearDown() {
 reset(mockHandler);
 }

 @Test
 public void shouldReturnSameInstanceForSameInvocation() {
 Object mock = mock(Map.class, new ReturnsDeepStubs());

 ArgumentCaptor<InvocationOnMock> invocationCaptor = ArgumentCaptor.forClass(InvocationOnMock.class);

 given(mock.entrySet().iterator().next()).willReturn(mapEntry("key1", 1));
 given(mock.entrySet().iterator().next().getValue()).willReturn(1);
 given(mock.entrySet().iterator()).willReturn(Collections.singletonList(mockEntry("key1", 1)).iterator());

 InvocationOnMock invocation1 = invoke(mock, 0, new Object[0]);
 InvocationOnMock invocation2 = invoke(mock, 0, new Object[0]);

 assertThat(invocation1).isSameAs(invocation2);
 }

 @Test
 public void shouldReturnSameInstanceForNestedInvocation() {
 Object map = mock(Map.class, new ReturnsDeepStubs());
 Object number = mock(Number.class, new ReturnsDeepStubs());

 ArgumentCaptor<InvocationOnMock> mapInvocationCaptor = ArgumentCaptor.forClass(InvocationOnMock.class);
 ArgumentCaptor<InvocationOnMock> numberInvocationCaptor = ArgumentCaptor.forClass(InvocationOnMock.class);

 given(map.entrySet().iterator().next()).willReturn(mapEntry("key1", map));
 given(map.entrySet().iterator().next().getValue()).willReturn(map);
 given(map.entrySet().iterator()).willReturn(Arrays.asList(mapEntry("key1", map)).iterator());

 given(map.entrySet().iterator().next().getValue().iterator().next()).willReturn(number);

 InvocationOnMock mapInvocation = invoke(map, 0, new Object[0]);
 InvocationOnMock numberInvocation = invoke(map, 0, new Object[0], mapInvocation.getArguments());

 assertThat(mapInvocation).isSameAs(mapInvocationCaptor.capture().getValue());
 assertThat(numberInvocation).isSameAs(numberInvocationCaptor.capture().getValue());

 Map.Entry<Object, Object> mapEntry = mapInvocationCaptor.getValue().getArgument(0);
 Number numberFromMap = mapEntry.getValue();
 Number numberFromInvocation = numberInvocationCaptor.getValue().getArgument(0);

 assertThat(numberFromMap).isSameAs(numberFromInvocation);
 }

 @Test
 public void shouldDelegateToEmptyValuesAnswerForNonMockableTypes() {
 Object nonMockableType = mock(String.class);

 ArgumentCaptor<Class<?>> typeCaptor = ArgumentCaptor.forClass(Class.class);

 given(returnsDeepStubs.actualParameterizedType(nonMockableType)).willReturn(typeCaptor.capture());

 Object result = returnsDeepStubs.answer(new InvocationOnMock(nonMockableType, new GenericInvocation("test"), new Object[0]));

 assertThat(typeCaptor.getValue()).isInstanceOf(String.class);
 assertThat(result).isInstanceOf(String.class);
 }

 @Test
 public void shouldRecordDeepStubForSameReturnType() throws Throwable {
 Object mock = mock(Map.class, new ReturnsDeepStubs());
 Object deepStub = mock(Map.class, new ReturnsDeepStubs());

 InvocationOnMock invocation = new InvocationOnMock(mock, new GenericInvocation("get"), new Object[0]);
 container.getStubber().answer(new AnswerWithContext<Map>() {
 @Override
 public Map answer(InvocationOnMock invocationContext) throws Throwable {
 return (Map) deepStub;
 }
 });

 given(mock.get("key")).willReturn( Collections.singletonMap("key", deepStub) );

 Object result = invoke(mock, "key");

 assertEquals(deepStub, result);
 }

 @Test(expected = IllegalStateException.class)
 public void shouldThrowExceptionForDifferentReturnTypes() throws Throwable {
 Object mock = mock(Map.class, new ReturnsDeepStubs());
 Object deepStub1 = mock(Map.class, new ReturnsDeepStubs());
 Object deepStub2 = mock(List.class, new ReturnsDeepStubs());

 InvocationOnMock invocation = new InvocationOnMock(mock, new GenericInvocation("get"), new Object[0]);
 container.getStubber().answer(new AnswerWithContext<Map>() {
 @Override
 public Map answer(InvocationOnMock invocationContext) throws Throwable {
 return (Map) deepStub1;
 }
 });

 given(mock.get("key")).willReturn( Collections.singletonMap("key", deepStub1) );

 try {
 Object result = invoke(mock, "key");
 } finally {
 container.getVerifications().verifyNoMoreInteractions();
 }

 verify(mockHandler.getMock(), times(1)).getMockForConcretes(List.class);
 }
}

class mapEntry<K, V> {
 Map.Entry<K, V> entry;

 mapEntry(K key, V value) {
 this.entry = new AbstractMap.SimpleEntry<>(key, value);
 }
}