import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private MockCandidateFilter nextFilter;
 private MockUtil mockUtil;

 @Before
 public void setUp() {
 mockUtil = spy(MockUtil.class);
 nextFilter = mock(MockCandidateFilter.class);
 filter = new NameBasedCandidateFilter(nextFilter);
 }

 @Test
 public void testFilterCandidateWithSingleMock() {
 // Given
 Collection<Object> mocks = new ArrayList<>();
 Object fieldInstance = mock(Object.class);
 Field field = mock(Field.class);
 String mockName = "mockName";
 when(mockUtil.getMockName(mock(Object.class))).thenReturn(mockName);

 mocks.add(mock(Object.class));

 // When
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 // Then
 verify(nextFilter).filterCandidate(mocks, field, fieldInstance);
 assertNull(result);
 }

 @Test
 public void testFilterCandidateWithMultipleMocksAndConflictingName() {
 // Given
 Collection<Object> mocks = new ArrayList<>();
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 Object fieldInstance = mock(Object.class);
 Field field = mock(Field.class);
 String mockName1 = "mockName1";
 String mockName2 = "mockName1"; // conflicting name
 when(mockUtil.getMockName(mock1)).thenReturn(mockName1);
 when(mockUtil.getMockName(mock2)).thenReturn(mockName2);

 mocks.add(mock1);
 mocks.add(mock2);

 // When
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 // Then
 verify(nextFilter).filterCandidate(mocks, field, fieldInstance);
 assertNotNull(result);
 // The actual result doesn't matter in this test, as we're just checking the behavior
 // of filterCandidate method when there are conflicting names
 }

 @Test(expected = NullPointerException.class)
 public void testFilterCandidateWithNullCollection() {
 // Given
 Collection<Object> mocks = null;
 Object fieldInstance = mock(Object.class);
 Field field = mock(Field.class);

 // When
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 // Then
 verifyNoMoreInteractions(nextFilter);
 }

 @Test(expected = NullPointerException.class)
 public void testFilterCandidateWithNullField() {
 // Given
 Collection<Object> mocks = new ArrayList<>();
 Object fieldInstance = mock(Object.class);
 Field field = null;

 // When
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 // Then
 verifyNoMoreInteractions(nextFilter);
 }

 @Test(expected = NullPointerException.class)
 public void testFilterCandidateWithNullFieldInstance() {
 // Given
 Collection<Object> mocks = new ArrayList<>();
 Object fieldInstance = null;
 Field field = mock(Field.class);

 // When
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 // Then
 verifyNoMoreInteractions(nextFilter);
 }

 @Test
 public void testFilterCandidateWithMultipleMocksAndNoConflictingName() {
 // Given
 Collection<Object> mocks = new ArrayList<>();
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 Object fieldInstance = mock(Object.class);
 Field field = mock(Field.class);
 String mockName1 = "mockName1";
 String mockName2 = "mockName2";
 when(mockUtil.getMockName(mock1)).thenReturn(mockName1);
 when(mockUtil.getMockName(mock2)).thenReturn(mockName2);

 mocks.add(mock1);
 mocks.add(mock2);

 // When
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 // Then
 verify(nextFilter).filterCandidate(Collections.singletonList(mock1), field, fieldInstance);
 assertNull(result);
 }

 @Test
 public void testFilterCandidateWithSingleMockAndNoNameMatch() {
 // Given
 Collection<Object> mocks = new ArrayList<>();
 Object fieldInstance = mock(Object.class);
 Field field = mock(Field.class);
 String mockName = "wrongMockName";
 when(mockUtil.getMockName(mock(Object.class))).thenReturn(mockName);

 mocks.add(mock(Object.class));

 // When
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 // Then
 verifyNoMoreInteractions(nextFilter);
 assertNull(result);
 }
}