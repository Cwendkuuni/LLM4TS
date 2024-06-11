import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;
import org.mockito.MockHandler;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.bytebuddy.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.bytebuddy.ClassInstantiator;
import org.mockito.internal.creation.bytebuddy.MockMethodInterceptor;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.util.StringJoiner;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerAnnotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Arrays;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMockNormalScenario() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        Type typeToMock = String.class;
        MockCreationSettings settings = MockMakerAnnotations.getSettings(Mockito.mock(Type.class));
        settings.setSerializable(false);

        Object mockedInstance = byteBuddyMockMaker.createMock(settings, mockHandler);

        Assert.assertNotNull(mockedInstance);
        Assert.assertEquals(String.class, typeToMock.cast(mockedInstance));

        InvocationOnMock mockInvocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(mockHandler.handle(Mockito.any(), Mockito.any())).thenReturn(mockInvocation);
        Assert.assertEquals(mockHandler, byteBuddyMockMaker.getHandler(mockedInstance));
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockSerializableModeAcrossClassloaders() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        Type typeToMock = String.class;
        MockCreationSettings settings = MockMakerAnnotations.getSettings(Mockito.mock(Type.class));
        settings.setSerializable(SerializableMode.ACROSS_CLASSLOADERS);

        byteBuddyMockMaker.createMock(settings, mockHandler);
    }

    @Test(expected = ClassCastException.class)
    public void testCreateMockClassCastException() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        Type mockedType = Integer[].class;
        MockCreationSettings settings = MockMakerAnnotations.getSettings(Mockito.mock(Type.class));
        settings.setTypeToMock(mockedType);

        byteBuddyMockMaker.createMock(settings, mockHandler);
    }

    @Test(expected = InstantiationException.class)
    public void testCreateMockInstantiationException() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        Type typeToMock = Mockito.class;

        byteBuddyMockMaker.createMock(MockMakerAnnotations.getSettings(typeToMock), mockHandler);
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler1 = Mockito.mock(MockHandler.class);
        Object mockedInstance1 = byteBuddyMockMaker.createMock(MockMakerAnnotations.getSettings(String.class), mockHandler1);

        MockHandler mockHandler2 = Mockito.mock(MockHandler.class);
        byteBuddyMockMaker.resetMock(mockedInstance1, mockHandler2, MockMakerAnnotations.getSettings(String.class));

        Assert.assertEquals(mockHandler2, byteBuddyMockMaker.getHandler(mockedInstance1));
    }

    @Test
    public void testGetHandler() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        Object mockedInstance = byteBuddyMockMaker.createMock(MockMakerAnnotations.getSettings(String.class), mockHandler);

        Assert.assertEquals(mockHandler, byteBuddyMockMaker.getHandler(mockedInstance));
    }

    @Test
    public void testInitializeClassInstantiator() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Assert.assertNotNull(byteBuddyMockMaker.initializeClassInstantiator());
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiatorMissingObjenesis() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        byteBuddyMockMaker.initializeClassInstantiator();
    }

    @Test
    public void testAsInternalMockHandler() {
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        InternalMockHandler internalMockHandler = byteBuddyMockMaker.asInternalMockHandler(mockHandler);

        Assert.assertNotNull(internalMockHandler);
        Assert.assertEquals(mockHandler, internalMockHandler.getMockHandler());
    }
}