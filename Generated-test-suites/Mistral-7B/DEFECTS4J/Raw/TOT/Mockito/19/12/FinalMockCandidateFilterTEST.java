import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FinalMockCandidateFilterTest {

 private FinalMockCandidateFilter filter;
 private Object fieldInstance;
 private Object mock1, mock2;

 @Before
 public void setup() {
 mock1 = mock(Object.class);
 mock2 = mock(Object.class);
 fieldInstance = mock(Object.class);
 filter = new FinalMockCandidateFilter();
 }

 @Test
 public void testFilterCandidateWithSingleMock() {
 final Field field = Mockito.mock(Field.class);
 final List<Object> mocks = new ArrayList<Object>() {{
 add(mock1);
 }} ;

 ArgumentCaptor<Object> matchingMockCaptor = ArgumentCaptor.forClass(Object.class);
 filter.filterCandidate(mocks, field, fieldInstance);
 OngoingInjecter injecter = Mockito.capture(filter.getFilteredInjecter());
 Mockito.verify(field).set(matchingMockCaptor.capture(Mockito.any()), Mockito.eq(mock1));

 Object matchingMock = matchingMockCaptor.getValue();
 verify(injecter).thenInject();
 assertEquals(matchingMock, injecter.getFiltered());
 }

 @Test
 public void testFilterCandidateWithMultipleMocks() {
 final Field field = Mockito.mock(Field.class);
 final List<Object> mocks = new ArrayList<Object>() {{
 add(mock1);
 add(mock2);
 }} ;

 filter.filterCandidate(mocks, field, fieldInstance);
 OngoingInjecter injecter = Mockito.capture(filter.getFilteredInjecter());
 verify(injecter).thenInject();
 assertNull(injecter.getFiltered());
 }

 @Test(expected = RuntimeException.class)
 public void testFilterCandidateWithException() {
 final Field field = Mockito.mock(Field.class);
 final List<Object> mocks = new ArrayList<Object>() {{
 add(mock1);
 add(mock2);
 }} ;

 doThrow(new RuntimeException("Test Exception")).when(fieldInstance).set(Mockito.any(), Mockito.any());
 filter.filterCandidate(mocks, field, fieldInstance);
 }

 @Test
 public void testGetFilteredInjecter() {
 OngoingInjecter injecter = Mockito.mock(OngoingInjecter.class);
 filter.setFilteredInjecter(injecter);
 assertSame(injecter, filter.getFilteredInjecter());
 }
}