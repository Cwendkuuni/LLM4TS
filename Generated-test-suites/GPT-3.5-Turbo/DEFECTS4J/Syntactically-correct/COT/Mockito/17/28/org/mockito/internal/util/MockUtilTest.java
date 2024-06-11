
import org.junit.Test;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockHandler;
import org.mockito.internal.util.MockHandlerInterface;
import org.mockito.internal.util.reflection.LenientCopyTool;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    @Test
    public void testCreateMock() throws Exception {
        MockUtil mockUtil = new MockUtil();

        MockSettingsImpl mockSettings = new MockSettingsImpl();

        MyClass mock = mockUtil.createMock(MyClass.class, mockSettings);

        assertThat(mock).isNotNull();
    }

    @Test(expected = NotAMockException.class)
    public void testResetMockWithNullArgument() throws Exception {
        MockUtil mockUtil = new MockUtil();
        mockUtil.resetMock(null);
    }

    @Test
    public void testGetMockHandler() throws Exception {
        MockUtil mockUtil = new MockUtil();

        MyClass mock = mock(MyClass.class);
        MockHandlerInterface<MyClass> mockHandler = mockUtil.getMockHandler(mock);

        assertThat(mockHandler).isNotNull();
    }

    @Test
    public void testIsMockReturnsTrueWhenObjectIsMock() throws Exception {
        MockUtil mockUtil = new MockUtil();

        MyClass mock = mock(MyClass.class);
        boolean isMock = mockUtil.isMock(mock);

        assertThat(isMock).isTrue();
    }

    @Test
    public void testIsMockReturnsFalseWhenObjectIsNotAMock() throws Exception {
        MockUtil mockUtil = new MockUtil();

        Object object = new Object();
        boolean isMock = mockUtil.isMock(object);

        assertThat(isMock).isFalse();
    }

    @Test
    public void testGetMockName() throws Exception {
        MockUtil mockUtil = new MockUtil();

        MyClass mock = mock(MyClass.class);
        MockName mockName = mockUtil.getMockName(mock);

        assertThat(mockName).isNotNull();
    }
}
