import org.mockito.ArgumentMatchers.*;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.CreationValidator;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.util.reflection.MethodInterceptor;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.reflection.FieldAccessible;
import org.mockito.internal.util.reflection.FieldSetter;

import java.lang.reflect.Method;
import java.util.Arrays;

@SuppressWarnings("unchecked")
public class MockUtilTest {
    private final CreationValidator creationValidator = mock(CreationValidator.class);
    private final LenientCopyTool lenientCopyTool = mock(LenientCopyTool.class);
    private final ClassImposterizer classImposterizer = mock(ClassImposterizer.class, new MockSettingsImpl().withExtraInterfaces(MockHandler.class));

    @Test
    public void testDefaultConstructor() {
        MockUtil mockUtil = new MockUtil();
        assertNotNull(mockUtil);
    }

    @Test
    public void testConstructorWithCreationValidator() {
        MockUtil mockUtil = new MockUtil(creationValidator);
        assertNotNull(mockUtil);
    }

    @Test
    public <T> void testCreateMockWithNullClass() {
        assertThrows(NullPointerException.class, () -> createMock(null, new MockSettingsImpl()));
    }

    @Test
    public <T> void testCreateMockWithValidClass() {
        MockUtil mockUtil = new MockUtil(new CreationValidator());
        Class<String> classToMock = String.class;
        T mock = createMock(classToMock, new MockSettingsImpl());
        assertInstanceOf(MockitoMock.class, mock);
    }

    @Test
    public <T> void testCreateMockWithExtraInterfaces() {
        MockUtil mockUtil = new MockUtil(new CreationValidator());
        Class<String> classToMock = String.class;
        Class<?>[] extraInterfaces = {MyInterface.class};
        MockSettingsImpl settings = new MockSettingsImpl().withExtraInterfaces(extraInterfaces);
        T mock = createMock(classToMock, settings);
        assertInstanceOf(MyInterface.class, mock);
    }

    @Test
    public <T> void testCreateMockWithNullSettings() {
        assertThrows(NullPointerException.class, () -> createMock(String.class, null));
    }

    @Test
    public <T> void testCreateMockWithInvalidClass() {
        MockUtil mockUtil = new MockUtil(new CreationValidator());
        assertThrows(IllegalArgumentException.class, () -> createMock(Object.class, new MockSettingsImpl()));
    }

    @Test
    public <T> void testCreateMockWithSpiedInstanceDifferentType() {
        MockUtil mockUtil = new MockUtil(new CreationValidator());
        Class<String> classToMock = String.class;
        Object spiedInstance = new Object();
        MockSettingsImpl settings = new MockSettingsImpl();
        assertThrows(IllegalArgumentException.class, () -> createMock(classToMock, settings, spiedInstance));
    }

    @Test
    public <T> void testResetMockWithNullMock() {
        assertThrows(NullPointerException.class, () -> resetMock(null));
    }

    @Test
    public <T> void testResetMock() {
        MockUtil mockUtil = new MockUtil(new CreationValidator());
        Class<String> classToMock = String.class;
        T mock = createMock(classToMock, new MockSettingsImpl());
        resetMock(mock);
        // Add assertions based on the class under test behavior
    }

    @Test
    public <T> void testGetMockHandlerWithNullMock() {
        assertThrows(NullPointerException.class, () -> getMockHandler(null));
    }

    @Test
    public <T> void testGetMockHandler() {
        MockUtil mockUtil = new MockUtil(new CreationValidator());
        Class<String> classToMock = String.class;
        T mock = createMock(classToMock, new MockSettingsImpl());
        MockHandlerInterface<T> mockHandler = getMockHandler(mock);
        assertNotNull(mockHandler);
    }

    @Test
    public <T> void testIsMockitoMockWithNullMock() {
        assertThrows(NullPointerException.class, () -> isMockitoMock(null));
    }

    @Test
    public <T> void testIsMockitoMockWithMock() {
        MockUtil mockUtil = new MockUtil(new CreationValidator());
        Class<String> classToMock = String.class;
        T mock = createMock(classToMock, new MockSettingsImpl());
        assertTrue(isMockitoMock(mock));
    }

    @Test
    public <T> void testIsMockitoMockWithNullInterceptor() {
        MockUtil mockUtil = new MockUtil(new CreationValidator());
        Object mock = mock(String.class);
        assertFalse(isMockitoMock(mock));
    }

    @Test
    public <T> void testIsMockitoMockWithNonMockitoInterceptor() {
        MockUtil mockUtil = new MockUtil(new CreationValidator());
        Object mock = createMock(String.class, new MockSettingsImpl());
        assertFalse(isMockitoMock(mock));
    }

    @Test
    public <T> void testGetInterceptorWithNullMock() {
        assertThrows(NullPointerException.class, () -> getInterceptor(null));
    }

    @Test
    public <T> void testGetInterceptor() {
        MockUtil mockUtil = new MockUtil(new CreationValidator());
        Class<String> classToMock = String.class;
        T mock = createMock(classToMock, new MockSettingsImpl());
        MethodInterceptorFilter interceptorFilter = getInterceptor(mock);
        assertNotNull(interceptorFilter);
    }

    @Test
    public <T> void testGetMockHandlerWithNullInterceptor() {
        assertThrows(NullPointerException.class, () -> getMockHandler((T) null));
    }

    @Test
    public <T> void testGetMockHandlerWithMock() {
        MockUtil mockUtil = new MockUtil(new CreationValidator());
        Class<String> classToMock = String.class;
        T mock = createMock(classToMock, new MockSettingsImpl());
        MockHandlerInterface<T> mockHandler = getMockHandler(mock);
        MockHandler<T> handler = (MockHandler<T>) getInterceptor(mock).getHandler();
        assertEquals(mockHandler, handler);
    }

    @Test
    public <T> void testGetMockNameWithNullMock() {
        assertThrows(NotAMockException.class, () -> getMockName(null));
    }

    @Test
    public <T> void testGetMockName() {
        MockUtil mockUtil = new MockUtil(new CreationValidator());
        Class<String> classToMock = String.class;
        T mock = createMock(classToMock, new MockSettingsImpl());
        MockName mockName = getMockName(mock);
        assertNotNull(mockName);
    }

    private <T> T createMock(Class<T> classToMock, MockSettingsImpl settings, Object spiedInstance) {
        try {
            return mockUtil.createMock(classToMock, settings, spiedInstance);
        } catch (NotAMockException e) {
            throw new RuntimeException(e);
        }
    }

    private void resetMock(T mock) {
        mockUtil.resetMock(mock);
    }

    private <T> MethodInterceptorFilter getInterceptor(T mock) {
        Factory factory = (Factory) mock;
        Callback callback = factory.getCallback(0);
        if (callback instanceof MethodInterceptorFilter) {
            return (MethodInterceptorFilter) callback;
        }
        return null;
    }

    private MockName getMockName(Object mock) {
        return mockUtil.getMockName(mock);
    }

    private boolean isMockitoMock(Object mock) {
        return mockUtil.isMockitoMock(mock);
    }
}