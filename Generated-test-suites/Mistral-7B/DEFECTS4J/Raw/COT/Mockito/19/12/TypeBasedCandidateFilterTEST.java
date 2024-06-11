import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.collections.Arrays;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

public class TypeBasedCandidateFilterTest {

 private TypeBasedCandidateFilter filter;
 private MockCandidateFilter nextMock;

 @Before
 public void setUp() {
 filter = new TypeBasedCandidateFilter(nextMock = MockitoJUnit.mock(MockCandidateFilter.class));
 }

 @Test
 public void testFilterCandidate_EmptyMocks_ReturnsNull() {
 Collection<Object> mocks = new ArrayList<>();
 Field field = new Field("testField", String.class, null, null);
 Object fieldInstance = new Object();

 assertNull(filter.filterCandidate(mocks, field, fieldInstance));
 }

 @Test
 public void testFilterCandidate_NullFieldInstance_ThrowsIllegalArgumentException() {
 Collection<Object> mocks = Arrays.asList(new MockitoStub(), new MockitoStub());
 Field field = new Field("testField", String.class, null, null);

 assertThrows(IllegalArgumentException.class, () -> filter.filterCandidate(mocks, field, null));
 }

 @Test
 public void testFilterCandidate_EmptyMockTypeMatches_ReturnsNextFilter() {
 Collection<Object> mocks = Arrays.asList(new MockitoStub(), new MockitoStub());
 Field field = new Field("testField", String.class, null, null);
 Object fieldInstance = new Object();

 assertSame(nextMock, filter.filterCandidate(mocks, field, fieldInstance));
 }

 @Test
 public void testFilterCandidate_SingleMockTypeMatch_ReturnsIt() {
 Collection<Object> mocks = Arrays.asList(new MockitoStub(), new StringMock());
 Field field = new Field("testField", String.class, null, null);
 Object fieldInstance = new Object();

 assertEquals(new StringMock(), filter.filterCandidate(mocks, field, fieldInstance));
 }

 @Test
 public void testFilterCandidate_MultipleMockTypeMatches_ReturnsNextFilter() {
 Collection<Object> mocks = Arrays.asList(new MockitoStub(), new StringMock(), new StringMock());
 Field field = new Field("testField", String.class, null, null);
 Object fieldInstance = new Object();

 assertSame(nextMock, filter.filterCandidate(mocks, field, fieldInstance));
 }

 @Test
 public void testFilterCandidate_NullMockType_ThrowsIllegalArgumentException() {
 Collection<Object> mocks = Arrays.asList(new MockitoStub(), null);
 Field field = new Field("testField", String.class, null, null);
 Object fieldInstance = new Object();

 assertThrows(IllegalArgumentException.class, () -> filter.filterCandidate(mocks, field, fieldInstance));
 }
}