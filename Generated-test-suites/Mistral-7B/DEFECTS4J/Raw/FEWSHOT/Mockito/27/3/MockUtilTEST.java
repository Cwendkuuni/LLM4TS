package org.mockito.internal.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.MockName;
import org.mockito.internal.MockCreationValidator;
import org.mockito.cglib.proxy.Factory;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;

import java.io.Serializable;
import java.util.Arrays;

public class MockUtilTest {

 MockUtil mockUtil = new MockUtil();
 MockCreationValidator creationValidator = mock(MockCreationValidator.class);
 MockSettingsImpl settings = new MockSettingsImpl();
 Class<?> dummyClass = DummyClass.class;

 @Test
 public void testCreateMock() {
 MockHandler<DummyClass> mockHandler = mock(MockHandler.class);
 when(mockHandler.getMockSettings()).thenReturn(settings);
 when(mockUtil.getMockHandler(any())).thenReturn(mockHandler);

 DummyClass mock = mockUtil.createMock(dummyClass, settings);
 assertNotNull(mock);
 verify(mockUtil, times(2)).validateType(dummyClass);
 verify(mockUtil, times(1)).validateExtraInterfaces(dummyClass, settings.getExtraInterfaces());
 verify(mockUtil, times(1)).validateMockedType(dummyClass, settings.getSpiedInstance());
 verify(mockUtil, times(1)).initiateMockName(dummyClass);
 verify(ClassImposterizer.INSTANCE, times(1)).imposterise(any(), eq(dummyClass), anyArray());
 if (settings.getSpiedInstance() != null) {
 verify(new LenientCopyTool(), times(1)).copyToMock(settings.getSpiedInstance(), mock);
 }
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClass() {
 mockUtil.createMock(null, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidClass() {
 settings.setSpiedInstance(new Object());
 mockUtil.createMock(Integer.class, settings);
 }

 @Test
 public void testCreateMockWithExtraInterfaces() {
 settings.setSpiedInstance(new Object());
 settings.setExtraInterfaces(new Class[]{Serializable.class, DummyInterface.class});
 DummyClass mock = mockUtil.createMock(dummyClass, settings);
 assertNotNull(mock);
 verify(mockUtil, times(2)).validateType(dummyClass);
 verify(mockUtil, times(2)).validateExtraInterfaces(dummyClass, anyArray());
 verify(mockUtil, times(1)).validateMockedType(dummyClass, eq(settings.getSpiedInstance()));
 verify(mockUtil, times(1)).initiateMockName(dummyClass);
 verify(ClassImposterizer.INSTANCE, times(1)).imposterise(any(), eq(dummyClass), anyArray());
 if (settings.getSpiedInstance() != null) {
 verify(new LenientCopyTool(), times(1)).copyToMock(settings.getSpiedInstance(), mock);
 }
 }

 @Test
 public void testResetMock() {
 DummyClass mock = mock(DummyClass.class);
 mockUtil.resetMock(mock);
 MethodInterceptorFilter newFilter = anyMethodInterceptorFilter();
 verify(mock, times(1)).setCallback(eq(0), eq(newFilter));
 }

 @Test
 public void testGetMockHandler() {
 DummyClass mock = mock(DummyClass.class);
 when(getInterceptor(mock)).thenReturn(anyMethodInterceptorFilter());
 MockHandler<DummyClass> mockHandler = mockUtil.getMockHandler(mock);
 assertNotNull(mockHandler);
 assertEquals(anyMethodInterceptorFilter(), mockHandler.getInterceptor());
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() {
 mockUtil.getMockHandler(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNotAMock() {
 DummyClass notAMock = new DummyClass() {};
 mockUtil.getMockHandler(notAMock);
 }

 @Test
 public void testIsMock() {
 assertFalse(mockUtil.isMock(new Object()));
 assertTrue(mockUtil.isMock(mock(DummyClass.class)));
 }

 @Test
 public void testIsMockitoMock() {
 assertFalse(mockUtil.isMockitoMock(new Object()));
 assertTrue(mockUtil.isMockitoMock(mock(DummyClass.class)));
 }

 @Test
 public void testGetInterceptor() {
 DummyClass mock = mock(DummyClass.class);
 when(getInterceptor(mock)).thenReturn(anyMethodInterceptorFilter());
 MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
 assertSame(anyMethodInterceptorFilter(), interceptor);
 }

 @Test(expected = NullPointerException.class)
 public void testGetInterceptorWithNullMock() {
 mockUtil.getInterceptor(null);
 }

 @Test
 public void testGetMockName() {
 DummyClass mock = mock(DummyClass.class);
 when(mockUtil.getMockHandler(mock)).thenReturn(mock(MockHandler.class));
 when(mockHandler.getMockSettings()).thenReturn(settings);
 when(settings.getMockName()).thenReturn(new MockName("mockName"));
 MockName name = mockUtil.getMockName(mock);
 assertEquals("mockName", name.getName());
 }

 @Test
 public void testValidateType() {
 Class<?> dummyClass = DummyClass.class;
 mockUtil.validateType(dummyClass);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValidateTypeWithNullClass() {
 mockUtil.validateType(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValidateTypeWithInvalidClass() {
 mockUtil.validateType(Integer.class);
 }

 @Test
 public void testValidateMockedType() {
 mockUtil.validateMockedType(DummyClass.class, new Object());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValidateMockedTypeWithNullClass() {
 mockUtil.validateMockedType(null, new Object());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValidateMockedTypeWithNullInstance() {
 mockUtil.validateMockedType(DummyClass.class, null);
 }

 @Test
 public void testValidateExtraInterfaces() {
 mockUtil.validateExtraInterfaces(DummyClass.class, new Class[0]);
 mockUtil.validateExtraInterfaces(DummyClass.class, new Class[]{Serializable.class});
 }

 @Test(expected = IllegalArgumentException.class)
 public void testValidateExtraInterfacesWithNullClass() {
 mockUtil.validateExtraInterfaces(null, new Class[0]);
 }

 @Test
 public void testValidateExtraInterfacesWithInvalidClass() {
 mockUtil.validateExtraInterfaces(DummyClass.class, Integer.class);
 }

 private MethodInterceptorFilter anyMethodInterceptorFilter() {
 return spy(MethodInterceptorFilter.class);
 }
}