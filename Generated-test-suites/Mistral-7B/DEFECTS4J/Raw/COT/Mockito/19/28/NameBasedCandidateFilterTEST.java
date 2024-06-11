import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private MockCandidateFilter nextFilter;
 private MockUtil mockUtil;
 private Stubber stubber;

 @Before
 public void setUp() {
 mockUtil = mock(MockUtil.class);
 nextFilter = mock(MockCandidateFilter.class);
 filter = new NameBasedCandidateFilter(nextFilter);
 stubber = mockito3(stubber -> {
 // stub for MockUtil.getMockName(mock)
 when(mockUtil.getMockName(any())).thenAnswer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 Object mock = invocation.getArgument(0);
 return mock.toString();
 }
 });
 });
 }

 @Test
 public void testFilterCandidate_NoConflictingMocks() {
 // Given
 List<Object> mocks = new ArrayList<>();
 mocks.add(new Object() {});
 mocks.add(new Object() {});

 Field field = mock(Field.class);
 Object fieldInstance = new Object();

 // When & Then
 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 // There should be no candidate returned if there are no name matches
 assertEquals(nextFilter, result);
 }

 @Test
 public void testFilterCandidate_SingleMockNameMatch() {
 // Given
 List<Object> mocks = new ArrayList<>();
 mocks.add(new Object() {});
 mocks.add(new Object() {
 @Override
 public String toString() {
 return "mockName";
 }
 });

 Field field = mock(Field.class);
 Object fieldInstance = new Object();

 // When & Then
 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 // The expected candidate should be the mock with the matching name
 assertEquals(mocks.get(1), result);
 }

 @Test(expected = IllegalStateException.class)
 public void testFilterCandidate_ConflictingMocks() {
 // Given
 List<Object> mocks = new ArrayList<>();
 mocks.add(new Object() {
 @Override
 public String toString() {
 return "mockName";
 }
 });
 mocks.add(new Object() {
 @Override
 public String toString() {
 return "mockName";
 }
 });

 Field field = mock(Field.class);
 Object fieldInstance = new Object();

 // When & Then
 filter.filterCandidate(mocks, field, fieldInstance);

 // We expect an IllegalStateException to be thrown since there are conflicting mock names
 }
}