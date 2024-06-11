import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.naming.Name;
import org.mockito.internal.util.naming.SimpleName;
import org.mockito.invocation.Invocation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.MockUtil.getMockName;
import static org.mockito.internal.util.naming.SimpleName.simpleName;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private MockCandidateFilter nextMock;
 private MockUtil mockUtil;

 @Before
 public void setup() {
 mockUtil = new MockUtil();
 nextMock = new StubMockCandidateFilter();
 filter = new NameBasedCandidateFilter(nextMock);
 }

 @Test
 public void testFilterCandidate_SingleMockAndFieldMatch() {
 // Given
 Object mock = new Object() {};
 String fieldName = simpleName("fieldName").toString();
 Field field = new Field("field", Object.class, mock.getClass());
 Object fieldInstance = mock;

 // When
 OngoingInjecter result = filter.filterCandidate(singletonList(mock), field, fieldInstance);

 // Then
 assertEquals(mock, result.getCandidate());
 }

 @Test
 public void testFilterCandidate_SingleMockAndFieldMismatch() {
 // Given
 Object mock = new Object() {};
 String fieldName = simpleName("wrongFieldName").toString();
 Field field = new Field("field", Object.class, mock.getClass());
 Object fieldInstance = mock;

 // When
 OngoingInjecter result = filter.filterCandidate(singletonList(mock), field, fieldInstance);

 // Then
 assertEquals(null, result);
 }

 @Test
 public void testFilterCandidate_MultipleMocksWithMatchingFieldName() {
 // Given
 Object mock1 = new Object() {};
 Object mock2 = new Object() {};
 String fieldName = simpleName("fieldName").toString();
 Field field = new Field("field", Object.class, mock1.getClass());
 Object fieldInstance1 = mock1;
 Field fieldInstance1 = mock1.getClass().getField("field");
 Field fieldInstance2 = mock2.getClass().getField("field");

 // When
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock1);
 mocks.add(mock2);
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance1);

 // Then
 assertEquals(mock1, result.getCandidate());
 }

 @Test
 public void testFilterCandidate_MultipleMocksWithMismatchedFieldName() {
 // Given
 Object mock1 = new Object() {};
 Object mock2 = new Object() {};
 String fieldName1 = simpleName("fieldName1").toString();
 String fieldName2 = simpleName("fieldName2").toString();
 Field field1 = new Field("field1", Object.class, mock1.getClass());
 Object fieldInstance1 = mock1;
 Field fieldInstance1_1 = mock1.getClass().getField("field1");
 Field fieldInstance2 = mock2.getClass().getField("field2");

 // When
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock1);
 mocks.add(mock2);
 OngoingInjecter result = filter.filterCandidate(mocks, field1, fieldInstance1_1);

 // Then
 assertEquals(mock1, result.getCandidate());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFilterCandidate_NullMocks() {
 // Given
 Collection<Object> nullMocks = null;
 Field field = new Field("field", Object.class, Object.class);
 Object fieldInstance = new Object();

 // When
 filter.filterCandidate(nullMocks, field, fieldInstance);

 // Then
 // Expecting an IllegalArgumentException due to null mocks
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFilterCandidate_NullField() {
 // Given
 Collection<Object> mocks = singletonList(new Object());
 Field nullField = null;
 Object fieldInstance = new Object();
 // When, Then
 assertEquals(null, filter.filterCandidate(mocks, nullField, fieldInstance));
 // Expecting an IllegalArgumentException due to null field
 }

 @Test(expected = NullPointerException.class)
 public void testFilterCandidate_NullFieldInstance() {
 // Given
 Collection<Object> mocks = singletonList(new Object());
 Field field = new Field("field", Object.class, Object.class);
 Object nullFieldInstance = null;
 // When, Then
 assertEquals(null, filter.filterCandidate(mocks, field, nullFieldInstance));
 // Expecting a NullPointerException due to null field instance
 }

 @Test(expected = IllegalAccessException.class)
 public void testFilterCandidate_IllegalAccessExceptionThrown() throws Exception {
 // Given
 Object mock = new Object() {};
 String illegalAccessFieldName = simpleName("illegalAccessField").toString();
 Field illegalAccessField = new Field(illegalAccessFieldName, Object.class, mock.getClass());
 Object fieldInstance = mock;
 // When
 Collection<Object> mocks = singletonList(mock);
 // Then
 assertEquals(null, filter.filterCandidate(mocks, illegalAccessField, fieldInstance));
 // Expecting an IllegalAccessException due to illegal access
 illegalAccessField.setAccessible(true);
 illegalAccessField.set(fieldInstance, new Object());
 }

 @Test
 public void testNextFilterCandidate() {
 // Given
 Object mock = new Object() {};
 String fieldName = simpleName("fieldName").toString();
 Field field = new Field("field", Object.class, mock.getClass());
 Object fieldInstance = mock;
 nextMock.andReturn(new Object()); // Stub next filter to return an object

 // When
 OngoingInjecter result = filter.filterCandidate(singletonList(mock), field, fieldInstance);

 // Then
 assertEquals(nextMock.getFilteredCandidate(), result.getCandidate());
 }

 private Collection<Object> singletonList(Object obj) {
 return new ArrayList<Object>() {{ add(obj); }};
 }
}

class StubMockCandidateFilter implements MockCandidateFilter {
 @Override
 public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
 return new OngoingInjecter(this.getClass().getFilteredCandidate());
 }
}