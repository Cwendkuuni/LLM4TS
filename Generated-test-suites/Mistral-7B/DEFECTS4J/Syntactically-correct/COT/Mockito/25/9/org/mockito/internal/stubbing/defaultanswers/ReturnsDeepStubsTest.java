import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

 @Test
 public void testAnswerMethodReturnsExistingMock() throws Throwable {
     Object mock = mock(Object.class, Answers.RETURNS_DEEP_STUBS);
     InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
     invocation.setTarget(mock);

     assertEquals(mock, returnsDeepStubs.answer(invocation));

     verify(mock).answer(ArgumentMatchers.any(InvocationOnMock.class));
 }

 @Test
 public void testAnswerMethodReturnsEmptyValuesForNonMockableTypes() throws Throwable {
     Object nonMockableType = new Object() {};
     InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
     invocation.setTarget(nonMockableType);

     assertEquals(new ReturnsEmptyValues().returnValueFor(nonMockableType.getClass()),
             returnsDeepStubs.answer(invocation));
 }

 @Test(expected = IllegalStateException.class)
 public void testAnswerMethodThrowsExceptionForNonInvocationOnMock() throws Throwable {
     Object nonInvocationOnMock = new Object() {};
     returnsDeepStubs.answer(Mockito.mock(Object.class).eq(nonInvocationOnMock));
 }

 @Test
 public void testGetMockMethodReturnsExistingStub() throws Throwable {
     Object mock = mock(Object.class, Answers.RETURNS_DEEP_STUBS);
     InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
     invocation.setTarget(mock);

     Object deepMock = returnsDeepStubs.getMock(invocation);

     assertEquals(mock, deepMock);
 }

 @Test
 public void testGetMockMethodReturnsNewlyCreatedMockForNewInvocation() throws Throwable {
     Object mock = mock(Object.class, Answers.RETURNS_DEEP_STUBS);
     InvocationOnMock newInvocation = Mockito.mock(InvocationOnMock.class);
     newInvocation.setTarget(mock);

     Object newDeepMock = returnsDeepStubs.getMock(newInvocation);

     assertEquals(newDeepMock, newMock(Object.class, Answers.RETURNS_DEEP_STUBS));
 }

 @Test
 public void testRecordDeepStubMockMethodCreatesNewMockAndAnswersWithIt() throws Throwable {
     Object mock = mock(Object.class, Answers.RETURNS_DEEP_STUBS);
     InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
     invocation.setTarget(mock);

     Object deepMock = returnsDeepStubs.recordDeepStubMock(invocation, Mockito.mock(InvocationContainerImpl.class));

     assertEquals(deepMock, returnsDeepStubs.answer(invocation));
 }

 @Test
 public void testRecordDeepStubMockMethodDoesNotCreateNewMockForEqualInvocations() throws Throwable {
     Object mock = mock(Object.class, Answers.RETURNS_DEEP_STUBS);
     InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
     invocation1.setTarget(mock);

     InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
     invocation2.setTarget(mock);

     InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);

     ReturnsDeepStubs returnsDeepStubsSpy = Mockito.spy(new ReturnsDeepStubs());
     Object deepMock = returnsDeepStubsSpy.recordDeepStubMock(invocation1, container);

     assertEquals(deepMock, returnsDeepStubsSpy.answer(invocation2));
 }

 @Test
 public void testActualParameterizedTypeMethodReturnsCorrectMetadata() throws Throwable {
     Class<?> clz = Integer[].class;
     Object mock = mock(clz, Answers.RETURNS_DEEP_STUBS);

     assertEquals(GenericMetadataSupport.forRawType(clz.getComponentType()),
             returnsDeepStubs.actualParameterizedType(mock));
 }

 @Test(expected = IllegalStateException.class)
 public void testActualParameterizedTypeMethodThrowsExceptionForNonInvocationOnMock() throws Throwable {
     Object nonInvocationOnMock = new Object() {};

     try {
         returnsDeepStubs.actualParameterizedType(nonInvocationOnMock);
     } catch (IllegalStateException e) {
         throw new AssertionError("Unexpected exception: " + e.getMessage(), e);
     }
 }
}