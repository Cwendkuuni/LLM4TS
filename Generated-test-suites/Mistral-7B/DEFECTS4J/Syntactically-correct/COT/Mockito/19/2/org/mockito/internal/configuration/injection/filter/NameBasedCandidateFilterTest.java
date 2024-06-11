import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private MockCandidateFilter nextFilter;
 private Stubber stubber;
 private Object fieldInstance;
 private Field field;
 private Collection<Object> mocks;

 @Before
 public void setUp() {
 stubber = mockitoThreeAnswers();
 nextFilter = spy(MockitoThreeAnswerMockCandidateFilter.instance());
 filter = new NameBasedCandidateFilter(nextFilter);
 mocks = new ArrayList<>();
 field = mockitoField("fieldName");
 fieldInstance = new Object();
 }

 @Test
 public void testFilterCandidate_MultipleMocksWithSameName_ReturnsNextFilter() {
 mocks.add(mock("Mock1"));
 mocks.add(mock("Mock1"));
 when(nextFilter.filterCandidate(mocks, field, fieldInstance))
 .thenAnswer(stubber.answers.get(0));

 filter.filterCandidate(mocks, field, fieldInstance);

 verifyNoMoreInteractions(mocks.get(0));
 verifyNoMoreInteractions(mocks.get(1));
 }

 @Test
 public void testFilterCandidate_SingleMock_ReturnsNextFilter() {
 when(nextFilter.filterCandidate(mocks, field, fieldInstance))
 .thenAnswer(stubber.answers.get(1));

 filter.filterCandidate(mocks, field, fieldInstance);

 verifyNoMoreInteractions(mocks.get(0));
 }

 @Test
 public void testFilterCandidate_NoMocks_ReturnsNextFilter() {
 when(nextFilter.filterCandidate(mocks, field, fieldInstance))
 .thenAnswer(stubber.answers.get(2));

 filter.filterCandidate(mocks, field, fieldInstance);

 verifyNoMoreInteractions(nextFilter);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFilterCandidate_NullFieldInstance_ThrowsIllegalArgumentException() {
 filter.filterCandidate(mocks, field, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFilterCandidate_NullCollection_ThrowsIllegalArgumentException() {
 filter.filterCandidate(null, field, fieldInstance);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFilterCandidate_NullField_ThrowsIllegalArgumentException() {
 filter.filterCandidate(mocks, null, fieldInstance);
 }

 private static Object mock(String name) {
 AccessibleObject accessibleObject = MockUtil.findAccessibleField(Object.class, "mockName").setAccessible(true);
 when(accessibleObject.get(mockitoThreeAnswerMockitoMockitoAnswerStub.instance()))
 .thenReturn(name);
 return mockitoThreeAnswerMockitoMockitoAnswerStub.instance();
 }

 private static Field mockitoField(String name) {
 try {
 return Field.class.getDeclaredField(name);
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }
 }

 private static Stubber mockitoThreeAnswers() {
 return mock(Stubber.class);
 }

 private static MockCandidateFilter mockitoThreeAnswerMockCandidateFilter() {
 return mock(MockCandidateFilter.class);
 }
}

class MockitoThreeAnswerMockCandidateFilter implements MockCandidateFilter {
 private static final MockitoThreeAnswerMockCandidateFilter INSTANCE = new MockitoThreeAnswerMockCandidateFilter();

 private MockitoThreeAnswerMockCandidateFilter() {
 }

 public static MockCandidateFilter instance() {
 return INSTANCE;
 }

 @Override
 public OngoingInjecter filterCandidate(Collection<Object> mocks,
 Field field, Object fieldInstance) {
 Object answer = new Object();
 Answer answerAnswer = Mockito.answer(i -> answer);
 when(field.get(fieldInstance)).thenAnswer(answerAnswer);
 return new OngoingInjecter() {
 @Override
 public void inject(InvocationOnMock invocation) {
 invocation.proceed();
 }
 };
 }
}