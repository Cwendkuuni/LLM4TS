package org.mockito.internal.creation.bytebuddy;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import org.mockito.Mockito;
import org.junit.Test;
import org.junit.Assert;
import java.lang.reflect.Constructor;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock_SerializationException() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings mockSettings = Mockito.mock(MockCreationSettings.class);
        Mockito.when(mockSettings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);
        
        try {
            mockMaker.createMock(mockSettings, Mockito.mock(InternalMockHandler.class));
            Assert.fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            Assert.assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testCreateMock_ClassCastException() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings mockSettings = Mockito.mock(MockCreationSettings.class);
        Class typeToMock = Mockito.mock(Class.class);
        Mockito.when(mockSettings.getTypeToMock()).thenReturn(typeToMock);
        Mockito.when(typeToMock.getSimpleName()).thenReturn("TestClass");
        
        try {
            mockMaker.createMock(mockSettings, Mockito.mock(InternalMockHandler.class));
            Assert.fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            Assert.assertTrue(e.getMessage().contains("ClassCastException occurred while creating the mockito mock"));
        }
    }

    @Test
    public void testCreateMock_InstantiationException() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings mockSettings = Mockito.mock(MockCreationSettings.class);
        Class typeToMock = Mockito.mock(Class.class);
        Mockito.when(mockSettings.getTypeToMock()).thenReturn(typeToMock);
        Mockito.when(typeToMock.getSuperclass()).thenReturn(Object.class);
        
        try {
            mockMaker.createMock(mockSettings, Mockito.mock(InternalMockHandler.class));
            Assert.fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            Assert.assertTrue(e.getMessage().contains("Unable to create mock instance of type 'Object'"));
        }
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings mockSettings = Mockito.mock(MockCreationSettings.class);
        Class typeToMock = Mockito.mock(Class.class);
        Mockito.when(mockSettings.getTypeToMock()).thenReturn(typeToMock);
        Object mockObject = Mockito.mock(Object.class);
        
        Object result = mockMaker.ensureMockIsAssignableToMockedType(mockSettings, mockObject);
        
        Assert.assertEquals(mockObject, result);
        Mockito.verify(mockSettings, Mockito.times(1)).getTypeToMock();
        Mockito.verify(typeToMock, Mockito.times(1)).cast(mockObject);
    }

    @Test
    public void testDescribeClass_TypeIsNull() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Class type = null;
        
        String result = mockMaker.describeClass(type);
        
        Assert.assertEquals("null", result);
    }

    @Test
    public void testDescribeClass_InstanceIsNull() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object instance = null;
        
        String result = mockMaker.describeClass(instance);
        
        Assert.assertEquals("null", result);
    }

    @Test
    public void testGetHandler_NotMockMethodInterceptor() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = Mockito.mock(Object.class);
        
        MockHandler result = mockMaker.getHandler(mock);
        
        Assert.assertNull(result);
    }

    @Test
    public void testGetHandler_MockMethodInterceptor() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockMethodInterceptor.MockAccess mockMethodInterceptor = Mockito.mock(MockMethodInterceptor.MockAccess.class);
        Mockito.when(mockMethodInterceptor.getMockitoInterceptor()).thenReturn(Mockito.mock(MockMethodInterceptor.class));
        Object mock = mockMethodInterceptor;
        
        MockHandler result = mockMaker.getHandler(mock);
        
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof InternalMockHandler);
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = Mockito.mock(MockMethodInterceptor.MockAccess.class);
        MockHandler newHandler = Mockito.mock(MockHandler.class);
        MockCreationSettings settings = Mockito.mock(MockCreationSettings.class);
        Mockito.when(settings.getTypeToMock()).thenReturn(Mockito.mock(Class.class));
        
        mockMaker.resetMock(mock, newHandler, settings);
        
        Mockito.verify(mock, Mockito.times(1)).setMockitoInterceptor(Mockito.any(MockMethodInterceptor.class));
    }

    @Test
    public void testInitializeClassInstantiator_Objenesis() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Construction construction = Mockito.mock(Construction.class);
        ClassInstantiator classInstantiator = Mockito.mock(ClassInstantiator.class);
        Mockito.when(classInstantiator.enableClassCache()).thenReturn(true);
        Mockito.when(construction.forClassType(ClassInstantiator.class)).thenReturn(classInstantiator);
        GlobalConfiguration globalConfiguration = new GlobalConfiguration(construction);
        
        ClassInstantiator result = mockMaker.initializeClassInstantiator();
        
        Assert.assertEquals(classInstantiator, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiator_Throwable() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Construction construction = Mockito.mock(Construction.class);
        Mockito.when(construction.forClassType(ClassInstantiator.class)).thenThrow(new Throwable());
        GlobalConfiguration globalConfiguration = new GlobalConfiguration(construction);
        
        mockMaker.initializeClassInstantiator();
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandler_NotInternalMockHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        
        mockMaker.asInternalMockHandler(mockHandler);
    }

    @Test
    public void testAsInternalMockHandler_InternalMockHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        InternalMockHandler mockHandler = Mockito.mock(InternalMockHandler.class);
        
        InternalMockHandler result = mockMaker.asInternalMockHandler(mockHandler);
        
        Assert.assertEquals(mockHandler, result);
    }

}