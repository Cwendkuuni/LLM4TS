package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private ClassInstantiator classInstantiator;
    private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;

    @Before
    public void setUp() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        classInstantiator = mock(ClassInstantiator.class);
        cachingMockBytecodeGenerator = mock(CachingMockBytecodeGenerator.class);
        when(byteBuddyMockMaker.initializeClassInstantiator()).thenReturn(classInstantiator);
        when(classInstantiator.instantiate(MockitoTestClass.class)).thenReturn(new MockitoTestClass());
        when(cachingMockBytecodeGenerator.get(MockitoTestClass.class, new Class[0])).thenReturn(MockitoTestClassMock.class);
    }

    @Test
    public void testCreateMock() {
        MockHandler mockHandler = mock(MockHandler.class);
        MockCreationSettings<MockitoTestClass> settings = new MockCreationSettings.Builder<MockitoTestClass>().build();
        MockitoTestClassMock mock = byteBuddyMockMaker.createMock(settings, mockHandler);
        assertSame(MockitoTestClassMock.class, mock.getClass());
        verify(cachingMockBytecodeGenerator).get(MockitoTestClass.class, new Class[0]);
        verify(classInstantiator).instantiate(MockitoTestClassMock.class);
        verifyNoMoreInteractions(classInstantiator, cachingMockBytecodeGenerator);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableModeAcrossClassloaders() {
        MockHandler mockHandler = mock(MockHandler.class);
        MockCreationSettings<MockitoTestClass> settings = new MockCreationSettings.Builder<MockitoTestClass>()
                .serializableMode(SerializableMode.ACROSS_CLASSLOADERS)
                .build();
        byteBuddyMockMaker.createMock(settings, mockHandler);
    }

    @Test
    public void testGetHandler() {
        MockHandler mockHandler = mock(MockHandler.class);
        Object mock = spy(new MockitoTestClass());
        byteBuddyMockMaker.setMockitoInterceptor(mock, mockHandler, new MockCreationSettings.Builder<MockitoTestClass>().build());
        MockHandler returnedMockHandler = byteBuddyMockMaker.getHandler(mock);
        assertSame(mockHandler, returnedMockHandler);
    }

    @Test
    public void testResetMock() {
        MockHandler oldMockHandler = mock(MockHandler.class);
        MockHandler newMockHandler = mock(MockHandler.class);
        Object mock = spy(new MockitoTestClass());
        byteBuddyMockMaker.setMockitoInterceptor(mock, oldMockHandler, new MockCreationSettings.Builder<MockitoTestClass>().build());
        byteBuddyMockMaker.resetMock(mock, newMockHandler, new MockCreationSettings.Builder<MockitoTestClass>().build());
        MockHandler returnedMockHandler = byteBuddyMockMaker.getHandler(mock);
        assertSame(newMockHandler, returnedMockHandler);
    }

    @Test
    public void testDescribeClass() {
        Class<?> type = MockitoTestClass.class;
        assertSame(type.getCanonicalName(), byteBuddyMockMaker.describeClass(type));
    }

    @Test
    public void testDescribeClassNull() {
        assertSame("null", byteBuddyMockMaker.describeClass(null));
    }

    @Test
    public void testDescribeClassObject() {
        Object instance = new MockitoTestClass();
        assertSame(byteBuddyMockMaker.describeClass(instance.getClass()), byteBuddyMockMaker.describeClass(instance));
    }

    private static class MockitoTestClass {

    }

    private static class MockitoTestClassMock {

    }
}