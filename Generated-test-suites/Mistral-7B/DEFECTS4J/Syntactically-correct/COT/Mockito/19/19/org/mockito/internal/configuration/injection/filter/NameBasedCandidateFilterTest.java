import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.naming.Named;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class NameBasedCandidateFilterTest {

 private MockCandidateFilter nextMock;
 private NameBasedCandidateFilter filter;
 private MockUtil mockUtilMock;

 @Before
 public void setUp() {
 nextMock = mock(MockCandidateFilter.class);
 mockUtilMock = spy(new MockUtil());
 filter = new NameBasedCandidateFilter(nextMock);
 }

 @Test
 public void testFilterCandidate_singleMock_noConflict() {
 // Given
 String mockName = "mockName";
 Object mock = mock(Named.class, "mockName");
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock);
 Field field = new Field("testField", String.class, Named.class);
 Object fieldInstance = new Object();

 // When
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 // Then
 assertEquals(mock, result.getCandidate());
 }

 @Test
 public void testFilterCandidate_multipleMocks_noConflict() {
 // Given
 String mockName1 = "mockName1";
 Object mock1 = mock(Named.class, "mockName1");
 String mockName2 = "mockName2";
 Object mock2 = mock(Named.class, "mockName2");
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock1);
 mocks.add(mock2);
 Field field = new Field("testField", String.class, Named.class);
 Object fieldInstance = new Object();

 // When
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 // Then
 assertEquals(mock1, result.getCandidate());
 }

 @Test(expected = IllegalStateException.class)
 public void testFilterCandidate_multipleMocks_conflictingNames() {
 // Given
 String mockName1 = "mockName1";
 Object mock1 = mock(Named.class, "mockName1");
 String mockName2 = "mockName1"; // same name
 Object mock2 = mock(Named.class, "mockName2");
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock1);
 mocks.add(mock2);
 Field field = new Field("testField", String.class, Named.class);
 Object fieldInstance = new Object();

 // When
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 // Then (exception should be thrown)
 }

 @Test
 public void testFilterCandidate_multipleMocks_nextFilterCalled() {
 // Given
 String mockName1 = "mockName1";
 Object mock1 = mock(Named.class, "mockName1");
 String mockName2 = "mockName2";
 Object mock2 = mock(Named.class, "mockName2");
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock1);
 mocks.add(mock2);
 Field field = new Field("testField", String.class, Named.class);
 Object fieldInstance = new Object();

 // When
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 // Then
 nextMock.assertMethodsCalledOnceWith(mocks, field, fieldInstance);
 }
}