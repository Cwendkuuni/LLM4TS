package org.mockito.internal.creation.bytebuddy;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    private MockMaker mockMaker;

    @Before
    public void setUp() {
        mockMaker = new ByteBuddyMockMaker();
    }

    @Test
    public void testCreateMockWithNullTypeToMock() {
        Assert.assertNull(mockMaker.createMock(new MockCreationSettings<>(null), null));
    }

    @Test
    public void testCreateMockWithNullHandler() {
        try {
            mockMaker.createMock(new MockCreationSettings<>(Object.class), null);
            Assert.fail("Expected MockitoException");
        } catch (MockitoException e) {
            Assert.assertEquals("MockHandler cannot be null", e.getMessage());
        }
    }

    @Test
    public void testCreateMockWithSerializableModeAcrossClassloaders() {
        try {
            mockMaker.createMock(new MockCreationSettings<>(Object.class, SerializableMode.ACROSS_CLASSLOADERS), null);
            Assert.fail("Expected MockitoException");
        } catch (MockitoException e) {
            Assert.assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testCreateMockWithValidTypeToMockAndHandler() {
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        Object mockedInstance = mockMaker.createMock(new MockCreationSettings<>(String.class), mockHandler);
        MockMethodInterceptor.MockAccess mockAccess = (MockMethodInterceptor.MockAccess) mockedInstance;
        Assert.assertNotNull(mockAccess);
        Assert.assertEquals(mockHandler, mockAccess.getMockitoInterceptor().getMockHandler());
    }

    @Test
    public void testGetHandlerWithNullMock() {
        Assert.assertNull(mockMaker.getHandler(null));
    }

    @Test
    public void testGetHandlerWithMock() {
        Object mock = mockMaker.createMock(new MockCreationSettings<>(String.class), Mockito.mock(MockHandler.class));
        MockHandler mockHandler = mockMaker.getHandler(mock);
        Assert.assertNotNull(mockHandler);
    }

    @Test
    public void testResetMockWithNullMockHandler() {
        Object mock = mockMaker.createMock(new MockCreationSettings<>(String.class), Mockito.mock(MockHandler.class));
        mockMaker.resetMock(mock, null, new MockCreationSettings<>(String.class, SerializableMode.PROXY));
        MockHandler newMockHandler = Mockito.mock(MockHandler.class);
        mockMaker.resetMock(mock, newMockHandler, new MockCreationSettings<>(String.class, SerialializableMode.PROXY));
        MockMethodInterceptor.MockAccess mockAccess = (MockMethodInterceptor.MockAccess) mock;
        Assert.assertEquals(newMockHandler, mockAccess.getMockitoInterceptor().getMockHandler());
    }

    @Test
    public void testResetMockWithNullMock() {
        Object mock = mockMaker.createMock(new MockCreationSettings<>(String.class), Mockito.mock(MockHandler.class));
        mockMaker.resetMock(null, Mockito.mock(MockHandler.class), new MockCreationSettings<>(String.class, SerialializableMode.PROXY));
        Assert.assertNull(mock);
    }

    @Test
    public void testInitializeClassInstantiatorWithNullConfiguration() {
        try {
            new ByteBuddyMockMaker().initializeClassInstantiator(null);
            Assert.fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            Assert.assertEquals("GlobalConfiguration cannot be null", e.getMessage());
        }
    }

    @Test
    public void testInitializeClassInstantiatorWithValidConfiguration() {
        GlobalConfiguration globalConfiguration = new GlobalConfiguration();
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.initializeClassInstantiator(globalConfiguration);
    }

    @Test
    public void testAsInternalMockHandlerWithNullMockHandler() {
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        try {
            mockMaker.asInternalMockHandler(null);
            Assert.fail("Expected MockitoException");
        } catch (MockitoException e) {
            Assert.assertEquals("At the moment you cannot provide own implementations of MockHandler", e.getMessage());
        }
    }
}