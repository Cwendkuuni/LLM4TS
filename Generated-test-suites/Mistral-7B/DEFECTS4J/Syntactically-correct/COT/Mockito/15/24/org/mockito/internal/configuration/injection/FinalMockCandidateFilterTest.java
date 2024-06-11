package org.mockito.internal.configuration.injection.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

public class FinalMockCandidateFilterTest {

 private FinalMockCandidateFilter filter;
 private Collection<Object> mocks;
 private Object fieldInstance;
 private Field field;

 @Before
 public void setup() {
 mocks = new ArrayList<>();
 fieldInstance = new Object();
 field = FieldUtils.getField(fieldInstance, "testField");
 filter = new FinalMockCandidateFilter();
 }

 @Test
 public void testFilterCandidate_singleMock_success() {
 Object mock = new Object() {
 @Override
 public void testMethod() {
 // empty implementation
 }
 };
 mocks.add(mock);

 OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);
 Assert.assertTrue(actual.thenInject());
 }

 @Test(expected = MockitoException.class)
 public void testFilterCandidate_singleMock_failure() {
 Object mock = new Object() {
 @Override
 public void testMethod() throws Exception {
 throw new Exception();
 }
 };
 mocks.add(mock);

 try {
 filter.filterCandidate(mocks, field, fieldInstance).thenInject();
 } catch (Exception e) {
 // expected
 }
 }

 @Test
 public void testFilterCandidate_multipleMocks_noMatchingMock_failure() {
 OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);
 Assert.assertFalse(actual.thenInject());
 }

 @Test
 public void testFilterCandidate_multipleMocks_matchingMock_success() {
 Object mock1 = new Object() {
 @Override
 public void testMethod() {
 // empty implementation
 }
 };
 Object mock2 = new Object() {
 @Override
 public void testMethod() {
 // empty implementation
 }
 };
 mocks.add(mock1);
 mocks.add(mock2);

 Object matchingMock = mock1; // change this to mock2 to test the second mock

 OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);
 Assert.assertTrue(actual.thenInject());
 }

 @Test(expected = MockitoException.class)
 public void testFilterCandidate_multipleMocks_nullFieldInstance() {
 filter.filterCandidate(mocks, field, null);
 }

 @Test(expected = MockitoException.class)
 public void testFilterCandidate_multipleMocks_nullField() {
 filter.filterCandidate(mocks, null, fieldInstance);
 }

 @Test(expected = MockitoException.class)
 public void testFilterCandidate_multipleMocks_invalidFieldAccess() {
 try {
 Field invalidField = FieldUtils.getField(fieldInstance, "nonExistentField");
 filter.filterCandidate(mocks, invalidField, fieldInstance);
 } catch (NoSuchFieldException e) {
 // expected
 }
 }
}