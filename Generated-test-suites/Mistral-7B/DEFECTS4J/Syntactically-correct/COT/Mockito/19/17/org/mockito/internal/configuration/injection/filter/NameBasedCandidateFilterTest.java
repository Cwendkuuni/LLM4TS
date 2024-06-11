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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private MockCandidateFilter nextMock;
 private Stubber stubber;

 @Before
 public void setup() {
 nextMock = mock(MockCandidateFilter.class);
 filter = new NameBasedCandidateFilter(nextMock);
 stubber = mockito3(stubber -> {});
 }

 @Test
 public void testFilterCandidateWithSingleMock() {
 Collection<Object> mocks = new ArrayList<>();
 Object mock = stubber.createMock(Object.class);
 mocks.add(mock);

 Field field = mockito(Field.class);
 Object fieldInstance = new Object();

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 assertEquals(mock, result.getCandidate());
 verify(nextMock).filterCandidate(eq(mocks), eq(field), eq(fieldInstance));
 }

 @Test
 public void testFilterCandidateWithMultipleMocks() {
 Collection<Object> mocks = new ArrayList<>();
 Object mock1 = stubber.createMock(Object.class);
 Object mock2 = stubber.createMock(Object.class);
 mocks.add(mock1);
 mocks.add(mock2);

 Field field = mockito(Field.class);
 Object fieldInstance = new Object();

 List<Object> expectedMockNameMatches = new ArrayList<>();
 expectedMockNameMatches.add(mock1);

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 assertEquals(mock1, result.getCandidate());
 assertEquals(expectedMockNameMatches, result.getConflictingMocks());
 verify(nextMock).filterCandidate(eq(expectedMockNameMatches), eq(field), eq(fieldInstance));

 verifyNoMoreInteractions(nextMock);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFilterCandidateWithEmptyMocksCollection() {
 Collection<Object> mocks = new ArrayList<>();
 Field field = mockito(Field.class);
 Object fieldInstance = new Object();

 filter.filterCandidate(mocks, field, fieldInstance);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFilterCandidateWithNullField() {
 Collection<Object> mocks = new ArrayList<>();
 Object mock = stubber.createMock(Object.class);
 mocks.add(mock);
 Field field = null;
 Object fieldInstance = new Object();

 filter.filterCandidate(mocks, field, fieldInstance);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFilterCandidateWithNullFieldInstance() {
 Collection<Object> mocks = new ArrayList<>();
 Object mock = stubber.createMock(Object.class);
 mocks.add(mock);
 Field field = mockito(Field.class);
 Object fieldInstance = null;

 filter.filterCandidate(mocks, field, fieldInstance);
 }
}