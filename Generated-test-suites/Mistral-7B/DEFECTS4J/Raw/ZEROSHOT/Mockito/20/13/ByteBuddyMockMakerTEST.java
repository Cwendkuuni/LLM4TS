package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerImpl;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private ClassInstantiator classInstantiator;
    private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;

    @Before
    public void setup() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        classInstantiator = mock(ClassInstantiator.class);
        cachingMockBytecodeGenerator = mock(CachingMockBytecodeGenerator.class);
        new GlobalConfiguration().enableClassCache();
        when(byteBuddyMockMaker.initializeClassInstantiator()).thenReturn(classInstantiator);
        when(byteBuddyMockMaker.new CachingMockBytecodeGenerator()).thenReturn(cachingMockBytecodeGenerator);
    }

    @Test
    public void testCreateMockSuccess() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder().withTypeToMock(String.class).build();
        MockHandler handler = mock(MockHandler.class);
        Object mock = byteBuddyMockMaker.createMock(settings, handler);
        assertThat(mock, isInstanceOf(String.class));
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableModeAcrossClassLoadersFailure() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder().withTypeToMock(String.class)
                .withSerializableMode(SerializableMode.ACROSS_CLASSLOADERS).build();
        MockHandler handler = mock(MockHandler.class);
        byteBuddyMockMaker.createMock(settings, handler);
    }

    @Test
    public void testGetHandler() {
        Object mock = createMock();
        MockHandler handler = byteBuddyMockMaker.getHandler(mock);
        assert handler != null;
    }

    @Test
    public void testResetMock() {
        Object mock = createMock();
        MockHandler newHandler = mock(MockHandler.class);
        byteBuddyMockMaker.resetMock(mock, newHandler, createSettings());
        MockHandler oldHandler = byteBuddyMockMaker.getHandler(mock);
        assertThat(oldHandler, is(not(newHandler)));
    }

    @Test
    public void testDescribeClass() {
        Class<?> mockClass = Mockito.mock(String.class);
        Object proxyInstance = Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { String.class }, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return null;
            }
        });
        Object instance = createMock();
        Object nullInstance = null;

        String actualMockClassDescription = byteBuddyMockMaker.describeClass(mockClass);
        String actualProxyInstanceDescription = byteBuddyMockMaker.describeClass(proxyInstance);
        String actualNullInstanceDescription = byteBuddyMockMaker.describeClass(nullInstance);

        String expectedMockClassDescription = "'java.lang.String', loaded by classloader : 'mockito.internal.bytebuddy.ByteBuddyMockMakerTest$1'";
        String expectedProxyInstanceDescription = "'java.lang.reflect.Proxy$InvocationHandler', loaded by classloader : 'mockito.internal.bytebuddy.ByteBuddyMockMakerTest$1'";
        String expectedNullInstanceDescription = "null";

        assertThat(actualMockClassDescription, is(equalTo(expectedMockClassDescription)));
        assertThat(actualProxyInstanceDescription, is(equalTo(expectedProxyInstanceDescription)));
        assertThat(actualNullInstanceDescription, is(equalTo(expectedNullInstanceDescription)));
    }

    private Object createMock() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder().withTypeToMock(String.class).build();
        MockHandler handler = mock(MockHandler.class);
        return byteBuddyMockMaker.createMock(settings, handler);
    }

    private MockCreationSettings<Object> createSettings() {
        return new MockCreationSettings.Builder().withTypeToMock(String.class).build();
    }
}