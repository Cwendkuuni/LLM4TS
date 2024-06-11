import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.*;
import org.mockito.internal.util.MockUtil;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockSettingsImpl settings = MockSettingsImpl.DEFAULT;
        Class<String> stringClass = String.class;
        String mock = new MockUtil().createMock(stringClass, settings);
        assertEquals(mock, mock);
    }

    @Test
    public void testCreateMockWithNullSettings() {
        Class<String> stringClass = String.class;
        assertThrows(NullPointerException.class, () -> new MockUtil().createMock(stringClass, null));
    }

    @Test
    public void testCreateMockWithNullClass() {
        MockSettingsImpl settings = MockSettingsImpl.DEFAULT;
        new MockUtil().createMock(null, settings);
    }

    @Test
    public void testCreateMockWithInterface() {
        MockSettingsImpl settings = MockSettingsImpl.DEFAULT;
        Class<?> interfaceClass = Mockito.class;
        assertThrows(UnsupportedOperationException.class, () -> new MockUtil().createMock(interfaceClass, settings));
    }

    @Test
    public void testCreateMockWithArray() {
        MockSettingsImpl settings = MockSettingsImpl.DEFAULT;
        Class<?> arrayClass = int[].class;
        assertThrows(UnsupportedOperationException.class, () -> new MockUtil().createMock(arrayClass, settings));
    }

    @Test
    public void testCreateMockWithPrimitiveType() {
        MockSettingsImpl settings = MockSettingsImpl.DEFAULT;
        Class<Integer> intClass = int.class;
        assertThrows(UnsupportedOperationException.class, () -> new MockUtil().createMock(intClass, settings));
    }

    @Test
    public void testCreateMockWithGenericType() {
        MockSettingsImpl settings = MockSettingsImpl.DEFAULT;
        Class<List<Integer>> listClass = List.class.getTypeParams()[0];
        assertThrows(UnsupportedOperationException.class, () -> new MockUtil().createMock(listClass, settings));
    }

    @Test
    public void testCreateMockWithAlreadyMockedObject() {
        Object mock = Mockito.mock(Object.class);
        MockHandlerInterface<Object> handler = new MockUtil().getMockHandler(mock);
        assertNotNull(handler);
        assertThrows(NotAMockException.class, () -> new MockUtil().createMock(mock.getClass(), new MockSettingsImpl()));
    }

    @Test
    public void testCreateMockWithNonMockObject() {
        Object nonMock = new Object();
        assertFalse(new MockUtil().isMock(nonMock));
        assertThrows(NotAMockException.class, () -> new MockUtil().createMock(nonMock.getClass(), new MockSettingsImpl()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateMockWithObjectThatCannotBeImposterized() {
        Class<Thread> threadClass = Thread.class;
        new MockUtil().createMock(threadClass, new MockSettingsImpl());
    }

    @Test
    public void testCreateMockWithSerializableClassAndExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<String> stringClass = String.class;
        Class<?>[] extraInterfaces = {Serializable.class, Cloneable.class};
        MockUtil mockUtil = new MockUtil(settings);
        String mock = mockUtil.createMock(stringClass, settings);
        assertNotNull(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithAlreadyInitializedMockName() {
        Object mock = Mockito.mock(Object.class);
        String mockName = new MockUtil().getMockName(mock);
        new MockUtil().getMockHandler(mock).getMockSettings().setMockName("AlreadyInitializedName");
        assertThrows(NotAMockException.class, () -> new MockUtil().createMock(mock.getClass(), new MockSettingsImpl()));
    }

    @Test
    public void testCreateMockWithNullSpiedInstance() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<String> stringClass = String.class;
        assertThrows(NullPointerException.class, () -> new MockUtil().createMock(stringClass, settings, null));
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithSpiedInstanceOfDifferentClass() {
        Object spiedInstance = new Object();
        Class<String> stringClass = String.class;
        assertThrows(NotAMockException.class, () -> new MockUtil().createMock(stringClass, new MockSettingsImpl(), spiedInstance));
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithSpiedInstanceThatIsNotSerializable() {
        Object spiedInstance = new Thread();
        Class<String> stringClass = String.class;
        assertThrows(NotAMockException.class, () -> new MockUtil().createMock(stringClass, new MockSettingsImpl(), spiedInstance));
    }

    @Test
    public void testResetMock() {
        MockSettingsImpl settings = MockSettingsImpl.DEFAULT;
        Object mock = Mockito.mock(Object.class, settings);
        new MockUtil().resetMock(mock);
        assertNotNull(mock);
    }

    @Test(expected = NullPointerException.class)
    public void testResetMockWithNullMock() {
        new MockUtil().resetMock(null);
    }

    @Test
    public void testNewMethodInterceptorFilter() {
        MockSettingsImpl settings = MockSettingsImpl.DEFAULT;
        MethodInterceptorFilter filter = new MockUtil().newMethodInterceptorFilter(settings);
        assertNotNull(filter);
    }

    @Test
    public void testGetMockHandler() {
        Object mock = Mockito.mock(Object.class);
        MockHandlerInterface<Object> handler = new MockUtil().getMockHandler(mock);
        assertNotNull(handler);
    }

    @Test(expected = NullPointerException.class)
    public void testIsMockitoMockWithNullMock() {
        new MockUtil().isMockitoMock(null);
    }

    @Test
    public void testIsMock() {
        Object mock = Mockito.mock(Object.class);
        assertTrue(new MockUtil().isMock(mock));
    }

    @Test(expected = NotAMockException.class)
    public void testIsMockWithNullMockOrNotAMockitoMock() {
        Object mock = new Object();
        assertFalse(new MockUtil().isMock(mock));
    }

    @Test
    public void testGetInterceptor() {
        Object mock = Mockito.mock(Object.class);
        MethodInterceptorFilter interceptorFilter = Mockito.mock(MethodInterceptorFilter.class);
        Mockito.doReturn(interceptorFilter).when(mock).getInterceptor();
        MethodInterceptorFilter interceptor = new MockUtil().getInterceptor(mock);
        assertEquals(interceptor, interceptorFilter);
    }

    @Test
    public void testGetMockName() {
        Object mock = Mockito.mock(Object.class);
        MockName mockName = new MockUtil().getMockName(mock);
        assertNotNull(mockName);
    }
}