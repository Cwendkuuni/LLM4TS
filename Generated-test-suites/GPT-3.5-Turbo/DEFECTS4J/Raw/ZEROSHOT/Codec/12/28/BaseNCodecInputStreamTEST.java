import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

public class BaseNCodecInputStreamTest {

	private BaseNCodecInputStream baseNCodecInputStream;
	private BaseNCodec baseNCodecMock;
	private InputStream inputStreamMock;

	@Before
	public void setUp() {
		baseNCodecMock = mock(BaseNCodec.class);
		inputStreamMock = mock(InputStream.class);
		baseNCodecInputStream = new BaseNCodecInputStream(inputStreamMock, baseNCodecMock, true);
	}

	@Test
	public void testRead() throws IOException {
		when(baseNCodecMock.hasData()).thenReturn(false, false, true);
		when(inputStreamMock.read(any(byte[].class), anyInt(), anyInt())).thenReturn(1);
		when(baseNCodecMock.readResults(any(byte[].class), anyInt(), anyInt())).thenReturn(1);

		int result = baseNCodecInputStream.read();

		assertEquals(1, result);
		verify(inputStreamMock, times(2)).read(any(byte[].class), anyInt(), anyInt());
		verify(baseNCodecMock, times(1)).encode(any(byte[].class), anyInt(), anyInt());
	}

	@Test
	public void testReadReturnsEOF() throws IOException {
		when(baseNCodecMock.hasData()).thenReturn(false, false, true);
		when(inputStreamMock.read(any(byte[].class), anyInt(), anyInt())).thenReturn(-1);
		when(baseNCodecMock.readResults(any(byte[].class), anyInt(), anyInt())).thenReturn(-1);

		int result = baseNCodecInputStream.read();

		assertEquals(-1, result);
		verify(inputStreamMock, times(1)).read(any(byte[].class), anyInt(), anyInt());
		verify(baseNCodecMock, never()).encode(any(byte[].class), anyInt(), anyInt());
	}

	@Test
	public void testReadIOException() throws IOException {
		when(baseNCodecMock.hasData()).thenReturn(false, false, true);
		when(inputStreamMock.read(any(byte[].class), anyInt(), anyInt())).thenThrow(new IOException());

		int result = baseNCodecInputStream.read();

		assertEquals(BaseNCodec.EOF, result);
		verify(inputStreamMock, times(1)).read(any(byte[].class), anyInt(), anyInt());
		verify(baseNCodecMock, never()).encode(any(byte[].class), anyInt(), anyInt());
	}

	@Test
	public void testReadBytes() throws IOException {
		byte[] buffer = new byte[5];
		when(inputStreamMock.read(any(byte[].class), anyInt(), anyInt())).thenReturn(5);
		when(baseNCodecMock.readResults(any(byte[].class), anyInt(), anyInt())).thenReturn(5);

		int result = baseNCodecInputStream.read(buffer, 0, buffer.length);

		assertEquals(5, result);
		verify(inputStreamMock, times(1)).read(any(byte[].class), anyInt(), anyInt());
		verify(baseNCodecMock, never()).encode(any(byte[].class), anyInt(), anyInt());
	}

	@Test
	public void testReadBytesReturnsEOF() throws IOException {
		byte[] buffer = new byte[5];
		when(inputStreamMock.read(any(byte[].class), anyInt(), anyInt())).thenReturn(-1);
		when(baseNCodecMock.readResults(any(byte[].class), anyInt(), anyInt())).thenReturn(-1);

		int result = baseNCodecInputStream.read(buffer, 0, buffer.length);

		assertEquals(-1, result);
		verify(inputStreamMock, times(1)).read(any(byte[].class), anyInt(), anyInt());
		verify(baseNCodecMock, never()).encode(any(byte[].class), anyInt(), anyInt());
	}

	@Test
	public void testReadBytesIOException() throws IOException {
		byte[] buffer = new byte[5];
		when(inputStreamMock.read(any(byte[].class), anyInt(), anyInt())).thenThrow(new IOException());

		int result = baseNCodecInputStream.read(buffer, 0, buffer.length);

		assertEquals(BaseNCodec.EOF, result);
		verify(inputStreamMock, times(1)).read(any(byte[].class), anyInt(), anyInt());
		verify(baseNCodecMock, never()).encode(any(byte[].class), anyInt(), anyInt());
	}

	@Test
	public void testMarkSupported() {
		boolean result = baseNCodecInputStream.markSupported();
		assertFalse(result);
	}

	@Test
	public void testSkip() throws IOException {
		long skipAmount = 512L;
		doNothing().when(inputStreamMock).skip(skipAmount);

		baseNCodecInputStream.skip(skipAmount);

		verify(inputStreamMock, times(1)).skip(skipAmount);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSkipNegative() throws IOException {
		long skipAmount = -1L;

		baseNCodecInputStream.skip(skipAmount);
	}

	@Test
	public void testAvailable() throws IOException {
		int available = 1;
		when(baseNCodecMock.hasData()).thenReturn(true);

		int result = baseNCodecInputStream.available();

		assertEquals(available, result);
		verify(baseNCodecMock, times(1)).hasData();
	}

	@Test
	public void testAvailableEOF() throws IOException {
		int available = 0;
		when(baseNCodecMock.hasData()).thenReturn(false);

		int result = baseNCodecInputStream.available();

		assertEquals(available, result);
		verify(baseNCodecMock, times(1)).hasData();
	}

	@Test
	public void testClose() throws IOException {
		doNothing().when(inputStreamMock).close();
		doNothing().when(baseNCodecMock).eof();
		when(baseNCodecMock.hasData()).thenReturn(false);

		baseNCodecInputStream.close();

		verify(inputStreamMock, times(1)).close();
		verify(baseNCodecMock, times(1)).eof();
	}

}