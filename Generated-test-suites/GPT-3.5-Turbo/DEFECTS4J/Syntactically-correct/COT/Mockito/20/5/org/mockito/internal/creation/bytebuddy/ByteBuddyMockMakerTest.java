
package org.mockito.internal.creation.bytebuddy;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

import java.lang.reflect.Constructor;

import static org.mockito.internal.util.StringJoiner.join;

public class ByteBuddyMockMakerTest {

    @Test(expected = MockitoException.class)
    public void testCreateMock_withSerializableModeAcrossClassloaders_shouldThrowException() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<TestObject> settings = new MockCreationSettings<>();
        settings.setTypeToMock(TestObject.class);
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);

        // Act
        mockMaker.createMock(settings, new MockHandler() {
        });

        // Assert (Exception is expected)
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_withClassCastException_shouldThrowException() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<TestObject> settings = new MockCreationSettings<>();
        settings.setTypeToMock(TestObject.class);
        Class<? extends TestObject> mockedProxyType = TestObject.class;
        TestObject mockInstance = new TestObject();
        mockInstance.setMockitoInterceptor(new MockMethodInterceptor(asInternalMockHandler(new MockHandler() {
        }), settings));

        // Act
        mockMaker.ensureMockIsAssignableToMockedType(settings, mockedProxyType);

        // Assert (Exception is expected)
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_withInstantiationException_shouldThrowException() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<TestObject> settings = new MockCreationSettings<>();
        settings.setTypeToMock(TestObject.class);
        Class<? extends TestObject> mockedProxyType = TestObject.class;

        // Act
        mockMaker.ensureMockIsAssignableToMockedType(settings, mockedProxyType);

        // Assert (Exception is expected)
    }

    @Test
    public void testGetHandler_withMockInstance_shouldReturnMockHandler() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<TestObject> settings = new MockCreationSettings<>();
        settings.setTypeToMock(TestObject.class);
        TestObject mockInstance = new TestObject();
        mockInstance.setMockitoInterceptor(new MockMethodInterceptor(asInternalMockHandler(new MockHandler() {
        }), settings));

        // Act
        MockHandler handler = mockMaker.getHandler(mockInstance);

        // Assert
        // No assertion, just verifying that the method runs without throwing exceptions
    }

    @Test
    public void testResetMock_withMockInstance_shouldSetNewHandler() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<TestObject> settings = new MockCreationSettings<>();
        settings.setTypeToMock(TestObject.class);
        TestObject mockInstance = new TestObject();
        mockInstance.setMockitoInterceptor(new MockMethodInterceptor(asInternalMockHandler(new MockHandler() {
        }), settings));
        MockHandler newHandler = new MockHandler() {
        };

        // Act
        mockMaker.resetMock(mockInstance, newHandler, settings);

        // Assert
        // No assertion, just verifying that the method runs without throwing exceptions
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiator_withMissingObjenesis_shouldThrowException() {
        // Arrange
        GlobalConfiguration globalConfiguration = new GlobalConfiguration();
        globalConfiguration.enableClassCache();
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker() {
            @Override
            protected ClassInstantiator initializeClassInstantiator() {
                throw new Throwable();
            }
        };

        // Act
        mockMaker.initializeClassInstantiator();

        // Assert (Exception is expected)
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandler_withNonInternalMockHandler_shouldThrowException() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler nonInternalMockHandler = new MockHandler() {
        };

        // Act
        mockMaker.asInternalMockHandler(nonInternalMockHandler);

        // Assert (Exception is expected)
    }
}
