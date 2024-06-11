package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import org.mockito.Mockito;
import org.junit.Before;
import org.junit.Test;

public class MockUtilTest {
    private MockUtil mockUtil;
    private MockCreationValidator creationValidator;

    @Before
    public void setUp() {
        creationValidator = Mockito.mock(MockCreationValidator.class);
        mockUtil = new MockUtil(creationValidator);
    }

    @Test
    public void testCreateMock() {
        // Test case when the extra interfaces are null
        Class<?> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        String mock = mockUtil.createMock(classToMock, settings);
        assertEquals(classToMock, mock.getClass());

        // Test case when the extra interfaces are not null
        Class<?>[] extraInterfaces = {Serializable.class};
        settings.setExtraInterfaces(extraInterfaces);
        mock = mockUtil.createMock(classToMock, settings);
        assertEquals(classToMock, mock.getClass());
        assertEquals(extraInterfaces.length, ((Factory) mock).getInterfaces().length);
        assertEquals(Serializable.class, ((Factory) mock).getInterfaces()[0]);

        // Test case when the spied instance is not null
        Object spiedInstance = Mockito.mock(Object.class);
        settings.setSpiedInstance(spiedInstance);
        mock = mockUtil.createMock(classToMock, settings);
        assertEquals(classToMock, mock.getClass());
        assertEquals(spiedInstance, settings.getSpiedInstance());

        // Test case when the class is serializable
        settings.setExtraInterfaces(null);
        settings.setSerializable(true);
        mock = mockUtil.createMock(classToMock, settings);
        assertEquals(classToMock, mock.getClass());
        assertEquals(Serializable.class, ((Factory) mock).getInterfaces()[0]);
    }

    @Test
    public void testResetMock() {
        // Test case when the mock is null
        Integer mock = null;
        try {
            mockUtil.resetMock(mock);
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }

        // Test case when the mock is a Mockito mock
        mock = Mockito.mock(Integer.class);
        mockUtil.resetMock(mock);
        assertEquals(MockHandlerInterface.class, mock.getClass());
    }

    @Test
    public void testGetMethodInterceptorFilter() {
        // Test case when the mock is not a Factory
        Integer mock = Mockito.mock(Integer.class);
        MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
        assertEquals(null, interceptor);

        // Test case when the mock is a Factory
        mock = Mockito.mock(Integer.class, Mockito.withSettings().extraInterfaces(Serializable.class));
        interceptor = mockUtil.getInterceptor(mock);
        assertEquals(MethodInterceptorFilter.class, interceptor.getClass());
    }

    @Test
    public void testGetMockHandler() {
        // Test case when the mock is null
        Integer mock = null;
        try {
            mockUtil.getMockHandler(mock);
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }

        // Test case when the mock is a Mockito mock
        mock = Mockito.mock(Integer.class);
        MockHandlerInterface<?> handler = mockUtil.getMockHandler(mock);
        assertEquals(MockHandler.class, handler.getClass());

        // Test case when the mock is a non-Mockito mock
        mock = 123;
        try {
            mockUtil.getMockHandler(mock);
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is: class java.lang.Integer", e.getMessage());
        }
    }

    @Test
    public void testIsMock() {
        // Test case when the mock is null
        Integer mock = null;
        boolean isMock = mockUtil.isMock(mock);
        assertEquals(false, isMock);

        // Test case when the mock is a Mockito mock
        mock = Mockito.mock(Integer.class);
        isMock = mockUtil.isMock(mock);
        assertEquals(true, isMock);
    }

    @Test
    public void testGetMockName() {
        // Test case when the mock is a Mockito mock
        Integer mock = Mockito.mock(Integer.class);
        MockName mockName = mockUtil.getMockName(mock);
        assertEquals(MockName.class, mockName.getClass());
    }
}