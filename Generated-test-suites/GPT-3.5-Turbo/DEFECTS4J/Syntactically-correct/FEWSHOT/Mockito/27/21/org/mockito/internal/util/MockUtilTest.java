package org.mockito.internal.util;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {
    
    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock() {
        // Test case 1
        Class<String> classToMock1 = String.class;
        MockSettingsImpl settings1 = mock(MockSettingsImpl.class);
        when(settings1.isSerializable()).thenReturn(false);
        when(settings1.getExtraInterfaces()).thenReturn(null);
        when(settings1.getSpiedInstance()).thenReturn(null);

        try {
            String mock1 = mockUtil.createMock(classToMock1, settings1);
            assertNotNull(mock1);
            assertTrue(mockUtil.isMock(mock1));
        } catch (Exception e) {
            fail(e.getMessage());
        }

        // Test case 2
        Class<Integer> classToMock2 = Integer.class;
        Class<?>[] extraInterfaces2 = {Serializable.class};
        Object spiedInstance2 = "spiedInstance";

        MockSettingsImpl settings2 = mock(MockSettingsImpl.class);
        when(settings2.isSerializable()).thenReturn(true);
        when(settings2.getExtraInterfaces()).thenReturn(extraInterfaces2);
        when(settings2.getSpiedInstance()).thenReturn(spiedInstance2);

        try {
            Integer mock2 = mockUtil.createMock(classToMock2, settings2);
            assertNotNull(mock2);
            assertTrue(mockUtil.isMock(mock2));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testResetMock() {
        // Test case 1
        MockHandlerInterface<String> oldMockHandler1 = mock(MockHandlerInterface.class);
        MockHandler<String> newMockHandler1 = mock(MockHandler.class);
        MethodInterceptorFilter newFilter1 = mock(MethodInterceptorFilter.class);

        String mock1 = mock(String.class);
        when(mockUtil.getMockHandler(mock1)).thenReturn(oldMockHandler1);
        when(newMockHandler1.getHandler()).thenReturn(oldMockHandler1);
        when(newFilter1.getHandler()).thenReturn(oldMockHandler1);

        try {
            mockUtil.resetMock(mock1);
            verify((Factory) mock1).setCallback(0, newFilter1);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetMockHandler() {
        // Test case 1
        MockHandlerInterface<String> mockHandler1 = mock(MockHandlerInterface.class);
        String mock1 = mock(String.class);
        when(mockUtil.isMockitoMock(mock1)).thenReturn(true);
        when(mockUtil.getInterceptor(mock1)).thenReturn(null);
        when(mockHandler1.getMockSettings()).thenReturn(mock(MockSettingsImpl.class));

        try {
            MockHandlerInterface<String> actualMockHandler1 = mockUtil.getMockHandler(mock1);
            assertEquals(mockHandler1, actualMockHandler1);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        // Test case 2
        String mock2 = null;
        when(mockUtil.isMockitoMock(mock2)).thenReturn(false);

        try {
            mockUtil.getMockHandler(mock2);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }
    }

    @Test
    public void testIsMock() {
        // Test case 1
        String mock1 = mock(String.class);
        when(mockUtil.isMockitoMock(mock1)).thenReturn(true);

        try {
            boolean isMock1 = mockUtil.isMock(mock1);
            assertTrue(isMock1);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        // Test case 2
        Object nonMock = new Object();
        when(mockUtil.isMockitoMock(nonMock)).thenReturn(false);

        try {
            boolean isMock2 = mockUtil.isMock(nonMock);
            assertFalse(isMock2);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetMockName() {
        // Test case 1
        MockName mockName1 = mock(MockName.class);
        MockHandlerInterface<String> mockHandler1 = mock(MockHandlerInterface.class);
        when(mockUtil.getMockHandler(mock1)).thenReturn(mockHandler1);
        when(mockHandler1.getMockSettings()).thenReturn(mock(MockSettingsImpl.class));
        when(mockHandler1.getMockSettings().getMockName()).thenReturn(mockName1);

        try {
            MockName actualMockName1 = mockUtil.getMockName(mock1);
            assertEquals(mockName1, actualMockName1);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}