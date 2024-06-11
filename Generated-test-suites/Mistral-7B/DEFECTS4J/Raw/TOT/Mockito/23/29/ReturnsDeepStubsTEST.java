import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.GenericMetadataSupport;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object deepStub1;
 private Object deepStub2;
 private Object deepStub3;

 @Before
 public void setUp() {
 returnsDeepStubs = new ReturnsDeepStubs();

 deepStub1 = mock(Object.class, withSettings().defaultAnswer(returnsDeepStubs));
 deepStub2 = mock(Map.class, withSettings().defaultAnswer(returnsDeepStubs));
 deepStub3 = mock(Map.Entry.class, withSettings().defaultAnswer(returnsDeepStubs));
 }

 @Test
 public void testGetMock_ReturnsSameInstanceForSameInvocation() {
 Object result1 = deepStub1.iterator().next();
 Object result2 = deepStub1.iterator().next();

 assertEquals(result1, result2);
 }

 @Test
 public void testGetMock_ReturnsDifferentInstancesForDifferentInvocations() {
 Object result1 = deepStub1.iterator().next();
 Object result2 = deepStub2.entrySet().iterator().next().getValue().iterator().next();

 assertNotSame(result1, result2);
 }

 @Test
 public void testGetMock_ReturnsPreviousMockForSameInvocationAndType() {
 Object container = mock(InvocationContainerImpl.class);
 InternalMockHandler handler = new MockUtil().getMockHandler(deepStub1);
 handler.setInvocationContainer(container);
 stub(deepStub1.iterator()).toReturn(deepStub2);

 Object result = deepStub1.iterator().next();
 verify(deepStub2).answer(any(InvocationOnMock.class));

 assertEquals(deepStub2, result);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetMock_ThrowsExceptionForNonMockableType() {
 deepStub1 = mock(Thread.class, withSettings().defaultAnswer(returnsDeepStubs));

 throwsException(IllegalStateException.class, () -> deepStub1.getThreadGroup());
 }

 @Test
 public void testGetMock_CreatesNewMockForDifferentTypes() {
 deepStub1 = mock(Object.class, withSettings().defaultAnswer(returnsDeepStubs));
 deepStub2 = mock(Map.class, withSettings().defaultAnswer(returnsDeepStubs));

 assertNotSame(deepStub1, deepStub2);
 }

 @Test
 public void testReturnsEmptyValues_ReturnsEmptyValuesForNonMockableTypes() {
 Answer<Object> emptyValues = new ReturnsEmptyValues();
 deepStub1 = mock(Object.class, withSettings().defaultAnswer(emptyValues));

 assertEquals(Arrays.asList(), deepStub1.iterator().toList());
 }

 @Test
 public void testAnswer_ReturnInstanceOfSameType() {
 Type returnType = ArgumentMatchers.typeCapture(Type.class, deepStub2::iterator).getRawType();
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(deepStub2.iterator()).thenAnswer(returnsDeepStubs);

 Object result = returnsDeepStubs.answer(invocation);

 assertEquals(deepStub2.getClass(), result.getClass());
 }

 @Test
 public void testAnswer_ReturnInstanceOfSubtype() {
 Type returnType = ArgumentMatchers.typeCapture(Type.class, deepStub2::iterator).getRawType();
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(deepStub2.iterator()).thenAnswer(new ReturnsDeepStubs() {
 @Override
 protected GenericMetadataSupport actualParameterizedType(Object mock) {
 return new GenericMetadataSupport() {
 @Override public Type getRawType() { return Map.Entry.class; } };
 }
 });

 Object result = returnsDeepStubs.answer(invocation);

 assertEquals(Map.Entry.class, result.getClass());
 }
}