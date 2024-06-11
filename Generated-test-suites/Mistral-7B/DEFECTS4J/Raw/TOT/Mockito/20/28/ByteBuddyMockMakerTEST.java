import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InternalMockHandler;
import org.mockito.MockCreationSettings;
import org.mockito.MockHandler;
import org.mockito.Mockito;
import org.mockito.plugins.MockMaker;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class ByteBuddyMockMakerTest {

 private MockMaker mockMaker;
 private ClassInstantiator classInstantiator;
 private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;

 @Before
 public void setup() {
 mockMaker = new ByteBuddyMockMaker();
 classInstantiator = mockMaker.initializeClassInstantiator();
 cachingMockBytecodeGenerator = mock(CachingMockBytecodeGenerator.class);
 }

 @Test
 public void testCreateMockWithValidSettings() {
 // Given
 MockCreationSettings<String> settings = new MockCreationSettings.Builder<String>()
 .forClass(String.class)
 .withSerializableMode(SerializableMode.SERIAL)
 .build();
 InternalMockHandler handler = mock(InternalMockHandler.class);

 // When
 String mock = mockMaker.createMock(settings, handler);

 // Then
 verify(cachingMockBytecodeGenerator).get(eq(String.class), isA(Class[].class));
 ArgumentCaptor<MockHandler> handlerCaptor = ArgumentCaptor.forClass(MockHandler.class);
 verify(handler).handle(eq(mock));
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockWithInvalidSerializableMode() {
 // Given
 MockCreationSettings<String> settings = new MockCreationSettings.Builder<String>()
 .forClass(String.class)
 .withSerializableMode(SerializableMode.ACROSS_CLASSLOADERS)
 .build();
 InternalMockHandler handler = mock(InternalMockHandler.class);

 // When and Then
 mockMaker.createMock(settings, handler);
 }

 @Test(expected = ClassCastException.class)
 public void testCreateMockWithClassCastException() {
 // Given
 Class<?> mockedType = Object.class;
 MockCreationSettings<Object> settings = new MockCreationSettings.Builder<Object>()
 .forClass(mockedType)
 .build();
 InternalMockHandler handler = mock(InternalMockHandler.class);

 // When
 Object mock = mockMaker.createMock(settings, handler);

 // Then
 verify(classInstantiator).instantiate(eq(mockedType));
 ArgumentCaptor<Class<?>> typeCaptor = ArgumentCaptor.forClass(Class.class);
 verify(cachingMockBytecodeGenerator).get(eq(mockedType), typeCaptor.capture());
 ArgumentCaptor<MockHandler> handlerCaptor = ArgumentCaptor.forClass(MockHandler.class);
 verify(handler).handle(handlerCaptor.capture());
 Object returnedMock = typeCaptor.getValue().cast(mock);
 ArgumentCaptor<InternalMockHandler> internalHandlerCaptor = ArgumentCaptor.forClass(InternalMockHandler.class);
 verify(handlerCaptor.getValue()).setMockitoInterceptor(internalHandlerCaptor.capture());
 Class<?> returnedType = internalHandlerCaptor.getValue().getMockedType();
 verifyThat(returnedType).isEqualTo(mockedType);

 // Verify that the returned mock instance is the same as the one passed to the handler
 verify(handlerCaptor.getValue()).handle(eq(returnedMock));
 }

 @Test
 public void testGetHandler() {
 // Given
 Object mock = mock(String.class);

 // When
 MockHandler handler = mockMaker.getHandler(mock);

 // Then
 assertNull(handler);
 }

 @Test
 public void testResetMock() {
 // Given
 Object mock = mock(String.class);
 InternalMockHandler handler = mock(InternalMockHandler.class);
 mockMaker.createMock(new MockCreationSettings.Builder<String>().build(), handler);
 mockMaker.resetMock(mock, handler, new MockCreationSettings.Builder<String>().build());

 // Then
 verifyZeroInteractions(mock);
 verify(handler).handle(eq(mock));
 }

 @Test
 public void testInitializeClassInstantiatorWithClassCache() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, UndeclaredThrowableException {
 Constructor<?> constructor = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis")
 .getDeclaredConstructor(boolean.class);
 ClassInstantiator classInstantiator = Class.forName("org.mockito.internal.creation.instance.ClassInstantiator")
 .cast(constructor.newInstance(true));
 assertEquals(ClassInstantiator.class.cast(mockMaker.initializeClassInstantiator()), classInstantiator);
 }

 @Test(expected = MockitoException.class)
 public void testInitializeClassInstantiatorWithoutClassCache() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, UndeclaredThrowableException {
 Constructor<?> constructor = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis")
 .getDeclaredConstructor(boolean.class);
 ClassInstantiator classInstantiator = Class.forName("org.mockito.internal.creation.instance.ClassInstantiator")
 .cast(constructor.newInstance(false));
 mockMaker = new ByteBuddyMockMaker();
 mockMaker.initializeClassInstantiator();
 }
}