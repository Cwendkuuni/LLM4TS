import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {

 private MockCandidateFilter mockFilter;
 private NameBasedCandidateFilter nameBasedFilter;

 @Before
 public void setUp() {
 mockFilter = mock(MockCandidateFilter.class);
 nameBasedFilter = new NameBasedCandidateFilter(mockFilter);
 }

 @Test
 public void testFilterCandidate_SingleMock_NoConflictingFields() {
 // Given
 Object mock = Mockito.spy(new Object());
 String mockName = mock.getClass().getSimpleName();
 Field field = MockUtil.getField(mock, "fieldName");
 Object fieldInstance = mock;
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock);

 // When
 OngoingInjecter result = nameBasedFilter.filterCandidate(mocks, field, fieldInstance);

 // Then
 assertEquals(mock, result.getCandidate());
 }

 @Test
 public void testFilterCandidate_SingleMock_ConflictingFields() {
 // Given
 Object mock1 = Mockito.spy(new Object());
 Object mock2 = Mockito.spy(new Object());
 String mockName1 = mock1.getClass().getSimpleName();
 String mockName2 = mock2.getClass().getSimpleName();
 Field field = MockUtil.getField(mock1, "fieldName");
 Object fieldInstance = mock1;
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock1);
 mocks.add(mock2);

 // When
 OngoingInjecter result = nameBasedFilter.filterCandidate(mocks, field, fieldInstance);

 // Then
 assertEquals(mock1, result.getCandidate());

 // Check that mock2 was not used
 verifyNoInteractions(mock2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFilterCandidate_NullField() {
 // Given
 Collection<Object> mocks = new ArrayList<>();
 Object fieldInstance = new Object();
 Field nullField = null;

 // When & Then
 throwException(IllegalArgumentException.class, () -> nameBasedFilter.filterCandidate(mocks, nullField, fieldInstance));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFilterCandidate_NullFieldInstance() {
 // Given
 Collection<Object> mocks = new ArrayList<>();
 Field field = MockUtil.getField(mocks.get(0), "fieldName");
 Object nullFieldInstance = null;

 // When & Then
 throwException(IllegalArgumentException.class, () -> nameBasedFilter.filterCandidate(mocks, field, nullFieldInstance));
 }

 @Test
 public void testFilterCandidate_EmptyList() {
 // Given
 Field field = MockUtil.getField(Mockito.spy(new Object()), "fieldName");
 Object fieldInstance = new Object();

 // When
 OngoingInjecter result = nameBasedFilter.filterCandidate(new ArrayList<>(), field, fieldInstance);

 // Then
 assertNull(result);
 }

 @Test
 public void testFilterCandidate_MultipleMocks_NoConflictingFields() {
 // Given
 Object mock1 = Mockito.spy(new Object());
 Object mock2 = Mockito.spy(new Object());
 String mockName1 = mock1.getClass().getSimpleName();
 String mockName2 = mock2.getClass().getSimpleName();
 Field field = MockUtil.getField(mock1, "fieldName");
 Object fieldInstance = mock1;
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock1);
 mocks.add(mock2);

 // When
 OngoingInjecter result = nameBasedFilter.filterCandidate(mocks, field, fieldInstance);

 // Then
 assertEquals(mock1, result.getCandidate());

 // Check that mock2 was not used
 verifyNoInteractions(mock2);
 }

 @Test
 public void testFilterCandidate_MultipleMocks_ConflictingFields() {
 // Given
 Object mock1 = Mockito.spy(new Object());
 Object mock2 = Mockito.spy(new Object());
 Object mock3 = Mockito.spy(new Object());
 String mockName1 = mock1.getClass().getSimpleName();
 String mockName2 = mock2.getClass().getSimpleName();
 String mockName3 = mock3.getClass().getSimpleName();
 Field field = MockUtil.getField(mock1, "fieldName");
 Object fieldInstance = mock1;
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock1);
 mocks.add(mock2);
 mocks.add(mock3);

 // When
 OngoingInjecter result = nameBasedFilter.filterCandidate(mocks, field, fieldInstance);

 // Then
 assertEquals(mock1, result.getCandidate());

 // Check that mock2 and mock3 were not used
 verifyNoInteractions(mock2, mock3);
 }

 private void throwException(Class<? extends Throwable> exceptionClass, Runnable runner) {
 try {
 runner.run();
 } catch (Throwable e) {
 if (!(e.getClass().isAssignableFrom(exceptionClass))) {
 throw new RuntimeException("Unexpected exception: " + e.getClass(), e);
 }
 }
}
}