import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.plugins.MockMaker;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.bytebuddy.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.MockMethodInterceptor;
import org.mockito.internal.util.StringJoiner;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.MockitoAnnotations;
import org.mockito.plugins.MockMaker;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito. ArgumentMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class ByteBuddyMockMakerTest {

 private ByteBuddyMockMaker byteBuddyMockMaker;
 private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;
 private ClassInstantiator classInstantiator;

 @Before
 public void setup() {
 byteBuddyMockMaker = new ByteBuddyMockMaker();
 cachingMockBytecodeGenerator = mock(CachingMockBytecodeGenerator.class);
 classInstantiator = mock(ClassInstantiator.class);
 when(byteBuddyMockMaker.cachingMockBytecodeGenerator).thenReturn(cachingMockBytecodeGenerator);
 when(cachingMockBytecodeGenerator.get(any(), any())).thenReturn(mock(Object.class));
 when(classInstantiator.instantiate(any())).thenAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return invocation.getArgumentAt(0, Object.class);
 }
 });
 }

 @Test
 public void testCreateMockWithValidSettings() {
 MockHandler mockHandler = mock(MockHandler.class);
 MockCreationSettings settings = new MockCreationSettings.Builder()
 .forClass(String.class)
 .withSerializableMode(SerializableMode.DEFAULT)
 .build();
 Object mockObject = byteBuddyMockMaker.createMock(settings, mockHandler);
 verify(cachingMockBytecodeGenerator).get(eq(String.class), isA(Class[].class));
 verify(classInstantiator).instantiate(eq(String.class.cast(any())));
 verify(mockHandler).handle(eq(mockObject), any());
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockWithInvalidSerializableMode() {
 MockHandler mockHandler = mock(MockHandler.class);
 MockCreationSettings settings = new MockCreationSettings.Builder()
 .forClass(String.class)
 .withSerializableMode(SerializableMode.ACROSS_CLASSLOADERS)
 .build();
 byteBuddyMockMaker.createMock(settings, mockHandler);
 }

 @Test
 public void testGetHandler() {
 Object mockObject = mock(Object.class);
 MockHandler mockHandler = mock(MockHandler.class);
 byteBuddyMockMaker.createMock(new MockCreationSettings.Builder().build(), mockHandler);
 MockitoAnnotations.initMocks(this);
 byteBuddyMockMaker.getHandler(mockObject);
 verifyNoMoreInteractions(mockHandler);
 }

 @Test
 public void testResetMock() {
 Object mockObject = mock(Object.class);
 MockHandler newMockHandler = mock(MockHandler.class);
 byteBuddyMockMaker.createMock(new MockCreationSettings.Builder().build(), mockHandler);
 MockitoAnnotations.initMocks(this);
 byteBuddyMockMaker.resetMock(mockObject, newMockHandler, new MockCreationSettings.Builder().build());
 ArgumentCaptor<MockHandler> mockHandlerCaptor = ArgumentCaptor.forClass(MockHandler.class);
 verify(mockObject).setMockitoInterceptor(Mockito.argumentCaptor(MockHandler.class));
 verify(mockObject).getMockitoInterceptor().setMockHandler(newMockHandlerCaptor.capture());
 MockHandler capturedMockHandler = mockHandlerCaptor.getValue();
 verifyZeroInteractions(mockHandler);
 verify(capturedMockHandler).handle(any(), any());
 }

 @Test
 public void testDescribeClass() {
 Class<?> type = null;
 Class<?> instance = null;
 Class<?> mockType = mock(Class.class);
 ClassLoader classLoader = mock(ClassLoader.class);
 Object mockInstance = mock(Object.class);
 when(type.getClassLoader()).thenReturn(classLoader);
 when(instance.getClass()).thenReturn(type);
 when(mockInstance.getClass()).thenReturn(mockType);

 assertEquals("null", byteBuddyMockMaker.describeClass(type));
 assertEquals("'org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker$MockClassLoader@6a88e2d2', loaded by classloader : 'mockClassLoader'", byteBuddyMockMaker.describeClass(instance));
 assertEquals("'org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker$MockClass@22c16f81', loaded by classloader : 'mockClassLoader'", byteBuddyMockMaker.describeClass(mockInstance));
 }
}