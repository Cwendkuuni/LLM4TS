import org.apache.commons.compress.archivers.sevenz.Coders;
import org.apache.commons.compress.archivers.sevenz.SevenZMethod;
import org.junit.Test;
import org.junit.jupitermock.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CodersTest {
    @Test
    public void testAddDecoder() throws Exception {
        InputStream inputStreamMock = mock(InputStream.class);
        Coder coderMock = mock(Coder.class);
        byte[] password = new byte[16];
        when(coderMock.decompressionMethodId).thenCallRealMethod();

        InputStream outputStream = Coders.addDecoder(inputStreamMock, coderMock, password);
        verify(coderMock).decompressionMethodId;
        verifyNoMoreInteractions(coderMock);
        assertNotNull(outputStream);

        // Test for unsupported compression method
        Coder unsupportedCoder = mock(Coder.class);
        when(unsupportedCoder.decompressionMethodId).thenReturn(new byte[]{0x01, 0x02, 0x03});
        try {
            Coders.addDecoder(inputStreamMock, unsupportedCoder, password);
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("Unsupported compression method [0x01, 0x02, 0x03]", e.getMessage());
        }
    }

    @Test
    public void testAddEncoder() throws Exception {
        OutputStream outputStreamMock = mock(OutputStream.class);
        SevenZMethod method = SevenZMethod.DEFLATE;
        byte[] password = new byte[16];
        Coder decoderMock = mock(Coder.class);

        OutputStream output = Coders.addEncoder(outputStreamMock, method, password);
        verify(decoderMock).encode(outputStreamMock, password);
        verifyNoMoreInteractions(decoderMock);
        assertNotNull(output);

        // Test for unsupported compression method
        SevenZMethod unsupportedMethod = new SevenZMethod("UNSUPPORTED");
        try {
            Coders.addEncoder(outputStreamMock, unsupportedMethod, password);
            fail("Expected IOException");
        } catch (IOException e) {
            assertEquals("Unsupported compression method UNSUPPORTED", e.getMessage());
        }
    }
}