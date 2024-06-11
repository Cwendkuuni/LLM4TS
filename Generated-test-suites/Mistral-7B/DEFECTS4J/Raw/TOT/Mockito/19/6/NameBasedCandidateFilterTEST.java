import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private MockCandidateFilter nextMock;
 private MockUtil mockUtil;

 @Before
 public void setup() {
 filter = new NameBasedCandidateFilter(new StubNextFilter());
 nextMock = Mockito.spy(new StubNextFilter());
 mockUtil = mock(MockUtil.class);
 Whitebox.setInternalValue(filter, "mockUtil", mockUtil);
 }

 @Test
 public void testFilterCandidate_MultipleMocks_MatchingName() {
 // given
 List<Object> mocks = new ArrayList<>();
 mocks.add(mock(MockitoTestClass.class));
 mocks.add(mock(MockitoTestClass.class));
 mocks.add(mock(DifferentClass.class));

 Field field = Whitebox.getInternalState(MockitoTestClass.class, "field");
 Object fieldInstance = new MockitoTestClass();

 // when
 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 // then
 assertEquals(mocks.get(0), result);
 verify(nextMock).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
 }

 @Test
 public void testFilterCandidate_MultipleMocks_DifferentNames() {
 // given
 List<Object> mocks = new ArrayList<>();
 mocks.add(mock(MockitoTestClass.class));
 mocks.add(mock(DifferentClass.class));

 Field field = Whitebox.getInternalState(MockitoTestClass.class, "field");
 Object fieldInstance = new MockitoTestClass();

 // when
 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 // then
 assertEquals(mocks.get(1), result);
 }

 @Test
 public void testFilterCandidate_SingleMock() {
 // given
 List<Object> mocks = new ArrayList<>();
 mocks.add(mock(MockitoTestClass.class));

 Field field = Whitebox.getInternalState(MockitoTestClass.class, "field");
 Object fieldInstance = new MockitoTestClass();

 // when
 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 // then
 assertEquals(mocks.get(0), result);
 }

 @Test
 public void testFilterCandidate_EmptyMockCollection() {
 // given
 List<Object> mocks = new ArrayList<>();

 Field field = Whitebox.getInternalState(MockitoTestClass.class, "field");
 Object fieldInstance = new MockitoTestClass();

 // when
 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 // then
 assertEquals(null, result);
 }

 @Test
 public void testFilterCandidate_NullField() {
 // given
 List<Object> mocks = new ArrayList<>();
 mocks.add(mock(MockitoTestClass.class));

 Object fieldInstance = null;

 // when
 Object result = filter.filterCandidate(mocks, null, fieldInstance);

 // then
 assertEquals(null, result);
 }

 @Test
 public void testFilterCandidate_NullMockCollection() {
 // given
 Field field = Whitebox.getInternalState(MockitoTestClass.class, "field");
 Object fieldInstance = new MockitoTestClass();

 // when
 Object result = filter.filterCandidate(null, field, fieldInstance);

 // then
 assertEquals(nextMock, result);
 }

 @Test
 public void testFilterCandidate_NullFieldInstance() {
 // given
 List<Object> mocks = new ArrayList<>();
 mocks.add(mock(MockitoTestClass.class));

 Field nullField = Whitebox.getInternalState(MockitoTestClass.class, "nullField");

 // when
 Object result = filter.filterCandidate(mocks, nullField, null);

 // then
 assertEquals(nextMock, result);
 }

 private static class StubNextFilter implements MockCandidateFilter {
 @Override
 public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
 throw new UnsupportedOperationException("Stub method");
 }
 }

 private static class MockitoTestClass {
 private String field;
 private String nullField;
 }
}