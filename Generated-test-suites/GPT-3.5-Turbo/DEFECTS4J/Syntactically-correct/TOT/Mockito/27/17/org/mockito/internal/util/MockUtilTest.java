import org.junit.Test;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockHandlerInterface;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.MockUtil;

import java.io.Serializable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    @Test
    public void testCreateMock_typicalUseCase() {
        // Prepare
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(creationValidator);
        ClassToMock classToMock = mock(ClassToMock.class);
        MockSettingsImpl settings = new MockSettingsImpl();

        // Act
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, settings);

        // Assert
        assertNotNull(mock);
        verify(creationValidator).validateType(ClassToMock.class);
        verify(creationValidator).validateExtraInterfaces(ClassToMock.class, settings.getExtraInterfaces());
        verify(creationValidator).validateMockedType(ClassToMock.class, settings.getSpiedInstance());
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_nullMock() {
        // Prepare
        MockUtil mockUtil = new MockUtil();
        Object mock = null;

        // Act
        mockUtil.getMockHandler(mock);

        // Assert
        // NotAMockException should be thrown
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_nonMockitoMock() {
        // Prepare
        MockUtil mockUtil = new MockUtil();
        Object mock = new Object();

        // Act
        mockUtil.getMockHandler(mock);

        // Assert
        // NotAMockException should be thrown
    }

    @Test
    public void testGetInterceptor_nonFactoryMock() {
        // Prepare
        MockUtil mockUtil = new MockUtil();
        Object mock = new Object();

        // Act
        MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);

        // Assert
        assertNull(interceptor);
    }

    @Test
    public void testGetMockName_typicalUseCase() {
        // Prepare
        MockUtil mockUtil = new MockUtil();
        Object mock = mock(ClassToMock.class);

        // Act
        MockName mockName = mockUtil.getMockName(mock);

        // Assert
        assertNotNull(mockName);
    }

}

class ClassToMock {}