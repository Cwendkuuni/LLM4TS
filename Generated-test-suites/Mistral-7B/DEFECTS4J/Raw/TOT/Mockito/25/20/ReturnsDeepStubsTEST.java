package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Map<String, Number> map;
 private Number number;

 @Before
 public void setUp() {
 returnsDeepStubs = new ReturnsDeepStubs();
 map = mock(Map.class, new ReturnsDeepStubs());
 number = map.entrySet().iterator().next().getValue().iterator().next();
 }

 @Test
 public void testDeepStubsForMapEntrySet() {
 Map<String, Number> map = mock(Map.class, new ReturnsDeepStubs());

 // typical use case
 assertEquals(1, map.entrySet().iterator().next().getValue().iterator().next().intValue());

 // edge case 1: empty map
 Map<String, Number> emptyMap = Collections.emptyMap();
 assertEquals(null, emptyMap.entrySet().iterator().next());

 // edge case 2: null map
 Map<String, Number> nullMap = null;
 try {
 assertEquals(null, nullMap.entrySet().iterator().next());
 } catch (NullPointerException e) {
 // expected
 }

 // error scenario 1: non-map type
 Number nonMap = mock(Number.class, new ReturnsEmptyValues());
 try {
 map.entrySet().iterator().next().setValue(nonMap);
 } catch (ClassCastException e) {
 // expected
 }

 // error scenario 2: null value
 map.put(null, null);
 try {
 number = map.entrySet().iterator().next().getValue().iterator().next();
 } catch (NullPointerException e) {
 // expected
 }

 // verification
 verify(map).entrySet();
 }

 @Test
 public void testDeepStubsForNumber() {
 Number number = mock(Number.class, new ReturnsDeepStubs());

 // typical use case
 assertEquals(1, number.intValue());

 // edge case 1: empty number
 Number emptyNumber = Number.class.cast(mock(Serializable.class));
 assertEquals(0, emptyNumber.intValue());

 // edge case 2: null number
 Number nullNumber = null;
 try {
 assertEquals(0, nullNumber.intValue());
 } catch (NullPointerException e) {
 // expected
 }

 // error scenario 1: non-number type
 String nonNumber = mock(String.class);
 try {
 assertEquals(nonNumber.toString().length(), number.intValue());
 } catch (ClassCastException e) {
 // expected
 }

 // error scenario 2: negative number
 Number negativeNumber = mock(Number.class, new Answers() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return -1;
 }
 });
 try {
 assertEquals(1, negativeNumber.intValue());
 } catch (NegativeArraySizeException e) {
 // expected
 }

 // verification
 verify(number).intValue();
 }
}