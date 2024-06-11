package org.mockito.internal.util;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class MockUtilTest {
    
    private MockUtil mockUtil;
    
    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }
    
    @Test
    public void testCreateMock() {
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        
        mockUtil.createMock(classToMock, settings);
        
        verify(mockUtil.creationValidator).validateType(classToMock);
        verify(mockUtil.creationValidator).validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        verify(mockUtil.creationValidator).validateMockedType(classToMock, settings.getSpiedInstance());
        verify(settings).initiateMockName(classToMock);
        verify(mockUtil, times(2)).getMockHandler(any());
        verify(mockUtil).isMockitoMock(any());
        verify(Enhancer.class).isEnhanced(classToMock);
        verify(mockUtil).getInterceptor(any());
        verify(ClassImposterizer.INSTANCE).imposterise(any(), eq(classToMock), any());
        verify(mockUtil).resetMock(any());
        verify(settings).getSpiedInstance();
        verify(new LenientCopyTool()).copyToMock(any(), any());
    }

    @Test
    public void testResetMock() {
        String mock = mock(String.class);
        
        mockUtil.resetMock(mock);
        
        verify(mockUtil).getMockHandler(mock);
        verify(getMockHandler(mock)).getHandler();
        verify(mock).setCallback(0, any());
        verify(withSettings(), times(2)).defaultAnswer(RETURNS_DEFAULTS);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMockWithNullMock() {
        mockUtil.resetMock(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockitoMock() {
        Object mock = new Object();
        
        mockUtil.getMockHandler(mock);
    }

    @Test
    public void testIsMock() {
        String mock = mock(String.class);
        
        boolean result = mockUtil.isMock(mock);
        
        assertTrue(result);
        verify(mockUtil).isMockitoMock(mock);
    }

    @Test
    public void testIsMockWithNonMockitoMock() {
        Object mock = new Object();
        
        boolean result = mockUtil.isMock(mock);
        
        assertFalse(result);
        verify(mockUtil).isMockitoMock(mock);
    }

    @Test
    public void testGetMockName() {
        String mock = mock(String.class);
        
        mockUtil.getMockName(mock);
        
        verify(mockUtil).getMockHandler(mock);
        verify(getMockHandler(mock)).getMockSettings();
        verify(mockUtil.getMockName(mock)).getMockName();
    }
    
}