import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mockMap;
 private Object mockSet;
 private Object mockNumber;

 @Before
 public void setUp() {
 mockMap = Mockito.mock(Map.class, new ReturnsDeepStubs());
 mockSet = Mockito.mock(Set.class, new ReturnsDeepStubs());
 mockNumber = Mockito.mock(Number.class, new ReturnsDeepStubs());
 }

 @Test
 public void testReturnsDeepStubsForMapEntrySetIteratorNextValueIteratorNext() throws Exception {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Type keyType = Map.Entry.class.getGenericComponentType();
 Type valueType = Number.class;
 Type mapType = Map.class;
 Type setType = Set.class;

 Mockito.when(mockMap.entrySet()).thenReturn(Mockito.mock(Set.class, new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 List entryList = Mockito.mock(List.class);
 Mockito.when(entryList.iterator()).thenReturn(Mockito.mock(java.util.Iterator.class, new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 Mockito.when(this.iterator().next()).thenReturn(Mockito.argThat(arg -> arg instanceof Map.Entry));
 Map.Entry entry = (Map.Entry) this.iterator().next();
 Mockito.when(entry.getValue()).thenReturn(mockNumber);
 return entry;
 }
 }));
 Mockito.when(entryList.size()).thenReturn(1);
 return entryList;
 }
 }));

 Mockito.when(mockMap.entrySet().iterator().next()).thenReturn(Mockito.argThat(arg -> arg instanceof Map.Entry));

 InvocationOnMock deepStubInvocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(deepStubInvocation.getMock()).thenReturn(mockMap);
 Mockito.when(deepStubInvocation.getMethod()).thenReturn(getMethod(mapType, "entrySet"));

 Object deepStub = returnsDeepStubs.answer(deepStubInvocation);

 assertSame(mockMap, deepStub);

 InvocationOnMock numberInvocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(numberInvocation.getMock()).thenReturn(mockNumber);
 Mockito.when(numberInvocation.getMethod()).thenReturn(getMethod(Number.class, ""));

 Object numberDeepStub = returnsDeepStubs.answer(numberInvocation);

 assertSame(mockNumber, numberDeepStub);
 }

 @Test
 public void testReturnsDeepStubsForGenericNestClass() throws Exception {
 Class<GenericsNest> genericsNestClass = (Class<GenericsNest>) Class.forName("GenericsNest");

 InvocationOnMock genericsNestInvocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(genericsNestInvocation.getMock()).thenReturn(Mockito.mock(genericsNestClass));
 Mockito.when(genericsNestInvocation.getMethod()).thenReturn(getMethod(genericsNestClass, ""));

 Object deepStub = returnsDeepStubs.answer(genericsNestInvocation);

 assertSame(Mockito.mock(genericsNestClass), deepStub);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testReturnsDefaultAnswerForNonMockableType() throws Exception {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Type nonMockableType = String.class;

 Mockito.when(invocation.getMock()).thenReturn(null);
 Mockito.when(invocation.getMethod()).thenReturn(getMethod(Object.class, ""));

 Object deepStub = returnsDeepStubs.answer(invocation);

 assertEquals("", deepStub.toString());
 }

 private Method getMethod(Class<?> clazz, String name) throws NoSuchMethodException {
 return clazz.getMethod(name, (Type[]) null);
 }
}