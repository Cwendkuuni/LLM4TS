import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.naming.MockName;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private MockCandidateFilter nextMock;
 private Collection<Object> mocks;
 private Object fieldInstance;
 private Field field;

 @Before
 public void setup() {
 mocks = new ArrayList<>();
 nextMock = mock(MockCandidateFilter.class);
 filter = new NameBasedCandidateFilter(nextMock);
 fieldInstance = mock(Object.class);
 field = MockUtil.createPrivateField(fieldInstance, "name", String.class);
 }

 @Test
 public void testFilterCandidate_sameName_singleMock() {
 when(nextMock.filterCandidate(mocks, field, fieldInstance)).thenReturn(null);
 MockName mockName = new MockName("MockName");
 when(mockUtil.getMockName(mocks.get(0))).thenReturn(mockName);

 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 verify(nextMock).filterCandidate(mocks, field, fieldInstance);
 verify(mocks.get(0)).equals(result);
 }

 @Test
 public void testFilterCandidate_sameName_multipleMocks() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 mocks.add(mock1);
 mocks.add(mock2);
 when(nextMock.filterCandidate(mocks, field, fieldInstance)).thenReturn(mock1);
 when(mockUtil.getMockName(mock1)).thenReturn(new MockName("MockName1"));
 when(mockUtil.getMockName(mock2)).thenReturn(new MockName("MockName2"));

 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 verify(nextMock, Mockito.never()).filterCandidate(mocks, field, fieldInstance);
 verify(mock1).equals(result);
 }

 @Test
 public void testFilterCandidate_differentName() {
 Object mock = mock(Object.class);
 when(nextMock.filterCandidate(mocks, field, fieldInstance)).thenReturn(null);
 when(mockUtil.getMockName(mock)).thenReturn(new MockName("MockNameDifferent"));

 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 verify(nextMock).filterCandidate(mocks, field, fieldInstance);
 verify(mocks).isEmpty();
 verify(mock).equals(result);
 }

 @Test
 public void testFilterCandidate_sameName_conflictingFields() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 mocks.add(mock1);
 mocks.add(mock2);
 when(nextMock.filterCandidate(mocks, field, fieldInstance)).thenAnswer(new Answer<OngoingInjecter>() {
 @Override
 public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
 return null;
 }
 });
 when(mockUtil.getMockName(mock1)).thenReturn(new MockName("MockName1"));
 when(mockUtil.getMockName(mock2)).thenReturn(new MockName("MockName1"));

 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 verify(nextMock).filterCandidate(mocks, field, fieldInstance);
 verify(mock2).equals(result);
 }
}