package org.mockito.internal.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.RETURNS_DEFAULTS;
import static org.mockito.Mockito.withSettings;

import org.junit.Test;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.MockName;
import org.mockito.internal.creation.Factory;
import org.mockito.internal.creation.MockCreationSettings;

import java.lang.reflect.Proxy;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl(classToMock, null, null, null);
        settings.setMockName("MockString");

        MockUtil mockUtil = new MockUtil();
        String mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);

        MockHandler<String> mockHandler = mockUtil.getMockHandler(mock);
        assertEquals("MockString", mockHandler.getMockSettings().getMockName());

        MockName mockName = mockUtil.getMockName(mock);
        assertEquals("MockString", mockName.getName());

        verify(mockHandler).addAnswer(Mockito.any());
    }

    @Test
    public void testCreateMockWithExtraInterfaces() {
        Class<String> classToMock = String.class;
        Class<Number> extraInterface = Number.class;
        MockSettingsImpl settings = new MockSettingsImpl(classToMock, new Class[]{extraInterface}, null, null);
        settings.setMockName("MockString");

        MockUtil mockUtil = new MockUtil();
        String mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);

        MockHandler<String> mockHandler = mockUtil.getMockHandler(mock);
        assertEquals("MockString", mockHandler.getMockSettings().getMockName());

        verify(mockHandler).addAnswer(Mockito.any());
        verify(mock).toString(); // Calling extra interface method
    }

    @Test
    public void testCreateMockWithSpiedInstance() {
        String spiedString = "Hello World";
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl(classToMock, null, null, spiedString);
        settings.setMockName("MockString");

        MockUtil mockUtil = new MockUtil();
        String mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);

        MockHandler<String> mockHandler = mockUtil.getMockHandler(mock);
        assertEquals("MockString", mockHandler.getMockSettings().getMockName());

        assertEquals(spiedString, mockHandler.getMockSettings().getSpiedInstance());

        verify(mockHandler).tansform(Mockito.anyString()); // Assuming there's a method named transform in the class
    }

    @Test
    public void testResetMock() {
        String mockString = mock(String.class);
        MockUtil mockUtil = new MockUtil();

        mockUtil.resetMock(mockString);

        MockHandlerInterface<String> oldMockHandler = mockUtil.getMockHandler(mockString);
        MockHandler<String> newMockHandler = new MockHandler<>(oldMockHandler);
        MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, withSettings().defaultAnswer(RETURNS_DEFAULTS));

        Factory factory = (Factory) mockString;
        factory.setCallback(0, newFilter);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMockWithNullMock() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.resetMock(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockitoMock() {
        Object nonMockitoMock = new Object();
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(nonMockitoMock);
    }

    @Test
    public void testIsMock() {
        String mockString = mock(String.class);
        assertTrue(MockUtil.isMock(mockString));

        Object nonMockitoMock = new Object();
        assertFalse(MockUtil.isMock(nonMockitoMock));
    }
}