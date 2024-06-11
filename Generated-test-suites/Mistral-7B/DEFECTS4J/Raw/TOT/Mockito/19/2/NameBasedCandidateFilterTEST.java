import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter.OngoingInjecter;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private MockUtil mockUtil;

 @Before
 public void setUp() {
 filter = new NameBasedCandidateFilter(null);
 mockUtil = spy(new MockUtil());
 }

 @Test
 public void testFilterCandidate_multipleMocksWithSameName_conflictingFields() {
 // given
 Collection<Object> mocks = Arrays.asList(mock(ClassA.class), mock(ClassA.class));
 Field field = MockUtil.getDeclaredField(ClassB.class, "field");
 Object fieldInstance = new ClassB();

 // when
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 // then
 Object expectedResult = mocks.get(0); // the first mock is the one that will be used
 assertEquals(expectedResult, result.getMock());
 }

 @Test
 public void testFilterCandidate_multipleMocksWithSameName_noConflictingFields() {
 // given
 Collection<Object> mocks = Arrays.asList(mock(ClassA.class), mock(ClassB.class));
 Field field = MockUtil.getDeclaredField(ClassB.class, "field");
 Object fieldInstance = new ClassB();

 // when
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 // then
 Object expectedResult = mocks.get(1); // the second mock is the one that will be used
 assertEquals(expectedResult, result.getMock());
 }

 @Test(expected = IllegalStateException.class)
 public void testFilterCandidate_noMocks_shouldThrowException() {
 // given
 Field field = MockUtil.getDeclaredField(ClassB.class, "field");
 Object fieldInstance = new ClassB();

 // when, then
 filter.filterCandidate(Collections.emptyList(), field, fieldInstance);
 }

 @Test
 public void testFilterCandidate_fieldNameMatchesMockName() {
 // given
 Collection<Object> mocks = Arrays.asList(mock(ClassA.class));
 Field field = MockUtil.getDeclaredField(ClassB.class, "field");
 Object fieldInstance = new ClassB();

 // when
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 // then
 Object expectedResult = mocks.get(0);
 assertEquals(expectedResult, result.getMock());
 }

 @Test
 public void testFilterCandidate_fieldNameDoesntMatchMockName() {
 // given
 Collection<Object> mocks = Arrays.asList(mock(ClassA.class));
 Field anotherField = MockUtil.getDeclaredField(ClassB.class, "anotherField");
 Object fieldInstance = new ClassB();

 // when
 OngoingInjecter result = filter.filterCandidate(mocks, anotherField, fieldInstance);

 // then
 assertEquals(null, result);
 }
}