package org.mockito.internal.creation.bytebuddy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerRegistry;
import static org.mockito.Mockito.*;
import static org.mockito.internal.util.StringJoiner.join;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

public class ByteBuddyMockMakerTest {

 private ByteBuddyMockMaker byteBuddyMockMaker;
 private MockMakerRegistry mockMakerRegistry;

 @Before
 public void setup() {
 MockMakerRegistry.initMocks(this);
 byteBuddyMockMaker = new ByteBuddyMockMaker();
 mockMakerRegistry.register(ByteBuddyMockMaker.class, byteBuddyMockMaker);
 }

 @Test
 public void testCreateMockWithValidSettingsAndHandler() {
 Class<MyMockType> myMockType = MyMockType.class;
 MockCreationSettings<MyMockType> mockCreationSettings = new MockCreationSettings<MyMockType>(myMockType, SerializableMode.NORMAL, null);
 MockHandler mockHandler = mock(MockHandler.class);

 MyMockType createdMock = byteBuddyMockMaker.createMock(mockCreationSettings, mockHandler);
 assertNotNull(createdMock);

 verify(mockHandler).handle(eq(any()), eq(myMockType));
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockWithSerializableModeACrosClassLoaders() {
 Class<MyMockType> myMockType = MyMockType.class;
 MockCreationSettings<MyMockType> mockCreationSettings = new MockCreationSettings<MyMockType>(myMockType, SerializableMode.ACROSS_CLASSLOADERS, null);
 MockHandler mockHandler = mock(MockHandler.class);

 byteBuddyMockMaker.createMock(mockCreationSettings, mockHandler);
 }

 @Test
 public void testGetHandler() {
 MyMockType mock = mock(MyMockType.class);
 assertNull(byteBuddyMockMaker.getHandler(mock));

 MyMockType mockWithHandler = spy(MyMockType.class);
 when(byteBuddyMockMaker.getHandler(mockWithHandler)).thenReturn(mockHandler());
 assertSame(mockHandler(), byteBuddyMockMaker.getHandler(mockWithHandler));
 }

 @Test
 public void testResetMock() {
 MyMockType myMock = mock(MyMockType.class);
 MockHandler mockHandler = mock(MockHandler.class);
 byteBuddyMockMaker.resetMock(myMock, mockHandler, new MockCreationSettings<>(MyMockType.class, SerializableMode.NORMAL, null));

 verify(mockHandler).handle(eq(eq(any()), eq(MyMockType.class)), times(2));
 }

 @Test
 public void testDescribeClass() {
 assertEquals("class 'org.mockito.test.MyMockType', loaded by classloader : 'sun.misc.Launcher$AppClassLoader@68b4a2b2'", byteBuddyMockMaker.describeClass(MyMockType.class));
 assertEquals("null", byteBuddyMockMaker.describeClass(null));
 }

 @Test(expected = MockitoException.class)
 public void testExceptionWhenInitClassInstantiatorFails() {
 byteBuddyMockMaker = new ByteBuddyMockMaker();
 }

}

 class MyMockType {
 }