package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.RETURNS_DEFAULTS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.withSettings;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.MockitoSession;

public class MockUtilTest {

 private MockitoSession session = Mockito.session();
 private CreationValidator creationValidator = new CreationValidator();
 private MockUtil util = new MockUtil(creationValidator);

 @Test
 public void testCreateMock() {
 MockSettingsImpl settings = new MockSettingsImpl(session, creationValidator, null, null);
 Class<String> classToMock = String.class;
 String spiedInstance = "mock instance";
 String mockName = "mockName";
 settings.initiateMockName(classToMock);
 settings.setSpiedInstance(spiedInstance);
 String expectedMock = mock(classToMock, settings);
 T mock = util.createMock(classToMock, settings);
 assertSame(expectedMock, mock);
 assertSame(spiedInstance, session.getMockConstructionCallStack().getMostRecentMock().getActualValue());
 assertSame(mockName, Mockito.mockConstructionCalls().getMostRecentMockName());
 }

 @Test
 public void testCreateMockWithExtraInterfaces() {
 MockSettingsImpl settings = new MockSettingsImpl(session, creationValidator, new Class[0], new Class[0]);
 Class<String> classToMock = String.class;
 String mockName = "mockName";
 settings.initiateMockName(classToMock);
 settings.setSpiedInstance(null);
 String expectedMock = mock(classToMock, settings.getExtraInterfaces());
 T mock = util.createMock(classToMock, settings);
 assertSame(expectedMock, mock);
 }

 @Test
 public void testCreateMockWithNullSpiedInstance() {
 Class<String> classToMock = String.class;
 assertThrows(NotAMockException.class, () -> util.createMock(classToMock, null));
 }

 @Test
 public void testResetMock() {
 String mockName = "mockName";
 Object mock = mock(String.class);
 util.resetMock(mock);
 MockHandlerInterface<String> mockHandler = util.getMockHandler(mock);
 assertSame(mockHandler, getMockHandler(mock));
 MockHandler<String> newMockHandler = new MockHandler<>(mockHandler);
 assertFalse(newMockHandler.isSpy());
 }

 @Test
 public void testGetMockHandler() {
 String mockName = "mockName";
 Object mock = mock(String.class);
 MockHandlerInterface<String> mockHandler = getMockHandler(mock);
 assertSame(mockHandler, util.getMockHandler(mock));
 }

 @Test
 public void testIsMockitoMock() {
 Object mock = mock(String.class);
 assertTrue(util.isMockitoMock(mock));
 }

 @Test
 public void testIsMock() {
 Object mock = mock(String.class);
 assertTrue(util.isMock(mock));
 }

 @Test
 public void testGetInterceptor() {
 String mockName = "mockName";
 Object mock = mock(String.class, withSettings().name(mockName));
 MockHandlerInterface<String> mockHandler = util.getMockHandler(mock);
 MethodInterceptorFilter interceptor = util.getInterceptor(mock);
 assertSame(mockHandler, getMockHandler(mock));
 assertSame(interceptor, mockHandler.getInterceptor());
 }

 @Test
 public void testGetMockName() {
 String mockName = "mockName";
 Object mock = mock(String.class, withSettings().name(mockName));
 String name = util.getMockName(mock);
 assertSame(mockName, name);
 }

 @Test
 public void testGetInterceptorWithNullMock() {
 assertNull(util.getInterceptor(null));
 }

 private MockHandlerInterface<String> getMockHandler(Object mock) {
 return Mockito.mockConstructionCalls().getMostRecentMock();
 }

}