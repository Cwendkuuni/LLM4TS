package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private InterfaceA interfaceA;
 private InterfaceB interfaceB;
 private InterfaceC interfaceC;

 @Before
 public void setup() {
 returnsDeepStubs = new ReturnsDeepStubs();
 interfaceA = Mockito.spy(new ClassA(), returnsDeepStubs);
 interfaceB = Mockito.mock(InterfaceB.class, with(Mockito.callRealMethods()));
 interfaceC = Mockito.mock(InterfaceC.class, with(Mockito.callRealMethods()));
 }

 @Test
 public void testDeepStubbingWithGenericClass() {
 // given
 InterfaceA<String> mockInterfaceA = mock(InterfaceA.class, with(Mockito.callRealMethods()));
 InterfaceB mockInterfaceB = mock(InterfaceB.class, with(Mockito.callRealMethods()));
 InterfaceC<List<Integer>> mockInterfaceC = mock(InterfaceC.class, with(Mockito.callRealMethods()));

 // when
 InterfaceA<String> deepStubA = returnsDeepStubs.getDeepStub(interfaceA, new InterfaceA<String>() {}.getClass());
 InterfaceB deepStubB = returnsDeepStubs.getDeepStub(mockInterfaceB, InterfaceB.class);
 InterfaceC<List<Integer>> deepStubC = returnsDeepStubs.getDeepStub(mockInterfaceC, new InterfaceC<List<Integer>>() {}.getClass());

 // then
 assertThat(deepStubA).isSameAs(mockInterfaceA);
 assertThat(deepStubB).isInstanceOf(InterfaceB.class);
 assertThat(deepStubC).isSameAs(mockInterfaceC);

 // test deep stubbing with generic class and method
 List<Integer> list = new ArrayList<>();
 deepStubC.addAll(list);
 verify(mockInterfaceC).addAll(argThat(isA(list.getClass())));
 }

 @Test
 public void testDeepStubbingWithNestedGenerics() {
 // given
 InterfaceD<List<InterfaceE<String>>> interfaceD = Mockito.mock(InterfaceD.class, with(Mockito.callRealMethods()));
 InterfaceE<String> interfaceE = Mockito.mock(InterfaceE.class, with(Mockito.callRealMethods()));

 // when
 InterfaceD<List<InterfaceE<String>>> deepStubD = returnsDeepStubs.getDeepStub(interfaceD, new InterfaceD<List<InterfaceE<String>>>() {}.getClass());

 // then
 assertThat(deepStubD).isSameAs(interfaceD);
 }

 @Test(expected = AssertionError.class)
 public void testDeepStubbingWithNonMockableClass() {
 // given
 Object nonMockableObject = new Object() {};

 // when
 Object deepStubNonMockableObject = returnsDeepStubs.getDeepStub(nonMockableObject, nonMockableObject.getClass());

 // then AssertionError is thrown
 }

 @Test
 public void testDeepStubbingWithCustomAnswer() {
 // given
 InterfaceA<String> mockInterfaceA = mock(InterfaceA.class, with(new ReturnsAnswer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "Custom Answer";
 }
 }));

 // when
 InterfaceA<String> deepStubA = returnsDeepStubs.getDeepStub(mockInterfaceA, new InterfaceA<String>() {}.getClass());

 // then
 assertThat(deepStubA).isSameAs(mockInterfaceA);
 verifyNoMoreInteractions(deepStubA);
 // deep stubbing with custom answer does not affect verification
 }
}

interface InterfaceA<T> {
 T addAll(Collection<? extends T> collection);
}

interface InterfaceB {
}

interface InterfaceC<T> {
 T addAll(Collection<? extends T> collection);
}

interface InterfaceD<T> {
}

interface InterfaceE<T> {
}

class ClassA {
}