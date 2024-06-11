import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.MockHandler;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.bytebuddy.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.util.StringJoiner;
import org.mockito.plugins.MockMaker;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

@RunWith(org.mockito.runners.JUnit4.class)
public class ByteBuddyMockMakerTest {

 private ByteBuddyMockMaker byteBuddyMockMaker;
 private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;
 private ClassInstantiator classInstantiator;

 @Before
 public void setup() {
 byteBuddyMockMaker = new ByteBuddyMockMaker();
 cachingMockBytecodeGenerator = mock(CachingMockBytecodeGenerator.class);
 classInstantiator = mock(ClassInstantiator.class);
 }

 @Test
 public void testCreateMock_ValidScenario() {
 // Given
 MockHandler handler = mock(MockHandler.class);
 MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
 .withTypeToMock(Integer.class)
 .withSerializableMode(SerializableMode.DEFAULT)
 .build();

 // When
 Object mock = byteBuddyMockMaker.createMock(settings, handler);

 // Then
 verify(handler).handle(Mockito.any());
 }

 @Test(expected = MockitoException.class)
 public void testCreateMock_SerializationAcrossClassloadersNotSupported() {
 // Given
 MockHandler handler = mock(MockHandler.class);
 MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
 .withTypeToMock(Integer.class)
 .withSerializableMode(SerializableMode.ACROSS_CLASSLOADERS)
 .build();

 // When & Then
 byteBuddyMockMaker.createMock(settings, handler);
 }

 @Test
 public void testCreateMock_ClassCastException() throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, UndeclaredThrowableException {
 // Given
 Class<? extends Object> mockedProxyType = mock(Class.class);
 when(cachingMockBytecodeGenerator.get(Mockito.eq(Integer.class), Mockito.any()))
 .thenReturn(mockedProxyType);
 when(classInstantiator.instantiate(Mockito.eq(mockedProxyType)))
 .thenThrow(new ClassCastException());

 MockHandler handler = mock(MockHandler.class);
 MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
 .withTypeToMock(Integer.class)
 .withSerializableMode(SerializableMode.DEFAULT)
 .build();

 // When & Then
 byteBuddyMockMaker.createMock(settings, handler);
 }

 @Test
 public void testCreateMock_InstantiationException() {
 // Given
 Class<? extends Object> mockedProxyType = mock(Class.class);
 when(cachingMockBytecodeGenerator.get(Mockito.eq(Integer.class), Mockito.any()))
 .thenReturn(mockedProxyType);
 when(classInstantiator.instantiate(Mockito.eq(mockedProxyType)))
 .thenThrow(new InstantiationException());

 MockHandler handler = mock(MockHandler.class);
 MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
 .withTypeToMock(Integer.class)
 .withSerializableMode(SerialiableMode.DEFAULT)
 .build();

 // When & Then
 byteBuddyMockMaker.createMock(settings, handler);
 }

 @Test
 public void testGetHandler() {
 // Given
 Object mock = mock(Integer.class);

 // When
 MockHandler handler = byteBuddyMockMaker.getHandler(mock);

 // Then
 assertNull(handler);
 }

 @Test
 public void testResetMock() {
 // Given
 Object mock = mock(Integer.class);
 MockHandler newHandler = mock(MockHandler.class);
 MockCreationSettings settings = new MockCreationSettings.Builder()
 .withTypeToMock(Integer.class)
 .withSerializableMode(SerialiableMode.DEFAULT)
 .build();

 // When
 byteBuddyMockMaker.resetMock(mock, newHandler, settings);

 // Then
 MockMethodInterceptor mockAccess = (MockMethodInterceptor) mock;
 assertEquals(newHandler, mockAccess.getMockitoInterceptor().getMockHandler());
 }

 @Test
 public void testInitializeClassInstantiator() {
 // Given
 // When
 byteBuddyMockMaker = new ByteBuddyMockMaker();

 // Then
 assertNotNull(byteBuddyMockMaker.classInstantiator);
 }

 @Test(expected = IllegalStateException.class)
 public void testInitializeClassInstantiator_ObjenesisMissingOnClasspath() {
 // Given
 // When & Then
 byteBuddyMockMaker = new ByteBuddyMockMaker();
 }
}