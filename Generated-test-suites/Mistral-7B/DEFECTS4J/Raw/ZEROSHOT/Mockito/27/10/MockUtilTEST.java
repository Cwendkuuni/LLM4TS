package org.mockito.internal.util.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InvocationNotifierHandler;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.MockCreationValidator;
import org.mockito.internal.util.reflection.ArrayUtils;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.test.TestUtils;
import org.mockito.internal.util.test.TestUtilsSpiedInstance;
import org.mockito.internal.util.test.TestUtilsMockSettings;
import org.powermock.api.mockito.PowerMockito;
import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.Arrays;

public class MockUtilTest {

 private MockUtil mockUtil;
 private MockCreationValidator mockCreationValidator;
 private MockSettingsImpl mockSettings;
 private Object spiedInstance;

 @Before
 public void setUp() {
 mockCreationValidator = TestUtils.createMockCreationValidator();
 mockUtil = new MockUtil(mockCreationValidator);
 mockSettings = new TestUtilsMockSettings();
 spiedInstance = new TestUtilsSpiedInstance();
 }

 @Test
 public void testCreateMock() {
 Class<?> clazzToMock = TestUtils.TestClassToMock.class;
 MockSettingsImpl settings = mockSettings;
 settings.setSpiedInstance(spiedInstance);

 MockName expectedMockName = new MockName("MockClassToMock");
 settings.initiateMockName(clazzToMock);
 assertEquals(expectedMockName.getMockName(), mockUtil.createMock(clazzToMock, settings).getClass().getName());

 Class<?>[] extraInterfaces = new Class[] {TestUtils.TestExtraInterface.class};
 settings.setExtraInterfaces(extraInterfaces);
 settings.setSerializable(true);

 Class<?> ancillaryTypes[] = new Class[] {Serializable.class, TestUtils.TestExtraInterface.class};
 MockName expectedAncillaryMockName = new MockName("MockClassToMock_MockExtraInterface");
 MockName expectedClassMockName = new MockName("MockClassToMock");
 Class<?> mock = mockUtil.createMock(clazzToMock, settings);
 Class<?>[] interfaces = mock.getClass().getInterfaces();
 assertArrayEquals(ancillaryTypes, interfaces);
 assertEquals(expectedAncillaryMockName.getMockName(), interfaces[0].getName());
 assertEquals(expectedClassMockName.getMockName(), clazzToMock.getName());

 PowerMockito.mockStatic(ClassImposterizer.class);
 PowerMockito.when(ClassImposterizer.class, "imposterise").thenReturn(PowerMockito.mock(MethodInterceptorFilter.class));
 assertThrows(NotAMockException.class, () -> mockUtil.createMock(clazzToMock, settings));
 }

 @Test
 public void testCreateMockWithNullClassToMock() {
 assertThrows(NullPointerException.class, () -> mockUtil.createMock(null, mockSettings));
 }

 @Test
 public void testCreateMockWithNullExtraInterfaces() {
 Class<?> clazzToMock = TestUtils.TestClassToMock.class;
 mockSettings.setSpiedInstance(spiedInstance);
 mockSettings.setExtraInterfaces(null);
 MockName expectedClassMockName = new MockName("MockClassToMock");
 MockName expectedMockName = new MockName("MockClassToMock");
 Class<?> mock = mockUtil.createMock(clazzToMock, mockSettings);
 Class<?>[] interfaces = mock.getClass().getInterfaces();
 assertArrayEquals(new Class[] {}, interfaces);
 assertEquals(expectedClassMockName.getMockName(), clazzToMock.getName());
 }

 @Test
 public void testCreateMockWithNullSpiedInstance() {
 Class<?> clazzToMock = TestUtils.TestClassToMock.class;
 mockSettings.setSpiedInstance(null);
 MockName expectedClassMockName = new MockName("MockClassToMock");
 Class<?> mock = mockUtil.createMock(clazzToMock, mockSettings);
 Class<?>[] interfaces = mock.getClass().getInterfaces();
 assertArrayEquals(new Class[] {}, interfaces);
 assertEquals(expectedClassMockName.getMockName(), clazzToMock.getName());
 }

 @Test
 public void testCreateMockWithSpiedInstanceAndExtraInterfaces() {
 Class<?> clazzToMock = TestUtils.TestClassToMock.class;
 MockSettingsImpl settings = mockSettings;
 settings.setSpiedInstance(spiedInstance);
 settings.setExtraInterfaces(new Class[] {TestUtils.TestExtraInterface.class});
 settings.setSerializable(true);

 MockName expectedMockName = new MockName("MockClassToMock_MockExtraInterface");
 MockName expectedClassMockName = new MockName("MockClassToMock");
 Class<?> mock = mockUtil.createMock(clazzToMock, settings);
 Class<?>[] interfaces = mock.getClass().getInterfaces();
 assertArrayEquals(new Class[] {Serializable.class, TestUtils.TestExtraInterface.class}, interfaces);
 assertEquals(expectedMockName.getMockName(), mock.getClass().getName());
 assertEquals(expectedClassMockName.getMockName(), clazzToMock.getName());
 }

 @Test
 public void testResetMock() {
 Object mock = mockUtil.createMock(TestUtils.TestClassToMock.class, mockSettings);
 MockHandlerInterface<Object> oldMockHandler = mockUtil.getMockHandler(mock);
 MockHandler<Object> newMockHandler = new MockHandler<Object>(oldMockHandler);
 MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, mockSettings);
 Factory factory = (Factory) mock;
 factory.setCallback(0, newFilter);
 assertTrue(mockUtil.isMockitoMock(mock));
 mockUtil.resetMock(mock);
 assertFalse(mockUtil.isMockitoMock(mock));
 }

 @Test
 public void testIsMock() {
 Object mock = mockUtil.createMock(TestUtils.TestClassToMock.class, mockSettings);
 assertTrue(mockUtil.isMock(mock));
 assertFalse(mockUtil.isMock(new Object()));
 }

 @Test
 public void testGetInterceptor() {
 Object mock = mockUtil.createMock(TestUtils.TestClassToMock.class, mockSettings);
 Factory factory = (Factory) mock;
 MethodInterceptorFilter interceptor = (MethodInterceptorFilter) mockUtil.getInterceptor(mock);
 assertEquals(interceptor.getHandler(), factory.getCallback(0));
 }

 @Test
 public void testGetMockHandler() {
 Object mock = mockUtil.createMock(TestUtils.TestClassToMock.class, mockSettings);
 MockHandlerInterface<Object> mockHandler = mockUtil.getMockHandler(mock);
 assertEquals(mockHandler.getMockSettings().getMockName().getMockName(), "MockClassToMock");
 }

 @Test
 public void testIsMockitoMock() {
 Object mock = mockUtil.createMock(TestUtils.TestClassToMock.class, mockSettings);
 assertTrue(mockUtil.isMockitoMock(mock));
 }

 @Test
 public void testGetMockName() {
 Object mock = mockUtil.createMock(TestUtils.TestClassToMock.class, mockSettings);
 MockName mockName = mockUtil.getMockName(mock);
 assertEquals(mockName.getMockName(), "MockClassToMock");
 }
}