import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*; // For given, when, then, and thenThrow statements

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mockGenericsNest;
 private Object nestedNumber;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 returnsDeepStubs = Mockito.spy(new ReturnsDeepStubs());
 mockGenericsNest = mock(GenericsNest.class);
 nestedNumber = 1;
 }

 @Test
 public void testAnswerReturnsDeepStubForNonMockableType() {
 given(mockGenericsNest.getClass()).willReturn(String.class); // Mocking GenericsNest as String for testing
 when(returnsDeepStubs.answer(Arguments.any(InvocationOnMock.class))).thenAnswer(new ReturnsDeepValues());

 Object result = returnsDeepStubs.answer(mock(InvocationOnMock.class).withAnyArguments());

 then(mockGenericsNest).should(never()).iterator(); // Verifying that iterator was not called

 assertEquals("", result); // Testing that ReturnsEmptyValues is used for non-mockable types
 }

 @Test
 public void testAnswerReturnsDeepStubForMockableType() {
 given(mockGenericsNest.getClass()).willReturn(GenericsNest.class); // Restoring GenericsNest mock

 when(mockGenericsNest.entrySet().iterator().next().getValue().iterator().next())
         .thenReturn(nestedNumber);

 Object result = returnsDeepStubs.answer(mock(InvocationOnMock.class)
         .withAnyArguments()
         .withMock(mockGenericsNest));

 then(mockGenericsNest).iterator().should(times(1)).returning(Collections.singletonMap(ArgumentMatchers.any(), ArgumentMatchers.isA(Number.class)).iterator()); // Verifying that iterator was called once and returned the correct result

 then(mockGenericsNest.entrySet().iterator().next()).should(times(1)).returning(Map.entry(ArgumentMatchers.any(), ArgumentMatchers.isA(Number.class))); // Verifying that entrySet().iterator() was called once and returned the correct entry

 then(mockGenericsNest.entrySet().iterator().next().getValue()).should(times(1)).iterator().returning(Arrays.asList(nestedNumber)); // Verifying that the value of the entry was an Iterable containing the number

 assertEquals(nestedNumber, result); // Testing that the deep stub was returned
 }

 @Test(expected = Throwable.class)
 public void testAnswerThrowsException() {
 given(mockGenericsNest.getClass()).willThrow(new RuntimeException()); // Throwing an exception for testing

 doThrow(new RuntimeException()).when(returnsDeepStubs).answer(any(InvocationOnMock.class));

 // This should cause ReturnsDeepStubs to throw the exception
 returnsDeepStubs.answer(mock(InvocationOnMock.class).withAnyArguments());
 }
}