package org.mockito.internal.configuration.injection.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class FinalMockCandidateFilterTest {

 private FinalMockCandidateFilter filter;
 private Collection<Object> mocks;
 private Object fieldInstance;
 private Object mockObject;
 private Field field;

 @Before
 public void setup() {
 mocks = new ArrayList<>();
 fieldInstance = new Object();
 mockObject = new MockObject();
 field = new Field(mockObject.getClass(), "testField");
 mocks.add(mockObject);
 filter = new FinalMockCandidateFilter();
 }

 @Test
 public void testFilterCandidate_OneMock_Success() {
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 Assert.assertTrue(result.thenInject());
 }

 @Test(expected = MockitoException.class)
 public void testFilterCandidate_OneMock_Failure() {
 try {
 filter.filterCandidate(mocks, field, fieldInstance);
 } catch (MockitoException e) {
 // expected
 }
 field.setAccessible(true);
 try {
 new FieldSetter(fieldInstance, field).set(mockObject);
 } catch (Exception e) {
 throw new MockitoException("Problems injecting dependency in testField", e);
 }
 }

 @Test
 public void testFilterCandidate_MultipleMocks_NoMatch() {
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 Assert.assertFalse(result.thenInject());
 }

 @Test(expected = MockitoException.class)
 public void testFilterCandidate_MultipleMocks_Exception() throws Exception {
 FieldSetter setter = new FieldSetter(fieldInstance, field);
 mockObject = new MockObjectWithException();
 mocks.add(mockObject);
 filter.filterCandidate(mocks, field, fieldInstance);
 setter.set(mockObject, "value");
 }
}

class MockObject {
 private String testField;
}

class MockObjectWithException extends MockObject {
 @Override
 public void setTestField(String testField) throws Exception {
 throw new Exception("Test exception");
 }
}