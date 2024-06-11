import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class SetterlessPropertyTest {

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = mock(Object.class);

        SetterlessProperty setterlessProperty = new SetterlessProperty(null, null, null, null, null);

        setterlessProperty.deserializeAndSet(parser, context, instance);

        verify(instance, times(1)).getClass();
        verify(parser, times(1)).getCurrentToken();
        verifyNoMoreInteractions(parser);
        verifyNoMoreInteractions(context);
    }
}