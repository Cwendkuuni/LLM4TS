import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private Object mock1;
 private Object mock2;
 private Object fieldInstance;

 @Before
 public void setUp() {
 mock1 = mock(String.class);
 mock2 = mock(String.class);
 fieldInstance = new Object();
 filter = new NameBasedCandidateFilter(new StubMockCandidateFilter()) {
 @Override
 public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
 throw new UnsupportedOperationException("Not used in test");
 }
 };
 }

 @Test
 public void testFilterCandidate_MocksWithSameName_ReturnsCorrectMock() {
 List<Object> mocks = new ArrayList<>();
 mocks.add(mock1);
 mocks.add(mock2);

 Mockito.doAnswer(new Answer<OngoingInjecter>() {
 @Override
 public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
 Object[] args = invocation.getArguments();
 Field field = (Field) args[1];
 assertEquals(mock1.getClass().getName(), field.getName());
 return null;
 }
 }).when(filter).filterCandidate(mocks, any(Field.class), fieldInstance);

 filter.filterCandidate(mocks, new Field(String.class, "name"), fieldInstance);
 }

 @Test
 public void testFilterCandidate_MocksWithDifferentNames_ReturnsOtherMock() {
 List<Object> mocks = new ArrayList<>();
 mocks.add(mock1);
 mocks.add(mock2);

 Mockito.doAnswer(new Answer<OngoingInjecter>() {
 @Override
 public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
 Object[] args = invocation.getArguments();
 Field field = (Field) args[1];
 assertEquals(mock2.getClass().getName(), field.getName());
 return null;
 }
 }).when(filter).filterCandidate(mocks, new Field(String.class, "name2"), fieldInstance);

 filter.filterCandidate(mocks, new Field(String.class, "name"), fieldInstance);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testFilterCandidate_EmptyMockList_ThrowsException() {
 List<Object> mocks = Collections.emptyList();
 filter.filterCandidate(mocks, new Field(String.class, "name"), fieldInstance);
 }

 @Test
 public void testFilterCandidate_SingleMock_ReturnsMock() {
 List<Object> mocks = new ArrayList<>();
 mocks.add(mock1);

 Mockito.doAnswer(new Answer<OngoingInjecter>() {
 @Override
 public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
 Object[] args = invocation.getArguments();
 Field field = (Field) args[1];
 assertEquals(mock1.getClass().getName(), field.getName());
 return null;
 }
 }).when(filter).filterCandidate(mocks, any(Field.class), fieldInstance);

 filter.filterCandidate(mocks, new Field(String.class, "name"), fieldInstance);
 }

 @Test
 public void testFilterCandidate_FieldWithNullName_ReturnsNull() {
 Field field = new Field(String.class, null);
 filter.filterCandidate(Collections.emptyList(), field, fieldInstance);
 assertEquals(null, filter.next);
 }
}

class StubMockCandidateFilter implements MockCandidateFilter {
 @Override
 public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
 throw new UnsupportedOperationException("Not used in test");
 }
}