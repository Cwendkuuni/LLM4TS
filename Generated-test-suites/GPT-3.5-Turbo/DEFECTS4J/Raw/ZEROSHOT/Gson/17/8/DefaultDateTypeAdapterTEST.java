import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DefaultDateTypeAdapterTest {

    @Test
    public void testWrite() throws Exception {
        JsonWriter mockedJsonWriter = mock(JsonWriter.class);
        Date date = new Date();
        
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        adapter.write(mockedJsonWriter, date);

        verify(mockedJsonWriter).value(anyString());
    }

    @Test
    public void testRead() throws Exception {
        JsonReader mockedJsonReader = mock(JsonReader.class);
        when(mockedJsonReader.peek()).thenReturn(JsonToken.STRING);
        when(mockedJsonReader.nextString()).thenReturn("2020-01-01T10:00:00");
        
        DefaultDateTypeAdapter adapter = new DefaultDateTypeAdapter(Date.class);
        Date result = adapter.read(mockedJsonReader);

        assertEquals(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse("2020-01-01T10:00:00"), result);
    }
}