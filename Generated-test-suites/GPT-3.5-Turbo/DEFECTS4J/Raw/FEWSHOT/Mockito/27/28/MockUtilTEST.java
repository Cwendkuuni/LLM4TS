package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setup() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock() {
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        String mock = mockUtil.createMock(classToMock, settings);
        
        assertNotNull(mock);
        verify(mockUtil.getMockHandler(mock), times(1)).getMockSettings();
        verify(mockUtil.getMockHandler(mock), times(1)).getHandler();
        verify(mockUtil.getMockHandler(mock), times(1)).getInterceptedMethodMatcher();
    }

    @Test
    public void testResetMock() {
        String mock = mock(String.class);
        when(mock.length()).thenReturn(5);

        mockUtil.resetMock(mock);

        verify(mock, times(1)).length();
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testIsMock() {
        String mock = mock(String.class);

        assertEquals(true, mockUtil.isMock(mock));
        assertEquals(false, mockUtil.isMock("Not a mock"));
        assertEquals(false, mockUtil.isMock(null));
    }

    @Test
    public void testGetMockName() {
        String mock = mock(String.class);

        MockName mockName = mockUtil.getMockName(mock);

        assertNotNull(mockName);
        verify(mockUtil.getMockHandler(mock), times(1)).getMockSettings();
    }
}