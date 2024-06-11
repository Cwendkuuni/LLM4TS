import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito. ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsDeepStubsTest {

 private static final class DummyClass<T> implements Comparable<T>, Cloneable, Map<T, Set<Number>> {}

 @Test
 public void testAnswerMethodReturnsDeepStubForMockableTypes() throws Exception {
  DummyClass<Number> mock = mock(DummyClass.class, new ReturnsDeepStubs());
  Number number = mock.entrySet().iterator().next().getValue().iterator().next();

  Object deepStub = ReturnsDeepStubs.this.answer(Mockito.argThat(i -> i.getMock() instanceof DummyClass));

  verify(mock).entrySet();
  verify(mock.entrySet()).iterator();
  verify(mock.entrySet().iterator()).next();
  verify(mock.entrySet().iterator().next()).getValue();
  verify(mock.entrySet().iterator().next().getValue()).iterator();
  verify(mock.entrySet().iterator().next().getValue().iterator()).next();

  assertSame(deepStub, number);
 }

 @Test(expected = IllegalStateException.class)
 public void testAnswerMethodReturnsEmptyValuesForNonMockableTypes() throws Exception {
  String mockString = mock(String.class, new ReturnsEmptyValues());

  when(mockString.length()).thenReturn(5);

  Object emptyValue = ReturnsDeepStubs.this.answer(Mockito.argThat(i -> i.getMock() == mockString));

  verifyZeroInteractions(mockString);

  assertNull(emptyValue);
 }

 @Test
 public void testGetMockMethodReturnsPreviousCreatedMockIfInvocationMatches() throws Exception {
  DummyClass<Number> mock1 = mock(DummyClass.class, new ReturnsDeepStubs());
  DummyClass<Number> mock2 = mock(DummyClass.class, new ReturnsDeepStubs());

  when(mock1.entrySet().iterator().next().getValue().iterator().next()).thenReturn(5);

  Object deepStub1 = ReturnsDeepStubs.this.getMock(
          new InvocationOnMock(Mockito.mock(GenericDeclaration.class), Arrays.asList("entrySet", "iterator", "next", "getValue", "iterator", "next"), null),
          Mockito.mock(GenericMetadataSupport.class));

  Object deepStub2 = ReturnsDeepStubs.this.getMock(
          new InvocationOnMock(mock1, Arrays.asList("entrySet", "iterator", "next", "getValue", "iterator", "next"), null),
          Mockito.mock(GenericMetadataSupport.class));

  assertSame(deepStub1, deepStub2);
 }

 @Test
 public void testGetMockMethodReturnsNewDeepStubMockIfInvocationDoesNotMatch() throws Exception {
  DummyClass<Number> mock1 = mock(DummyClass.class, new ReturnsDeepStubs());
  DummyClass<String> mock2 = mock(DummyClass.class, new ReturnsDeepStubs());

  Object deepStub1 = ReturnsDeepStubs.this.getMock(
          new InvocationOnMock(Mockito.mock(GenericDeclaration.class), Arrays.asList("entrySet", "iterator", "next", "getValue", "iterator", "next"), null),
          Mockito.mock(GenericMetadataSupport.class));

  Object deepStub2 = ReturnsDeepStubs.this.getMock(
          new InvocationOnMock(mock1, Arrays.asList("entrySet", "iterator", "next", "getValue", "iterator", "next"), null),
          Mockito.mock(GenericMetadataSupport.class));

  assertNotSame(deepStub1, deepStub2);
 }

 @Test
 public void testCreateNewDeepStubMockMethodCreatesNewMockWithContextualGenericsMetadata() throws Exception {
  DummyClass<Number> mock = mock(DummyClass.class, new ReturnsDeepStubs());
  DummyClass<Number> deepStub = (DummyClass<Number>) ReturnsDeepStubs.this.createNewDeepStubMock(
          Mockito.mock(GenericMetadataSupport.class));

  verify(mockitoCore).mock(DummyClass.class, withSettings().defaultAnswer(ReturnsDeepStubs.this));
  verify(mockitoCore).mock(DummyClass.class, withSettings());

  assertSame(mock.getClass(), deepStub.getClass());
 }

 @Test
 public void testRecordDeepStubMockMethodRecordsAnswerForInvocation() throws Exception {
  DummyClass<Number> mock = mock(DummyClass.class, new ReturnsDeepStubs());
  Object answer = new Object() {};
  InvocationContainerImpl container = new InvocationContainerImpl(mock, new ArrayList<>());

  when(mock.entrySet().iterator().next().getValue()).thenAnswer(invocation -> answer);

  ReturnsDeepStubs.this.recordDeepStubMock(answer, container);

  verify(container).addAnswer(ArgumentMatchers.eq(answer), ArgumentMatchers.eq(false));
  verify(mock).entrySet();
  verify(mock.entrySet()).iterator();
  verify(mock.entrySet().iterator()).next();
  verify(mock.entrySet().iterator().next()).getValue();
 }

 @Test(expected = Throwable.class)
 public void testRecordDeepStubMockMethodThrowsExceptionIfAnswerIsNull() throws Exception {
  DummyClass<Number> mock = mock(DummyClass.class, new ReturnsDeepStubs());
  InvocationContainerImpl container = new InvocationContainerImpl(mock, new ArrayList<>());

  try {
   ReturnsDeepStubs.this.recordDeepStubMock(null, container);
  } catch (IllegalArgumentException e) {
   throw new AssertionError("Unexpected exception: " + e.getMessage());
  }

  verifyZeroInteractions(container);
  verifyZeroInteractions(mock);
 }
}