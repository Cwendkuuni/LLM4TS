import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@PrepareForTest({NameBasedCandidateFilter.class, MockUtil.class})
public class NameBasedCandidateFilterTest extends PowerMockTestCase {

 private NameBasedCandidateFilter filter;
 private MockCandidateFilter nextFilter;
 private Collection<Object> mocks;
 private Field field;
 private Object fieldInstance;

 @Before
 public void setup() {
 mocks = new ArrayList<>();
 nextFilter = mock(MockCandidateFilter.class);
 filter = new NameBasedCandidateFilter(nextFilter);
 field = MockUtil.class.getDeclaredField("mockName");
 field.setAccessible(true);
 fieldInstance = mock(Object.class);
 }

 @Test
 public void testFilterCandidate_SingleMock_NoConflict() {
 Object mock = mock(Object.class);
 mocks.add(mock);

 when(field.getName()).thenReturn("testFieldName");
 when(mockUtil.getMockName(mock)).thenAnswer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "testFieldName";
 }
 });

 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 verify(nextFilter).filterCandidate(mocks, field, fieldInstance);
 assertEquals(mock, result);
 }

 @Test
 public void testFilterCandidate_MultipleMocks_NoConflict() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 mocks.add(mock1);
 mocks.add(mock2);

 when(field.getName()).thenReturn("testFieldName");
 when(mockUtil.getMockName(mock1)).thenAnswer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "testFieldName";
 }
 });
 when(mockUtil.getMockName(mock2)).thenAnswer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "anotherTestFieldName";
 }
 });

 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 verify(nextFilter).filterCandidate(mocks, field, fieldInstance);
 assertEquals(mock1, result);
 }

 @Test(expected = IllegalStateException.class)
 public void testFilterCandidate_MultipleMocks_Conflict() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 mocks.add(mock1);
 mocks.add(mock2);

 when(field.getName()).thenReturn("testFieldName");
 when(mockUtil.getMockName(mock1)).thenAnswer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "testFieldName";
 }
 });
 when(mockUtil.getMockName(mock2)).thenAnswer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return "testFieldName";
 }
 });

 filter.filterCandidate(mocks, field, fieldInstance);
 }

 @Test
 public void testFilterCandidate_EmptyList() {
 filter.filterCandidate(new ArrayList<>(), field, fieldInstance);
 }
}