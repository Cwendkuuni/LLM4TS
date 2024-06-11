package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues;
 private MyInterface mockedObject;

 @Before
 public void setUp() {
 mockedObject = Mockito.mock(MyInterface.class);
 returnsEmptyValues = Mockito.spy(new ReturnsEmptyValues());
 Mockito.doReturn(returnsEmptyValues).when(mockedObject).answer();
 }

 interface MyInterface {
 Collection<String> getCollection();
 Set<String> getSet();
 Map<String, String> getMap();

 int compareTo(Object o);
 }

 @Test
 public void testCollectionReturn() {
 // typical use case
 List<String> result = mockedObject.getCollection();
 assertThat(result).isEmpty();

 // edge case
 List<String> expected = Collections.singletonList("test");
 Mockito.when(mockedObject.getCollection()).thenReturn(expected);
 List<String> resultWithExpected = mockedObject.getCollection();
 assertThat(resultWithExpected).containsExactly("test");
 }

 @Test
 public void testSetReturn() {
 // typical use case
 Set<String> result = mockedObject.getSet();
 assertThat(result).isEmpty();

 // edge case
 Set<String> expected = new HashSet<String>() {{
 add("test");
 }};
 Mockito.when(mockedObject.getSet()).thenReturn(expected);
 Set<String> resultWithExpected = mockedObject.getSet();
 assertThat(resultWithExpected).contains("test");
 }

 @Test
 public void testMapReturn() {
 // typical use case
 Map<String, String> result = mockedObject.getMap();
 assertThat(result).isEmpty();

 // edge case
 Map<String, String> expected = new LinkedHashMap<String, String>() {{
 put("testKey", "testValue");
 }};
 Mockito.when(mockedObject.getMap()).thenReturn(expected);
 Map<String, String> resultWithExpected = mockedObject.getMap();
 assertThat(resultWithExpected).containsEntry("testKey", "testValue");
 }

 @Test
 public void testCompareToZero() {
 // typical use case
 Object other = new Object();
 int result = mockedObject.compareTo(other);
 assertThat(result).isZero();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCompareToNonZero() {
 // error scenario
 Object other = new Object();
 Mockito.when(mockedObject.compareTo(other)).thenReturn(1);
 int result = mockedObject.compareTo(other);
 }
}