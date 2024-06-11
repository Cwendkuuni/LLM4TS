import org.junit.Test;
import org.mockito.MockHandler;
import org.mockito.Mockito;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.plugins.MockMaker;
import org.mockito.MockCreationSettings;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        MockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        Class<String> typeToMock = String.class;

        MockCreationSettings<String> settings = new MockCreationSettings<String>()
                .serializable();

        String mockedProxy = mockMaker.createMock(settings, mockHandler);

        Mockito.verify(mockHandler).handle(Mockito.isInstance(mockedProxy));
    }
}