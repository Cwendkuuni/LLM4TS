
package org.mockito.internal.creation.bytebuddy;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);

        try {
            mockMaker.createMock(settings, mockHandler);
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings settings = mock(MockCreationSettings.class);
        Object mock = new Object();

        try {
            mockMaker.ensureMockIsAssignableToMockedType(settings, mock);
        } catch (ClassCastException e) {
            assertEquals("java.lang.Object cannot be cast to class java.lang.Object", e.getMessage());
        }
    }

    @Test
    public void testGetHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = mock(MockHandler.class);
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);

        when(mockAccess.getMockitoInterceptor()).thenReturn(mockHandler);

        assertEquals(mockHandler, mockMaker.getHandler(mockAccess));
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings settings = mock(MockCreationSettings.class);
        MockHandler mockHandler = mock(MockHandler.class);
        MockMethodInterceptor.MockAccess mockAccess = mock(MockMethodInterceptor.MockAccess.class);

        try {
            mockMaker.resetMock(mockAccess, mockHandler, settings);
        } catch (MockitoException e) {
            assertEquals("At the moment you cannot provide own implementations of MockHandler. Please see the javadocs for the MockMaker interface.", e.getMessage());
        }
    }
}
