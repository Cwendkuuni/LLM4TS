
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.util.CreationValidator;
import org.mockito.internal.util.MockHandler;
import org.mockito.internal.util.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;

public class MockUtilTest {
    @Test
    public void testCreateMock() {
        MockUtil util = new MockUtil(new CreationValidator());
        
        String testString = "test";
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.setExtraInterfaces(new Class<?>[]{Comparable.class});

        try {
            util.createMock(null, mockSettings);
            fail("Should throw IllegalArgumentException for null classToMock");
        } catch (IllegalArgumentException e) {
            assertEquals("classToMock cannot be null", e.getMessage());
        }
        
        try {
            util.createMock(String.class, null);
            fail("Should throw IllegalArgumentException for null settings");
        } catch (IllegalArgumentException e) {
            assertEquals("settings cannot be null", e.getMessage());
        }

        try {
            util.createMock(String.class, mockSettings);
            fail("Should throw IllegalArgumentException for invalid classToMock");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid classToMock: class java.lang.String", e.getMessage());
        }

        mockSettings.setSpiedInstance(testString);
        try {
            util.createMock(String.class, mockSettings);
            fail("Should throw IllegalArgumentException for invalid spiedInstance");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid spiedInstance: test", e.getMessage());
        }
    }

    @Test
    public void testResetMock() {
        MockUtil util = new MockUtil(new CreationValidator());
        
        try {
            util.resetMock(null);
            fail("Should throw NotAMockException for null mock");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }

        String testString = "test";
        MockHandlerInterface<String> mockHandler = new MockHandler<>(null);
        Factory factory = new Factory() {
            public void setCallback(int index, Callback callback) {}
            public Callback getCallback(int index) { return null; }
        };
        factory.setCallback(0, new MethodInterceptorFilter(mockHandler, null));

        try {
            util.resetMock(factory);
        } catch (Exception e) {
            fail("Should not throw exception for valid mock");
        }

        Factory invalidFactory = new Factory() {
            public void setCallback(int index, Callback callback) {}
            public Callback getCallback(int index) { return null; }
        };

        try {
            util.resetMock(invalidFactory);
            fail("Should throw NotAMockException for invalid mock");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is: class org.mockito.internal.util.MockUtilTest$5", e.getMessage());
        }
    }

    @Test
    public void testGetMockHandler() {
        MockUtil util = new MockUtil(new CreationValidator());
        
        try {
            util.getMockHandler(null);
            fail("Should throw NotAMockException for null mock");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }

        String testString = "test";
        MockHandlerInterface<String> mockHandler = new MockHandler<>(null);
        Factory factory = new Factory() {
            public void setCallback(int index, Callback callback) {}
            public Callback getCallback(int index) { return null; }
        };
        factory.setCallback(0, new MethodInterceptorFilter(mockHandler, null));

        try {
            util.getMockHandler(factory);
        } catch (Exception e) {
            fail("Should not throw exception for valid mock");
        }

        Factory invalidFactory = new Factory() {
            public void setCallback(int index, Callback callback) {}
            public Callback getCallback(int index) { return null; }
        };

        try {
            util.getMockHandler(invalidFactory);
            fail("Should throw NotAMockException for invalid mock");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is: class org.mockito.internal.util.MockUtilTest$8", e.getMessage());
        }
    }

    @Test
    public void testIsMock() {
        MockUtil util = new MockUtil(new CreationValidator());
        
        assertFalse(util.isMock(null));

        String testString = "test";
        MockHandlerInterface<String> mockHandler = new MockHandler<>(null);
        Factory factory = new Factory() {
            public void setCallback(int index, Callback callback) {}
            public Callback getCallback(int index) { return null; }
        };
        factory.setCallback(0, new MethodInterceptorFilter(mockHandler, null));

        assertTrue(util.isMock(factory));

        Factory invalidFactory = new Factory() {
            public void setCallback(int index, Callback callback) {}
            public Callback getCallback(int index) { return null; }
        };

        assertFalse(util.isMock(invalidFactory));
    }

    @Test
    public void testGetMockName() {
        MockUtil util = new MockUtil(new CreationValidator());
        
        try {
            util.getMockName(null);
            fail("Should throw NotAMockException for null mock");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }

        String testString = "test";
        MockHandlerInterface<String> mockHandler = new MockHandler<>(null);
        Factory factory = new Factory() {
            public void setCallback(int index, Callback callback) {}
            public Callback getCallback(int index) { return null; }
        };
        factory.setCallback(0, new MethodInterceptorFilter(mockHandler, null));

        try {
            util.getMockName(factory);
        } catch (Exception e) {
            fail("Should not throw exception for valid mock");
        }

        Factory invalidFactory = new Factory() {
            public void setCallback(int index, Callback callback) {}
            public Callback getCallback(int index) { return null; }
        };

        try {
            util.getMockName(invalidFactory);
            fail("Should throw NotAMockException for invalid mock");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is: class org.mockito.internal.util.MockUtilTest$11", e.getMessage());
        }
    }
}
