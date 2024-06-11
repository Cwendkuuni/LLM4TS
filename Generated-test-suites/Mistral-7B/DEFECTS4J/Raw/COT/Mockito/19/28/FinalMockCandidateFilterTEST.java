import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class FinalMockCandidateFilterTest {

 private Collection<Object> mocks;
 private Field field;
 private Object fieldInstance;
 private FinalMockCandidateFilter filter;

 @Before
 public void setUp() {
 mocks = new ArrayList<>();
 field = Mockito.mock(Field.class);
 fieldInstance = Mockito.mock(Object.class);
 filter = new FinalMockCandidateFilter();
 }

 /**
 * Test method filterCandidate with one mock and a successful property setter injection
 */
 @Test
 public void testFilterCandidateOneMockSuccessPropertySetter() {
 Object mock = Mockito.mock(ArgumentMatchers.anyClass());
 mocks.add(mock);
 when(field.set(fieldInstance, Argument.any())).thenReturn(true);

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 assertSame(mock, result.thenInject());
 }

 /**
 * Test method filterCandidate with one mock and a successful field setter injection
 */
 @Test
 public void testFilterCandidateOneMockSuccessFieldSetter() {
 when(field.set(fieldInstance, Argument.any())).thenThrow(new RuntimeException());
 when(new FieldSetter(fieldInstance, field).set(Argument.any(), Argument.any())).thenReturn(true);

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 assertSame(fieldInstance, result.thenInject());
 }

 /**
 * Test method filterCandidate with no mock and no exception
 */
 @Test
 public void testFilterCandidateNoMockNoException() {
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 assertNull(result.thenInject());
 }

 /**
 * Test method filterCandidate with no mock and an exception
 */
 @Test(expected = MockitoException.class)
 public void testFilterCandidateNoMockException() {
 when(field.set(fieldInstance, Argument.any())).thenThrow(new RuntimeException());
 when(new FieldSetter(fieldInstance, field).set(Argument.any(), Argument.any())).thenReturn(false);

 filter.filterCandidate(mocks, field, fieldInstance);
 }

 /**
 * Test method filterCandidate with multiple mocks and null result
 */
 @Test
 public void testFilterCandidateMultipleMocksNullResult() {
 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 assertNull(result.thenInject());
 }
}