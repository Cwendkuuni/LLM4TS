import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

 private Object genericNest;
 private GenericsNest<Object> genericNestMock;
 private Object number;

 @Before
 public void setUp() {
 genericNest = new GenericsNest<Object>() {};
 genericNestMock = Mockito.spy(genericNest);
 number = 1;
 }

 @Test
 public void testDeepStubOfEntrySetIteratorNextValueIteratorNext() {
 // Given
 Mockito.doAnswer(new ReturnsDeepStubs()).when(genericNestMock).entrySet();
 Mockito.doAnswer(new Answer<Set<Number>>() {
 @Override
 public Set<Number> answer(InvocationOnMock invocation) throws Throwable {
 Set<Number> set = new HashSet<>();
 set.add(number);
 return set;
 }
 }).when(genericNestMock).entrySet().iterator();
 Mockito.doAnswer(new Answer<Number>() {
 @Override
 public Number answer(InvocationOnMock invocation) throws Throwable {
 return number;
 }
 }).when(genericNestMock.entrySet().iterator().next().getValue()).iterator();

 // When
 Object deepStub = genericNestMock.entrySet().iterator().next().getValue().iterator().next();

 // Then
 assertEquals(number, deepStub);
 }

 @Test(expected = IllegalStateException.class)
 public void testDeepStubOfNonMockableType() {
 // Given
 Mockito.doAnswer(new ReturnsDeepStubs()).when(genericNestMock).entrySet();
 // When & Then
 verify(genericNestMock).entrySet();
 verifyNoMoreInteractions(genericNestMock);

 try {
 Object deepStub = genericNestMock.entrySet().iterator().next().getValue().iterator().next();
 fail("Should throw IllegalStateException");
 } catch (IllegalStateException e) {
 // Then
 assertEquals("Cannot mock this class or interface: java.lang.IllegalStateException", e.getMessage());
 }
 }
}