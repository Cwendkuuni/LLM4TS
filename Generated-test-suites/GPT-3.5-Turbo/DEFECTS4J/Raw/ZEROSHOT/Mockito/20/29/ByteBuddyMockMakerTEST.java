import org.junit.*;
import org.mockito.*;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.*;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;

    @Before
    public void setUp() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableModeAcrossClassloaders() {
        MockCreationSettings<Object> settings = Mockito.mock(MockCreationSettings.class);
        Mockito.when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);

        byteBuddyMockMaker.createMock(settings, Mockito.mock(MockHandler.class));
    }

    @Test
    public void testCreateMockWithValidSettings() {
        MockCreationSettings<Object> settings = Mockito.mock(MockCreationSettings.class);
        Mockito.when(settings.getSerializableMode()).thenReturn(SerializableMode.NONE);
        Mockito.when(settings.getTypeToMock()).thenReturn(Object.class);
        Mockito.when(settings.getExtraInterfaces()).thenReturn(new Class[0]);

        Object mock = byteBuddyMockMaker.createMock(settings, Mockito.mock(MockHandler.class));

        Assert.assertEquals(Object.class, mock.getClass().getSuperclass());
        Assert.assertTrue(mock instanceof InternalMockHandler.MockAccess);
        Assert.assertNotNull(((InternalMockHandler.MockAccess) mock).getMockitoInterceptor());
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithClassCastException() throws Exception {
        ClassInstantiator classInstantiator = Mockito.mock(ClassInstantiator.class);
        Mockito.when(classInstantiator.instantiate(Mockito.any())).thenThrow(ClassCastException.class);

        GlobalConfiguration globalConfiguration = Mockito.mock(GlobalConfiguration.class);
        Mockito.when(globalConfiguration.enableClassCache()).thenReturn(true);

        Mockito.whenNew(ClassInstantiator.UsingObjenesis.class).withArguments(Mockito.anyBoolean()).thenReturn(Mockito.mock(ClassInstantiator.UsingObjenesis.class));

        ByteBuddyMockMaker byteBuddyMockMaker = Mockito.spy(new ByteBuddyMockMaker());
        Mockito.doReturn(classInstantiator).when(byteBuddyMockMaker).initializeClassInstantiator();

        MockCreationSettings<Object> settings = Mockito.mock(MockCreationSettings.class);
        Mockito.when(settings.getTypeToMock()).thenReturn(Object.class);
        Mockito.when(byteBuddyMockMaker.describeClass(Mockito.any())).thenReturn("mockedProxyType");
        Mockito.when(byteBuddyMockMaker.describeClass(settings.getTypeToMock())).thenReturn("typeToMock");
        Mockito.when(byteBuddyMockMaker.describeClass(Mockito.anyObject())).thenReturn("mockInstance");

        byteBuddyMockMaker.createMock(settings, Mockito.mock(MockHandler.class));
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithMockitoInstantiationException() throws Exception {
        ClassInstantiator classInstantiator = Mockito.mock(ClassInstantiator.class);
        Mockito.when(classInstantiator.instantiate(Mockito.any())).thenThrow(org.mockito.internal.creation.instance.InstantiationException.class);

        GlobalConfiguration globalConfiguration = Mockito.mock(GlobalConfiguration.class);
        Mockito.when(globalConfiguration.enableClassCache()).thenReturn(true);

        Mockito.whenNew(ClassInstantiator.UsingObjenesis.class).withArguments(Mockito.anyBoolean()).thenReturn(Mockito.mock(ClassInstantiator.UsingObjenesis.class));

        ByteBuddyMockMaker byteBuddyMockMaker = Mockito.spy(new ByteBuddyMockMaker());
        Mockito.doReturn(classInstantiator).when(byteBuddyMockMaker).initializeClassInstantiator();

        MockCreationSettings<Object> settings = Mockito.mock(MockCreationSettings.class);
        Mockito.when(settings.getTypeToMock()).thenReturn(Object.class);
        Mockito.when(settings.getTypeToMock().getSuperclass()).thenReturn(Object.class);
        Mockito.when(byteBuddyMockMaker.describeClass(Mockito.any())).thenReturn("mockedProxyType");

        byteBuddyMockMaker.createMock(settings, Mockito.mock(MockHandler.class));
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        MockCreationSettings<Object> settings = Mockito.mock(MockCreationSettings.class);
        Mockito.when(settings.getTypeToMock()).thenReturn(Object.class);

        Object mock = new Object();
        Object result = byteBuddyMockMaker.ensureMockIsAssignableToMockedType(settings, mock);

        Assert.assertEquals(Object.class, result.getClass());
        Assert.assertEquals(mock, result);
    }

    @Test
    public void testDescribeClassWithTypeNull() {
        Class type = null;
        String result = byteBuddyMockMaker.describeClass(type);

        Assert.assertEquals("null", result);
    }

    @Test
    public void testDescribeClassWithTypeNotNull() {
        Class type = Object.class;
        String result = byteBuddyMockMaker.describeClass(type);

        Assert.assertEquals("'java.lang.Object', loaded by classloader : 'null'", result);
    }

    @Test
    public void testDescribeClassWithInstanceNull() {
        Object instance = null;
        String result = byteBuddyMockMaker.describeClass(instance);

        Assert.assertEquals("null", result);
    }

    @Test
    public void testDescribeClassWithInstanceNotNull() {
        Object instance = new Object();
        String result = byteBuddyMockMaker.describeClass(instance);

        Assert.assertEquals("'java.lang.Object', loaded by classloader : 'null'", result);
    }

    @Test
    public void testGetHandlerWithMockNotInstanceOfMockMethodInterceptorMockAccess() {
        Object mock = new Object();
        MockHandler result = byteBuddyMockMaker.getHandler(mock);

        Assert.assertNull(result);
    }

    @Test
    public void testGetHandlerWithMockInstanceOfMockMethodInterceptorMockAccess() {
        MockMethodInterceptor.MockAccess mockAccess = Mockito.mock(MockMethodInterceptor.MockAccess.class);
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        Mockito.when(mockAccess.getMockitoInterceptor()).thenReturn(Mockito.mock(MockMethodInterceptor.class));
        Mockito.when(mockAccess.getMockitoInterceptor().getMockHandler()).thenReturn(mockHandler);

        Object mock = mockAccess;
        MockHandler result = byteBuddyMockMaker.getHandler(mock);

        Assert.assertEquals(mockHandler, result);
    }

    @Test
    public void testResetMock() {
        MockMethodInterceptor.MockAccess mockAccess = Mockito.mock(MockMethodInterceptor.MockAccess.class);
        MockMethodInterceptor mockitoInterceptor = Mockito.mock(MockMethodInterceptor.class);
        Mockito.when(mockAccess.getMockitoInterceptor()).thenReturn(mockitoInterceptor);

        Object mock = mockAccess;
        MockHandler newHandler = Mockito.mock(MockHandler.class);
        MockCreationSettings settings = Mockito.mock(MockCreationSettings.class);

        byteBuddyMockMaker.resetMock(mock, newHandler, settings);

        Mockito.verify(mockAccess).setMockitoInterceptor(mockitoInterceptor);
    }
}