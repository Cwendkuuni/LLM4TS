import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.lang.reflect.GenericMetadataSupport;
import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsDeepStubsTest {

 private static final Class<ReturnsDeepStubs> RETURNS_DEEP_STUBS = ReturnsDeepStubs.class;

 @Test
 public void testAnswer() {
 // Test basic functionality
 Object mock = mock(IFoo.class, new ReturnsDeepStubs());
 Object returnValue = new Object();
 when(mock.someMethod()).thenReturn(returnValue);
 Object result = mock.someMethod();

 assertSame(returnValue, result);
 verifyZeroInteractions(mock);

 // Test with empty values answer
 mock = mock(IFoo.class, new ReturnsEmptyValues());
 result = mock.someMethod();
 assertNull(result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnsupportedType() {
 // Test unsupported type
 mock = mock(UnsupportedType.class, new ReturnsDeepStubs());
 }

 @Test
 public void testCreateNewDeepStubMock() throws Exception {
 // Test creating new deep stub mock
 GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport() {
 @Override
 public Class<?> getRawType() {
 return IFoo.class;
 }

 @Override
 public Class<? extends Annotation>[] getAnnotations() {
 return new Class[0];
 }

 @Override
 public Class<? extends Annotation>[] getDeclaredAnnotations() {
 return new Class[0];
 }

 @Override
 public String getName() {
 return "name";
 }

 @Override
 public boolean isInterface() {
 return true;
 }

 @Override
 public boolean isArray() {
 return false;
 }

 @Override
 public Type getComponentType() {
 return null;
 }

 @Override
 public Type[] getActualTypeArguments() {
 return new Type[]{String.class, Number.class};
 }

 @Override
 public Type getGenericSuperclass() {
 return Map.class;
 }

 @Override
 public Class<? extends Type> getDeclaringType() {
 return Object.class;
 }

 @Override
 public boolean isMemberClass() {
 return false;
 }

 @Override
 public boolean isLocalClass() {
 return false;
 }

 @Override
 public boolean isSynthetic() {
 return false;
 }

 @Override
 public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
 return false;
 }

 @Override
 public Annotation[] getAnnotationsByType(Class<? extends Annotation> annotationType) {
 return new Annotation[0];
 }

 @Override
 public int getModifiers() {
 return 0;
 }

 @Override
 public String toString() {
 return "IFoo<String, Number>";
 }

 @Override
 public boolean isPrimitive() {
 return false;
 }
 };

 Object deepStubMock = ReturnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);
 assertNotNull(deepStubMock);
 assertSame(Map.class, deepStubMock.getClass().getGenericSuperclass());
 }

 @Test
 public void testGetMock() throws Exception {
 // Test getting mock from InvocationContainerImpl
 InvocationContainerImpl container = mock(InvocationContainerImpl.class);
 Object mock = mock(IFoo.class, new ReturnsDeepStubs());
 when(container.getInvocationForStubbing()).thenReturn(new InvocationOnMock(mock, Mockito.method(IFoo.class, "someMethod"), new Object[0], new Object[0][], new Object[0], false, false, false, false, false, false, false, false, false, false, false, false, false));
 Object deepStubMock = ReturnsDeepStubs.getMock(new InvocationOnMock(mock, Mockito.method(IFoo.class, "someMethod"), new Object[0], new Object[0][], new Object[0], false, false, false, false, false, false, false, false, false, false, false, false, false), returnTypeGenericMetadata);
 assertSame(mock, deepStubMock);
 }

 @Test
 public void testRecordDeepStubMock() throws Exception {
 // Test recording deep stub mock
 Object mock = mock(IFoo.class, new ReturnsDeepStubs());
 Object deepStubMock = mock(IDeepStub.class);
 InvocationContainerImpl container = mock(InvocationContainerImpl.class);
 when(container.addAnswer(any(Answer.class), anyBoolean())).thenReturn(deepStubMock);
 when(container.getInvocationForStubbing()).thenReturn(new InvocationOnMock(mock, Mockito.method(IFoo.class, "someMethod"), new Object[0], new Object[0][], new Object[0], false, false, false, false, false, false, false, false, false, false, false, false, false));
 ReturnsDeepStubs.recordDeepStubMock(deepStubMock, container);
 verify(deepStubMock).answer(any(InvocationOnMock.class));
 }

 @Test
 public void testActualParameterizedType() throws Exception {
 // Test getting actual parameterized type
 Object mock = mock(IFoo.class, new ReturnsDeepStubs());
 GenericMetadataSupport returnTypeGenericMetadata = ReturnsDeepStubs.actualParameterizedType(mock);
 assertSame(IFoo.class, returnTypeGenericMetadata.getRawType());
 }
}

interface IFoo<K extends Comparable<K> & Cloneable, V extends Number> extends Map<K, Set<V>> {
}

interface IDeepStub extends Serializable {
}