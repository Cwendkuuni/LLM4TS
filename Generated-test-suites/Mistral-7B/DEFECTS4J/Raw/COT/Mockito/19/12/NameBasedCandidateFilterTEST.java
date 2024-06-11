import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class NameBasedCandidateFilterTest {

 private NameBasedCandidateFilter filter;
 private MockCandidateFilter nextMock;
 private Stubber stubber;
 private InvocationOnMock invocation;

 @Before
 public void setUp() {
 nextMock = mock(MockCandidateFilter.class);
 filter = new NameBasedCandidateFilter(nextMock);
 stubber = mockito3(nextMock);
 invocation = mock(InvocationOnMock.class);
 }

 @Test
 public void testFilterCandidateWithSingleMock() {
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(new Object() {});
 Object fieldInstance = new Object();
 Field field = new Field(Object.class, "testField");

 Object result = filter.filterCandidate(mocks, field, fieldInstance);
 assertEquals(result, nextMock);
 }

 @Test
 public void testFilterCandidateWithMultipleMocksHavingSameName() {
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(new Object() {});
 mocks.add(new Object() {});
 Object fieldInstance = new Object();
 Field field = new Field(Object.class, "testField");

 when(nextMock.filterCandidate(anyCollection(), any(Field.class), eq(fieldInstance)))
 .thenAnswer(new Answer<OngoingInjecter>() {
 @Override
 public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
 return mock(OngoingInjecter.class);
 }
 });

 List<Object> mockNameMatches = new ArrayList<>();
 for (Object mock : mocks) {
 if (field.getName().equals(mockUtil.getMockName(mock).toString())) {
 mockNameMatches.add(mock);
 }
 }

 Object result = filter.filterCandidate(mocks, field, fieldInstance);
 assertEquals(result, filter.filterCandidate(mockNameMatches, field, fieldInstance).getMock());
 }

 @Test(expected = AssertionError.class)
 public void testFilterCandidateWithMultipleMocksHavingSameNameThrowsException() {
 Collection<Object> mocks = new ArrayList<>();
 mocks.add(new Object() {});
 mocks.add(new Object() {});
 Object fieldInstance = new Object();
 Field field = new Field(Object.class, "testField");

 when(nextMock.filterCandidate(anyCollection(), any(Field.class), eq(fieldInstance)))
 .thenAnswer(new Answer<OngoingInjecter>() {
 @Override
 public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
 throw new AssertionError("Multiple mocks with the same name found.");
 }
 });

 filter.filterCandidate(mocks, field, fieldInstance);
 }
}