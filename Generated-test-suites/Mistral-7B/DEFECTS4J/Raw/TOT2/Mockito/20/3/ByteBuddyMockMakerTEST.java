import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockHandler;
import org.mockito.invocation.MockHandlerImpl;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.bytebuddy.MockCreationSettings;
import org.mockito.internal.creation.bytebuddy.MockMethodInterceptor;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        MockHandler handler = new MockHandlerImpl();
        MockCreationSettings settings = new MockCreationSettings<Object>() {};
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = byteBuddyMockMaker.createMock(settings, handler);
        verify(handler).handle(Mockito.any());
    }

    @Test
    public void testGetHandler() {
        Object mock = Mockito.mock(Object.class);
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler handler = byteBuddyMockMaker.getHandler(mock);
        assertNotNull(handler);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableModeACROSS_CLASSLOADERS() {
        MockHandler handler = new MockHandlerImpl();
        MockCreationSettings settings = new MockCreationSettings<Object>() {
            {
                setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
            }
        } ;
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        byteBuddyMockMaker.createMock(settings, handler);
    }

    @Test(expected = InstantiationException.class)
    public void testCreateMockWithInstantiationException() {
        MockHandler handler = new MockHandlerImpl();
        MockCreationSettings settings = new MockCreationSettings<Object>() {};
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        try {
            byteBuddyMockMaker.createMock(settings, handler);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            // pass
        }
    }
}

//