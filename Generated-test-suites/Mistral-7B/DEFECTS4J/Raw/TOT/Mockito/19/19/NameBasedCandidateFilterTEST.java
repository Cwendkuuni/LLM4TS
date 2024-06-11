import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private Object mock1, mock2;

 @Before
 public void setup() {
 mock1 = mock(Object.class, "mock1");
 mock2 = mock(Object.class, "mock2");
 filter = new NameBasedCandidateFilter(new NameBasedCandidateFilter(new NameBasedCandidateFilter(new StubFilter()))) {
 };
 }

 @Test
 public void testFilterCandidate_MultipleMocksWithSameName_ReturnCorrectMock() {
 List<Object> mocks = new ArrayList<>();
 mocks.add(mock1);
 mocks.add(mock2);

 Field field = Mockito.mock(Field.class);
 Object fieldInstance = Mockito.mock(Object.class);

 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 verify(mock1).equals(result);
 }

 @Test
 public void testFilterCandidate_MultipleMocksWithSameName_VerifyNoOtherMocksReturned() {
 List<Object> mocks = new ArrayList<>();
 mocks.add(mock1);
 mocks.add(mock2);

 Field field = Mockito.mock(Field.class);
 Object fieldInstance = Mockito.mock(Object.class);

 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 assertNull(filter.filterCandidate(mocks, field, fieldInstance));
 }

 @Test
 public void testFilterCandidate_SingleMock_ReturnNull() {
 List<Object> mocks = new ArrayList<>();
 mocks.add(mock1);

 Field field = Mockito.mock(Field.class);
 Object fieldInstance = Mockito.mock(Object.class);

 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 assertNull(result);
 }

 @Test
 public void testFilterCandidate_SingleMock_NextFilterCalled() {
 List<Object> mocks = new ArrayList<>();
 mocks.add(mock1);

 Field field = Mockito.mock(Field.class);
 Object fieldInstance = Mockito.mock(Object.class);

 MockitoAnswer mockitoAnswer = new MockitoAnswer<OngoingInjecter>() {
 @Override
 public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
 throw new UnsupportedOperationException("Not used");
 }
 };

 NameBasedCandidateFilter next = spy(new NameBasedCandidateFilter(new StubFilter()) {
 });

 Mockito.doAnswer(mockitoAnswer).when(next).filterCandidate(Mockito.anyCollection(), Mockito.any(Field.class), Mockito.any(Object.class));

 Object result = filter.filterCandidate(mocks, field, fieldInstance);

 verify(next).filterCandidate(mocks, field, fieldInstance);
 }
}

class StubFilter implements MockCandidateFilter {
 @Override
 public OngoingInjecter filterCandidate(Collection<Object> mocks, Field field, Object fieldInstance) {
 return null;
 }
}