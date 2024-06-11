package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.Configuration;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private MockCreationSettings mockCreationSettings;

    @Before
    public void setUp() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        mockCreationSettings = mock(MockCreationSettings.class);
        when(mockCreationSettings.getSerializableMode()).thenReturn(SerializableMode.STUBS);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithAcrossClassloadersSerializableMode() {
        when(mockCreationSettings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);
        byteBuddyMockMaker.createMock(mockCreationSettings, null);
    }
    
    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        assertEquals(Object.class, byteBuddyMockMaker.ensureMockIsAssignableToMockedType(mockCreationSettings, new Object()));
    }

    @Test
    public void testDescribeClassWithNull() {
        assertEquals("null", byteBuddyMockMaker.describeClass(null));
    }

    @Test
    public void testDescribeClassWithType() {
        assertEquals("'java.lang.Object', loaded by classloader : 'null'", byteBuddyMockMaker.describeClass(Object.class));
    }

    @Test
    public void testDescribeClassWithInstance() {
        Object object = new Object();
        assertEquals("'java.lang.Object', loaded by classloader : 'null'", byteBuddyMockMaker.describeClass(object));
    }

    @Test
    public void testGetHandlerWithNonMockAccessMock() {
        MockHandler handler = byteBuddyMockMaker.getHandler(new Object());
        assertEquals(null, handler);
    }

    @Test
    public void testResetMock() {
        Object mock = mock(Object.class);
        MockHandler newHandler = mock(MockHandler.class);
        byteBuddyMockMaker.resetMock(mock, newHandler, mockCreationSettings);
        verify(mock).setMockitoInterceptor(any(MockMethodInterceptor.class));
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandlerWithNonInternalMockHandler() {
        MockHandler mockHandler = mock(MockHandler.class);
        byteBuddyMockMaker.asInternalMockHandler(mockHandler);
    }
}