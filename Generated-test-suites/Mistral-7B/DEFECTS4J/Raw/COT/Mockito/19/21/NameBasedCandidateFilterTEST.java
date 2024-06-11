import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private MockCandidateFilter next;
 private MockUtil mockUtil;

 @Before
 public void setup() {
 initMocks(this);
 mockUtil = PowerMockito.mock(MockUtil.class);
 next = mock(MockCandidateFilter.class);
 filter = new NameBasedCandidateFilter(next);
 }

 @Test
 public void testFilterCandidate_SingleMock_NoConflict() {
 // Given
 Object mock = mock(Object.class);
 Field mockField = Whitebox.getDeclaredField(mock.getClass(), "testField");
 Object fieldInstance = mock();
 String mockName = mockUtil.getMockName(mock);
 mockField.setAccessible(true);
 mockField.set(mock, mockName);

 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock);

 // When
 Object result = filter.filterCandidate(mocks, mockField, fieldInstance);

 // Then
 assertEquals(mock, result);
 verify(next).filterCandidate(mocks, mockField, fieldInstance);
 }

 @Test
 public void testFilterCandidate_MultipleMocks_NoConflict() {
 // Given
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 Field mockField = Whitebox.getDeclaredField(mock1.getClass(), "testField");
 Object fieldInstance = mock();
 String mockName1 = mockUtil.getMockName(mock1);
 mockField.setAccessible(true);
 mockField.set(mock1, mockName1);

 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock1);
 mocks.add(mock2);

 // When
 Object result = filter.filterCandidate(mocks, mockField, fieldInstance);

 // Then
 assertEquals(mock1, result);
 verify(next).filterCandidate(mocks, mockField, fieldInstance);
 }

 @Test
 public void testFilterCandidate_MultipleMocks_WithConflict() {
 // Given
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 String mockName1 = "mock1";
 String mockName2 = "mock1";
 Field mockField = Whitebox.getDeclaredField(mock1.getClass(), "testField");
 Object fieldInstance = mock();
 mockField.setAccessible(true);
 mockField.set(mock1, mockName1);
 mockField.set(mock2, mockName2);

 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock1);
 mocks.add(mock2);

 // When
 Object result = filter.filterCandidate(mocks, mockField, fieldInstance);

 // Then
 assertEquals(mock1, result);
 verify(next).filterCandidate(mocks, mockField, fieldInstance);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFilterCandidate_NullMocks() {
 // Given
 Collection<Object> mocks = null;

 // When & Then
 filter.filterCandidate(mocks, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFilterCandidate_NullField() {
 // Given
 Collection<Object> mocks = new ArrayList<>();
 Object fieldInstance = mock();
 Field mockField = null;

 // When & Then
 filter.filterCandidate(mocks, mockField, fieldInstance);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFilterCandidate_NullFieldInstance() {
 // Given
 Collection<Object> mocks = new ArrayList<>();
 Field mockField = Whitebox.getDeclaredField(mock.getClass(), "testField");

 // When & Then
 filter.filterCandidate(mocks, mockField, null);
 }
}