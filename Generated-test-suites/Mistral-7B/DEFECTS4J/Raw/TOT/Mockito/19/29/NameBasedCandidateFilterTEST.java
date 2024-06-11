package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private MockUtil mockUtil;
 private Object fieldInstance;
 private Collection<Object> mocks;

 @Before
 public void setUp() {
 mockUtil = spy(new MockUtil());
 filter = new NameBasedCandidateFilter(Mockito.mock(MockCandidateFilter.class)) {
 @Override
 public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
 return super.filterCandidate(mocks, field, fieldInstance);
 }
 };
 fieldInstance = new Object();
 mocks = new ArrayList<>();
 mocks.add(mock(Object.class, "mock1"));
 mocks.add(mock(Object.class, "mock2"));
 mocks.add(mock(Object.class, "mock3"));
 }

 @Test
 public void testFilterCandidate_WhenMocksHaveSameNameAsField_ThenReturnCorrectMock() {
 // Given
 String fieldName = "mock1";
 Field testField = Mockito.spy(new Field("testField", Integer.TYPE, fieldInstance) {
 @Override
 public String toString() {
 return fieldName;
 }
 });

 // When
 OngoingInjecter result = filter.filterCandidate(mocks, testField, fieldInstance);

 // Then
 assertNotNull(result);
 assertEquals(mocks.get(0), result.getMock());
 }

 @Test
 public void testFilterCandidate_WhenMocksHaveDifferentNameThanField_ThenReturnNull() {
 // Given
 String fieldName = "mockDifferent";
 Field testField = new Field("testField", Integer.TYPE, fieldInstance) {
 @Override
 public String toString() {
 return fieldName;
 }
 };

 // When
 OngoingInjecter result = filter.filterCandidate(mocks, testField, fieldInstance);

 // Then
 assertNull(result);
 }

 @Test
 public void testFilterCandidate_WhenMocksHaveSameNameAsAnotherField_ThenReturnCorrectMock() {
 // Given
 String fieldName1 = "mockSame";
 Field testField1 = new Field("testField1", Integer.TYPE, fieldInstance) {
 @Override
 public String toString() {
 return fieldName1;
 }
 };
 String fieldName2 = "mockSame";
 Field testField2 = new Field("testField2", Integer.TYPE, fieldInstance) {
 @Override
 public String toString() {
 return fieldName2;
 }
 };
 mocks.add(mock(Object.class, fieldName1));
 mocks.add(mock(Object.class, fieldName2));

 // When
 OngoingInjecter result = filter.filterCandidate(mocks, testField1, fieldInstance);

 // Then
 assertNotNull(result);
 assertEquals(mocks.get(0), result.getMock());
 }

 @Test
 public void testFilterCandidate_WhenMocksHaveSameNameAsAnotherFieldButUsedAnswerToCheck_ThenReturnCorrectMock() {
 // Given
 String fieldName1 = "mockSameAnswer";
 String fieldName2 = "mockSameAnswer";
 Field testField1 = new Field("testField1", Integer.TYPE, fieldInstance) {
 @Override
 public String toString() {
 return fieldName1;
 }
 };
 Field testField2 = new Field("testField2", Integer.TYPE, fieldInstance) {
 @Override
 public String toString() {
 return fieldName2;
 }
 };
 mocks.add(mock(Object.class, fieldName1));
 mocks.add(mock(Object.class, fieldName2));
 Answer<Object> answer = Mockito.answer(() -> mocks.get(1));
 Mockito.doAnswer(answer).when(mockUtil).getMockName(Mockito.any());

 // When
 OngoingInjecter result = filter.filterCandidate(mocks, testField1, fieldInstance);

 // Then
 assertNotNull(result);
 assertEquals(mocks.get(1), result.getMock());
 }
}