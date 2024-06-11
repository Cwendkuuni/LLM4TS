package org.mockito.internal.util.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.MockitoCreationSettings;
import org.mockito.internal.util.MockName;
import org.mockito.internal.creation.Factory;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.CreationValidator;
import org.mockito.internal.creation.MethodInterceptor;
import org.mockito.internal.util.creation.ClassImposterizer;

import java.lang.reflect.Proxy;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MockUtilTest {

 private MockUtil mockUtil;
 private CreationValidator creationValidatorMock;
 private MockitoCreationSettings settingsMock;
 private Factory factoryMock;
 private LenientCopyTool lenientCopyToolMock;
 private ClassImposterizer classImposterizerMock;

 @Before
 public void setup() {
 mockUtil = new MockUtil();
 creationValidatorMock = mock(CreationValidator.class);
 settingsMock = mock(MockitoCreationSettings.class);
 factoryMock = mock(Factory.class);
 lenientCopyToolMock = mock(LenientCopyTool.class);
 classImposterizerMock = mock(ClassImposterizer.class);
 when(creationValidatorMock.validateType(any())).thenAnswer(invocation -> invocation.getArgument(0));
 when(creationValidatorMock.validateExtraInterfaces(any(), any()))
 .thenAnswer(invocation -> Arrays.asList((Class<?>) invocation.getArgument(1)));
 when(creationValidatorMock.validateMockedType(any(), any()))
 .thenAnswer(invocation -> invocation.getArgument(0));
 when(settingsMock.getExtraInterfaces()).thenReturn(new Class[0]);
 when(settingsMock.getSpiedInstance()).thenReturn(mock(Object.class));
 when(settingsMock.getMockName()).thenReturn(mock(MockName.class));
 when(factoryMock.getCallback(0)).thenReturn(mock(MethodInterceptorFilter.class));
 when(lenientCopyToolMock.copyToMock(any(), any()))
 .then(invocation -> invocation.getArgument(1));
 when(classImposterizerMock.imposterise(any(), any(), anyArray()))
 .thenAnswer(invocation -> invocation.getArguments()[1]);
 mockUtil = new MockUtil(creationValidatorMock);
 }

 @Test
 public void testCreateMock_ValidArgument() {
 Class<Object> classToMock = Object.class;
 MockSettingsImpl settings = settingsMock;
 Object spiedInstance = mock(Object.class);
 Object mock = mockUtil.createMock(classToMock, settings);
 assertTrue(isMockitoMock(mock));
 assertNotNull(mock);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_NullArgument() {
 Class<Object> classToMock = Object.class;
 MockSettingsImpl settings = settingsMock;
 Object spiedInstance = null;
 mockUtil.createMock(classToMock, settings, spiedInstance);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_NonMockObject() {
 Class<Object> classToMock = Object.class;
 MockSettingsImpl settings = settingsMock;
 Object spiedInstance = new Object();
 mockUtil.createMock(classToMock, settings, spiedInstance);
 }

 @Test
 public void testResetMock() {
 Object mock = mock(Object.class);
 mockUtil.resetMock(mock);
 assertNotNull(mock);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandler_NullArgument() {
 Object mock = null;
 mockUtil.getMockHandler(mock);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandler_NonMockObject() {
 Object mock = new Object();
 mockUtil.getMockHandler(mock);
 }

 @Test
 public void testIsMock() {
 Object mock = mock(Object.class);
 assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testIsMock_NullArgument() {
 Object mock = null;
 assertFalse(mockUtil.isMock(mock));
 }

 @Test
 public void testGetInterceptor() {
 Object mock = mock(Object.class);
 assertNotNull(mockUtil.getInterceptor(mock));
 }

 @Test
 public void testGetMockName() {
 Object mock = mock(Object.class);
 MockName mockName = mockUtil.getMockName(mock);
 assertNotNull(mockName);
 }
}