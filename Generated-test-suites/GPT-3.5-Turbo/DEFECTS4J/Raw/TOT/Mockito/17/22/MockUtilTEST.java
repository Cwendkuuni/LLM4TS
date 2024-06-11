
package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

import static org.mockito.Mockito.*;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setup() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock_shouldCreateMockWithGivenClass() {
        // Arrange
        Class<?> classToMock = MockUtil.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        Object mock = mockUtil.createMock(classToMock, settings);

        // Assert
        Assert.assertNotNull(mock);
        Assert.assertTrue(classToMock.isInstance(mock));
    }

    @Test
    public void testCreateMock_shouldThrowNotAMockExceptionWhenClassToMockIsNull() {
        // Arrange
        Class<?> classToMock = null;
        MockSettingsImpl settings = new MockSettingsImpl();

        try {
            // Act
            mockUtil.createMock(classToMock, settings);
        } catch (NotAMockException e) {
            // Assert
            Assert.assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }
    }

    @Test
    public void testCreateMock_shouldCloneSpiedInstanceWhenSpiedInstanceIsNotNull() {
        // Arrange
        Class<?> classToMock = MockUtil.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        Object spiedInstance = new MockUtil();

        // Act
        Object mock = mockUtil.createMock(classToMock, settings.spiedInstance(spiedInstance));

        // Assert
        Assert.assertNotSame(spiedInstance, mock);
    }

    @Test
    public void testResetMock_shouldResetMockBySettingNewFilterCallback() {
        // Arrange
        Object mock = mock(MockUtil.class);

        // Act
        mockUtil.resetMock(mock);

        // Assert
        MethodInterceptorFilter filter = mockUtil.getInterceptor(mock);
        Assert.assertNotNull(filter);
    }

    @Test
    public void testResetMock_shouldThrowNotAMockExceptionWhenMockIsNull() {
        // Arrange
        Object mock = null;

        try {
            // Act
            mockUtil.resetMock(mock);
        } catch (NotAMockException e) {
            // Assert
            Assert.assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }
    }

    @Test
    public void testIsMock_shouldReturnTrueWhenMockIsNotNullAndIsMockitoMock() {
        // Arrange
        Object mock = mock(MockUtil.class);

        // Act
        boolean isMock = mockUtil.isMock(mock);

        // Assert
        Assert.assertTrue(isMock);
    }

    @Test
    public void testIsMock_shouldReturnFalseWhenMockIsNull() {
        // Arrange
        Object mock = null;

        // Act
        boolean isMock = mockUtil.isMock(mock);

        // Assert
        Assert.assertFalse(isMock);
    }

    @Test
    public void testIsMock_shouldReturnFalseWhenMockIsNotNullButNotMockitoMock() {
        // Arrange
        Object mock = new Object();

        // Act
        boolean isMock = mockUtil.isMock(mock);

        // Assert
        Assert.assertFalse(isMock);
    }

    @Test
    public void testGetMockName_shouldReturnMockNameWhenGivenMockIsValidMock() {
        // Arrange
        Object mock = mock(MockUtil.class);

        // Act
        MockName mockName = mockUtil.getMockName(mock);

        // Assert
        Assert.assertNotNull(mockName);
    }

    @Test
    public void testGetMockName_shouldThrowNotAMockExceptionWhenMockIsNull() {
        // Arrange
        Object mock = null;

        try {
            // Act
            mockUtil.getMockName(mock);
        } catch (NotAMockException e) {
            // Assert
            Assert.assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }
    }

}

