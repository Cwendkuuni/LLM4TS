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
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private MockCandidateFilter nextMock;
 private MockUtil mockUtilMock;
 private Collection<Object> mocks;
 private Object fieldInstance;
 private Field field;

 @Before
 public void setup() {
 mockUtilMock = mock(MockUtil.class);
 nextMock = mock(MockCandidateFilter.class);
 filter = new NameBasedCandidateFilter(nextMock);
 mocks = new ArrayList<>();
 field = mock(Field.class);
 fieldInstance = mock(Object.class);
 }

 @Test
 public void testFilterCandidate_MultipleMocksWithSameName_ReturnsCorrectMock() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 mocks.add(mock1);
 mocks.add(mock2);
 when(mockUtilMock.getMockName(mock1)).thenReturn("MockName1");
 when(mockUtilMock.getMockName(mock2)).thenReturn("MockName1");

 when(nextMock.filterCandidate(mocks, field, fieldInstance))
 .thenAnswer(new Answer<OngoingInjecter>() {
 @Override
 public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
 fail("next filter should not be called");
 return null;
 }
 });

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 assertEquals(mock1, result.getMock());
 }

 @Test
 public void testFilterCandidate_MultipleMocksWithDifferentNames_NextFilterIsCalled() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 mocks.add(mock1);
 mocks.add(mock2);
 when(mockUtilMock.getMockName(mock1)).thenReturn("MockName1");
 when(mockUtilMock.getMockName(mock2)).thenReturn("MockName2");

 when(nextMock.filterCandidate(mocks, field, fieldInstance))
 .thenReturn(mock(OngoingInjecter.class));

 OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
 verify(nextMock, times(1)).filterCandidate(mocks, field, fieldInstance);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFilterCandidate_EmptyMocksCollection() {
 filter.filterCandidate(new ArrayList<>(), field, fieldInstance);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFilterCandidate_NullField() {
 filter.filterCandidate(mocks, null, fieldInstance);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFilterCandidate_NullFieldInstance() {
 filter.filterCandidate(mocks, field, null);
 }
}