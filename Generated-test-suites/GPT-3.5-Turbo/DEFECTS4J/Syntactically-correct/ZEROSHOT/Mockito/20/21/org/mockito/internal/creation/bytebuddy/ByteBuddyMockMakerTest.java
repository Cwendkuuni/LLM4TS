import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

import static org.mockito.Mockito.*;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<TestClass> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);

        try {
            mockMaker.createMock(settings, handler);
        } catch (MockitoException e) {
            Assert.assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
      ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
      MockCreationSettings<TestClass> settings = mock(MockCreationSettings.class);
      TestClass mock = mock(TestClass.class);

      try {
          mockMaker.ensureMockIsAssignableToMockedType(settings, mock);
      } catch (ClassCastException e) {
          Assert.fail("ClassCastException should not be thrown");
      }
    }

    @Test
    public void testDescribeClass() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        Assert.assertEquals("null", mockMaker.describeClass(null));

        Class<?> type = TestClass.class;
        String expected = "'" + type.getCanonicalName() + "', loaded by classloader : '" + type.getClassLoader() + "'";
        Assert.assertEquals(expected, mockMaker.describeClass(type));

        Object instance = new TestClass();
        type = instance.getClass();
        expected = "'" + type.getCanonicalName() + "', loaded by classloader : '" + type.getClassLoader() + "'";
        Assert.assertEquals(expected, mockMaker.describeClass(instance));
    }

    @Test
    public void testGetHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockMethodInterceptor.MockAccess mock = mock(MockMethodInterceptor.MockAccess.class);
        MockHandler handler = mock(MockHandler.class);
        when(mock.getMockitoInterceptor()).thenReturn(handler);

        Assert.assertEquals(handler, mockMaker.getHandler(mock));
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockMethodInterceptor.MockAccess mock = mock(MockMethodInterceptor.MockAccess.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);

        mockMaker.resetMock(mock, newHandler, settings);
        verify(mock).setMockitoInterceptor(any(MockMethodInterceptor.class));
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = mock(MockHandler.class);

        mockMaker.asInternalMockHandler(handler);
    }

    private static class TestClass {

    }
}