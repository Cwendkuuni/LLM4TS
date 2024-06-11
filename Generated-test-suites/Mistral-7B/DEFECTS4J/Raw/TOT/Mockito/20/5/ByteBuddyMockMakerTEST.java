package org.mockito.internal.creation.bytebuddy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockMaker;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;

public class ByteBuddyMockMakerTest {

 private ByteBuddyMockMaker mockMaker;
 private ClassInstantiator classInstantiatorSpy;

 @Before
 public void setUp() {
 mockMaker = new ByteBuddyMockMaker();
 classInstantiatorSpy = spy(mockMaker.classInstantiator);
 GlobalConfiguration.getInstance().setClassCacheDisabled(true); // disable class cache for testing
 }

 @Test
 public void testCreateMockSuccess() {
 // given
 MockCreationSettings<String> settings = new MockCreationSettings<String>(String.class);
 MockHandler handler = mock(MockHandler.class);

 // when
 String mock = mockMaker.createMock(settings, handler);

 // then
 verify(classInstantiatorSpy).instantiate(eq(String.class));
 assertNotNull(mock);
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockExceptionWhenSerializableModeIsAcrossClassloaders() {
 // given
 MockCreationSettings<String> settings = new MockCreationSettings<String>(String.class);
 settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
 MockHandler handler = mock(MockHandler.class);

 // when, then
 mockMaker.createMock(settings, handler);
 }

 @Test(expected = ClassCastException.class)
 public void testCreateMockClassCastException() {
 // given
 Class<Integer> typeToMock = Integer.class;
 MockCreationSettings<Integer> settings = new MockCreationSettings<Integer>(typeToMock);
 MockHandler handler = mock(MockHandler.class);
 Class<? extends Integer> mockedProxyType = mock(Class.class);

 // when
 mockMaker = new ByteBuddyMockMaker();
 T mockInstance = mockMaker.createMock(settings, handler);

 // then
 assertEquals(Integer.class, typeToMock);
 assertEquals(mockedProxyType, mockInstance.getClass());

 // when creating the mock with the wrong class
 T wrongMockInstance = mockMaker.createMock(settings, handler);
 fail("ClassCastException should be thrown");
 }

 @Test(expected = InstantiationException.class)
 public void testCreateMockInstantiationException() {
 // given
 Class<String> typeToMock = String.class;
 MockCreationSettings<String> settings = new MockCreationSettings<String>(typeToMock);
 MockHandler handler = mock(MockHandler.class);

 // when
 mockMaker = new ByteBuddyMockMaker();
 mockMaker.createMock(settings, handler);

 // then
 fail("InstantiationException should be thrown");
 }

 @Test
 public void testGetHandler() {
 // given
 Object mock = mock(Object.class);

 // when
 MockHandler handler = mockMaker.getHandler(mock);

 // then
 assertNull(handler);
 }

 @Test
 public void testResetMock() {
 // given
 Object mock = mock(Object.class);
 MockHandler newHandler = mock(MockHandler.class);
 MockCreationSettings settings = new MockCreationSettings(Object.class);

 // when
 mockMaker.resetMock(mock, newHandler, settings);

 // then
 MockHandler oldHandler = mockMaker.getHandler(mock);
 assertEquals(newHandler, oldHandler);
 }
}