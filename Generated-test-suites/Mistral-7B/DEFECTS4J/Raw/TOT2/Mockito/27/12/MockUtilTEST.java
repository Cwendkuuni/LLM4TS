
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.internal.util.MockUtil;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.eq;

public class MockUtilTest {

    private MockUtil mockUtil;
    private MockSettingsImpl settings;
    private Class<?> mockClass;
    private Object spiedInstance;
    @Captor private ArgumentCaptor<Class<?>> classCaptor;
    @Captor private ArgumentCaptor<MockSettingsImpl> settingsCaptor;

    @Before
    public void setup() {
        mockUtil = new MockUtil();
        settings = new MockSettingsImpl();
        mockClass = mock(String.class);
        spiedInstance = new Object();
    }

    @Test
    public void testCreateMockWithValidArguments() {
        mockUtil.createMock(mockClass, settings);
        verify(settings).validateType(mockClass);
        verify(settings).validateExtraInterfaces(mockClass, settings.getExtraInterfaces());
        verify(settings).validateMockedType(mockClass, spiedInstance);
        verify(settings).initiateMockName(mockClass);
        verify(settings).setMockName(Mockito.eq(settings), Mockito.eq("MockString"));
        verify(settings).setSerializable(Mockito.eq(settings), eq(true));
        verify(settings).setAncillaryTypes(Mockito.eq(settings), anyArray());
        verify(settings).setSpiedInstance(Mockito.eq(settings), spiedInstance);
        verify(ClassImposterizer.class).imposterise(any(MethodInterceptorFilter.class), classCaptor.capture(), anyArray());
        MockHandlerInterface<String> mockHandler = mockUtil.getMockHandler(Mockito.capture(MockHandler.class));
        verify(mockHandler).handle(Mockito.any());
        verifyNoMoreInteractions(mockHandler);
        verify(mockHandler).getMockSettings().getMockName().equals("MockString");
        String mock = classCaptor.getValue();
        assertThat(mock).isInstanceOf(String.class);
    }

    @Test
    public void testCreateMockWithInterface() {
        settings.setExtraInterfaces(new Class<?>[]{String.class});
        mockUtil.createMock(String.class, settings);
        verify(settings).validateType(String.class);
        verify(settings).validateExtraInterfaces(String.class, new Class<?>[]{String.class});
        verify(settings).validateMockedType(String.class, spiedInstance);
        verify(settings).initiateMockName(String.class);
        verify(settings).setMockName(Mockito.eq(settings), Mockito.eq("MockString"));
        verify(settings).setSerializable(Mockito.eq(settings), eq(true));
        verify(settings).setAncillaryTypes(Mockito.eq(settings), new Class<?>[]{Serializable.class, String.class});
        verify(settings).setSpiedInstance(Mockito.eq(settings), spiedInstance);
        verify(ClassImposterizer.class).imposterise(any(MethodInterceptorFilter.class), eq(String.class), anyArray());
        MockHandlerInterface<String> mockHandler = mockUtil.getMockHandler(Mockito.capture(MockHandler.class));
        verify(mockHandler).handle(Mockito.any());
        verifyNoMoreInteractions(mockHandler);
        verify(mockHandler).getMockSettings().getMockName().equals("MockString");
        String mock = Mockito.capture(String.class);
        verify(mockHandler).getMockSettings().getMockName().equals("MockString");
        assertThat(mock).isInstanceOf(String.class);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithInvalidClass() {
        mockUtil.createMock(Object.class, settings);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithNullClass() {
        mockUtil.createMock(null, settings);
    }

    @Test
    public void testResetMock() {
        String mock = mockUtil.createMock(mockClass, settings);
        mockUtil.resetMock(mock);
        MockHandlerInterface<String> oldMockHandler = mockUtil.getMockHandler(mock);
        MockHandler<String> newMockHandler = new MockHandler<String>(oldMockHandler);
        MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, settings);
        verify(mock).setCallback(0, newFilter);
        MockHandlerInterface<String> newMockHandler2 = mockUtil.getMockHandler(mock);
        verify(newMockHandler2).handle(Mockito.any());
        verifyNoMoreInteractions(oldMockHandler);
        verifyNoMoreInteractions(newMockHandler);
        verifyNoMoreInteractions(newFilter);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithNullMock() {
        mockUtil.createMock(null, settings);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonFactory() {
        Object nonFactory = new Object();
        mockUtil.getMockHandler(nonFactory);
    }

    @Test
    public void testIsMock() {
        Object mock = mockUtil.createMock(mockClass, settings);
        String mockName = mockUtil.getMockName(mock);
        Object nonMock = new Object();
        Object nonMock2 = mockUtil.createMock(mockClass, settings);
        Object factory = Mockito.mock(Factory.class);
        Object factoryMock = mockUtil.createMock(Factory.class, new MockSettingsImpl());
        assertThat(mockUtil.isMock(mock)).isTrue();
        assertThat(mockUtil.isMock(mockName)).isTrue();
        assertThat(mockUtil.isMock(factory)).isFalse();
        assertThat(mockUtil.isMock(factoryMock)).isTrue();
        assertThat(mockUtil.isMock(nonMock)).isFalse();
        assertThat(mockUtil.isMock(nonMock2)).isTrue();
    }

    @Test
    public void testGetInterceptor() {
        Object mock = mockUtil.createMock(mockClass, settings);
        Factory factory = Mockito.mock(Factory.class);
        Callback callback = factory.getCallback(0);
        Object interceptor = mockUtil.getInterceptor(mock);
        Object interceptor2 = mockUtil.getInterceptor(factory);
        assertThat(interceptor).isInstanceOf(MethodInterceptorFilter.class);
        assertThat(interceptor2).isNull();
    }

    @Test
    public void testGetMockName() {
        String mockName = mockUtil.getMockName(mockUtil.createMock(mockClass, settings));
        verify(settings).initiateMockName(mockClass);
        verify(settings).setMockName(Mockito.eq(settings), Mockito.eq("MockString"));
        assertThat(mockName).isEqualTo("MockString");
    }
}