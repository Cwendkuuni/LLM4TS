package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private Object mock1;
 private Object mock2;
 private Object fieldInstance;

 @Before
 public void setUp() {
 mock1 = mock(Object.class, "mock1");
 mock2 = mock(Object.class, "mock2");
 fieldInstance = new Object();
 filter = new NameBasedCandidateFilter(new DefaultMockCandidateFilter()) {
 @Override
 public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
 return super.filterCandidate(mocks, field, fieldInstance);
 }
 };
 }

 @Test
 public void testFilterCandidate_sameName_multipleMocks_returnsCorrectMock() {
 List<Object> mocks = new ArrayList<>();
 mocks.add(mock1);
 mocks.add(mock2);

 Field field = new Field("testField", Object.class, fieldInstance.getClass());

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 assertEquals(mock1, result.getMock());

 verify(mock1).answer(answer("test"));
 }

 @Test
 public void testFilterCandidate_multipleFieldsWithSameName_returnsCorrectField() {
 Object testObject = spy(fieldInstance.getClass());
 Field field1 = new Field("testField", Object.class, testObject.getClass());
 Field field2 = new Field("testField", Object.class, testObject.getClass());

 List<Field> fields = new ArrayList<>();
 fields.add(field1);
 fields.add(field2);

 Collection<Field> mocks = new ArrayList<>(fields);

 OngoingInjecter result = filter.filterCandidate(mocks, field1, fieldInstance);

 assertEquals(field1, result.getField());
 }

 @Test(expected = IllegalStateException.class)
 public void testFilterCandidate_emptyCollection_throwsException() {
 Collection<Object> mocks = new ArrayList<>();

 OngoingInjecter result = filter.filterCandidate(mocks, new Field("testField", Object.class, fieldInstance.getClass()), fieldInstance);
 }

 @Test
 public void testFilterCandidate_noMatchingName_returnsNull() {
 List<Object> mocks = new ArrayList<>();
 mocks.add(mock1);
 mocks.add(mock2);

 Field field = new Field("wrongName", Object.class, fieldInstance.getClass());

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 assertEquals(null, result);
 }

 private Answer<String> answer(final String test) {
 return new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return test;
 }
 };
}
}