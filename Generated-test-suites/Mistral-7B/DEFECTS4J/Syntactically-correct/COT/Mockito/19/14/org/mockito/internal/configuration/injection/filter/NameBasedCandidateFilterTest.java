import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.stubs.Answer;
import org.mockito.stubs.Stub;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private MockCandidateFilter nextFilter;
 private MockUtil mockUtil;

 @Before
 public void setup() {
 mockUtil = mock(MockUtil.class);
 nextFilter = spy(new MockCandidateFilter() {
 @Override
 public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
 return mock(OngoingInjecter.class);
 }
 });
 filter = new NameBasedCandidateFilter(nextFilter);
 }

 @Test
 public void testFilterCandidate_MultipleMocksWithSameName_ReturnsNextFilter() {
 List<Object> mocks = new ArrayList<>();
 mocks.add(mock(Object.class, "mock1"));
 mocks.add(mock(Object.class, "mock2"));
 mocks.add(mock(Object.class, "mock3"));

 Field field = new Field("testField", Object.class, Class.forName("java.lang.Object"));
 Object fieldInstance = new Object();

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 assertEquals(nextFilter, result);
 }

 @Test
 public void testFilterCandidate_SingleMock_ReturnsNextFilter() {
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock(Object.class, "mock1"));

 Field field = new Field("testField", Object.class, Class.forName("java.lang.Object"));
 Object fieldInstance = new Object();

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

 assertEquals(nextFilter, result);
 }

 @Test(expected = NullPointerException.class)
 public void testFilterCandidate_NullCollection_ThrowsException() {
 Field field = new Field("testField", Object.class, Class.forName("java.lang.Object"));
 Object fieldInstance = new Object();

 filter.filterCandidate(null, field, fieldInstance);
 }

 @Test(expected = NullPointerException.class)
 public void testFilterCandidate_NullField_ThrowsException() {
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock(Object.class, "mock1"));

 Filter filter = spy(new NameBasedCandidateFilter(nextFilter));
 Object fieldInstance = new Object();

 filter.filterCandidate(mocks, null, fieldInstance);
 }

 @Test(expected = NullPointerException.class)
 public void testFilterCandidate_NullFieldInstance_ThrowsException() {
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(mock(Object.class, "mock1"));
 Field field = new Field("testField", Object.class, Class.forName("java.lang.Object"));

 Filter filter = spy(new NameBasedCandidateFilter(nextFilter));

 filter.filterCandidate(mocks, field, null);
 }
}