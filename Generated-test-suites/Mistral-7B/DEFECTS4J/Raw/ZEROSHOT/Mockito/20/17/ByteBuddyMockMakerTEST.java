package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

 private ByteBuddyMockMaker byteBuddyMockMaker;
 private ClassInstantiator classInstantiator;
 private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;

 @Before
 public void setUp() {
 byteBuddyMockMaker = new ByteBuddyMockMaker();
 classInstantiator = mock(ClassInstantiator.class);
 cachingMockBytecodeGenerator = mock(CachingMockBytecodeGenerator.class);
 }

 @Test
 public void testCreateMockWithValidSettingsAndHandler() {
 Class<?> typeToMock = MockitoMockito.class;
 MockCreationSettings<Object> settings = new MockCreationSettings<Object>(typeToMock, SerializableMode.DEFAULT, new Object[]{}, null);
 MockHandler handler = mock(MockHandler.class);
 Object mock = byteBuddyMockMaker.createMock(settings, handler);

 verify(classInstantiator).instantiate(typeToMock);
 verify(handler).handleAllCalls(any(Object.class), any(Object[].class));
 }

 @Test
 public void testCreateMockWithInvalidSerializableMode() {
 Class<?> typeToMock = MockitoMockito.class;
 MockCreationSettings<Object> settings = new MockCreationSettings<Object>(typeToMock, SerializableMode.ACROSS_CLASSLOADERS, new Object[]{}, null);
 MockHandler handler = mock(MockHandler.class);

 assertThrows(MockitoException.class, () -> byteBuddyMockMaker.createMock(settings, handler));
 }

 @Test
 public void testGetHandler() {
 Object mock = spy(new Object() {});
 byteBuddyMockMaker.setHandler(mock, mock(MockHandler.class), new MockCreationSettings<Object>(MockitoMockito.class, SerializableMode.DEFAULT, new Object[]{}, null));
 MockHandler returnedHandler = byteBuddyMockMaker.getHandler(mock);

 assert(returnedHandler != null);
 }

 @Test
 public void testResetMock() {
 Object mock = spy(new Object() {});
 MockHandler handler = mock(MockHandler.class);
 byteBuddyMockMaker.setHandler(mock, handler, new MockCreationSettings<Object>(MockitoMockito.class, SerializableMode.DEFAULT, new Object[]{}, null));
 byteBuddyMockMaker.resetMock(mock, handler, new MockCreationSettings<Object>(MockitoMockito.class, SerializableMode.DEFAULT, new Object[]{}, null));

 MockHandler resetHandler = byteBuddyMockMaker.getHandler(mock);

 assert(resetHandler.equals(handler));
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockWithInstantiationException() {
 Class<?> typeToMock = MockitoMockito.class;
 MockCreationSettings<Object> settings = new MockCreationSettings<Object>(typeToMock, SerializableMode.DEFAULT, new Object[]{}, null);
 MockHandler handler = mock(MockHandler.class);

 ByteBuddyMockMaker spy = spy(byteBuddyMockMaker);
 when(spy.instantiate(typeToMock)).thenThrow(new InstantiationException("Instantiation failed"));

 Object mock = spyBuddyMockMaker.createMock(settings, handler);
 }

 @Test(expected = ClassCastException.class)
 public void testCreateMockWithClassCastException() {
 Class<?> typeToMock = String.class;
 Class<? extends Object> mockedProxyType = MockitoMockito.class;
 MockHandler handler = mock(MockHandler.class);

 ByteBuddyMockMaker spy = spy(byteBuddyMockMaker);when(spy.cachingMockBytecodeGenerator.get(typeToMock, new Class[0]))
 .thenReturn(mockedProxyType);
 when(classInstantiator.instantiate(mockedProxyType)).thenReturn(new Object() {});

 Object mock = spyBuddyMockMaker.createMock(new MockCreationSettings<Object>(typeToMock, SerializableMode.DEFAULT, new Object[0], null), handler);
 }

 @Test
 public void testDescribeClass() {
 Class<?> type = MockitoMockito.class;
 Object instance = new Object();

 String descriptionOfType = ByteBuddyMockMaker.describeClass(type);
 String descriptionOfInstance = ByteBuddyMockMaker.describeClass(instance);

 assertEquals("Class: '" + type.getCanonicalName() + "', loaded by classloader : '" + type.getClassLoader() + "'", descriptionOfType);
 assertEquals("Class: 'class org.mockito.internal.creation.bytebuddy.ByteBuddyMockMakerTest$TestClass$1 at ByteBuddyMockMakerTest.lambda$testCreateMockWithClassCastException$0(ByteBuddyMockMakerTest.java:XXX)', loaded by classloader : 'sun.misc.Launcher$AppClassLoader@XXX'", descriptionOfInstance);
 }
}