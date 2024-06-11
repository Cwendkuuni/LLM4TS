import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.ObjenesisClassInstantiator;
import org.mockito.internal.creation.instance.mock.*;
import org.mockito.internal.creation.instance.DefaultInstantiatorProvider;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker mockMaker;
    private MockCreationSettings<TestClass> settings;
    private MockHandler handler;

    @Before
    public void setUp() {
        mockMaker = new ByteBuddyMockMaker();
        handler = mock(MockHandler.class);
        settings = mock(MockCreationSettings.class);
        when(settings.getSerializableMode()).thenReturn(SerializableMode.NONE);
        when(settings.getTypeToMock()).thenReturn(TestClass.class);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_SerializableModeAcrossClassloaders() {
        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);
        mockMaker.createMock(settings, handler);
    }

    @Test
    public void testCreateMock_Success() {
        TestClass mock = mockMaker.createMock(settings, handler);
        assertNotNull(mock);
        assertTrue(mock instanceof TestClass);

        MockHandler actualHandler = mockMaker.getHandler(mock);
        assertEquals(handler, actualHandler);

        mockMaker.resetMock(mock, handler, settings);
        MockHandler newHandler = mockMaker.getHandler(mock);
        assertEquals(handler, newHandler);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_ClassCastException() {
        ClassInstantiator classInstantiator = mock(ClassInstantiator.class);
        when(classInstantiator.instantiate(TestClass.class)).thenReturn((TestClass) new Object());
        mockMaker.setClassInstantiator(classInstantiator);

        mockMaker.createMock(settings, handler);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_InstantiationException() {
        ClassInstantiator classInstantiator = mock(ClassInstantiator.class);
        when(classInstantiator.instantiate(TestClass.class)).thenThrow(InstantiationException.class);
        mockMaker.setClassInstantiator(classInstantiator);

        mockMaker.createMock(settings, handler);
    }

    @Test(expected = MockitoException.class)
    public void testGetHandler_NotMockMethodInterceptor() {
        mockMaker.getHandler(new Object());
    }

    @Test(expected = MockitoException.class)
    public void testResetMock_NotMockMethodInterceptor() {
        Object mock = new Object();
        mockMaker.resetMock(mock, handler, settings);
    }

    private class TestClass {}

}